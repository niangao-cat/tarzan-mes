package tarzan.method.infra.repository.impl;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.*;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.domian.Language;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtBomComponentIface;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO5;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 装配清单头 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomRepositoryImpl extends BaseRepositoryImpl<MtBom> implements MtBomRepository {

    private static final String ALL = "ALL";
    private static final List<String> UPDATE_METHOD = Arrays.asList("ALL", "UPDATE", "");
    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomMapper mtBomMapper;

    @Autowired
    private MtBomComponentMapper mtBomComponentMapper;

    @Autowired
    private MtBomReferencePointMapper mtBomReferencePointMapper;

    @Autowired
    private MtBomSubstituteGroupMapper mtBomSubstituteGroupMapper;

    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtBomHisRepository mtBomHisRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtBomSiteAssignRepository mtBomSiteAssignRepository;

    @Autowired
    private MtBomSiteAssignMapper mtBomSiteAssignMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;

    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Override
    public MtBomVO7 bomBasicGet(Long tenantId, String bomId) {
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomBasicGet】"));
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(bomId);
        mtBom = this.mtBomMapper.selectOne(mtBom);

        if (mtBom != null) {
            MtBomVO7 bomVO7 = new MtBomVO7();
            BeanUtils.copyProperties(mtBom, bomVO7);
            bomVO7.setSiteId(mtBomSiteAssignRepository.bomLimitEnableSiteQuery(tenantId, bomId));
            return bomVO7;
        }
        return null;
    }

    @Override
    public List<String> propertyLimitBomQuery(Long tenantId, MtBomVO condition) {
        if (ObjectFieldsHelper.isAllFieldNull(condition)) {
            throw new MtException("MT_BOM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0002", "BOM", "【API:propertyLimitBomQuery】"));
        }

        List<MtBom> mtBoms = this.mtBomMapper.selectBoms(tenantId, condition);
        if (CollectionUtils.isEmpty(mtBoms)) {
            return Collections.emptyList();
        }

        return mtBoms.stream().map(MtBom::getBomId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bomCopy(Long tenantId, MtBomVO2 condition) {
        if (StringUtils.isEmpty(condition.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomCopy】"));
        }
        if (StringUtils.isEmpty(condition.getBomName())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomName", "【API:bomCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRevision())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "revision", "【API:bomCopy】"));
        }

        if (StringUtils.isEmpty(condition.getBomType())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomType", "【API:bomCopy】"));
        }

        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_BOM_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0003", "BOM", "【API:bomCopy】"));
        }
        if (!mtGenTypes.stream().anyMatch(t -> t.getTypeCode().equals(condition.getBomType()))) {
            throw new MtException("MT_BOM_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0003", "BOM", "【API:bomCopy】"));
        }

        MtBom oldBom = new MtBom();
        oldBom.setTenantId(tenantId);
        oldBom.setBomId(condition.getBomId());
        oldBom = this.mtBomMapper.selectOne(oldBom);
        if (oldBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomCopy】"));
        }

        MtBom dulBom = new MtBom();
        dulBom.setTenantId(tenantId);
        dulBom.setBomName(condition.getBomName());
        dulBom.setRevision(condition.getRevision());
        dulBom.setBomType(condition.getBomType());
        List<MtBom> dulBoms = this.mtBomMapper.select(dulBom);
        if (CollectionUtils.isNotEmpty(dulBoms)) {
            throw new MtException("MT_BOM_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0005", "BOM", "【API:bomCopy】"));
        }

        List<String> sqlList = new ArrayList<String>();
        Map<String, Map<String, String>> bomTls = null;
        List<MtBomVO8> bomtl = this.mtBomMapper.selectBomTL(tenantId, condition.getBomId());
        if (CollectionUtils.isNotEmpty(bomtl)) {
            bomTls = new HashMap<String, Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            for (MtBomVO8 tl : bomtl) {
                map.put(tl.getLang(), tl.getDescription());
            }
            bomTls.put("description", map);
        }

        String newBomId = this.customSequence.getNextKey("mt_bom_s");
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 保存BOM头
        Date now = new Date(System.currentTimeMillis());
        MtBom newBom = new MtBom();
        newBom.setTenantId(tenantId);
        newBom.setBomId(newBomId);
        newBom.setBomName(condition.getBomName());
        newBom.setRevision(condition.getRevision());
        newBom.setBomType(condition.getBomType());
        newBom.setCurrentFlag("N");
        newBom.setDateFrom(oldBom.getDateFrom());
        newBom.setDateTo(oldBom.getDateTo());
        newBom.setDescription(oldBom.getDescription());
        newBom.setBomStatus("NEW");
        newBom.setCopiedFromBomId(oldBom.getBomId());
        newBom.setReleasedFlag("N");
        newBom.setPrimaryQty(oldBom.getPrimaryQty());
        newBom.setAutoRevisionFlag(oldBom.getAutoRevisionFlag());
        newBom.setCreatedBy(userId);
        newBom.setCreationDate(now);
        newBom.setLastUpdatedBy(userId);
        newBom.setLastUpdateDate(now);
        newBom.setObjectVersionNumber(1L);
        newBom.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_cid_s")));
        newBom.set_tls(bomTls);
        newBom.setAutoRevisionFlag(oldBom.getAutoRevisionFlag());
        newBom.setAssembleAsMaterialFlag(oldBom.getAssembleAsMaterialFlag());
        sqlList.addAll(MtSqlHelper.getInsertSql(newBom));

        /* 新增逻辑BOM_ATTR复制 */
        List<MtExtendAttrVO3> bomAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId, "mt_bom_attr",
                "BOM_ID", Arrays.asList(oldBom.getBomId()));
        if (CollectionUtils.isNotEmpty(bomAttrs)) {
            for (MtExtendAttrVO3 bomAttr : bomAttrs) {
                sqlList.add(this.mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_bom_attr", "BOM_ID",
                        newBomId, bomAttr.getAttrName(), bomAttr.getAttrValue(), bomAttr.getLang(), now, userId));
            }
        }

        /* 新增逻辑 */
        List<String> siteIds = condition.getSiteId();
        if (CollectionUtils.isEmpty(siteIds)) {
            MtBomSiteAssign tmp = new MtBomSiteAssign();
            tmp.setTenantId(tenantId);
            tmp.setBomId(oldBom.getBomId());
            List<MtBomSiteAssign> oldAssigns = mtBomSiteAssignMapper.select(tmp);

            for (MtBomSiteAssign oldAssign : oldAssigns) {
                oldAssign.setTenantId(tenantId);
                oldAssign.setAssignId(this.customSequence.getNextKey("mt_bom_site_assign_s"));
                oldAssign.setBomId(newBomId);
                oldAssign.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_site_assign_cid_s")));
                oldAssign.setCreatedBy(userId);
                oldAssign.setCreationDate(now);
                oldAssign.setLastUpdatedBy(userId);
                oldAssign.setLastUpdateDate(now);
                oldAssign.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(oldAssign));
            }
        } else {
            for (String siteId : siteIds) {
                MtBomSiteAssign newAssign = new MtBomSiteAssign();
                newAssign.setTenantId(tenantId);
                newAssign.setAssignId(this.customSequence.getNextKey("mt_bom_site_assign_s"));
                newAssign.setSiteId(siteId);
                newAssign.setEnableFlag("Y");
                newAssign.setBomId(newBomId);
                newAssign.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_site_assign_cid_s")));
                newAssign.setCreatedBy(userId);
                newAssign.setCreationDate(now);
                newAssign.setLastUpdatedBy(userId);
                newAssign.setLastUpdateDate(now);
                newAssign.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(newAssign));
            }
        }
        // 获取BOM行
        MtBomComponent bomComponent = new MtBomComponent();
        bomComponent.setTenantId(tenantId);
        bomComponent.setBomId(oldBom.getBomId());
        List<MtBomComponent> oldBomComponents = this.mtBomComponentMapper.select(bomComponent);
        Map<String, String> bomComponentMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(oldBomComponents)) {
            for (MtBomComponent oldBomComponent : oldBomComponents) {
                // BOM Component
                final String oldBomCompId = oldBomComponent.getBomComponentId();
                final String newBomCompId = this.customSequence.getNextKey("mt_bom_component_s");
                oldBomComponent.setTenantId(tenantId);
                oldBomComponent.setBomComponentId(newBomCompId);
                oldBomComponent.setBomId(newBomId);
                oldBomComponent.setCopiedFromComponentId(oldBomCompId);
                oldBomComponent.setCreatedBy(userId);
                oldBomComponent.setCreationDate(now);
                oldBomComponent.setLastUpdatedBy(userId);
                oldBomComponent.setLastUpdateDate(now);
                oldBomComponent.setObjectVersionNumber(1L);
                oldBomComponent.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_component_cid_s")));
                sqlList.addAll(MtSqlHelper.getInsertSql(oldBomComponent));
                bomComponentMap.put(oldBomCompId, newBomCompId);

                // 保存BOM行参考点
                MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
                mtBomReferencePoint.setTenantId(tenantId);
                mtBomReferencePoint.setBomComponentId(oldBomCompId);
                List<MtBomReferencePoint> oldBomReferencePoints = this.mtBomReferencePointMapper
                        .select(mtBomReferencePoint);
                if (CollectionUtils.isNotEmpty(oldBomReferencePoints)) {
                    for (MtBomReferencePoint c : oldBomReferencePoints) {
                        String oldBomReferencePointId = c.getBomReferencePointId();
                        String newBomReferencePointId = this.customSequence.getNextKey("mt_bom_reference_point_s");
                        c.setTenantId(tenantId);
                        c.setBomReferencePointId(newBomReferencePointId);
                        c.setBomComponentId(newBomCompId);
                        c.setCopiedFromPointId(oldBomReferencePointId);
                        c.setCreatedBy(userId);
                        c.setLastUpdatedBy(userId);
                        c.setCreationDate(now);
                        c.setLastUpdateDate(now);
                        c.setObjectVersionNumber(1L);
                        c.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                        sqlList.addAll(MtSqlHelper.getInsertSql(c));
                    }
                }

                // 保存BOM行替代组
                MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
                mtBomSubstituteGroup.setTenantId(tenantId);
                mtBomSubstituteGroup.setBomComponentId(oldBomCompId);
                List<MtBomSubstituteGroup> oldBomSubstituteGroups = this.mtBomSubstituteGroupMapper
                        .select(mtBomSubstituteGroup);
                if (CollectionUtils.isNotEmpty(oldBomSubstituteGroups)) {
                    for (MtBomSubstituteGroup c : oldBomSubstituteGroups) {
                        final String oldGroupId = c.getBomSubstituteGroupId();
                        final String newGroupId = this.customSequence.getNextKey("mt_bom_substitute_group_s");
                        c.setTenantId(tenantId);
                        c.setBomSubstituteGroupId(newGroupId);
                        c.setBomComponentId(newBomCompId);
                        c.setCopiedFromGroupId(oldGroupId);
                        c.setCreatedBy(userId);
                        c.setLastUpdatedBy(userId);
                        c.setCreationDate(now);
                        c.setLastUpdateDate(now);
                        c.setObjectVersionNumber(1L);
                        c.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                        sqlList.addAll(MtSqlHelper.getInsertSql(c));

                        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
                        mtBomSubstitute.setTenantId(tenantId);
                        mtBomSubstitute.setBomSubstituteGroupId(oldGroupId);
                        List<MtBomSubstitute> mtBomSubstitutes = this.mtBomSubstituteMapper.select(mtBomSubstitute);
                        if (CollectionUtils.isNotEmpty(mtBomSubstitutes)) {
                            for (MtBomSubstitute b : mtBomSubstitutes) {
                                String oldBomSubstituteId = b.getBomSubstituteId();
                                String newBomSubstituteId = this.customSequence.getNextKey("mt_bom_substitute_s");
                                b.setTenantId(tenantId);
                                b.setBomSubstituteId(newBomSubstituteId);
                                b.setBomSubstituteGroupId(newGroupId);
                                b.setCopiedFromSubstituteId(oldBomSubstituteId);
                                b.setCreatedBy(userId);
                                b.setLastUpdatedBy(userId);
                                b.setCreationDate(now);
                                b.setLastUpdateDate(now);
                                b.setObjectVersionNumber(1L);
                                b.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_cid_s")));
                                sqlList.addAll(MtSqlHelper.getInsertSql(b));
                            }
                        }
                    }
                }
            }
        }

        /* 新增逻辑BOM_COMPONENT_ATTR复制 */
        if (MapUtils.isNotEmpty(bomComponentMap)) {
            List<String> oldBomComponentIds = bomComponentMap.entrySet().stream().map(t -> t.getKey())
                    .collect(Collectors.toList());
            List<MtExtendAttrVO3> bomComponentAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", oldBomComponentIds);
            if (CollectionUtils.isNotEmpty(bomComponentAttrs)) {
                for (MtExtendAttrVO3 bomComponentAttr : bomComponentAttrs) {
                    sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_bom_component_attr",
                            "BOM_COMPONENT_ID", bomComponentMap.get(bomComponentAttr.getMainTableKeyValue()),
                            bomComponentAttr.getAttrName(), bomComponentAttr.getAttrValue(), bomComponentAttr.getLang(),
                            now, userId));
                }
            }
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return newBomId;
    }

    @Override
    public void bomAvailableVerify(Long tenantId, MtBomVO3 condition) {
        if (StringUtils.isEmpty(condition.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomAvailableVerify】"));
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(condition.getBomId());
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomAvailableVerify】"));
        }
        if (mtBom.getDateFrom().getTime() > new Date(System.currentTimeMillis()).getTime() || (mtBom.getDateTo() != null
                && mtBom.getDateTo().getTime() <= new Date(System.currentTimeMillis()).getTime())) {
            throw new MtException("MT_BOM_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0007", "BOM", "【API:bomAvailableVerify】"));
        }
        if (!mtBom.getBomStatus().equals("CAN_RELEASE") && !mtBom.getBomStatus().equals("FREEZE")) {
            MtGenStatus mtGenStatus = mtGenStatusRepository.getGenStatus(tenantId, "BOM", "BOM_STATUS",
                    mtBom.getBomStatus());
            String statusDesc = "";
            if (mtGenStatus != null && StringUtils.isNotEmpty(mtGenStatus.getDescription())) {
                statusDesc = mtGenStatus.getDescription();
            }
            throw new MtException("MT_BOM_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0008", "BOM", statusDesc, "【API:bomAvailableVerify】"));
        }
        if (StringUtils.isNotEmpty(condition.getBomType()) && !condition.getBomType().equals(mtBom.getBomType())) {
            String sourceDesc = "";
            String targetDesc = "";
            MtGenType mtGenType = this.mtGenTypeRepository.getGenType(tenantId, "BOM", "BOM_TYPE", mtBom.getBomType());
            if (mtGenType != null && StringUtils.isNotEmpty(mtGenType.getDescription())) {
                sourceDesc = mtGenType.getDescription();
            }
            mtGenType = this.mtGenTypeRepository.getGenType(tenantId, "BOM", "BOM_TYPE", condition.getBomType());
            if (mtGenType != null && StringUtils.isNotEmpty(mtGenType.getDescription())) {
                targetDesc = mtGenType.getDescription();
            }
            throw new MtException("MT_BOM_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0009", "BOM", sourceDesc, targetDesc, "【API:bomAvailableVerify】"));
        }
        /* 新增一个报错 判断输入站点是否有效 */

        if (!StringUtils.isEmpty(condition.getSiteId())) {
            MtBomVO7 bom = self().bomBasicGet(tenantId, condition.getBomId());
            if (!bom.getSiteId().contains(condition.getSiteId())) {
                throw new MtException("MT_BOM_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0058", "BOM", "【API:bomAvailableVerify】"));
            }
        }

    }

    @Override
    public void bomUpdateVerify(Long tenantId, String bomId) {
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomUpdateVerify】"));
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(bomId);
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomUpdateVerify】"));
        }

        String bomStatus = mtBom.getBomStatus() == null ? "" : mtBom.getBomStatus();
        if (bomStatus.equals("FREEZE") || bomStatus.equals("ABANDON")) {
            MtGenStatus mtGenStatus = mtGenStatusRepository.getGenStatus(tenantId, "BOM", "BOM_STATUS", bomStatus);
            String statusDesc = "";
            if (mtGenStatus != null && StringUtils.isNotEmpty(mtGenStatus.getDescription())) {
                statusDesc = mtGenStatus.getDescription();
            }
            throw new MtException("MT_BOM_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0010", "BOM", statusDesc, "【API:bomUpdateVerify】"));
        }

        String releasedFlag = mtBom.getReleasedFlag() == null ? "" : mtBom.getReleasedFlag();
        if (releasedFlag.equals("Y")) {
            /* 新增逻辑 调用bomRouterLimitEoQuery 如果找到数据就报错 */
            MtEoVO5 mtEoVO5 = new MtEoVO5();
            List<String> status = new ArrayList<String>();
            status.add("RELEASED");
            status.add("WORKING");
            mtEoVO5.setStatus(status);
            mtEoVO5.setBomId(mtBom.getBomId());
            List<String> ls = mtEoRepository.bomRouterLimitEoQuery(tenantId, mtEoVO5);
            if (ls.size() > 0) {
                throw new MtException("MT_BOM_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0011", "BOM", "【API:bomUpdateVerify】"));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bomStatusUpdate(Long tenantId, String bomId, String status) {
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomStatusUpdate】"));
        }
        if (StringUtils.isEmpty(status)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "status", "【API:bomStatusUpdate】"));
        }

        List<MtGenStatus> mtGenStatuz = mtGenStatusRepository.getGenStatuz(tenantId, "BOM", "BOM_STATUS");
        if (CollectionUtils.isEmpty(mtGenStatuz)) {
            throw new MtException("MT_BOM_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0012", "BOM", "【API:bomStatusUpdate】"));
        }
        if (!mtGenStatuz.stream().anyMatch(t -> t.getStatusCode().equals(status))) {
            throw new MtException("MT_BOM_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0013", "BOM", "【API:bomStatusUpdate】"));
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(bomId);
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomStatusUpdate】"));
        }
        if (mtBom.getBomStatus().equals("ABANDON")) {
            String description = "ABANDON";
            String bomStatus = mtBom.getBomStatus();
            Optional<MtGenStatus> abandon = mtGenStatuz.stream().filter(t -> t.getStatusCode().equals(bomStatus))
                    .findFirst();
            if (abandon.isPresent()) {
                description = abandon.get().getDescription();
            }
            throw new MtException("MT_BOM_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0010", "BOM", description, "【API:bomStatusUpdate】"));
        }

        mtBom.setBomStatus(status);
        self().updateByPrimaryKeySelective(mtBom);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bomCurrentFlagUpdate(Long tenantId, String bomId) {
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomCurrentFlagUpdate】"));
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(bomId);
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomCurrentFlagUpdate】"));
        }

        bomUpdateVerify(tenantId, bomId);

        List<String> sqlList = new ArrayList<String>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        MtBom updateBom = new MtBom();
        updateBom.setTenantId(tenantId);
        updateBom.setBomId(bomId);
        updateBom.setCurrentFlag("Y");
        updateBom.setLastUpdatedBy(userId);
        updateBom.setLastUpdateDate(now);
        updateBom.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_cid_s")));
        sqlList.addAll(MtSqlHelper.getUpdateSql(updateBom));

        MtBom tmpBom = new MtBom();
        tmpBom.setTenantId(tenantId);
        tmpBom.setBomName(mtBom.getBomName());
        tmpBom.setBomType(mtBom.getBomType());
        List<MtBom> mtBoms = this.mtBomMapper.select(tmpBom);

        List<MtBom> tmpBoms = mtBoms.stream().filter(t -> !t.getBomId().equals(bomId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(tmpBoms)) {
            for (MtBom c : tmpBoms) {
                MtBom otherBom = new MtBom();
                otherBom.setTenantId(tenantId);
                otherBom.setBomId(c.getBomId());
                otherBom.setCurrentFlag("N");
                otherBom.setLastUpdatedBy(userId);
                otherBom.setLastUpdateDate(now);
                otherBom.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_cid_s")));
                sqlList.addAll(MtSqlHelper.getUpdateSql(otherBom));
            }
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bomReleasedFlagUpdate(Long tenantId, String bomId, String releasedFlag) {
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomReleasedFlagUpdate】"));
        }
        if (StringUtils.isEmpty(releasedFlag)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "releasedFlag", "【API:bomReleasedFlagUpdate】"));
        }
        if (!releasedFlag.equals("Y") && !releasedFlag.equals("N")) {
            throw new MtException("MT_BOM_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0014", "BOM", "【API:bomReleasedFlagUpdate】"));
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(bomId);
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomReleasedFlagUpdate】"));
        }

        mtBom.setReleasedFlag(releasedFlag);
        self().updateByPrimaryKeySelective(mtBom);
    }

    @Override
    public List<MtBomVO7> bomBasicBatchGet(Long tenantId, List<String> bomIds) {
        if (CollectionUtils.isEmpty(bomIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomBasicBatchGet】"));
        }
        return this.mtBomMapper.selectByIdsCustom(tenantId, bomIds);
    }

    /**
     * bomAutoRevisionGet-获取装配清单是否自动升版本策略
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    @Override
    public String bomAutoRevisionGet(Long tenantId, String bomId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomAutoRevisionGet】"));
        }

        // 2. 获取bom数据
        MtBomVO7 mtBom = bomBasicGet(tenantId, bomId);
        if (mtBom == null || StringUtils.isEmpty(mtBom.getBomId())) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomAutoRevisionGet】"));
        }

        // 3. 若Bom维护了自动升级版本策略，则直接返回该值
        if (StringUtils.isNotEmpty(mtBom.getAutoRevisionFlag())) {
            return mtBom.getAutoRevisionFlag();
        }

        // 4. 获取系统全局参数的bom自动升级版本策略
        String systemAutoRevisionFlag = profileClient.getProfileValueByOptions(tenantId,
                DetailsHelper.getUserDetails().getUserId(),
                DetailsHelper.getUserDetails().getRoleId(), "BOM_AUTO_REVISION_FLAG");

        // 获取不到默认为 N
        if (StringUtils.isEmpty(systemAutoRevisionFlag)) {
            systemAutoRevisionFlag = "N";
        }

        return systemAutoRevisionFlag;
    }

    /**
     * bomRevisionGenerate-生成装配清单版本号
     *
     * @param tenantId
     * @param bomVO4
     * @return
     */
    @Override
    public String bomRevisionGenerate(Long tenantId, MtBomVO4 bomVO4) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomVO4.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomRevisionGenerate】"));
        }

        // 2. 获取装配清单自动升版本策略
        String autoRevisionFlag = bomAutoRevisionGet(tenantId, bomVO4.getBomId());

        if ("N".equals(autoRevisionFlag)) {
            if (StringUtils.isEmpty(bomVO4.getRevision())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "revision", "【API:bomRevisionGenerate】"));
            }
            return bomVO4.getRevision();
        } else {
            // 3. 获取bom数据
            MtBomVO7 mtBom = bomBasicGet(tenantId, bomVO4.getBomId());
            if (mtBom == null || StringUtils.isEmpty(mtBom.getBomId())) {
                throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0004", "BOM", "【API:bomRevisionGenerate】"));
            }

            MtBom propertyBom = new MtBom();
            propertyBom.setTenantId(tenantId);
            propertyBom.setBomName(mtBom.getBomName());
            propertyBom.setBomType(mtBom.getBomType());
            List<MtBom> mtBomList = mtBomMapper.select(propertyBom);

            // 获取三个维度下，数字类型的revision集合
            List<Integer> revisionList = new ArrayList<>();
            for (MtBom tempBom : mtBomList) {
                if (NumberHelper.isNumeric(tempBom.getRevision())) {
                    revisionList.add(Integer.valueOf(tempBom.getRevision()));
                }
            }

            // 获取最大版本号，+1 后输出
            if (revisionList != null && revisionList.size() > 0) {
                Collections.sort(revisionList, Collections.reverseOrder());
                return new DecimalFormat("00").format(revisionList.get(0) + 1);
            } else {
                // 如果没有版本，初始为01
                return "01";
            }
        }
    }

    /**
     * bomPropertyUpdate-新增更新装配清单基础属性
     *
     * @param tenantId
     * @param dto
     * @return 处理的BomId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bomPropertyUpdate(Long tenantId, MtBom dto, String fullUpdate) {
        String dealBomId = "";
        // 2. 判断是否输入 bomId：如果输入，执行更新；如果未输入，执行新增
        if (StringUtils.isEmpty(dto.getBomId())) {
            if (StringUtils.isEmpty(dto.getBomName())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomName", "【API:bomPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getBomType())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomType", "【API:bomPropertyUpdate】"));
            }
            if (dto.getDateFrom() == null) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "dateFrom", "【API:bomPropertyUpdate】"));
            }
            // 执行新增
            String autoRevisionFlag = dto.getAutoRevisionFlag();
            if (StringUtils.isEmpty(autoRevisionFlag)) {
                // 获取系统全局参数的bom自动升级版本策略
                String systemAutoRevisionFlag = profileClient.getProfileValueByOptions(tenantId,
                        DetailsHelper.getUserDetails().getUserId(),
                        DetailsHelper.getUserDetails().getRoleId(), "BOM_AUTO_REVISION_FLAG");
                if (StringUtils.isNotEmpty(systemAutoRevisionFlag)) {
                    autoRevisionFlag = systemAutoRevisionFlag;
                } else {
                    autoRevisionFlag = "N";
                }
            }
            dto.setAutoRevisionFlag(autoRevisionFlag);
            if (autoRevisionFlag.equals("Y")) {
                dto.setRevision("01");
            } else if (StringUtils.isEmpty(dto.getRevision())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "revision", "【API:bomPropertyUpdate】"));
            } else {
                dto.setRevision(dto.getRevision());
            }

            // 校验bom唯一性
            MtBom temp = new MtBom();
            temp.setTenantId(tenantId);
            temp.setBomName(dto.getBomName());
            temp.setRevision(dto.getRevision());
            temp.setBomType(dto.getBomType());
            temp = mtBomMapper.selectOne(temp);
            if (temp != null) {
                throw new MtException("MT_BOM_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0068", "BOM", "【API:bomPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            dealBomId = dto.getBomId();

            MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
            mtBomHisVO1.setBomId(dto.getBomId());
            mtBomHisVO1.setEventTypeCode("BOM_CREATE");
            mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);
        } else {
            // 执行更新
            // 获取bom数据
            if (dto.getBomName() != null && "".equals(dto.getBomName())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomName", "【API:bomPropertyUpdate】"));
            }
            if (dto.getBomType() != null && "".equals(dto.getBomType())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomType", "【API:bomPropertyUpdate】"));
            }
            MtBomVO7 oldBomVO7 = bomBasicGet(tenantId, dto.getBomId());
            if (oldBomVO7 == null || StringUtils.isEmpty(oldBomVO7.getBomId())) {
                throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0004", "BOM", "【API:bomPropertyUpdate】"));
            }
            // 获取Bom自动升级版本策略
            String autoRevisionFlag = bomAutoRevisionGet(tenantId, oldBomVO7.getBomId());

            // bom copy check, bom auto revision is Y, bom name and bom type are
            // changed
            boolean bomNameUpdateFlag = !(dto.getBomName() == null || oldBomVO7.getBomName().equals(dto.getBomName()));
            boolean bomTypeUpdateFlag = !(dto.getBomType() == null || oldBomVO7.getBomType().equals(dto.getBomType()));
            if ("Y".equals(autoRevisionFlag) && (bomNameUpdateFlag || bomTypeUpdateFlag)) {
                MtBomVO4 bomVO4 = new MtBomVO4();
                bomVO4.setBomId(dto.getBomId());
                bomVO4.setRevision(dto.getRevision());
                String revision = bomRevisionGenerate(tenantId, bomVO4);

                MtBomVO2 bomVO2 = new MtBomVO2();
                bomVO2.setBomId(dto.getBomId());
                bomVO2.setBomName(dto.getBomName());
                bomVO2.setRevision(revision);
                bomVO2.setBomType(dto.getBomType());
                String bomId = bomCopy(tenantId, bomVO2);

                // 获取新复制的 Bom 数据
                MtBomVO7 newBomVO7 = bomBasicGet(tenantId, bomId);

                MtBom newBom = new MtBom();
                // 更新输入的其他字段
                BeanUtils.copyProperties(newBomVO7, newBom);
                newBom.setTenantId(tenantId);
                newBom.setCurrentFlag(dto.getCurrentFlag());
                newBom.setDateFrom(dto.getDateFrom());
                newBom.setDateTo(dto.getDateTo());
                newBom.setDescription(dto.getDescription());
                newBom.setBomStatus(dto.getBomStatus());
                newBom.setReleasedFlag(dto.getReleasedFlag());
                newBom.setPrimaryQty(dto.getPrimaryQty());
                newBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
                newBom.setAssembleAsMaterialFlag(dto.getAssembleAsMaterialFlag());
                if ("Y".equals(fullUpdate)) {
                    newBom.set_tls(dto.get_tls());
                    self().updateByPrimaryKey(newBom);
                } else {
                    self().updateByPrimaryKeySelective(newBom);
                }

                dealBomId = newBom.getBomId();
            } else {

                MtBom oldBom = new MtBom();
                //增量更新
                BeanUtils.copyProperties(oldBomVO7, oldBom);
                oldBom.setTenantId(tenantId);
                oldBom.setBomName(dto.getBomName());
                oldBom.setBomType(dto.getBomType());
                oldBom.setCurrentFlag(dto.getCurrentFlag());
                oldBom.setDateFrom(dto.getDateFrom());
                oldBom.setDateTo(dto.getDateTo());
                oldBom.setDescription(dto.getDescription());
                oldBom.setBomStatus(dto.getBomStatus());
                oldBom.setReleasedFlag(dto.getReleasedFlag());
                oldBom.setPrimaryQty(dto.getPrimaryQty());
                oldBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
                oldBom.setAssembleAsMaterialFlag(dto.getAssembleAsMaterialFlag());

                if (("N".equals(autoRevisionFlag))) {
                    if (StringUtils.isEmpty(dto.getRevision())) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "revision", "【API:bomPropertyUpdate】"));
                    }
                    oldBom.setRevision(dto.getRevision());
                }


                // 校验bom唯一性
                MtBom temp = new MtBom();
                temp.setTenantId(tenantId);
                temp.setBomName(oldBom.getBomName());
                temp.setRevision(oldBom.getRevision());
                temp.setBomType(oldBom.getBomType());
                temp = mtBomMapper.selectOne(temp);
                if (temp != null && !oldBom.getBomId().equals(temp.getBomId())) {
                    throw new MtException("MT_BOM_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0068", "BOM", "【API:bomPropertyUpdate】"));
                }

                if ("Y".equals(fullUpdate)) {
                    oldBom.set_tls(dto.get_tls());
                    self().updateByPrimaryKey(oldBom);
                } else {
                    self().updateByPrimaryKeySelective(oldBom);
                }

                dealBomId = oldBom.getBomId();

                // 生成历史
                MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
                mtBomHisVO1.setBomId(oldBom.getBomId());
                mtBomHisVO1.setEventTypeCode("BOM_UPDATE");
                mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);
            }
        }
        return dealBomId;
    }

    /**
     * sourceBomLimitTargetBomUpdate-根据来源BOM更新目标BOM
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String sourceBomLimitTargetBomUpdate(Long tenantId, MtBomVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getSourceBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "sourceBomId", "【API:sourceBomLimitTargetBomUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getTargetBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "targetBomId", "【API:sourceBomLimitTargetBomUpdate】"));
        }

        // 2. 验证参数对应 Bom 是否存在
        MtBomVO7 sourceBom = bomBasicGet(tenantId, dto.getSourceBomId());
        if (sourceBom == null || StringUtils.isEmpty(sourceBom.getBomId())) {
            throw new MtException("MT_BOM_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0045", "BOM", "sourceBomId", "【API:sourceBomLimitTargetBomUpdate】"));
        }

        MtBomVO7 targetBom = bomBasicGet(tenantId, dto.getTargetBomId());
        if (targetBom == null || StringUtils.isEmpty(targetBom.getBomId())) {
            throw new MtException("MT_BOM_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0045", "BOM", "targetBomId", "【API:sourceBomLimitTargetBomUpdate】"));
        }

        // 3. 验证目标 Bom 是否可以更新
        bomUpdateVerify(tenantId, dto.getTargetBomId());

        // 4. 获取目标 Bom 自动升级版本策略
        String autoRevisionFlag = bomAutoRevisionGet(tenantId, dto.getTargetBomId());

        // 新加逻辑，只有修改主属性的情况下，才判断为自动升级版本
        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setTenantId(tenantId);
        mtBomComponent.setBomId(dto.getSourceBomId());
        List<MtBomComponent> sourceBomComponents = mtBomComponentMapper.select(mtBomComponent);

        mtBomComponent = new MtBomComponent();
        mtBomComponent.setTenantId(tenantId);
        mtBomComponent.setBomId(dto.getTargetBomId());
        List<MtBomComponent> targetBomComponents = mtBomComponentMapper.select(mtBomComponent);

        if ("Y".equals(autoRevisionFlag)) {
            // 判断bom组件：
            // a. 来源有，目标全无，则以来源数据新增 此时认为需要自动升级版本
            // b. 目标有，来源全无，则目标全部失效 此时认为不需要自动升级版本
            // c. 来源有，目标也有，则匹配 MATERIAL_ID + BOM_COMPONENT_TYPE 相同的数据
            if (CollectionUtils.isNotEmpty(sourceBomComponents) && CollectionUtils.isEmpty(targetBomComponents)) {
                autoRevisionFlag = "Y";
            } else if (CollectionUtils.isNotEmpty(sourceBomComponents)
                    && CollectionUtils.isNotEmpty(targetBomComponents)) {
                // 默认都能匹配的上，是不需要自动升级版本的
                autoRevisionFlag = "N";

                Map<String, MtBomComponent> sourceBomComponentMap = sourceBomComponents.stream()
                        .collect(Collectors.toMap(m -> m.getBomComponentId(), m -> m));

                for (MtBomComponent tempTarget : targetBomComponents) {
                    // 对每一个目标组件筛选来源组件
                    List<MtBomComponent> result = sourceBomComponents.stream()
                            .filter(s -> s.getMaterialId().equals(tempTarget.getMaterialId())
                                    && s.getBomComponentType().equals(tempTarget.getBomComponentType()))
                            .collect(Collectors.toList());

                    // 如果无对应来源组件，则无效改目标组件 此时认为不需要自动升级版本
                    // 如果有对应来源组件，则判断必要字段是否更新
                    if (CollectionUtils.isNotEmpty(result)) {
                        // 剔除来源组件Map数据: 筛选结果唯一
                        MtBomComponent tempSource = result.get(0);
                        sourceBomComponentMap.remove(tempSource.getBomComponentId());

                        // assembleMethod有变更，则需要自动升版本
                        if (!tempTarget.getAssembleMethod().equals(tempSource.getAssembleMethod())) {
                            autoRevisionFlag = "Y";
                            break;
                        }
                    }
                }

                if ("N".equals(autoRevisionFlag)) {
                    // 处理匹配之后，如果来源还有未匹配的数据，则对这部分来源组件执行目标新增 此时认为需要自动升级版本
                    if (MapUtils.isNotEmpty(sourceBomComponentMap)) {
                        autoRevisionFlag = "Y";
                    }
                }
            }

        }

        if ("Y".equals(autoRevisionFlag)) {
            // 5. 表示需要自动升版本, 复制为新的装配清单
            // 获取版本号
            MtBomVO4 bomVO4 = new MtBomVO4();
            bomVO4.setBomId(targetBom.getBomId());
            bomVO4.setRevision(targetBom.getRevision());
            String revision = bomRevisionGenerate(tenantId, bomVO4);

            // 复制 Bom 屬性赋值
            MtBomVO2 bomVO2 = new MtBomVO2();
            bomVO2.setBomId(sourceBom.getBomId());
            bomVO2.setBomName(targetBom.getBomName());
            bomVO2.setRevision(revision);
            bomVO2.setBomType(targetBom.getBomType());

            // 结束API返回复制后新的bomId
            return bomCopy(tenantId, bomVO2);
        }

        /*
         * 变更为拼sql的方式
         */
        List<String> sqlList = new ArrayList<>();

        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 6. 处理不需要自动升版本的情况
        // 6.1. 来源装配清单属性内容更新至目标装配清单上
        targetBom.setTenantId(tenantId);
        targetBom.setDateFrom(sourceBom.getDateFrom());
        targetBom.setDateTo(sourceBom.getDateTo());
        targetBom.setDescription(sourceBom.getDescription());
        targetBom.setBomStatus(sourceBom.getBomStatus());
        targetBom.setCopiedFromBomId(dto.getSourceBomId());
        targetBom.setPrimaryQty(sourceBom.getPrimaryQty());
        targetBom.setAutoRevisionFlag(sourceBom.getAutoRevisionFlag());
        targetBom.setAssembleAsMaterialFlag(sourceBom.getAssembleAsMaterialFlag());
        targetBom.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_cid_s")));
        targetBom.setLastUpdateDate(now);
        targetBom.setLastUpdatedBy(userId);
        MtBom mtBom = new MtBom();
        BeanUtils.copyProperties(targetBom, mtBom);
        sqlList.addAll(MtSqlHelper.getUpdateSql(mtBom));

        // 6.2. 处理装配清单组件行数据
        List<String> componentSqlList = mtBomComponentRepository.sourceLimitTargetBomComponentUpdateSqlGet(tenantId,
                sourceBomComponents, targetBomComponents, dto.getTargetBomId(), now);

        sqlList.addAll(componentSqlList);

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        // 将当前版本置为 Y
        bomCurrentFlagUpdate(tenantId, dto.getTargetBomId());

        // 6.3. 记录历史
        MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
        mtBomHisVO1.setBomId(dto.getTargetBomId());
        mtBomHisVO1.setEventTypeCode("BOM_ALL_UPDATE");
        mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);

        return targetBom.getBomId();
    }

    /**
     * bomAllUpdate-新增更新整体装配清单
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bomAllUpdate(Long tenantId, MtBomVO6 dto) {

        List<String> yesNo = Arrays.asList("Y", "N");

        // 1. 验证参数有效性
        // 判断新增或者更新
        Boolean flag = false;
        if (StringUtils.isEmpty(dto.getBomId())) {
            flag = true;
        }
        if (flag && StringUtils.isEmpty(dto.getBomName()) || !flag && "".equals(dto.getBomName())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomName", "【API:bomAllUpdate】"));
        }
        if (flag && StringUtils.isEmpty(dto.getBomType()) || !flag && "".equals(dto.getBomType())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomType", "【API:bomAllUpdate】"));
        }
        if (flag && dto.getDateFrom() == null) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "dateFrom", "【API:bomAllUpdate】"));
        }

        // 校验bomType有效性
        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("BOM");
        mtGenTypeVO2.setTypeGroup("BOM_TYPE");
        List<MtGenType> bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        if (!bomTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList())
                .contains(dto.getBomType())) {
            throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0056", "BOM", "bomType:" + dto.getBomType(), "【API:bomAllUpdate】"));
        }

        // 验证currentFlag有效性
        if (StringUtils.isNotEmpty(dto.getCurrentFlag()) && !yesNo.contains(dto.getCurrentFlag())) {
            throw new MtException("MT_BOM_0055 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0055 ", "BOM", "currentFlag", "【API:bomAllUpdate】"));
        }

        // 验证assembleAsMaterialFlag有效性
        if (StringUtils.isNotEmpty(dto.getAssembleAsMaterialFlag())
                && !yesNo.contains(dto.getAssembleAsMaterialFlag())) {
            throw new MtException("MT_BOM_0055 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0055 ", "BOM", "assembleAsMaterialFlag", "【API:bomAllUpdate】"));
        }

        // 校验bomStatus有效性
        if (StringUtils.isNotEmpty(dto.getBomStatus())) {
            MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
            mtGenStatusVO2.setModule("BOM");
            mtGenStatusVO2.setStatusGroup("BOM_STATUS");
            List<MtGenStatus> bomStatus = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
            if (!bomStatus.stream().map(MtGenStatus::getStatusCode).distinct().collect(Collectors.toList())
                    .contains(dto.getBomStatus())) {
                throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0056", "BOM", "bomStatus:" + dto.getBomStatus(), "【API:bomAllUpdate】"));
            }
        }

        // 验证 releasedFlag 有效性
        if (StringUtils.isNotEmpty(dto.getReleasedFlag()) && !yesNo.contains(dto.getReleasedFlag())) {
            throw new MtException("MT_BOM_0055 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0055 ", "BOM", "releasedFlag", "【API:bomAllUpdate】"));
        }

        // 验证 autoRevisionFlag 有效性
        if (StringUtils.isNotEmpty(dto.getAutoRevisionFlag()) && !yesNo.contains(dto.getAutoRevisionFlag())) {
            throw new MtException("MT_BOM_0055 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0055 ", "BOM", "autoRevisionFlag", "【API:bomAllUpdate】"));
        }

        if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {
            for (MtBomComponentVO7 bomComponent : dto.getMtBomComponentList()) {
                Boolean temp = false;
                if (StringUtils.isEmpty(bomComponent.getBomComponentId())) {
                    temp = true;
                }
                if (temp && bomComponent.getLineNumber() == null) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "lineNumber", "【API:bomAllUpdate】"));
                }
                if (temp && StringUtils.isEmpty(bomComponent.getMaterialId())
                        || !temp && "".equals(bomComponent.getMaterialId())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "materialId", "【API:bomAllUpdate】"));
                }
                if (temp && StringUtils.isEmpty(bomComponent.getBomComponentType())
                        || !temp && "".equals(bomComponent.getBomComponentType())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentType", "【API:bomAllUpdate】"));
                }
                if (temp && bomComponent.getDateFrom() == null) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "dateFrom", "【API:bomAllUpdate】"));
                }
                if (temp && bomComponent.getQty() == null) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", "【API:bomAllUpdate】"));
                }

                // 校验materialId有效性
                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,
                        bomComponent.getMaterialId());
                if (mtMaterial == null || !"Y".equals(mtMaterial.getEnableFlag())) {
                    throw new MtException("MT_BOM_0054",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054", "BOM",
                                    "bomComponent.materialId:" + bomComponent.getMaterialId(), "【API:bomAllUpdate】"));
                }

                // 校验bomComponentType有效性
                mtGenTypeVO2.setModule("BOM");
                mtGenTypeVO2.setTypeGroup("BOM_COMPONENT_TYPE");
                bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                if (!bomTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList())
                        .contains(bomComponent.getBomComponentType())) {
                    throw new MtException("MT_BOM_0056",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056", "BOM",
                                    "bomComponentType:" + bomComponent.getBomComponentType(), "【API:bomAllUpdate】"));
                }

                // 验证 keyMaterialFlag 有效性
                if (StringUtils.isNotEmpty(bomComponent.getKeyMaterialFlag())
                        && !yesNo.contains(bomComponent.getKeyMaterialFlag())) {
                    throw new MtException("MT_BOM_0055 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0055 ", "BOM", "keyMaterialFlag", "【API:bomAllUpdate】"));
                }

                // 校验assembleMethod有效性
                if (StringUtils.isNotEmpty(bomComponent.getAssembleMethod())) {
                    mtGenTypeVO2.setModule("MATERIAL");
                    mtGenTypeVO2.setTypeGroup("ASSY_METHOD");
                    bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                    if (!bomTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList())
                            .contains(bomComponent.getAssembleMethod())) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056", "BOM",
                                        "assembleMethod:" + bomComponent.getAssembleMethod(), "【API:bomAllUpdate】"));
                    }
                }

                // 验证 assembleAsReqFlag 有效性
                if (StringUtils.isNotEmpty(bomComponent.getAssembleAsReqFlag())
                        && !yesNo.contains(bomComponent.getAssembleAsReqFlag())) {
                    throw new MtException("MT_BOM_0055 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0055 ", "BOM", "assembleAsReqFlag", "【API:bomAllUpdate】"));
                }

                // 校验 attritionPolicy 有效性
                if (StringUtils.isNotEmpty(bomComponent.getAttritionPolicy())) {
                    mtGenTypeVO2.setModule("BOM");
                    mtGenTypeVO2.setTypeGroup("ATTRITION_POLICY");
                    bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                    if (!bomTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList())
                            .contains(bomComponent.getAttritionPolicy())) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056", "BOM",
                                        "attritionPolicy:" + bomComponent.getAttritionPolicy(), "【API:bomAllUpdate】"));
                    }
                }

                if (CollectionUtils.isNotEmpty(bomComponent.getMtBomReferencePointList())) {
                    for (MtBomReferencePointVO9 bomReferencePoint : bomComponent.getMtBomReferencePointList()) {
                        Boolean temp1 = false;
                        if (StringUtils.isEmpty(bomReferencePoint.getBomReferencePointId())) {
                            temp1 = true;
                        }
                        if (temp1 && StringUtils.isEmpty(bomReferencePoint.getReferencePoint())
                                || !temp1 && "".equals(bomReferencePoint.getReferencePoint())) {
                            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0001", "BOM", "referencePoint", "【API:bomAllUpdate】"));
                        }
                        if (temp1 && bomReferencePoint.getLineNumber() == null) {
                            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0001", "BOM", "lineNumber", "【API:bomAllUpdate】"));
                        }
                        if (temp1 && StringUtils.isEmpty(bomReferencePoint.getEnableFlag())
                                || !temp1 && "".equals(bomReferencePoint.getEnableFlag())) {
                            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0001", "BOM", "enableFlag", "【API:bomAllUpdate】"));
                        }
                        if (temp1 && bomReferencePoint.getQty() == null) {
                            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0001", "BOM", "qty", "【API:bomAllUpdate】"));
                        }

                        // 验证 bomReferencePoint.enableFlag 有效性
                        if (!yesNo.contains(bomReferencePoint.getEnableFlag())) {
                            throw new MtException("MT_BOM_0055 ",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0055 ", "BOM",
                                            "bomReferencePoint.enableFlag", "【API:bomAllUpdate】"));
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList())) {
                    for (MtBomSubstituteGroupVO4 bomSubstituteGroup : bomComponent.getMtBomSubstituteGroupList()) {
                        Boolean temp1 = false;
                        if (StringUtils.isEmpty(bomSubstituteGroup.getBomSubstituteGroupId())) {
                            temp1 = true;
                        }
                        if (temp1 && StringUtils.isEmpty(bomSubstituteGroup.getSubstituteGroup())
                                || !temp1 && "".equals(bomSubstituteGroup.getSubstituteGroup())) {
                            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0001", "BOM", "substituteGroup", "【API:bomAllUpdate】"));
                        }
                        if (temp1 && StringUtils.isEmpty(bomSubstituteGroup.getSubstitutePolicy())
                                || !temp1 && "".equals(bomSubstituteGroup.getSubstitutePolicy())) {
                            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0001", "BOM", "substitutePolicy", "【API:bomAllUpdate】"));
                        }
                        if (temp1 && StringUtils.isEmpty(bomSubstituteGroup.getEnableFlag())
                                || !temp1 && "".equals(bomSubstituteGroup.getEnableFlag())) {
                            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0001", "BOM", "enableFlag", "【API:bomAllUpdate】"));
                        }

                        // 校验substitutePolicy有效性
                        mtGenTypeVO2.setModule("BOM");
                        mtGenTypeVO2.setTypeGroup("SUBSTITUTE_POLICY");
                        bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                        if (!bomTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList())
                                .contains(bomSubstituteGroup.getSubstitutePolicy())) {
                            throw new MtException("MT_BOM_0056",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056", "BOM",
                                            "aubstitutePolicy:" + bomSubstituteGroup.getSubstitutePolicy(),
                                            "【API:bomAllUpdate】"));
                        }

                        // 验证 bomSubstituteGroup.enableFlag 有效性
                        if (!yesNo.contains(bomSubstituteGroup.getEnableFlag())) {
                            throw new MtException("MT_BOM_0055 ",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0055 ", "BOM",
                                            "bomSubstituteGroup.enableFlag", "【API:bomAllUpdate】"));
                        }

                        if (CollectionUtils.isNotEmpty(bomSubstituteGroup.getMtBomSubstituteList())) {
                            for (MtBomSubstituteVO8 bomSubstitute : bomSubstituteGroup.getMtBomSubstituteList()) {

                                Boolean temp2 = false;
                                if (StringUtils.isEmpty(bomSubstitute.getBomSubstituteId())) {
                                    temp2 = true;
                                }
                                if (temp2 && StringUtils.isEmpty(bomSubstitute.getMaterialId())
                                        || !temp2 && "".equals(bomSubstitute.getMaterialId())) {
                                    throw new MtException("MT_BOM_0001",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001",
                                                    "BOM", "materialId", "【API:bomAllUpdate】"));
                                }
                                if (temp2 && bomSubstitute.getDateFrom() == null) {
                                    throw new MtException("MT_BOM_0001",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001",
                                                    "BOM", "dateFrom", "【API:bomAllUpdate】"));
                                }
                                if (temp2 && bomSubstitute.getSubstituteValue() == null) {
                                    throw new MtException("MT_BOM_0001",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001",
                                                    "BOM", "substituteValue", "【API:bomAllUpdate】"));
                                }

                                // 校验materialId有效性
                                mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,
                                        bomSubstitute.getMaterialId());
                                if (mtMaterial == null || !"Y".equals(mtMaterial.getEnableFlag())) {
                                    throw new MtException("MT_BOM_0054",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054",
                                                    "BOM", "bomSubstitute.materialId:" + bomSubstitute.getMaterialId(),
                                                    "【API:bomAllUpdate】"));
                                }
                            }
                        }
                    }
                }
            }
        }

        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails() == null ? -1 : DetailsHelper.getUserDetails().getUserId();

        // 2. 如果BomId为空，则为新增（行和明细属性都是新增，无论是否输入各自的Id）
        if (StringUtils.isEmpty(dto.getBomId())) {
            Date now = new Date();

            // 2.1. 新增MtBom数据
            if (StringUtils.isEmpty(dto.getAutoRevisionFlag())) {
                // 获取系统全局参数的bom自动升级版本策略
                String systemAutoRevisionFlag = profileClient.getProfileValueByOptions(tenantId,
                        DetailsHelper.getUserDetails().getUserId(),
                        DetailsHelper.getUserDetails().getRoleId(), "BOM_AUTO_REVISION_FLAG");

                if (StringUtils.isNotEmpty(systemAutoRevisionFlag)) {
                    dto.setAutoRevisionFlag(systemAutoRevisionFlag);
                }
            }

            // revision未传入时
            if (StringUtils.isEmpty(dto.getRevision())) {
                // 如果autoVersionFlag不为Y则报错
                if (!"Y".equals(dto.getAutoRevisionFlag())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "revision", "【API:bomAllUpdate】"));
                } else {
                    MtBom propertyBom = new MtBom();
                    propertyBom.setTenantId(tenantId);
                    propertyBom.setBomName(dto.getBomName());
                    propertyBom.setBomType(dto.getBomType());
                    List<MtBom> mtBomList = mtBomMapper.select(propertyBom);

                    // 获取三个维度下，数字类型的revision集合
                    List<Integer> revisionList = new ArrayList<>();
                    for (MtBom tempBom : mtBomList) {
                        if (NumberHelper.isNumeric(tempBom.getRevision())) {
                            revisionList.add(Integer.valueOf(tempBom.getRevision()));
                        }
                    }

                    // 获取最大版本号，+1
                    if (revisionList != null && revisionList.size() > 0) {
                        Collections.sort(revisionList, Collections.reverseOrder());
                        dto.setRevision(new DecimalFormat("00").format(revisionList.get(0) + 1));
                    } else {
                        // 如果没有版本，初始为01
                        dto.setRevision("01");
                    }
                }
            }

            String newBomId = customSequence.getNextKey("mt_bom_s");
            MtBom newBom = new MtBom();
            newBom.setTenantId(tenantId);
            newBom.setBomId(newBomId);
            newBom.setBomName(dto.getBomName());
            newBom.setRevision(dto.getRevision());
            newBom.setBomType(dto.getBomType());
            newBom.setCurrentFlag(dto.getCurrentFlag());
            newBom.setDateFrom(dto.getDateFrom());
            newBom.setDateTo(dto.getDateTo());
            newBom.setDescription(dto.getDescription());
            newBom.setBomStatus(dto.getBomStatus());
            newBom.setReleasedFlag(dto.getReleasedFlag());
            newBom.setPrimaryQty(dto.getPrimaryQty());
            newBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
            newBom.setAssembleAsMaterialFlag(dto.getAssembleAsMaterialFlag());
            newBom.setCreatedBy(userId);
            newBom.setCreationDate(now);
            newBom.setLastUpdatedBy(userId);
            newBom.setLastUpdateDate(now);
            newBom.setObjectVersionNumber(1L);
            newBom.setCid(Long.valueOf(customSequence.getNextKey("mt_bom_cid_s")));
            sqlList.addAll(MtSqlHelper.getInsertSql(newBom));

            if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {
                sqlList.addAll(mtBomComponentRepository.bomComponentDetailInsert(tenantId, newBomId,
                        dto.getMtBomComponentList(), now));
            }

            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

            /*
             * 逻辑变更，无论是否需要自动升级版本，都记录历史
             */
            MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
            mtBomHisVO1.setBomId(newBom.getBomId());
            mtBomHisVO1.setEventTypeCode("BOM_ALL_CREATE");
            mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);

            return newBomId;
        }

        // 3. 更新数据（bomId不为空）

        // 更新时校验revision是否为空
        if (StringUtils.isEmpty(dto.getRevision())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "revision", "【API:bomAllUpdate】"));
        }

        // 3.1. 检验 Bom 是否存在
        MtBom oldBom = new MtBom();
        oldBom.setTenantId(tenantId);
        oldBom.setBomId(dto.getBomId());
        oldBom = mtBomMapper.selectOne(oldBom);
        if (oldBom == null || StringUtils.isEmpty(oldBom.getBomId())) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomAllUpdate】"));
        }

        // 3.2. 校验传入参数的层级关系是否存在
        if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {

            List<String> bomComponentIds = dto.getMtBomComponentList().stream().map(t -> {
                return t.getBomComponentId() == null ? "" : t.getBomComponentId();
            }).collect(Collectors.toList());

            // 去除空字符串的，空字符串的是未传入componentId的数据，不需要检验关系
            bomComponentIds.removeAll(Arrays.asList(""));

            // 记录需要检验bomComponent下数据层级关系的组件(有id的数据)
            List<MtBomComponentVO7> needAdjustComponents = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(bomComponentIds)) {
                List<MtBomComponent> mtBomComponents = mtBomComponentMapper.selectByBomIdAndComponentIds(tenantId,
                        dto.getBomId(), bomComponentIds);
                if (bomComponentIds.size() != mtBomComponents.size()) {
                    List<String> bomComIds = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                        List<String> temp = mtBomComponents.stream().map(t -> t.getBomComponentId())
                                .collect(Collectors.toList());
                        bomComIds.addAll(bomComponentIds.stream().filter(item -> !temp.contains(item))
                                .collect(Collectors.toList()));
                    } else {
                        bomComIds.addAll(bomComponentIds);
                    }
                    throw new MtException("MT_BOM_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0041", "BOM", bomComIds.toString(), "【API:bomAllUpdate】"));
                }

                needAdjustComponents = dto.getMtBomComponentList().stream()
                        .filter(t -> bomComponentIds.contains(t.getBomComponentId())).collect(Collectors.toList());
            }

            // 判断每一个需要检验层级关系的 bomComponent
            for (MtBomComponentVO7 bomComponent : needAdjustComponents) {
                String bomComponentId = bomComponent.getBomComponentId();

                // bomReferencePoint
                if (CollectionUtils.isNotEmpty(bomComponent.getMtBomReferencePointList())) {
                    List<String> bomReferencePointIds = bomComponent.getMtBomReferencePointList().stream().map(t -> {
                        return t.getBomReferencePointId() == null ? "" : t.getBomReferencePointId();
                    }).collect(Collectors.toList());

                    bomReferencePointIds.removeAll(Arrays.asList(""));

                    if (CollectionUtils.isNotEmpty(bomReferencePointIds)) {
                        List<MtBomReferencePoint> mtBomReferencePoints = mtBomReferencePointMapper
                                .selectByComponentIdAndPointIds(tenantId, bomComponentId, bomReferencePointIds);
                        if (bomReferencePointIds.size() != mtBomReferencePoints.size()) {
                            List<String> bomRefIds = new ArrayList<>();
                            if (CollectionUtils.isNotEmpty(mtBomReferencePoints)) {
                                List<String> temp = mtBomReferencePoints.stream().map(t -> t.getBomReferencePointId())
                                        .collect(Collectors.toList());
                                bomRefIds.addAll(bomReferencePointIds.stream().filter(item -> !temp.contains(item))
                                        .collect(Collectors.toList()));
                            } else {
                                bomRefIds.addAll(bomReferencePointIds);
                            }

                            throw new MtException("MT_BOM_0043", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0043", "BOM", bomRefIds.toString(), "【API:bomAllUpdate】"));
                        }
                    }
                }

                // bomSubstituteGroup
                if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList())) {
                    List<String> bomSubstituteGroupIds = bomComponent.getMtBomSubstituteGroupList().stream().map(t -> {
                        return t.getBomSubstituteGroupId() == null ? "" : t.getBomSubstituteGroupId();
                    }).collect(Collectors.toList());

                    bomSubstituteGroupIds.removeAll(Arrays.asList(""));

                    List<MtBomSubstituteGroupVO4> needAdjustSubstituteGroups = new ArrayList<>();

                    if (CollectionUtils.isNotEmpty(bomSubstituteGroupIds)) {
                        List<MtBomSubstituteGroup> mtBomSubstituteGroups = mtBomSubstituteGroupMapper
                                .selectByComponentIdAndGroupIds(tenantId, bomComponentId, bomSubstituteGroupIds);
                        if (bomSubstituteGroupIds.size() != mtBomSubstituteGroups.size()) {
                            List<String> bomSubIds = new ArrayList<>();
                            if (CollectionUtils.isNotEmpty(mtBomSubstituteGroups)) {
                                List<String> temp = mtBomSubstituteGroups.stream().map(t -> t.getBomSubstituteGroupId())
                                        .collect(Collectors.toList());
                                bomSubIds.addAll(bomSubstituteGroupIds.stream().filter(item -> !temp.contains(item))
                                        .collect(Collectors.toList()));
                            } else {
                                bomSubIds.addAll(bomSubstituteGroupIds);
                            }
                            throw new MtException("MT_BOM_0020", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_BOM_0020", "BOM", bomSubIds.toString(), "【API:bomAllUpdate】"));
                        }

                        needAdjustSubstituteGroups = bomComponent.getMtBomSubstituteGroupList().stream()
                                .filter(t -> bomSubstituteGroupIds.contains(t.getBomSubstituteGroupId()))
                                .collect(Collectors.toList());
                    }

                    // 判断每一个bomSubstituteGroup下的属性层级关系
                    for (MtBomSubstituteGroupVO4 bomSubstituteGroup : needAdjustSubstituteGroups) {
                        String substituteGroupId = bomSubstituteGroup.getBomSubstituteGroupId();

                        if (CollectionUtils.isNotEmpty(bomSubstituteGroup.getMtBomSubstituteList())) {
                            List<String> bomSubstituteIds = bomSubstituteGroup.getMtBomSubstituteList().stream()
                                    .map(t -> {
                                        return t.getBomSubstituteId() == null ? "" : t.getBomSubstituteId();
                                    }).collect(Collectors.toList());

                            bomSubstituteIds.removeAll(Arrays.asList(""));

                            if (CollectionUtils.isNotEmpty(bomSubstituteIds)) {
                                List<MtBomSubstitute> mtBomSubstitutes = mtBomSubstituteMapper
                                        .selectByGroupIdAndSubstituteIds(tenantId, substituteGroupId, bomSubstituteIds);
                                if (bomSubstituteIds.size() != mtBomSubstitutes.size()) {
                                    List<String> bomSubIds = new ArrayList<>();
                                    if (CollectionUtils.isNotEmpty(mtBomSubstitutes)) {
                                        List<String> temp = mtBomSubstitutes.stream().map(t -> t.getBomSubstituteId())
                                                .collect(Collectors.toList());
                                        bomSubIds.addAll(bomSubstituteIds.stream().filter(item -> !temp.contains(item))
                                                .collect(Collectors.toList()));
                                    } else {
                                        bomSubIds.addAll(bomSubstituteIds);
                                    }
                                    throw new MtException("MT_BOM_0044",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0044",
                                                    "BOM", bomSubIds.toString(), "【API:bomAllUpdate】"));
                                }
                            }
                        }
                    }
                }
            }
        }

        // 获取Bom自动升版本策略
        String autoRevisionFlag = bomAutoRevisionGet(tenantId, dto.getBomId());

        // 3.1. Y：表示需要自动升版本，调用API【bomCopy】复制为新的装配清单
        if ("Y".equals(autoRevisionFlag)) {

            /*
             * 新加逻辑： 校验传入的参数中，BOM头属性的：bomName、bomType，和BOM行属性的materialId、 bomComponentType、
             * assembleMethod是否有变更 如果有变更，才执行 bomCopy 复制为新的装配清单
             */
            boolean isNeedBomCopy = false;

            // 验证bom头关键字段是否变更
            if (!dto.getBomName().equals(oldBom.getBomName()) || !dto.getBomType().equals(oldBom.getBomType())) {
                isNeedBomCopy = true;
            }

            // 如果bom头关键字段没有变更，则判断bom行关键字段是否有变更
            if (!isNeedBomCopy) {
                if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {
                    for (MtBomComponentVO7 bomComponent : dto.getMtBomComponentList()) {
                        if (StringUtils.isNotEmpty(bomComponent.getBomComponentId())) {
                            MtBomComponent oldBomComponent = new MtBomComponent();
                            oldBomComponent.setTenantId(tenantId);
                            oldBomComponent.setBomComponentId(bomComponent.getBomComponentId());
                            oldBomComponent = mtBomComponentMapper.selectOne(oldBomComponent);

                            // 为了保险起见，旧数据和传入参数为null时，认为""
                            String oldMethod = oldBomComponent.getAssembleMethod() == null ? ""
                                    : oldBomComponent.getAssembleMethod();
                            String method = bomComponent.getAssembleMethod() == null ? ""
                                    : bomComponent.getAssembleMethod();

                            if (!bomComponent.getMaterialId().equals(oldBomComponent.getMaterialId())
                                    || !bomComponent.getBomComponentType().equals(oldBomComponent.getBomComponentType())
                                    || !oldMethod.equals(method)) {
                                isNeedBomCopy = true;
                                break;
                            }
                        } else {
                            // 存在新增的组件行时,应该需要自动升版本
                            isNeedBomCopy = true;
                            break;
                        }
                    }
                }
            }

            // 记录实际处理的bom
            MtBom dealBom = new MtBom();
            boolean isCopy = false;

            if (isNeedBomCopy) {
                // 获取版本号
                MtBomVO4 bomVO4 = new MtBomVO4();
                bomVO4.setBomId(oldBom.getBomId());
                bomVO4.setRevision(oldBom.getRevision());
                String revision = bomRevisionGenerate(tenantId, bomVO4);

                // 复制 Bom 屬性赋值
                MtBomVO2 bomVO2 = new MtBomVO2();
                bomVO2.setBomId(oldBom.getBomId());
                bomVO2.setBomName(dto.getBomName());
                bomVO2.setRevision(revision);
                bomVO2.setBomType(oldBom.getBomType());
                String newBomId = bomCopy(tenantId, bomVO2);

                // 获取新复制的 Bom 数据
                MtBomVO7 newBom = bomBasicGet(tenantId, newBomId);
                BeanUtils.copyProperties(newBom, dealBom);

                isCopy = true;
            } else {
                // 不需要自动升版本
                dealBom = oldBom;
            }

            Date now = new Date();

            // 更新 Bom 输入的其他字段
            dealBom.setTenantId(tenantId);
            dealBom.setBomType(dto.getBomType());
            dealBom.setBomName(dto.getBomName());
            dealBom.setCurrentFlag(dto.getCurrentFlag());
            dealBom.setDateFrom(dto.getDateFrom());
            dealBom.setDateTo(dto.getDateTo());
            dealBom.setDescription(dto.getDescription());
            dealBom.setBomStatus(dto.getBomStatus());
            dealBom.setReleasedFlag(dto.getReleasedFlag());
            dealBom.setPrimaryQty(dto.getPrimaryQty());
            dealBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
            dealBom.setAssembleAsMaterialFlag(dto.getAssembleAsMaterialFlag());
            dealBom.setLastUpdateDate(now);
            self().updateByPrimaryKeySelective(dealBom);

            // 处理 Bom 下的组件
            if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {
                sqlList.addAll(
                        getBomComponentSqlList(tenantId, dealBom.getBomId(), dto.getMtBomComponentList(), now, isCopy));
                this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }

            // 没有执行 bomCopy
            if (!isNeedBomCopy) {
                MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
                mtBomHisVO1.setBomId(dto.getBomId());
                mtBomHisVO1.setEventTypeCode("BOM_ALL_CREATE");
                mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);
            }

            return dealBom.getBomId();
        }

        // N：表示不需要自动升版本，则直接更新数据并记录历史
        // 更新 Bom 输入的其他字段
        oldBom.setTenantId(tenantId);
        oldBom.setBomType(dto.getBomType());
        oldBom.setBomName(dto.getBomName());
        oldBom.setRevision(dto.getRevision());
        oldBom.setCurrentFlag(dto.getCurrentFlag());
        oldBom.setDateFrom(dto.getDateFrom());
        oldBom.setDateTo(dto.getDateTo());
        oldBom.setDescription(dto.getDescription());
        oldBom.setBomStatus(dto.getBomStatus());
        oldBom.setReleasedFlag(dto.getReleasedFlag());
        oldBom.setPrimaryQty(dto.getPrimaryQty());
        oldBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
        oldBom.setAssembleAsMaterialFlag(dto.getAssembleAsMaterialFlag());

        self().updateByPrimaryKeySelective(oldBom);

        Date now = new Date();

        // 处理 Bom 下的组件
        if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {
            sqlList.addAll(
                    getBomComponentSqlList(tenantId, oldBom.getBomId(), dto.getMtBomComponentList(), now, false));
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
        mtBomHisVO1.setBomId(dto.getBomId());
        mtBomHisVO1.setEventTypeCode("BOM_ALL_CREATE");
        mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);

        return oldBom.getBomId();
    }


    @Override
    public String bomLimitSourceBomGet(Long tenantId, String bomId) {
        MtBom bom = new MtBom();
        bom.setTenantId(tenantId);
        bom.setBomId(bomId);
        MtBom mtBom = mtBomMapper.selectOne(bom);
        if (null == mtBom) {
            return null;
        }
        return mtBom.getCopiedFromBomId();
    }

    /**
     * bomAttrPropertyUpdate-装配清单新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/11/13
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bomAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "keyId", "【API:bomAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtBom mtBom = mtBomMapper.selectByPrimaryKey(dto.getKeyId());
        if (mtBom == null || StringUtils.isEmpty(mtBom.getBomId())) {
            throw new MtException("MT_BOM_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0069", "BOM", "keyId:" + dto.getKeyId(), "mt_bom", "【API:bomAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_bom_attr", dto.getKeyId(), dto.getEventId(),
                dto.getAttrs());
    }

    /**
     * 获取BomComponent 及其 下面的属性的拼接sql
     *
     * @param tenantId
     * @param bomId
     * @param bomComponentList
     * @param now
     * @return java.util.List<java.lang.String>
     * @author chuang.yang
     * @date 2019/3/16
     */
    public List<String> getBomComponentSqlList(Long tenantId, String bomId, List<MtBomComponentVO7> bomComponentList,
                                               Date now, boolean isCopy) {
        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails() == null ? -1 : DetailsHelper.getUserDetails().getUserId();

        for (MtBomComponentVO7 bomComponent : bomComponentList) {
            if (StringUtils.isEmpty(bomComponent.getBomComponentId())) {
                // 新增 BomComponent 以及属性信息
                sqlList.addAll(mtBomComponentRepository.bomComponentDetailInsert(tenantId, bomId,
                        Arrays.asList(bomComponent), now));

            } else {
                // 获取 新Bom下，复制出来的该Bom组件的数据
                MtBomComponent newBomComponent = new MtBomComponent();
                newBomComponent.setTenantId(tenantId);
                newBomComponent.setBomId(bomId);
                if (isCopy) {
                    newBomComponent.setCopiedFromComponentId(bomComponent.getBomComponentId());
                } else {
                    newBomComponent.setBomComponentId(bomComponent.getBomComponentId());
                }
                newBomComponent = mtBomComponentMapper.selectOne(newBomComponent);

                // 更新输入的其他字段
                newBomComponent.setLineNumber(bomComponent.getLineNumber());
                newBomComponent.setMaterialId(bomComponent.getMaterialId());
                newBomComponent.setBomComponentType(bomComponent.getBomComponentType());
                newBomComponent.setDateFrom(bomComponent.getDateFrom());
                newBomComponent.setDateTo(bomComponent.getDateTo());
                newBomComponent.setQty(bomComponent.getQty());
                newBomComponent.setKeyMaterialFlag(bomComponent.getKeyMaterialFlag());
                // newBomComponent.setAssembleAsMaterialFlag(bomComponent.getAssembleAsMaterialFlag());
                newBomComponent.setAssembleAsReqFlag(bomComponent.getAssembleAsReqFlag());
                newBomComponent.setAssembleMethod(bomComponent.getAssembleMethod());
                newBomComponent.setAttritionPolicy(bomComponent.getAttritionPolicy());
                newBomComponent.setAttritionChance(bomComponent.getAttritionChance());
                newBomComponent.setAttritionQty(bomComponent.getAttritionQty());
                newBomComponent.setIssuedLocatorId(bomComponent.getIssuedLocatorId());
                newBomComponent.setLastUpdateDate(now);
                newBomComponent.setCid(Long.valueOf(customSequence.getNextKey("mt_bom_component_cid_s")));
                sqlList.addAll(MtSqlHelper.getUpdateSql(newBomComponent));

                if (CollectionUtils.isNotEmpty(bomComponent.getMtBomReferencePointList())) {
                    for (MtBomReferencePointVO9 bomReferencePoint : bomComponent.getMtBomReferencePointList()) {
                        MtBomReferencePoint newBomReferencePoint = new MtBomReferencePoint();

                        if (StringUtils.isEmpty(bomReferencePoint.getBomReferencePointId())) {
                            // 如果没有传入 BomReferencePointId，则对该 BomReferencePoint
                            // 执行新增
                            newBomReferencePoint.setTenantId(tenantId);
                            newBomReferencePoint
                                    .setBomReferencePointId(customSequence.getNextKey("mt_bom_reference_point_s"));
                            newBomReferencePoint.setBomComponentId(newBomComponent.getBomComponentId());
                            newBomReferencePoint.setReferencePoint(bomReferencePoint.getReferencePoint());
                            newBomReferencePoint.setQty(bomReferencePoint.getQty());
                            newBomReferencePoint.setLineNumber(bomReferencePoint.getLineNumber());
                            newBomReferencePoint.setEnableFlag(bomReferencePoint.getEnableFlag());
                            newBomReferencePoint.setCreatedBy(userId);
                            newBomReferencePoint.setCreationDate(now);
                            newBomReferencePoint.setLastUpdatedBy(userId);
                            newBomReferencePoint.setLastUpdateDate(now);
                            newBomReferencePoint.setObjectVersionNumber(1L);
                            newBomReferencePoint
                                    .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                            sqlList.addAll(MtSqlHelper.getInsertSql(newBomReferencePoint));

                        } else {
                            // 在新复制出来的BOM参考点属性行中, 获取复制的新的 BomReferencePoint 数据
                            newBomReferencePoint.setTenantId(tenantId);
                            newBomReferencePoint.setBomComponentId(newBomComponent.getBomComponentId());
                            if (isCopy) {
                                newBomReferencePoint.setCopiedFromPointId(bomReferencePoint.getBomReferencePointId());
                            } else {
                                newBomReferencePoint.setBomReferencePointId(bomReferencePoint.getBomReferencePointId());
                            }
                            newBomReferencePoint = mtBomReferencePointMapper.selectOne(newBomReferencePoint);

                            // 更新输入的其他字段
                            newBomReferencePoint.setReferencePoint(bomReferencePoint.getReferencePoint());
                            newBomReferencePoint.setQty(bomReferencePoint.getQty());
                            newBomReferencePoint.setLineNumber(bomReferencePoint.getLineNumber());
                            newBomReferencePoint.setEnableFlag(bomReferencePoint.getEnableFlag());
                            newBomReferencePoint.setLastUpdateDate(now);
                            newBomReferencePoint
                                    .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                            sqlList.addAll(MtSqlHelper.getUpdateSql(newBomReferencePoint));
                        }
                    }

                }
                if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList())) {
                    for (MtBomSubstituteGroupVO4 bomSubstituteGroup : bomComponent.getMtBomSubstituteGroupList()) {
                        MtBomSubstituteGroup newBomSubstituteGroup = new MtBomSubstituteGroup();

                        if (StringUtils.isEmpty(bomSubstituteGroup.getBomSubstituteGroupId())) {
                            // 如果没有传入 BomSubstituteGroupId，则对该
                            // BomSubstituteGroup 执行新增
                            String newBomSubstituteGroupId = customSequence.getNextKey("mt_bom_substitute_group_s");
                            newBomSubstituteGroup.setTenantId(tenantId);
                            newBomSubstituteGroup.setBomSubstituteGroupId(newBomSubstituteGroupId);
                            newBomSubstituteGroup.setBomComponentId(newBomComponent.getBomComponentId());
                            newBomSubstituteGroup.setSubstituteGroup(bomSubstituteGroup.getSubstituteGroup());
                            newBomSubstituteGroup.setSubstitutePolicy(bomSubstituteGroup.getSubstitutePolicy());
                            newBomSubstituteGroup.setEnableFlag(bomSubstituteGroup.getEnableFlag());
                            newBomSubstituteGroup.setCreatedBy(userId);
                            newBomSubstituteGroup.setCreationDate(now);
                            newBomSubstituteGroup.setLastUpdatedBy(userId);
                            newBomSubstituteGroup.setLastUpdateDate(now);
                            newBomSubstituteGroup.setObjectVersionNumber(1L);
                            newBomSubstituteGroup
                                    .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                            sqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstituteGroup));

                        } else {
                            // 在新复制出来的BOM替代组属性行中, 获取复制的新的 BomSubstituteGroup 数据
                            newBomSubstituteGroup.setTenantId(tenantId);
                            newBomSubstituteGroup.setBomComponentId(newBomComponent.getBomComponentId());
                            if (isCopy) {
                                newBomSubstituteGroup
                                        .setCopiedFromGroupId(bomSubstituteGroup.getBomSubstituteGroupId());
                            } else {
                                newBomSubstituteGroup
                                        .setBomSubstituteGroupId(bomSubstituteGroup.getBomSubstituteGroupId());
                            }
                            newBomSubstituteGroup = mtBomSubstituteGroupMapper.selectOne(newBomSubstituteGroup);

                            // 更新输入的其他字段
                            newBomSubstituteGroup.setSubstituteGroup(bomSubstituteGroup.getSubstituteGroup());
                            newBomSubstituteGroup.setSubstitutePolicy(bomSubstituteGroup.getSubstitutePolicy());
                            newBomSubstituteGroup.setEnableFlag(bomSubstituteGroup.getEnableFlag());
                            newBomSubstituteGroup.setLastUpdateDate(now);
                            newBomSubstituteGroup
                                    .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                            sqlList.addAll(MtSqlHelper.getUpdateSql(newBomSubstituteGroup));
                        }

                        if (CollectionUtils.isNotEmpty(bomSubstituteGroup.getMtBomSubstituteList())) {
                            for (MtBomSubstituteVO8 bomSubstitute : bomSubstituteGroup.getMtBomSubstituteList()) {
                                MtBomSubstitute newBomSubstitute = new MtBomSubstitute();
                                if (StringUtils.isEmpty(bomSubstitute.getBomSubstituteId())) {
                                    // 新增组件替代属性
                                    newBomSubstitute.setTenantId(tenantId);
                                    newBomSubstitute
                                            .setBomSubstituteId(customSequence.getNextKey("mt_bom_substitute_s"));
                                    newBomSubstitute
                                            .setBomSubstituteGroupId(newBomSubstituteGroup.getBomSubstituteGroupId());
                                    newBomSubstitute.setMaterialId(bomSubstitute.getMaterialId());
                                    newBomSubstitute.setDateFrom(bomSubstitute.getDateFrom());
                                    newBomSubstitute.setDateTo(bomSubstitute.getDateTo());
                                    newBomSubstitute.setSubstituteValue(bomSubstitute.getSubstituteValue());
                                    newBomSubstitute.setSubstituteUsage(bomSubstitute.getSubstituteUsage());
                                    newBomSubstitute.setCreatedBy(userId);
                                    newBomSubstitute.setCreationDate(now);
                                    newBomSubstitute.setLastUpdatedBy(userId);
                                    newBomSubstitute.setLastUpdateDate(now);
                                    newBomSubstitute.setObjectVersionNumber(1L);
                                    newBomSubstitute
                                            .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_substitute_cid_s")));
                                    sqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstitute));
                                } else {
                                    // 在新复制出来的BOM替代项属性行中, 获取复制的新的 BomSubstitute
                                    // 数据
                                    newBomSubstitute.setTenantId(tenantId);
                                    newBomSubstitute
                                            .setBomSubstituteGroupId(newBomSubstituteGroup.getBomSubstituteGroupId());
                                    if (isCopy) {
                                        newBomSubstitute.setCopiedFromSubstituteId(bomSubstitute.getBomSubstituteId());
                                    } else {
                                        newBomSubstitute.setBomSubstituteId(bomSubstitute.getBomSubstituteId());
                                    }

                                    newBomSubstitute = mtBomSubstituteMapper.selectOne(newBomSubstitute);

                                    // 更新输入的其他字段
                                    newBomSubstitute.setTenantId(tenantId);
                                    newBomSubstitute.setMaterialId(bomSubstitute.getMaterialId());
                                    newBomSubstitute.setDateFrom(bomSubstitute.getDateFrom());
                                    newBomSubstitute.setDateTo(bomSubstitute.getDateTo());
                                    newBomSubstitute.setSubstituteValue(bomSubstitute.getSubstituteValue());
                                    newBomSubstitute.setSubstituteUsage(bomSubstitute.getSubstituteUsage());
                                    newBomSubstitute.setLastUpdateDate(now);
                                    newBomSubstitute
                                            .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_substitute_cid_s")));
                                    sqlList.addAll(MtSqlHelper.getUpdateSql(newBomSubstitute));
                                }
                            }
                        }
                    }
                }
            }
        }
        return sqlList;
    }


    @Override
    public List<MtBom> bomLimitBomNameQuery(Long tenantId, List<String> bomNames) {
        if (CollectionUtils.isEmpty(bomNames)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomName", "【API:bomLimitBomNameQuery】"));
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t1.BOM_NAME", bomNames, 1000);
        return this.mtBomMapper.selectByBomNameCustom(tenantId, whereInValuesSql);
    }

    @Override
    public List<MtBom> bomLimitBomIdQuery(Long tenantId, List<String> bomIds) {
        if (CollectionUtils.isEmpty(bomIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomLimitBomIdQuery】"));
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t1.BOM_ID", bomIds, 1000);
        return this.mtBomMapper.selectByBomIds(tenantId, whereInValuesSql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> bomAllBatchUpdate(Long tenantId, List<MtBomVO11> bomList) {
        // bom数据
        List<String> bomIds = bomList.stream().filter(t -> StringUtils.isNotEmpty(t.getBomId()))
                .map(MtBomVO11::getBomId).distinct().collect(Collectors.toList());
        Map<String, MtBom> mtBomVO7Map = null;
        Map<BomIdBomComId, MtBomComponent> mtBomComponentMap = null;
        Map<String, List<MtBomComponent>> bomComponentMap = null;
        Map<BomSiteAssign, MtBomSiteAssign> siteAssignMap = null;
        if (CollectionUtils.isNotEmpty(bomIds)) {
            String bomIdSql = StringHelper.getWhereInValuesSql("t1.BOM_ID", bomIds, 1000);
            List<MtBom> mtBomVO7s = mtBomMapper.selectByBomIds(tenantId, bomIdSql);
            if (CollectionUtils.isNotEmpty(mtBomVO7s)) {
                mtBomVO7Map = mtBomVO7s.stream().collect(Collectors.toMap(MtBom::getBomId, c -> c));
            }

            // bomComponent数据
            String whereInValuesSql = StringHelper.getWhereInValuesSql("BOM_ID", bomIds, 1000);
            List<MtBomComponent> mtBomComponents = mtBomComponentMapper.selectBomComponentByBomIds(tenantId, whereInValuesSql);
            if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                mtBomComponentMap = mtBomComponents.stream().collect(
                        Collectors.toMap(c -> new BomIdBomComId(c.getBomId(), c.getBomComponentId()), c -> c));
                bomComponentMap = mtBomComponents.stream().collect(Collectors.groupingBy(MtBomComponent::getBomId));
            }

            // bomSiteAssign数据
            List<MtBomSiteAssign> mtBomSiteAssigns =
                    mtBomSiteAssignRepository.bomLimitBomSiteAssignBatchQuery(tenantId, bomIds);
            if (CollectionUtils.isNotEmpty(mtBomSiteAssigns)) {
                siteAssignMap = mtBomSiteAssigns.stream()
                        .collect(Collectors.toMap(c -> new BomSiteAssign(c.getSiteId(), c.getBomId()), c -> c));
            }
        }

        // manufacturing数据
        List<String> materialSiteId = bomList.stream()
                .filter(c -> "MATERIAL".equalsIgnoreCase(c.getBomObjectType())
                        && StringUtils.isNotEmpty(c.getMaterialSiteId()))
                .map(MtBomVO11::getMaterialSiteId).distinct().collect(Collectors.toList());
        Long materialCount = bomList.stream().filter(c -> "MATERIAL".equalsIgnoreCase(c.getBomObjectType())
                && StringUtils.isNotEmpty(c.getMaterialSiteId())).count();
        Long woCount = bomList.stream()
                .filter(c -> "WO".equalsIgnoreCase(c.getBomObjectType()) && c.getWorkOrder() != null).count();

        Map<String, MtPfepManufacturing> pfepManufacturingMap = null;
        if (CollectionUtils.isNotEmpty(materialSiteId)) {
            List<MtPfepManufacturing> mtPfepManufacturings = mtPfepManufacturingRepository
                    .selectpfepManufacturingByMaterialSiteId(tenantId, materialSiteId);
            pfepManufacturingMap = mtPfepManufacturings.stream()
                    .collect(Collectors.toMap(MtPfepManufacturing::getMaterialSiteId, c -> c));
        }

        // bom类型
        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("BOM");
        mtGenTypeVO2.setTypeGroup("BOM_TYPE");
        List<MtGenType> bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> bomType = bomTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());

        // bomComponent类型
        mtGenTypeVO2.setTypeGroup("BOM_COMPONENT_TYPE");
        List<MtGenType> bomComTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> bomComType = bomComTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());

        // 装配类型
        mtGenTypeVO2.setModule("MATERIAL");
        mtGenTypeVO2.setTypeGroup("ASSY_METHOD");
        List<MtGenType> materialTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> materialType = materialTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());

        // 损耗类型
        mtGenTypeVO2.setModule("BOM");
        mtGenTypeVO2.setTypeGroup("ATTRITION_POLICY");
        List<MtGenType> attritionPolicyTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> attritionPolicyType =
                attritionPolicyTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());

        // bom状态
        MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("BOM");
        mtGenStatusVO2.setStatusGroup("BOM_STATUS");
        List<MtGenStatus> bomStatuses = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
        List<String> bomStatus = bomStatuses.stream().map(MtGenStatus::getStatusCode).collect(Collectors.toList());

        // 获取组件所有物料
        List<List<MtBomComponentVO7>> bomComponentList =
                bomList.stream().filter(t -> CollectionUtils.isNotEmpty(t.getMtBomComponentList()))
                        .map(MtBomVO11::getMtBomComponentList).collect(Collectors.toList());
        List<String> materialId = new ArrayList<>();
        List<String> issuedLocatorId = new ArrayList<>();
        AtomicInteger bomComCount = new AtomicInteger();
        bomComponentList.stream().forEach(c -> {
            materialId.addAll(c.stream().filter(t -> StringUtils.isNotEmpty(t.getMaterialId()))
                    .map(MtBomComponentVO7::getMaterialId).distinct().collect(Collectors.toList()));
            issuedLocatorId.addAll(c.stream().filter(t -> StringUtils.isNotEmpty(t.getIssuedLocatorId()))
                    .map(MtBomComponentVO7::getIssuedLocatorId).distinct().collect(Collectors.toList()));
            bomComCount.addAndGet(c.size());
        });
        Map<String, MtMaterialVO> mtMaterialVOMap = null;
        if (CollectionUtils.isNotEmpty(materialId)) {
            List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialBasicInfoBatchGet(tenantId, materialId);
            if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                mtMaterialVOS = mtMaterialVOS.stream()
                        .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                    mtMaterialVOMap =
                            mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, c -> c));
                }
            }
        }

        // 发料库位信息
        Map<String, MtModLocator> mtModLocatorMap = null;
        if (CollectionUtils.isNotEmpty(issuedLocatorId)) {
            List<MtModLocator> mtModLocators =
                    mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, issuedLocatorId);
            if (CollectionUtils.isNotEmpty(mtModLocators)) {
                mtModLocators = mtModLocators.stream()
                        .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtModLocators)) {
                    mtModLocatorMap = mtModLocators.stream()
                            .collect(Collectors.toMap(MtModLocator::getLocatorId, c -> c));
                }
            }
        }

        // 共有变量
        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> newBomList = new ArrayList<>(bomList.size());
        List<AuditDomain> newBomHisList = new ArrayList<>(bomList.size());
        List<AuditDomain> newBomComList = new ArrayList<>(bomComCount.get());
        List<AuditDomain> newBomComHisList = new ArrayList<>(bomComCount.get());

        List<AuditDomain> newBomSiteAssignList = new ArrayList<>(bomList.size());
        List<AuditDomain> newWoList = new ArrayList<>(woCount.intValue());
        List<AuditDomain> newWoHisList = new ArrayList<>(woCount.intValue());
        List<AuditDomain> newPefpList = new ArrayList<>(woCount.intValue());

        Long userId = -1L;
        Date now = new Date();
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("BOM_ALL_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 批量获取id，cid
        List<String> newBomIds = this.customDbRepository.getNextKeys("mt_bom_s", bomList.size() - bomIds.size());
        List<String> newBomCids = this.customDbRepository.getNextKeys("mt_bom_cid_s", bomList.size());
        List<String> newBomHisIds = this.customDbRepository.getNextKeys("mt_bom_his_s", bomList.size());
        List<String> newBomHisCids = this.customDbRepository.getNextKeys("mt_bom_his_cid_s", bomList.size());

        List<String> newBomComIds = this.customDbRepository.getNextKeys("mt_bom_component_s", bomComCount.get());
        List<String> newBomComCids = this.customDbRepository.getNextKeys("mt_bom_component_cid_s", bomComCount.get());
        List<String> newBomComHisIds = this.customDbRepository.getNextKeys("mt_bom_component_his_s", bomComCount.get());
        List<String> newBomComHisCids =
                this.customDbRepository.getNextKeys("mt_bom_component_his_cid_s", bomComCount.get());

        List<String> newBomSiteAssgnIds =
                this.customDbRepository.getNextKeys("mt_bom_site_assign_s", bomList.size() - bomIds.size());
        List<String> newBomSiteAssgnCids =
                this.customDbRepository.getNextKeys("mt_bom_site_assign_cid_s", bomList.size());

        List<String> newPfepManufacturingIds =
                this.customDbRepository.getNextKeys("mt_pfep_manufacturing_s", materialCount.intValue());
        List<String> newPfepManufacturingCids =
                this.customDbRepository.getNextKeys("mt_pfep_manufacturing_cid_s", materialCount.intValue());

        List<String> newWorkOrderCids = this.customDbRepository.getNextKeys("mt_work_order_cid_s", woCount.intValue());
        List<String> newWorkOrderHisIds =
                this.customDbRepository.getNextKeys("mt_work_order_his_s", woCount.intValue());
        List<String> newWorkOrderHisCids =
                this.customDbRepository.getNextKeys("mt_work_order_his_cid_s", woCount.intValue());
        MtBom mtBom;
        MtBomHis mtBomHis;
        BomIdBomComId bomIdBomComId;
        BomSiteAssign bomSiteAssign;
        MtBomSiteAssign mtBomSiteAssign;
        MtBomComponent mtBomComponent;
        MtBomComponentHis mtBomComponentHis;
        MtBomComponentIface headBomIface;
        MtWorkOrder mtWorkOrder;
        MtWorkOrderHis mtWorkOrderHis;
        MtPfepManufacturing mtPfepManufacturing;
        Map<String, String> bomAttrMap = new HashMap<>(bomList.size());
        Map<String, String> bomComAttrMap = new HashMap<>(bomComCount.get());
        List<String> result = new ArrayList<>(bomList.size());
        Map<MtBomComponentIface, String> bomAttr = new HashMap<>(bomList.size());
//        Map<MtBomComponentIface, List<String>> bomComponentAttr = new HashMap<>(bomList.size());
//        List<String> bomComponentIds;
        Map<MtBomComponentIface, String> bomComponentAttr = new HashMap<>(bomList.size());
        Long start = System.currentTimeMillis();
        for (MtBomVO11 dto : bomList) {
            Boolean flag = false;
            Boolean allFlag = false;
            if (StringUtils.isNotEmpty(dto.getBomId()) && ALL.equalsIgnoreCase(dto.getUpdateMethod())) {
                allFlag = true;
            }
            if (StringUtils.isEmpty(dto.getBomId())) {
                flag = true;
            }
            // 头数据参数校验校验
            if (!UPDATE_METHOD.contains(dto.getUpdateMethod())) {
                throw new MtException("MT_BOM_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0071", "BOM", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && StringUtils.isEmpty(dto.getBomName())
                    || !(flag || allFlag) && "".equals(dto.getBomName())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomName", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && StringUtils.isEmpty(dto.getBomType())
                    || !(flag || allFlag) && "".equals(dto.getBomType())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomType", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && StringUtils.isEmpty(dto.getRevision())
                    || !(flag || allFlag) && "".equals(dto.getRevision())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "revision", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && dto.getDateFrom() == null) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "dateFrom", "【API:bomAllBatchUpdate】"));
            }

            // 验证currentFlag有效性
            if (StringUtils.isNotEmpty(dto.getCurrentFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getCurrentFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "currentFlag", "【API:bomAllBatchUpdate】"));
            }

            // 验证assembleAsMaterialFlag有效性
            if (StringUtils.isNotEmpty(dto.getAssembleAsMaterialFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getAssembleAsMaterialFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "assembleAsMaterialFlag", "【API:bomAllBatchUpdate】"));
            }

            // 验证 releasedFlag 有效性
            if (StringUtils.isNotEmpty(dto.getReleasedFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getReleasedFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "releasedFlag", "【API:bomAllBatchUpdate】"));
            }

            // 验证 autoRevisionFlag 有效性
            if (StringUtils.isNotEmpty(dto.getAutoRevisionFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getAutoRevisionFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "autoRevisionFlag", "【API:bomAllBatchUpdate】"));
            }

            // 校验类型与状态
            if (StringUtils.isNotEmpty(dto.getBomType())
                    && (CollectionUtils.isEmpty(bomType) || !bomType.contains(dto.getBomType()))) {
                throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0056", "BOM", "bomType:" + dto.getBomType(), "【API:bomAllBatchUpdate】"));
            }
            if (StringUtils.isNotEmpty(dto.getBomStatus())
                    && (CollectionUtils.isEmpty(bomStatus) || !bomStatus.contains(dto.getBomStatus()))) {
                throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0056", "BOM", "bomStatus:" + dto.getBomStatus(), "【API:bomAllBatchUpdate】"));
            }

            // 新增bom
            if (flag) {
                mtBom = new MtBom();
                mtBom.setBomId(newBomIds.remove(0));
                mtBom.setCreatedBy(userId);
                mtBom.setCreationDate(now);
                mtBom.setObjectVersionNumber(1L);

            } else {
                // 判断bomId值的有效性
                if (MapUtils.isEmpty(mtBomVO7Map) || mtBomVO7Map.get(dto.getBomId()) == null) {
                    throw new MtException("MT_BOM_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0045", "BOM", dto.getBomId(), "【API:bomAllBatchUpdate】"));
                }
                mtBom = mtBomVO7Map.get(dto.getBomId());
                mtBom.setObjectVersionNumber(mtBom.getObjectVersionNumber() + 1L);
            }
            Map<String, String> tempTlMap = new HashMap<>(1);
            if (StringUtils.isNotEmpty(dto.getDescription())) {
                tempTlMap.put("description", dto.getDescription());
            }
            mtBom.setTenantId(tenantId);
            if (StringUtils.isNotEmpty(dto.getBomName())) {
                mtBom.setBomName(dto.getBomName());
            }
            if (StringUtils.isNotEmpty(dto.getRevision())) {
                mtBom.setRevision(dto.getRevision());
            }
            if (StringUtils.isNotEmpty(dto.getBomType())) {
                mtBom.setBomType(dto.getBomType());
            }
            if (StringUtils.isNotEmpty(dto.getCurrentFlag())) {
                mtBom.setCurrentFlag(dto.getCurrentFlag());
            }

            if (StringUtils.isNotEmpty(dto.getDescription())) {
                mtBom.setDescription(dto.getDescription());
            }
            if (StringUtils.isNotEmpty(dto.getBomStatus())) {
                mtBom.setBomStatus(dto.getBomStatus());
            }
            if (StringUtils.isNotEmpty(dto.getReleasedFlag())) {
                mtBom.setReleasedFlag(dto.getReleasedFlag());
            }
            if (StringUtils.isNotEmpty(dto.getAutoRevisionFlag())) {
                mtBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
            }
            mtBom.setDateFrom(dto.getDateFrom());
            mtBom.setDateTo(dto.getDateTo());
            mtBom.setPrimaryQty(dto.getPrimaryQty());
            if (MapUtils.isNotEmpty(tempTlMap)) {
                mtBom.set_tls(getTlsMap(tempTlMap));
            }
            mtBom.setCid(Long.valueOf(newBomCids.remove(0)));
            mtBom.setLastUpdatedBy(userId);
            mtBom.setLastUpdateDate(now);

            // 记录历史
            mtBomHis = new MtBomHis();
            mtBomHis.setTenantId(tenantId);
            mtBomHis.setBomHisId(newBomHisIds.remove(0));
            mtBomHis.setBomId(mtBom.getBomId());
            mtBomHis.setBomName(mtBom.getBomName());
            mtBomHis.setRevision(mtBom.getRevision());
            mtBomHis.setBomType(mtBom.getBomType());
            mtBomHis.setDateFrom(mtBom.getDateFrom());
            mtBomHis.setDateTo(mtBom.getDateTo());
            mtBomHis.setCurrentFlag(mtBom.getCurrentFlag());
            mtBomHis.setDescription(mtBom.getDescription());
            mtBomHis.setBomStatus(mtBom.getBomStatus());
            mtBomHis.setCopiedFromBomId(mtBom.getCopiedFromBomId());
            mtBomHis.setReleasedFlag(mtBom.getReleasedFlag());
            mtBomHis.setPrimaryQty(mtBom.getPrimaryQty());
            mtBomHis.setAutoRevisionFlag(mtBom.getAutoRevisionFlag());
            mtBomHis.setAssembleAsMaterialFlag(mtBom.getAssembleAsMaterialFlag());
            mtBomHis.setEventId(eventId);
            mtBomHis.setCid(Long.valueOf(newBomHisCids.remove(0)));
            mtBomHis.setCreatedBy(userId);
            mtBomHis.setCreationDate(now);
            mtBomHis.setObjectVersionNumber(1L);
            mtBomHis.setLastUpdatedBy(userId);
            mtBomHis.setLastUpdateDate(now);
            mtBom.setLatestHisId(mtBomHis.getBomHisId());

            newBomList.add(mtBom);
            newBomHisList.add(mtBomHis);

            // 扩展历史表数据
            bomAttrMap.put(mtBomHis.getBomId(), mtBomHis.getBomHisId());

            // 全量更新是失效原有组件
            if (allFlag && MapUtils.isNotEmpty(bomComponentMap)
                    && CollectionUtils.isNotEmpty(bomComponentMap.get(mtBom.getBomId()))) {
                bomComponentMap.get(mtBom.getBomId()).stream().forEach(c -> {
                    c.setDateTo(now);
                });
                newBomComList.addAll(bomComponentMap.get(mtBom.getBomId()));
            }

            // bomSiteAssign
            if (dto.getBomComponentIface() != null) {
                bomSiteAssign = new BomSiteAssign(dto.getSiteId(), mtBom.getBomId());
                if (MapUtils.isNotEmpty(siteAssignMap) && siteAssignMap.get(bomSiteAssign) != null) {
                    mtBomSiteAssign = siteAssignMap.get(bomSiteAssign);

                } else {
                    mtBomSiteAssign = new MtBomSiteAssign();
                    mtBomSiteAssign.setBomId(mtBom.getBomId());
                    mtBomSiteAssign.setSiteId(dto.getSiteId());
                    mtBomSiteAssign.setAssignId(newBomSiteAssgnIds.remove(0));
                    mtBomSiteAssign.setCreatedBy(userId);
                    mtBomSiteAssign.setCreationDate(now);
                    mtBomSiteAssign.setObjectVersionNumber(1L);
                }
                headBomIface = dto.getBomComponentIface();
                boolean bomEndDateCheck = headBomIface.getBomEndDate() == null;
                boolean bomStatusCheck = StringUtils.isEmpty(headBomIface.getBomStatus())
                        || "ACTIVE".equals(headBomIface.getBomStatus());
                boolean enableFlagCheck = StringUtils.isEmpty(headBomIface.getEnableFlag())
                        || "Y".equals(headBomIface.getEnableFlag());
                mtBomSiteAssign.setCid(Long.valueOf(newBomSiteAssgnCids.remove(0)));
                mtBomSiteAssign.setEnableFlag(bomEndDateCheck && bomStatusCheck && enableFlagCheck ? "Y" : "N");
                mtBomSiteAssign.setTenantId(tenantId);
                mtBomSiteAssign.setLastUpdatedBy(userId);
                mtBomSiteAssign.setLastUpdateDate(now);
                newBomSiteAssignList.add(mtBomSiteAssign);
            }

            if ("WO".equalsIgnoreCase(dto.getBomObjectType()) && dto.getWorkOrder() != null) {
                mtWorkOrder = dto.getWorkOrder();
                mtWorkOrder.setBomId(mtBom.getBomId());
                mtWorkOrder.setValidateFlag(MtBaseConstants.NO);
                mtWorkOrder.setTenantId(tenantId);
                mtWorkOrder.setCid(Long.valueOf(newWorkOrderCids.remove(0)));
                mtWorkOrder.setLastUpdatedBy(userId);
                mtWorkOrder.setLastUpdateDate(now);
                mtWorkOrder.setObjectVersionNumber(mtWorkOrder.getObjectVersionNumber() + 1L);

                mtWorkOrderHis = new MtWorkOrderHis();
                mtWorkOrderHis.setTenantId(tenantId);
                mtWorkOrderHis.setWorkOrderHisId(newWorkOrderHisIds.remove(0));
                mtWorkOrderHis.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderHis.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                mtWorkOrderHis.setWorkOrderType(mtWorkOrder.getWorkOrderType());
                mtWorkOrderHis.setSiteId(mtWorkOrder.getSiteId());
                mtWorkOrderHis.setProductionLineId(mtWorkOrder.getProductionLineId());
                mtWorkOrderHis.setWorkcellId(mtWorkOrder.getWorkcellId());
                mtWorkOrderHis.setMakeOrderId(mtWorkOrder.getMakeOrderId());
                mtWorkOrderHis.setProductionVersion(mtWorkOrder.getProductionVersion());
                mtWorkOrderHis.setMaterialId(mtWorkOrder.getMaterialId());
                mtWorkOrderHis.setQty(mtWorkOrder.getQty());
                mtWorkOrderHis.setUomId(mtWorkOrder.getUomId());
                mtWorkOrderHis.setPriority(mtWorkOrder.getPriority());
                mtWorkOrderHis.setStatus(mtWorkOrder.getStatus());
                mtWorkOrderHis.setLastWoStatus(mtWorkOrder.getLastWoStatus());
                mtWorkOrderHis.setPlanStartTime(mtWorkOrder.getPlanStartTime());
                mtWorkOrderHis.setPlanEndTime(mtWorkOrder.getPlanEndTime());
                mtWorkOrderHis.setLocatorId(mtWorkOrder.getLocatorId());
                mtWorkOrderHis.setBomId(mtWorkOrder.getBomId());
                mtWorkOrderHis.setRouterId(mtWorkOrder.getRouterId());
                mtWorkOrderHis.setValidateFlag(mtWorkOrder.getValidateFlag());
                mtWorkOrderHis.setRemark(mtWorkOrder.getRemark());
                mtWorkOrderHis.setOpportunityId(mtWorkOrder.getOpportunityId());
                mtWorkOrderHis.setCustomerId(mtWorkOrder.getCustomerId());
                mtWorkOrderHis.setCompleteControlType(mtWorkOrder.getCompleteControlType());
                mtWorkOrderHis.setCompleteControlQty(mtWorkOrder.getCompleteControlQty());
                mtWorkOrderHis.setEventId(dto.getEventId());
                mtWorkOrderHis.setTrxQty(0.0D);
                mtWorkOrderHis.setSourceIdentificationId(mtWorkOrder.getSourceIdentificationId());
                mtWorkOrderHis.setCid(Long.valueOf(newWorkOrderHisCids.remove(0)));
                mtWorkOrderHis.setCreationDate(now);
                mtWorkOrderHis.setCreatedBy(userId);
                mtWorkOrderHis.setLastUpdateDate(now);
                mtWorkOrderHis.setLastUpdatedBy(userId);
                mtWorkOrderHis.setObjectVersionNumber(1L);
                mtWorkOrder.setLatestHisId(mtWorkOrderHis.getWorkOrderHisId());
                newWoList.add(mtWorkOrder);
                newWoHisList.add(mtWorkOrderHis);
            }
            if ("MATERIAL".equalsIgnoreCase(dto.getBomObjectType())) {
                if (MapUtils.isNotEmpty(pfepManufacturingMap)
                        && pfepManufacturingMap.get(dto.getMaterialSiteId()) != null) {
                    mtPfepManufacturing = pfepManufacturingMap.get(dto.getMaterialSiteId());
                    mtPfepManufacturing.setObjectVersionNumber(mtPfepManufacturing.getObjectVersionNumber() + 1L);
                } else {
                    mtPfepManufacturing = new MtPfepManufacturing();
                    mtPfepManufacturing.setPfepManufacturingId(newPfepManufacturingIds.remove(0));
                    mtPfepManufacturing.setEnableFlag("Y");
                    mtPfepManufacturing.setCreatedBy(userId);
                    mtPfepManufacturing.setCreationDate(now);
                    mtPfepManufacturing.setObjectVersionNumber(1L);
                }
                mtPfepManufacturing.setTenantId(tenantId);
                mtPfepManufacturing.setMaterialSiteId(dto.getMaterialSiteId());
                mtPfepManufacturing.setDefaultBomId(mtBom.getBomId());
                mtPfepManufacturing.setLastUpdatedBy(userId);
                mtPfepManufacturing.setLastUpdateDate(now);
                mtPfepManufacturing.setCid(Long.valueOf(newPfepManufacturingCids.remove(0)));
                newPefpList.add(mtPfepManufacturing);
            }
            result.add(mtBom.getBomId());

            // bom扩展属性
            if (dto.getBomComponentIface() != null) {
                bomAttr.put(dto.getBomComponentIface(), mtBom.getBomId());
            }

            // 行数据校验
            if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {
//                bomComponentIds = new ArrayList<>(dto.getMtBomComponentList().size());
                for (MtBomComponentVO7 bomComponent : dto.getMtBomComponentList()) {
                    Boolean temp = false;
                    if (StringUtils.isEmpty(bomComponent.getBomComponentId())) {
                        temp = true;
                    }
                    if ((allFlag || temp) && bomComponent.getLineNumber() == null) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "lineNumber", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && StringUtils.isEmpty(bomComponent.getMaterialId())
                            || !(allFlag || temp) && "".equals(bomComponent.getMaterialId())) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "materialId", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && StringUtils.isEmpty(bomComponent.getBomComponentType())
                            || !temp && "".equals(bomComponent.getBomComponentType())) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "bomComponentType", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && bomComponent.getDateFrom() == null) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "dateFrom", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && bomComponent.getQty() == null) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "qty", "【API:bomAllBatchUpdate】"));
                    }
                    // 校验类型
                    if (StringUtils.isNotEmpty(bomComponent.getBomComponentType())
                            && (CollectionUtils.isEmpty(bomComType)
                            || !bomComType.contains(bomComponent.getBomComponentType()))) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056",
                                        "BOM", "bomComponentType:" + bomComponent.getBomComponentType(),
                                        "【API:bomAllBatchUpdate】"));
                    }
                    // 验证 keyMaterialFlag 有效性
                    if (StringUtils.isNotEmpty(bomComponent.getKeyMaterialFlag())
                            && !MtBaseConstants.YES_NO.contains(bomComponent.getKeyMaterialFlag())) {
                        throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0055", "BOM", "keyMaterialFlag", "【API:bomAllBatchUpdate】"));
                    }

                    // 验证 assembleAsReqFlag 有效性
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleAsReqFlag())
                            && !MtBaseConstants.YES_NO.contains(bomComponent.getAssembleAsReqFlag())) {
                        throw new MtException("MT_BOM_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0055",
                                        "BOM", "assembleAsReqFlag", "【API:bomAllBatchUpdate】"));
                    }

                    // 校验assembleMethod有效性
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleMethod())
                            && (CollectionUtils.isEmpty(materialType)
                            || !materialType.contains(bomComponent.getAssembleMethod()))) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056",
                                        "BOM", "assembleMethod:" + bomComponent.getAssembleMethod(),
                                        "【API:bomAllBatchUpdate】"));

                    }

                    // 校验 attritionPolicy 有效性
                    if (StringUtils.isNotEmpty(bomComponent.getAttritionPolicy()) && (CollectionUtils
                            .isEmpty(attritionPolicyType)
                            || !attritionPolicyType.contains(bomComponent.getAttritionPolicy()))) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056",
                                        "BOM", "attritionPolicy:" + bomComponent.getAttritionPolicy(),
                                        "【API:bomAllBatchUpdate】"));

                    }

                    // 校验物料
                    if (StringUtils.isNotEmpty(bomComponent.getMaterialId()) && (MapUtils.isEmpty(mtMaterialVOMap)
                            || mtMaterialVOMap.get(bomComponent.getMaterialId()) == null)) {
                        throw new MtException("MT_BOM_0054",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054",
                                        "BOM",
                                        "bomComponent.materialId:" + bomComponent.getMaterialId(),
                                        "【API:bomAllBatchUpdate】"));
                    }

                    // 校验发库位
                    if (StringUtils.isNotEmpty(bomComponent.getIssuedLocatorId()) && (MapUtils.isEmpty(mtModLocatorMap)
                            || mtModLocatorMap.get(bomComponent.getIssuedLocatorId()) == null)) {
                        throw new MtException("MT_BOM_0054",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054",
                                        "BOM",
                                        "bomComponent.issuedLocatorId:"
                                                + bomComponent.getIssuedLocatorId(),
                                        "【API:bomAllBatchUpdate】"));
                    }
                    if (flag || temp) {
                        // 新增
                        mtBomComponent = new MtBomComponent();
                        mtBomComponent.setBomComponentId(newBomComIds.remove(0));
                        mtBomComponent.setObjectVersionNumber(1L);
                        mtBomComponent.setCreatedBy(userId);
                        mtBomComponent.setCreationDate(now);
                    } else {
                        // 更新
                        bomIdBomComId = new BomIdBomComId(dto.getBomId(), bomComponent.getBomComponentId());
                        if (MapUtils.isEmpty(mtBomComponentMap) || mtBomComponentMap.get(bomIdBomComId) == null) {
                            throw new MtException("MT_BOM_0070",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0070",
                                            "BOM", dto.getBomId(), bomComponent.getBomComponentId(),
                                            "【API:bomAllBatchUpdate】"));
                        }
                        mtBomComponent = mtBomComponentMap.get(bomIdBomComId);
                        mtBomComponent.setObjectVersionNumber(mtBomComponent.getObjectVersionNumber() + 1L);
                    }
                    mtBomComponent.setTenantId(tenantId);
                    mtBomComponent.setBomId(mtBom.getBomId());
                    mtBomComponent.setLineNumber(bomComponent.getLineNumber());
                    if (StringUtils.isNotEmpty(bomComponent.getMaterialId())) {
                        mtBomComponent.setMaterialId(bomComponent.getMaterialId());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getBomComponentType())) {
                        mtBomComponent.setBomComponentType(bomComponent.getBomComponentType());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getKeyMaterialFlag())) {
                        mtBomComponent.setKeyMaterialFlag(bomComponent.getKeyMaterialFlag());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleMethod())) {
                        mtBomComponent.setAssembleMethod(bomComponent.getAssembleMethod());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleAsReqFlag())) {
                        mtBomComponent.setAssembleAsReqFlag(bomComponent.getAssembleAsReqFlag());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getAttritionPolicy())) {
                        mtBomComponent.setAttritionPolicy(bomComponent.getAttritionPolicy());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getIssuedLocatorId())) {
                        mtBomComponent.setIssuedLocatorId(bomComponent.getIssuedLocatorId());
                    }
                    mtBomComponent.setAttritionChance(bomComponent.getAttritionChance());
                    mtBomComponent.setAttritionQty(bomComponent.getAttritionQty());
                    mtBomComponent.setQty(bomComponent.getQty());
                    mtBomComponent.setDateFrom(bomComponent.getDateFrom());
                    mtBomComponent.setDateTo(bomComponent.getDateTo());
                    mtBomComponent.setCid(Long.valueOf(newBomComCids.remove(0)));
                    mtBomComponent.setLastUpdateDate(now);
                    mtBomComponent.setLastUpdatedBy(userId);

                    mtBomComponentHis = new MtBomComponentHis();
                    mtBomComponentHis.setTenantId(tenantId);
                    mtBomComponentHis.setBomComponentHisId(newBomComHisIds.remove(0));
                    mtBomComponentHis.setBomComponentId(mtBomComponent.getBomComponentId());
                    mtBomComponentHis.setBomId(mtBomComponent.getBomId());
                    mtBomComponentHis.setLineNumber(mtBomComponent.getLineNumber());
                    mtBomComponentHis.setMaterialId(mtBomComponent.getMaterialId());
                    mtBomComponentHis.setBomComponentType(mtBomComponent.getBomComponentType());
                    mtBomComponentHis.setDateFrom(mtBomComponent.getDateFrom());
                    mtBomComponentHis.setDateTo(mtBomComponent.getDateTo());
                    mtBomComponentHis.setQty(mtBomComponent.getQty());
                    mtBomComponentHis.setKeyMaterialFlag(mtBomComponent.getKeyMaterialFlag());
                    mtBomComponentHis.setAssembleMethod(mtBomComponent.getAssembleMethod());
                    mtBomComponentHis.setAssembleAsReqFlag(mtBomComponent.getAssembleAsReqFlag());
                    mtBomComponentHis.setAttritionPolicy(mtBomComponent.getAttritionPolicy());
                    mtBomComponentHis.setAttritionChance(mtBomComponent.getAttritionChance());
                    mtBomComponentHis.setAttritionQty(mtBomComponent.getAttritionQty());
                    mtBomComponentHis.setCopiedFromComponentId(mtBomComponent.getCopiedFromComponentId());
                    mtBomComponentHis.setIssuedLocatorId(mtBomComponent.getIssuedLocatorId());
                    mtBomComponentHis.setEventId(eventId);
                    mtBomComponentHis.setCid(Long.valueOf(newBomComHisCids.remove(0)));
                    mtBomComponentHis.setCreationDate(now);
                    mtBomComponentHis.setCreatedBy(userId);
                    mtBomComponentHis.setLastUpdateDate(now);
                    mtBomComponentHis.setLastUpdatedBy(userId);
                    mtBomComponentHis.setObjectVersionNumber(1L);
                    mtBomComponent.setLatestHisId(mtBomComponentHis.getBomComponentHisId());
                    newBomComList.add(mtBomComponent);
                    newBomComHisList.add(mtBomComponentHis);
                    bomComAttrMap.put(mtBomComponentHis.getBomComponentId(), mtBomComponentHis.getBomComponentId());
                    bomComponentAttr.put(bomComponent.getBomComponentIface(), mtBomComponentHis.getBomComponentId());
