package tarzan.general.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.general.api.dto.MtTagGroupDTO8;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupHis;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtTagGroupVO1;
import tarzan.general.domain.vo.MtTagGroupVO2;
import tarzan.general.infra.mapper.MtTagGroupMapper;

/**
 * 数据收集组表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Component
public class MtTagGroupRepositoryImpl extends BaseRepositoryImpl<MtTagGroup> implements MtTagGroupRepository {

    private static final String TABLE_NAME = "mt_tag_group";
    private static final String ATTR_TABLE_NAME = "mt_tag_group_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtTagGroupMapper mtTagGroupMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public MtTagGroup tagGroupGet(Long tenantId, String tagGroupId) {
        if (StringUtils.isEmpty(tagGroupId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tagGroupId", "【API:tagGroupGet】"));
        }

        MtTagGroup tagGroup = new MtTagGroup();
        tagGroup.setTenantId(tenantId);
        tagGroup.setTagGroupId(tagGroupId);
        return mtTagGroupMapper.selectOne(tagGroup);
    }

    @Override
    public List<String> propertyLimitTagGroupQuery(Long tenantId, MtTagGroup mtTagGroup) {
        if (ObjectFieldsHelper.isAllFieldNull(mtTagGroup)) {
            throw new MtException("MT_GENERAL_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0014", "GENERAL", "【API:propertyLimitTagGroupQuery】"));
        }
        MtTagGroup tagGroup = new MtTagGroup();
        tagGroup.setTenantId(tenantId);
        if (StringUtils.isNotEmpty(mtTagGroup.getTagGroupCode())) {
            tagGroup.setTagGroupCode(mtTagGroup.getTagGroupCode());
        }
        if (StringUtils.isNotEmpty(mtTagGroup.getTagGroupDescription())) {
            tagGroup.setTagGroupDescription(mtTagGroup.getTagGroupDescription());
        }
        if (StringUtils.isNotEmpty(mtTagGroup.getTagGroupType())) {
            tagGroup.setTagGroupType(mtTagGroup.getTagGroupType());
        }
        if (StringUtils.isNotEmpty(mtTagGroup.getSourceGroupId())) {
            tagGroup.setSourceGroupId(mtTagGroup.getSourceGroupId());
        }
        if (StringUtils.isNotEmpty(mtTagGroup.getBusinessType())) {
            tagGroup.setBusinessType(mtTagGroup.getBusinessType());
        }
        if (StringUtils.isNotEmpty(mtTagGroup.getStatus())) {
            tagGroup.setStatus(mtTagGroup.getStatus());
        }
        if (StringUtils.isNotEmpty(mtTagGroup.getCollectionTimeControl())) {
            tagGroup.setCollectionTimeControl(mtTagGroup.getCollectionTimeControl());
        }
        if (StringUtils.isNotEmpty(mtTagGroup.getUserVerification())) {
            tagGroup.setUserVerification(mtTagGroup.getUserVerification());
        }

        return mtTagGroupMapper.select(tagGroup).stream().map(MtTagGroup::getTagGroupId).collect(Collectors.toList());
    }

    @Override
    public List<MtTagGroup> tagGroupBatchGet(Long tenantId, List<String> tagGroupIds) {
        if (CollectionUtils.isEmpty(tagGroupIds)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "tagGroupId", "【API:tagGroupBatchGet】"));
        }
        return mtTagGroupMapper.selectTagGroupByCondition(tenantId, tagGroupIds);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long tagGroupBasicBatchUpdate(Long tenantId, List<MtTagGroupDTO8> dto, String fullUpdate) {
        Long result = 0L;
        // 1.判断数据必填项
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(dto)) {
            return result;
        }
        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getTagGroupCode()))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagGroupCode", "【API:tagGroupBatchUpdate】"));
        }

        // 获取用户和时间
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        // 判断是更新还是新增
        List<String> sqlList = new ArrayList<>();
        MtTagGroup mtTagGroup;
        MtGenStatusVO2 mtGenStatusVO2;
        MtGenTypeVO2 mtGenTypeVO2;
        // 获取状态组<TAG_GROUP_STATUS>中的状态值
        mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("GENERAL");
        mtGenStatusVO2.setStatusGroup("TAG_GROUP_STATUS");
        List<MtGenStatus> status = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
        // 获取类型组<TAG_GROUP_TYPE>中的类型值
        mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("GENERAL");
        mtGenTypeVO2.setTypeGroup("TAG_GROUP_TYPE");
        List<MtGenType> tagGroupTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        // 获取类型组<TAG_GROUP_BUSINESS_TYPE>中的类型值
        mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("GENERAL");
        mtGenTypeVO2.setTypeGroup("TAG_GROUP_BUSINESS_TYPE");
        List<MtGenType> businessTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
        // 获取类型组<TAG_GROUP_COLLECTION_TIME>中的类型值
        mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("GENERAL");
        mtGenTypeVO2.setTypeGroup("TAG_GROUP_COLLECTION_TIME");
        List<MtGenType> collectionTimeControls = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("TAG_GROUP_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // tagGroup主键和Cid
        List<String> tagGroupIds = this.customDbRepository.getNextKeys("mt_tag_group_s", dto.size());
        List<String> tagGroupCids = this.customDbRepository.getNextKeys("mt_tag_group_cid_s", dto.size());

        // tagGroupHis主键和Cid
        List<String> tagGroupHisIds = this.customDbRepository.getNextKeys("mt_tag_group_his_s", dto.size());
        List<String> tagGroupHisCids = this.customDbRepository.getNextKeys("mt_tag_group_his_cid_s", dto.size());


        List<String> groupIds = dto.stream().map(MtTagGroupDTO8::getSourceGroupId).filter(StringUtils::isNotEmpty)
                .distinct().collect(Collectors.toList());
        List<String> tagGroupCodes = dto.stream().map(MtTagGroupDTO8::getTagGroupCode).filter(StringUtils::isNotEmpty)
                .distinct().collect(Collectors.toList());

        // 根据tagGroupId批量获取数据收集组
        Map<String, MtTagGroup> tagGroupIdMap = new HashMap<>(0);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupIds)) {
            List<MtTagGroup> mtTagGroups = mtTagGroupMapper.selectTagGroupByCondition(tenantId, groupIds);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtTagGroups)) {
                tagGroupIdMap = mtTagGroups.stream().collect(Collectors.toMap(MtTagGroup::getTagGroupId, t -> t));
            }
        }

        // 根据tagGroupCode批量获取数据收集组
        Map<String, MtTagGroup> tagGroupCodeMap = new HashMap<>(0);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(tagGroupCodes)) {
            List<MtTagGroup> mtTagGroups = mtTagGroupMapper.selectTagGroupByTagGroupCodes(tenantId, tagGroupCodes);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtTagGroups)) {
                tagGroupCodeMap = mtTagGroups.stream().collect(Collectors.toMap(MtTagGroup::getTagGroupCode, t -> t));
            }
        }

        int i = 0;
        // 历史扩展列表
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        for (MtTagGroupDTO8 mtTagGroupDTO : dto) {
            // 判断输入值是否为允许值
            // status只允许为状态组<TAG_GROUP_STATUS>中的状态值
            if (StringUtils.isNotEmpty(mtTagGroupDTO.getStatus()) && (org.apache.commons.collections4.CollectionUtils.isEmpty(status) || status.stream()
                    .noneMatch(c -> c.getStatusCode().equalsIgnoreCase(mtTagGroupDTO.getStatus())))) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "status", "【API:tagGroupBatchUpdate】"));
            }

            // tagGroupType只允许为类型组<TAG_GROUP_TYPE>中的类型值
            if (StringUtils.isNotEmpty(mtTagGroupDTO.getTagGroupType())
                    && (org.apache.commons.collections4.CollectionUtils.isEmpty(tagGroupTypes) || tagGroupTypes.stream().noneMatch(
                    c -> c.getTypeCode().equalsIgnoreCase(mtTagGroupDTO.getTagGroupType())))) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "tagGroupType", "【API:tagGroupBatchUpdate】"));
            }

            // businessType只允许为类型组<TAG_GROUP_BUSINESS_TYPE>中的类型值
            if (StringUtils.isNotEmpty(mtTagGroupDTO.getBusinessType())
                    && (org.apache.commons.collections4.CollectionUtils.isEmpty(businessTypes) || businessTypes.stream().noneMatch(
                    c -> c.getTypeCode().equalsIgnoreCase(mtTagGroupDTO.getBusinessType())))) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "businessType", "【API:tagGroupBatchUpdate】"));
            }


            // collectionTimeControl只允许为类型组<TAG_GROUP_COLLECTION_TIME>中的类型值
            if (StringUtils.isNotEmpty(mtTagGroupDTO.getCollectionTimeControl()) && (org.apache.commons.collections4.CollectionUtils
                    .isEmpty(collectionTimeControls)
                    || collectionTimeControls.stream().noneMatch(c -> c.getTypeCode()
                    .equalsIgnoreCase(mtTagGroupDTO.getCollectionTimeControl())))) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "collectionTimeControl", "【API:tagGroupBatchUpdate】"));
            }
            // userVerification只允许Y或N
            if (StringUtils.isNotEmpty(mtTagGroupDTO.getUserVerification())
                    && !Arrays.asList("Y", "N").contains(mtTagGroupDTO.getUserVerification())) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "userVerification", "【API:tagGroupBatchUpdate】"));
            }
            // 查询是否来源数据收集组存在
            if (StringUtils.isNotEmpty(mtTagGroupDTO.getSourceGroupId())
                    && null == tagGroupIdMap.get(mtTagGroupDTO.getSourceGroupId())) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "sourceGroupId", "【API:tagGroupBatchUpdate】"));
            }
            // 查询是否存在记录
            mtTagGroup = tagGroupCodeMap.get(mtTagGroupDTO.getTagGroupCode());
            if (mtTagGroup == null) {
                // 新增逻辑
                if (StringUtils.isEmpty(mtTagGroupDTO.getTagGroupDescription())) {
                    throw new MtException("MT_GENERAL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                    "GENERAL", "tagGroupDescription", "【API:tagGroupBatchUpdate】"));
                }
                if (StringUtils.isEmpty(mtTagGroupDTO.getStatus())) {
                    throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_GENERAL_0001", "GENERAL", "status", "【API:tagGroupBatchUpdate】"));
                }
                if (StringUtils.isEmpty(mtTagGroupDTO.getTagGroupType())) {
                    throw new MtException("MT_GENERAL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                    "GENERAL", "tagGroupType", "【API:tagGroupBatchUpdate】"));
                }
                if (StringUtils.isEmpty(mtTagGroupDTO.getBusinessType())) {
                    throw new MtException("MT_GENERAL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                    "GENERAL", "businessType", "【API:tagGroupBatchUpdate】"));
                }
                if (StringUtils.isEmpty(mtTagGroupDTO.getCollectionTimeControl())) {
                    throw new MtException("MT_GENERAL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                    "GENERAL", "collectionTimeControl", "【API:tagGroupBatchUpdate】"));
                }
                if (StringUtils.isEmpty(mtTagGroupDTO.getUserVerification())) {
                    throw new MtException("MT_GENERAL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                    "GENERAL", "userVerification", "【API:tagGroupBatchUpdate】"));
                }
                MtTagGroupHis mtTagGroupHis = new MtTagGroupHis();
                mtTagGroup = new MtTagGroup();
                mtTagGroup.setTagGroupCode(mtTagGroupDTO.getTagGroupCode());
                mtTagGroup.setTagGroupDescription(mtTagGroupDTO.getTagGroupDescription());
                mtTagGroup.setStatus(mtTagGroupDTO.getStatus());
                mtTagGroup.setTagGroupType(mtTagGroupDTO.getTagGroupType());
                mtTagGroup.setSourceGroupId(mtTagGroupDTO.getSourceGroupId());
                mtTagGroup.setBusinessType(mtTagGroupDTO.getBusinessType());
                mtTagGroup.setCollectionTimeControl(mtTagGroupDTO.getCollectionTimeControl());
                mtTagGroup.setUserVerification(mtTagGroupDTO.getUserVerification());
                mtTagGroup.setLatestHisId(tagGroupHisIds.get(i));
                mtTagGroup.setTenantId(tenantId);
                mtTagGroup.setCreatedBy(userId);
                mtTagGroup.setLastUpdatedBy(userId);
                mtTagGroup.setLastUpdateDate(now);
                mtTagGroup.setCreationDate(now);
                mtTagGroup.setTagGroupId(tagGroupIds.get(i));
                mtTagGroup.setCid(Long.valueOf(tagGroupCids.get(i)));

                mtTagGroupHis.setTagGroupId(mtTagGroup.getTagGroupId());
                mtTagGroupHis.setTagGroupCode(mtTagGroup.getTagGroupCode());
                mtTagGroupHis.setTagGroupDescription(mtTagGroup.getTagGroupDescription());
                mtTagGroupHis.setTagGroupType(mtTagGroup.getTagGroupType());
                mtTagGroupHis.setSourceGroupId(mtTagGroup.getSourceGroupId());
                mtTagGroupHis.setBusinessType(mtTagGroup.getBusinessType());
                mtTagGroupHis.setStatus(mtTagGroup.getStatus());
                mtTagGroupHis.setCollectionTimeControl(mtTagGroup.getCollectionTimeControl());
                mtTagGroupHis.setUserVerification(mtTagGroup.getUserVerification());
                mtTagGroupHis.setEventId(eventId);
                mtTagGroupHis.setTenantId(tenantId);
                mtTagGroupHis.setCreatedBy(userId);
                mtTagGroupHis.setLastUpdatedBy(userId);
                mtTagGroupHis.setLastUpdateDate(now);
                mtTagGroupHis.setCreationDate(now);
                mtTagGroupHis.setCid(Long.valueOf(tagGroupHisCids.get(i)));
                mtTagGroupHis.setTagGroupHisId(tagGroupHisIds.get(i));

                sqlList.addAll(customDbRepository.getInsertSql(mtTagGroup));
                sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupHis));

                // 更新历史扩展表
                MtCommonExtendVO6 attrProperty = new MtCommonExtendVO6();
                attrProperty.setKeyId(mtTagGroupHis.getTagGroupId());
                attrPropertyList.add(attrProperty);

            } else {
                // 更新逻辑
                mtTagGroup.setTenantId(tenantId);
                mtTagGroup.setLastUpdatedBy(userId);
                mtTagGroup.setLastUpdateDate(now);

                if ("Y".equals(fullUpdate)) {
                    if (StringUtils.isEmpty(mtTagGroupDTO.getTagGroupDescription())) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "tagGroupDescription", "【API:tagGroupBatchUpdate】"));
                    } else {
                        mtTagGroup.setTagGroupDescription(mtTagGroupDTO.getTagGroupDescription());
                    }
                    if (StringUtils.isEmpty(mtTagGroupDTO.getStatus())) {
                        throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_GENERAL_0001", "GENERAL", "status", "【API:tagGroupBatchUpdate】"));
                    } else {
                        mtTagGroup.setStatus(mtTagGroupDTO.getStatus());
                    }
                    if (StringUtils.isEmpty(mtTagGroupDTO.getTagGroupType())) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "tagGroupType", "【API:tagGroupBatchUpdate】"));
                    } else {
                        mtTagGroup.setTagGroupType(mtTagGroupDTO.getTagGroupType());
                    }
                    if (StringUtils.isEmpty(mtTagGroupDTO.getBusinessType())) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "businessType", "【API:tagGroupBatchUpdate】"));
                    } else {
                        mtTagGroup.setBusinessType(mtTagGroupDTO.getBusinessType());
                    }
                    if (StringUtils.isEmpty(mtTagGroupDTO.getCollectionTimeControl())) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "collectionTimeControl",
                                        "【API:tagGroupBatchUpdate】"));
                    } else {
                        mtTagGroup.setCollectionTimeControl(mtTagGroupDTO.getCollectionTimeControl());
                    }
                    if (StringUtils.isEmpty(mtTagGroupDTO.getUserVerification())) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "userVerification", "【API:tagGroupBatchUpdate】"));
                    } else {
                        mtTagGroup.setUserVerification(mtTagGroupDTO.getUserVerification());
                    }
                    if (StringUtils.isNotEmpty(mtTagGroupDTO.getSourceGroupId())) {
                        mtTagGroup.setSourceGroupId(mtTagGroupDTO.getSourceGroupId());
                    }

                    MtTagGroupHis mtTagGroupHis = new MtTagGroupHis();
                    mtTagGroupHis.setTagGroupId(mtTagGroup.getTagGroupId());
                    mtTagGroupHis.setTagGroupCode(mtTagGroup.getTagGroupCode());
                    mtTagGroupHis.setTagGroupDescription(mtTagGroup.getTagGroupDescription());
                    mtTagGroupHis.setTagGroupType(mtTagGroup.getTagGroupType());
                    mtTagGroupHis.setSourceGroupId(mtTagGroup.getSourceGroupId());
                    mtTagGroupHis.setBusinessType(mtTagGroup.getBusinessType());
                    mtTagGroupHis.setStatus(mtTagGroup.getStatus());
                    mtTagGroupHis.setCollectionTimeControl(mtTagGroup.getCollectionTimeControl());
                    mtTagGroupHis.setUserVerification(mtTagGroup.getUserVerification());
                    mtTagGroupHis.setEventId(eventId);
                    mtTagGroupHis.setTenantId(tenantId);
                    mtTagGroupHis.setCreatedBy(userId);
                    mtTagGroupHis.setLastUpdatedBy(userId);
                    mtTagGroupHis.setLastUpdateDate(now);
                    mtTagGroupHis.setCreationDate(now);
                    mtTagGroupHis.setCid(Long.valueOf(tagGroupHisCids.get(i)));
                    mtTagGroupHis.setTagGroupHisId(tagGroupHisIds.get(i));

                    mtTagGroup.setLatestHisId(tagGroupHisIds.get(i));
                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtTagGroup));
                    sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupHis));

                    // 更新历史扩展表
                    MtCommonExtendVO6 attrProperty = new MtCommonExtendVO6();
                    attrProperty.setKeyId(mtTagGroupHis.getTagGroupId());
                    attrPropertyList.add(attrProperty);

                } else {
                    if ("".equals(mtTagGroupDTO.getTagGroupDescription())) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "tagGroupDescription", "【API:tagGroupBatchUpdate】"));
                    }

                    if (null != mtTagGroupDTO.getTagGroupDescription()) {
                        mtTagGroup.setTagGroupDescription(mtTagGroupDTO.getTagGroupDescription());
                    }
                    if (null != mtTagGroupDTO.getStatus()) {
                        mtTagGroup.setStatus(mtTagGroupDTO.getStatus());
                    }
                    if (null != mtTagGroupDTO.getTagGroupType()) {
                        mtTagGroup.setTagGroupType(mtTagGroupDTO.getTagGroupType());
                    }
                    if (null != mtTagGroupDTO.getTagGroupCode()) {
                        mtTagGroup.setTagGroupCode(mtTagGroupDTO.getTagGroupCode());
                    }
                    if (null != mtTagGroupDTO.getBusinessType()) {
                        mtTagGroup.setBusinessType(mtTagGroupDTO.getBusinessType());
                    }
                    if (null != mtTagGroupDTO.getCollectionTimeControl()) {
                        mtTagGroup.setCollectionTimeControl(mtTagGroupDTO.getCollectionTimeControl());
                    }
                    if (null != mtTagGroupDTO.getUserVerification()) {
                        mtTagGroup.setUserVerification(mtTagGroupDTO.getUserVerification());
                    }
                    if (null != mtTagGroupDTO.getSourceGroupId()) {
                        mtTagGroup.setSourceGroupId(mtTagGroupDTO.getSourceGroupId());
                    }

                    MtTagGroupHis mtTagGroupHis = new MtTagGroupHis();
                    mtTagGroupHis.setTagGroupId(mtTagGroup.getTagGroupId());
                    mtTagGroupHis.setTagGroupCode(mtTagGroup.getTagGroupCode());
                    mtTagGroupHis.setTagGroupDescription(mtTagGroup.getTagGroupDescription());
                    mtTagGroupHis.setTagGroupType(mtTagGroup.getTagGroupType());
                    mtTagGroupHis.setSourceGroupId(mtTagGroup.getSourceGroupId());
                    mtTagGroupHis.setBusinessType(mtTagGroup.getBusinessType());
                    mtTagGroupHis.setStatus(mtTagGroup.getStatus());
                    mtTagGroupHis.setCollectionTimeControl(mtTagGroup.getCollectionTimeControl());
                    mtTagGroupHis.setUserVerification(mtTagGroup.getUserVerification());
                    mtTagGroupHis.setEventId(eventId);
                    mtTagGroupHis.setTenantId(tenantId);
                    mtTagGroupHis.setCreatedBy(userId);
                    mtTagGroupHis.setLastUpdatedBy(userId);
                    mtTagGroupHis.setLastUpdateDate(now);
                    mtTagGroupHis.setCreationDate(now);
                    mtTagGroupHis.setCid(Long.valueOf(tagGroupHisCids.get(i)));
                    mtTagGroupHis.setTagGroupHisId(tagGroupHisIds.get(i));

                    mtTagGroup.setLatestHisId(tagGroupHisIds.get(i));
                    sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupHis));
                    sqlList.addAll(customDbRepository.getUpdateSql(mtTagGroup));

                    // 更新历史扩展表
                    MtCommonExtendVO6 attrProperty = new MtCommonExtendVO6();
                    attrProperty.setKeyId(mtTagGroupHis.getTagGroupId());
                    attrPropertyList.add(attrProperty);
                }
            }
            i++;
            result++;
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        // 批量更新扩展历史
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_tag_group_attr", eventId,
                    attrPropertyList);
        }
        return result;
    }

    @Override
    public List<MtTagGroupVO2> propertyLimitTagGroupPropertyQuery(Long tenantId, MtTagGroupVO1 dto) {
        return mtTagGroupMapper.selectCondition(tenantId, dto);
    }

    @Override
    public List<MtTagGroupVO2> selectCondition(Long tenantId, MtTagGroup mtTagGroup) {
        MtTagGroupVO1 mtTagGroupVO1 = new MtTagGroupVO1();
        BeanUtils.copyProperties(mtTagGroup, mtTagGroupVO1);
        return mtTagGroupMapper.selectCondition(tenantId, mtTagGroupVO1);
    }

    @Override
    public List<MtTagGroup> selectTagGroupByTagGroupCodes(Long tenantId, List<String> tagGroupCodes) {
        return mtTagGroupMapper.selectTagGroupByTagGroupCodes(tenantId, tagGroupCodes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tagGroupAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "keyId", "【API:tagGroupAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtTagGroup entity = new MtTagGroup();
        entity.setTenantId(tenantId);
        entity.setTagGroupId(dto.getKeyId());
        entity = this.mtTagGroupMapper.selectOne(entity);
        if (entity == null) {
            throw new MtException("MT_GENERAL_0059",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0059", "GENERAL",
                            dto.getKeyId(), TABLE_NAME, "【API:tagGroupAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                dto.getAttrs());
    }
}
