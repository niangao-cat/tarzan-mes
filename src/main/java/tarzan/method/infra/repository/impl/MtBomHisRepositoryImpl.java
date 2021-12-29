package tarzan.method.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO9;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.*;

/**
 * 装配清单头历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomHisRepositoryImpl extends BaseRepositoryImpl<MtBomHis> implements MtBomHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtBomComponentHisRepository mtBomComponentHisRepository;

    @Autowired
    private MtBomSubstituteGroupHisRepository mtBomSubstituteGroupHisRepository;

    @Autowired
    private MtBomSubstituteHisRepository mtBomSubstituteHisRepository;

    @Autowired
    private MtBomReferencePointHisRepository mtBomReferencePointHisRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtBomReferencePointMapper mtBomReferencePointMapper;

    @Autowired
    private MtBomComponentMapper mtBomComponentMapper;

    @Autowired
    private MtBomHisMapper mtBomHisMapper;

    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;

    @Autowired
    private MtBomSubstituteGroupMapper mtBomSubstituteGroupMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtBomComponentHisMapper mtBomComponentHisMapper;

    @Autowired
    private MtBomReferencePointHisMapper mtBomReferencePointHisMapper;

    @Autowired
    private MtBomSubstituteGroupHisMapper mtBomSubstituteGroupHisMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    /**
     * bomAllHisCreate-创建装配清单历史 修改名称 bomHisCreate->bomAllHisCreate 19.3.21
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtBomHisVO4 bomAllHisCreate(Long tenantId, MtBomHisVO1 dto) {
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomAllHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "eventTypeCode", "【API:bomAllHisCreate】"));
        }

        // 2. 根据 bomId 获取 Bom 数据
        MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, dto.getBomId());
        if (mtBom == null || StringUtils.isEmpty(mtBom.getBomId())) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomAllHisCreate】"));
        }

        // 3. 生成事件 并 记录事件与对象关系
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
        String eid = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        final String eventId = eid;
        List<String> sqlList = new ArrayList<String>();
        Long userId = DetailsHelper.getUserDetails().getUserId();

        MtBomHisVO4 result = new MtBomHisVO4();
        String bomHisId = null;
        List<String> bomComponentHisId = new ArrayList<String>();
        List<String> bomSubstituteGroupHisId = new ArrayList<String>();
        List<String> bomSubstituteHisId = new ArrayList<String>();
        List<String> bomReferencePointHisId = new ArrayList<String>();

        // 4. 生成历史数据
        // 4.1. 生成 BomHis 数据
        Date currentDate = new Date();
        MtBomHis mtBomHis = new MtBomHis();
        bomHisId = this.customDbRepository.getNextKey("mt_bom_his_s");
        mtBomHis.setTenantId(tenantId);
        mtBomHis.setBomHisId(bomHisId);
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
        mtBomHis.setEventId(eventId);
        mtBomHis.setAssembleAsMaterialFlag(mtBom.getAssembleAsMaterialFlag());
        mtBomHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_bom_his_cid_s")));
        mtBomHis.setCreatedBy(userId);
        mtBomHis.setCreationDate(currentDate);
        mtBomHis.setLastUpdatedBy(userId);
        mtBomHis.setLastUpdateDate(currentDate);
        sqlList.addAll(customDbRepository.getInsertSql(mtBomHis));

        // 更新最新历史ID
        MtBom bom = new MtBom();
        bom.setTenantId(tenantId);
        bom.setBomId(mtBomHis.getBomId());
        bom.setLatestHisId(mtBomHis.getBomHisId());
        sqlList.addAll(customDbRepository.getUpdateSql(bom));


        // 4.2. 生成 BomComponentHis 数据
        // 4.2.1. 查询 BomComponent 数据
        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setBomId(dto.getBomId());
        mtBomComponent.setTenantId(tenantId);
        List<MtBomComponent> mtBomComponentList = mtBomComponentMapper.select(mtBomComponent);

        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            // 4.2.2. 执行生成 BomComponentHis 数据
            for (MtBomComponent c : mtBomComponentList) {
                MtBomComponentHis mtBomComponentHis = new MtBomComponentHis();
                mtBomComponentHis.setTenantId(tenantId);
                mtBomComponentHis.setBomComponentHisId(this.customDbRepository.getNextKey("mt_bom_component_his_s"));
                mtBomComponentHis.setBomComponentId(c.getBomComponentId());
                mtBomComponentHis.setBomId(c.getBomId());
                mtBomComponentHis.setLineNumber(c.getLineNumber());
                mtBomComponentHis.setMaterialId(c.getMaterialId());
                mtBomComponentHis.setBomComponentType(c.getBomComponentType());
                mtBomComponentHis.setDateFrom(c.getDateFrom());
                mtBomComponentHis.setDateTo(c.getDateTo());
                mtBomComponentHis.setQty(c.getQty());
                mtBomComponentHis.setKeyMaterialFlag(c.getKeyMaterialFlag());
                mtBomComponentHis.setAssembleMethod(c.getAssembleMethod());
                mtBomComponentHis.setAssembleAsReqFlag(c.getAssembleAsReqFlag());
                mtBomComponentHis.setAttritionPolicy(c.getAttritionPolicy());
                mtBomComponentHis.setAttritionChance(c.getAttritionChance());
                mtBomComponentHis.setAttritionQty(c.getAttritionQty());
                mtBomComponentHis.setCopiedFromComponentId(c.getCopiedFromComponentId());
                mtBomComponentHis.setIssuedLocatorId(c.getIssuedLocatorId());
                mtBomComponentHis.setEventId(eventId);
                mtBomComponentHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_bom_component_his_cid_s")));
                mtBomComponentHis.setCreatedBy(userId);
                mtBomComponentHis.setCreationDate(currentDate);
                mtBomComponentHis.setLastUpdatedBy(userId);
                mtBomComponentHis.setLastUpdateDate(currentDate);
                mtBomComponentHis.setObjectVersionNumber(1L);
                sqlList.addAll(customDbRepository.getInsertSql(mtBomComponentHis));

                // 生成最新历史ID
                c.setLatestHisId(mtBomComponentHis.getBomComponentHisId());
                sqlList.addAll(customDbRepository.getUpdateSql(c));
                bomComponentHisId.add(mtBomComponentHis.getBomComponentHisId());



                // 新增逻辑 更新历史扩展表
                MtCommonExtendVO6 attrProperty = new MtCommonExtendVO6();
                attrProperty.setKeyId(mtBomComponentHis.getBomComponentId());
                attrPropertyList.add(attrProperty);


                // 4.4.1. 查询 BomSubstituteGroup 数据
                MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
                mtBomSubstituteGroup.setBomComponentId(c.getBomComponentId());
                mtBomSubstituteGroup.setTenantId(tenantId);
                List<MtBomSubstituteGroup> mtBomSubstituteGroupList =
                        mtBomSubstituteGroupMapper.select(mtBomSubstituteGroup);

                if (CollectionUtils.isNotEmpty(mtBomSubstituteGroupList)) {
                    // 4.4.2. 执行生成 BomSubstituteGroupHis 数据
                    for (MtBomSubstituteGroup s : mtBomSubstituteGroupList) {
                        MtBomSubstituteGroupHis mtBomSubstituteGroupHis = new MtBomSubstituteGroupHis();
                        mtBomSubstituteGroupHis.setTenantId(tenantId);
                        mtBomSubstituteGroupHis.setBomSubstituteGroupHisId(
                                this.customDbRepository.getNextKey("mt_bom_substitute_group_his_s"));
                        mtBomSubstituteGroupHis.setBomSubstituteGroupId(s.getBomSubstituteGroupId());
                        mtBomSubstituteGroupHis.setBomComponentId(s.getBomComponentId());
                        mtBomSubstituteGroupHis.setSubstituteGroup(s.getSubstituteGroup());
                        mtBomSubstituteGroupHis.setSubstitutePolicy(s.getSubstitutePolicy());
                        mtBomSubstituteGroupHis.setEnableFlag(s.getEnableFlag());
                        mtBomSubstituteGroupHis.setCopiedFromGroupId(s.getCopiedFromGroupId());
                        mtBomSubstituteGroupHis.setEventId(eventId);
                        mtBomSubstituteGroupHis.setCid(Long
                                .valueOf(this.customDbRepository.getNextKey("mt_bom_substitute_group_his_cid_s")));
                        mtBomSubstituteGroupHis.setCreatedBy(userId);
                        mtBomSubstituteGroupHis.setCreationDate(currentDate);
                        mtBomSubstituteGroupHis.setLastUpdatedBy(userId);
                        mtBomSubstituteGroupHis.setLastUpdateDate(currentDate);
                        sqlList.addAll(customDbRepository.getInsertSql(mtBomSubstituteGroupHis));
                        bomSubstituteGroupHisId.add(mtBomSubstituteGroupHis.getBomSubstituteGroupHisId());

                        // 4.5 生成 BomSubstituteHis 数据
                        // 4.5.1 查询 BomSubstitute 数据
                        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
                        mtBomSubstitute.setTenantId(tenantId);
                        mtBomSubstitute.setBomSubstituteGroupId(s.getBomSubstituteGroupId());
                        List<MtBomSubstitute> mtBomSubstituteList = mtBomSubstituteMapper.select(mtBomSubstitute);

                        if (CollectionUtils.isNotEmpty(mtBomSubstituteList)) {
                            // 4.5.2. 执行生成 BomSubstituteHis 数据
                            for (MtBomSubstitute sb : mtBomSubstituteList) {
                                MtBomSubstituteHis mtBomSubstituteHis = new MtBomSubstituteHis();
                                mtBomSubstituteHis.setTenantId(tenantId);
                                mtBomSubstituteHis.setBomSubstituteHisId(
                                        this.customDbRepository.getNextKey("mt_bom_substitute_his_s"));
                                mtBomSubstituteHis.setBomSubstituteId(sb.getBomSubstituteId());
                                mtBomSubstituteHis.setBomSubstituteGroupId(sb.getBomSubstituteGroupId());
                                mtBomSubstituteHis.setMaterialId(sb.getMaterialId());
                                mtBomSubstituteHis.setDateFrom(sb.getDateFrom());
                                mtBomSubstituteHis.setDateTo(sb.getDateTo());
                                mtBomSubstituteHis.setSubstituteValue(sb.getSubstituteValue());
                                mtBomSubstituteHis.setCopiedFromSubstituteId(sb.getCopiedFromSubstituteId());
                                mtBomSubstituteHis.setSubstituteUsage(sb.getSubstituteUsage());
                                mtBomSubstituteHis.setEventId(eventId);
                                mtBomSubstituteHis.setCid(Long.valueOf(
                                        this.customDbRepository.getNextKey("mt_bom_substitute_his_cid_s")));
                                mtBomSubstituteHis.setCreatedBy(userId);
                                mtBomSubstituteHis.setCreationDate(currentDate);
                                mtBomSubstituteHis.setLastUpdatedBy(userId);
                                mtBomSubstituteHis.setLastUpdateDate(currentDate);
                                sqlList.addAll(customDbRepository.getInsertSql(mtBomSubstituteHis));
                                bomSubstituteHisId.add(mtBomSubstituteHis.getBomSubstituteHisId());
                            }
                        }
                    }
                }

                // 4.6. 生成 BomReferencePointHis 数据
                // 4.6.1. 查询 BomReferencePoint 数据
                MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
                mtBomReferencePoint.setTenantId(tenantId);
                mtBomReferencePoint.setBomComponentId(c.getBomComponentId());
                List<MtBomReferencePoint> mtBomReferencePointList =
                        mtBomReferencePointMapper.select(mtBomReferencePoint);

                if (CollectionUtils.isNotEmpty(mtBomReferencePointList)) {
                    // 4.6.2. 执行生成 BomSubstituteHis 数据
                    for (MtBomReferencePoint r : mtBomReferencePointList) {
                        MtBomReferencePointHis mtBomReferencePointHis = new MtBomReferencePointHis();
                        mtBomReferencePointHis.setTenantId(tenantId);
                        mtBomReferencePointHis.setBomReferencePointHisId(
                                this.customDbRepository.getNextKey("mt_bom_reference_point_his_s"));
                        mtBomReferencePointHis.setBomReferencePointId(r.getBomReferencePointId());
                        mtBomReferencePointHis.setReferencePoint(r.getReferencePoint());
                        mtBomReferencePointHis.setQty(r.getQty());
                        mtBomReferencePointHis.setBomComponentId(r.getBomComponentId());
                        mtBomReferencePointHis.setLineNumber(r.getLineNumber());
                        mtBomReferencePointHis.setEnableFlag(r.getEnableFlag());
                        mtBomReferencePointHis.setCopiedFromPointId(r.getCopiedFromPointId());
                        mtBomReferencePointHis.setEventId(eventId);
                        mtBomReferencePointHis.setCid(Long
                                .valueOf(this.customDbRepository.getNextKey("mt_bom_reference_point_his_cid_s")));
                        mtBomReferencePointHis.setCreatedBy(userId);
                        mtBomReferencePointHis.setCreationDate(currentDate);
                        mtBomReferencePointHis.setLastUpdatedBy(userId);
                        mtBomReferencePointHis.setLastUpdateDate(currentDate);
                        sqlList.addAll(customDbRepository.getInsertSql(mtBomReferencePointHis));
                        bomReferencePointHisId.add(mtBomReferencePointHis.getBomReferencePointHisId());
                    }
                }
            }
        }


        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        // 批量更新历史扩展
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_bom_component_attr", eventId,
                    attrPropertyList);
        }

        result.setBomHisId(bomHisId);
        result.setBomComponentHisId(bomComponentHisId);
        result.setBomReferencePointHisId(bomReferencePointHisId);
        result.setBomSubstituteGroupHisId(bomSubstituteGroupHisId);
        result.setBomSubstituteHisId(bomSubstituteHisId);
        return result;
    }

    /**
     * bomHisQuery-获取装配清单历史
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    @Override
    public List<MtBomHis> bomHisQuery(Long tenantId, String bomId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomHisQuery】"));
        }
        return mtBomHisMapper.selectByBomId(tenantId, bomId);
    }

    /**
     * bomAllHisQuery-获取装配清单整体历史
     *
     * @param tenantId
     * @param bomId
     * @return
     */
    @Override
    public List<MtBomHisVO2> bomAllHisQuery(Long tenantId, String bomId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomAllHisQuery】"));
        }

        // 2. 获取历史数据
        List<MtBomHisVO2> resultList = new ArrayList<>();

        // 2.1. 获取 BomHis 数据
        List<MtBomHis> bomHisList = bomHisQuery(tenantId, bomId);
        if (CollectionUtils.isNotEmpty(bomHisList)) {
            for (MtBomHis tempBomHis : bomHisList) {
                MtBomHisVO2 mtBomHisVo2 = new MtBomHisVO2();
                BeanUtils.copyProperties(tempBomHis, mtBomHisVo2);

                // 2.2 获取 BomComponentHis 数据
                List<MtBomComponentHisVO1> bomComponentHisVo1List = new ArrayList<>();

                MtBomComponentHis mtBomComponentHis = new MtBomComponentHis();
                mtBomComponentHis.setTenantId(tenantId);
                mtBomComponentHis.setBomId(bomId);
                mtBomComponentHis.setEventId(tempBomHis.getEventId());

                Criteria criteria = new Criteria(mtBomComponentHis);
                List<WhereField> whereFields = new ArrayList<WhereField>();
                whereFields = new ArrayList<WhereField>();
                whereFields.add(new WhereField(MtBomComponentHis.FIELD_TENANT_ID, Comparison.EQUAL));
                whereFields.add(new WhereField(MtBomComponentHis.FIELD_BOM_ID, Comparison.EQUAL));
                whereFields.add(new WhereField(MtBomComponentHis.FIELD_EVENT_ID, Comparison.EQUAL));
                criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
                List<MtBomComponentHis> bomComponentHisList =
                        mtBomComponentHisRepository.selectOptional(mtBomComponentHis, criteria);

                if (CollectionUtils.isNotEmpty(bomComponentHisList)) {
                    for (MtBomComponentHis tempBomComponentHis : bomComponentHisList) {
                        MtBomComponentHisVO1 bomComponentHisVo1 = new MtBomComponentHisVO1();
                        BeanUtils.copyProperties(tempBomComponentHis, bomComponentHisVo1);

                        // 2.4. 获取 BomReferencePointHis 数据
                        MtBomReferencePointHis mtBomReferencePointHis = new MtBomReferencePointHis();
                        mtBomReferencePointHis.setTenantId(tenantId);
                        mtBomReferencePointHis.setBomComponentId(tempBomComponentHis.getBomComponentId());
                        mtBomReferencePointHis.setEventId(tempBomComponentHis.getEventId());

                        criteria = new Criteria(mtBomReferencePointHis);
                        whereFields = new ArrayList<WhereField>();
                        whereFields.add(new WhereField(MtBomReferencePointHis.FIELD_TENANT_ID, Comparison.EQUAL));
                        whereFields.add(new WhereField(MtBomReferencePointHis.FIELD_BOM_COMPONENT_ID,
                                Comparison.EQUAL));
                        whereFields.add(new WhereField(MtBomReferencePointHis.FIELD_EVENT_ID, Comparison.EQUAL));
                        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
                        List<MtBomReferencePointHis> bomReferencePointHisList = mtBomReferencePointHisRepository
                                .selectOptional(mtBomReferencePointHis, criteria);

                        // 设置组件对应 eferencePoint 历史
                        bomComponentHisVo1.setBomReferencePointHisList(bomReferencePointHisList);

                        // 2.5 获取 MtBomSubstituteGroupHis 数据
                        List<MtBomSubstituteGroupHisVO1> bomSubstituteGroupHisVo1List = new ArrayList<>();
                        MtBomSubstituteGroupHis mtBomSubstituteGroupHis = new MtBomSubstituteGroupHis();
                        mtBomSubstituteGroupHis.setTenantId(tenantId);
                        mtBomSubstituteGroupHis.setBomComponentId(tempBomComponentHis.getBomComponentId());
                        mtBomSubstituteGroupHis.setEventId(tempBomComponentHis.getEventId());

                        criteria = new Criteria(mtBomSubstituteGroupHis);
                        whereFields = new ArrayList<WhereField>();
                        whereFields.add(new WhereField(MtBomSubstituteGroupHis.FIELD_TENANT_ID, Comparison.EQUAL));
                        whereFields.add(new WhereField(MtBomSubstituteGroupHis.FIELD_BOM_COMPONENT_ID,
                                Comparison.EQUAL));
                        whereFields.add(new WhereField(MtBomSubstituteGroupHis.FIELD_EVENT_ID, Comparison.EQUAL));
                        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
                        List<MtBomSubstituteGroupHis> bomSubstituteGroupHisList = mtBomSubstituteGroupHisRepository
                                .selectOptional(mtBomSubstituteGroupHis, criteria);

                        if (CollectionUtils.isNotEmpty(bomSubstituteGroupHisList)) {
                            for (MtBomSubstituteGroupHis tempBomSubstituteGroupHis : bomSubstituteGroupHisList) {
                                MtBomSubstituteGroupHisVO1 mtBomSubstituteGroupHisVo1 =
                                        new MtBomSubstituteGroupHisVO1();
                                BeanUtils.copyProperties(tempBomSubstituteGroupHis, mtBomSubstituteGroupHisVo1);

                                // 2.6 获取 BomSubstituteHis 数据
                                MtBomSubstituteHis mtBomSubstituteHis = new MtBomSubstituteHis();
                                mtBomSubstituteHis.setTenantId(tenantId);
                                mtBomSubstituteHis.setBomSubstituteGroupId(
                                        tempBomSubstituteGroupHis.getBomSubstituteGroupId());
                                mtBomSubstituteHis.setEventId(tempBomSubstituteGroupHis.getEventId());

                                criteria = new Criteria(mtBomSubstituteHis);
                                whereFields = new ArrayList<WhereField>();
                                whereFields.add(new WhereField(MtBomSubstituteHis.FIELD_TENANT_ID, Comparison.EQUAL));
                                whereFields.add(new WhereField(MtBomSubstituteHis.FIELD_BOM_SUBSTITUTE_GROUP_ID,
                                        Comparison.EQUAL));
                                whereFields.add(new WhereField(MtBomSubstituteHis.FIELD_EVENT_ID, Comparison.EQUAL));
                                criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
                                List<MtBomSubstituteHis> bomSubstituteHisList = mtBomSubstituteHisRepository
                                        .selectOptional(mtBomSubstituteHis, criteria);

                                // 设置 group 对应 substitute 历史
                                mtBomSubstituteGroupHisVo1.setBomSubstituteHisList(bomSubstituteHisList);

                                bomSubstituteGroupHisVo1List.add(mtBomSubstituteGroupHisVo1);
                            }
                        }

                        // 设置组件对应 group 历史
                        bomComponentHisVo1.setBomSubstituteGroupHisList(bomSubstituteGroupHisVo1List);

                        bomComponentHisVo1List.add(bomComponentHisVo1);
                    }
                }

                // 设置 Bom 对应 组件历史
                mtBomHisVo2.setBomComponentHisList(bomComponentHisVo1List);

                resultList.add(mtBomHisVo2);
            }
        }

        return resultList;
    }

    @Override
    public List<MtBomHisVO3> eventLimitBomAllHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "eventId", "【API:eventLimitBomAllHisQuery】"));
        }

        MtBomHis mtBomHis = new MtBomHis();
        mtBomHis.setTenantId(tenantId);
        mtBomHis.setEventId(eventId);
        List<MtBomHis> mtBomHiz = this.mtBomHisMapper.select(mtBomHis);
        if (CollectionUtils.isEmpty(mtBomHiz)) {
            return Collections.emptyList();
        }

        List<MtBomHisVO3> result = new ArrayList<MtBomHisVO3>(mtBomHiz.size());
        MtBomHisVO3 mtBomHisVo3 = null;
        MtBomHis copyMtBomHis = null;
        MtBomComponentHis searchMtBomComponentHis = null;

        for (MtBomHis tmpMtBomHis : mtBomHiz) {
            mtBomHisVo3 = new MtBomHisVO3();

            copyMtBomHis = new MtBomHis();
            BeanUtils.copyProperties(tmpMtBomHis, copyMtBomHis);
            mtBomHisVo3.setBomHis(copyMtBomHis);

            searchMtBomComponentHis = new MtBomComponentHis();
            searchMtBomComponentHis.setTenantId(tenantId);
            searchMtBomComponentHis.setBomId(tmpMtBomHis.getBomId());
            searchMtBomComponentHis.setEventId(tmpMtBomHis.getEventId());
            List<MtBomComponentHis> mtBomComponentHiz = this.mtBomComponentHisMapper.select(searchMtBomComponentHis);

            if (CollectionUtils.isNotEmpty(mtBomComponentHiz)) {
                List<MtBomComponentHisVO1> list = new ArrayList<MtBomComponentHisVO1>(mtBomComponentHiz.size());
                MtBomComponentHisVO1 copyMtBomComponentHisVo1 = null;
                MtBomReferencePointHis searchMtBomReferencePointHis = null;

                for (MtBomComponentHis tmpMtBomComponentHis : mtBomComponentHiz) {
                    copyMtBomComponentHisVo1 = new MtBomComponentHisVO1();
                    BeanUtils.copyProperties(tmpMtBomComponentHis, copyMtBomComponentHisVo1);

                    searchMtBomReferencePointHis = new MtBomReferencePointHis();
                    searchMtBomReferencePointHis.setTenantId(tenantId);
                    searchMtBomReferencePointHis.setBomComponentId(tmpMtBomComponentHis.getBomComponentId());
                    searchMtBomReferencePointHis.setEventId(tmpMtBomComponentHis.getEventId());
                    List<MtBomReferencePointHis> mtBomReferencePointHiz =
                            this.mtBomReferencePointHisMapper.select(searchMtBomReferencePointHis);

                    copyMtBomComponentHisVo1.setBomReferencePointHisList(mtBomReferencePointHiz);

                    List<MtBomSubstituteGroupHisVO1> mtBomSubstituteGroupHiz = this.mtBomSubstituteGroupHisMapper
                            .selectBomSubstituteGroup(tenantId, tmpMtBomComponentHis.getBomComponentId(),
                                    tmpMtBomComponentHis.getEventId());
                    copyMtBomComponentHisVo1.setBomSubstituteGroupHisList(mtBomSubstituteGroupHiz);

                    list.add(copyMtBomComponentHisVo1);
                }
                mtBomHisVo3.setBomComponentHis(list);
            }

            result.add(mtBomHisVo3);
        }

        return result;
    }

    @Override
    public List<MtBomHis> eventLimitBomHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "eventId", "【API:eventLimitBomHisBatchQuery】"));
        }
        return this.mtBomHisMapper.selectByEventIds(tenantId, eventIds);
    }

    @Override
    public MtBomHisVO5 bomLatestHisGet(Long tenantId, String bomId) {
        // 1 判断输入参数是否合规：
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomLatestHisGet】"));
        }
        // 根据输入参数bomId依据倒叙逻辑在表MT_BOM_HIS获取最新的bomHisId和eventId进行输出
        return mtBomHisMapper.selectRecent(tenantId, bomId);
    }

}