//                    bomComponentIds.add(mtBomComponentHis.getBomComponentId());
                }
//                if (CollectionUtils.isNotEmpty(bomComponentIds) && dto.getBomComponentIface() != null) {
//                    bomComponentAttr.put(dto.getBomComponentIface(), bomComponentIds);
//                }
            }
        }
        System.out.println("bom数据处理：" + (System.currentTimeMillis() - start) + "毫秒");
        /**
         * 处理bom数据
         */
        List<String> replaceSql = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(newBomList)) {
            replaceSql.addAll(constructSql(newBomList));
            newBomList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomHisList)) {
            replaceSql.addAll(constructSql(newBomHisList));
            newBomHisList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomSiteAssignList)) {
            replaceSql.addAll(constructSql(newBomSiteAssignList));
            newBomSiteAssignList = null;
        }
        if (CollectionUtils.isNotEmpty(newWoList)) {
            replaceSql.addAll(constructSql(newWoList));
            newWoList = null;
        }
        if (CollectionUtils.isNotEmpty(newWoHisList)) {
            replaceSql.addAll(constructSql(newWoHisList));
            newWoHisList = null;
        }
        if (CollectionUtils.isNotEmpty(newPefpList)) {
            replaceSql.addAll(constructSql(newPefpList));
            newPefpList = null;
        }
        /**
         * 处理bom组件
         */
        if (CollectionUtils.isNotEmpty(newBomComList)) {
            replaceSql.addAll(constructSql(newBomComList));
            newBomComList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomComHisList)) {
            replaceSql.addAll(constructSql(newBomComHisList));
            newBomComHisList = null;
        }
        if (CollectionUtils.isNotEmpty(replaceSql)) {
            List<List<String>> commitSqlList = commitSqlList(replaceSql, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }

        if (MapUtils.isNotEmpty(bomAttrMap)) {
            sqlList.addAll(mtExtendSettingsRepository.attrHisBatchUpdate(tenantId, "mt_bom_attr", bomAttrMap, eventId));
        }
        if (MapUtils.isNotEmpty(bomComAttrMap)) {
            sqlList.addAll(mtExtendSettingsRepository.attrHisBatchUpdate(tenantId, "mt_bom_component_attr",
                    bomComAttrMap, eventId));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> splitSqlList = splitSqlList(sqlList);
            for (List<String> sql : splitSqlList) {
                jdbcTemplate.batchUpdate(sql.toArray(new String[sql.size()]));
            }
            sqlList.clear();
        }

        /**
         * 处理扩展属性
         */
        if (MapUtils.isNotEmpty(bomAttr)) {
            sqlList.addAll(saveAttrColumn(tenantId, bomAttr, "mt_bom_attr", "headAttribute"));
        }
        if (MapUtils.isNotEmpty(bomComponentAttr)) {
            //sqlList.addAll(saveAttrColumnAttr(tenantId, bomComponentAttr, "mt_bom_component_attr", "lineAttribute"));
            sqlList.addAll(saveAttrColumn(tenantId, bomComponentAttr, "mt_bom_component_attr", "lineAttribute"));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return result;
    }

    private static class BomIdBomComId implements Serializable {
        private static final long serialVersionUID = 6171034678690560373L;
        private String bomId;
        private String bomComponentId;

        public BomIdBomComId(String bomId, String bomComponentId) {
            this.bomId = bomId;
            this.bomComponentId = bomComponentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BomIdBomComId that = (BomIdBomComId) o;
            return Objects.equals(bomId, that.bomId) && Objects.equals(bomComponentId, that.bomComponentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bomId, bomComponentId);
        }
    }

    private static class BomSiteAssign implements Serializable {
        private static final long serialVersionUID = 2865952797693086800L;
        private String siteId;
        private String bomId;

        public BomSiteAssign(String siteId, String bomId) {
            this.siteId = siteId;
            this.bomId = bomId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BomSiteAssign that = (BomSiteAssign) o;
            return Objects.equals(siteId, that.siteId) && Objects.equals(bomId, that.bomId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(siteId, bomId);
        }
    }

    private Map<String, Map<String, String>> getTlsMap(Map<String, String> fieldValueMap) {
        Map<String, Map<String, String>> tlMap = new HashMap<>(fieldValueMap.size());
        Map<String, String> map;
        List<Language> languages = LanguageHelper.languages();
        for (Map.Entry<String, String> entry : fieldValueMap.entrySet()) {
            map = new HashMap<>(2);
            for (Language language : languages) {
                map.put(language.getCode(), entry.getValue());
            }
            tlMap.put(entry.getKey(), map);
        }
        return tlMap;
    }

    private List<String> constructSql(List<AuditDomain> ifaceSqlList) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<List<AuditDomain>> splitSqlList = StringHelper.splitSqlList(ifaceSqlList, SQL_ITEM_COUNT_LIMIT);
            for (List<AuditDomain> domains : splitSqlList) {
                sqlList.addAll(customDbRepository.getReplaceSql(domains));
            }
        }
        return sqlList;
    }

    private List<List<String>> commitSqlList(List<String> sqlList, int splitNum) {

        List<List<String>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    private List<List<String>> splitSqlList(List<String> sqlList) {
        List<List<String>> returnList = new ArrayList<>();

        if (sqlList.size() <= SQL_ITEM_COUNT_LIMIT) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / SQL_ITEM_COUNT_LIMIT;
            int splitRest = sqlList.size() % SQL_ITEM_COUNT_LIMIT;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * SQL_ITEM_COUNT_LIMIT, (i + 1) * SQL_ITEM_COUNT_LIMIT));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * SQL_ITEM_COUNT_LIMIT, sqlList.size()));
            }
        }

        return returnList;
    }

    /**
     * 保存扩展属性--一对一
     *
     * @param tenantId IRequest
     */
    private List<String> saveAttrColumn(Long tenantId, Map<MtBomComponentIface, String> attrMap, String tableName,
                                        String attributePrefix) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();
        List<String> result = new ArrayList<>();

        // 扩展表批量查询
        List<String> kidList = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(tableName);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> originMap =
                extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));
        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), attributePrefix);
            // 扩展属性更新时未传入时置为空
            if (MapUtils.isNotEmpty(originMap)) {
                if (CollectionUtils.isNotEmpty(originMap.get(t.getValue()))) {
                    for (MtExtendAttrVO1 attrVO1 : originMap.get(t.getValue())) {
                        fieldMap.putIfAbsent(attrVO1.getAttrName(), "");
                    }
                }
            }
            List<MtExtendVO5> saveExtendList = new ArrayList<>();
            MtExtendVO5 saveExtend;
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                for (Language language : languages) {
                    saveExtend = new MtExtendVO5();
                    saveExtend.setAttrName(entry.getKey());
                    saveExtend.setAttrValue(entry.getValue());
                    saveExtend.setLang(Strings.isEmpty(language.getCode()) ? "" : language.getCode());
                    saveExtendList.add(saveExtend);
                }
                attrTableMap.put(t.getValue(), saveExtendList);
            }
        });
        if (MapUtils.isNotEmpty(attrTableMap)) {
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, tableName, attrTableMap));
        }
        return result;
    }

    /**
     * 保存扩展属性--一对多
     *
     * @param tenantId
     * @param attrMap
     * @return
     */
    private List<String> saveAttrColumnAttr(Long tenantId, Map<MtBomComponentIface, List<String>> attrMap,
                                            String tableName, String attributePrefix) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();

        // 扩展表批量查询
        List<List<String>> lists = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        List<String> kidList = new ArrayList<>();
        for (List<String> list : lists) {
            kidList.addAll(list);
        }

        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(tableName);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

        Map<String, List<MtExtendAttrVO1>> originMap =
                extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));

        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        List<String> result = new ArrayList<>();
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), attributePrefix);

            // 扩展属性更新时未传入时置为空
            if (MapUtils.isNotEmpty(originMap)) {
                for (String s : t.getValue()) {
                    if (CollectionUtils.isNotEmpty(originMap.get(s))) {
                        for (MtExtendAttrVO1 attrVO1 : originMap.get(s)) {
                            fieldMap.putIfAbsent(attrVO1.getAttrName(), "");
                        }
                    }
                }
            }
            List<MtExtendVO5> saveExtendList = new ArrayList<>();
            MtExtendVO5 saveExtend;
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                for (Language language : languages) {
                    saveExtend = new MtExtendVO5();
                    saveExtend.setAttrName(entry.getKey());
                    saveExtend.setAttrValue(entry.getValue());
                    saveExtend.setLang(Strings.isEmpty(language.getCode()) ? "" : language.getCode());
                    saveExtendList.add(saveExtend);
                }
                for (String s : t.getValue()) {
                    attrTableMap.put(s, saveExtendList);
                }
            }
        });
        if (MapUtils.isNotEmpty(attrTableMap)) {
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, tableName, attrTableMap));
        }
        return result;

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<String> myBomAllBatchUpdate(Long tenantId, List<MtBomVO11> bomList) {
        // bom数据
        List<String> bomIds = bomList.stream().filter(t -> StringUtils.isNotEmpty(t.getBomId()))
                .map(MtBomVO11::getBomId).distinct().collect(Collectors.toList());
        Map<String, MtBom> mtBomVO7Map = null;
        Map<BomIdBomComId, MtBomComponent> mtBomComponentMap = null;
        Map<String, List<MtBomComponent>> bomComponentMap = null;
        Map<BomSiteAssign, MtBomSiteAssign> siteAssignMap = null;
        if (CollectionUtils.isNotEmpty(bomIds)) {
            String bomIdSql = StringHelper.getWhereInValuesSql("t1.BOM_ID", bomIds, 1000);
            List<MtBom> mtBomVO7s = mtBomMapper.selectByBomIds(tenantId, bomIdSql);
            if (CollectionUtils.isNotEmpty(mtBomVO7s)) {
                mtBomVO7Map = mtBomVO7s.stream().collect(Collectors.toMap(MtBom::getBomId, c -> c));
            }

            // bomComponent数据
            String whereInValuesSql = StringHelper.getWhereInValuesSql("BOM_ID", bomIds, 1000);
            List<MtBomComponent> mtBomComponents = mtBomComponentMapper.selectBomComponentByBomIds(tenantId, whereInValuesSql);
            if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                mtBomComponentMap = mtBomComponents.stream().collect(
                        Collectors.toMap(c -> new BomIdBomComId(c.getBomId(), c.getBomComponentId()), c -> c));
                bomComponentMap = mtBomComponents.stream().collect(Collectors.groupingBy(MtBomComponent::getBomId));
            }

            // bomSiteAssign数据
            List<MtBomSiteAssign> mtBomSiteAssigns =
                    mtBomSiteAssignRepository.bomLimitBomSiteAssignBatchQuery(tenantId, bomIds);
            if (CollectionUtils.isNotEmpty(mtBomSiteAssigns)) {
                siteAssignMap = mtBomSiteAssigns.stream()
                        .collect(Collectors.toMap(c -> new BomSiteAssign(c.getSiteId(), c.getBomId()), c -> c));
            }
        }

        // manufacturing数据
        List<String> materialSiteId = bomList.stream()
                .filter(c -> "MATERIAL".equalsIgnoreCase(c.getBomObjectType())
                        && StringUtils.isNotEmpty(c.getMaterialSiteId()))
                .map(MtBomVO11::getMaterialSiteId).distinct().collect(Collectors.toList());
        Long materialCount = bomList.stream().filter(c -> "MATERIAL".equalsIgnoreCase(c.getBomObjectType())
                && StringUtils.isNotEmpty(c.getMaterialSiteId())).count();
        Long woCount = bomList.stream()
                .filter(c -> "WO".equalsIgnoreCase(c.getBomObjectType()) && c.getWorkOrder() != null).count();

        Map<String, MtPfepManufacturing> pfepManufacturingMap = null;
        if (CollectionUtils.isNotEmpty(materialSiteId)) {
            List<MtPfepManufacturing> mtPfepManufacturings = mtPfepManufacturingRepository
                    .selectpfepManufacturingByMaterialSiteId(tenantId, materialSiteId);
            pfepManufacturingMap = mtPfepManufacturings.stream()
                    .collect(Collectors.toMap(MtPfepManufacturing::getMaterialSiteId, c -> c));
        }

        // bom类型
        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("BOM");
        mtGenTypeVO2.setTypeGroup("BOM_TYPE");
        List<MtGenType> bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> bomType = bomTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());

        // bomComponent类型
        mtGenTypeVO2.setTypeGroup("BOM_COMPONENT_TYPE");
        List<MtGenType> bomComTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> bomComType = bomComTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());

        // 装配类型
        mtGenTypeVO2.setModule("MATERIAL");
        mtGenTypeVO2.setTypeGroup("ASSY_METHOD");
        List<MtGenType> materialTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> materialType = materialTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());

        // 损耗类型
        mtGenTypeVO2.setModule("BOM");
        mtGenTypeVO2.setTypeGroup("ATTRITION_POLICY");
        List<MtGenType> attritionPolicyTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        List<String> attritionPolicyType =
                attritionPolicyTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());

        // bom状态
        MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("BOM");
        mtGenStatusVO2.setStatusGroup("BOM_STATUS");
        List<MtGenStatus> bomStatuses = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
        List<String> bomStatus = bomStatuses.stream().map(MtGenStatus::getStatusCode).collect(Collectors.toList());

        // 获取组件所有物料
        List<List<MtBomComponentVO7>> bomComponentList =
                bomList.stream().filter(t -> CollectionUtils.isNotEmpty(t.getMtBomComponentList()))
                        .map(MtBomVO11::getMtBomComponentList).collect(Collectors.toList());
        List<String> materialId = new ArrayList<>();
        List<String> issuedLocatorId = new ArrayList<>();
        AtomicInteger bomComCount = new AtomicInteger();
        AtomicInteger bomsubCount = new AtomicInteger();

        bomComponentList.stream().forEach(c -> {
            materialId.addAll(c.stream().filter(t -> StringUtils.isNotEmpty(t.getMaterialId()))
                    .map(MtBomComponentVO7::getMaterialId).distinct().collect(Collectors.toList()));
            issuedLocatorId.addAll(c.stream().filter(t -> StringUtils.isNotEmpty(t.getIssuedLocatorId()))
                    .map(MtBomComponentVO7::getIssuedLocatorId).distinct().collect(Collectors.toList()));
            bomComCount.addAndGet(c.size());
            bomsubCount.addAndGet((int) c.stream().filter(t -> CollectionUtils.isNotEmpty(t.getMtBomSubstituteGroupList())).count());
        });
        Map<String, MtMaterialVO> mtMaterialVOMap = null;
        if (CollectionUtils.isNotEmpty(materialId)) {
            List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialBasicInfoBatchGet(tenantId, materialId);
            if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                mtMaterialVOS = mtMaterialVOS.stream()
                        .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                    mtMaterialVOMap =
                            mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, c -> c));
                }
            }
        }

        // 发料库位信息
        Map<String, MtModLocator> mtModLocatorMap = null;
        if (CollectionUtils.isNotEmpty(issuedLocatorId)) {
            List<MtModLocator> mtModLocators =
                    mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, issuedLocatorId);
            if (CollectionUtils.isNotEmpty(mtModLocators)) {
                mtModLocators = mtModLocators.stream()
                        .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtModLocators)) {
                    mtModLocatorMap = mtModLocators.stream()
                            .collect(Collectors.toMap(MtModLocator::getLocatorId, c -> c));
                }
            }
        }

        // 共有变量
        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> newBomList = new ArrayList<>(bomList.size());
        List<AuditDomain> newBomHisList = new ArrayList<>(bomList.size());
        List<AuditDomain> newBomComList = new ArrayList<>(bomComCount.get());
        List<AuditDomain> newBomComHisList = new ArrayList<>(bomComCount.get());

        List<AuditDomain> newBomSiteAssignList = new ArrayList<>(bomList.size());
        List<AuditDomain> newWoList = new ArrayList<>(woCount.intValue());
        List<AuditDomain> newWoHisList = new ArrayList<>(woCount.intValue());
        List<AuditDomain> newPefpList = new ArrayList<>(woCount.intValue());

        List<AuditDomain> newBomGroupList = new ArrayList<>(bomsubCount.intValue());
        List<AuditDomain> newBomSubList = new ArrayList<>(bomsubCount.intValue());


        Long userId = -1L;
        Date now = new Date();
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        // 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("BOM_ALL_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 批量获取id，cid
        List<String> newBomIds = this.customDbRepository.getNextKeys("mt_bom_s", bomList.size() - bomIds.size());
        List<String> newBomCids = this.customDbRepository.getNextKeys("mt_bom_cid_s", bomList.size());
        List<String> newBomHisIds = this.customDbRepository.getNextKeys("mt_bom_his_s", bomList.size());
        List<String> newBomHisCids = this.customDbRepository.getNextKeys("mt_bom_his_cid_s", bomList.size());

        List<String> newBomComIds = this.customDbRepository.getNextKeys("mt_bom_component_s", bomComCount.get());
        List<String> newBomComCids = this.customDbRepository.getNextKeys("mt_bom_component_cid_s", bomComCount.get());
        List<String> newBomComHisIds = this.customDbRepository.getNextKeys("mt_bom_component_his_s", bomComCount.get());

        List<String> newBomGroupIds = this.customDbRepository.getNextKeys("mt_bom_substitute_group_s", bomComCount.get());
        List<String> newBomGroupCids = this.customDbRepository.getNextKeys("mt_bom_substitute_group_cid_s", bomComCount.get());

        List<String> newBomSubs = this.customDbRepository.getNextKeys("mt_bom_substitute_s", bomComCount.get());
        List<String> newBomSubCids = this.customDbRepository.getNextKeys("mt_bom_substitute_cid_s", bomComCount.get());


        List<String> newBomComHisCids =
                this.customDbRepository.getNextKeys("mt_bom_component_his_cid_s", bomComCount.get());

        List<String> newBomSiteAssgnIds =
                this.customDbRepository.getNextKeys("mt_bom_site_assign_s", bomList.size() - bomIds.size());
        List<String> newBomSiteAssgnCids =
                this.customDbRepository.getNextKeys("mt_bom_site_assign_cid_s", bomList.size());

        List<String> newPfepManufacturingIds =
                this.customDbRepository.getNextKeys("mt_pfep_manufacturing_s", materialCount.intValue());
        List<String> newPfepManufacturingCids =
                this.customDbRepository.getNextKeys("mt_pfep_manufacturing_cid_s", materialCount.intValue());

        List<String> newWorkOrderCids = this.customDbRepository.getNextKeys("mt_work_order_cid_s", woCount.intValue());
        List<String> newWorkOrderHisIds =
                this.customDbRepository.getNextKeys("mt_work_order_his_s", woCount.intValue());
        List<String> newWorkOrderHisCids =
                this.customDbRepository.getNextKeys("mt_work_order_his_cid_s", woCount.intValue());
        MtBom mtBom;
        MtBomHis mtBomHis;
        BomIdBomComId bomIdBomComId;
        BomSiteAssign bomSiteAssign;
        MtBomSiteAssign mtBomSiteAssign;
        MtBomComponent mtBomComponent;
        MtBomSubstituteGroup mtBomSubstituteGroup = null;
        MtBomSubstitute mtBomSubstitute = null;
        MtBomComponentHis mtBomComponentHis;
        MtBomComponentIface headBomIface;
        MtWorkOrder mtWorkOrder;
        MtWorkOrderHis mtWorkOrderHis;
        MtPfepManufacturing mtPfepManufacturing;
        Map<String, String> bomAttrMap = new HashMap<>(bomList.size());
        Map<String, String> bomComAttrMap = new HashMap<>(bomComCount.get());
        List<String> result = new ArrayList<>(bomList.size());
        Map<MtBomComponentIface, String> bomAttr = new HashMap<>(bomList.size());
