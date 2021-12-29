package tarzan.instruction.infra.repository.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocHisRepository;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO1;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 指令单据头表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@Component
public class MtInstructionDocRepositoryImpl extends BaseRepositoryImpl<MtInstructionDoc>
                implements MtInstructionDocRepository {


    private static final String STATUS_NEW = "NEW";
    private static final String STATUS_RELEASED = "RELEASED";
    private static final String STATUS_CANCEL = "CANCEL";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_COMPLETED_CANCEL = "COMPLETED_CANCEL";

    private static final String INSTRUCTION_DOC_CREATE_EVENT_TYPE_CODE = "INSTRUCTION_DOC_CREATE";
    private static final String INSTRUCTION_DOC_STATUS_UPDATE_EVENT_TYPE_CODE = "INSTRUCTION_DOC_STATUS_UPDATE";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtInstructionDocHisRepository mtInstructionDocHisRepository;

    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtNumrangeObjectRepository mtNumrangeObjectRepository;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Override
    public List<String> propertyLimitInstructionDocQuery(Long tenantId, MtInstructionDocVO4 mtInstructionDoc) {

        mtInstructionDoc.setTenantId(tenantId);
        return mtInstructionDocMapper.selectMtInstructionDocIds(tenantId, mtInstructionDoc);
    }

    @Override
    public MtInstructionDoc instructionDocPropertyGet(Long tenantId, String instructionDocId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId", "【API：instructionDocPropertyGet】"));
        }
        MtInstructionDoc doc = new MtInstructionDoc();
        doc.setTenantId(tenantId);
        doc.setInstructionDocId(instructionDocId);
        return mtInstructionDocMapper.selectOne(doc);

    }

    @Override
    public List<MtInstructionDoc> instructionDocPropertyBatchGet(Long tenantId, List<String> instructionDocIdList) {
        if (CollectionUtils.isEmpty(instructionDocIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocIdList",
                                            "【API：instructionDocPropertyBatchGet】"));
        }

        return mtInstructionDocMapper.selectByIdList(tenantId, instructionDocIdList);
    }

    @Override
    public String instructionDocNextNumGet(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "siteId", "【API：instructionDocNextNumGet】"));
        }

        // 2. 根据当前系统事件获取六位年月日
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        String dateStr = format.format(new Date());

        // 3. 根据输入siteId获取四位整数编码
        String siteStr = "";
        if (siteId.contains(".")) {
            String[] siteSplit = siteId.split("\\.");
            // 点前部分
            siteStr = siteSplit[0];
        } else {
            siteStr = siteId;
        }

        if (siteStr.length() > 4) {
            siteStr = siteStr.substring(siteStr.length() - 4);
        } else {
            siteStr = String.format("%04d", Integer.valueOf(siteStr));
        }

        // 4. 根据当前时间和站点获取盘点单编码，12位后的数字部分最大值
        StringBuilder siteDateStr = new StringBuilder("INSD").append(siteStr).append(dateStr);
        Integer maxNum = mtInstructionDocMapper.selectMaxNum(tenantId, siteDateStr.toString());

        if (maxNum == null) {
            return siteDateStr.append("0001").toString();
        } else {
            return siteDateStr.append(String.format("%04d", maxNum + 1)).toString();
        }
    }


    /**
     * 创建DOC前进行校验
     * 
     * @author benjamin
     * @date 2019-06-18 17:08
     * @param tenantId tenantId
     * @param mtLogisticInstructionDoc MtLogisticInstructionDocVO
     */
    private void instructionDocUpdateVerify(Long tenantId, MtInstructionDoc mtLogisticInstructionDoc) {
        if (StringUtils.isEmpty(mtLogisticInstructionDoc.getSiteId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "siteId", "【API:instructionDocUpdate】"));
        }
        if (StringUtils.isEmpty(mtLogisticInstructionDoc.getInstructionDocType())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionDocType", "【API:instructionDocUpdate】"));
        }

        if (StringUtils.isNotEmpty(mtLogisticInstructionDoc.getInstructionDocNum())) {
            MtInstructionDocVO4 queryDoc = new MtInstructionDocVO4();
            queryDoc.setInstructionDocNum(mtLogisticInstructionDoc.getInstructionDocNum());
            List<String> logisticInstructionDocNumList = propertyLimitInstructionDocQuery(tenantId, queryDoc);
            if (CollectionUtils.isNotEmpty(logisticInstructionDocNumList)) {
                throw new MtException("MT_INSTRUCTION_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0002",
                                                "INSTRUCTION", mtLogisticInstructionDoc.getInstructionDocNum(),
                                                "【API:instructionDocUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(mtLogisticInstructionDoc.getIdentification())) {
            MtInstructionDocVO4 queryDoc = new MtInstructionDocVO4();
            queryDoc.setIdentification(mtLogisticInstructionDoc.getIdentification());
            List<String> logisticInstructionDocNumList = propertyLimitInstructionDocQuery(tenantId, queryDoc);
            if (CollectionUtils.isNotEmpty(logisticInstructionDocNumList)) {
                throw new MtException("MT_INSTRUCTION_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0002",
                                                "INSTRUCTION", mtLogisticInstructionDoc.getIdentification(),
                                                "【API:instructionDocUpdate】"));
            }
        }

        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("INSTRUCTION");
        mtGenTypeVO2.setTypeGroup("INSTRUCTION_DOC_TYPE");
        List<String> instructionDocTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2).stream()
                        .map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
        if (!instructionDocTypeList.contains(mtLogisticInstructionDoc.getInstructionDocType())) {
            throw new MtException("MT_INSTRUCTION_0011",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0011",
                                            "INSTRUCTION", mtLogisticInstructionDoc.getInstructionDocType(),
                                            instructionDocTypeList.toString(), "【API:instructionDocUpdate】"));
        }

        if (StringUtils.isNotEmpty(mtLogisticInstructionDoc.getSupplierSiteId())
                        && StringUtils.isEmpty(mtLogisticInstructionDoc.getSupplierId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "supplierId", "【API:instructionDocUpdate】"));
        }

        boolean customerEmptyCheck = StringUtils.isEmpty(mtLogisticInstructionDoc.getCustomerId())
                        && StringUtils.isEmpty(mtLogisticInstructionDoc.getCustomerSiteId());
        boolean customerNotEmptyCheck = StringUtils.isNotEmpty(mtLogisticInstructionDoc.getCustomerId())
                        && StringUtils.isNotEmpty(mtLogisticInstructionDoc.getCustomerSiteId());
        if (!(customerEmptyCheck || customerNotEmptyCheck)) {
            throw new MtException("MT_INSTRUCTION_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                            "INSTRUCTION", "customerId", "customerSiteId",
                                            "【API:instructionDocUpdate】"));
        }

        if (StringUtils.isNotEmpty(mtLogisticInstructionDoc.getInstructionDocStatus())) {
            MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
            mtGenStatusVO2.setModule("INSTRUCTION");
            mtGenStatusVO2.setStatusGroup("INSTRUCTION_DOC_STATUS");
            List<MtGenStatus> genStatusList =
                            this.mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
            String status = "[]";

            if (CollectionUtils.isEmpty(genStatusList)) {
                throw new MtException("MT_INSTRUCTION_0044",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0044",
                                                "INSTRUCTION", mtLogisticInstructionDoc.getInstructionDocStatus(),
                                                status, "【API:instructionDocUpdate】"));
            }

            List<String> statusList =
                            genStatusList.stream().map(MtGenStatus::getStatusCode).collect(Collectors.toList());
            if (!statusList.contains(mtLogisticInstructionDoc.getInstructionDocStatus())) {
                status = statusList.toString();
                throw new MtException("MT_INSTRUCTION_0044",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0044",
                                                "INSTRUCTION", mtLogisticInstructionDoc.getInstructionDocStatus(),
                                                status, "【API:instructionDocUpdate】"));
            }
        }

        boolean sourceOrderEmptyCheck = StringUtils.isEmpty(mtLogisticInstructionDoc.getSourceOrderId())
                        && StringUtils.isEmpty(mtLogisticInstructionDoc.getSourceOrderType());
        boolean sourceOrderNotEmptyCheck = StringUtils.isNotEmpty(mtLogisticInstructionDoc.getSourceOrderId())
                        && StringUtils.isNotEmpty(mtLogisticInstructionDoc.getSourceOrderType());
        if (!(sourceOrderEmptyCheck || sourceOrderNotEmptyCheck)) {
            throw new MtException("MT_INSTRUCTION_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                            "INSTRUCTION", "sourceOrderId", "sourceOrderType",
                                            "【API:instructionDocUpdate】"));
        }
    }

    /**
     * 构建号码段对象
     * 
     * @author benjamin
     * @date 2019-07-18 12:42
     * @param createVO MtLogisticInstructionDocVO2
     * @param objectCode 对象编码
     * @param incomingValueList 标识参数列表
     * @param outsideNum 原有值
     * @return MtNumrangeVO2
     */
    private MtNumrangeVO2 constructNumRange(Long tenantId, MtInstructionDocDTO2 createVO, String objectCode,
                    List<String> incomingValueList, String outsideNum) {
        MtNumrangeVO2 createNum = new MtNumrangeVO2();
        createNum.setObjectCode(objectCode);
        createNum.setSiteId(createVO.getSiteId());

        String typeCode = null;
        String objectColumnCode = null;

        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        // objectCode tenantId唯一约束
        numrangeObject.setObjectCode(objectCode);
        List<String> objectIdList =
                mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        String objectId = null;
        if (CollectionUtils.isNotEmpty(objectIdList)) {
            objectId = objectIdList.get(0);
        }

        // 获取该对象所有属性列objectColumnCode
        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectId);
        List<MtNumrangeObjectColumn> mtNumrangeObjectColumnList = mtNumrangeObjectColumnRepository
                .propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);
        List<MtNumrangeObjectColumn> collect = mtNumrangeObjectColumnList.stream().filter(
                t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            objectColumnCode = collect.get(0).getObjectColumnCode();
            typeCode = collect.get(0).getTypeGroup();
        }

        Map<String, String> callObjectCodeMap = new HashMap<>(0);
        // 对属性列进行赋值并记录OBJECT_COLUMN_CODE及对应的由传入参数获取的值
        for (MtNumrangeObjectColumn column : mtNumrangeObjectColumnList) {
            if (StringUtils.isEmpty(column.getObjectColumnCode())) {
                continue;
            }
            switch (column.getObjectColumnCode()) {
                case "siteCode":
                    MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, createVO.getSiteId());
                    callObjectCodeMap.put("siteCode", mtModSite == null ? null : mtModSite.getSiteCode());
                    break;
                case "instructionDocType":
                    callObjectCodeMap.put("instructionDocType", createVO.getInstructionDocType());
                    break;
                case "sourceOrderType":
                    callObjectCodeMap.put("sourceOrderType", createVO.getSourceOrderType());
                    break;
                default:
                    break;
            }
        }

        if (StringUtils.isNotEmpty(outsideNum)) {
            createNum.setOutsideNum(outsideNum);
        } else {
            createNum.setCallObjectCodeList(callObjectCodeMap);
            createNum.setIncomingValueList(CollectionUtils.isEmpty(incomingValueList) ? null : incomingValueList);
        }

        if (StringUtils.isNotEmpty(typeCode)) {
            createNum.setObjectTypeCode(callObjectCodeMap.get(objectColumnCode));
        }
        return createNum;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtInstructionDocVO3 instructionDocUpdate(Long tenantId, MtInstructionDocDTO2 vo, String fullUpdate) {
        MtInstructionDocVO3 result = new MtInstructionDocVO3();
        MtInstructionDoc mtLogisticInstructionDoc;
        String instructionDocHisId;
        String warningMessage = null;
        if (StringUtils.isEmpty(vo.getInstructionDocId())) {
            // 新增逻辑
            mtLogisticInstructionDoc = new MtInstructionDoc();
            BeanUtils.copyProperties(vo, mtLogisticInstructionDoc);
            // verify before creating
            instructionDocUpdateVerify(tenantId, mtLogisticInstructionDoc);

            if (StringUtils.isNotEmpty(vo.getEventId())) {
                throw new MtException("MT_INSTRUCTION_0044",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0044",
                                                "INSTRUCTION", "eventId", "【API:instructionDocUpdate】"));
            }
            // construct logistic instruction doc object
            MtNumrangeVO2 createDocNum = constructNumRange(tenantId, vo, "INSTRUCTION_DOC_NUM",
                            vo.getNumIncomingValueList(), vo.getInstructionDocNum());
//            createDocNum.setObjectTypeCode("deliveryDoc");
            MtNumrangeVO5 generateDocNum = mtNumrangeRepository.numrangeGenerate(tenantId, createDocNum);
            mtLogisticInstructionDoc.setInstructionDocNum(generateDocNum.getNumber());
            warningMessage = generateDocNum.getWarningMessage();
            // when status is empty then set default value: NEW
            if (StringUtils.isEmpty(vo.getInstructionDocStatus())) {
                mtLogisticInstructionDoc.setInstructionDocStatus(STATUS_NEW);
            }

            mtLogisticInstructionDoc
                            .setIdentification(StringUtils.isEmpty(vo.getIdentification()) ? generateDocNum.getNumber()
                                            : vo.getIdentification());

            mtLogisticInstructionDoc.setTenantId(tenantId);
            List<MtInstructionDoc> originDocList =
                            mtInstructionDocMapper.selectByUnique(tenantId, mtLogisticInstructionDoc);
            if (CollectionUtils.isEmpty(originDocList)) {
                self().insertSelective(mtLogisticInstructionDoc);
            } else {
                final String checkDocNum = mtLogisticInstructionDoc.getInstructionDocNum();
                String errorFieldName =
                                originDocList.stream().anyMatch(doc -> checkDocNum.equals(doc.getInstructionDocNum()))
                                                ? "instructionDocNum: "
                                                                + mtLogisticInstructionDoc.getInstructionDocNum()
                                                : "identification: " + mtLogisticInstructionDoc.getIdentification();
                throw new MtException("MT_INSTRUCTION_0037",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0037",
                                                "INSTRUCTION", errorFieldName, "【API:instructionDocUpdate】"));
            }

            // create event
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventRequestId(vo.getEventRequestId());
            eventCreateVO.setEventTypeCode(INSTRUCTION_DOC_CREATE_EVENT_TYPE_CODE);
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // save history
            instructionDocHisId = mtInstructionDocHisRepository.saveLogisticInstructionDocHistory(tenantId,
                            mtLogisticInstructionDoc, eventId);

            // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
            mtLogisticInstructionDoc.setLatestHisId(instructionDocHisId);
            self().updateByPrimaryKeySelective(mtLogisticInstructionDoc);
        } else {
            // 更新逻辑
            if (StringUtils.isEmpty(vo.getEventId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "eventId", "【API:instructionDocUpdate】"));
            }

            mtLogisticInstructionDoc = instructionDocPropertyGet(tenantId, vo.getInstructionDocId());
            if (mtLogisticInstructionDoc == null) {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", vo.getInstructionDocId(), "【API:instructionDocUpdate】"));
            }

            // 校验instructionDocType有效性
            if (StringUtils.isNotEmpty(vo.getInstructionDocType())) {
                MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
                mtGenTypeVO2.setModule("INSTRUCTION");
                mtGenTypeVO2.setTypeGroup("INSTRUCTION_DOC_TYPE");
                List<MtGenType> instructionDocTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                List<String> instructionDocTypeCodes = instructionDocTypes.stream().map(MtGenType::getTypeCode)
                                .distinct().collect(Collectors.toList());
                if (!instructionDocTypeCodes.contains(vo.getInstructionDocType())) {
                    throw new MtException("MT_INSTRUCTION_0011",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0011",
                                                    "INSTRUCTION", "instructionDocType:" + vo.getInstructionDocType(),
                                                    instructionDocTypeCodes.toString(), "【API:instructionDocUpdate】"));
                }
            }

            if (StringUtils.isNotEmpty(vo.getSupplierSiteId()) && StringUtils.isEmpty(vo.getSupplierId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "supplierId", "【API:instructionDocUpdate】"));
            }

            if (StringUtils.isEmpty(vo.getCustomerId()) && StringUtils.isNotEmpty(vo.getCustomerSiteId())
                            || StringUtils.isNotEmpty(vo.getCustomerId())
                                            && StringUtils.isEmpty(vo.getCustomerSiteId())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "customerId", "customerSiteId",
                                                "【API:instructionDocUpdate】"));
            }

            if (StringUtils.isEmpty(vo.getSourceOrderId()) && StringUtils.isNotEmpty(vo.getSourceOrderType())
                            || StringUtils.isNotEmpty(vo.getSourceOrderId())
                                            && StringUtils.isEmpty(vo.getSourceOrderType())) {
                throw new MtException("MT_INSTRUCTION_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                                "INSTRUCTION", "sourceOrderId", "sourceOrderType",
                                                "【API:instructionDocUpdate】"));
            }

            // update field INSTRUCTION_DOC_STATUS & REMARK
            mtLogisticInstructionDoc.setInstructionDocStatus(vo.getInstructionDocStatus());
            mtLogisticInstructionDoc.setInstructionDocType(vo.getInstructionDocType());
            mtLogisticInstructionDoc.setSiteId(vo.getSiteId());
            mtLogisticInstructionDoc.setSourceOrderType(vo.getSourceOrderType());
            mtLogisticInstructionDoc.setSourceOrderId(vo.getSourceOrderId());
            mtLogisticInstructionDoc.setSupplierId(vo.getSupplierId());
            mtLogisticInstructionDoc.setSupplierSiteId(vo.getSupplierSiteId());
            mtLogisticInstructionDoc.setCustomerId(vo.getCustomerId());
            mtLogisticInstructionDoc.setCustomerSiteId(vo.getCustomerSiteId());
            mtLogisticInstructionDoc.setExpectedArrivalTime(vo.getExpectedArrivalTime());
            mtLogisticInstructionDoc.setDemandTime(vo.getDemandTime());
            mtLogisticInstructionDoc.setCostCenterId(vo.getCostCenterId());
            mtLogisticInstructionDoc.setPersonId(vo.getPersonId());
            mtLogisticInstructionDoc.setRemark(vo.getRemark());
            mtLogisticInstructionDoc.setReason(vo.getReason());
            mtLogisticInstructionDoc.setTenantId(tenantId);

            // save history
            instructionDocHisId = mtInstructionDocHisRepository.saveLogisticInstructionDocHistory(tenantId,
                            mtLogisticInstructionDoc, vo.getEventId());

            // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
            mtLogisticInstructionDoc.setLatestHisId(instructionDocHisId);
            if ("Y".equals(fullUpdate)) {
                mtLogisticInstructionDoc =
                                (MtInstructionDoc) ObjectFieldsHelper.setStringFieldsEmpty(mtLogisticInstructionDoc);
                self().updateByPrimaryKey(mtLogisticInstructionDoc);
            } else {
                self().updateByPrimaryKeySelective(mtLogisticInstructionDoc);
            }
        }

        result.setInstructionDocId(mtLogisticInstructionDoc.getInstructionDocId());
        result.setInstructionDocHisId(instructionDocHisId);
        result.setWarningMessage(warningMessage);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionDocRelease(Long tenantId, String instructionDocId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionDocId", "【API:instructionDocRelease】"));
        }

        instructionDocReleaseVerify(tenantId, instructionDocId);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_DOC_STATUS_UPDATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // update doc status
        MtInstructionDocDTO2 updateVO = new MtInstructionDocDTO2();
        updateVO.setInstructionDocId(instructionDocId);
        updateVO.setInstructionDocStatus(STATUS_RELEASED);
        updateVO.setEventId(eventId);
        instructionDocUpdate(tenantId, updateVO, "N");

        MtInstructionVO10 mtInstruction = new MtInstructionVO10();
        mtInstruction.setSourceDocId(instructionDocId);
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);


        // update by xie yi yang
        // 调用API{instructionPropertyBatchGet}获取instructionStatus≠CANCEL的instructionIdList
        if (CollectionUtils.isNotEmpty(instructionIdList)) {
            List<MtInstruction> mtInstructions =
                            mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
            if (CollectionUtils.isNotEmpty(mtInstructions)) {
                List<String> newInstructionIdList =
                                mtInstructions.stream().filter(t -> !STATUS_CANCEL.equals(t.getInstructionStatus()))
                                                .map(MtInstruction::getInstructionId).collect(Collectors.toList());

                // update instruction status
                for (String instructionId : newInstructionIdList) {
                    mtInstructionRepository.instructionRelease(tenantId, instructionId, eventRequestId);
                }
            }
        }
    }

    @Override
    public void instructionDocReleaseVerify(Long tenantId, String instructionDocId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId", "【API:instructionDocReleaseVerify】"));
        }

        MtInstructionDoc mtLogisticInstructionDoc = instructionDocPropertyGet(tenantId, instructionDocId);

        if (mtLogisticInstructionDoc == null
                        || !(STATUS_NEW.equals(mtLogisticInstructionDoc.getInstructionDocStatus()))) {
            throw new MtException("MT_INSTRUCTION_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0014", "INSTRUCTION", "【API:instructionDocReleaseVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionDocCancel(Long tenantId, String instructionDocId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionDocId", "【API:instructionDocCancel】"));
        }

        instructionDocCancelVerify(tenantId, instructionDocId);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_DOC_STATUS_UPDATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // update doc status
        MtInstructionDocDTO2 updateVO = new MtInstructionDocDTO2();
        updateVO.setInstructionDocId(instructionDocId);
        updateVO.setInstructionDocStatus(STATUS_CANCEL);
        updateVO.setEventId(eventId);
        instructionDocUpdate(tenantId, updateVO, "N");

        MtInstructionVO10 mtInstruction = new MtInstructionVO10();
        mtInstruction.setSourceDocId(instructionDocId);
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);

        // update instruction status
        for (String instructionId : instructionIdList) {
            mtInstructionRepository.instructionCancel(tenantId, instructionId, eventRequestId);
        }
    }

    @Override
    public void instructionDocCancelVerify(Long tenantId, String instructionDocId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId", "【API:instructionDocCancelVerify】"));
        }

        MtInstructionDoc mtLogisticInstructionDoc = instructionDocPropertyGet(tenantId, instructionDocId);

        if (mtLogisticInstructionDoc == null) {
            throw new MtException("MT_INSTRUCTION_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0020", "INSTRUCTION", "【API:instructionDocCancelVerify】"));
        }

        boolean cancelFlag = STATUS_NEW.endsWith(mtLogisticInstructionDoc.getInstructionDocStatus())
                        || STATUS_RELEASED.equals(mtLogisticInstructionDoc.getInstructionDocStatus());

        if (!cancelFlag) {
            throw new MtException("MT_INSTRUCTION_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0020", "INSTRUCTION", "【API:instructionDocCancelVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionDocComplete(Long tenantId, String instructionDocId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionDocId", "【API:instructionDocComplete】"));
        }

        instructionDocCompleteVerify(tenantId, instructionDocId);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_DOC_STATUS_UPDATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // update doc status
        MtInstructionDocDTO2 updateVO = new MtInstructionDocDTO2();
        updateVO.setInstructionDocId(instructionDocId);
        updateVO.setInstructionDocStatus(STATUS_COMPLETED);
        updateVO.setEventId(eventId);
        instructionDocUpdate(tenantId, updateVO, "N");
    }

    @Override
    public void instructionDocCompleteVerify(Long tenantId, String instructionDocId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId", "【API:instructionDocCompleteVerify】"));
        }

        MtInstructionDoc mtLogisticInstructionDoc = instructionDocPropertyGet(tenantId, instructionDocId);

        if (mtLogisticInstructionDoc == null
                        || !(STATUS_RELEASED.equals(mtLogisticInstructionDoc.getInstructionDocStatus()))) {
            throw new MtException("MT_INSTRUCTION_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0008", "INSTRUCTION", "【API:instructionDocCompleteVerify】"));
        }

        MtInstructionVO10 mtInstruction = new MtInstructionVO10();
        mtInstruction.setSourceDocId(instructionDocId);
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);

        if (CollectionUtils.isNotEmpty(instructionIdList)) {
            List<MtInstruction> mtInstructions =
                            mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
            for (MtInstruction tmpMtInstruction : mtInstructions) {
                if (!(STATUS_COMPLETED.equals(tmpMtInstruction.getInstructionStatus())
                                || STATUS_CANCEL.equals(tmpMtInstruction.getInstructionStatus()))) {
                    throw new MtException("MT_INSTRUCTION_0021",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0021",
                                                    "INSTRUCTION", tmpMtInstruction.getInstructionId(),
                                                    "【API:instructionDocCompleteVerify】"));
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionDocCompletedCancel(Long tenantId, String instructionDocId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId", "【API:instructionDocCompletedCancel】"));
        }

        instructionDocCompletedCancelVerify(tenantId, instructionDocId);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(INSTRUCTION_DOC_STATUS_UPDATE_EVENT_TYPE_CODE);
        eventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // update doc status
        MtInstructionDocDTO2 updateVO = new MtInstructionDocDTO2();
        updateVO.setInstructionDocId(instructionDocId);
        updateVO.setInstructionDocStatus(STATUS_COMPLETED_CANCEL);
        updateVO.setEventId(eventId);
        instructionDocUpdate(tenantId, updateVO, "N");
    }

    @Override
    public void instructionDocCompletedCancelVerify(Long tenantId, String instructionDocId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId",
                                            "【API:instructionDocCompletedCancelVerify】"));
        }

        MtInstructionDoc mtLogisticInstructionDoc = instructionDocPropertyGet(tenantId, instructionDocId);

        if (mtLogisticInstructionDoc == null
                        || !(STATUS_COMPLETED.equals(mtLogisticInstructionDoc.getInstructionDocStatus()))) {
            throw new MtException("MT_INSTRUCTION_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0022", "INSTRUCTION", "【API:instructionDocCompletedCancelVerify】"));
        }
    }

    @Override
    public MtInstructionDocVO5 instructionDocLimitInstructionAndActualQuery(Long tenantId, String instructionDocId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId",
                                            "【API:instructionDocLimitInstructionAndActualQuery】"));
        }

        MtInstructionDoc mtInstructionDoc = instructionDocPropertyGet(tenantId, instructionDocId);
        if (null == mtInstructionDoc) {
            return null;
        }

        MtInstructionDocVO5 result = new MtInstructionDocVO5();
        BeanUtils.copyProperties(mtInstructionDoc, result);

        MtInstructionVO10 mtInstruction = new MtInstructionVO10();
        mtInstruction.setSourceDocId(instructionDocId);
        List<MtInstruction> mtInstructions = mtInstructionMapper.selectForEmptyString(tenantId, mtInstruction);

        if (CollectionUtils.isNotEmpty(mtInstructions)) {

            List<MtInstructionVO2> instructionMessageList = new ArrayList<MtInstructionVO2>();
            MtInstructionVO2 mtInstructionVO = null;
            List<String> fromParentLocatorIds = new ArrayList<String>();
            List<String> toParentLocatorIds = new ArrayList<String>();

            for (MtInstruction instruction : mtInstructions) {
                mtInstructionVO = new MtInstructionVO2();

                String fromParentLocatorId = null;
                String fromLocatorId = instruction.getFromLocatorId();
                if (StringUtils.isNotEmpty(fromLocatorId)) {
                    MtModLocator mtModLocator =
                                    this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId, fromLocatorId);
                    if (null != mtModLocator) {
                        String parentLocatorId = mtModLocator.getParentLocatorId();
                        if (StringUtils.isNotEmpty(parentLocatorId)) {
                            mtModLocator = this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId,
                                            parentLocatorId);
                            if (null != mtModLocator && "AREA".equals(mtModLocator.getLocatorCategory())) {
                                fromParentLocatorId = parentLocatorId;
                            }
                        }
                    }
                }

                String toParentLocatorId = null;
                String toLocatorId = instruction.getToLocatorId();
                if (StringUtils.isNotEmpty(toLocatorId)) {
                    MtModLocator mtModLocator =
                                    this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId, toLocatorId);
                    if (null != mtModLocator) {
                        String parentLocatorId = mtModLocator.getParentLocatorId();
                        if (StringUtils.isNotEmpty(parentLocatorId)) {
                            mtModLocator = this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId,
                                            parentLocatorId);
                            if (null != mtModLocator && "AREA".equals(mtModLocator.getLocatorCategory())) {
                                toParentLocatorId = parentLocatorId;
                            }
                        }
                    }
                }

                BeanUtils.copyProperties(instruction, mtInstructionVO);
                mtInstructionVO.setFromParentLocatorId(fromParentLocatorId);
                mtInstructionVO.setToParentLocatorId(toParentLocatorId);
                mtInstructionVO.setInstructionActualMessageList(
                                new ArrayList<MtInstructionVO2.InstructionActualMessage>());
                instructionMessageList.add(mtInstructionVO);

                if (null != fromParentLocatorId) {
                    fromParentLocatorIds.add(fromParentLocatorId);
                }
                if (null != toParentLocatorId) {
                    toParentLocatorIds.add(toParentLocatorId);
                }
            }

            if (CollectionUtils.isNotEmpty(fromParentLocatorIds)) {
                long distinctCount = fromParentLocatorIds.stream().distinct().count();
                if (1 == distinctCount) {
                    result.setFromParentLocatorId(fromParentLocatorIds.get(0));
                    instructionMessageList.stream().forEach(c -> c.setFromParentLocatorId(null));
                }
            }

            if (CollectionUtils.isNotEmpty(toParentLocatorIds)) {
                long distinctCount = toParentLocatorIds.stream().distinct().count();
                if (1 == distinctCount) {
                    result.setToParentLocatorId(toParentLocatorIds.get(0));
                    instructionMessageList.stream().forEach(c -> c.setToParentLocatorId(null));
                }
            }

            List<String> instructionIds =
                            mtInstructions.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
            List<MtInstructionActual> mtInstructionActuals =
                            this.mtInstructionActualRepository.instructionLimitActualBatchGet(tenantId, instructionIds);
            Map<String, List<MtInstructionActual>> groupMap = mtInstructionActuals.stream()
                            .collect(Collectors.groupingBy(MtInstructionActual::getInstructionId));

            for (Map.Entry<String, List<MtInstructionActual>> entry : groupMap.entrySet()) {
                BigDecimal sumQty = BigDecimal.ZERO;
                MtUomVO1 mtUomVO1 = null;

                Optional<MtInstruction> instructionOptional = mtInstructions.stream()
                                .filter(c -> c.getInstructionId().equals(entry.getKey())).findFirst();
                if (instructionOptional.isPresent()) {
                    MtInstruction instruction = instructionOptional.get();

                    MtInstructionVO2 vo = instructionMessageList.stream()
                                    .filter(c -> c.getInstructionId().equals(instruction.getInstructionId()))
                                    .findFirst().get();


                    List<MtInstructionVO2.InstructionActualMessage> instructionActualMessageList =
                                    vo.getInstructionActualMessageList();
                    MtInstructionVO2.InstructionActualMessage instructionActualMessage = null;

                    for (MtInstructionActual tmpMtInstructionActual : entry.getValue()) {
                        instructionActualMessage = new MtInstructionVO2.InstructionActualMessage();

                        mtUomVO1 = new MtUomVO1();
                        mtUomVO1.setSourceUomId(tmpMtInstructionActual.getUomId());
                        mtUomVO1.setSourceValue(tmpMtInstructionActual.getActualQty());
                        mtUomVO1.setTargetUomId(instruction.getUomId());
                        mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                        sumQty = sumQty.add(BigDecimal.valueOf(mtUomVO1.getTargetValue()));

                        instructionActualMessage.setActualId(tmpMtInstructionActual.getActualId());
                        instructionActualMessage.setActualQty(tmpMtInstructionActual.getActualQty());
                        instructionActualMessage.setFromLocatorId(tmpMtInstructionActual.getFromLocatorId());
                        instructionActualMessage.setSourceOrderId(tmpMtInstructionActual.getSourceOrderId());
                        instructionActualMessage.setSourceOrderType(tmpMtInstructionActual.getSourceOrderType());
                        instructionActualMessage.setToLocatorId(tmpMtInstructionActual.getToLocatorId());
                        instructionActualMessageList.add(instructionActualMessage);
                    }

                    BigDecimal executableQty = BigDecimal.valueOf(instruction.getQuantity()).subtract(sumQty);
                    vo.setExecutableQty(executableQty.doubleValue());
                }
            }

            result.setInstructionMessageList(instructionMessageList);
        }

        return result;
    }

}
