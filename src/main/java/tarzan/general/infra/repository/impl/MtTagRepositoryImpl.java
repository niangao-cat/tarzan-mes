package tarzan.general.infra.repository.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.convertor.ApplicationContextHelper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.actual.domain.repository.MtDataRecordRepository;
import tarzan.general.domain.entity.*;
import tarzan.general.domain.repository.*;
import tarzan.general.domain.vo.*;
import tarzan.general.infra.mapper.MtTagApiMapper;
import tarzan.general.infra.mapper.MtTagMapper;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.vo.MtBomComponentVO8;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;

/**
 * 数据收集项表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Component
public class MtTagRepositoryImpl extends BaseRepositoryImpl<MtTag> implements MtTagRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MtTagRepositoryImpl.class);
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtTagMapper mtTagMapper;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private MtTagGroupObjectRepository mtTagGroupObjectRepository;

    @Autowired
    private MtDataRecordRepository mtDataRecordRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtTagApiMapper mtTagApiMapper;

    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Override
    public MtTag tagGet(Long tenantId, String tagId) {
        // 1.校验参数的合规性
        if (StringUtils.isEmpty(tagId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagId", "【API:tagGet】"));
        }
        MtTag tag = new MtTag();
        tag.setTenantId(tenantId);
        tag.setTagId(tagId);
        return mtTagMapper.selectOne(tag);
    }

    @Override
    public List<String> tagCodeAndGroupCodeLimitTagGroupAssignGet(Long tenantId, MtTagVO dto) {
        // 1.校验参数的合规性
        if (StringUtils.isEmpty(dto.getTagGroupCode())) {
            throw new MtException("MT_GENERAL_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001", "GENERAL",
                            "tagGroupCode", "【API:tagCodeAndGroupCodeLimitTagGroupAssignGet】"));
        }
        return mtTagMapper.getTagGroupAssignId(tenantId, dto);
    }

    @Override
    public List<String> propertyLimitTagQuery(Long tenantId, MtTag dto) {
        return mtTagMapper.queryTagIds(tenantId, dto);
    }

    @Override
    public MtMqttMessageVO3 edginkMessageAnalysis(Long tenantId, MtMqttMessageVO1 dto) {
        // 1.校验参数的合规性
        if (dto == null || StringUtils.isEmpty(dto.getTagGroupCode())) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagGroupCode", "【API:edginkMessageAnalysis】"));
        }
        if (CollectionUtils.isEmpty(dto.getTags())
                || dto.getTags().stream().anyMatch(t -> StringUtils.isEmpty(t.getTagName()))) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "tagName", "【API:edginkMessageAnalysis】"));
        }

        // 2.转换数据收集组(P1)
        MtTagGroup mtTagGroup = new MtTagGroup();
        mtTagGroup.setTagGroupCode(dto.getTagGroupCode());
        List<String> list = mtTagGroupRepository.propertyLimitTagGroupQuery(tenantId, mtTagGroup);
        if (CollectionUtils.isEmpty(list)) {
            throw new MtException("MT_DATA_RECORD_0003",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0003",
                            "DATA_RECORD", "tagGroupCode:" + dto.getTagGroupCode(),
                            "【API:edginkMessageAnalysis】"));

        }
        if (list.size() > 1) {
            throw new MtException("MT_DATA_RECORD_0004",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0004",
                            "DATA_RECORD", "tagGroupCode:" + dto.getTagGroupCode(),
                            "【API:edginkMessageAnalysis】"));

        }

        // 获取P4
        MtTagGroup mtTagGroup1 = mtTagGroupRepository.tagGroupGet(tenantId, list.get(0));

        // 3.转换WKC(P3)
        String wkcId = null;
        List<String> temp = new ArrayList<>();
        if (StringUtils.isNotEmpty(dto.getWkcCode())) {
            MtModWorkcellVO1 modWorkcellVO = new MtModWorkcellVO1();
            modWorkcellVO.setWorkcellCode(dto.getWkcCode());
            temp.addAll(mtModWorkcellRepository.propertyLimitWorkcellQuery(tenantId, modWorkcellVO).stream().distinct()
                    .collect(Collectors.toList()));

        } else {
            MtTagGroupObject mtTagGroupObject = new MtTagGroupObject();
            mtTagGroupObject.setTagGroupId(list.get(0));
            temp.addAll(mtTagGroupObjectRepository.propertyLimitTagGroupObjectQuery(tenantId, mtTagGroupObject));
        }
        if (CollectionUtils.isNotEmpty(temp) && temp.size() == 1) {
            wkcId = temp.get(0);
        }

        // 4.转换参数信息（若有多个TAG,则循环处理）
        MtMqttMessageVO3 result = new MtMqttMessageVO3();
        List<MtDataRecordVO3.TagData> messageVO4List = new ArrayList<>();
        MtDataRecordVO3.TagData mtMqttMessageVO4;
        MtTagVO mtTagVO = new MtTagVO();
        mtTagVO.setTagGroupCode(dto.getTagGroupCode());
        for (MtMqttMessageVO2 t : dto.getTags()) {
            mtMqttMessageVO4 = new MtDataRecordVO3.TagData();
            mtMqttMessageVO4.setTagValue(t.getItemValue());
            mtMqttMessageVO4.setRecordDate(t.getGetDate());
            mtTagVO.setTagCode(t.getTagName());
            List<String> tagGroupAssignId = self().tagCodeAndGroupCodeLimitTagGroupAssignGet(tenantId, mtTagVO);
            if (CollectionUtils.isNotEmpty(tagGroupAssignId) && StringUtils.isNotEmpty(tagGroupAssignId.get(0))) {
                throw new MtException("MT_DATA_RECORD_0003",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0003",
                                "DATA_RECORD", "tagName:" + t.getTagName(),
                                "【API:edginkMessageAnalysis】"));
            }
            MtTagGroupAssign mtTagGroupAssign =
                    mtTagGroupAssignRepository.tagGroupAssignGet(tenantId, tagGroupAssignId.get(0));
            if (mtTagGroupAssign == null || StringUtils.isEmpty(mtTagGroupAssign.getTagId())) {
                throw new MtException("MT_DATA_RECORD_0003",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0003",
                                "DATA_RECORD", "tagName:" + t.getTagName(),
                                "【API:edginkMessageAnalysis】"));
            }
            MtTag mtTag = self().tagGet(tenantId, mtTagGroupAssign.getTagId());
            if (mtTag != null && StringUtils.isNotEmpty(mtTag.getApiId())) {
                MtTagApi mtTagApi = new MtTagApi();
                mtTagApi.setTenantId(tenantId);
                mtTagApi.setApiId(mtTag.getApiId());
                mtTagApi = mtTagApiMapper.selectOne(mtTagApi);
                if (mtTagApi != null) {
                    String conversionValue = (String) getApiparameterTypes(tenantId, mtTagApi, t.getItemValue());
                    mtMqttMessageVO4.setTagValue(conversionValue);
                    if (StringUtils.isNotEmpty(conversionValue)
                            && ("EO".equals(mtTagGroup1.getTagGroupType())
                            || "DISTRIBUTION".equals(mtTagGroup1.getTagGroupType()))
                            && t.getTagName().startsWith("BARCODE")) {
                        result.setEoId(conversionValue);

                    }
                }
            }
            mtMqttMessageVO4.setTagId(mtTagGroupAssign.getTagId());
            messageVO4List.add(mtMqttMessageVO4);
        }
        result.setTagGroupId(list.get(0));
        result.setWorkcellId(wkcId);
        result.setTagGroupType(mtTagGroup1.getTagGroupType());
        result.setTagData(messageVO4List);
        return result;

    }

    private Object getApiparameterTypes(Long tenantId, MtTagApi dto, String parameter) {

        Object target = null;
        Method method = null;
        try {
            target = ApplicationContextHelper.getContext().getBean(dto.getApiClass());
            method = target.getClass().getDeclaredMethod(dto.getApiFunction(), Long.class, String.class);
        } catch (BeansException | NoSuchMethodException | SecurityException e) {
            target = null;
            method = null;
        }
        if (null == target || null == method) {
            return null;
        }

        try {
            return method.invoke(target, tenantId, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.info(e.getMessage());
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> edginkMessageAnalysisAndRecordProcess(Long tenantId, MtMqttMessageVO1 dto) {
        // 校验参数的合规性
        if (dto == null || StringUtils.isEmpty(dto.getTagGroupCode())) {
            throw new MtException("MT_DATA_RECORD_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0001",
                            "DATA_RECORD", "tagGroupCode",
                            "【API:edginkMessageAnalysisAndRecordProcess】"));
        }
        if (CollectionUtils.isEmpty(dto.getTags())
                || dto.getTags().stream().anyMatch(t -> StringUtils.isEmpty(t.getTagName()))) {
            throw new MtException("MT_DATA_RECORD_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_DATA_RECORD_0001",
                            "DATA_RECORD", "tagName", "【API:edginkMessageAnalysisAndRecordProcess】"));
        }

        // 创建事件(P5)
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ENGINK_TAG_RECORD_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 1.解析消息(P1)
        MtMqttMessageVO3 mtMqttMessageVO3 = edginkMessageAnalysis(tenantId, dto);

        // 2.分发类型首先获取应分发组清单
        // P2
        List<String> tagDataIds = new ArrayList<>();
        MtDataRecordVO4 mtDataRecordVO4 = null;
        Map<String, String> wkcMap = new HashMap<>(0);
        Map<String, String> tagGroupTyepMap = new HashMap<>(0);
        if ("DISTRIBUTION".equals(mtMqttMessageVO3.getTagGroupType())) {
            MtDataRecordVO3 mtDataRecordVO3 = new MtDataRecordVO3();
            mtDataRecordVO3.setEoId(mtMqttMessageVO3.getEoId());
            mtDataRecordVO3.setTagGroupId(mtMqttMessageVO3.getTagGroupId());
            mtDataRecordVO3.setWorkcellId(mtMqttMessageVO3.getWorkcellId());
            mtDataRecordVO3.setTagDataList(mtMqttMessageVO3.getTagData());

            // P7,8,9,11,12
            mtDataRecordVO4 = mtDataRecordRepository.distributionGroupLimitTagGroupQuery(tenantId, mtDataRecordVO3);
            MtTagGroupObject mtTagGroupObject;
            if (mtDataRecordVO4 != null) {

                // 批量获取tagGroup
                List<MtTagGroup> mtTagGroups =
                        mtTagGroupRepository.tagGroupBatchGet(tenantId, mtDataRecordVO4.getTagGroupIdList());
                tagGroupTyepMap.putAll(mtTagGroups.stream()
                        .collect(Collectors.toMap(MtTagGroup::getTagGroupId, MtTagGroup::getTagGroupType)));
                tagDataIds.addAll(mtDataRecordVO4.getTagGroupIdList());
                for (String t : mtDataRecordVO4.getTagGroupIdList()) {
                    mtTagGroupObject = new MtTagGroupObject();
                    mtTagGroupObject.setTagGroupId(t);
                    List<String> list = mtTagGroupObjectRepository.propertyLimitTagGroupObjectQuery(tenantId,
                            mtTagGroupObject);
                    if (CollectionUtils.isNotEmpty(list)) {
                        // 仅会获取到一条数据(P4)
                        MtTagGroupObject mtTagGroupObject1 =
                                mtTagGroupObjectRepository.tagGroupObjectGet(tenantId, list.get(0));
                        if (mtTagGroupObject1 != null && StringUtils.isNotEmpty(mtTagGroupObject1.getWorkcellId())) {
                            wkcMap.put(t, mtTagGroupObject1.getWorkcellId());
                        }
                    }
                }

            }
        } else {

            tagDataIds.add(mtMqttMessageVO3.getTagGroupId());
            tagGroupTyepMap.put(mtMqttMessageVO3.getTagGroupId(), mtMqttMessageVO3.getTagGroupType());
        }

        // 3.其他类型创建实绩（当[P2]为多个时，循环处理）
        MtTagGroupObject mtTagGroupObject;
        MtDataRecordVO mtDataRecordVO;
        List<String> result = new ArrayList<>();
        for (String t : tagDataIds) {
            if ("WKC".equals(tagGroupTyepMap.get(t))) {
                mtTagGroupObject = new MtTagGroupObject();
                mtTagGroupObject.setTagGroupId(t);
                List<String> list =
                        mtTagGroupObjectRepository.propertyLimitTagGroupObjectQuery(tenantId, mtTagGroupObject);

                mtDataRecordVO = new MtDataRecordVO();
                mtDataRecordVO.setTagGroupId(t);
                if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
                    MtTagGroupObject tagGroupObject =
                            mtTagGroupObjectRepository.tagGroupObjectGet(tenantId, list.get(0));
                    if (tagGroupObject != null) {
                        mtDataRecordVO.setWorkcellId(tagGroupObject.getWorkcellId());
                    }

                } else {
                    mtDataRecordVO.setWorkcellId(null);
                }
                for (MtDataRecordVO3.TagData e : mtMqttMessageVO3.getTagData()) {
                    mtDataRecordVO.setTagId(e.getTagId());
                    mtDataRecordVO.setTagValue(e.getTagValue());
                    mtDataRecordVO.setRecordDate(e.getRecordDate());
                    mtDataRecordVO.setEventId(eventId);
                    MtDataRecordVO6 mtDataRecordVO6 =
                            mtDataRecordRepository.allTypeDataRecordCreate(tenantId, mtDataRecordVO);
                    String tagRecordId = null;
                    if (null != mtDataRecordVO6) {
                        tagRecordId = mtDataRecordVO6.getDataRecordId();
                    }
                    if (StringUtils.isNotEmpty(tagRecordId)) {
                        result.add(tagRecordId);
                    }
                }
            }
            if ("EO".equals(tagGroupTyepMap.get(t))) {
                mtDataRecordVO = new MtDataRecordVO();
                if (mtDataRecordVO4 != null && StringUtils.isNotEmpty(mtDataRecordVO4.getBomComponentId())) {
                    MtBomComponentVO8 bomComponentVO8 = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                            mtDataRecordVO4.getBomComponentId());
                    if (bomComponentVO8 != null) {
                        mtDataRecordVO.setComponentMaterialId(bomComponentVO8.getMaterialId());
                    }
                    mtDataRecordVO.setOperationId(mtDataRecordVO4.getOperationId());
                    mtDataRecordVO.setEoStepActualId(mtDataRecordVO4.getEoStepActualId());
                    mtDataRecordVO.setAssembleConfirmId(mtDataRecordVO4.getAssembleConfirmId());
                    mtDataRecordVO.setNcCodeId(mtDataRecordVO4.getNcCodeId());
                }

                mtDataRecordVO.setTagGroupId(t);
                mtDataRecordVO.setEoId(mtMqttMessageVO3.getEoId());
                mtDataRecordVO.setWorkcellId(wkcMap.get(t));
                for (MtDataRecordVO3.TagData e : mtMqttMessageVO3.getTagData()) {
                    mtDataRecordVO.setTagId(e.getTagId());
                    mtDataRecordVO.setTagValue(e.getTagValue());
                    mtDataRecordVO.setRecordDate(e.getRecordDate());
                    mtDataRecordVO.setEventId(eventId);
                    MtDataRecordVO6 mtDataRecordVO6 =
                            mtDataRecordRepository.allTypeDataRecordCreate(tenantId, mtDataRecordVO);
                    String tagRecordId = null;
                    if (null != mtDataRecordVO6) {
                        tagRecordId = mtDataRecordVO6.getDataRecordId();
                    }
                    if (StringUtils.isNotEmpty(tagRecordId)) {
                        result.add(tagRecordId);
                    }
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long tagBatchUpdate(Long tenantId, List<MtTagVO2> voList, String fullUpdate) {
        if (CollectionUtils.isEmpty(voList) || voList.stream().anyMatch(vo -> StringUtils.isEmpty(vo.getTagCode()))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "TAG_CODE", "【API:tagBatchUpdate】"));
        }
        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("TAG_GROUP_ASSIGN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        List<String> sqlList = new ArrayList<String>();
        MtTag tag;
        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("GENERAL");
        mtGenTypeVO2.setTypeGroup("TAG_COLLECTION_METHOD");
        List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

        mtGenTypeVO2.setTypeGroup("TAG_VALUE_TYPE");
        List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

        // 批量获取主键和cid
        List<String> tagIds = this.customDbRepository.getNextKeys("mt_tag_s", voList.size());
        List<String> tagCids = this.customDbRepository.getNextKeys("mt_tag_cid_s", voList.size());

        List<String> tagHisCids = this.customDbRepository.getNextKeys("mt_tag_his_cid_s", voList.size());
        List<String> tagHisIds = this.customDbRepository.getNextKeys("mt_tag_his_s", voList.size());

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date currentDate = new Date(System.currentTimeMillis());

        // 批量查询,先将输入的tagCode变成集合
        List<String> tagCodeList = voList.stream().map(MtTagVO2::getTagCode).collect(Collectors.toList());
        List<MtTag> mtTagList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tagCodeList)) {
            // 根据tagCode列表去查询对应的集合数据
            mtTagList = mtTagMapper.selectByCodeList(tenantId, tagCodeList);
        }
        Map<String, MtTag> mtTagMap = new HashMap<>(0);

        if (CollectionUtils.isNotEmpty(mtTagList)) {
            // 将数据封装成map集合
            mtTagMap = mtTagList.stream().collect(Collectors.toMap(t -> t.getTagCode(), t -> t));
        }

        // 批量查询，将unit变成集合
        List<String> unitList = voList.stream().filter(t -> StringUtils.isNotEmpty(t.getUnit())).map(MtTagVO2::getUnit)
                .collect(Collectors.toList());
        List<MtUom> mtUoms = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(unitList)) {
            mtUoms = mtUomRepository.uomPropertyBatchGetByCodes(tenantId, unitList);
        }
        Map<String, MtUom> mtUomMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtUoms)) {
            mtUomMap = mtUoms.stream().collect(Collectors.toMap(MtUom::getUomCode, t -> t));
        }

        if (voList.size() != mtUomMap.size()) {
            throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0045", "GENERAL", "Unit", "【API:tagBatchUpdate】"));
        }
        for (int i = 0; i < voList.size(); i++) {
            // 从map集合中根据输入的tagCode取数据
            tag = mtTagMap.get(voList.get(i).getTagCode());
            // 检验参数
            if (null != voList.get(i).getEnableFlag()
                    && !Arrays.asList("Y", "N").contains(voList.get(i).getEnableFlag())) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "Enable_Flag", "【API:tagBatchUpdate】"));
            }
            if (null != voList.get(i).getValueAllowMissing()
                    && !Arrays.asList("Y", "N").contains(voList.get(i).getValueAllowMissing())) {
                throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0045", "GENERAL", "Value_Allow_Missing", "【API:tagBatchUpdate】"));
            }

            if (null != voList.get(i).getCollectionMethod()) {
                int finalI = i;
                boolean collectionMethodFlag = mtGenTypes.stream().noneMatch(
                        t -> voList.get(finalI).getCollectionMethod().equalsIgnoreCase(t.getTypeCode()));
                if (collectionMethodFlag) {
                    throw new MtException("MT_GENERAL_0045",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0045",
                                    "GENERAL", "Collection_Method", "【API:tagBatchUpdate】"));
                }
            }

            if (null != voList.get(i).getValueType()) {
                int finalI1 = i;
                boolean typeGroupFlag = genTypes.stream()
                        .noneMatch(t -> voList.get(finalI1).getValueType().equalsIgnoreCase(t.getTypeCode()));
                if (typeGroupFlag) {
                    throw new MtException("MT_GENERAL_0045", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_GENERAL_0045", "GENERAL", "Type_Group", "【API:tagBatchUpdate】"));
                }
            }

            String uomId = null;
            if (null != voList.get(i).getUnit()) {
                MtUom uom = mtUomMap.get(voList.get(i).getUnit());
                uomId = null == uom ? "" : uom.getUomId();
            }

            if (null == tag) {
                // 新增数据
                MtTag tagN = new MtTag();
                BeanUtils.copyProperties(voList.get(i), tagN);
                tagN.setTenantId(tenantId);
                tagN.setTagId(tagIds.get(i));
                tagN.setCid(Long.valueOf(tagCids.get(i)));
                tagN.setLastUpdateDate(currentDate);
                tagN.setCreationDate(currentDate);
                tagN.setLastUpdatedBy(userId);
                tagN.setCreatedBy(userId);
                tagN.setObjectVersionNumber(1L);

                MtTagHis mtTagHis = new MtTagHis();
                mtTagHis.setTagId(tagN.getTagId());
                mtTagHis.setTagCode(tagN.getTagCode());
                mtTagHis.setTagDescription(tagN.getTagDescription());
                mtTagHis.setRemark(tagN.getRemark());
                mtTagHis.setEnableFlag(tagN.getEnableFlag());
                mtTagHis.setCollectionMethod(tagN.getCollectionMethod());
                mtTagHis.setValueType(tagN.getValueType());
                mtTagHis.setTrueValue(tagN.getTrueValue());
                mtTagHis.setFalseValue(tagN.getFalseValue());
                mtTagHis.setMinimumValue(tagN.getMinimumValue());
                mtTagHis.setMaximalValue(tagN.getMaximalValue());
                mtTagHis.setUnit(tagN.getUnit());
                mtTagHis.setValueAllowMissing(tagN.getValueAllowMissing());
                mtTagHis.setMandatoryNum(tagN.getMandatoryNum());
                mtTagHis.setOptionalNum(tagN.getOptionalNum());
                mtTagHis.setApiId(tagN.getApiId());
                mtTagHis.setCreationDate(tagN.getCreationDate());
                mtTagHis.setCreatedBy(tagN.getCreatedBy());
                mtTagHis.setLastUpdateDate(tagN.getLastUpdateDate());
                mtTagHis.setLastUpdatedBy(tagN.getLastUpdatedBy());
                mtTagHis.setObjectVersionNumber(tagN.getObjectVersionNumber());
                mtTagHis.setTableId(tagN.getTableId());
                mtTagHis.set_tls(tagN.get_tls());
                mtTagHis.set_token(tagN.get_token());
                mtTagHis.set_status(tagN.get_status());
                mtTagHis.setFlex(tagN.getFlex());
                mtTagHis.setEventId(eventId);
                mtTagHis.setTenantId(tenantId);
                mtTagHis.setCid(Long.valueOf(tagHisCids.get(i)));
                mtTagHis.setTagHisId(tagHisIds.get(i));

                sqlList.addAll(customDbRepository.getInsertSql(mtTagHis));
                sqlList.addAll(customDbRepository.getInsertSql(tagN));
            } else {
                // 更新数据
                tag.setTenantId(tenantId);
                tag.setCid(Long.valueOf(tagCids.get(i)));
                tag.setLastUpdateDate(currentDate);
                tag.setLastUpdatedBy(userId);
                tag.setObjectVersionNumber(1L);

                if ("Y".equalsIgnoreCase(fullUpdate)) {
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> StringUtils.isEmpty(vo.getTagCode()))) {
                        throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_GENERAL_0001", "GENERAL", "TAG_CODE", "【API:tagBatchUpdate】"));
                    }
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> StringUtils.isEmpty(vo.getTagDescription()))) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "Tag_Description", "【API:tagBatchUpdate】"));
                    }
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> StringUtils.isEmpty(vo.getCollectionMethod()))) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "Collection_Method", "【API:tagBatchUpdate】"));
                    }
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> StringUtils.isEmpty(vo.getValueType()))) {
                        throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_GENERAL_0001", "GENERAL", "Value_Type", "【API:tagBatchUpdate】"));
                    }
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> StringUtils.isEmpty(vo.getValueAllowMissing()))) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "Value_Allow_Missing", "【API:tagBatchUpdate】"));
                    }

                    tag.setMandatoryNum(voList.get(i).getMandatoryNum());
                    tag.setMaximalValue(voList.get(i).getMaximalValue());
                    tag.setMinimumValue(voList.get(i).getMinimumValue());
                    tag.setOptionalNum(voList.get(i).getOptionalNum());
                    tag.setApiId(voList.get(i).getApiId() == null ? "" : voList.get(i).getApiId());
                    tag.setEnableFlag(voList.get(i).getEnableFlag());
                    tag.setRemark(voList.get(i).getRemark());
                    tag.setTagDescription(
                            voList.get(i).getTagDescription() == null ? "" : voList.get(i).getTagDescription());
                    tag.setTrueValue(voList.get(i).getTrueValue() == null ? "" : voList.get(i).getTrueValue());
                    tag.setValueAllowMissing(voList.get(i).getValueAllowMissing());
                    tag.setFalseValue(voList.get(i).getFalseValue() == null ? "" : voList.get(i).getFalseValue());
                    tag.setCollectionMethod(voList.get(i).getCollectionMethod());
                    tag.setValueType(voList.get(i).getValueType());
                    tag.setUnit(uomId);

                    MtTagHis mtTagHis = new MtTagHis();
                    mtTagHis.setTagId(tag.getTagId());
                    mtTagHis.setTagCode(tag.getTagCode());
                    mtTagHis.setTagDescription(tag.getTagDescription());
                    mtTagHis.setRemark(tag.getRemark());
                    mtTagHis.setEnableFlag(tag.getEnableFlag());
                    mtTagHis.setCollectionMethod(tag.getCollectionMethod());
                    mtTagHis.setValueType(tag.getValueType());
                    mtTagHis.setTrueValue(tag.getTrueValue());
                    mtTagHis.setFalseValue(tag.getFalseValue());
                    mtTagHis.setMinimumValue(tag.getMinimumValue());
                    mtTagHis.setMaximalValue(tag.getMaximalValue());
                    mtTagHis.setUnit(tag.getUnit());
                    mtTagHis.setValueAllowMissing(tag.getValueAllowMissing());
                    mtTagHis.setMandatoryNum(tag.getMandatoryNum());
                    mtTagHis.setOptionalNum(tag.getOptionalNum());
                    mtTagHis.setApiId(tag.getApiId());
                    mtTagHis.setCreationDate(tag.getCreationDate());
                    mtTagHis.setCreatedBy(tag.getCreatedBy());
                    mtTagHis.setLastUpdateDate(tag.getLastUpdateDate());
                    mtTagHis.setLastUpdatedBy(tag.getLastUpdatedBy());
                    mtTagHis.setObjectVersionNumber(tag.getObjectVersionNumber());
                    mtTagHis.setTableId(tag.getTableId());
                    mtTagHis.set_tls(tag.get_tls());
                    mtTagHis.set_token(tag.get_token());
                    mtTagHis.set_status(tag.get_status());
                    mtTagHis.setFlex(tag.getFlex());
                    mtTagHis.setEventId(eventId);
                    mtTagHis.setTenantId(tenantId);
                    mtTagHis.setCid(Long.valueOf(tagHisCids.get(i)));
                    mtTagHis.setTagHisId(tagHisIds.get(i));

                    sqlList.addAll(customDbRepository.getInsertSql(mtTagHis));
                    // tag.setTagCode(tagVO.getTagCode()); //前面已经判断设置了，这里可以不设置
                    sqlList.addAll(customDbRepository.getFullUpdateSql(tag));
                } else {
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> "".equalsIgnoreCase(vo.getTagCode()))) {
                        throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_GENERAL_0001", "GENERAL", "TAG_CODE", "【API:tagBatchUpdate】"));
                    }
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> "".equalsIgnoreCase(vo.getTagDescription()))) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "Tag_Description", "【API:tagBatchUpdate】"));
                    }

                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> "".equalsIgnoreCase(vo.getCollectionMethod()))) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "Collection_Method", "【API:tagBatchUpdate】"));
                    }
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> "".equalsIgnoreCase(vo.getValueType()))) {
                        throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_GENERAL_0001", "GENERAL", "Value_Type", "【API:tagBatchUpdate】"));
                    }
                    if (CollectionUtils.isEmpty(voList)
                            || voList.stream().anyMatch(vo -> "".equalsIgnoreCase(vo.getValueAllowMissing()))) {
                        throw new MtException("MT_GENERAL_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                        "GENERAL", "Value_Allow_Missing", "【API:tagBatchUpdate】"));
                    }

                    if (null != voList.get(i).getMandatoryNum()) {
                        tag.setMandatoryNum(voList.get(i).getMandatoryNum());
                    }
                    if (null != voList.get(i).getMaximalValue()) {
                        tag.setMaximalValue(voList.get(i).getMaximalValue());
                    }
                    if (null != voList.get(i).getMinimumValue()) {
                        tag.setMinimumValue(voList.get(i).getMinimumValue());
                    }
                    if (null != voList.get(i).getOptionalNum()) {
                        tag.setOptionalNum(voList.get(i).getOptionalNum());
                    }

                    // 不判断非空
                    if (null != voList.get(i).getApiId()) {
                        tag.setApiId(voList.get(i).getApiId());
                    }
                    if (null != voList.get(i).getEnableFlag()) {
                        tag.setEnableFlag(voList.get(i).getEnableFlag());
                    }
                    if (null != voList.get(i).getRemark()) {
                        tag.setRemark(voList.get(i).getRemark());
                    }
                    if (null != voList.get(i).getTagDescription()) {
                        tag.setTagDescription(voList.get(i).getTagDescription());
                    }
                    if (null != voList.get(i).getTrueValue()) {
                        tag.setTrueValue(voList.get(i).getTrueValue());
                    }
                    if (null != voList.get(i).getValueAllowMissing()) {
                        tag.setValueAllowMissing(voList.get(i).getValueAllowMissing());
                    }
                    if (null != voList.get(i).getFalseValue()) {
                        tag.setFalseValue(voList.get(i).getFalseValue());
                    }

                    MtTagHis mtTagHis = new MtTagHis();
                    BeanUtils.copyProperties(tag, mtTagHis);
                    mtTagHis.setEventId(eventId);
                    mtTagHis.setTenantId(tenantId);
                    mtTagHis.setCid(Long.valueOf(tagHisCids.get(i)));
                    mtTagHis.setTagHisId(tagHisIds.get(i));

                    sqlList.addAll(customDbRepository.getInsertSql(mtTagHis));
                    sqlList.addAll(customDbRepository.getUpdateSql(tag));
                }
            }
        }
        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return Long.valueOf(voList.size());
    }

    @Override
    public List<MtTagVO4> propertyLimitTagPropertyQuery(Long tenantId, MtTagVO3 dto) {
        List<MtTagVO4> voList = mtTagMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return voList;
        }
        List<String> unitList = voList.stream().map(MtTagVO4::getUnit).filter(StringUtils::isNotEmpty).distinct()
                .collect(Collectors.toList());
        Map<String, MtUomVO> mtUomVOMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(unitList)) {
            List<MtUomVO> mtUomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, unitList);
            if (CollectionUtils.isNotEmpty(mtUomVOS)) {
                mtUomVOMap = mtUomVOS.stream().collect(Collectors.toMap(t -> t.getUomId(), t -> t));
            }
        }

        for (MtTagVO4 tagVO4 : voList) {
            tagVO4.setUomCode(mtUomVOMap.get(tagVO4.getUnit()) == null ? null
                    : mtUomVOMap.get(tagVO4.getUnit()).getUomCode());
            tagVO4.setUomName(mtUomVOMap.get(tagVO4.getUnit()) == null ? null
                    : mtUomVOMap.get(tagVO4.getUnit()).getUomName());
        }
        voList.sort(Comparator.comparing(c -> new BigDecimal(c.getTagId())));
        return voList;
    }

    @Override
    public List<MtTag> selectByCodeList(Long tenantId, List<String> tagCodeList) {
        return mtTagMapper.selectByCodeList(tenantId, tagCodeList);
    }
}