//        Map<MtBomComponentIface, List<String>> bomComponentAttr = new HashMap<>(bomList.size());
//        List<String> bomComponentIds;
        Map<MtBomComponentIface, String> bomComponentAttr = new HashMap<>(bomList.size());
        Long start = System.currentTimeMillis();
        for (MtBomVO11 dto : bomList) {
            Boolean flag = false;
            Boolean allFlag = false;
            if (StringUtils.isNotEmpty(dto.getBomId()) && ALL.equalsIgnoreCase(dto.getUpdateMethod())) {
                allFlag = true;
            }
            if (StringUtils.isEmpty(dto.getBomId())) {
                flag = true;
            }
            // 头数据参数校验校验
            if (!UPDATE_METHOD.contains(dto.getUpdateMethod())) {
                throw new MtException("MT_BOM_0071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0071", "BOM", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && StringUtils.isEmpty(dto.getBomName())
                    || !(flag || allFlag) && "".equals(dto.getBomName())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomName", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && StringUtils.isEmpty(dto.getBomType())
                    || !(flag || allFlag) && "".equals(dto.getBomType())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomType", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && StringUtils.isEmpty(dto.getRevision())
                    || !(flag || allFlag) && "".equals(dto.getRevision())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "revision", "【API:bomAllBatchUpdate】"));
            }
            if ((flag || allFlag) && dto.getDateFrom() == null) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "dateFrom", "【API:bomAllBatchUpdate】"));
            }

            // 验证currentFlag有效性
            if (StringUtils.isNotEmpty(dto.getCurrentFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getCurrentFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "currentFlag", "【API:bomAllBatchUpdate】"));
            }

            // 验证assembleAsMaterialFlag有效性
            if (StringUtils.isNotEmpty(dto.getAssembleAsMaterialFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getAssembleAsMaterialFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "assembleAsMaterialFlag", "【API:bomAllBatchUpdate】"));
            }

            // 验证 releasedFlag 有效性
            if (StringUtils.isNotEmpty(dto.getReleasedFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getReleasedFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "releasedFlag", "【API:bomAllBatchUpdate】"));
            }

            // 验证 autoRevisionFlag 有效性
            if (StringUtils.isNotEmpty(dto.getAutoRevisionFlag())
                    && !MtBaseConstants.YES_NO.contains(dto.getAutoRevisionFlag())) {
                throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0055", "BOM", "autoRevisionFlag", "【API:bomAllBatchUpdate】"));
            }

            // 校验类型与状态
            if (StringUtils.isNotEmpty(dto.getBomType())
                    && (CollectionUtils.isEmpty(bomType) || !bomType.contains(dto.getBomType()))) {
                throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0056", "BOM", "bomType:" + dto.getBomType(), "【API:bomAllBatchUpdate】"));
            }
            if (StringUtils.isNotEmpty(dto.getBomStatus())
                    && (CollectionUtils.isEmpty(bomStatus) || !bomStatus.contains(dto.getBomStatus()))) {
                throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0056", "BOM", "bomStatus:" + dto.getBomStatus(), "【API:bomAllBatchUpdate】"));
            }

            // 新增bom
            if (flag) {
                mtBom = new MtBom();
                mtBom.setBomId(newBomIds.remove(0));
                mtBom.setCreatedBy(userId);
                mtBom.setCreationDate(now);
                mtBom.setObjectVersionNumber(1L);

            } else {
                // 判断bomId值的有效性
                if (MapUtils.isEmpty(mtBomVO7Map) || mtBomVO7Map.get(dto.getBomId()) == null) {
                    throw new MtException("MT_BOM_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0045", "BOM", dto.getBomId(), "【API:bomAllBatchUpdate】"));
                }
                mtBom = mtBomVO7Map.get(dto.getBomId());
                mtBom.setObjectVersionNumber(mtBom.getObjectVersionNumber() + 1L);
            }
            Map<String, String> tempTlMap = new HashMap<>(1);
            if (StringUtils.isNotEmpty(dto.getDescription())) {
                tempTlMap.put("description", dto.getDescription());
            }
            mtBom.setTenantId(tenantId);
            if (StringUtils.isNotEmpty(dto.getBomName())) {
                mtBom.setBomName(dto.getBomName());
            }
            if (StringUtils.isNotEmpty(dto.getRevision())) {
                mtBom.setRevision(dto.getRevision());
            }
            if (StringUtils.isNotEmpty(dto.getBomType())) {
                mtBom.setBomType(dto.getBomType());
            }
            if (StringUtils.isNotEmpty(dto.getCurrentFlag())) {
                mtBom.setCurrentFlag(dto.getCurrentFlag());
            }

            if (StringUtils.isNotEmpty(dto.getDescription())) {
                mtBom.setDescription(dto.getDescription());
            }
            if (StringUtils.isNotEmpty(dto.getBomStatus())) {
                mtBom.setBomStatus(dto.getBomStatus());
            }
            if (StringUtils.isNotEmpty(dto.getReleasedFlag())) {
                mtBom.setReleasedFlag(dto.getReleasedFlag());
            }
            if (StringUtils.isNotEmpty(dto.getAutoRevisionFlag())) {
                mtBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
            }
            mtBom.setDateFrom(dto.getDateFrom());
            mtBom.setDateTo(dto.getDateTo());
            mtBom.setPrimaryQty(dto.getPrimaryQty());
            if (MapUtils.isNotEmpty(tempTlMap)) {
                mtBom.set_tls(getTlsMap(tempTlMap));
            }
            mtBom.setCid(Long.valueOf(newBomCids.remove(0)));
            mtBom.setLastUpdatedBy(userId);
            mtBom.setLastUpdateDate(now);

            // 记录历史
            mtBomHis = new MtBomHis();
            mtBomHis.setTenantId(tenantId);
            mtBomHis.setBomHisId(newBomHisIds.remove(0));
            mtBomHis.setBomId(mtBom.getBomId());
            mtBomHis.setBomName(mtBom.getBomName());
            mtBomHis.setRevision(mtBom.getRevision());
            mtBomHis.setBomType(mtBom.getBomType());
            mtBomHis.setDateFrom(mtBom.getDateFrom());
            mtBomHis.setDateTo(mtBom.getDateTo());
            mtBomHis.setCurrentFlag(mtBom.getCurrentFlag());
            mtBomHis.setDescription(mtBom.getDescription());
            mtBomHis.setBomStatus(mtBom.getBomStatus());
            mtBomHis.setCopiedFromBomId(mtBom.getCopiedFromBomId());
            mtBomHis.setReleasedFlag(mtBom.getReleasedFlag());
            mtBomHis.setPrimaryQty(mtBom.getPrimaryQty());
            mtBomHis.setAutoRevisionFlag(mtBom.getAutoRevisionFlag());
            mtBomHis.setAssembleAsMaterialFlag(mtBom.getAssembleAsMaterialFlag());
            mtBomHis.setEventId(eventId);
            mtBomHis.setCid(Long.valueOf(newBomHisCids.remove(0)));
            mtBomHis.setCreatedBy(userId);
            mtBomHis.setCreationDate(now);
            mtBomHis.setObjectVersionNumber(1L);
            mtBomHis.setLastUpdatedBy(userId);
            mtBomHis.setLastUpdateDate(now);
            mtBom.setLatestHisId(mtBomHis.getBomHisId());

            newBomList.add(mtBom);
            newBomHisList.add(mtBomHis);

            // 扩展历史表数据
            bomAttrMap.put(mtBomHis.getBomId(), mtBomHis.getBomHisId());

            // 全量更新是失效原有组件
            if (allFlag && MapUtils.isNotEmpty(bomComponentMap)
                    && CollectionUtils.isNotEmpty(bomComponentMap.get(mtBom.getBomId()))) {
                bomComponentMap.get(mtBom.getBomId()).stream().forEach(c -> {
                    c.setDateTo(now);
                });
                newBomComList.addAll(bomComponentMap.get(mtBom.getBomId()));
            }

            // bomSiteAssign
            if (dto.getBomComponentIface() != null) {
                bomSiteAssign = new BomSiteAssign(dto.getSiteId(), mtBom.getBomId());
                if (MapUtils.isNotEmpty(siteAssignMap) && siteAssignMap.get(bomSiteAssign) != null) {
                    mtBomSiteAssign = siteAssignMap.get(bomSiteAssign);

                } else {
                    mtBomSiteAssign = new MtBomSiteAssign();
                    mtBomSiteAssign.setBomId(mtBom.getBomId());
                    mtBomSiteAssign.setSiteId(dto.getSiteId());
                    mtBomSiteAssign.setAssignId(newBomSiteAssgnIds.remove(0));
                    mtBomSiteAssign.setCreatedBy(userId);
                    mtBomSiteAssign.setCreationDate(now);
                    mtBomSiteAssign.setObjectVersionNumber(1L);
                }
                headBomIface = dto.getBomComponentIface();
                boolean bomEndDateCheck = headBomIface.getBomEndDate() == null;
                boolean bomStatusCheck = StringUtils.isEmpty(headBomIface.getBomStatus())
                        || "ACTIVE".equals(headBomIface.getBomStatus());
                boolean enableFlagCheck = StringUtils.isEmpty(headBomIface.getEnableFlag())
                        || "Y".equals(headBomIface.getEnableFlag());
                mtBomSiteAssign.setCid(Long.valueOf(newBomSiteAssgnCids.remove(0)));
                mtBomSiteAssign.setEnableFlag(bomEndDateCheck && bomStatusCheck && enableFlagCheck ? "Y" : "N");
                mtBomSiteAssign.setTenantId(tenantId);
                mtBomSiteAssign.setLastUpdatedBy(userId);
                mtBomSiteAssign.setLastUpdateDate(now);
                newBomSiteAssignList.add(mtBomSiteAssign);
            }

            if ("WO".equalsIgnoreCase(dto.getBomObjectType()) && dto.getWorkOrder() != null) {
                mtWorkOrder = dto.getWorkOrder();
                mtWorkOrder.setBomId(mtBom.getBomId());
                mtWorkOrder.setValidateFlag(MtBaseConstants.NO);
                mtWorkOrder.setTenantId(tenantId);
                mtWorkOrder.setCid(Long.valueOf(newWorkOrderCids.remove(0)));
                mtWorkOrder.setLastUpdatedBy(userId);
                mtWorkOrder.setLastUpdateDate(now);
                mtWorkOrder.setObjectVersionNumber(mtWorkOrder.getObjectVersionNumber() + 1L);

                mtWorkOrderHis = new MtWorkOrderHis();
                mtWorkOrderHis.setTenantId(tenantId);
                mtWorkOrderHis.setWorkOrderHisId(newWorkOrderHisIds.remove(0));
                mtWorkOrderHis.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderHis.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                mtWorkOrderHis.setWorkOrderType(mtWorkOrder.getWorkOrderType());
                mtWorkOrderHis.setSiteId(mtWorkOrder.getSiteId());
                mtWorkOrderHis.setProductionLineId(mtWorkOrder.getProductionLineId());
                mtWorkOrderHis.setWorkcellId(mtWorkOrder.getWorkcellId());
                mtWorkOrderHis.setMakeOrderId(mtWorkOrder.getMakeOrderId());
                mtWorkOrderHis.setProductionVersion(mtWorkOrder.getProductionVersion());
                mtWorkOrderHis.setMaterialId(mtWorkOrder.getMaterialId());
                mtWorkOrderHis.setQty(mtWorkOrder.getQty());
                mtWorkOrderHis.setUomId(mtWorkOrder.getUomId());
                mtWorkOrderHis.setPriority(mtWorkOrder.getPriority());
                mtWorkOrderHis.setStatus(mtWorkOrder.getStatus());
                mtWorkOrderHis.setLastWoStatus(mtWorkOrder.getLastWoStatus());
                mtWorkOrderHis.setPlanStartTime(mtWorkOrder.getPlanStartTime());
                mtWorkOrderHis.setPlanEndTime(mtWorkOrder.getPlanEndTime());
                mtWorkOrderHis.setLocatorId(mtWorkOrder.getLocatorId());
                mtWorkOrderHis.setBomId(mtWorkOrder.getBomId());
                mtWorkOrderHis.setRouterId(mtWorkOrder.getRouterId());
                mtWorkOrderHis.setValidateFlag(mtWorkOrder.getValidateFlag());
                mtWorkOrderHis.setRemark(mtWorkOrder.getRemark());
                mtWorkOrderHis.setOpportunityId(mtWorkOrder.getOpportunityId());
                mtWorkOrderHis.setCustomerId(mtWorkOrder.getCustomerId());
                mtWorkOrderHis.setCompleteControlType(mtWorkOrder.getCompleteControlType());
                mtWorkOrderHis.setCompleteControlQty(mtWorkOrder.getCompleteControlQty());
                mtWorkOrderHis.setEventId(dto.getEventId());
                mtWorkOrderHis.setTrxQty(0.0D);
                mtWorkOrderHis.setSourceIdentificationId(mtWorkOrder.getSourceIdentificationId());
                mtWorkOrderHis.setCid(Long.valueOf(newWorkOrderHisCids.remove(0)));
                mtWorkOrderHis.setCreationDate(now);
                mtWorkOrderHis.setCreatedBy(userId);
                mtWorkOrderHis.setLastUpdateDate(now);
                mtWorkOrderHis.setLastUpdatedBy(userId);
                mtWorkOrderHis.setObjectVersionNumber(1L);
                mtWorkOrder.setLatestHisId(mtWorkOrderHis.getWorkOrderHisId());
                newWoList.add(mtWorkOrder);
                newWoHisList.add(mtWorkOrderHis);
            }
            if ("MATERIAL".equalsIgnoreCase(dto.getBomObjectType())) {
                if (MapUtils.isNotEmpty(pfepManufacturingMap)
                        && pfepManufacturingMap.get(dto.getMaterialSiteId()) != null) {
                    mtPfepManufacturing = pfepManufacturingMap.get(dto.getMaterialSiteId());
                    mtPfepManufacturing.setObjectVersionNumber(mtPfepManufacturing.getObjectVersionNumber() + 1L);
                } else {
                    mtPfepManufacturing = new MtPfepManufacturing();
                    mtPfepManufacturing.setPfepManufacturingId(newPfepManufacturingIds.remove(0));
                    mtPfepManufacturing.setEnableFlag("Y");
                    mtPfepManufacturing.setCreatedBy(userId);
                    mtPfepManufacturing.setCreationDate(now);
                    mtPfepManufacturing.setObjectVersionNumber(1L);
                }
                mtPfepManufacturing.setTenantId(tenantId);
                mtPfepManufacturing.setMaterialSiteId(dto.getMaterialSiteId());
                mtPfepManufacturing.setDefaultBomId(mtBom.getBomId());
                mtPfepManufacturing.setLastUpdatedBy(userId);
                mtPfepManufacturing.setLastUpdateDate(now);
                mtPfepManufacturing.setCid(Long.valueOf(newPfepManufacturingCids.remove(0)));
                newPefpList.add(mtPfepManufacturing);
            }
            result.add(mtBom.getBomId());

            // bom扩展属性
            if (dto.getBomComponentIface() != null) {
                bomAttr.put(dto.getBomComponentIface(), mtBom.getBomId());
            }

            // 行数据校验
            if (CollectionUtils.isNotEmpty(dto.getMtBomComponentList())) {
//                bomComponentIds = new ArrayList<>(dto.getMtBomComponentList().size());
                for (MtBomComponentVO7 bomComponent : dto.getMtBomComponentList()) {
                    Boolean temp = false;
                    if (StringUtils.isEmpty(bomComponent.getBomComponentId())) {
                        temp = true;
                    }
                    if ((allFlag || temp) && bomComponent.getLineNumber() == null) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "lineNumber", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && StringUtils.isEmpty(bomComponent.getMaterialId())
                            || !(allFlag || temp) && "".equals(bomComponent.getMaterialId())) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "materialId", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && StringUtils.isEmpty(bomComponent.getBomComponentType())
                            || !temp && "".equals(bomComponent.getBomComponentType())) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "bomComponentType", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && bomComponent.getDateFrom() == null) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "dateFrom", "【API:bomAllBatchUpdate】"));
                    }
                    if ((allFlag || temp) && bomComponent.getQty() == null) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "qty", "【API:bomAllBatchUpdate】"));
                    }
                    // 校验类型
                    if (StringUtils.isNotEmpty(bomComponent.getBomComponentType())
                            && (CollectionUtils.isEmpty(bomComType)
                            || !bomComType.contains(bomComponent.getBomComponentType()))) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056",
                                        "BOM", "bomComponentType:" + bomComponent.getBomComponentType(),
                                        "【API:bomAllBatchUpdate】"));
                    }
                    // 验证 keyMaterialFlag 有效性
                    if (StringUtils.isNotEmpty(bomComponent.getKeyMaterialFlag())
                            && !MtBaseConstants.YES_NO.contains(bomComponent.getKeyMaterialFlag())) {
                        throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0055", "BOM", "keyMaterialFlag", "【API:bomAllBatchUpdate】"));
                    }

                    // 验证 assembleAsReqFlag 有效性
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleAsReqFlag())
                            && !MtBaseConstants.YES_NO.contains(bomComponent.getAssembleAsReqFlag())) {
                        throw new MtException("MT_BOM_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0055",
                                        "BOM", "assembleAsReqFlag", "【API:bomAllBatchUpdate】"));
                    }

                    // 校验assembleMethod有效性
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleMethod())
                            && (CollectionUtils.isEmpty(materialType)
                            || !materialType.contains(bomComponent.getAssembleMethod()))) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056",
                                        "BOM", "assembleMethod:" + bomComponent.getAssembleMethod(),
                                        "【API:bomAllBatchUpdate】"));

                    }

                    // 校验 attritionPolicy 有效性
                    if (StringUtils.isNotEmpty(bomComponent.getAttritionPolicy()) && (CollectionUtils
                            .isEmpty(attritionPolicyType)
                            || !attritionPolicyType.contains(bomComponent.getAttritionPolicy()))) {
                        throw new MtException("MT_BOM_0056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0056",
                                        "BOM", "attritionPolicy:" + bomComponent.getAttritionPolicy(),
                                        "【API:bomAllBatchUpdate】"));

                    }

                    // 校验物料
                    if (StringUtils.isNotEmpty(bomComponent.getMaterialId()) && (MapUtils.isEmpty(mtMaterialVOMap)
                            || mtMaterialVOMap.get(bomComponent.getMaterialId()) == null)) {
                        throw new MtException("MT_BOM_0054",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054",
                                        "BOM",
                                        "bomComponent.materialId:" + bomComponent.getMaterialId(),
                                        "【API:bomAllBatchUpdate】"));
                    }

                    // 校验发库位
                    if (StringUtils.isNotEmpty(bomComponent.getIssuedLocatorId()) && (MapUtils.isEmpty(mtModLocatorMap)
                            || mtModLocatorMap.get(bomComponent.getIssuedLocatorId()) == null)) {
                        throw new MtException("MT_BOM_0054",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054",
                                        "BOM",
                                        "bomComponent.issuedLocatorId:"
                                                + bomComponent.getIssuedLocatorId(),
                                        "【API:bomAllBatchUpdate】"));
                    }
                    if (flag || temp) {
                        // 新增
                        mtBomComponent = new MtBomComponent();
                        mtBomComponent.setBomComponentId(newBomComIds.remove(0));
                        mtBomComponent.setObjectVersionNumber(1L);
                        mtBomComponent.setCreatedBy(userId);
                        mtBomComponent.setCreationDate(now);
                        if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList())) {
                            mtBomSubstituteGroup = new MtBomSubstituteGroup();
                            BeanUtils.copyProperties(bomComponent.getMtBomSubstituteGroupList().get(0), mtBomSubstituteGroup);
                            mtBomSubstituteGroup.setBomComponentId(mtBomComponent.getBomComponentId());
                            mtBomSubstituteGroup.setBomSubstituteGroupId(newBomGroupIds.remove(0));
                        }
                    } else {
                        // 更新
                        bomIdBomComId = new BomIdBomComId(dto.getBomId(), bomComponent.getBomComponentId());
                        if (MapUtils.isEmpty(mtBomComponentMap) || mtBomComponentMap.get(bomIdBomComId) == null) {
                            throw new MtException("MT_BOM_0070",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0070",
                                            "BOM", dto.getBomId(), bomComponent.getBomComponentId(),
                                            "【API:bomAllBatchUpdate】"));
                        }
                        mtBomComponent = mtBomComponentMap.get(bomIdBomComId);
                        mtBomComponent.setObjectVersionNumber(mtBomComponent.getObjectVersionNumber() + 1L);

                        //获取原来的数据
                        if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList())) {
                            mtBomSubstituteGroup = new MtBomSubstituteGroup();

                            BeanUtils.copyProperties(bomComponent.getMtBomSubstituteGroupList().get(0), mtBomSubstituteGroup);
                            mtBomSubstituteGroup.setBomComponentId(mtBomComponent.getBomComponentId());


                            List<MtBomSubstituteGroup> mtBomSubstituteGroups = mtBomSubstituteGroupRepository.selectByCondition(Condition.builder(MtBomSubstituteGroup.class)
                                    .andWhere(Sqls.custom()
                                            .andEqualTo(MtBomSubstituteGroup.FIELD_BOM_COMPONENT_ID, mtBomComponent.getBomComponentId())).build());
                            if (CollectionUtils.isNotEmpty(mtBomSubstituteGroups)) {
                                mtBomSubstituteGroup.setBomSubstituteGroupId(mtBomSubstituteGroups.get(0).getBomSubstituteGroupId());
                            } else {
                                mtBomSubstituteGroup.setBomSubstituteGroupId(newBomGroupIds.remove(0));
                            }
                        }

                    }
                    mtBomComponent.setTenantId(tenantId);
                    mtBomComponent.setBomId(mtBom.getBomId());
                    mtBomComponent.setLineNumber(bomComponent.getLineNumber());
                    if (StringUtils.isNotEmpty(bomComponent.getMaterialId())) {
                        mtBomComponent.setMaterialId(bomComponent.getMaterialId());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getBomComponentType())) {
                        mtBomComponent.setBomComponentType(bomComponent.getBomComponentType());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getKeyMaterialFlag())) {
                        mtBomComponent.setKeyMaterialFlag(bomComponent.getKeyMaterialFlag());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleMethod())) {
                        mtBomComponent.setAssembleMethod(bomComponent.getAssembleMethod());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getAssembleAsReqFlag())) {
                        mtBomComponent.setAssembleAsReqFlag(bomComponent.getAssembleAsReqFlag());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getAttritionPolicy())) {
                        mtBomComponent.setAttritionPolicy(bomComponent.getAttritionPolicy());
                    }
                    if (StringUtils.isNotEmpty(bomComponent.getIssuedLocatorId())) {
                        mtBomComponent.setIssuedLocatorId(bomComponent.getIssuedLocatorId());
                    }
                    mtBomComponent.setAttritionChance(bomComponent.getAttritionChance());
                    mtBomComponent.setAttritionQty(bomComponent.getAttritionQty());
                    mtBomComponent.setQty(bomComponent.getQty());
                    mtBomComponent.setDateFrom(bomComponent.getDateFrom());
                    mtBomComponent.setDateTo(bomComponent.getDateTo());
                    mtBomComponent.setCid(Long.valueOf(newBomComCids.remove(0)));
                    mtBomComponent.setLastUpdateDate(now);
                    mtBomComponent.setLastUpdatedBy(userId);

                    mtBomComponentHis = new MtBomComponentHis();
                    mtBomComponentHis.setTenantId(tenantId);
                    mtBomComponentHis.setBomComponentHisId(newBomComHisIds.remove(0));
                    mtBomComponentHis.setBomComponentId(mtBomComponent.getBomComponentId());
                    mtBomComponentHis.setBomId(mtBomComponent.getBomId());
                    mtBomComponentHis.setLineNumber(mtBomComponent.getLineNumber());
                    mtBomComponentHis.setMaterialId(mtBomComponent.getMaterialId());
                    mtBomComponentHis.setBomComponentType(mtBomComponent.getBomComponentType());
                    mtBomComponentHis.setDateFrom(mtBomComponent.getDateFrom());
                    mtBomComponentHis.setDateTo(mtBomComponent.getDateTo());
                    mtBomComponentHis.setQty(mtBomComponent.getQty());
                    mtBomComponentHis.setKeyMaterialFlag(mtBomComponent.getKeyMaterialFlag());
                    mtBomComponentHis.setAssembleMethod(mtBomComponent.getAssembleMethod());
                    mtBomComponentHis.setAssembleAsReqFlag(mtBomComponent.getAssembleAsReqFlag());
                    mtBomComponentHis.setAttritionPolicy(mtBomComponent.getAttritionPolicy());
                    mtBomComponentHis.setAttritionChance(mtBomComponent.getAttritionChance());
                    mtBomComponentHis.setAttritionQty(mtBomComponent.getAttritionQty());
                    mtBomComponentHis.setCopiedFromComponentId(mtBomComponent.getCopiedFromComponentId());
                    mtBomComponentHis.setIssuedLocatorId(mtBomComponent.getIssuedLocatorId());
                    mtBomComponentHis.setEventId(eventId);
                    mtBomComponentHis.setCid(Long.valueOf(newBomComHisCids.remove(0)));
                    mtBomComponentHis.setCreationDate(now);
                    mtBomComponentHis.setCreatedBy(userId);
                    mtBomComponentHis.setLastUpdateDate(now);
                    mtBomComponentHis.setLastUpdatedBy(userId);
                    mtBomComponentHis.setObjectVersionNumber(1L);
                    mtBomComponent.setLatestHisId(mtBomComponentHis.getBomComponentHisId());
                    newBomComList.add(mtBomComponent);
                    newBomComHisList.add(mtBomComponentHis);

                    if (!Objects.isNull(mtBomSubstituteGroup)) {
                        mtBomSubstituteGroup.setTenantId(tenantId);
                        mtBomSubstituteGroup.setCreatedBy(userId);
                        mtBomSubstituteGroup.setCreationDate(now);
                        mtBomSubstituteGroup.setLastUpdatedBy(userId);
                        mtBomSubstituteGroup.setLastUpdateDate(now);
                        mtBomSubstituteGroup.setObjectVersionNumber(1l);
                        mtBomSubstituteGroup.setCid(userId);
                        newBomGroupList.add(mtBomSubstituteGroup);
                        if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList())) {
                            if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList().get(0).getMtBomSubstituteList())) {
                                List<MtBomSubstituteVO8> mtBomSubstituteList = bomComponent.getMtBomSubstituteGroupList().get(0).getMtBomSubstituteList();
                                mtBomSubstituteMapper.deleteByBomSubstituteGroupId(mtBomSubstituteGroup.getBomSubstituteGroupId());
                                // newBomSubList
                                for (MtBomSubstituteVO8 vo8 : mtBomSubstituteList) {
                                    mtBomSubstitute = new MtBomSubstitute();
                                    BeanUtils.copyProperties(vo8, mtBomSubstitute);
                                    mtBomSubstitute.setBomSubstituteGroupId(mtBomSubstituteGroup.getBomSubstituteGroupId());
                                    mtBomSubstitute.setBomSubstituteId(customDbRepository.getNextKey("mt_bom_substitute_s"));
                                    mtBomSubstitute.setTenantId(tenantId);
                                    mtBomSubstitute.setCreatedBy(userId);
                                    mtBomSubstitute.setCreationDate(now);
                                    mtBomSubstitute.setLastUpdatedBy(userId);
                                    mtBomSubstitute.setLastUpdateDate(now);
                                    mtBomSubstitute.setObjectVersionNumber(1l);
                                    mtBomSubstitute.setCid(userId);
                                    newBomSubList.add(mtBomSubstitute);
                                }
                            }
                        }
                    }
                    bomComAttrMap.put(mtBomComponentHis.getBomComponentId(), mtBomComponentHis.getBomComponentId());
                    bomComponentAttr.put(bomComponent.getBomComponentIface(), mtBomComponentHis.getBomComponentId());
                }
            }
        }
        System.out.println("bom数据处理：" + (System.currentTimeMillis() - start) + "毫秒");
        /**
         * 处理bom数据
         */
        List<String> replaceSql = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(newBomList)) {
            replaceSql.addAll(constructSql(newBomList));
            newBomList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomHisList)) {
            replaceSql.addAll(constructSql(newBomHisList));
            newBomHisList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomSiteAssignList)) {
            replaceSql.addAll(constructSql(newBomSiteAssignList));
            newBomSiteAssignList = null;
        }
        if (CollectionUtils.isNotEmpty(newWoList)) {
            replaceSql.addAll(constructSql(newWoList));
            newWoList = null;
        }
        if (CollectionUtils.isNotEmpty(newWoHisList)) {
            replaceSql.addAll(constructSql(newWoHisList));
            newWoHisList = null;
        }
        if (CollectionUtils.isNotEmpty(newPefpList)) {
            replaceSql.addAll(constructSql(newPefpList));
            newPefpList = null;
        }
        /**
         * 处理bom组件
         */
        if (CollectionUtils.isNotEmpty(newBomComList)) {
            replaceSql.addAll(constructSql(newBomComList));
            newBomComList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomComHisList)) {
            replaceSql.addAll(constructSql(newBomComHisList));
            newBomComHisList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomGroupList)) {
            replaceSql.addAll(constructSql(newBomGroupList));
            newBomGroupList = null;
        }
        if (CollectionUtils.isNotEmpty(newBomSubList)) {
            replaceSql.addAll(constructSql(newBomSubList));
            newBomSubList = null;
        }
        if (CollectionUtils.isNotEmpty(replaceSql)) {
            List<List<String>> commitSqlList = commitSqlList(replaceSql, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }

        if (MapUtils.isNotEmpty(bomAttrMap)) {
            sqlList.addAll(mtExtendSettingsRepository.attrHisBatchUpdate(tenantId, "mt_bom_attr", bomAttrMap, eventId));
        }
        if (MapUtils.isNotEmpty(bomComAttrMap)) {
            sqlList.addAll(mtExtendSettingsRepository.attrHisBatchUpdate(tenantId, "mt_bom_component_attr",
                    bomComAttrMap, eventId));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> splitSqlList = splitSqlList(sqlList);
            for (List<String> sql : splitSqlList) {
                jdbcTemplate.batchUpdate(sql.toArray(new String[sql.size()]));
            }
            sqlList.clear();
        }

        /**
         * 处理扩展属性
         */
        if (MapUtils.isNotEmpty(bomAttr)) {
            sqlList.addAll(saveAttrColumn(tenantId, bomAttr, "mt_bom_attr", "headAttribute"));
        }
        if (MapUtils.isNotEmpty(bomComponentAttr)) {
            //sqlList.addAll(saveAttrColumnAttr(tenantId, bomComponentAttr, "mt_bom_component_attr", "lineAttribute"));
            sqlList.addAll(saveAttrColumn(tenantId, bomComponentAttr, "mt_bom_component_attr", "lineAttribute"));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return result;
    }

}
