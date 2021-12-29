package tarzan.actual.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtDataRecord;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.repository.MtDataRecordHisRepository;
import tarzan.actual.domain.repository.MtDataRecordRepository;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtEoStepWipRepository;
import tarzan.actual.domain.vo.MtEoStepWipVO6;
import tarzan.actual.domain.vo.MtEoStepWipVO7;
import tarzan.actual.infra.mapper.MtDataRecordMapper;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtTagGroupObjectRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.*;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * 数据收集实绩 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
@Component
public class MtDataRecordRepositoryImpl extends BaseRepositoryImpl<MtDataRecord> implements MtDataRecordRepository {

    private static final String TAG_GROUP_TYPE_EO = "EO";
    private static final String TAG_GROUP_TYPE_WKC = "WKC";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private MtTagGroupObjectRepository mtTagGroupObjectRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;

    @Autowired
    private MtDataRecordHisRepository mtDataRecordHisRepository;

    @Autowired
    private MtDataRecordMapper mtDataRecordMapper;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public MtDataRecord dataRecordGet(Long tenantId, String dataRecordId) {
        if (StringUtils.isEmpty(dataRecordId)) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "dataRecordId", "【API:dataRecordGet】"));
        }

        MtDataRecord record = new MtDataRecord();
        record.setTenantId(tenantId);
        record.setDataRecordId(dataRecordId);
        return mtDataRecordMapper.selectOne(record);
    }

    @Override
    public List<MtDataRecord> dataRecordBatchGet(Long tenantId, List<String> dataRecordIdList) {
        if (CollectionUtils.isEmpty(dataRecordIdList)) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "dataRecordIdList", "【API:dataRecordBatchGet】"));
        }

        return mtDataRecordMapper.selectByIdList(tenantId, dataRecordIdList);
    }

    @Override
    public List<MtDataRecord> propertyLimitDataRecordQuery(Long tenantId, MtDataRecordVO5 dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_DATA_RECORD_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0002", "DATA_RECORD", "【API:propertyLimitDataRecordQuery】"));
        }
        return mtDataRecordMapper.selectPropertyLimitDataRecord(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtDataRecordVO6 dataRecordAndHisCreate(Long tenantId, MtDataRecordVO createVO) {
        //V20200812 modify by penglin.sui for zhenyong.ban 取消数据收集组为空的校验
//        if (StringUtils.isEmpty(createVO.getTagGroupId())) {
//            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagGroupId", "【API:dataRecordAndHisCreate】"));
//        }
        if (StringUtils.isEmpty(createVO.getTagId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagId", "【API:dataRecordAndHisCreate】"));
        }
        if (StringUtils.isEmpty(createVO.getTagValue())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagValue", "【API:dataRecordAndHisCreate】"));
        }
        if (StringUtils.isEmpty(createVO.getEventId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "eventId", "【API:dataRecordAndHisCreate】"));
        }

        //V20200812 modify by penglin.sui for zhenyong.ban 取消数据收集组ID不为空，才去取值
        MtTagGroup mtTagGroup = null;
        if(!StringUtils.isEmpty(createVO.getTagGroupId())) {
            mtTagGroup = mtTagGroupRepository.tagGroupGet(tenantId, createVO.getTagGroupId());
            if (mtTagGroup == null) {
                return null;
            }
        }

        MtTag mtTag = mtTagRepository.tagGet(tenantId, createVO.getTagId());
        if (mtTag == null) {
            return null;
        }

        MtTagGroupAssign mtTagGroupAssign = getTagGroupAssign(tenantId, createVO.getTagGroupId(), createVO.getTagId());
        if (mtTagGroupAssign == null) {
            return null;
        }

        String materialId = null;
        if (StringUtils.isNotEmpty(createVO.getEoId())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, createVO.getEoId());
            if (mtEo == null) {
                throw new MtException("MT_DATA_RECORD_0003",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0003",
                                "DATA_RECORD", createVO.getEoId(), "【API:dataRecordAndHisCreate】"));
            }
            materialId = mtEo.getMaterialId();
        }

        String operationId;
        if (StringUtils.isNotEmpty(createVO.getEoStepActualId())) {
            MtEoStepActual mtEoStepActual =
                    mtEoStepActualRepository.eoStepPropertyGet(tenantId, createVO.getEoStepActualId());
            if (mtEoStepActual == null) {
                throw new MtException("MT_DATA_RECORD_0003",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0003",
                                "DATA_RECORD", createVO.getEoStepActualId(),
                                "【API:dataRecordAndHisCreate】"));
            }
            operationId = mtEoStepActual.getOperationId();
        } else {
            operationId = createVO.getOperationId();
        }

        MtDataRecord mtDataRecord = constructDataRecord(tenantId, createVO, mtTagGroup, mtTag, mtTagGroupAssign,
                materialId, operationId, DetailsHelper.getUserDetails().getUserId());
        mtDataRecord.setTenantId(tenantId);
        self().insertSelective(mtDataRecord);

        String dataRecordHisId =
                mtDataRecordHisRepository.saveDataHistory(tenantId, mtDataRecord, createVO.getEventId());

        // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
        mtDataRecord.setLatestHisId(dataRecordHisId);
        self().updateByPrimaryKeySelective(mtDataRecord);

        MtDataRecordVO6 result = new MtDataRecordVO6();
        result.setDataRecordId(mtDataRecord.getDataRecordId());
        result.setDataRecordHisId(dataRecordHisId);
        return result;
    }

    private MtTagGroupAssign getTagGroupAssign(Long tenantId, String tagGroupId, String tagId) {
        MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
        mtTagGroupAssign.setTenantId(tenantId);
        mtTagGroupAssign.setTagGroupId(tagGroupId);
        mtTagGroupAssign.setTagId(tagId);

        Criteria criteria = new Criteria(mtTagGroupAssign);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtTagGroupAssign.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtTagGroupAssign.FIELD_TAG_GROUP_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtTagGroupAssign.FIELD_TAG_ID, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        List<MtTagGroupAssign> mtTagGroupAssignList =
                mtTagGroupAssignRepository.selectOptional(mtTagGroupAssign, criteria);

        if (CollectionUtils.isEmpty(mtTagGroupAssignList)) {
            return null;
        } else {
            return mtTagGroupAssignList.get(0);
        }
    }

    /**
     * 构建数据收集实绩对象
     *
     * @author benjamin
     * @date 2019-07-02 15:03
     * @param createVO MtDataRecordVO
     * @param mtTagGroup MtTagGroup
     * @param mtTag MtTag
     * @param mtTagGroupAssign MtTagGroupAssign
     * @param materialId 物料Id
     * @param operationId 工艺Id
     * @param userId 用户Id
     * @return MtDataRecord
     */
    private MtDataRecord constructDataRecord(Long tenantId, MtDataRecordVO createVO, MtTagGroup mtTagGroup, MtTag mtTag,
                                             MtTagGroupAssign mtTagGroupAssign, String materialId, String operationId, Long userId) {
        MtDataRecord mtDataRecord = new MtDataRecord();

        mtDataRecord.setEoId(createVO.getEoId());
        mtDataRecord.setMaterialId(materialId);
        mtDataRecord.setOperationId(operationId);
        mtDataRecord.setEoStepActualId(createVO.getEoStepActualId());
        mtDataRecord.setWorkcellId(createVO.getWorkcellId());
        mtDataRecord.setComponentMaterialId(createVO.getComponentMaterialId());
        mtDataRecord.setAssembleConfirmId(createVO.getAssembleConfirmId());
        mtDataRecord.setNcCodeId(createVO.getNcCodeId());
        mtDataRecord.setTagGroupId(createVO.getTagGroupId());
        mtDataRecord.setTagId(createVO.getTagId());
        mtDataRecord.setTagValue(createVO.getTagValue());
        mtDataRecord.setTagCalculateResult(createVO.getTagCalculateResult());
        mtDataRecord.setRecordRemark(createVO.getRecordRemark());
        mtDataRecord.setRecordDate(createVO.getRecordDate() == null ? new Date(System.currentTimeMillis())
                : createVO.getRecordDate());
        mtDataRecord.setUserId(
                StringUtils.isEmpty(createVO.getUserId()) ? String.valueOf(userId) : createVO.getUserId());
        // 2019-11-13新增
        mtDataRecord.setMaterialLotId(createVO.getMaterialLotId());

        //V20200812 modify by penglin.sui for zhenyong.ban 取消数据收集组ID不为空，才取值
        if(Objects.nonNull(mtTagGroup)) {
            // tag group
            mtDataRecord.setTagGroupCode(mtTagGroup.getTagGroupCode());
            mtDataRecord.setTagGroupDescription(mtTagGroup.getTagGroupDescription());
            mtDataRecord.setTagGroupType(mtTagGroup.getTagGroupType());
            mtDataRecord.setSourceGroupId(mtTagGroup.getSourceGroupId());
            mtDataRecord.setBusinessType(mtTagGroup.getBusinessType());
            mtDataRecord.setTagGroupStatus(mtTagGroup.getStatus());
            mtDataRecord.setCollectionTimeControl(mtTagGroup.getCollectionTimeControl());
            mtDataRecord.setUserVerification(mtTagGroup.getUserVerification());
        }

        // tag
        mtDataRecord.setTagCode(mtTag.getTagCode());
        mtDataRecord.setTagDescription(mtTag.getTagDescription());
        mtDataRecord.setRemark(mtTag.getRemark());
        mtDataRecord.setTagEnableFlag(mtTag.getEnableFlag());
        mtDataRecord.setValueType(mtTag.getValueType());
        mtDataRecord.setApiId(mtTag.getApiId());

        // tag assign
        mtDataRecord.setTrueValue(mtTagGroupAssign.getTrueValue());
        mtDataRecord.setFalseValue(mtTagGroupAssign.getFalseValue());
        mtDataRecord.setCollectionMethod(mtTagGroupAssign.getCollectionMethod());
        mtDataRecord.setValueAllowMissing(mtTagGroupAssign.getValueAllowMissing());
        mtDataRecord.setMinimumValue(mtTagGroupAssign.getMinimumValue());
        mtDataRecord.setMaximalValue(mtTagGroupAssign.getMaximalValue());
        mtDataRecord.setUnit(mtTagGroupAssign.getUnit());
        mtDataRecord.setMandatoryNum(mtTagGroupAssign.getMandatoryNum());
        mtDataRecord.setOptionalNum(mtTagGroupAssign.getOptionalNum());
        mtDataRecord.setTenantId(tenantId);

        return mtDataRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtDataRecordVO6 dataRecordAndHisUpdate(Long tenantId, MtDataRecordVO2 updateVO, String fullUpdate) {
        if (StringUtils.isEmpty(updateVO.getDataRecordId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "dataRecordId", "【API:dataRecordAndHisUpdate】"));
        }
        if (StringUtils.isEmpty(updateVO.getEventId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "eventId", "【API:dataRecordAndHisUpdate】"));
        }

        MtDataRecord mtDataRecord = new MtDataRecord();
        mtDataRecord.setTenantId(tenantId);
        mtDataRecord.setDataRecordId(updateVO.getDataRecordId());
        mtDataRecord = mtDataRecordMapper.selectOne(mtDataRecord);
        if (mtDataRecord == null) {
            throw new MtException("MT_DATA_RECORD_0003",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0003",
                            "DATA_RECORD", updateVO.getDataRecordId(), "【API:dataRecordAndHisUpdate】"));
        }


        String dataRecordHisId =
                mtDataRecordHisRepository.saveDataHistory(tenantId, mtDataRecord, updateVO.getEventId());

        // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
        if ("Y".equalsIgnoreCase(fullUpdate)) {
            BeanUtils.copyProperties(updateVO, mtDataRecord);
            mtDataRecord.setTenantId(tenantId);
            mtDataRecord.setLatestHisId(dataRecordHisId);
            mtDataRecord = (MtDataRecord) ObjectFieldsHelper.setStringFieldsEmpty(mtDataRecord);
            self().updateByPrimaryKey(mtDataRecord);
        } else {
            BeanUtils.copyProperties(updateVO, mtDataRecord, getNullPropertyNames(updateVO));
            mtDataRecord.setTenantId(tenantId);
            mtDataRecord.setLatestHisId(dataRecordHisId);
            self().updateByPrimaryKeySelective(mtDataRecord);
        }

        MtDataRecordVO6 result = new MtDataRecordVO6();
        result.setDataRecordId(mtDataRecord.getDataRecordId());
        result.setDataRecordHisId(dataRecordHisId);

        return result;
    }

    /**
     * 获取源对象的所有值为null的成员变量名称
     *
     * @author benjamin
     * @date 2019-07-08 11:23
     * @param source 源对象
     * @return Array
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtDataRecordVO6 allTypeDataRecordCreate(Long tenantId, MtDataRecordVO createVO) {
        if (StringUtils.isEmpty(createVO.getTagGroupId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagGroupId", "【API:allTypeDataRecordCreate】"));
        }
        if (StringUtils.isEmpty(createVO.getTagId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagId", "【API:allTypeDataRecordCreate】"));
        }
        if (StringUtils.isEmpty(createVO.getTagValue())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagValue", "【API:allTypeDataRecordCreate】"));
        }
        if (StringUtils.isEmpty(createVO.getEventId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "eventId", "【API:allTypeDataRecordCreate】"));
        }

        MtTagGroup mtTagGroup = mtTagGroupRepository.tagGroupGet(tenantId, createVO.getTagGroupId());
        if (mtTagGroup == null) {
            throw new MtException("MT_DATA_RECORD_0003",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0003",
                            "DATA_RECORD", createVO.getTagGroupId(), "【API:allTypeDataRecordCreate】"));
        }

        switch (mtTagGroup.getTagGroupType()) {
            case TAG_GROUP_TYPE_EO:
                if (StringUtils.isEmpty(createVO.getEoId())) {
                    throw new MtException("MT_DATA_RECORD_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0001",
                                    "DATA_RECORD", "eoId", "【API:allTypeDataRecordCreate】"));
                }
                break;
            case TAG_GROUP_TYPE_WKC:
                if (StringUtils.isEmpty(createVO.getWorkcellId())) {
                    throw new MtException("MT_DATA_RECORD_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0001",
                                    "DATA_RECORD", "workcellId", "【API:allTypeDataRecordCreate】"));
                }
                createVO.setEoId(null);
                createVO.setOperationId(null);
                createVO.setEoStepActualId(null);
                createVO.setComponentMaterialId(null);
                createVO.setAssembleConfirmId(null);
                createVO.setNcCodeId(null);
                break;
            default:
                break;
        }

        return dataRecordAndHisCreate(tenantId, createVO);
    }

    @Override
    public MtDataRecordVO4 distributionGroupLimitTagGroupQuery(Long tenantId, MtDataRecordVO3 queryVO) {
        if (StringUtils.isEmpty(queryVO.getTagGroupId())) {
            throw new MtException("MT_DATA_RECORD_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0001",
                            "DATA_RECORD", "tagGroupId", "【API:distributionGroupLimitTagGroupQuery】"));
        }
        if (StringUtils.isEmpty(queryVO.getWorkcellId())) {
            throw new MtException("MT_DATA_RECORD_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0001",
                            "DATA_RECORD", "workcellId", "【API:distributionGroupLimitTagGroupQuery】"));
        }
        if (CollectionUtils.isEmpty(queryVO.getTagDataList())) {
            throw new MtException("MT_DATA_RECORD_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0001",
                            "DATA_RECORD", "tagData", "【API:distributionGroupLimitTagGroupQuery】"));
        }

        MtEo mtEo = null;
        MtEoStepWipVO6 eoRouterActualResultVO = null;
        MtEoStepActual eoStepActual = null;
        if (StringUtils.isNotEmpty(queryVO.getEoId())) {
            // use EO to get MATERIAL_ID(P1) and WORK_ORDER_ID(P6)
            mtEo = mtEoRepository.eoPropertyGet(tenantId, queryVO.getEoId());

            // use EO and Workcell to get EO_STEP_ACTUAL_ID(P2), ROUTER_STEP_ID(P3) and
            // ROUTER_ID(P4)
            MtEoStepWipVO7 eoRouterActualQueryVO = new MtEoStepWipVO7();
            eoRouterActualQueryVO.setWorkcellId(queryVO.getWorkcellId());
            List<MtEoStepWipVO6> mtEoStepWipVO6s =
                    mtEoStepWipRepository.wkcWipLimitEoQuery(tenantId, eoRouterActualQueryVO);
            mtEoStepWipVO6s = mtEoStepWipVO6s.stream().filter(t -> queryVO.getEoId().equals(t.getEoId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(mtEoStepWipVO6s) && mtEoStepWipVO6s.size() == 1) {
                eoRouterActualResultVO = mtEoStepWipVO6s.get(0);
            }
            // use EO Step Actual to get OPERATION_ID(P5)
            if (eoRouterActualResultVO != null) {
                eoStepActual = mtEoStepActualRepository.eoStepPropertyGet(tenantId,
                        eoRouterActualResultVO.getEoStepActualId());
            }
        }

        MtTagGroup mtTagGroup = new MtTagGroup();
        mtTagGroup.setSourceGroupId(queryVO.getTagGroupId());
        List<String> tagGroupIdList = mtTagGroupRepository.propertyLimitTagGroupQuery(tenantId, mtTagGroup);

        // use SOURCE_GROUP_ID to get TAG_GROUP_ID
        List<String> resultTagGroupIdList = getTagGroupIdListByGroupObject(tenantId, tagGroupIdList, mtEo,
                eoRouterActualResultVO, eoStepActual, queryVO);

        // construct result vo
        MtDataRecordVO4 resultVO = new MtDataRecordVO4();
        resultVO.setTagGroupIdList(resultTagGroupIdList);
        resultVO.setMaterialId(mtEo == null ? null : mtEo.getMaterialId());
        resultVO.setRouterStepId(eoRouterActualResultVO == null ? null : eoRouterActualResultVO.getRouterStepId());
        resultVO.setEoStepActualId(eoRouterActualResultVO == null ? null : eoRouterActualResultVO.getEoStepActualId());
        resultVO.setOperationId(eoStepActual == null ? null : eoStepActual.getOperationId());
        resultVO.setEoId(queryVO.getEoId());
        resultVO.setNcCodeId(null);
        resultVO.setBomComponentId(null);
        resultVO.setAssembleConfirmId(null);

        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dataRecordAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "keyId", "【API:dataRecordAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtDataRecord dataRecord = new MtDataRecord();
        dataRecord.setTenantId(tenantId);
        dataRecord.setDataRecordId(dto.getKeyId());
        dataRecord = mtDataRecordMapper.selectOne(dataRecord);
        if (dataRecord == null) {
            throw new MtException("MT_DATA_RECORD_0006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0006",
                            "DATA_RECORD", dto.getKeyId(), "mt_data_record",
                            "【API:dataRecordAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_data_record_attr", dto.getKeyId(), dto.getEventId(),
                dto.getAttrs());
    }

    @Override
    public void eoStepActualDataRecordCompleteValidate(Long tenantId, MtDataRecordVO7 vo) {
        // 1.验证参数
        if (StringUtils.isEmpty(vo.getEoStepActualId())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0006", "DATA_RECORD", "eoStepActualId ", "【API:MT_DATA_RECORD_0001】"));
        }

        // 2调用API 获取tagGroupId
        // objectLimitTagGroupQuery
        // 根据数据采集组ID （TAG_GROUP_ID）由表mt_tag_group_assign
        // 获取所有数据采集组对应的采集项ID（TAG _ID）及MANDATORY_NUM必须的数据条数
        MtTagGroupObjectVO2 objectVO2 = new MtTagGroupObjectVO2();
        objectVO2.setMaterialId(vo.getMaterialId());
        objectVO2.setOperationId(vo.getOperationId());
        objectVO2.setRouterId(vo.getRouterId());
        objectVO2.setRouterStepId(vo.getRouterStepId());
        objectVO2.setWorkcellId(vo.getWorkcellId());
        objectVO2.setWorkOrderId(vo.getWorkOrderId());
        objectVO2.setEoId(vo.getEoId());
        objectVO2.setNcCodeId(vo.getNcCodeId());
        objectVO2.setBomId(vo.getBomId());
        objectVO2.setBomComponentId(vo.getBomComponentId());
        objectVO2.setMaterialLotId(vo.getMaterialLotId());

        List<String> tagGroupIdList = mtTagGroupObjectRepository.objectLimitTagGroupQuery(tenantId, objectVO2);

        List<MtTagGroupAssign> mtTagGroupAssignList =
                mtTagGroupAssignRepository.selectByCondition(Condition.builder(MtTagGroupAssign.class)
                        .andWhere(Sqls.custom().andEqualTo(MtTagGroupAssign.FIELD_TENANT_ID, tenantId)
                                .andIn(MtTagGroupAssign.FIELD_TAG_GROUP_ID, tagGroupIdList))
                        .build());
        if (CollectionUtils.isEmpty(mtTagGroupAssignList)) {
            return;
        }
        List<String> tagIds = mtTagGroupAssignList.stream().map(MtTagGroupAssign::getTagId).distinct()
                .collect(Collectors.toList());


        // 由TAG_GROUP_ID至mt_tag_group获取TAG_GROUP_CODE
        List<MtTagGroup> mtTagGroups = mtTagGroupRepository.selectByCondition(Condition.builder(MtTagGroup.class)
                .andWhere(Sqls.custom().andEqualTo(MtTagGroupAssign.FIELD_TENANT_ID, tenantId)
                        .andIn(MtTagGroup.FIELD_TAG_GROUP_ID, tagGroupIdList))
                .build());

        // 由TAG _ID至mt_tag获取TAG _CODE
        List<MtTag> mtTagList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tagIds)) {
            mtTagList = mtTagRepository.selectByCondition(Condition.builder(MtTag.class).andWhere(Sqls.custom()
                    .andEqualTo(MtTagGroupAssign.FIELD_TENANT_ID, tenantId).andIn(MtTag.FIELD_TAG_ID, tagIds))
                    .build());
        }


        // 3.从mt_data_record表查数据获取所有的数据采集实绩
        MtDataRecord record = new MtDataRecord();
        record.setTenantId(tenantId);
        record.setEoStepActualId(vo.getEoStepActualId());
        List<MtDataRecord> dataRecords = mtDataRecordMapper.select(record);


        // 4 将第二步获取的所有数据采集项进入循环
        if (CollectionUtils.isEmpty(dataRecords)) {
            throw new MtException("MT_DATA_RECORD_0007",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0007",
                            "DATA_RECORD", "TAG_GROUP_CODE ", "TAG _CODE",
                            "【API:MT_DATA_RECORD_0001】"));
        }

        if (CollectionUtils.isNotEmpty(mtTagGroups)) {
            for (MtTagGroup mtTagGroup : mtTagGroups) {
                String tagGroupCode = mtTagGroup.getTagGroupCode();
                for (MtTag tag : mtTagList) {
                    String tagCode = tag.getTagCode();
                    Long mandatoryNum = tag.getMandatoryNum();
                    if (mandatoryNum == null) {
                        mandatoryNum = 1L;
                    }
                    if ("Y".equals(vo.getPassFlag())) {
                        long count = dataRecords.stream()
                                .filter(t -> tagGroupCode.equals(t.getTagGroupCode())
                                        && tagCode.equals(t.getTagCode())
                                        && "OK".equalsIgnoreCase(t.getTagCalculateResult()))
                                .count();

                        // 若x>=MANDATORY_NUM (MANDATORY_NUM为空则视为1)则进入下一循环
                        if (count <= mandatoryNum) {
                            throw new MtException("MT_DATA_RECORD_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "MT_DATA_RECORD_0007", "DATA_RECORD", "TAG_GROUP_CODE ",
                                            "TAG _CODE", "【API:MT_DATA_RECORD_0001】"));
                        }
                    } else if ("N".equals(vo.getPassFlag())) {
                        long count = dataRecords.stream().filter(
                                t -> tagGroupCode.equals(t.getTagGroupCode()) && tagCode.equals(t.getTagCode()))
                                .count();

                        // 若x>=MANDATORY_NUM (MANDATORY_NUM为空则视为1)则进入下一循环
                        if (count <= mandatoryNum) {
                            throw new MtException("MT_DATA_RECORD_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "MT_DATA_RECORD_0007", "DATA_RECORD", "TAG_GROUP_CODE ",
                                            "TAG _CODE", "【API:MT_DATA_RECORD_0001】"));
                        }
                    }
                }
            }
        }

    }

    /**
     * 通过TAG组获取TAG组对象
     *
     * 返回TAG组对象对应到TAG组Id集合
     *
     * @author benjamin
     * @date 2019-07-02 20:20
     * @param tenantId IRequest
     * @param tagGroupIdList TAG组集合(SOURCE_GROUP)
     * @param mtEo MtEo
     * @param mtEoRouterActual MtEoRouterActualVO4
     * @param mtEoStepActual MtEoStepActual
     * @param queryVO MtDataRecordVO3
     * @return List
     */
    private List<String> getTagGroupIdListByGroupObject(Long tenantId, List<String> tagGroupIdList, MtEo mtEo,
                                                        MtEoStepWipVO6 mtEoRouterActual, MtEoStepActual mtEoStepActual, MtDataRecordVO3 queryVO) {
        List<String> tagGroupObjectIdList = new ArrayList<>();

        MtTagGroupObject mtTagGroupObject;
        if (mtEo != null) {
            // material
            if (StringUtils.isNotEmpty(mtEo.getMaterialId())) {
                mtTagGroupObject = new MtTagGroupObject();
                mtTagGroupObject.setMaterialId(mtEo.getMaterialId());
                tagGroupObjectIdList.addAll(mtTagGroupObjectRepository.propertyLimitTagGroupObjectBatchQuery(tenantId,
                        tagGroupIdList, mtTagGroupObject));
            }
            // work order
            if (StringUtils.isNotEmpty(mtEo.getWorkOrderId())) {
                mtTagGroupObject = new MtTagGroupObject();
                mtTagGroupObject.setWorkOrderId(mtEo.getWorkOrderId());
                tagGroupObjectIdList.addAll(mtTagGroupObjectRepository.propertyLimitTagGroupObjectBatchQuery(tenantId,
                        tagGroupIdList, mtTagGroupObject));
            }
        }

        if (mtEoRouterActual != null) {
            // router step
            if (StringUtils.isNotEmpty(mtEoRouterActual.getRouterStepId())) {
                mtTagGroupObject = new MtTagGroupObject();
                mtTagGroupObject.setRouterStepId(mtEoRouterActual.getRouterStepId());
                tagGroupObjectIdList.addAll(mtTagGroupObjectRepository.propertyLimitTagGroupObjectBatchQuery(tenantId,
                        tagGroupIdList, mtTagGroupObject));
            }

            // router
            if (StringUtils.isNotEmpty(mtEoRouterActual.getRouterId())) {
                mtTagGroupObject = new MtTagGroupObject();
                mtTagGroupObject.setRouterId(mtEoRouterActual.getRouterId());
                tagGroupObjectIdList.addAll(mtTagGroupObjectRepository.propertyLimitTagGroupObjectBatchQuery(tenantId,
                        tagGroupIdList, mtTagGroupObject));
            }
        }

        // operation
        if (mtEoStepActual != null && StringUtils.isNotEmpty(mtEoStepActual.getOperationId())) {
            mtTagGroupObject = new MtTagGroupObject();
            mtTagGroupObject.setOperationId(mtEoStepActual.getOperationId());
            tagGroupObjectIdList.addAll(mtTagGroupObjectRepository.propertyLimitTagGroupObjectBatchQuery(tenantId,
                    tagGroupIdList, mtTagGroupObject));
        }

        // eo
        if (StringUtils.isNotEmpty(queryVO.getEoId())) {
            mtTagGroupObject = new MtTagGroupObject();
            mtTagGroupObject.setWorkOrderId(queryVO.getEoId());
            tagGroupObjectIdList.addAll(mtTagGroupObjectRepository.propertyLimitTagGroupObjectBatchQuery(tenantId,
                    tagGroupIdList, mtTagGroupObject));
        }

        List<MtTagGroupObject> tagGroupObjectList = mtTagGroupObjectRepository.tagGroupObjectBatchGet(tenantId,
                tagGroupObjectIdList.stream().distinct().collect(Collectors.toList()));

        if (CollectionUtils.isNotEmpty(tagGroupObjectList)) {
            return tagGroupObjectList.stream().map(MtTagGroupObject::getTagGroupId).collect(Collectors.toList());
        }

        return null;
    }
}
