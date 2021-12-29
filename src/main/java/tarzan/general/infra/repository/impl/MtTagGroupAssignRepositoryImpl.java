package tarzan.general.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.entity.MtTagGroupAssignHis;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtTagGroupAssignVO;
import tarzan.general.infra.mapper.MtTagGroupAssignMapper;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;

/**
 * 数据收集项分配收集组表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Component
public class MtTagGroupAssignRepositoryImpl extends BaseRepositoryImpl<MtTagGroupAssign>
        implements MtTagGroupAssignRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtTagGroupAssignMapper mtTagGroupAssignMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public MtTagGroupAssign tagGroupAssignGet(Long tenantId, String tagGroupAssignId) {
        // 1.校验参数的合规性
        if (StringUtils.isEmpty(tagGroupAssignId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagGroupAssignId", "【API:tagGroupAssignGet】"));
        }
        MtTagGroupAssign tagGroupAssign = new MtTagGroupAssign();
        tagGroupAssign.setTenantId(tenantId);
        tagGroupAssign.setTagGroupAssignId(tagGroupAssignId);
        return mtTagGroupAssignMapper.selectOne(tagGroupAssign);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long tagGroupAssignBatchUpdate(Long tenantId, List<MtTagGroupAssignVO> tagGroupAssignList,
                                          String fullUpdate) {
        if (CollectionUtils.isEmpty(tagGroupAssignList)) {
            return Long.valueOf("0");
        }

        // 参数校验
        if (tagGroupAssignList.stream().anyMatch(a -> StringUtils.isEmpty(a.getTagGroupCode()))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagGroupCode", "【API:tagGroupAssignBatchUpdate】"));
        }
        if (tagGroupAssignList.stream().anyMatch(a -> StringUtils.isEmpty(a.getTagCode()))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagCode", "【API:tagGroupAssignBatchUpdate】"));
        }

        List<MtTagGroupAssignVO> errorList = null;
        if ("Y".equals(fullUpdate)) {
            errorList = tagGroupAssignList.stream().filter(t -> StringUtils.isEmpty(t.getCollectionMethod()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(errorList)) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0001", "GENERAL", "collectionMethod", "【API:tagGroupAssignBatchUpdate】"));
            }

            errorList = tagGroupAssignList.stream().filter(t -> StringUtils.isEmpty(t.getValueAllowMissing()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(errorList)) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0001", "GENERAL", "valueAllowMissing", "【API:tagGroupAssignBatchUpdate】"));
            }
        }

        // 获取类型定义
        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("GENERAL");
        queryType.setTypeGroup("TAG_COLLECTION_METHOD");
        List<MtGenType> genTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
        Map<String, MtGenType> genTypeMap =
                genTypeList.stream().collect(Collectors.toMap(t -> t.getTypeCode(), t -> t));

        // 判断 collectionMethod 输入是否符合规格
        errorList = tagGroupAssignList.stream()
                .filter(t -> StringUtils.isNotEmpty(t.getCollectionMethod())
                        && genTypeMap.get(t.getCollectionMethod()) == null)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(errorList)) {
            throw new MtException("MT_GENERAL_0045", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0045", "GENERAL", "collectionMethod", "【API:tagGroupAssignBatchUpdate】"));
        }

        // 判断 valueAllowMissing 输入是否符合规格
        errorList = tagGroupAssignList.stream()
                .filter(t -> StringUtils.isNotEmpty(t.getValueAllowMissing())
                        && !Arrays.asList("Y", "N").contains(t.getValueAllowMissing()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(errorList)) {
            throw new MtException("MT_GENERAL_0045", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0045", "GENERAL", "valueAllowMissing", "【API:tagGroupAssignBatchUpdate】"));
        }

        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("TAG_GROUP_ASSIGN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 批量获取数据
        // uom
        List<String> uomCodes = tagGroupAssignList.stream().filter(t -> StringUtils.isNotEmpty(t.getUnit()))
                .map(MtTagGroupAssignVO::getUnit).collect(Collectors.toList());
        List<MtUom> mtUomList = mtUomRepository.uomPropertyBatchGetByCodes(tenantId, uomCodes);

        // tagGroup
        List<String> tagGroupCodes = tagGroupAssignList.stream().map(MtTagGroupAssignVO::getTagGroupCode)
                .collect(Collectors.toList());
        List<MtTagGroup> mtTagGroupList = mtTagGroupRepository.selectTagGroupByTagGroupCodes(tenantId, tagGroupCodes);
        if (CollectionUtils.isEmpty(mtTagGroupList) || mtTagGroupList.size() != tagGroupCodes.size()) {
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "tagGroupCode", "【API:tagGroupAssignBatchUpdate】"));
        }

        // tag
        List<String> tagCodes =
                tagGroupAssignList.stream().map(MtTagGroupAssignVO::getTagCode).collect(Collectors.toList());
        List<MtTag> mtTagList = mtTagRepository.selectByCodeList(tenantId, tagCodes);
        if (CollectionUtils.isEmpty(mtTagList) || mtTagList.size() != tagCodes.size()) {
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "tagCode", "【API:tagGroupAssignBatchUpdate】"));
        }

        // 转为map数据
        Map<String, MtUom> uomMap = new HashMap<>(tagGroupAssignList.size());
        Map<String, MtTagGroup> tagGroupMap = new HashMap<>(tagGroupAssignList.size());
        Map<String, MtTag> tagMap = new HashMap<>(tagGroupAssignList.size());
        if (CollectionUtils.isNotEmpty(mtUomList)) {
            uomMap = mtUomList.stream().collect(Collectors.toMap(t -> t.getUomCode(), t -> t));
        }
        if (CollectionUtils.isNotEmpty(mtTagGroupList)) {
            tagGroupMap = mtTagGroupList.stream().collect(Collectors.toMap(t -> t.getTagGroupCode(), t -> t));
        }
        if (CollectionUtils.isNotEmpty(mtTagList)) {
            tagMap = mtTagList.stream().collect(Collectors.toMap(t -> t.getTagCode(), t -> t));
        }

        // 主表ID/CID
        List<String> tagGroupAssignIds =
                this.customDbRepository.getNextKeys("mt_tag_group_assign_s", tagGroupAssignList.size());
        List<String> tagGroupAssignCids =
                this.customDbRepository.getNextKeys("mt_tag_group_assign_cid_s", tagGroupAssignList.size());

        // 历史表表ID/CID
        List<String> tagGroupAssignHisIds =
                this.customDbRepository.getNextKeys("mt_tag_group_assign_his_s", tagGroupAssignList.size());
        List<String> tagGroupAssignHisCids =
                this.customDbRepository.getNextKeys("mt_tag_group_assign_his_cid_s", tagGroupAssignList.size());

        // 公用变量
        List<String> sqlList = new ArrayList<>();
        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        int result = 0;

        for (MtTagGroupAssignVO tagGroupAssign : tagGroupAssignList) {

            // 获取单位ID
            MtUom mtUom = uomMap.get(tagGroupAssign.getUnit());
            String uomId = mtUom != null ? mtUom.getUomId() : "";

            // 准备数据结构
            MtTagGroupAssign newTagGroupAssign = constructTagGroupAssign(tenantId, tagGroupAssignIds.get(result),
                    tagGroupAssignCids.get(result), now, userId,
                    tagGroupMap.get(tagGroupAssign.getTagGroupCode()).getTagGroupId(),
                    tagMap.get(tagGroupAssign.getTagCode()).getTagId(), uomId, tagGroupAssign);

            // 查询唯一性
            MtTagGroupAssign uniqueRecord = new MtTagGroupAssign();
            uniqueRecord.setTagGroupId(newTagGroupAssign.getTagGroupId());
            uniqueRecord.setTagId(newTagGroupAssign.getTagId());
            uniqueRecord.setTenantId(tenantId);
            MtTagGroupAssign oldTagGroupAssign = mtTagGroupAssignMapper.selectOne(uniqueRecord);

            // 判断更新还是新增
            if (oldTagGroupAssign == null) {
                // 新增
                sqlList.addAll(customDbRepository.getInsertSql(newTagGroupAssign));
            } else {
                // 更新
                newTagGroupAssign.setTagGroupAssignId(oldTagGroupAssign.getTagGroupAssignId());
                if ("Y".equals(fullUpdate)) {
                    sqlList.addAll(customDbRepository.getFullUpdateSql(newTagGroupAssign));
                } else {
                    sqlList.addAll(customDbRepository.getUpdateSql(newTagGroupAssign));
                }
            }

            // 记录历史
            MtTagGroupAssignHis mtTagGroupAssignHis = new MtTagGroupAssignHis();
            mtTagGroupAssignHis.setTenantId(tenantId);
            mtTagGroupAssignHis.setTagGroupAssignHisId(tagGroupAssignHisIds.get(result));
            mtTagGroupAssignHis.setCid(Long.valueOf(tagGroupAssignHisCids.get(result)));
            mtTagGroupAssignHis.setTagGroupAssignId(newTagGroupAssign.getTagGroupAssignId());
            mtTagGroupAssignHis.setTagGroupId(newTagGroupAssign.getTagGroupId());
            mtTagGroupAssignHis.setSerialNumber(newTagGroupAssign.getSerialNumber());
            mtTagGroupAssignHis.setTagId(newTagGroupAssign.getTagId());
            mtTagGroupAssignHis.setCollectionMethod(newTagGroupAssign.getCollectionMethod());
            mtTagGroupAssignHis.setValueAllowMissing(newTagGroupAssign.getValueAllowMissing());
            mtTagGroupAssignHis.setTrueValue(newTagGroupAssign.getTrueValue());
            mtTagGroupAssignHis.setFalseValue(newTagGroupAssign.getFalseValue());
            mtTagGroupAssignHis.setMinimumValue(newTagGroupAssign.getMinimumValue());
            mtTagGroupAssignHis.setMaximalValue(newTagGroupAssign.getMaximalValue());
            mtTagGroupAssignHis.setUnit(newTagGroupAssign.getUnit());
            mtTagGroupAssignHis.setMandatoryNum(newTagGroupAssign.getMandatoryNum());
            mtTagGroupAssignHis.setOptionalNum(newTagGroupAssign.getOptionalNum());
            mtTagGroupAssignHis.setEventId(eventId);
            mtTagGroupAssignHis.setCreationDate(now);
            mtTagGroupAssignHis.setCreatedBy(userId);
            mtTagGroupAssignHis.setLastUpdateDate(now);
            mtTagGroupAssignHis.setLastUpdatedBy(userId);
            sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupAssignHis));
            result++;
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return Long.valueOf(result);
    }

    /**
     * 构建MtTagGroupAssignVO
     *
     * @author benjamin
     * @date 2019/9/19 11:06 AM
     * @param tenantId 租户Id
     * @param tagGroupId 数据收集组Id
     * @param tagId 数据收集项Id
     * @param uomId 单位Id
     * @param vo MtTagGroupAssignVO
     * @return MtTagGroupAssignVO
     */
    private MtTagGroupAssign constructTagGroupAssign(Long tenantId, String tagGroupAssignId, String cid, Date now,
                                                     Long userId, String tagGroupId, String tagId, String uomId, MtTagGroupAssignVO vo) {
        MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
        mtTagGroupAssign.setTagGroupAssignId(tagGroupAssignId);
        mtTagGroupAssign.setCid(Long.valueOf(cid));
        mtTagGroupAssign.setCreatedBy(userId);
        mtTagGroupAssign.setCreationDate(now);
        mtTagGroupAssign.setLastUpdatedBy(userId);
        mtTagGroupAssign.setLastUpdateDate(now);
        mtTagGroupAssign.setTenantId(tenantId);
        mtTagGroupAssign.setTagGroupId(tagGroupId);
        mtTagGroupAssign.setTagId(tagId);
        mtTagGroupAssign.setUnit(uomId);
        mtTagGroupAssign.setSerialNumber(vo.getSerialNumber());
        mtTagGroupAssign.setCollectionMethod(vo.getCollectionMethod());
        mtTagGroupAssign.setValueAllowMissing(vo.getValueAllowMissing());
        mtTagGroupAssign.setTrueValue(vo.getTrueValue());
        mtTagGroupAssign.setFalseValue(vo.getFalseValue());
        mtTagGroupAssign.setMinimumValue(vo.getMinimumValue());
        mtTagGroupAssign.setMaximalValue(vo.getMaximalValue());
        mtTagGroupAssign.setMandatoryNum(vo.getMandatoryNum());
        mtTagGroupAssign.setOptionalNum(vo.getOptionalNum());
        return mtTagGroupAssign;
    }

    @Override
    public List<String> propertyLimitTagGroupAssignQuery(Long tenantId, MtTagGroupAssign dto) {
        return mtTagGroupAssignMapper.selectForEmptyString(tenantId, dto);
    }
}
