package tarzan.method.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.util.MtSqlHelper;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.entity.MtBomSubstituteGroup;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomHisRepository;
import tarzan.method.domain.repository.MtBomSubstituteGroupRepository;
import tarzan.method.domain.repository.MtBomSubstituteRepository;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.MtBomComponentMapper;
import tarzan.method.infra.mapper.MtBomSubstituteGroupMapper;
import tarzan.method.infra.mapper.MtBomSubstituteMapper;

/**
 * 装配清单行替代组 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomSubstituteGroupRepositoryImpl extends BaseRepositoryImpl<MtBomSubstituteGroup>
                implements MtBomSubstituteGroupRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomSubstituteGroupMapper mtBomSubstituteGroupMapper;

    @Autowired
    private MtBomComponentMapper mtBomComponentMapper;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtBomHisRepository mtBomHisRepository;

    @Autowired
    private CustomSequence customSequence;

    @Override
    public MtBomSubstituteGroup bomSubstituteGroupBasicGet(Long tenantId, String bomSubstituteGroupId) {
        if (StringUtils.isEmpty(bomSubstituteGroupId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomSubstituteGroupId", "【API:bomSubstituteGroupBasicGet】"));
        }

        MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
        mtBomSubstituteGroup.setTenantId(tenantId);
        mtBomSubstituteGroup.setBomSubstituteGroupId(bomSubstituteGroupId);
        return this.mtBomSubstituteGroupMapper.selectOne(mtBomSubstituteGroup);
    }

    @Override
    public List<MtBomSubstituteGroupVO3> bomSubstituteQuery(Long tenantId, String bomComponentId) {
        if (StringUtils.isEmpty(bomComponentId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomSubstituteQuery】"));
        }

        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setTenantId(tenantId);
        mtBomComponent.setBomComponentId(bomComponentId);
        mtBomComponent = this.mtBomComponentMapper.selectOne(mtBomComponent);
        if (mtBomComponent == null) {
            throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0015", "BOM", "【API:bomSubstituteQuery】"));
        }

        MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
        mtBomSubstituteGroup.setTenantId(tenantId);
        mtBomSubstituteGroup.setBomComponentId(mtBomComponent.getBomComponentId());
        mtBomSubstituteGroup.setEnableFlag("Y");
        List<MtBomSubstituteGroup> mtBomSubstituteGroups = this.mtBomSubstituteGroupMapper.select(mtBomSubstituteGroup);
        if (CollectionUtils.isEmpty(mtBomSubstituteGroups)) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isNotEmpty(mtBomSubstituteGroups) && mtBomSubstituteGroups.size() > 1) {
            throw new MtException("MT_BOM_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0016", "BOM", "【API:bomSubstituteQuery】"));
        }

        List<MtBomSubstitute> mtBomSubstitutes = this.mtBomSubstituteMapper.selectEnableSubstitute(tenantId,
                        mtBomSubstituteGroups.get(0).getBomSubstituteGroupId());
        if (CollectionUtils.isEmpty(mtBomSubstitutes)) {
            return Collections.emptyList();
        }

        final List<MtBomSubstituteGroupVO3> list = new ArrayList<MtBomSubstituteGroupVO3>();
        mtBomSubstitutes.stream().forEach(t -> {
            MtBomSubstituteGroupVO3 vo = new MtBomSubstituteGroupVO3();
            vo.setBomSubstituteGroupId(t.getBomSubstituteGroupId());
            vo.setBomSubstituteId(t.getBomSubstituteId());
            vo.setBomSubstituteUsage(t.getSubstituteUsage());
            list.add(vo);
        });
        return list;
    }

    @Override
    public List<Map<String, String>> componentLimitBomSubstituteGroupQuery(Long tenantId, String bomComponentId) {
        if (StringUtils.isEmpty(bomComponentId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:componentLimitBomSubstituteGroupQuery】"));
        }

        MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
        mtBomSubstituteGroup.setTenantId(tenantId);
        mtBomSubstituteGroup.setBomComponentId(bomComponentId);
        List<MtBomSubstituteGroup> mtBomSubstituteGroups = this.mtBomSubstituteGroupMapper.select(mtBomSubstituteGroup);
        if (CollectionUtils.isEmpty(mtBomSubstituteGroups)) {
            return Collections.emptyList();
        }

        final List<Map<String, String>> list = new ArrayList<>();
        mtBomSubstituteGroups.stream().forEach(t -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("bomComponentId", t.getBomComponentId());
            map.put("bomSubstituteGroupId", t.getBomSubstituteGroupId());
            list.add(map);
        });
        return list;
    }

    @Override
    public List<MtBomSubstituteGroupVO2> propertyLimitBomSubstituteGroupQuery(Long tenantId,
                    MtBomSubstituteGroupVO condition) {
        if (condition.getBomComponentId() == null && condition.getBomId() == null && condition.getEnableFlag() == null
                        && condition.getOnlyAvailableFlag() == null && condition.getSubstituteGroup() == null
                        && condition.getSubstitutePolicy() == null) {
            throw new MtException("MT_BOM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0002", "BOM", "【API:propertyLimitBomSubstituteGroupQuery】"));
        }

        List<MtBomSubstituteGroupVO2> list = this.mtBomSubstituteGroupMapper.selectConditionCustom(tenantId, condition);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list;
    }

    @Override
    public List<MtBomSubstituteGroup> bomSubstituteGroupBasicBatchGet(Long tenantId,
                    List<String> bomSubstituteGroupIds) {
        if (CollectionUtils.isEmpty(bomSubstituteGroupIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomSubstituteGroupId", "【API:bomSubstituteGroupBasicBatchGet】"));
        }
        return this.mtBomSubstituteGroupMapper.selectByIdsCustom(tenantId, bomSubstituteGroupIds);
    }

    /**
     * bomSubstituteGroupUpdate-新增更新装配清单组件行替
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bomSubstituteGroupUpdate(Long tenantId, MtBomSubstituteGroup dto) {
        List<String> enableFlags = Arrays.asList("Y", "N");
        String bomSubstituteGroupId = dto.getBomSubstituteGroupId();
        if (StringUtils.isEmpty(bomSubstituteGroupId)) {
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomSubstituteGroupUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getSubstituteGroup())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "substituteGroup", "【API:bomSubstituteGroupUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getSubstitutePolicy())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "substitutePolicy", "【API:bomSubstituteGroupUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "enableFlag", "【API:bomSubstituteGroupUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0053", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }
            if (!"CHANCE".equals(dto.getSubstitutePolicy()) && !"PRIORITY".equals(dto.getSubstitutePolicy())) {
                throw new MtException("MT_BOM_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0052", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }

            MtBomComponent mtBomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (mtBomComponent == null || StringUtils.isEmpty(mtBomComponent.getBomComponentId())) {
                throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0015", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }

            MtBomSubstituteGroup existBomSubstituteGroup = new MtBomSubstituteGroup();
            existBomSubstituteGroup.setTenantId(tenantId);
            existBomSubstituteGroup.setBomComponentId(dto.getBomComponentId());
            existBomSubstituteGroup.setSubstituteGroup(dto.getSubstituteGroup());
            existBomSubstituteGroup = mtBomSubstituteGroupMapper.selectOne(existBomSubstituteGroup);
            if (null != existBomSubstituteGroup) {
                throw new MtException("MT_BOM_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0029", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);

            MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
            mtBomHisVO1.setBomId(mtBomComponent.getBomId());
            mtBomHisVO1.setEventTypeCode("BOM_SUBSTITUTE_GROUP_CREATE");
            mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);

            bomSubstituteGroupId = dto.getBomSubstituteGroupId();
        } else {
            if (null != dto.getBomComponentId() && "".equals(dto.getBomComponentId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomSubstituteGroupUpdate】"));
            }
            if (null != dto.getSubstituteGroup() && "".equals(dto.getSubstituteGroup())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "substituteGroup", "【API:bomSubstituteGroupUpdate】"));
            }
            if (null != dto.getSubstitutePolicy() && "".equals(dto.getSubstitutePolicy())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "substitutePolicy", "【API:bomSubstituteGroupUpdate】"));
            }
            if (null != dto.getSubstitutePolicy() && !"CHANCE".equals(dto.getSubstitutePolicy())
                            && !"PRIORITY".equals(dto.getSubstitutePolicy())) {
                throw new MtException("MT_BOM_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0052", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "enableFlag", "【API:bomSubstituteGroupUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0053", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }

            MtBomSubstituteGroup mtBomSubstituteGroup =
                            bomSubstituteGroupBasicGet(tenantId, dto.getBomSubstituteGroupId());
            if (null == mtBomSubstituteGroup) {
                throw new MtException("MT_BOM_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0020", "BOM", dto.getBomSubstituteGroupId(), "【API:bomSubstituteGroupUpdate】"));
            }

            String bomComponentId = null == dto.getBomComponentId() ? mtBomSubstituteGroup.getBomComponentId()
                            : dto.getBomComponentId();

            MtBomSubstituteGroup existBomSubstituteGroup = new MtBomSubstituteGroup();
            existBomSubstituteGroup.setTenantId(tenantId);
            existBomSubstituteGroup.setBomSubstituteGroupId(dto.getBomSubstituteGroupId());
            existBomSubstituteGroup.setBomComponentId(bomComponentId);
            existBomSubstituteGroup = mtBomSubstituteGroupMapper.selectOne(existBomSubstituteGroup);
            if (null == existBomSubstituteGroup) {
                throw new MtException("MT_BOM_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0050", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }

            MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, bomComponentId);
            if (null == mtBomComponent) {
                throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0015", "BOM", "【API:bomSubstituteGroupUpdate】"));
            }

            mtBomSubstituteGroup.setTenantId(tenantId);
            mtBomSubstituteGroup.setBomComponentId(dto.getBomComponentId());
            mtBomSubstituteGroup.setSubstituteGroup(dto.getSubstituteGroup());
            mtBomSubstituteGroup.setSubstitutePolicy(dto.getSubstitutePolicy());
            mtBomSubstituteGroup.setEnableFlag(dto.getEnableFlag());
            self().updateByPrimaryKeySelective(mtBomSubstituteGroup);

            MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
            mtBomHisVO1.setBomId(mtBomComponent.getBomId());
            mtBomHisVO1.setEventTypeCode("BOM_SUBSTITUTE_GROUP_UPDATE");
            mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);
        }

        return bomSubstituteGroupId;
    }

    /**
     * 根据来源Bom组件SubstituteGroup更新目标Bom组件SubstituteGroup
     *
     * @author chuang.yang
     * @date 2019/4/27
     * @param tenantId
     * @param sourceBomComponentId
     * @param targetBomComponentId
     * @param now
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> sourceLimitTargetBomSubstituteGroupUpdateGet(Long tenantId, String sourceBomComponentId,
                    String targetBomComponentId, Date now) {
        MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
        mtBomSubstituteGroup.setTenantId(tenantId);
        mtBomSubstituteGroup.setBomComponentId(sourceBomComponentId);
        List<MtBomSubstituteGroup> sourceBomSubstituteGroups = mtBomSubstituteGroupMapper.select(mtBomSubstituteGroup);

        mtBomSubstituteGroup = new MtBomSubstituteGroup();
        mtBomSubstituteGroup.setTenantId(tenantId);
        mtBomSubstituteGroup.setBomComponentId(targetBomComponentId);
        List<MtBomSubstituteGroup> targetBomSubstituteGroups = mtBomSubstituteGroupMapper.select(mtBomSubstituteGroup);

        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isEmpty(sourceBomSubstituteGroups) && CollectionUtils.isEmpty(targetBomSubstituteGroups)) {
            return sqlList;
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();

        // 来源有，目标全无，则以来源数据新增
        if (CollectionUtils.isEmpty(targetBomSubstituteGroups)) {
            for (MtBomSubstituteGroup s : sourceBomSubstituteGroups) {
                MtBomSubstituteGroup newBomSubstituteGroup = new MtBomSubstituteGroup();
                newBomSubstituteGroup.setTenantId(tenantId);
                newBomSubstituteGroup
                                .setBomSubstituteGroupId(this.customSequence.getNextKey("mt_bom_substitute_group_s"));
                newBomSubstituteGroup.setBomComponentId(targetBomComponentId);
                newBomSubstituteGroup.setSubstituteGroup(s.getSubstituteGroup());
                newBomSubstituteGroup.setSubstitutePolicy(s.getSubstitutePolicy());
                newBomSubstituteGroup.setEnableFlag(s.getEnableFlag());
                newBomSubstituteGroup.setCopiedFromGroupId(s.getBomSubstituteGroupId());
                newBomSubstituteGroup
                                .setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                newBomSubstituteGroup.setCreatedBy(userId);
                newBomSubstituteGroup.setCreationDate(now);
                newBomSubstituteGroup.setLastUpdateDate(now);
                newBomSubstituteGroup.setLastUpdatedBy(userId);
                newBomSubstituteGroup.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstituteGroup));

                // 处理替代项
                List<String> bomSubstituteSqlList = mtBomSubstituteRepository.sourceLimitTargetBomSubstituteUpdateGet(
                                tenantId, s.getBomSubstituteGroupId(), newBomSubstituteGroup.getBomSubstituteGroupId(),
                                now);
                sqlList.addAll(bomSubstituteSqlList);
            }
        } else if (CollectionUtils.isEmpty(sourceBomSubstituteGroups)) {
            for (MtBomSubstituteGroup t : targetBomSubstituteGroups) {
                t.setTenantId(tenantId);
                t.setEnableFlag("N");
                t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                t.setLastUpdateDate(now);
                t.setLastUpdatedBy(userId);
                sqlList.addAll(MtSqlHelper.getUpdateSql(t));
            }
        } else {
            // 目标有，来源也有，则匹配 SUBSTITUTE_GROUP 相同的数据
            Map<String, MtBomSubstituteGroup> sourceBomSubstituteGroupMap = sourceBomSubstituteGroups.stream()
                            .collect(Collectors.toMap(m -> m.getBomSubstituteGroupId(), m -> m));

            for (MtBomSubstituteGroup t : targetBomSubstituteGroups) {
                // 对每一个目标组件筛选来源
                List<MtBomSubstituteGroup> result = sourceBomSubstituteGroups.stream()
                                .filter(s -> s.getSubstituteGroup().equals(t.getSubstituteGroup()))
                                .collect(Collectors.toList());

                // 如果无对应来源，则无效改目标
                if (CollectionUtils.isEmpty(result)) {
                    t.setTenantId(tenantId);
                    t.setEnableFlag("N");
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                    t.setLastUpdateDate(now);
                    t.setLastUpdatedBy(userId);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));
                } else {
                    MtBomSubstituteGroup tempSource = result.get(0);

                    // 剔除来源Map数据: 筛选结果唯一
                    sourceBomSubstituteGroupMap.remove(tempSource.getBomSubstituteGroupId());

                    // 根据筛选出来的来源更新该目标
                    t.setTenantId(tenantId);
                    t.setSubstitutePolicy(tempSource.getSubstitutePolicy());
                    t.setEnableFlag(tempSource.getEnableFlag());
                    t.setCopiedFromGroupId(tempSource.getBomSubstituteGroupId());
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                    t.setLastUpdateDate(now);
                    t.setLastUpdatedBy(userId);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));

                    // 处理替代项
                    List<String> bomSubstituteSqlList =
                                    mtBomSubstituteRepository.sourceLimitTargetBomSubstituteUpdateGet(tenantId,
                                                    tempSource.getBomSubstituteGroupId(), t.getBomSubstituteGroupId(),
                                                    now);
                    sqlList.addAll(bomSubstituteSqlList);
                }
            }

            // 如果处理完所有目标后，还有来源未筛选到，则对这部分来源执行目标新增
            if (!MapUtils.isEmpty(sourceBomSubstituteGroupMap)) {
                for (Map.Entry<String, MtBomSubstituteGroup> entry : sourceBomSubstituteGroupMap.entrySet()) {
                    MtBomSubstituteGroup sourceBomSubstituteGroup = entry.getValue();

                    MtBomSubstituteGroup newBomSubstituteGroup = new MtBomSubstituteGroup();
                    newBomSubstituteGroup.setTenantId(tenantId);
                    newBomSubstituteGroup.setBomSubstituteGroupId(
                                    this.customSequence.getNextKey("mt_bom_substitute_group_s"));
                    newBomSubstituteGroup.setBomComponentId(targetBomComponentId);
                    newBomSubstituteGroup.setSubstituteGroup(sourceBomSubstituteGroup.getSubstituteGroup());
                    newBomSubstituteGroup.setSubstitutePolicy(sourceBomSubstituteGroup.getSubstitutePolicy());
                    newBomSubstituteGroup.setEnableFlag(sourceBomSubstituteGroup.getEnableFlag());
                    newBomSubstituteGroup.setCopiedFromGroupId(sourceBomSubstituteGroup.getBomSubstituteGroupId());
                    newBomSubstituteGroup.setCid(
                                    Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                    newBomSubstituteGroup.setCreatedBy(userId);
                    newBomSubstituteGroup.setCreationDate(now);
                    newBomSubstituteGroup.setLastUpdateDate(now);
                    newBomSubstituteGroup.setLastUpdatedBy(userId);
                    newBomSubstituteGroup.setObjectVersionNumber(1L);
                    sqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstituteGroup));
                }
            }
        }
        return sqlList;
    }

    @Override
    public List<MtBomSubstituteGroupVO7> bomSubstituteBatchQuery(Long tenantId, List<String> bomComponentIds) {
        // 参数校验
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomSubstituteBatchQuery】"));
        }

        // 返回值
        List<MtBomSubstituteGroupVO7> result = new ArrayList<>(bomComponentIds.size());

        // 获取所有的组件
        List<MtBomComponent> mtBomComponents =
                        mtBomComponentRepository.selectBomComponentByBomComponentIds(tenantId, bomComponentIds);
        List<String> existsIds = mtBomComponents.stream().map(MtBomComponent::getBomComponentId)
                        .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        bomComponentIds.removeAll(existsIds);
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            throw new MtException("MT_BOM_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0041", "BOM", bomComponentIds.toString(), "【API:bomSubstituteBatchQuery】"));
        }

        // 获取所有的的替代组
        SecurityTokenHelper.close();
        List<MtBomSubstituteGroup> mtBomSubstituteGroups = this.mtBomSubstituteGroupMapper.selectByCondition(Condition
                        .builder(MtBomSubstituteGroup.class)
                        .andWhere(Sqls.custom().andEqualTo(MtBomSubstituteGroup.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtBomSubstituteGroup.FIELD_ENABLE_FLAG, MtBaseConstants.YES))
                        .andWhere(Sqls.custom().andIn(MtBomSubstituteGroup.FIELD_BOM_COMPONENT_ID, existsIds)).build());
        if (CollectionUtils.isNotEmpty(mtBomSubstituteGroups)) {

            // 一个组件仅对应一个替代组
            Map<String, String> subGroupMap;
            Map<String, String> substitutePolicyMap;
            List<String> subGroupIds = mtBomSubstituteGroups.stream().map(MtBomSubstituteGroup::getBomSubstituteGroupId)
                            .collect(Collectors.toList());
            try {
                subGroupMap = mtBomSubstituteGroups.stream()
                                .collect(Collectors.toMap(MtBomSubstituteGroup::getBomComponentId,
                                                MtBomSubstituteGroup::getBomSubstituteGroupId));
                substitutePolicyMap = mtBomSubstituteGroups.stream().collect(Collectors.toMap(
                                MtBomSubstituteGroup::getBomComponentId, MtBomSubstituteGroup::getSubstitutePolicy));
            } catch (Exception e) {
                throw new MtException("MT_BOM_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0016", "BOM", "【API:bomSubstituteBatchQuery】"));
            }

            // 获取替代项物料
            Date now = new Date();
            SecurityTokenHelper.close();
            List<MtBomSubstitute> mtBomSubstitutes = mtBomSubstituteRepository.selectByCondition(Condition
                            .builder(MtBomSubstitute.class)
                            .andWhere(Sqls.custom().andEqualTo(MtBomSubstitute.FIELD_TENANT_ID, tenantId)
                                            .andLessThanOrEqualTo(MtBomSubstitute.FIELD_DATE_FROM, now))
                            .andWhere(Sqls.custom().andEqualTo(MtBomSubstitute.FIELD_DATE_TO, null)
                                            .orGreaterThan(MtBomSubstitute.FIELD_DATE_TO, now))
                            .andWhere(Sqls.custom().andIn(MtBomSubstitute.FIELD_BOM_SUBSTITUTE_GROUP_ID, subGroupIds))
                            .build());
            Map<String, List<MtBomSubstitute>> mtBomSubstituteMap = mtBomSubstitutes.stream()
                            .collect(Collectors.groupingBy(MtBomSubstitute::getBomSubstituteGroupId));
            MtBomSubstituteGroupVO7 mtBomSubstituteGroupVO7;
            List<MtBomSubstituteGroupVO8> bomSubstituteList;
            MtBomSubstituteGroupVO8 mtBomSubstituteGroupVO8;
            for (Map.Entry<String, String> entry : subGroupMap.entrySet()) {
                if (CollectionUtils.isEmpty(mtBomSubstituteMap.get(entry.getValue()))) {
                    continue;
                }
                mtBomSubstituteGroupVO7 = new MtBomSubstituteGroupVO7();
                mtBomSubstituteGroupVO7.setBomComponentId(entry.getKey());
                mtBomSubstituteGroupVO7.setBomSubstituteGroupId(entry.getValue());
                mtBomSubstituteGroupVO7.setSubstitutePolicy(substitutePolicyMap.get(entry.getKey()));

                bomSubstituteList = new ArrayList<>(mtBomSubstituteMap.get(entry.getValue()).size());
                for (MtBomSubstitute mtBomSubstitute : mtBomSubstituteMap.get(entry.getValue())) {
                    mtBomSubstituteGroupVO8 = new MtBomSubstituteGroupVO8();
                    mtBomSubstituteGroupVO8.setBomSubstituteId(mtBomSubstitute.getBomSubstituteId());
                    mtBomSubstituteGroupVO8.setMaterialId(mtBomSubstitute.getMaterialId());
                    mtBomSubstituteGroupVO8.setSubstituteValue(mtBomSubstitute.getSubstituteValue());
                    mtBomSubstituteGroupVO8.setSubstituteUsage(mtBomSubstitute.getSubstituteUsage());
                    bomSubstituteList.add(mtBomSubstituteGroupVO8);
                }
                mtBomSubstituteGroupVO7.setBomSubstituteList(bomSubstituteList);
                result.add(mtBomSubstituteGroupVO7);
            }
        }
        return result;
    }

}
