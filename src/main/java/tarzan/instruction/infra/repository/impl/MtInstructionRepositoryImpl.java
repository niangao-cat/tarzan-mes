package tarzan.instruction.infra.repository.impl;

import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtInstructionActualDetailMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.instruction.domain.entity.MtBusinessInstructionTypeR;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionHisRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.instruction.infra.mapper.MtBusinessInstructionTypeRMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO1;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;
import tarzan.modeling.domain.vo.MtModLocatorVO9;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓储物流指令内容表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@Component
public class MtInstructionRepositoryImpl extends BaseRepositoryImpl<MtInstruction> implements MtInstructionRepository {

    private static final String TYPE_RECEIVE_FROM_SUPPLIER = "RECEIVE_FROM_SUPPLIER";
    private static final String TYPE_RETURN_TO_SUPPLIER = "RETURN_TO_SUPPLIER";
    private static final String TYPE_SHIP_TO_CUSTOMER = "SHIP_TO_CUSTOMER";
    private static final String TYPE_RETURN_FROM_CUSTOMER = "RETURN_FROM_CUSTOMER";
    private static final String TYPE_TRANSFER_OVER_LOCATOR = "TRANSFER_OVER_LOCATOR";
    private static final String TYPE_TRANSFER_OVER_SITE = "TRANSFER_OVER_SITE";
    private static final String TYPE_SENT_FROM_SITE = "SENT_FROM_SITE";
    private static final String TYPE_RECEIVE_TO_SITE = "RECEIVE_TO_SITE";
    private static final String TYPE_SHIP_TO_MISCELLANEOUS = "SHIP_TO_MISCELLANEOUS";
    private static final String TYPE_RECEIVE_FROM_COSTCENTER = "RECEIVE_FROM_COSTCENTER";

    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_RELEASED = "RELEASED";
    private static final String STATUS_NEW = "NEW";
    private static final String STATUS_COMPLETED_CANCEL = "COMPLETED_CANCEL";
    private static final String STATUS_CANCEL = "CANCEL";

    private static final String INSTRUCTION_CREATE_EVENT_TYPE_CODE = "INSTRUCTION_CREATE";
    private static final String INSTRUCTION_STATUS_UPDATE_EVENT_TYPE_CODE = "INSTRUCTION_STATUS_UPDATE";
    private static final String INSTRUCTION_EXECUTE_BACK = "INSTRUCTION_EXECUTE_BACK";

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
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtInstructionHisRepository mtInstructionHisRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Autowired
    private MtInstructionActualDetailMapper mtInstructionActualDetailMapper;

    @Autowired
    private MtBusinessInstructionTypeRMapper mtBusinessInstructionTypeRMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtInstructionDetailRepository mtInstructionDetailRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private MtNumrangeObjectRepository mtNumrangeObjectRepository;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;


    @Override
    public List<String> propertyLimitInstructionQuery(Long tenantId, MtInstructionVO10 mtInstruction) {
        return mtInstructionMapper.selectForEmptyString(tenantId, mtInstruction).stream()
                        .map(MtInstruction::getInstructionId).collect(Collectors.toList());
    }

    @Override
    public MtInstruction instructionPropertyGet(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionPropertyGet】"));
        }

        MtInstruction instruction = new MtInstruction();
        instruction.setTenantId(tenantId);
        instruction.setInstructionId(instructionId);

        return mtInstructionMapper.selectOne(instruction);
    }

    @Override
    public List<MtInstruction> instructionPropertyBatchGet(Long tenantId, List<String> instructionIdList) {
        if (CollectionUtils.isEmpty(instructionIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocIdList",
                                            "【API：instructionPropertyBatchGet】"));
        }

        return mtInstructionMapper.selectByIdList(tenantId, instructionIdList);
    }

    @Override
    public String instructionNextNumGet(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "siteId", "【API：instructionNextNumGet】"));
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
        StringBuilder siteDateStr = new StringBuilder("INS").append(siteStr).append(dateStr);
        Integer maxNum = mtInstructionMapper.selectMaxNum(tenantId, siteDateStr.toString());

        if (maxNum == null) {
            return siteDateStr.append("0001").toString();
        } else {
            return siteDateStr.append(String.format("%04d", maxNum + 1)).toString();
        }
    }

    /**
     * 创建DOC前进行校验
     *
     * @param tenantId tenantId
     * @param createVO MtLogisticInstructionVO
     * @author benjamin
     * @date 2019-06-19 15:24
     */
    private void instructionUpdateVerify(Long tenantId, MtInstructionVO createVO) {
        if (StringUtils.isEmpty(createVO.getSiteId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "siteId", "【API:instructionUpdate】"));
        }
        if (createVO.getQuantity() == null) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "quantity", "【API:instructionUpdate】"));
        }
        if (StringUtils.isEmpty(createVO.getInstructionType()) && StringUtils.isEmpty(createVO.getBusinessType())) {
            throw new MtException("MT_INSTRUCTION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                            "INSTRUCTION", "instructionType,businseeType", "【API:instructionUpdate】"));
        }
        if (StringUtils.isNotEmpty(createVO.getInstructionType())
                        && StringUtils.isNotEmpty(createVO.getBusinessType())) {
            MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
            typeR.setTenantId(tenantId);
            typeR.setInstructionType(createVO.getInstructionType());
            typeR.setBussinessType(createVO.getBusinessType());
            typeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
            if (null == typeR) {
                throw new MtException("MT_INSTRUCTION_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0045",
                                                "INSTRUCTION", "instructionType,businseeType",
                                                "【API:instructionUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(createVO.getInstructionNum())) {
            MtInstructionVO10 queryIns = new MtInstructionVO10();
            queryIns.setInstructionNum(createVO.getInstructionNum());
            List<String> logisticInstructionNumList = propertyLimitInstructionQuery(tenantId, queryIns);
            if (CollectionUtils.isNotEmpty(logisticInstructionNumList)) {
                throw new MtException("MT_INSTRUCTION_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0002",
                                                "INSTRUCTION", "instructionNum", "【API:instructionUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(createVO.getIdentification())) {
            MtInstructionVO10 queryIns = new MtInstructionVO10();
            queryIns.setIdentification(createVO.getIdentification());
            List<String> logisticInstructionNumList = propertyLimitInstructionQuery(tenantId, queryIns);
            if (CollectionUtils.isNotEmpty(logisticInstructionNumList)) {
                throw new MtException("MT_INSTRUCTION_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0002",
                                                "INSTRUCTION", "identification", "【API:instructionUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(createVO.getInstructionStatus())) {
            MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
            mtGenStatusVO2.setModule("INSTRUCTION");
            mtGenStatusVO2.setStatusGroup("INSTRUCTION_STATUS");
            List<MtGenStatus> genStatusList = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);

            String status = "[]";

            if (CollectionUtils.isEmpty(genStatusList)) {
                throw new MtException("MT_INSTRUCTION_0044",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0044",
                                                "INSTRUCTION", createVO.getInstructionStatus(), status,
                                                "【API:instructionUpdate】"));
            }

            List<String> statusList =
                            genStatusList.stream().map(MtGenStatus::getStatusCode).collect(Collectors.toList());
            if (!statusList.contains(createVO.getInstructionStatus())) {
                status = statusList.toString();
                throw new MtException("MT_INSTRUCTION_0044",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0044",
                                                "INSTRUCTION", createVO.getInstructionStatus(), status,
                                                "【API:instructionUpdate】"));
            }
        }

        if (StringUtils.isEmpty(createVO.getMaterialId()) && StringUtils.isEmpty(createVO.getEoId())) {
            throw new MtException("MT_INSTRUCTION_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0003", "INSTRUCTION", "materialId,eoId", "【API:instructionUpdate】"));
        }

        doubleFieldsStringValueNullCheck(tenantId, "fromSiteId", createVO.getFromSiteId(), "toSiteId",
                        createVO.getToSiteId());

        doubleFieldsStringValueNullCheck(tenantId, "fromLocatorId", createVO.getFromLocatorId(), "toLocatorId",
                        createVO.getToLocatorId());

        MtGenTypeVO2 queryGenTypeVO = new MtGenTypeVO2();
        queryGenTypeVO.setModule("INSTRUCTION");
        queryGenTypeVO.setTypeGroup("INSTRUCTION_MOVE_TYPE");
        List<String> instructionTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryGenTypeVO).stream()
                        .map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
        if (!instructionTypeList.contains(createVO.getInstructionType())) {
            throw new MtException("MT_INSTRUCTION_0004",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0004",
                                            "INSTRUCTION", createVO.getInstructionType(),
                                            instructionTypeList.toString(), "【API:instructionUpdate】"));
        }

        doubleFieldsStringValueCheck(tenantId, "materialId", createVO.getMaterialId(), "uomId", createVO.getUomId());

        doubleFieldsStringValueCheck(tenantId, "orderType", createVO.getOrderType(), "orderId", createVO.getOrderId());

        doubleFieldsStringValueCheck(tenantId, "sourceOrderType", createVO.getSourceOrderType(), "sourceOrderId",
                        createVO.getSourceOrderId());

        if (StringUtils.isNotEmpty(createVO.getSupplierSiteId()) && StringUtils.isEmpty(createVO.getSupplierId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "supplierId", "【API:instructionUpdate】"));
        }

        doubleFieldsStringValueCheck(tenantId, "customerId", createVO.getCustomerId(), "customerSiteId",
                        createVO.getCustomerSiteId());

        if (StringUtils.isNotEmpty(createVO.getSourceDocId())) {
            if (mtInstructionDocRepository.instructionDocPropertyGet(tenantId, createVO.getSourceDocId()) == null) {
                throw new MtException("MT_INSTRUCTION_0033",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0033",
                                                "INSTRUCTION", createVO.getSourceDocId(), "【API:instructionUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(createVO.getSourceInstructionId())) {
            MtInstruction temp = new MtInstruction();
            temp.setTenantId(tenantId);
            temp.setInstructionId(createVO.getSourceInstructionId());
            if (mtInstructionMapper.selectOne(temp) == null) {
                throw new MtException("MT_INSTRUCTION_0034",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0034",
                                                "INSTRUCTION", createVO.getSourceInstructionId(),
                                                "【API:instructionUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(createVO.getEventId())) {
            throw new MtException("MT_INSTRUCTION_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0044", "INSTRUCTION", "eventId", "【API:instructionUpdate】"));
        }
    }

    /**
     * 依据指令类型进行校验
     *
     * @param tenantId tenantId
     * @param createVO MtLogisticInstructionVO
     * @author benjamin
     * @date 2019-06-19 18:28
     */
    private void instructionTypeVerify(Long tenantId, MtInstructionVO createVO) {
        Map<String, String> fieldsMap = new HashMap<>(5);
        List<String> instructionTypes = new ArrayList<>();
        if (StringUtils.isEmpty(createVO.getInstructionType()) && StringUtils.isEmpty(createVO.getBusinessType())) {
            throw new MtException("MT_INSTRUCTION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                            "INSTRUCTION", "instructionType", "businessType",
                                            "【API:instructionUpdate】"));
        }
        if (StringUtils.isNotEmpty(createVO.getInstructionType())
                        && StringUtils.isNotEmpty(createVO.getBusinessType())) {
            MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
            typeR.setTenantId(tenantId);
            typeR.setInstructionType(createVO.getInstructionType());
            typeR.setBussinessType(createVO.getBusinessType());
            typeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
            if (null == typeR) {
                throw new MtException("MT_INSTRUCTION_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0045",
                                                "INSTRUCTION", "instructionType", "businessType",
                                                "【API:instructionUpdate】"));
            }
            instructionTypes.add(createVO.getInstructionType());
        } else if (StringUtils.isNotEmpty(createVO.getInstructionType())
                        && StringUtils.isEmpty(createVO.getBusinessType())) {
            instructionTypes.add(createVO.getInstructionType());
        }
        if (StringUtils.isEmpty(createVO.getInstructionType()) && StringUtils.isNotEmpty(createVO.getBusinessType())) {
            MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
            typeR.setTenantId(tenantId);
            typeR.setBussinessType(createVO.getBusinessType());
            List<MtBusinessInstructionTypeR> typeRS = mtBusinessInstructionTypeRMapper.select(typeR);
            instructionTypes = typeRS.stream().map(MtBusinessInstructionTypeR::getInstructionType)
                            .collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(instructionTypes)) {
            for (String type : instructionTypes) {
                switch (type) {
                    case TYPE_RECEIVE_FROM_SUPPLIER:
                        fieldsMap.put("supplierId", createVO.getSupplierId());
                        fieldsMap.put("toLocatorId", createVO.getToLocatorId());
                        fieldsMap.put("toSiteId", createVO.getToSiteId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_RETURN_TO_SUPPLIER:
                        fieldsMap.put("supplierId", createVO.getSupplierId());
                        fieldsMap.put("fromLocatorId", createVO.getFromLocatorId());
                        fieldsMap.put("fromSiteId", createVO.getFromSiteId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_SHIP_TO_CUSTOMER:
                        fieldsMap.put("fromSiteId", createVO.getFromSiteId());
                        fieldsMap.put("fromLocatorId", createVO.getFromLocatorId());
                        fieldsMap.put("customerId", createVO.getCustomerId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_RETURN_FROM_CUSTOMER:
                        fieldsMap.put("toSiteId", createVO.getToSiteId());
                        fieldsMap.put("toLocatorId", createVO.getToLocatorId());
                        fieldsMap.put("customerId", createVO.getCustomerId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_TRANSFER_OVER_LOCATOR:
                        fieldsMap.put("fromSiteId", createVO.getFromSiteId());
                        fieldsMap.put("fromLocatorId", createVO.getFromLocatorId());
                        fieldsMap.put("toLocatorId", createVO.getToLocatorId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_TRANSFER_OVER_SITE:
                        fieldsMap.put("fromSiteId", createVO.getFromSiteId());
                        fieldsMap.put("fromLocatorId", createVO.getFromLocatorId());
                        fieldsMap.put("toSiteId", createVO.getToSiteId());
                        fieldsMap.put("toLocatorId", createVO.getToLocatorId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        if (createVO.getFromSiteId().equals(createVO.getToSiteId())) {
                            throw new MtException("MT_INSTRUCTION_0005",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_INSTRUCTION_0005", "INSTRUCTION", "fromSiteId",
                                                            "toSiteId", "【API:instructionUpdate】"));
                        }
                        break;
                    case TYPE_SENT_FROM_SITE:
                        fieldsMap.put("fromSiteId", createVO.getFromSiteId());
                        fieldsMap.put("fromLocatorId", createVO.getFromLocatorId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_RECEIVE_TO_SITE:
                        fieldsMap.put("toSiteId", createVO.getToSiteId());
                        fieldsMap.put("toLocatorId", createVO.getToLocatorId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_SHIP_TO_MISCELLANEOUS:
                        fieldsMap.put("fromSiteId", createVO.getFromSiteId());
                        fieldsMap.put("fromLocatorId", createVO.getFromLocatorId());
                        fieldsMap.put("costCenterId", createVO.getCostCenterId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    case TYPE_RECEIVE_FROM_COSTCENTER:
                        fieldsMap.put("toSiteId", createVO.getToSiteId());
                        fieldsMap.put("toLocatorId", createVO.getToLocatorId());
                        fieldsMap.put("costCenterId", createVO.getCostCenterId());
                        fieldsStringValueNullCheck(tenantId, fieldsMap);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 校验两个String类型成员变量方法 两个变量同时为空时，抛出异常
     *
     * @param tenantId tenantId
     * @param firstFieldName 前一个字段名
     * @param firstFieldValue 前一个字段值
     * @param lastFieldName 后一个字段名
     * @param lastFieldValue 后一个字段值
     * @author benjamin
     * @date 2019-06-19 16:02
     */
    private void doubleFieldsStringValueNullCheck(Long tenantId, String firstFieldName, String firstFieldValue,
                    String lastFieldName, String lastFieldValue) {
        boolean emptyCheck = StringUtils.isEmpty(firstFieldValue) && StringUtils.isEmpty(lastFieldValue);
        if (emptyCheck) {
            throw new MtException("MT_INSTRUCTION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                            "INSTRUCTION", firstFieldName, lastFieldName, "【API:instructionUpdate】"));
        }
    }

    /**
     * 校验两个String类型成员变量方法 两个变量不满足同时为空或同时非空时，抛出异常
     *
     * @param tenantId tenantId
     * @param firstFieldName 前一个字段名
     * @param firstFieldValue 前一个字段值
     * @param lastFieldName 后一个字段名
     * @param lastFieldValue 后一个字段值
     * @author benjamin
     * @date 2019-06-19 16:02
     */
    private void doubleFieldsStringValueCheck(Long tenantId, String firstFieldName, String firstFieldValue,
                    String lastFieldName, String lastFieldValue) {
        boolean emptyCheck = StringUtils.isEmpty(firstFieldValue) && StringUtils.isEmpty(lastFieldValue);
        boolean notEmptyCheck = StringUtils.isNotEmpty(firstFieldValue) && StringUtils.isNotEmpty(lastFieldValue);
        if (!(emptyCheck || notEmptyCheck)) {
            throw new MtException("MT_INSTRUCTION_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0007",
                                            "INSTRUCTION", firstFieldName, lastFieldName, "【API:instructionUpdate】"));
        }
    }

    /**
     * 校验成员变量是否为空
     *
     * @param tenantId tenantId
     * @param fieldsMap Map
     * @author benjamin
     * @date 2019-06-19 18:27
     */
    private void fieldsStringValueNullCheck(Long tenantId, Map<String, String> fieldsMap) {
        for (Map.Entry<String, String> item : fieldsMap.entrySet()) {
            if (StringUtils.isEmpty(item.getValue())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", item.getKey(), "【API:instructionUpdate】"));
            }
        }
    }

    /**
     * 创建Instruction对象
     *
     * @param createVO MtLogisticInstructionVO
     * @return MtLogisticInstruction
     * @author benjamin
     * @date 2019-06-19 17:54
     */
    private MtInstruction createInstruction(MtInstructionVO createVO) {
        String instructionNum = StringUtils.isEmpty(createVO.getInstructionNum()) ? "" : createVO.getInstructionNum();
        String identification = StringUtils.isEmpty(createVO.getIdentification()) ? "" : createVO.getIdentification();

        MtInstruction mtInstruction = new MtInstruction();
        mtInstruction.setInstructionNum(instructionNum);
        mtInstruction.setSourceInstructionId(createVO.getSourceInstructionId());
        mtInstruction.setSourceDocId(createVO.getSourceDocId());
        mtInstruction.setInstructionType(createVO.getInstructionType());

        if (StringUtils.isEmpty(createVO.getInstructionStatus())) {
            mtInstruction.setInstructionStatus(STATUS_NEW);
        } else {
            mtInstruction.setInstructionStatus(createVO.getInstructionStatus());
        }

        mtInstruction.setSiteId(createVO.getSiteId());
        mtInstruction.setMaterialId(createVO.getMaterialId());
        mtInstruction.setUomId(createVO.getUomId());
        mtInstruction.setEoId(createVO.getEoId());
        mtInstruction.setDisRouterId(createVO.getDisRouterId());
        mtInstruction.setOrderType(createVO.getOrderType());
        mtInstruction.setOrderId(createVO.getOrderId());
        mtInstruction.setSourceOrderType(createVO.getSourceOrderType());
        mtInstruction.setSourceOrderId(createVO.getSourceOrderId());
        mtInstruction.setSourceOrderLineId(createVO.getSourceOrderLineId());
        mtInstruction.setSourceOrderLineLocationId(createVO.getSourceOrderLineLocationId());
        mtInstruction.setSourceOrderLineDistId(createVO.getSourceOrderLineDistributionId());
        mtInstruction.setSourceOutsideCompLineId(createVO.getSourceOutsideComponentLineId());
        mtInstruction.setFromSiteId(createVO.getFromSiteId());
        mtInstruction.setToSiteId(createVO.getToSiteId());
        mtInstruction.setFromLocatorId(createVO.getFromLocatorId());
        mtInstruction.setToLocatorId(createVO.getToLocatorId());
        mtInstruction.setCostCenterId(createVO.getCostCenterId());
        mtInstruction.setQuantity(createVO.getQuantity());
        mtInstruction.setSupplierId(createVO.getSupplierId());
        mtInstruction.setSupplierSiteId(createVO.getSupplierSiteId());
        mtInstruction.setCustomerId(createVO.getCustomerId());
        mtInstruction.setCustomerSiteId(createVO.getCustomerSiteId());
        mtInstruction.setDemandTime(createVO.getDemandTime());
        mtInstruction.setWaveSequence(createVO.getWaveSequence());

        mtInstruction.setShiftCode(createVO.getShiftCode());
        mtInstruction.setShiftDate(createVO.getShiftDate());
        mtInstruction.setCoverQty(createVO.getCoverQty());
        mtInstruction.setBusinessType(createVO.getBusinessType());
        mtInstruction.setRemark(createVO.getRemark());
        mtInstruction.setIdentification(identification);
        mtInstruction.setFromOwnerType(createVO.getFromOwnerType());
        mtInstruction.setToOwnerType(createVO.getToOwnerType());
        return mtInstruction;
    }

    /**
     * 构建号码段对象
     *
     * @param createVO MtInstructionVO
     * @param objectCode 对象编码
     * @param incomingValueList 标识参数列表
     * @param outsideNum 原有值
     * @return MtNumrangeVO2
     * @author benjamin
     * @date 2019-07-18 12:42
     */
    private MtNumrangeVO2 constructNumRange(Long tenantId, MtInstructionVO createVO, String objectCode,
                    List<String> incomingValueList, String outsideNum) {
        MtNumrangeVO2 createNum = new MtNumrangeVO2();
        createNum.setObjectCode(objectCode);
        createNum.setSiteId(createVO.getSiteId());

        String typeCode = null;
        String objectColumnCode = null;
        Map<String, String> callObjectCodeMap = new HashMap<>(0);

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

        for (MtNumrangeObjectColumn column : mtNumrangeObjectColumnList) {
            if (StringUtils.isEmpty(column.getObjectColumnCode())) {
                continue;
            }
            switch (column.getObjectColumnCode()) {
                case "siteCode":
                    MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, createVO.getSiteId());
                    callObjectCodeMap.put("siteCode", mtModSite == null ? null : mtModSite.getSiteCode());
                    break;
                case "materialCode":
                    MtMaterialBasic mtMaterialBasic = mtMaterialBasisRepository.materialBasicPropertyGet(tenantId,
                            createVO.getSiteId(), createVO.getMaterialId());
                    callObjectCodeMap.put("materialCode",
                            mtMaterialBasic == null ? null : mtMaterialBasic.getMakeBuyCode());
                    break;
                case "orderType":
                    callObjectCodeMap.put("orderType", createVO.getOrderType());
                    break;
                case "eoNum":
                    List<MtEo> mtEos =
                            mtEoRepository.eoPropertyBatchGet(tenantId, Arrays.asList(createVO.getEoId()));
                    String eoNum = null;
                    if (CollectionUtils.isNotEmpty(mtEos)) {
                        eoNum = mtEos.get(0).getEoNum();
                    }
                    callObjectCodeMap.put("eoNum", eoNum);
                    break;
                case "instructionType":
                    callObjectCodeMap.put("instructionType", createVO.getInstructionType());
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
            createNum.setIncomingValueList(CollectionUtils.isEmpty(incomingValueList) ? null : incomingValueList);
        }
        if (StringUtils.isNotEmpty(typeCode)) {
            createNum.setObjectTypeCode(callObjectCodeMap.get(objectColumnCode));
        }
        createNum.setCallObjectCodeList(callObjectCodeMap);
        return createNum;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtInstructionVO6 instructionUpdate(Long tenantId, MtInstructionVO dto, String fullUpdate) {
        MtInstructionVO6 result = new MtInstructionVO6();
        MtInstruction mtInstruction;
        String instructionHisId;
        String warningMessage = null;
        // 若输入参数instructionId为空，则执行指令新增逻辑；instructionId不为空，则执行指令更新逻辑
        if (StringUtils.isEmpty(dto.getInstructionId())) {
            // 新增逻辑
            // verify before creating
            instructionUpdateVerify(tenantId, dto);
            // base on instruction type to verify
            instructionTypeVerify(tenantId, dto);

            // construction object
            MtNumrangeVO2 createNum = constructNumRange(tenantId, dto, "INSTRUCTION_NUM", dto.getNumIncomingValueList(),
                            dto.getInstructionNum());
            MtNumrangeVO5 generateNum = mtNumrangeRepository.numrangeGenerate(tenantId, createNum);
            // 文档4. b) 3.
            dto.setInstructionNum(generateNum.getNumber());
            dto.setIdentification(StringUtils.isEmpty(dto.getIdentification()) ? generateNum.getNumber()
                            : dto.getIdentification());
            warningMessage = generateNum.getWarningMessage();
            mtInstruction = createInstruction(dto);
            mtInstruction.setTenantId(tenantId);

            List<MtInstruction> originList = mtInstructionMapper.selectByUnique(tenantId, mtInstruction);
            if (CollectionUtils.isEmpty(originList)) {
                self().insertSelective(mtInstruction);
            } else {
                final String checkNum = mtInstruction.getInstructionNum();
                String errorFieldName = originList.stream().anyMatch(o -> checkNum.equals(o.getInstructionNum()))
                                ? "instructionNum: " + mtInstruction.getInstructionNum()
                                : "identification: " + mtInstruction.getIdentification();
                throw new MtException("MT_INSTRUCTION_0037",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0037",
                                                "INSTRUCTION", errorFieldName, "【API:instructionUpdate】"));
            }

            // create event
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode(INSTRUCTION_CREATE_EVENT_TYPE_CODE);
            eventCreateVO.setParentEventId(dto.getParentEventId());
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // save history
            instructionHisId =
                            mtInstructionHisRepository.saveLogisticInstructionHistory(tenantId, mtInstruction, eventId);

            mtInstruction.setLatestHisId(instructionHisId);
            self().updateByPrimaryKeySelective(mtInstruction);

        } else {
            // 更新逻辑
            if (StringUtils.isEmpty(dto.getInstructionId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "instructionId", "【API:instructionUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEventId())) {
                throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0001", "INSTRUCTION", "eventId", "【API:instructionUpdate】"));
            }

            mtInstruction = instructionPropertyGet(tenantId, dto.getInstructionId());
            if (mtInstruction == null) {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", "instructionId", "【API:instructionUpdate】"));
            }
            if (StringUtils.isNotEmpty(dto.getInstructionType())) {
                throw new MtException("MT_INSTRUCTION_0044",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0044",
                                                "INSTRUCTION", "instructionType", "【API:instructionUpdate】"));
            }
            String instructionType = mtInstruction.getInstructionType();
            if (StringUtils.isNotEmpty(dto.getBusinessType())) {
                MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
                typeR.setTenantId(tenantId);
                typeR.setInstructionType(instructionType);
                typeR.setBussinessType(dto.getBusinessType());
                typeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
                if (null == typeR) {
                    throw new MtException("MT_INSTRUCTION_0045",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0045",
                                                    "INSTRUCTION", "instructionType", "businessType",
                                                    "【API:instructionUpdate】"));
                }
            }
            switch (instructionType) {
                case "RECEIVE_FROM_SUPPLIER":
                    if (null != dto.getSupplierId() && "".equals(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "supplierId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToLocatorId() && "".equals(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToSiteId() && "".equals(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionUpdate】"));

                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "fromSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "fromLocatorId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerSiteId", "【API:instructionUpdate】"));
                    }
                    break;
                case "RETURN_TO_SUPPLIER":
                    if (null != dto.getSupplierId() && "".equals(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "supplierId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getSupplierSiteId() && "".equals(dto.getSupplierSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "supplierSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromLocatorId() && "".equals(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromSiteId() && "".equals(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "toSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "ToLocatorId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerSiteId", "【API:instructionUpdate】"));
                    }
                    break;
                case "SHIP_TO_CUSTOMER":
                    if (null != dto.getCustomerSiteId() && "".equals(dto.getCustomerSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "customerSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getSourceOrderId() && "".equals(dto.getSourceOrderId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "sourceOrderId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromSiteId() && "".equals(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromLocatorId() && "".equals(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getCustomerId() && "".equals(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "customerId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getSourceOrderType() && "".equals(dto.getSourceOrderType())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "sourceOrderType",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "toSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "toLocatorId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierId", "【API:instructionUpdate】"));
                    }
                    break;
                case "RETURN_FROM_CUSTOMER":
                    if (null != dto.getCustomerSiteId() && "".equals(dto.getCustomerSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "customerSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getSourceOrderType() && "".equals(dto.getSourceOrderType())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "sourceOrderType",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getSourceOrderId() && "".equals(dto.getSourceOrderId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "sourceOrderId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToSiteId() && "".equals(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToLocatorId() && "".equals(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getCustomerId() && "".equals(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "customerId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "FromSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "fromLocatorId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierSiteId", "【API:instructionUpdate】"));
                    }
                    break;
                case "TRANSFER_OVER_LOCATOR":
                    if (null != dto.getFromSiteId() && "".equals(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromLocatorId() && "".equals(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToLocatorId() && "".equals(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    break;
                case "TRANSFER_OVER_SITE":
                    if (null != dto.getFromSiteId() && "".equals(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromLocatorId() && "".equals(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToLocatorId() && "".equals(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerSiteId", "【API:instructionUpdate】"));
                    }
                    break;
                case "SENT_FROM_SITE":
                    if (null != dto.getFromSiteId() && "".equals(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromLocatorId() && "".equals(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "toSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "toLocatorId", "【API:instructionUpdate】"));
                    }
                    break;
                case "RECEIVE_TO_SITE":
                    if (null != dto.getToSiteId() && "".equals(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToLocatorId() && "".equals(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "costCenterId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "fromSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "fromLocatorId", "【API:instructionUpdate】"));
                    }
                    break;
                case "SHIP_TO_MISCELLANEOUS":
                    if (null != dto.getFromSiteId() && "".equals(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getFromLocatorId() && "".equals(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getCostCenterId() && "".equals(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "costCenterId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "toSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "toLocatorId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerSiteId", "【API:instructionUpdate】"));
                    }
                    break;
                case "RECEIVE_FROM_COSTCENTER":
                    if (null != dto.getToSiteId() && "".equals(dto.getToSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toSiteId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getToLocatorId() && "".equals(dto.getToLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (null != dto.getCostCenterId() && "".equals(dto.getCostCenterId())) {
                        throw new MtException("MT_INSTRUCTION_0041",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0041", "INSTRUCTION", "costCenterId",
                                                        "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getFromSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "FromSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getFromLocatorId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "fromLocatorId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getSupplierSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "supplierSiteId", "【API:instructionUpdate】"));
                    }
                    if (StringUtils.isNotEmpty(dto.getCustomerSiteId())) {
                        throw new MtException("MT_INSTRUCTION_0012",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0012", "INSTRUCTION", instructionType,
                                                        "customerSiteId", "【API:instructionUpdate】"));
                    }
                    break;
                default:
                    break;
            }

            // update fields
            mtInstruction.setSourceDocId(dto.getSourceDocId());
            mtInstruction.setSourceInstructionId(dto.getSourceInstructionId());
            mtInstruction.setInstructionStatus(dto.getInstructionStatus());
            mtInstruction.setSiteId(dto.getSiteId());
            mtInstruction.setMaterialId(dto.getMaterialId());
            mtInstruction.setUomId(dto.getUomId());
            mtInstruction.setEoId(dto.getEoId());
            mtInstruction.setDisRouterId(dto.getDisRouterId());
            mtInstruction.setOrderId(dto.getOrderId());
            mtInstruction.setOrderType(dto.getOrderType());
            mtInstruction.setSourceOrderId(dto.getSourceOrderId());
            mtInstruction.setSourceOrderType(dto.getSourceOrderType());
            mtInstruction.setSourceOrderLineId(dto.getSourceOrderLineId());
            mtInstruction.setSourceOrderLineDistId(dto.getSourceOrderLineDistributionId());
            mtInstruction.setSourceOrderLineLocationId(dto.getSourceOrderLineLocationId());
            mtInstruction.setSourceOutsideCompLineId(dto.getSourceOutsideComponentLineId());
            mtInstruction.setFromSiteId(dto.getFromSiteId());
            mtInstruction.setToSiteId(dto.getToSiteId());
            mtInstruction.setFromLocatorId(dto.getFromLocatorId());
            mtInstruction.setToLocatorId(dto.getToLocatorId());
            mtInstruction.setCostCenterId(dto.getCostCenterId());
            mtInstruction.setSupplierId(dto.getSupplierId());
            mtInstruction.setSupplierSiteId(dto.getSupplierSiteId());
            mtInstruction.setCustomerId(dto.getCustomerId());
            mtInstruction.setCustomerSiteId(dto.getCustomerSiteId());
            mtInstruction.setDemandTime(dto.getDemandTime());
            mtInstruction.setWaveSequence(dto.getWaveSequence());
            mtInstruction.setShiftCode(dto.getShiftCode());
            mtInstruction.setShiftDate(dto.getShiftDate());
            mtInstruction.setBusinessType(dto.getBusinessType());
            mtInstruction.setRemark(dto.getRemark());
            mtInstruction.setFromOwnerType(dto.getFromOwnerType());
            mtInstruction.setToOwnerType(dto.getToOwnerType());
            mtInstruction.setTenantId(tenantId);
            if (null != dto.getQuantity()) {
                mtInstruction.setQuantity(dto.getQuantity());
            }

            if (null != dto.getCoverQty()) {
                mtInstruction.setCoverQty(dto.getCoverQty());
            }

            if ("Y".equalsIgnoreCase(fullUpdate)) {
                if (dto.getQuantity() == null) {
                    mtInstruction.setQuantity(0.0D);
                }
                if (dto.getCoverQty() == null) {
                    mtInstruction.setCoverQty(null);
                }
            } else {
                if (dto.getQuantity() == null) {
                    mtInstruction.setQuantity(mtInstruction.getQuantity());
                }
                if (dto.getCoverQty() == null) {
                    mtInstruction.setCoverQty(mtInstruction.getCoverQty());
                }
            }
            // save history return instructionHisId
            instructionHisId = mtInstructionHisRepository.saveLogisticInstructionHistory(tenantId, mtInstruction,
                            dto.getEventId());

            mtInstruction.setLatestHisId(instructionHisId);
            // 是否全变量修改
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtInstruction = (MtInstruction) ObjectFieldsHelper.setStringFieldsEmpty(mtInstruction);
                self().updateByPrimaryKey(mtInstruction);
            } else {
                self().updateByPrimaryKeySelective(mtInstruction);
            }
        }
        String instructionId = mtInstruction.getInstructionId();
        // result
        result.setInstructionId(instructionId);
        result.setInstructionHisId(instructionHisId);
        result.setWarningMessage(warningMessage);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtInstructionVO4 instructionExecute(Long tenantId, MtInstructionVO3 executeVO) {
        if (StringUtils.isEmpty(executeVO.getInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionExecute】"));
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotMessageList())) {
            for (MtInstructionVO3.MaterialLotList materialLotList : executeVO.getMaterialLotMessageList()) {
                if (StringUtils.isEmpty(materialLotList.getMaterialLotId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "materialLotId", "【API:instructionExecute】"));
                }
                if (null == materialLotList.getQty()) {
                    throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_INSTRUCTION_0001", "INSTRUCTION", "qty", "【API:instructionExecute】"));
                }
                if (materialLotList.getQty() <= 0) {
                    throw new MtException("MT_INSTRUCTION_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_INSTRUCTION_0035", "INSTRUCTION", "qty", "【API:instructionExecute】"));
                }
                if (StringUtils.isEmpty(materialLotList.getUomId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "uomId", "【API:instructionExecute】"));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())) {
            for (MtInstructionVO3.MaterialMessageList materialMessageList : executeVO.getMaterialMessageList()) {
                if (StringUtils.isEmpty(materialMessageList.getMaterialId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "materialId", "【API:instructionExecute】"));
                }
                if (null == materialMessageList.getQty()) {
                    throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_INSTRUCTION_0001", "INSTRUCTION", "qty", "【API:instructionExecute】"));
                }
                if (materialMessageList.getQty() <= 0) {
                    throw new MtException("MT_INSTRUCTION_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_INSTRUCTION_0035", "INSTRUCTION", "qty", "【API:instructionExecute】"));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(executeVO.getContainerMessageList())) {
            for (MtInstructionVO3.ContainerMessageList containerMessageList : executeVO.getContainerMessageList()) {
                if (StringUtils.isEmpty(containerMessageList.getContainerId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "containerId", "【API:instructionExecute】"));
                }
            }
        }

        MtInstruction curInstruction = instructionPropertyGet(tenantId, executeVO.getInstructionId());
        if (null == curInstruction) {
            throw new MtException("MT_INSTRUCTION_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                            "INSTRUCTION", executeVO.getInstructionId(), "【API:instructionExecute】"));
        }

        if (StringUtils.isEmpty(curInstruction.getEoId())) {
            if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())
                            && CollectionUtils.isNotEmpty(executeVO.getMaterialLotMessageList())) {
                throw new MtException("MT_INSTRUCTION_0039",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                                "INSTRUCTION", "materialMessageList", "materialLotMessageList",
                                                "【API:instructionExecute】"));
            }

            if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())
                            && CollectionUtils.isNotEmpty(executeVO.getContainerMessageList())) {
                throw new MtException("MT_INSTRUCTION_0039",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                                "INSTRUCTION", "materialMessageList", "containerMessageList",
                                                "【API:instructionExecute】"));
            }

            if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotMessageList())
                            && CollectionUtils.isNotEmpty(executeVO.getContainerMessageList())) {
                throw new MtException("MT_INSTRUCTION_0039",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                                "INSTRUCTION", "materialLotMessageList", "containerMessageList",
                                                "【API:instructionExecute】"));
            }

            BigDecimal actualQty = BigDecimal.ZERO;
            List<String> actualIds = new ArrayList<String>();
            List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository
                            .instructionLimitActualPropertyGet(tenantId, executeVO.getInstructionId());
            if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                actualQty = mtInstructionActuals.stream().map(MtInstructionActual::getActualQty).collect(
                                CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(null == c ? 0.0D : c)));
                actualIds = mtInstructionActuals.stream().map(MtInstructionActual::getActualId)
                                .collect(Collectors.toList());
            }

            List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository
                            .instructionLimitActualDetailQuery(tenantId, executeVO.getInstructionId());
            List<String> actualDetailIds = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(mtInstructionActualDetails)) {
                actualDetailIds = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getActualDetailId)
                                .collect(Collectors.toList());
            }

            if ((CollectionUtils.isEmpty(actualIds) && CollectionUtils.isEmpty(actualDetailIds))
                            || (CollectionUtils.isNotEmpty(actualIds) && actualQty.compareTo(BigDecimal.ZERO) == 0)) {
                if (CollectionUtils.isEmpty(executeVO.getMaterialMessageList())
                                && CollectionUtils.isEmpty(executeVO.getMaterialLotMessageList())
                                && CollectionUtils.isEmpty(executeVO.getContainerMessageList())) {
                    throw new MtException("MT_INSTRUCTION_0046",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0046",
                                                    "INSTRUCTION", "materialMessageList", "materialLotMessageList",
                                                    "containerMessageList", "【API:instructionExecute】"));
                }
            }

            if (CollectionUtils.isNotEmpty(actualIds) && CollectionUtils.isEmpty(actualDetailIds)
                            && actualQty.compareTo(BigDecimal.ZERO) > 0) {
                if (CollectionUtils.isEmpty(executeVO.getMaterialMessageList())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "materialMessageList", "【API:instructionExecute】"));
                }
            }

            if (CollectionUtils.isNotEmpty(actualIds) && CollectionUtils.isNotEmpty(actualDetailIds)) {

                if (CollectionUtils.isEmpty(executeVO.getMaterialLotMessageList())
                                && CollectionUtils.isEmpty(executeVO.getContainerMessageList())) {
                    throw new MtException("MT_INSTRUCTION_0003",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                    "INSTRUCTION", "materialLotMessageList", "containerMessageList",
                                                    "【API:instructionExecute】"));
                }

                if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotMessageList())
                                && CollectionUtils.isNotEmpty(executeVO.getContainerMessageList())) {
                    throw new MtException("MT_INSTRUCTION_0039",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                                    "INSTRUCTION", "materialLotMessageList", "containerMessageList",
                                                    "【API:instructionExecute】"));
                }
            }
        }

        MtInstructionVO4 returnVO = new MtInstructionVO4();
        String eventId = createExecuteEventByType(tenantId, curInstruction.getInstructionType(),
                        executeVO.getEventRequestId());

        if (StringUtils.isEmpty(curInstruction.getEoId())) {
            // step 7 - solve material (with step 8/9)
            Map<String, List<String>> resultMap = solveMaterial(tenantId, executeVO, curInstruction, eventId);
            returnVO.setActualList(resultMap);
        } else {
            // step 12 - solve eo (with step 13/14)
            String actualId = solveEo(tenantId, curInstruction, eventId);
            Map<String, List<String>> resultMap = new HashMap<String, List<String>>(0);
            resultMap.put(actualId, new ArrayList<String>());
            returnVO.setActualList(resultMap);
        }

        returnVO.setEventId(eventId);
        return returnVO;
    }

    /**
     * step5 - 根据指令类型创建对应的事件
     *
     * @param tenantId tenantId
     * @param instructionType 指令类型
     * @param eventRequestId 事件组Id
     * @return String
     * @author benjamin
     * @date 2019-06-20 16:23
     */
    private String createExecuteEventByType(Long tenantId, String instructionType, String eventRequestId) {
        String eventTypeCode = null;
        switch (instructionType) {
            case TYPE_RECEIVE_FROM_SUPPLIER:
                eventTypeCode = "RECEIVE_FROM_SUPPLIER";
                break;
            case TYPE_RETURN_TO_SUPPLIER:
                eventTypeCode = "RETURN_TO_SUPPLIER";
                break;
            case TYPE_SHIP_TO_CUSTOMER:
                eventTypeCode = "SHIP_TO_CUSTOMER";
                break;
            case TYPE_RETURN_FROM_CUSTOMER:
                eventTypeCode = "RETURN_FROM_CUSTOMER";
                break;
            case TYPE_TRANSFER_OVER_LOCATOR:
                eventTypeCode = "TRANSFER_OVER_LOCATOR";
                break;
            case TYPE_TRANSFER_OVER_SITE:
                eventTypeCode = "TRANSFER_OVER_SITE";
                break;
            case TYPE_SENT_FROM_SITE:
                eventTypeCode = "SENT_FROM_SITE";
                break;
            case TYPE_RECEIVE_TO_SITE:
                eventTypeCode = "RECEIVE_TO_SITE";
                break;
            case TYPE_SHIP_TO_MISCELLANEOUS:
                eventTypeCode = "SHIP_TO_MISCELLANEOUS";
                break;
            case TYPE_RECEIVE_FROM_COSTCENTER:
                eventTypeCode = "RECEIVE_FROM_COSTCENTER";
                break;
            default:
                break;
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setEventRequestId(eventRequestId);

        return mtEventRepository.eventCreate(tenantId, eventCreateVO);
    }

    /**
     * step7 - 根据物料创建实绩 返回实绩Id
     * <p>
     * 包含step 8/9/10/11
     *
     * @param tenantId tenantId
     * @param executeVO MtLogisticInstructionVO3
     * @param mtInstruction MtLogisticInstruction
     * @param eventId 事件Id
     * @param executeVO 子事件Id
     * @return String
     * @author benjamin
     * @date 2019-06-20 16:27
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, List<String>> solveMaterial(Long tenantId, MtInstructionVO3 executeVO,
                    MtInstruction mtInstruction, String eventId) {
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtInstruction.getMaterialId());
        if (null == mtMaterialVO || StringUtils.isEmpty(mtMaterialVO.getPrimaryUomId())) {
            throw new MtException("MT_INSTRUCTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0013", "INSTRUCTION", "【API:instructionExecute】"));
        }

        Map<String, List<String>> resultMap = new HashMap<String, List<String>>(0);
        final List<MtInstructionVO3.MaterialLotNewList> materialLotNewLists = new ArrayList<>();
        Map<String, BigDecimal> actualQty = new HashMap<String, BigDecimal>(0);

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotMessageList())) {

            executeVO.getMaterialLotMessageList().forEach(c -> {
                if (null == c.getFromLocatorId()) {
                    c.setFromLocatorId("");
                }
                if (null == c.getToLocatorId()) {
                    c.setToLocatorId("");
                }
            });

            Map<String, List<MtInstructionVO3.MaterialLotList>> groupMap = executeVO.getMaterialLotMessageList()
                            .stream()
                            .collect(Collectors.groupingBy(c -> c.getFromLocatorId() + "::" + c.getToLocatorId()));

            for (Map.Entry<String, List<MtInstructionVO3.MaterialLotList>> entry : groupMap.entrySet()) {
                BigDecimal sumQty = BigDecimal.ZERO;
                MtUomVO1 mtUomVO1 = null;

                for (MtInstructionVO3.MaterialLotList materialLot : entry.getValue()) {
                    mtUomVO1 = new MtUomVO1();
                    mtUomVO1.setSourceUomId(materialLot.getUomId());
                    mtUomVO1.setSourceValue(materialLot.getQty());
                    mtUomVO1.setTargetUomId(mtMaterialVO.getPrimaryUomId());
                    mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                    sumQty = sumQty.add(BigDecimal.valueOf(mtUomVO1.getTargetValue()));
                }

                if (sumQty.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new MtException("MT_INSTRUCTION_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_INSTRUCTION_0035", "INSTRUCTION", "【API:instructionExecute】"));
                }
                actualQty.put(entry.getKey(), sumQty);
            }
        } else {
            if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())) {

                executeVO.getMaterialMessageList().forEach(c -> {
                    if (null == c.getFromLocatorId()) {
                        c.setFromLocatorId("");
                    }
                    if (null == c.getToLocatorId()) {
                        c.setToLocatorId("");
                    }
                });

                Map<String, List<MtInstructionVO3.MaterialMessageList>> groupMap = executeVO.getMaterialMessageList()
                                .stream()
                                .collect(Collectors.groupingBy(c -> c.getFromLocatorId() + "::" + c.getToLocatorId()));

                for (Map.Entry<String, List<MtInstructionVO3.MaterialMessageList>> entry : groupMap.entrySet()) {
                    BigDecimal sumQty = BigDecimal.ZERO;

                    for (MtInstructionVO3.MaterialMessageList materialMessageList : entry.getValue()) {
                        sumQty = sumQty.add(BigDecimal.valueOf(materialMessageList.getQty()));
                    }

                    if (sumQty.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new MtException("MT_INSTRUCTION_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_INSTRUCTION_0035", "INSTRUCTION", "【API:instructionExecute】"));
                    }
                    actualQty.put(entry.getKey(), sumQty);
                }
            }

            if (CollectionUtils.isNotEmpty(executeVO.getContainerMessageList())) {

                MtContLoadDtlVO10 mtContLoadDtlVO10 = null;
                for (MtInstructionVO3.ContainerMessageList containerMessageList : executeVO.getContainerMessageList()) {
                    final String containerId = containerMessageList.getContainerId();
                    final String toLocatorId = containerMessageList.getToLocatorId();

                    mtContLoadDtlVO10 = new MtContLoadDtlVO10();
                    mtContLoadDtlVO10.setContainerId(containerId);
                    mtContLoadDtlVO10.setAllLevelFlag("Y");
                    List<MtContLoadDtlVO4> detailVO4List = mtContainerLoadDetailRepository
                                    .containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);

                    if (CollectionUtils.isNotEmpty(detailVO4List)) {
                        List<String> materialLotIds = detailVO4List.stream().map(MtContLoadDtlVO4::getMaterialLotId)
                                        .collect(Collectors.toList());
                        List<MtMaterialLot> mtMaterialLots =
                                        mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);

                        mtMaterialLots.stream().forEach(c -> {
                            MtInstructionVO3.MaterialLotNewList materialLotNewList =
                                            new MtInstructionVO3.MaterialLotNewList();
                            materialLotNewList.setContainerId(containerId);
                            materialLotNewList.setLocatorId(null == c.getLocatorId() ? "" : c.getLocatorId());
                            materialLotNewList.setMaterialLotId(c.getMaterialLotId());
                            materialLotNewList.setPrimaryUomId(c.getPrimaryUomId());
                            materialLotNewList.setPrimaryUomQty(c.getPrimaryUomQty());
                            materialLotNewList.setToLocatorId(null == toLocatorId ? "" : toLocatorId);
                            materialLotNewLists.add(materialLotNewList);
                        });

                        Map<String, List<MtInstructionVO3.MaterialLotNewList>> groupMap =
                                        materialLotNewLists.stream().collect(Collectors
                                                        .groupingBy(c -> c.getLocatorId() + "::" + c.getToLocatorId()));

                        for (Map.Entry<String, List<MtInstructionVO3.MaterialLotNewList>> entry : groupMap.entrySet()) {
                            BigDecimal sumQty = BigDecimal.ZERO;
                            MtUomVO1 mtUomVO1 = null;

                            for (MtInstructionVO3.MaterialLotNewList materialLotNewList : entry.getValue()) {
                                mtUomVO1 = new MtUomVO1();
                                mtUomVO1.setSourceUomId(materialLotNewList.getPrimaryUomId());
                                mtUomVO1.setSourceValue(materialLotNewList.getPrimaryUomQty());
                                mtUomVO1.setTargetUomId(mtMaterialVO.getPrimaryUomId());
                                mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                                sumQty = sumQty.add(BigDecimal.valueOf(mtUomVO1.getTargetValue()));
                            }

                            if (sumQty.compareTo(BigDecimal.ZERO) <= 0) {
                                throw new MtException("MT_INSTRUCTION_0035",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_INSTRUCTION_0035", "INSTRUCTION",
                                                                "【API:instructionExecute】"));
                            }
                            actualQty.put(entry.getKey(), sumQty);
                        }
                    }
                }
            }
        }

        MtInstructionActualVO mtInstructionActualVO = null;
        for (Map.Entry<String, BigDecimal> entry : actualQty.entrySet()) {
            List<String> instructionActualDetailIds = new ArrayList<String>();

            mtInstructionActualVO = new MtInstructionActualVO();
            mtInstructionActualVO.setInstructionId(mtInstruction.getInstructionId());
            mtInstructionActualVO.setInstructionType(mtInstruction.getInstructionType());
            mtInstructionActualVO.setBusinessType(mtInstruction.getBusinessType());
            mtInstructionActualVO.setMaterialId(mtInstruction.getMaterialId());
            mtInstructionActualVO.setUomId(mtMaterialVO.getPrimaryUomId());
            mtInstructionActualVO.setSourceOrderType(mtInstruction.getSourceOrderType());
            mtInstructionActualVO.setSourceOrderId(mtInstruction.getSourceOrderId());
            mtInstructionActualVO.setFromSiteId(mtInstruction.getFromSiteId());
            mtInstructionActualVO.setToSiteId(mtInstruction.getToSiteId());
            mtInstructionActualVO.setCostCenterId(mtInstruction.getCostCenterId());
            mtInstructionActualVO.setSupplierId(mtInstruction.getSupplierId());
            mtInstructionActualVO.setSupplierSiteId(mtInstruction.getSupplierSiteId());
            mtInstructionActualVO.setFromOwnerType(mtInstruction.getFromOwnerType());
            mtInstructionActualVO.setToOwnerType(mtInstruction.getToOwnerType());
            mtInstructionActualVO.setCustomerId(mtInstruction.getCustomerId());
            mtInstructionActualVO.setCustomerSiteId(mtInstruction.getCustomerSiteId());
            mtInstructionActualVO.setEventId(eventId);

            String[] locatorIds = entry.getKey().split("::", -1);

            mtInstructionActualVO.setFromLocatorId(locatorIds[0]);
            mtInstructionActualVO.setToLocatorId(locatorIds[1]);
            mtInstructionActualVO.setActualQty(entry.getValue().doubleValue());

            String instructionActualId = mtInstructionActualRepository
                            .instructionActualUpdate(tenantId, mtInstructionActualVO).getActualId();

            if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotMessageList())) {
                Map<String, List<MtInstructionVO3.MaterialLotList>> groupMap = executeVO.getMaterialLotMessageList()
                                .stream()
                                .collect(Collectors.groupingBy(c -> c.getFromLocatorId() + "::" + c.getToLocatorId()));

                List<MtInstructionVO3.MaterialLotList> tmpList = new ArrayList<MtInstructionVO3.MaterialLotList>();
                for (Map.Entry<String, List<MtInstructionVO3.MaterialLotList>> tmpEntry : groupMap.entrySet()) {
                    if (tmpEntry.getKey().equals(entry.getKey())) {
                        tmpList = tmpEntry.getValue();
                        break;
                    }
                }

                MtInstructionActualDetail mtInstructionActualDetail = null;
                for (MtInstructionVO3.MaterialLotList materialLot : tmpList) {
                    //单位换算
                    MtUomVO1 tempUomVO1 = new MtUomVO1();
                    tempUomVO1.setSourceUomId(materialLot.getUomId());
                    tempUomVO1.setSourceValue(materialLot.getQty());
                    tempUomVO1.setTargetUomId(mtMaterialVO.getPrimaryUomId());
                    tempUomVO1 = mtUomRepository.uomConversion(tenantId, tempUomVO1);

                    mtInstructionActualDetail = new MtInstructionActualDetail();
                    mtInstructionActualDetail.setActualId(instructionActualId);
                    mtInstructionActualDetail.setMaterialLotId(materialLot.getMaterialLotId());
                    mtInstructionActualDetail.setUomId(materialLot.getUomId());
                    mtInstructionActualDetail.setActualQty(tempUomVO1.getTargetValue());
                    mtInstructionActualDetail.setContainerId(materialLot.getContainerId());
                    mtInstructionActualDetail.setFromLocatorId(materialLot.getFromLocatorId());
                    mtInstructionActualDetail.setToLocatorId(materialLot.getToLocatorId());
                    instructionActualDetailIds.add(mtInstructionActualDetailRepository
                                    .instructionActualDetailCreate(tenantId, mtInstructionActualDetail));
                }
            }

            if (CollectionUtils.isNotEmpty(materialLotNewLists)) {
                Map<String, List<MtInstructionVO3.MaterialLotNewList>> groupMap = materialLotNewLists.stream()
                                .collect(Collectors.groupingBy(c -> c.getLocatorId() + "::" + c.getToLocatorId()));
                List<MtInstructionVO3.MaterialLotNewList> tmpList =
                                new ArrayList<MtInstructionVO3.MaterialLotNewList>();
                for (Map.Entry<String, List<MtInstructionVO3.MaterialLotNewList>> tmpEntry : groupMap.entrySet()) {
                    if (tmpEntry.getKey().equals(entry.getKey())) {
                        tmpList = tmpEntry.getValue();
                        break;
                    }
                }

                MtInstructionActualDetail mtInstructionActualDetail = null;
                for (MtInstructionVO3.MaterialLotNewList materialLot : tmpList) {
                    //单位换算
                    MtUomVO1 tempUomVO1 = new MtUomVO1();
                    tempUomVO1.setSourceUomId(materialLot.getPrimaryUomId());
                    tempUomVO1.setSourceValue(materialLot.getPrimaryUomQty());
                    tempUomVO1.setTargetUomId(mtMaterialVO.getPrimaryUomId());
                    tempUomVO1 = mtUomRepository.uomConversion(tenantId, tempUomVO1);

                    mtInstructionActualDetail = new MtInstructionActualDetail();
                    mtInstructionActualDetail.setActualId(instructionActualId);
                    mtInstructionActualDetail.setMaterialLotId(materialLot.getMaterialLotId());
                    mtInstructionActualDetail.setUomId(materialLot.getPrimaryUomId());
                    mtInstructionActualDetail.setActualQty(tempUomVO1.getTargetValue());
                    mtInstructionActualDetail.setContainerId(materialLot.getContainerId());
                    mtInstructionActualDetail.setFromLocatorId(materialLot.getLocatorId());
                    mtInstructionActualDetail.setToLocatorId(materialLot.getToLocatorId());
                    instructionActualDetailIds.add(mtInstructionActualDetailRepository
                                    .instructionActualDetailCreate(tenantId, mtInstructionActualDetail));
                }
            }

            resultMap.put(instructionActualId, instructionActualDetailIds);
        }
        return resultMap;
    }

    /**
     * step12 - 根据EO创建实绩 返回实绩Id
     * <p>
     * 包含step 13/14
     *
     * @param tenantId tenantId
     * @param mtInstruction MtLogisticInstruction
     * @param eventId 事件Id
     * @return String
     * @author benjamin
     * @date 2019-06-20 16:30
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String solveEo(Long tenantId, MtInstruction mtInstruction, String eventId) {
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtInstruction.getEoId());

        if (mtEo.getQty() == null || BigDecimal.valueOf(mtEo.getQty()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_INSTRUCTION_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0035", "INSTRUCTION", "【API:instructionExecute】"));
        }

        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        mtInstructionActualVO.setInstructionId(mtInstruction.getInstructionId());
        mtInstructionActualVO.setInstructionType(mtInstruction.getInstructionType());
        mtInstructionActualVO.setBusinessType(mtInstruction.getBusinessType());
        mtInstructionActualVO.setEoId(mtInstruction.getEoId());
        mtInstructionActualVO.setSourceOrderType(mtInstruction.getSourceOrderType());
        mtInstructionActualVO.setSourceOrderId(mtInstruction.getSourceOrderId());
        mtInstructionActualVO.setFromSiteId(mtInstruction.getFromSiteId());
        mtInstructionActualVO.setToSiteId(mtInstruction.getToSiteId());
        mtInstructionActualVO.setFromLocatorId(mtInstruction.getFromLocatorId());
        mtInstructionActualVO.setToLocatorId(mtInstruction.getToLocatorId());
        mtInstructionActualVO.setToOwnerType(mtInstruction.getToOwnerType());
        mtInstructionActualVO.setFromOwnerType(mtInstruction.getFromOwnerType());
        mtInstructionActualVO.setSupplierId(mtInstruction.getSupplierId());
        mtInstructionActualVO.setSupplierSiteId(mtInstruction.getSupplierSiteId());
        mtInstructionActualVO.setActualQty(mtEo.getQty());
        mtInstructionActualVO.setEventId(eventId);
        return mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO).getActualId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtInstructionVO15 noInstructionExecute(Long tenantId, MtInstructionVO13 executeVO) {
        if (StringUtils.isEmpty(executeVO.getInstructionType()) && StringUtils.isEmpty(executeVO.getBusinessType())) {
            throw new MtException("MT_INSTRUCTION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                            "INSTRUCTION", "instructionType", "businseeType",
                                            "【API:noInstructionExecute】"));
        }

        if (StringUtils.isEmpty(executeVO.getInstructionType())
                        && StringUtils.isNotEmpty(executeVO.getBusinessType())) {
            MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
            typeR.setTenantId(tenantId);
            typeR.setBussinessType(executeVO.getBusinessType());
            typeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
            if (null == typeR) {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", executeVO.getBusinessType(),
                                                "【API:noInstructionExecute】"));
            }
            executeVO.setInstructionType(typeR.getInstructionType());
        }

        if (StringUtils.isNotEmpty(executeVO.getInstructionType())
                        && StringUtils.isNotEmpty(executeVO.getBusinessType())) {
            MtBusinessInstructionTypeR typeR = new MtBusinessInstructionTypeR();
            typeR.setTenantId(tenantId);
            typeR.setInstructionType(executeVO.getInstructionType());
            typeR.setBussinessType(executeVO.getBusinessType());
            typeR = mtBusinessInstructionTypeRMapper.selectOne(typeR);
            if (null == typeR) {
                throw new MtException("MT_INSTRUCTION_0045",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0045",
                                                "INSTRUCTION", executeVO.getInstructionType(),
                                                executeVO.getBusinessType(), "【API:noInstructionExecute】"));
            }
        }

        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("INSTRUCTION");
        queryType.setTypeGroup("INSTRUCTION_MOVE_TYPE");
        List<MtGenType> instructionTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
        if (!instructionTypes.stream().anyMatch(c -> c.getTypeCode().equals(executeVO.getInstructionType()))) {
            throw new MtException("MT_INSTRUCTION_0004",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0004",
                                            "INSTRUCTION", executeVO.getInstructionType(),
                                            "【API:noInstructionExecute】"));
        }

        if (CollectionUtils.isEmpty(executeVO.getMaterialLotIdList())
                        && CollectionUtils.isEmpty(executeVO.getContainerIdList())) {
            throw new MtException("MT_INSTRUCTION_0046",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0046",
                                            "INSTRUCTION", "materialLotIdList", "containerIdList",
                                            "【API:noInstructionExecute】"));
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotIdList())
                        && CollectionUtils.isNotEmpty(executeVO.getContainerIdList())) {
            throw new MtException("MT_INSTRUCTION_0039",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                            "INSTRUCTION", "materialLotIdList", "containerIdList",
                                            "【API:noInstructionExecute】"));
        }

        MtInstructionVO15 resultVO = new MtInstructionVO15();
        Map<String, List<String>> result = new HashMap<String, List<String>>(0);
        String eventId = null;

        Map<String, List<String>> map = new HashMap<String, List<String>>(0);
        if (CollectionUtils.isNotEmpty(executeVO.getContainerIdList())) {

            MtContLoadDtlVO10 mtContLoadDtlVO10 = null;
            for (String containerId : executeVO.getContainerIdList()) {
                mtContLoadDtlVO10 = new MtContLoadDtlVO10();
                mtContLoadDtlVO10.setContainerId(containerId);
                mtContLoadDtlVO10.setAllLevelFlag("Y");
                List<MtContLoadDtlVO4> detailVO4List = mtContainerLoadDetailRepository
                                .containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);

                if (CollectionUtils.isNotEmpty(detailVO4List)) {
                    List<String> materialLotIdList = detailVO4List.stream().map(MtContLoadDtlVO4::getMaterialLotId)
                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                        map.put(containerId, materialLotIdList);
                    }
                }
            }

        } else {
            map.put("", executeVO.getMaterialLotIdList());
        }

        if (MapUtils.isNotEmpty(map)) {
            String organizationId = null;
            if (StringUtils.isNotEmpty(executeVO.getToLocatorId())) {
                MtModLocatorOrgRelVO2 mtModLocatorOrgRelVO2 = new MtModLocatorOrgRelVO2();
                mtModLocatorOrgRelVO2.setLocatorId(executeVO.getToLocatorId());
                mtModLocatorOrgRelVO2.setOrganizationType("SITE");
                List<MtModLocatorOrgRelVO3> locators = this.mtModLocatorOrgRelRepository
                                .locatorLimitOrganizationQuery(tenantId, mtModLocatorOrgRelVO2);
                // 业务上保证1条数据
                if (CollectionUtils.isNotEmpty(locators)) {
                    organizationId = locators.get(0).getOrganizationId();
                }
            }

            final List<MtInstructionVO14> resultList = new ArrayList<MtInstructionVO14>();
            final String orgId = organizationId;
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                final String containerId = entry.getKey();
                List<MtMaterialLot> mtMaterialLots =
                                mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, entry.getValue());

                mtMaterialLots.stream().forEach(c -> {
                    MtInstructionVO14 vo = new MtInstructionVO14();
                    if (StringUtils.isEmpty(containerId)) {
                        vo.setContainerId(null);
                    } else {
                        vo.setContainerId(containerId);
                    }
                    vo.setFromLocatorId(null == c.getLocatorId() ? "" : c.getLocatorId());
                    vo.setFromSiteId(null == c.getSiteId() ? "" : c.getSiteId());
                    vo.setMaterialId(c.getMaterialId());
                    vo.setMaterialLotId(c.getMaterialLotId());
                    vo.setQty(c.getPrimaryUomQty());
                    vo.setToLocatorId(null == executeVO.getToLocatorId() ? "" : executeVO.getToLocatorId());
                    vo.setToSiteId(null == orgId ? "" : orgId);
                    vo.setUomId(c.getPrimaryUomId());
                    vo.setOwnerType(null == c.getOwnerType() ? "" : c.getOwnerType());
                    resultList.add(vo);
                });
            }

            if (CollectionUtils.isNotEmpty(resultList)) {
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("NO_INSTRUCTION_EXECUTE");
                eventCreateVO.setEventRequestId(executeVO.getEventRequestId());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

                Map<String, List<MtInstructionVO14>> groupMap = resultList.stream()
                                .collect(Collectors.groupingBy(c -> c.getMaterialId() + "::" + c.getUomId() + "::"
                                                + c.getFromLocatorId() + "::" + c.getToLocatorId() + "::"
                                                + c.getFromSiteId() + "::" + c.getToSiteId() + "::"
                                                + c.getOwnerType()));

                MtInstructionActualVO mtInstructionActualVO = null;
                for (Map.Entry<String, List<MtInstructionVO14>> entry : groupMap.entrySet()) {

                    BigDecimal sumQty = entry.getValue().stream().map(MtInstructionVO14::getQty).collect(
                                    CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c == null ? 0.0D : c)));

                    String[] groups = entry.getKey().split("::", -1);

                    mtInstructionActualVO = new MtInstructionActualVO();
                    mtInstructionActualVO.setInstructionType(executeVO.getInstructionType());
                    mtInstructionActualVO.setBusinessType(executeVO.getBusinessType());
                    mtInstructionActualVO.setMaterialId(groups[0]);
                    mtInstructionActualVO.setUomId(groups[1]);
                    mtInstructionActualVO.setFromLocatorId(groups[2]);
                    mtInstructionActualVO.setToLocatorId(groups[3]);
                    mtInstructionActualVO.setFromSiteId(groups[4]);
                    mtInstructionActualVO.setToSiteId(groups[5]);
                    mtInstructionActualVO.setFromOwnerType(groups[6]);
                    mtInstructionActualVO.setToOwnerType(executeVO.getToOwnerType());
                    mtInstructionActualVO.setCostCenterId(executeVO.getCostCenterId());
                    mtInstructionActualVO.setEventId(eventId);
                    mtInstructionActualVO.setSupplierId(executeVO.getSupplierId());
                    mtInstructionActualVO.setSupplierSiteId(executeVO.getSupplierSiteId());
                    mtInstructionActualVO.setCustomerId(executeVO.getCustomerId());
                    mtInstructionActualVO.setCustomerSiteId(executeVO.getCustomerSiteId());
                    mtInstructionActualVO.setSourceOrderId(executeVO.getSourceOrderId());
                    mtInstructionActualVO.setSourceOrderType(executeVO.getSourceOrderType());
                    mtInstructionActualVO.setActualQty(sumQty.doubleValue());

                    String instructionActualId = mtInstructionActualRepository
                                    .instructionActualUpdate(tenantId, mtInstructionActualVO).getActualId();

                    MtInstructionActualDetail mtInstructionActualDetail = null;
                    List<String> actualDetailIds = new ArrayList<String>();
                    for (MtInstructionVO14 mtInstructionVO14 : entry.getValue()) {
                        mtInstructionActualDetail = new MtInstructionActualDetail();
                        mtInstructionActualDetail.setActualId(instructionActualId);
                        mtInstructionActualDetail.setMaterialLotId(mtInstructionVO14.getMaterialLotId());
                        mtInstructionActualDetail.setUomId(mtInstructionVO14.getUomId());
                        mtInstructionActualDetail.setActualQty(mtInstructionVO14.getQty());
                        mtInstructionActualDetail.setContainerId(mtInstructionVO14.getContainerId());
                        mtInstructionActualDetail.setFromLocatorId(mtInstructionVO14.getFromLocatorId());
                        mtInstructionActualDetail.setToLocatorId(mtInstructionVO14.getToLocatorId());
                        actualDetailIds.add(mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId,
                                        mtInstructionActualDetail));
                    }

                    result.put(instructionActualId, actualDetailIds);
                }
            }
        }

        resultVO.setEventId(eventId);
        resultVO.setActualList(result);
        return resultVO;
    }

    @Override
    public void instructionExecuteVerify(Long tenantId, MtInstructionVO8 vo) {
        if (StringUtils.isEmpty(vo.getInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionExecuteVerify】"));
        }

        if (CollectionUtils.isNotEmpty(vo.getMaterialMessageList())) {
            for (MtInstructionVO3.MaterialMessageList materialMessageList : vo.getMaterialMessageList()) {
                if (StringUtils.isEmpty(materialMessageList.getMaterialId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "materialId", "【API:instructionExecuteVerify】"));
                }
                if (null == materialMessageList.getQty()) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "qty", "【API:instructionExecuteVerify】"));
                }
                if (materialMessageList.getQty() <= 0) {
                    throw new MtException("MT_INSTRUCTION_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0035",
                                                    "INSTRUCTION", "qty", "【API:instructionExecuteVerify】"));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(vo.getMaterialLotMessageList())) {
            for (MtInstructionVO3.MaterialLotList materialLotList : vo.getMaterialLotMessageList()) {
                if (StringUtils.isEmpty(materialLotList.getMaterialLotId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "materialLotId", "【API:instructionExecuteVerify】"));
                }
                if (null == materialLotList.getQty()) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "qty", "【API:instructionExecuteVerify】"));
                }
                if (materialLotList.getQty() <= 0) {
                    throw new MtException("MT_INSTRUCTION_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0035",
                                                    "INSTRUCTION", "qty", "【API:instructionExecuteVerify】"));
                }
                if (StringUtils.isEmpty(materialLotList.getUomId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "uomId", "【API:instructionExecuteVerify】"));
                }
            }
        }

        MtInstruction mtInstruction = instructionPropertyGet(tenantId, vo.getInstructionId());
        if (null == mtInstruction) {
            throw new MtException("MT_INSTRUCTION_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                            "INSTRUCTION", vo.getInstructionId(), "【API:instructionExecuteVerify】"));
        }

        List<String> fromLocatorIdList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(mtInstruction.getFromLocatorId())) {
            fromLocatorIdList.add(mtInstruction.getFromLocatorId());

            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(mtInstruction.getFromLocatorId());
            mtModLocatorVO9.setQueryType("ALL");
            List<String> locatorIds = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
            if (CollectionUtils.isNotEmpty(locatorIds)) {
                fromLocatorIdList.addAll(locatorIds);
            }
        }

        List<String> toLocatorIdList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(mtInstruction.getToLocatorId())) {
            toLocatorIdList.add(mtInstruction.getToLocatorId());

            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(mtInstruction.getToLocatorId());
            mtModLocatorVO9.setQueryType("ALL");
            List<String> locatorIds = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
            if (CollectionUtils.isNotEmpty(locatorIds)) {
                toLocatorIdList.addAll(locatorIds);
            }
        }

        // 若输入参数中fromLocatorId，toLocatorId不全为空，调用API{locatorBasicPropertyBatchGet}
        List<String> allLocatorIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(vo.getMaterialMessageList())) {
            allLocatorIds.addAll(vo.getMaterialMessageList().stream()
                            .map(MtInstructionVO3.MaterialMessageList::getFromLocatorId).distinct()
                            .collect(Collectors.toList()));

            allLocatorIds.addAll(vo.getMaterialMessageList().stream()
                            .map(MtInstructionVO3.MaterialMessageList::getToLocatorId).distinct()
                            .collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(vo.getMaterialLotMessageList())) {
            allLocatorIds.addAll(vo.getMaterialLotMessageList().stream()
                            .map(MtInstructionVO3.MaterialLotList::getFromLocatorId).distinct()
                            .collect(Collectors.toList()));

            allLocatorIds.addAll(vo.getMaterialLotMessageList().stream()
                            .map(MtInstructionVO3.MaterialLotList::getToLocatorId).distinct()
                            .collect(Collectors.toList()));
        }

        allLocatorIds = allLocatorIds.stream().filter(t -> StringUtils.isNotEmpty(t)).distinct()
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(allLocatorIds)) {
            List<MtModLocator> mtModLocators =
                            mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, allLocatorIds);

            if (CollectionUtils.isEmpty(mtModLocators)) {
                throw new MtException("MT_INSTRUCTION_0058", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0058", "INSTRUCTION", "【API:instructionExecuteVerify】"));
            }

            List<MtModLocator> noEqualYList = mtModLocators.stream()
                            .filter(t -> !"Y".equalsIgnoreCase(t.getEnableFlag())).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(noEqualYList)) {
                throw new MtException("MT_INSTRUCTION_0058", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0058", "INSTRUCTION", "【API:instructionExecuteVerify】"));
            }
        }



        BigDecimal sumQty = BigDecimal.ZERO;
        List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository
                        .instructionLimitActualPropertyGet(tenantId, vo.getInstructionId());
        if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
            MtUomVO1 mtUomVO1 = null;
            for (MtInstructionActual mtInstructionActual : mtInstructionActuals) {
                mtUomVO1 = new MtUomVO1();
                mtUomVO1.setSourceUomId(mtInstructionActual.getUomId());
                mtUomVO1.setSourceValue(mtInstructionActual.getActualQty());
                mtUomVO1.setTargetUomId(mtInstruction.getUomId());
                mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                sumQty = sumQty.add(BigDecimal.valueOf(mtUomVO1.getTargetValue()));
            }
        }

        BigDecimal exeQty = BigDecimal.valueOf(mtInstruction.getQuantity()).subtract(sumQty);

        if (!(STATUS_RELEASED.equals(mtInstruction.getInstructionStatus())
                        || STATUS_COMPLETED_CANCEL.equals(mtInstruction.getInstructionStatus()))) {
            throw new MtException("MT_INSTRUCTION_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0032",
                                            "INSTRUCTION", mtInstruction.getInstructionId(),
                                            "【API:instructionExecuteVerify】"));
        }

        if (StringUtils.isNotEmpty(mtInstruction.getSourceInstructionId())) {
            MtInstruction sourceMtInstruction =
                            instructionPropertyGet(tenantId, mtInstruction.getSourceInstructionId());
            if (!STATUS_COMPLETED.equals(sourceMtInstruction.getInstructionStatus())) {
                throw new MtException("MT_INSTRUCTION_0010", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0010", "INSTRUCTION", "【API:instructionExecuteVerify】"));
            }
        }

        String qtyFlagLimit = StringUtils.isEmpty(vo.getQtyLimitFlag()) ? "Y" : vo.getQtyLimitFlag();

        if (CollectionUtils.isNotEmpty(vo.getMaterialMessageList())) {
            BigDecimal materialQty = BigDecimal.ZERO;
            for (MtInstructionVO3.MaterialMessageList materialMessageList : vo.getMaterialMessageList()) {
                if (!materialMessageList.getMaterialId().equals(mtInstruction.getMaterialId())) {
                    throw new MtException("MT_INSTRUCTION_0047",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0047",
                                                    "INSTRUCTION", materialMessageList.getMaterialId(),
                                                    "【API:instructionExecuteVerify】"));
                }

                if (CollectionUtils.isNotEmpty(fromLocatorIdList)
                                && StringUtils.isNotEmpty(materialMessageList.getFromLocatorId())
                                && !fromLocatorIdList.contains(materialMessageList.getFromLocatorId())) {
                    throw new MtException("MT_INSTRUCTION_0049",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0049",
                                                    "INSTRUCTION", materialMessageList.getFromLocatorId(),
                                                    "【API:instructionExecuteVerify】"));
                }

                if (CollectionUtils.isNotEmpty(toLocatorIdList)
                                && StringUtils.isNotEmpty(materialMessageList.getToLocatorId())
                                && !toLocatorIdList.contains(materialMessageList.getToLocatorId())) {
                    throw new MtException("MT_INSTRUCTION_0050",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0050",
                                                    "INSTRUCTION", materialMessageList.getToLocatorId(),
                                                    "【API:instructionExecuteVerify】"));
                }

                materialQty = materialQty.add(BigDecimal.valueOf(materialMessageList.getQty()));
            }

            if ("Y".equals(qtyFlagLimit) && exeQty.compareTo(materialQty) < 0) {
                throw new MtException("MT_INSTRUCTION_0048", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0048", "INSTRUCTION", "【API:instructionExecuteVerify】"));
            }
        }

        if (CollectionUtils.isNotEmpty(vo.getMaterialLotMessageList())) {
            BigDecimal materialQty = BigDecimal.ZERO;

            MtInstructionDetailVO dto = new MtInstructionDetailVO();
            dto.setInstructionId(vo.getInstructionId());
            List<MtInstructionDetailVO1> mtInstructionDetails =
                            this.mtInstructionDetailRepository.propertyLimitInstructionDetailQuery(tenantId, dto);

            List<String> materialLotIds = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(mtInstructionDetails)) {
                materialLotIds = mtInstructionDetails.stream().map(MtInstructionDetailVO1::getMaterialLotId)
                                .collect(Collectors.toList());
            }

            // materialLotIds为空则执行下面步骤 6-c
            if (CollectionUtils.isEmpty(materialLotIds)) {
                List<String> inputMaterialLotIds = vo.getMaterialLotMessageList().stream()
                                .map(MtInstructionVO3.MaterialLotList::getMaterialLotId).collect(Collectors.toList());
                List<MtMaterialLot> mtMaterialLots =
                                this.mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, inputMaterialLotIds);
                Map<String, String> materialLotMap = new HashMap<String, String>(0);
                for (String inputMaterialLotId : inputMaterialLotIds) {
                    Optional<MtMaterialLot> optional = mtMaterialLots.stream()
                                    .filter(c -> c.getMaterialLotId().equals(inputMaterialLotId)).findFirst();
                    if (optional.isPresent()) {
                        materialLotMap.put(inputMaterialLotId, optional.get().getMaterialId());
                    } else {
                        materialLotMap.put(inputMaterialLotId, "");
                    }
                }

                for (Map.Entry<String, String> tmpMap : materialLotMap.entrySet()) {
                    if (!tmpMap.getValue().equals(mtInstruction.getMaterialId())) {
                        throw new MtException("MT_INSTRUCTION_0054",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0054", "INSTRUCTION", tmpMap.getKey(),
                                                        "【API:instructionExecuteVerify】"));
                    }
                }
            }



            for (MtInstructionVO3.MaterialLotList materialLotList : vo.getMaterialLotMessageList()) {
                if (CollectionUtils.isNotEmpty(materialLotIds)
                                && !materialLotIds.contains(materialLotList.getMaterialLotId())) {
                    throw new MtException("MT_INSTRUCTION_0051",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0051",
                                                    "INSTRUCTION", materialLotList.getMaterialLotId(),
                                                    "【API:instructionExecuteVerify】"));
                }

                if (CollectionUtils.isNotEmpty(fromLocatorIdList)
                                && StringUtils.isNotEmpty(materialLotList.getFromLocatorId())
                                && !fromLocatorIdList.contains(materialLotList.getFromLocatorId())) {
                    throw new MtException("MT_INSTRUCTION_0049",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0049",
                                                    "INSTRUCTION", materialLotList.getFromLocatorId(),
                                                    "【API:instructionExecuteVerify】"));
                }

                if (CollectionUtils.isNotEmpty(toLocatorIdList)
                                && StringUtils.isNotEmpty(materialLotList.getToLocatorId())
                                && !toLocatorIdList.contains(materialLotList.getToLocatorId())) {
                    throw new MtException("MT_INSTRUCTION_0050",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0050",
                                                    "INSTRUCTION", materialLotList.getToLocatorId(),
                                                    "【API:instructionExecuteVerify】"));
                }

                MtUomVO1 mtUomVO1 = new MtUomVO1();
                mtUomVO1.setSourceUomId(materialLotList.getUomId());
                mtUomVO1.setSourceValue(materialLotList.getQty());
                mtUomVO1.setTargetUomId(mtInstruction.getUomId());
                mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                materialQty = materialQty.add(BigDecimal.valueOf(mtUomVO1.getTargetValue()));
            }

            if ("Y".equals(qtyFlagLimit) && exeQty.compareTo(materialQty) < 0) {
                throw new MtException("MT_INSTRUCTION_0048", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0048", "INSTRUCTION", "【API:instructionExecuteVerify】"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtInstructionVO5> instructionExecutedBack(Long tenantId, String instructionId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionExecutedBack】"));
        }

        List<MtInstructionActual> mtInstructionActuals =
                        mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);
        if (CollectionUtils.isEmpty(mtInstructionActuals)) {
            return Collections.emptyList();
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_EXECUTE_BACK);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        final List<MtInstructionVO5> resultList = new ArrayList<>();
        MtInstructionVO5 result = null;
        MtInstructionActualVO actualUpdateVO = null;
        for (MtInstructionActual mtInstructionActual : mtInstructionActuals) {
            if (StringUtils.isEmpty(mtInstructionActual.getMaterialId())) {
                actualUpdateVO = new MtInstructionActualVO();
                actualUpdateVO.setInstructionId(instructionId);
                actualUpdateVO.setActualId(mtInstructionActual.getActualId());
                actualUpdateVO.setInstructionType(mtInstructionActual.getInstructionType());
                actualUpdateVO.setEventId(eventId);
                actualUpdateVO.setActualQty(BigDecimal.valueOf(mtInstructionActual.getActualQty())
                                .multiply(BigDecimal.valueOf(-1L)).doubleValue());
                mtInstructionActualRepository.instructionActualUpdate(tenantId, actualUpdateVO);

                result = new MtInstructionVO5();
                result.setMaterialLotId(null);
                result.setContainerId(null);
                result.setActualId(mtInstructionActual.getActualId());
                result.setEventId(eventId);
                resultList.add(result);
            } else {
                MtInstructionActualDetail detail = new MtInstructionActualDetail();
                detail.setActualId(mtInstructionActual.getActualId());
                List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository
                                .propertyLimitInstructionActualDetailQuery(tenantId, detail);

                actualUpdateVO = new MtInstructionActualVO();
                actualUpdateVO.setInstructionId(instructionId);
                actualUpdateVO.setActualId(mtInstructionActual.getActualId());
                actualUpdateVO.setInstructionType(mtInstructionActual.getInstructionType());
                actualUpdateVO.setEventId(eventId);
                actualUpdateVO.setActualQty(BigDecimal.valueOf(mtInstructionActual.getActualQty())
                                .multiply(BigDecimal.valueOf(-1L)).doubleValue());
                mtInstructionActualRepository.instructionActualUpdate(tenantId, actualUpdateVO);

                if (CollectionUtils.isEmpty(detailList)) {
                    result = new MtInstructionVO5();
                    result.setMaterialLotId(null);
                    result.setContainerId(null);
                    result.setActualId(mtInstructionActual.getActualId());
                    result.setEventId(eventId);
                    resultList.add(result);
                } else {
                    List<String> actualDetailIds = detailList.stream()
                                    .map(MtInstructionActualDetailVO::getActualDetailId).collect(Collectors.toList());
                    mtInstructionActualDetailRepository.instructionActualDetailBatchDelete(tenantId, actualDetailIds);

                    for (MtInstructionActualDetailVO ever : detailList) {
                        result = new MtInstructionVO5();
                        result.setMaterialLotId(ever.getMaterialLotId());
                        result.setContainerId(ever.getContainerId());
                        result.setActualId(ever.getActualId());
                        result.setEventId(eventId);
                        resultList.add(result);
                    }
                }
            }
        }

        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionRelease(Long tenantId, String instructionId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionRelease】"));
        }

        instructionReleaseVerify(tenantId, instructionId);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_STATUS_UPDATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtInstructionVO updateVO = new MtInstructionVO();
        updateVO.setInstructionId(instructionId);
        updateVO.setInstructionStatus(STATUS_RELEASED);
        updateVO.setEventId(eventId);
        instructionUpdate(tenantId, updateVO, "N");
    }

    @Override
    public void instructionReleaseVerify(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionReleaseVerify】"));
        }

        MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);

        if (mtInstruction == null || !(STATUS_NEW.equals(mtInstruction.getInstructionStatus()))) {
            throw new MtException("MT_INSTRUCTION_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0015", "INSTRUCTION", "【API:instructionReleaseVerify】"));
        }

        if (StringUtils.isNotEmpty(mtInstruction.getSourceDocId())) {
            MtInstructionDoc mtLogisticInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId,
                            mtInstruction.getSourceDocId());
            if (null == mtLogisticInstructionDoc) {
                throw new MtException("MT_INSTRUCTION_0057", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0057", "INSTRUCTION", "【API:instructionReleaseVerify】"));
            }
            if (!STATUS_RELEASED.equals(mtLogisticInstructionDoc.getInstructionDocStatus())) {
                throw new MtException("MT_INSTRUCTION_0056", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0056", "INSTRUCTION", "【API:instructionReleaseVerify】"));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionCancel(Long tenantId, String instructionId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionCancel】"));
        }

        instructionCancelVerify(tenantId, instructionId);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_STATUS_UPDATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtInstructionVO updateVO = new MtInstructionVO();
        updateVO.setInstructionId(instructionId);
        updateVO.setInstructionStatus(STATUS_CANCEL);
        updateVO.setEventId(eventId);
        instructionUpdate(tenantId, updateVO, "N");
    }

    @Override
    public void instructionCancelVerify(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionCancelVerify】"));
        }

        MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);

        if (mtInstruction == null) {
            throw new MtException("MT_INSTRUCTION_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0031", "INSTRUCTION", "【API:instructionCancelVerify】"));
        }

        boolean statusCheck = STATUS_NEW.equals(mtInstruction.getInstructionStatus())
                        || STATUS_RELEASED.equals(mtInstruction.getInstructionStatus())
                        || STATUS_COMPLETED_CANCEL.equals(mtInstruction.getInstructionStatus());
        if (!statusCheck) {
            throw new MtException("MT_INSTRUCTION_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0031", "INSTRUCTION", "【API:instructionCancelVerify】"));
        }

        if (STATUS_RELEASED.equals(mtInstruction.getInstructionStatus())) {
            // 逻辑修改，指令实绩如果存在且数量大于0，则报错
            List<MtInstructionActual> mtInstructionActuals =
                            mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);
            if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                for (MtInstructionActual mtInstructionActual : mtInstructionActuals) {
                    if (mtInstructionActual.getActualQty() != null && BigDecimal
                                    .valueOf(mtInstructionActual.getActualQty()).compareTo(BigDecimal.ZERO) == 1) {
                        throw new MtException("MT_INSTRUCTION_0018",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0018", "INSTRUCTION",
                                                        "【API:instructionCancelVerify】"));
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionComplete(Long tenantId, String instructionId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionComplete】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_STATUS_UPDATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtInstructionVO updateVO = new MtInstructionVO();
        updateVO.setInstructionId(instructionId);
        updateVO.setInstructionStatus(STATUS_COMPLETED);
        updateVO.setEventId(eventId);
        instructionUpdate(tenantId, updateVO, "N");
    }

    @Override
    public void instructionCompleteVerify(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionCompleteVerify】"));
        }

        MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);
        if (mtInstruction == null) {
            throw new MtException("MT_INSTRUCTION_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0023", "INSTRUCTION", instructionId, "【API:instructionCompleteVerify】"));
        }

        if (!(STATUS_RELEASED.equals(mtInstruction.getInstructionStatus())
                        || STATUS_COMPLETED_CANCEL.equals(mtInstruction.getInstructionStatus()))) {
            throw new MtException("MT_INSTRUCTION_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0032", "INSTRUCTION", "【API:instructionCompleteVerify】"));
        }

        List<MtInstructionActual> mtInstructionActuals =
                        mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);

        if (CollectionUtils.isEmpty(mtInstructionActuals)) {
            throw new MtException("MT_INSTRUCTION_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0024", "INSTRUCTION", "【API:instructionCompleteVerify】"));
        }

        MtUomVO1 transferUomVO = null;
        BigDecimal sumActualQty = BigDecimal.ZERO;
        for (MtInstructionActual mtInstructionActual : mtInstructionActuals) {
            if (StringUtils.isNotEmpty(mtInstructionActual.getMaterialId())) {
                if (!mtInstructionActual.getUomId().equals(mtInstruction.getUomId())) {
                    transferUomVO = new MtUomVO1();
                    transferUomVO.setSourceUomId(mtInstructionActual.getUomId());
                    transferUomVO.setSourceValue(mtInstructionActual.getActualQty());
                    transferUomVO.setTargetUomId(mtInstruction.getUomId());
                    transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                    sumActualQty = sumActualQty.add(BigDecimal.valueOf(transferUomVO.getTargetValue()));
                } else {
                    sumActualQty = sumActualQty.add(BigDecimal.valueOf(mtInstructionActual.getActualQty()));
                }
            } else {
                sumActualQty = sumActualQty.add(BigDecimal.valueOf(mtInstructionActual.getActualQty()));
            }
        }

        if (sumActualQty.compareTo(BigDecimal.valueOf(mtInstruction.getQuantity())) < 0) {
            throw new MtException("MT_INSTRUCTION_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0025", "INSTRUCTION", "【API:instructionCompleteVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionCompletedCancel(Long tenantId, String instructionId, String eventRequestId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionCompletedCancel】"));
        }

        instructionCompletedCancelVerify(tenantId, instructionId);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(INSTRUCTION_STATUS_UPDATE_EVENT_TYPE_CODE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtInstructionVO updateVO = new MtInstructionVO();
        updateVO.setInstructionId(instructionId);
        updateVO.setInstructionStatus(STATUS_COMPLETED_CANCEL);
        updateVO.setEventId(eventId);
        instructionUpdate(tenantId, updateVO, "N");
    }

    @Override
    public void instructionCompletedCancelVerify(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionCompletedCancelVerify】"));
        }

        MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);
        if (mtInstruction == null || !STATUS_COMPLETED.equals(mtInstruction.getInstructionStatus())) {
            throw new MtException("MT_INSTRUCTION_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0026", "INSTRUCTION", "【API:instructionCompletedCancelVerify】"));
        }

        MtInstructionVO10 subQueryInstruction = new MtInstructionVO10();
        subQueryInstruction.setSourceInstructionId(instructionId);

        // sub list only have one element
        List<String> subInstructionIdList = propertyLimitInstructionQuery(tenantId, subQueryInstruction);

        if (CollectionUtils.isNotEmpty(subInstructionIdList)) {
            MtInstruction subInstruction = instructionPropertyGet(tenantId, subInstructionIdList.get(0));
            if (subInstruction == null || !STATUS_RELEASED.equals(subInstruction.getInstructionStatus())) {
                throw new MtException("MT_INSTRUCTION_0027",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0027",
                                                "INSTRUCTION", "【API:instructionCompletedCancelVerify】"));
            }

            List<MtInstructionActual> subInstructionActuals = mtInstructionActualRepository
                            .instructionLimitActualPropertyGet(tenantId, subInstructionIdList.get(0));
            if (CollectionUtils.isNotEmpty(subInstructionActuals)) {
                throw new MtException("MT_INSTRUCTION_0028",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0028",
                                                "INSTRUCTION", "【API:instructionCompletedCancelVerify】"));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtInstructionVO9 instructionExecutedCancel(Long tenantId, MtInstructionVO12 executeVO) {
        if (StringUtils.isEmpty(executeVO.getInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionExecutedCancel】"));
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())
                        && CollectionUtils.isNotEmpty(executeVO.getMaterialLotIdList())) {
            throw new MtException("MT_INSTRUCTION_0039",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                            "INSTRUCTION", "materialMessageList", "materialLotIdList",
                                            "【API:instructionExecutedCancel】"));
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())
                        && CollectionUtils.isNotEmpty(executeVO.getContainerIdList())) {
            throw new MtException("MT_INSTRUCTION_0039",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                            "INSTRUCTION", "materialMessageList", "containerIdList",
                                            "【API:instructionExecutedCancel】"));
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotIdList())
                        && CollectionUtils.isNotEmpty(executeVO.getContainerIdList())) {
            throw new MtException("MT_INSTRUCTION_0039",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0039",
                                            "INSTRUCTION", "materialLotIdList", "containerIdList",
                                            "【API:instructionExecutedCancel】"));
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())) {
            for (MtInstructionVO3.MaterialMessageList materialMessageList : executeVO.getMaterialMessageList()) {
                if (StringUtils.isEmpty(materialMessageList.getMaterialId())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "materialId", "【API:instructionExecutedCancel】"));
                }
                if (null == materialMessageList.getQty()) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "qty", "【API:instructionExecutedCancel】"));
                }
                if (materialMessageList.getQty() <= 0) {
                    throw new MtException("MT_INSTRUCTION_0035",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0035",
                                                    "INSTRUCTION", "qty", "【API:instructionExecutedCancel】"));
                }
            }
        }

        List<MtInstructionActual> mtInstructionActuals = this.mtInstructionActualRepository
                        .instructionLimitActualPropertyGet(tenantId, executeVO.getInstructionId());
        if (CollectionUtils.isEmpty(mtInstructionActuals)) {
            throw new MtException("MT_INSTRUCTION_0017",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0017",
                                            "INSTRUCTION", executeVO.getInstructionId(),
                                            "【API:instructionExecutedCancel】"));
        }

        List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository
                        .instructionLimitActualDetailQuery(tenantId, executeVO.getInstructionId());

        for (MtInstructionActual mtInstructionActual : mtInstructionActuals) {
            if (StringUtils.isNotEmpty(mtInstructionActual.getEoId())) {
                throw new MtException("MT_INSTRUCTION_0043", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0043", "INSTRUCTION", "【API:instructionExecutedCancel】"));
            }

            if (StringUtils.isNotEmpty(mtInstructionActual.getActualId())
                            && BigDecimal.valueOf(mtInstructionActual.getActualQty()).compareTo(BigDecimal.ZERO) > 0
                            && CollectionUtils.isEmpty(mtInstructionActualDetails)) {
                if (CollectionUtils.isEmpty(executeVO.getMaterialMessageList())) {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "materialMessageList",
                                                    "【API:instructionExecutedCancel】"));
                } else {
                    for (MtInstructionVO3.MaterialMessageList materialMessageList : executeVO
                                    .getMaterialMessageList()) {

                        String fromLocatorId = null == materialMessageList.getFromLocatorId() ? ""
                                        : materialMessageList.getFromLocatorId();
                        String toLocatorId = null == materialMessageList.getToLocatorId() ? ""
                                        : materialMessageList.getToLocatorId();

                        Optional<MtInstructionActual> optional = mtInstructionActuals.stream()
                                        .filter(c -> c.getMaterialId().equals(materialMessageList.getMaterialId())
                                                        && c.getFromLocatorId().equals(fromLocatorId)
                                                        && c.getToLocatorId().equals(toLocatorId))
                                        .findFirst();
                        if (!optional.isPresent()) {
                            throw new MtException("MT_INSTRUCTION_0040",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_INSTRUCTION_0040", "INSTRUCTION",
                                                            "【API:instructionExecutedCancel】"));
                        } else {
                            if (BigDecimal.valueOf(materialMessageList.getQty())
                                            .compareTo(BigDecimal.valueOf(optional.get().getActualQty())) > 0) {
                                throw new MtException("MT_INSTRUCTION_0042",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_INSTRUCTION_0042", "INSTRUCTION",
                                                                "【API:instructionExecutedCancel】"));
                            }
                        }
                    }
                }
            }

            if (StringUtils.isNotEmpty(mtInstructionActual.getActualId())
                            && CollectionUtils.isNotEmpty(mtInstructionActualDetails)) {
                if (CollectionUtils.isEmpty(executeVO.getMaterialLotIdList())
                                && CollectionUtils.isEmpty(executeVO.getContainerIdList())) {
                    throw new MtException("MT_INSTRUCTION_0003",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                                    "INSTRUCTION", "materialLotIdList", "containerIdList",
                                                    "【API:instructionExecutedCancel】"));
                }

                if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotIdList())) {
                    for (String materialLotId : executeVO.getMaterialLotIdList()) {
                        if (!mtInstructionActualDetails.stream()
                                        .anyMatch(c -> c.getMaterialLotId().equals(materialLotId))) {
                            throw new MtException("MT_INSTRUCTION_0040",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_INSTRUCTION_0040", "INSTRUCTION",
                                                            "【API:instructionExecutedCancel】"));
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(executeVO.getContainerIdList())) {
                    for (String containerId : executeVO.getContainerIdList()) {
                        if (mtInstructionActualDetails.stream()
                                        .noneMatch(c -> c.getContainerId().equals(containerId))) {
                            throw new MtException("MT_INSTRUCTION_0040",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_INSTRUCTION_0040", "INSTRUCTION",
                                                            "【API:instructionExecutedCancel】"));
                        }
                    }
                }
            }
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(executeVO.getEventRequestId());
        eventCreateVO.setEventTypeCode("INSTRUCTION_EXECUTE_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        List<String> actualIds = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialMessageList())) {
            for (MtInstructionVO3.MaterialMessageList materialMessageList : executeVO.getMaterialMessageList()) {

                String fromLocatorId = null == materialMessageList.getFromLocatorId() ? ""
                                : materialMessageList.getFromLocatorId();
                String toLocatorId = null == materialMessageList.getToLocatorId() ? ""
                                : materialMessageList.getToLocatorId();

                Optional<MtInstructionActual> optional = mtInstructionActuals.stream()
                                .filter(c -> c.getMaterialId().equals(materialMessageList.getMaterialId())
                                                && c.getFromLocatorId().equals(fromLocatorId)
                                                && c.getToLocatorId().equals(toLocatorId))
                                .findFirst();

                MtInstructionActual mtInstructionActual = optional.get();
                Double qty = materialMessageList.getQty();

                MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                mtInstructionActualVO.setActualId(mtInstructionActual.getActualId());
                mtInstructionActualVO.setInstructionId(mtInstructionActual.getInstructionId());
                mtInstructionActualVO.setInstructionType(mtInstructionActual.getInstructionType());
                mtInstructionActualVO.setActualQty(
                                BigDecimal.valueOf(qty).multiply(BigDecimal.valueOf(-1.0D)).doubleValue());
                mtInstructionActualVO.setEventId(eventId);
                mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

                actualIds.add(mtInstructionActual.getActualId());
            }
        }

        if (CollectionUtils.isNotEmpty(executeVO.getMaterialLotIdList())) {
            List<MtInstructionActualDetail> list = mtInstructionActualDetails.stream()
                            .filter(c -> executeVO.getMaterialLotIdList().contains(c.getMaterialLotId()))
                            .collect(Collectors.toList());

            Map<String, List<MtInstructionActualDetail>> groupMap =
                            list.stream().collect(Collectors.groupingBy(c -> c.getActualId()));

            for (Map.Entry<String, List<MtInstructionActualDetail>> entry : groupMap.entrySet()) {
                List<String> deleteActualDetailIds = new ArrayList<String>();
                BigDecimal sumQty = BigDecimal.ZERO;
                MtUomVO1 mtUomVO1 = null;

                Optional<MtInstructionActual> optional = mtInstructionActuals.stream()
                                .filter(c -> c.getActualId().equals(entry.getKey())).findFirst();

                for (MtInstructionActualDetail mtInstructionActualDetail : entry.getValue()) {
                    mtUomVO1 = new MtUomVO1();
                    mtUomVO1.setSourceUomId(mtInstructionActualDetail.getUomId());
                    mtUomVO1.setSourceValue(mtInstructionActualDetail.getActualQty());
                    mtUomVO1.setTargetUomId(optional.get().getUomId());
                    mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                    sumQty = sumQty.add(BigDecimal.valueOf(mtUomVO1.getTargetValue()));
                    deleteActualDetailIds.add(mtInstructionActualDetail.getActualDetailId());
                }

                MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                mtInstructionActualVO.setActualId(optional.get().getActualId());
                mtInstructionActualVO.setInstructionId(optional.get().getInstructionId());
                mtInstructionActualVO.setInstructionType(optional.get().getInstructionType());
                mtInstructionActualVO.setActualQty(sumQty.multiply(BigDecimal.valueOf(-1.0D)).doubleValue());
                mtInstructionActualVO.setEventId(eventId);
                mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

                this.mtInstructionActualDetailMapper.batchDelete(tenantId, deleteActualDetailIds);

                actualIds.add(entry.getKey());
            }
        }

        if (CollectionUtils.isNotEmpty(executeVO.getContainerIdList())) {
            List<MtInstructionActualDetail> list = mtInstructionActualDetails.stream()
                            .filter(c -> executeVO.getContainerIdList().contains(c.getContainerId()))
                            .collect(Collectors.toList());

            Map<String, List<MtInstructionActualDetail>> groupMap =
                            list.stream().collect(Collectors.groupingBy(c -> c.getActualId()));

            for (Map.Entry<String, List<MtInstructionActualDetail>> entry : groupMap.entrySet()) {
                List<String> deleteActualDetailIds = new ArrayList<String>();
                BigDecimal sumQty = BigDecimal.ZERO;
                MtUomVO1 mtUomVO1 = null;

                Optional<MtInstructionActual> optional = mtInstructionActuals.stream()
                                .filter(c -> c.getActualId().equals(entry.getKey())).findFirst();

                for (MtInstructionActualDetail mtInstructionActualDetail : entry.getValue()) {
                    mtUomVO1 = new MtUomVO1();
                    mtUomVO1.setSourceUomId(mtInstructionActualDetail.getUomId());
                    mtUomVO1.setSourceValue(mtInstructionActualDetail.getActualQty());
                    mtUomVO1.setTargetUomId(optional.get().getUomId());
                    mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                    sumQty = sumQty.add(BigDecimal.valueOf(mtUomVO1.getTargetValue()));
                    deleteActualDetailIds.add(mtInstructionActualDetail.getActualDetailId());
                }

                MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                mtInstructionActualVO.setActualId(optional.get().getActualId());
                mtInstructionActualVO.setInstructionId(optional.get().getInstructionId());
                mtInstructionActualVO.setInstructionType(optional.get().getInstructionType());
                mtInstructionActualVO.setActualQty(sumQty.multiply(BigDecimal.valueOf(-1.0D)).doubleValue());
                mtInstructionActualVO.setEventId(eventId);
                mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

                this.mtInstructionActualDetailMapper.batchDelete(tenantId, deleteActualDetailIds);

                actualIds.add(entry.getKey());
            }
        }


        MtInstructionVO9 result = new MtInstructionVO9();
        result.setActualId(actualIds);
        result.setEventId(eventId);
        return result;
    }

    @Override
    public MtInstructionVO11 instructionLimitInstructionAndActualQuery(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId",
                                            "【API:instructionLimitInstructionAndActualQuery】"));
        }

        MtInstruction instruction = new MtInstruction();
        instruction.setTenantId(tenantId);
        instruction.setInstructionId(instructionId);
        instruction = mtInstructionMapper.selectOne(instruction);
        if (null == instruction) {
            return null;
        }

        String fromParentLocatorId = null;
        String fromLocatorId = instruction.getFromLocatorId();
        if (StringUtils.isNotEmpty(fromLocatorId)) {
            MtModLocator mtModLocator = this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId, fromLocatorId);
            if (null != mtModLocator) {
                String parentLocatorId = mtModLocator.getParentLocatorId();
                if (StringUtils.isNotEmpty(parentLocatorId)) {
                    mtModLocator = this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId, parentLocatorId);
                    if (null != mtModLocator && "AREA".equals(mtModLocator.getLocatorCategory())) {
                        fromParentLocatorId = parentLocatorId;
                    }
                }
            }
        }

        String toParentLocatorId = null;
        String toLocatorId = instruction.getToLocatorId();
        if (StringUtils.isNotEmpty(toLocatorId)) {
            MtModLocator mtModLocator = this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId, toLocatorId);
            if (null != mtModLocator) {
                String parentLocatorId = mtModLocator.getParentLocatorId();
                if (StringUtils.isNotEmpty(parentLocatorId)) {
                    mtModLocator = this.mtModLocatorRepository.locatorBasicPropertyGet(tenantId, parentLocatorId);
                    if (null != mtModLocator && "AREA".equals(mtModLocator.getLocatorCategory())) {
                        toParentLocatorId = parentLocatorId;
                    }
                }
            }
        }

        MtInstructionVO11 result = new MtInstructionVO11();
        BeanUtils.copyProperties(instruction, result);
        result.setFromParentLocatorId(fromParentLocatorId);
        result.setToParentLocatorId(toParentLocatorId);

        List<MtInstructionActual> mtInstructionActuals =
                        this.mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);
        if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
            BigDecimal targetValue = BigDecimal.ZERO;
            List<MtInstructionVO11.InstructionActualLine> instructionActualLines =
                            new ArrayList<MtInstructionVO11.InstructionActualLine>();
            MtInstructionVO11.InstructionActualLine instructionActualLine = null;

            for (MtInstructionActual mtInstructionActual : mtInstructionActuals) {
                instructionActualLine = new MtInstructionVO11.InstructionActualLine();
                instructionActualLine.setActualId(mtInstructionActual.getActualId());
                instructionActualLine.setActualQty(mtInstructionActual.getActualQty());
                instructionActualLine.setSourceOrderId(mtInstructionActual.getSourceOrderId());
                instructionActualLine.setSourceOrderType(mtInstructionActual.getSourceOrderType());
                instructionActualLines.add(instructionActualLine);

                MtUomVO1 transferUomVO = new MtUomVO1();
                transferUomVO.setSourceUomId(mtInstructionActual.getUomId());
                transferUomVO.setSourceValue(mtInstructionActual.getActualQty());
                transferUomVO.setTargetUomId(instruction.getUomId());
                transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                if (null != transferUomVO && null != transferUomVO.getTargetValue()) {
                    targetValue = targetValue.add(BigDecimal.valueOf(transferUomVO.getTargetValue()));
                }
            }

            result.setInstructionActualLines(instructionActualLines);
            result.setExecutableQty(
                            (BigDecimal.valueOf(instruction.getQuantity()).subtract(targetValue)).doubleValue());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void instructionAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "keyId", "【API:instructionAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtInstruction mtInstruction = new MtInstruction();
        mtInstruction.setTenantId(tenantId);
        mtInstruction.setInstructionId(dto.getKeyId());
        mtInstruction = mtInstructionMapper.selectOne(mtInstruction);
        if (mtInstruction == null || StringUtils.isEmpty(mtInstruction.getInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0055",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0055",
                                            "INSTRUCTION", "keyId:" + dto.getKeyId(), "mt_instruction",
                                            "【API:instructionAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    public void instructionExecuteStatusVerify(Long tenantId, String instructionId) {
        // 校验参数合规性
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteStatusVerify】"));
        }

        // 查询数据
        MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);

        if (null == mtInstruction) {
            throw new MtException("MT_INSTRUCTION_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteStatusVerify】"));
        }

        if (!STATUS_RELEASED.equals(mtInstruction.getInstructionStatus())
                        && !STATUS_COMPLETED_CANCEL.equals(mtInstruction.getInstructionStatus())) {
            throw new MtException("MT_INSTRUCTION_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0032",
                                            "INSTRUCTION", mtInstruction.getInstructionId(),
                                            "【API:instructionExecuteVerify】"));
        }

        if (StringUtils.isNotEmpty(mtInstruction.getSourceInstructionId())) {
            MtInstruction sourceMtInstruction =
                            instructionPropertyGet(tenantId, mtInstruction.getSourceInstructionId());
            if (!STATUS_COMPLETED.equals(sourceMtInstruction.getInstructionStatus())) {
                throw new MtException("MT_INSTRUCTION_0010",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0010",
                                                "INSTRUCTION", "【API:instructionExecuteStatusVerify】"));
            }
        }

    }

    @Override
    public void instructionExecuteMaterialLotVerify(Long tenantId, MtInstructionVO16 vo) {
        // 校验参数合规性
        if (StringUtils.isEmpty(vo.getInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId",
                                            "【API:instructionExecuteMaterialLotVerify】"));
        }

        if (CollectionUtils.isEmpty(vo.getMaterialLotId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "materialLotId",
                                            "【API:instructionExecuteMaterialLotVerify】"));
        }

        // 执行物料批校验
        MtInstructionDetailVO dto = new MtInstructionDetailVO();
        dto.setInstructionId(vo.getInstructionId());
        List<MtInstructionDetailVO1> mtInstructionDetails =
                        this.mtInstructionDetailRepository.propertyLimitInstructionDetailQuery(tenantId, dto);

        if (CollectionUtils.isEmpty(mtInstructionDetails)) {
            MtInstruction mtInstruction = instructionPropertyGet(tenantId, vo.getInstructionId());
            if (mtInstruction == null) {
                throw new MtException("MT_INSTRUCTION_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0023",
                                                "INSTRUCTION", vo.getInstructionId(),
                                                "【API:instructionExecuteMaterialLotVerify】"));
            }
        } else {
            List<String> materialLotIds = mtInstructionDetails.stream().map(MtInstructionDetailVO1::getMaterialLotId)
                            .collect(Collectors.toList());
            for (String id : vo.getMaterialLotId()) {
                if (!materialLotIds.contains(id)) {
                    throw new MtException("MT_INSTRUCTION_0051",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0051",
                                                    "INSTRUCTION", id, "【API:instructionExecuteMaterialLotVerify】"));
                }
            }
        }
    }

    @Override
    public void instructionExecuteMaterialVerify(Long tenantId, MtInstructionVO17 vo) {
        // 校验参数合规性
        if (StringUtils.isEmpty(vo.getInstructionId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteMaterialVerify】"));
        }

        if (StringUtils.isEmpty(vo.getMaterialId()) && CollectionUtils.isEmpty(vo.getMaterialLotId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "materialId、materialLotId",
                                            "【API:instructionExecuteMaterialVerify】"));
        }

        // 调用API{instructionPropertyGet}
        MtInstruction mtInstruction = instructionPropertyGet(tenantId, vo.getInstructionId());

        if (null == mtInstruction) {
            throw new MtException("MT_INSTRUCTION_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteMaterialVerify】"));
        }

        // 若输入参数materialId有值
        if (StringUtils.isNotEmpty(vo.getMaterialId()) && !mtInstruction.getMaterialId().equals(vo.getMaterialId())) {
            throw new MtException("MT_INSTRUCTION_0047",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0047",
                                            "INSTRUCTION", vo.getMaterialId(),
                                            "【API:instructionExecuteMaterialVerify】"));
        }

        // 4.若输入参数materialLotId列表有值
        if (CollectionUtils.isNotEmpty(vo.getMaterialLotId())) {
            List<MtMaterialLot> mtMaterialLots =
                            mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, vo.getMaterialLotId());
            if (CollectionUtils.isEmpty(mtMaterialLots)) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "materialLotId",
                                                "【API:instructionExecuteMaterialVerify】"));
            }
            for (MtMaterialLot materialLot : mtMaterialLots) {
                if (!materialLot.getMaterialId().equals(mtInstruction.getMaterialId())) {
                    throw new MtException("MT_INSTRUCTION_0054",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0054",
                                                    "INSTRUCTION", materialLot.getMaterialLotId(),
                                                    "【API:instructionExecuteMaterialVerify】"));
                }
            }
        }
    }

    @Override
    public void instructionExecuteLocatorVerify(Long tenantId, MtInstructionVO18 dto) {
        // 校验参数合规性
        String instructionId = dto.getInstructionId();
        String fromLocatorId = dto.getFromLocatorId();
        String toLocatorId = dto.getToLocatorId();
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteLocatorVerify】"));
        }

        if (StringUtils.isEmpty(fromLocatorId) && StringUtils.isEmpty(toLocatorId)) {
            throw new MtException("MT_INSTRUCTION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                            "INSTRUCTION", "fromLocatorId、toLocatorId",
                                            "【API:instructionExecuteMaterialVerify】"));
        }

        // 2.调用API{locatorBasicPropertyBatchGet} 获取输出参数enableFlag
        List<String> locatorIds = Arrays.asList(fromLocatorId, toLocatorId);
        List<MtModLocator> mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIds);

        if (CollectionUtils.isEmpty(mtModLocators)) {
            throw new MtException("MT_INSTRUCTION_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0058", "INSTRUCTION", "【API:instructionExecuteLocatorVerify】"));
        }

        List<MtModLocator> noEqualYList = mtModLocators.stream().filter(t -> !"Y".equalsIgnoreCase(t.getEnableFlag()))
                        .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(noEqualYList)) {
            throw new MtException("MT_INSTRUCTION_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0058", "INSTRUCTION", "【API:instructionExecuteLocatorVerify】"));
        }

        // 3.调用API{instructionPropertyGet}，传入参数instructionId=输入参数instructionId，
        MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);

        if (null == mtInstruction) {
            throw new MtException("MT_INSTRUCTION_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteLocatorVerify】"));
        }

        String instructionFromLocatorId = mtInstruction.getFromLocatorId();
        String instructionToLocatorId = mtInstruction.getToLocatorId();

        // b)获取组织对象全部库位,调用API{organizationLimitLocatorAllQuery}
        // i.获取来源库位下所有库位
        List<String> locatorIdFroms = new ArrayList<>();
        if (StringUtils.isNotEmpty(instructionFromLocatorId)) {
            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(instructionFromLocatorId);
            mtModLocatorVO9.setQueryType("ALL");
            locatorIdFroms = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
            locatorIdFroms.add(instructionFromLocatorId);
        }

        // ii.获取目标库位下所有库位
        List<String> locatorIdTos = new ArrayList<>();
        if (StringUtils.isNotEmpty(instructionToLocatorId)) {
            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(instructionToLocatorId);
            mtModLocatorVO9.setQueryType("ALL");
            locatorIdTos = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
            locatorIdTos.add(instructionToLocatorId);
        }

        // 4.执行库位校验
        // i.来源库位校验
        if (CollectionUtils.isNotEmpty(locatorIdFroms)) {
            List<String> isFindList =
                            locatorIdFroms.stream().filter(t -> t.equals(fromLocatorId)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(isFindList)) {
                throw new MtException("MT_INSTRUCTION_0049",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0049",
                                                "INSTRUCTION", "fromLocatorId",
                                                "【API:instructionExecuteLocatorVerify】"));
            }
        }

        // ii.目标库位校验
        if (CollectionUtils.isNotEmpty(locatorIdTos)) {
            List<String> isFindList =
                            locatorIdTos.stream().filter(t -> t.equals(toLocatorId)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(isFindList)) {
                throw new MtException("MT_INSTRUCTION_0050",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0050",
                                                "INSTRUCTION", "toLocatorId", "【API:instructionExecuteLocatorVerify】"));
            }
        }
    }

    @Override
    public void instructionExecuteQtyVerify(Long tenantId, MtInstructionVO19 dto) {
        // 校验参数合规性
        String instructionId = dto.getInstructionId();
        String uomId = dto.getUomId();
        Double qty = dto.getQty();
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteQtyVerify】"));
        }

        if (null == qty) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "qty", "【API:instructionExecuteQtyVerify】"));
        }

        // 2.调用API{instructionPropertyGet}，传入参数instructionId=输入参数instructionId
        MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);

        if (null == mtInstruction) {
            throw new MtException("MT_INSTRUCTION_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteQtyVerify】"));
        }
        String uomId1 = mtInstruction.getUomId();
        Double quantity = mtInstruction.getQuantity();

        // 3.调用API{instructionLimitActualPropertyGet}，传入参数instructionId=输入参数instructionId
        List<MtInstructionActual> mtInstructionActuals =
                        this.mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);

        BigDecimal executableQty;
        BigDecimal targetValue = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {

            for (MtInstructionActual actual : mtInstructionActuals) {
                // 计算可执行数,调用API{uomConversion}
                MtUomVO1 transferUomVO = new MtUomVO1();
                transferUomVO.setSourceUomId(actual.getUomId());
                transferUomVO.setSourceValue(actual.getActualQty());
                transferUomVO.setTargetUomId(uomId1);
                transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                if (null != transferUomVO && null != transferUomVO.getTargetValue()) {
                    targetValue = targetValue.add(BigDecimal.valueOf(transferUomVO.getTargetValue()));
                }
            }
        }

        executableQty = BigDecimal.valueOf(quantity).subtract(targetValue);

        // 4.执行数量校验
        Double targetValueQty = 0D;
        if (StringUtils.isEmpty(uomId)) {
            targetValueQty = qty;
        } else {
            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceUomId(uomId);
            transferUomVO.setSourceValue(qty);
            transferUomVO.setTargetUomId(uomId1);
            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
            if (null != transferUomVO && null != transferUomVO.getTargetValue()) {
                targetValueQty = transferUomVO.getTargetValue();
            }
        }

        if (executableQty.compareTo(BigDecimal.valueOf(targetValueQty)) < 0) {
            throw new MtException("MT_INSTRUCTION_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0048", "INSTRUCTION", "【API:instructionExecuteQtyVerify】"));
        }

    }

    @Override
    public void instructionExecuteLocatorBatchVerify(Long tenantId, List<MtInstructionVO18> dtos) {
        List<String> instructionIds = dtos.stream().map(MtInstructionVO18::getInstructionId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        List<String> fromLocatorIdList = dtos.stream().map(MtInstructionVO18::getFromLocatorId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        List<String> toLocatorIdList = dtos.stream().map(MtInstructionVO18::getToLocatorId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

        // 1.检验参数
        if (CollectionUtils.isEmpty(instructionIds) || instructionIds.size() != dtos.size()) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionExecuteLocatorBatchVerify】"));
        }

        if (CollectionUtils.isEmpty(fromLocatorIdList) && CollectionUtils.isEmpty(toLocatorIdList)) {
            throw new MtException("MT_INSTRUCTION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                            "INSTRUCTION", "fromLocatorId ", "toLocatorId",
                                            "【API:instructionExecuteLocatorBatchVerify】"));
        }

        // 2.调用API{locatorBasicPropertyBatchGet}，传入参数locatorId列表为输入参数fromLocatorId、toLocatorId，获取输出参数enableFlag
        List<String> locatorIds = new ArrayList<>();
        locatorIds.addAll(fromLocatorIdList);
        locatorIds.addAll(toLocatorIdList);
        locatorIds = locatorIds.stream().filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        List<MtModLocator> mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIds);

        if (CollectionUtils.isEmpty(mtModLocators)) {
            throw new MtException("MT_INSTRUCTION_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0058", "INSTRUCTION", "【API:instructionExecuteLocatorBatchVerify】"));
        }

        List<MtModLocator> noEqualYList = mtModLocators.stream().filter(t -> !"Y".equalsIgnoreCase(t.getEnableFlag()))
                        .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(noEqualYList)) {
            throw new MtException("MT_INSTRUCTION_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0058", "INSTRUCTION", "【API:instructionExecuteLocatorBatchVerify】"));
        }

        // 3.调用API{instructionPropertyBatchGet}，传入参数instructionId=输入参数instructionId列表，
        List<MtInstruction> mtInstructions = instructionPropertyBatchGet(tenantId, instructionIds);
        List<String> instructionsFromLocatorIds = new ArrayList<>();
        List<String> instructionsToLocatorIds = new ArrayList<>();

        Map<String, List<MtInstruction>> fromLocatorIdMap = new HashMap<>();
        Map<String, List<MtInstruction>> toLocatorIdMap = new HashMap<>();
        Map<String, MtInstruction> mtInstructionMap = new HashMap<>();


        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            // a.获取输出参数

            mtInstructionMap = mtInstructions.stream().collect(Collectors.toMap(t -> t.getInstructionId(), t -> t));
        }


        Map<String, List<String>> locatorIdFromsMap = new HashMap<>();
        Map<String, List<String>> locatorIdTosMap = new HashMap<>();

        for (MtInstruction mtInstruction : mtInstructions) {
            // organizationLimitLocatorAllQuery
            // i. 获取来源库位下所有库位
            List<String> locatorIdFroms = new ArrayList<>();
            if (StringUtils.isNotEmpty(mtInstruction.getFromLocatorId())) {
                MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
                mtModLocatorVO9.setLocatorId(mtInstruction.getFromLocatorId());
                mtModLocatorVO9.setQueryType("ALL");
                locatorIdFroms = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
                locatorIdFroms.add(mtInstruction.getFromLocatorId());
            }
            locatorIdFromsMap.put(mtInstruction.getInstructionId(), locatorIdFroms);

            // ii. 获取目标库位下所有库位
            List<String> locatorIdTos = new ArrayList<>();
            if (StringUtils.isNotEmpty(mtInstruction.getToLocatorId())) {

                MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
                mtModLocatorVO9.setLocatorId(mtInstruction.getToLocatorId());
                mtModLocatorVO9.setQueryType("ALL");
                locatorIdTos = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
                locatorIdTos.add(mtInstruction.getToLocatorId());
            }
            locatorIdTosMap.put(mtInstruction.getInstructionId(), locatorIdTos);
        }

        // 4.执行库位校验
        // i.来源库位校验
        for (MtInstructionVO18 dto : dtos) {
            String fromLocatorId = dto.getFromLocatorId();
            String toLocatorId = dto.getToLocatorId();
            List<String> locatorIdFroms = locatorIdFromsMap.get(dto.getInstructionId());
            List<String> locatorIdTos = locatorIdTosMap.get(dto.getInstructionId());

            if (CollectionUtils.isNotEmpty(locatorIdFroms) && StringUtils.isNotEmpty(fromLocatorId)
                            && !locatorIdFroms.contains(fromLocatorId)) {
                throw new MtException("MT_INSTRUCTION_0049",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0049",
                                                "INSTRUCTION", "fromLocatorId",
                                                "【API:instructionExecuteLocatorBatchVerify】"));
            }


            // ii.目标库位校验
            if (CollectionUtils.isNotEmpty(locatorIdTos) && StringUtils.isNotEmpty(toLocatorId)
                            && !locatorIdTos.contains(toLocatorId)) {
                throw new MtException("MT_INSTRUCTION_0050",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0050",
                                                "INSTRUCTION", "toLocatorId",
                                                "【API:instructionExecuteLocatorBatchVerify】"));
            }
        }

    }

    @Override
    public void instructionExecuteMaterialBatchVerify(Long tenantId, List<MtInstructionVO17> dtos) {
        List<String> materialIdList = dtos.stream().map(MtInstructionVO17::getMaterialId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        List<String> instructionIdList = dtos.stream().map(MtInstructionVO17::getInstructionId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

        // 1.检验参数
        if (CollectionUtils.isEmpty(instructionIdList) || instructionIdList.size() != dtos.size()) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionExecuteMaterialBatchVerify】"));
        }

        // 取出所有的materialLotId列表
        List<String> materialLotIdList = new ArrayList<>();

        for (MtInstructionVO17 dto : dtos) {
            List<String> materialLotIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(dto.getMaterialLotId())) {
                materialLotIds = dto.getMaterialLotId().stream().filter(t -> StringUtils.isNotEmpty(t))
                                .collect(Collectors.toList());
            }

            if (StringUtils.isEmpty(dto.getMaterialId()) && CollectionUtils.isEmpty(materialLotIds)) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "materialId、materialLotId",
                                                "【API:instructionExecuteMaterialBatchVerify】"));
            }
            if (CollectionUtils.isNotEmpty(materialLotIds)) {
                materialLotIdList.addAll(materialLotIds);
            }
        }


        // 2. 调用API{instructionPropertyBatchGet}，传入参数instructionId=输入参数instructionId列表
        List<MtInstruction> mtInstructions = instructionPropertyBatchGet(tenantId, instructionIdList);
        List<String> materialIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            // a.获取输出参数
            materialIds = mtInstructions.stream().map(MtInstruction::getMaterialId).collect(Collectors.toList());
        }

        // 3. 若输入参数materialId有值
        if (CollectionUtils.isNotEmpty(materialIdList)) {

            for (String materialId : materialIdList) {
                List<String> isFinds =
                                materialIds.stream().filter(t -> materialId.equals(t)).collect(Collectors.toList());

                if (CollectionUtils.isEmpty(isFinds)) {
                    throw new MtException("MT_INSTRUCTION_0047",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0047",
                                                    "INSTRUCTION", "materialId",
                                                    "【API:instructionExecuteMaterialBatchVerify】"));
                }
            }
        }

        // 4. 若输入参数materialLotId列表有值
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            List<MtMaterialLot> mtMaterialLots =
                            mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);

            if (CollectionUtils.isNotEmpty(mtMaterialLots)) {
                List<String> materialIdLists = mtMaterialLots.stream().map(MtMaterialLot::getMaterialId)
                                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(materialIdLists)) {
                    for (String materialId : materialIdLists) {
                        List<String> isFinds = materialIds.stream().filter(t -> materialId.equals(t))
                                        .collect(Collectors.toList());

                        if (CollectionUtils.isEmpty(isFinds)) {
                            throw new MtException("MT_INSTRUCTION_0047",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_INSTRUCTION_0047", "INSTRUCTION", "materialId",
                                                            "【API:instructionExecuteMaterialBatchVerify】"));
                        }
                    }
                } else {
                    throw new MtException("MT_INSTRUCTION_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                    "INSTRUCTION", "instructionId ",
                                                    "【API:instructionExecuteMaterialBatchVerify】"));
                }
            } else {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", "materialLotId",
                                                "【API:instructionExecuteMaterialBatchVerify】"));
            }
        }
    }

    @Override
    public void instructionExecuteQtyBatchVerify(Long tenantId, List<MtInstructionVO19> dtos) {
        List<String> instructionIds = dtos.stream().map(MtInstructionVO19::getInstructionId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        List<Double> qtys = dtos.stream().map(MtInstructionVO19::getQty).collect(Collectors.toList());


        // 1.检验参数
        if (CollectionUtils.isEmpty(instructionIds) || instructionIds.size() != dtos.size()) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId ", "【API:instructionExecuteQtyBatchVerify】"));
        }

        if (CollectionUtils.isEmpty(qtys) || qtys.size() != dtos.size()) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "qty", "【API:instructionExecuteQtyBatchVerify】"));
        }

        // 2.调用API{instructionPropertyBatchGet}，传入参数instructionId=输入参数instructionId列表
        List<MtInstruction> mtInstructions = instructionPropertyBatchGet(tenantId, instructionIds);

        // 一个指令只会对应一个MtInstruction
        Map<String, List<MtInstruction>> instructionMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            // a.获取输出参数
            instructionMap = mtInstructions.stream().collect(Collectors.groupingBy(MtInstruction::getInstructionId));
        }

        // 调用API{instructionLimitActualPropertyBatchGet}，传入参数instructionId=输入参数instructionId列表
        List<MtInstructionActualVO2> mtInstructionActualVO2s =
                        mtInstructionActualRepository.instructionLimitActualPropertyBatchGet(tenantId, instructionIds);

        Map<String, List<MtInstructionActualVO2>> instructionActualMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtInstructionActualVO2s)) {
            // a) 获取输出参数（可能为多组列表）
            instructionActualMap = mtInstructionActualVO2s.stream()
                            .collect(Collectors.groupingBy(MtInstructionActualVO2::getInstructionId));
        }

        // b) 计算可执行数,调用API{uomConversion}
        for (MtInstructionVO19 dto : dtos) {
            // 一个指令只会对应一个MtInstruction,这里取第一个即可
            MtInstruction mtInstruction = new MtInstruction();
            if (MapUtils.isNotEmpty(instructionMap)) {
                mtInstruction = instructionMap.get(dto.getInstructionId()).get(0);
            }

            List<MtInstructionActualVO2> actualVO2s = new ArrayList<>();
            if (MapUtils.isNotEmpty(instructionActualMap)) {
                actualVO2s = instructionActualMap.get(dto.getInstructionId());
            }

            BigDecimal sumTargetValue = BigDecimal.ZERO;
            BigDecimal executableQty;
            if (CollectionUtils.isNotEmpty(actualVO2s)) {

                for (MtInstructionActualVO2 actualVO2 : actualVO2s) {
                    MtUomVO1 transferUomVO = new MtUomVO1();
                    transferUomVO.setSourceUomId(actualVO2.getUomId());
                    transferUomVO.setSourceValue(actualVO2.getActualQty());
                    transferUomVO.setTargetUomId(mtInstruction.getUomId());
                    transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

                    if (null != transferUomVO && null != transferUomVO.getTargetValue()) {
                        // 对同一指令id的qty求和
                        sumTargetValue = sumTargetValue.add(BigDecimal.valueOf(transferUomVO.getTargetValue()));
                    }
                }
            }
            // 定义executableQty=quantity（第一步输出参数quantity）-sumTargetValue
            executableQty = BigDecimal.valueOf(mtInstruction.getQuantity()).subtract(sumTargetValue);

            // 3.执行数量校验
            Double targetValueQty = 0D;
            if (StringUtils.isEmpty(dto.getUomId())) {
                targetValueQty = dto.getQty();
            } else {
                MtUomVO1 transferUomVO = new MtUomVO1();
                transferUomVO.setSourceUomId(dto.getUomId());
                transferUomVO.setSourceValue(dto.getQty());
                transferUomVO.setTargetUomId(mtInstruction.getUomId());
                transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                if (null != transferUomVO && null != transferUomVO.getTargetValue()) {
                    targetValueQty = transferUomVO.getTargetValue();
                }
            }

            if (executableQty.compareTo(BigDecimal.valueOf(targetValueQty)) < 0) {
                throw new MtException("MT_INSTRUCTION_0048",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0048",
                                                "INSTRUCTION", "【API:instructionExecuteQtyBatchVerify】"));
            }
        }
    }

    @Override
    public void instructionExecuteStatusBatchVerify(Long tenantId, List<String> instructionIdList) {
        // 1.检验参数
        if (CollectionUtils.isEmpty(instructionIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionExecuteStatusBatchVerify】"));
        }

        // 2. 调用API{instructionPropertyBatchGet}，传入参数instructionId=输入参数instructionId
        List<MtInstruction> mtInstructions = instructionPropertyBatchGet(tenantId, instructionIdList);

        List<String> instructionStatusList = new ArrayList<>();
        List<String> sourceInstructionIdList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            instructionStatusList = mtInstructions.stream().map(MtInstruction::getInstructionStatus)
                            .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
            sourceInstructionIdList = mtInstructions.stream().map(MtInstruction::getSourceInstructionId)
                            .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        }

        // 3. 校验指令状态是否合规
        if (CollectionUtils.isEmpty(instructionStatusList) || instructionStatusList.stream()
                        .filter(t -> !"RELEASED".equalsIgnoreCase(t) && !"COMPLETED_CANCEL".equalsIgnoreCase(t))
                        .collect(Collectors.toList()).size() > 0) {
            throw new MtException("MT_INSTRUCTION_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0032",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionExecuteStatusBatchVerify】"));
        }

        if (CollectionUtils.isNotEmpty(sourceInstructionIdList)) {
            for (String instructionId : sourceInstructionIdList) {
                MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);

                if (null != mtInstruction && !"COMPLETED".equalsIgnoreCase(mtInstruction.getInstructionStatus())) {
                    throw new MtException("MT_INSTRUCTION_0010",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0010",
                                                    "INSTRUCTION", "【API:instructionExecuteStatusBatchVerify】"));
                }
            }
        }

    }

    @Override
    public void instructionExecuteMaterialLotBatchVerify(Long tenantId, List<MtInstructionVO16> dtos) {
        List<String> instructionIds = dtos.stream().map(MtInstructionVO16::getInstructionId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

        List<String> materialLotAllIds = new ArrayList<>();
        for (MtInstructionVO16 dto : dtos) {

            List<String> materialLotIds = dto.getMaterialLotId();
            if (CollectionUtils.isEmpty(materialLotIds) || CollectionUtils.isEmpty(materialLotIds.stream()
                            .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList()))) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "materialLotId  ",
                                                "【API:instructionExecuteMaterialLotBatchVerify】"));
            }
            materialLotAllIds.addAll(materialLotIds);
        }
        // 1.检验参数
        if (CollectionUtils.isEmpty(instructionIds) || instructionIds.size() != dtos.size()) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionExecuteMaterialLotBatchVerify】"));
        }

        // 执行物料批验证
        // i. 调用API{ propertyLimitInstructionDetailBatchQuery }传入参数instructionId=输入参数instructionId列表
        List<MtInstructionDetailVO3> mtInstructionDetailVO3s = new ArrayList<>();
        for (String instructionId : instructionIds) {
            MtInstructionDetailVO3 detailVO3 = new MtInstructionDetailVO3();
            detailVO3.setInstructionId(instructionId);
            mtInstructionDetailVO3s.add(detailVO3);
        }

        List<MtInstructionDetailVO4> detailVO4s = mtInstructionDetailRepository
                        .propertyLimitInstructionDetailBatchQuery(tenantId, mtInstructionDetailVO3s);
        if (CollectionUtils.isEmpty(detailVO4s)) {
            throw new MtException("MT_INSTRUCTION_0023",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0023",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionExecuteMaterialLotBatchVerify】"));
        }
        if (CollectionUtils.isEmpty(detailVO4s.stream().map(MtInstructionDetailVO4::getMaterialLotId)
                        .collect(Collectors.toList()))) {
            List<MtInstruction> mtInstructions = instructionPropertyBatchGet(tenantId, instructionIds);
            if (CollectionUtils.isEmpty(mtInstructions)) {
                throw new MtException("MT_INSTRUCTION_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0023",
                                                "INSTRUCTION", "instructionId ",
                                                "【API:instructionExecuteMaterialLotBatchVerify】"));
            }
        }

        // ii. 获取输入参数materialLotId列表
        List<String> materialLotIdList = detailVO4s.stream().map(MtInstructionDetailVO4::getMaterialLotId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        for (String materialLotId : materialLotAllIds) {
            List<String> isFinds = materialLotIdList.stream().filter(t -> t.equals(materialLotId))
                            .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(isFinds)) {
                throw new MtException("MT_INSTRUCTION_0051",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0051",
                                                "INSTRUCTION", "materialLotId ",
                                                "【API:instructionExecuteMaterialLotBatchVerify】"));
            }
        }
    }

    @Override
    public void instructionExecuteBatchVerify(Long tenantId, List<MtInstructionVO8> dtos) {
        List<String> instructionIds = dtos.stream().map(MtInstructionVO8::getInstructionId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(instructionIds) || dtos.size() != instructionIds.size()) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionExecuteBatchVerify】"));
        }

        // 第一步，调用API{instructionPropertyBatchGet}，传入参数instructionId=输入参数instructionId，
        List<MtInstruction> mtInstructions = instructionPropertyBatchGet(tenantId, instructionIds);
        if (CollectionUtils.isEmpty(mtInstructions)) {
            throw new MtException("MT_INSTRUCTION_0023",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0023",
                                            "INSTRUCTION", "instructionId ", "【API:instructionExecuteBatchVerify】"));
        }

        Map<String, List<MtInstruction>> instructionMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            // a.获取输出参数
            instructionMap = mtInstructions.stream().collect(Collectors.groupingBy(MtInstruction::getInstructionId));
        }

        Map<String, List<String>> locatorIdFromsMap = new HashMap<>();
        Map<String, List<String>> locatorIdTosMap = new HashMap<>();
        for (MtInstruction mtInstruction : mtInstructions) {
            // organizationLimitLocatorAllQuery
            // i. 获取来源库位下所有库位
            List<String> locatorIdFroms = new ArrayList<>();
            if (StringUtils.isNotEmpty(mtInstruction.getFromLocatorId())) {
                MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
                mtModLocatorVO9.setLocatorId(mtInstruction.getFromLocatorId());
                mtModLocatorVO9.setQueryType("ALL");
                locatorIdFroms = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
                locatorIdFroms.add(mtInstruction.getFromLocatorId());
            }
            locatorIdFromsMap.put(mtInstruction.getInstructionId(), locatorIdFroms);

            // ii. 获取目标库位下所有库位
            List<String> locatorIdTos = new ArrayList<>();
            if (StringUtils.isNotEmpty(mtInstruction.getToLocatorId())) {

                MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
                mtModLocatorVO9.setLocatorId(mtInstruction.getToLocatorId());
                mtModLocatorVO9.setQueryType("ALL");
                locatorIdTos = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
                locatorIdTos.add(mtInstruction.getToLocatorId());
            }
            locatorIdTosMap.put(mtInstruction.getInstructionId(), locatorIdTos);
        }

        // 若输入参数中fromLocatorId，toLocatorId不全为空，调用API{locatorBasicPropertyBatchGet}
        List<String> fromLocatorIdList = new ArrayList<>();
        List<String> toLocatorIdList = new ArrayList<>();
        List<String> locatorIdList = new ArrayList<>();
        for (MtInstructionVO8 dto : dtos) {
            List<MtInstructionVO3.MaterialMessageList> messageList = dto.getMaterialMessageList();
            if (CollectionUtils.isNotEmpty(messageList)) {
                fromLocatorIdList.addAll(messageList.stream()
                                .map(MtInstructionVO3.MaterialMessageList::getFromLocatorId)
                                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList()));
                toLocatorIdList.addAll(messageList.stream().map(MtInstructionVO3.MaterialMessageList::getToLocatorId)
                                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList()));
            }

            List<MtInstructionVO3.MaterialLotList> lotMessageList = dto.getMaterialLotMessageList();
            if (CollectionUtils.isNotEmpty(lotMessageList)) {
                fromLocatorIdList.addAll(lotMessageList.stream().map(MtInstructionVO3.MaterialLotList::getFromLocatorId)
                                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList()));
                toLocatorIdList.addAll(lotMessageList.stream().map(MtInstructionVO3.MaterialLotList::getToLocatorId)
                                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList()));
            }
        }
        locatorIdList.addAll(fromLocatorIdList);
        locatorIdList.addAll(toLocatorIdList);

        locatorIdList = locatorIdList.stream().filter(t -> StringUtils.isNotEmpty(t)).distinct()
                        .collect(Collectors.toList());

        List<MtModLocator> mtModLocators;
        if (CollectionUtils.isNotEmpty(locatorIdList)) {
            mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIdList);
            if (CollectionUtils.isEmpty(mtModLocators)) {
                throw new MtException("MT_INSTRUCTION_0058",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0058",
                                                "INSTRUCTION", "【API:instructionExecuteLocatorBatchVerify】"));
            }

            List<MtModLocator> noEqualYList = mtModLocators.stream()
                            .filter(t -> !"Y".equalsIgnoreCase(t.getEnableFlag())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(noEqualYList)) {
                throw new MtException("MT_INSTRUCTION_0058",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0058",
                                                "INSTRUCTION", "【API:instructionExecuteLocatorBatchVerify】"));
            }
        }



        // 第二步，调用API{instructionLimitActualPropertyBatchGet}，传入参数instructionId=输入参数instructionId列表
        List<MtInstructionActualVO2> mtInstructionActualVO2s =
                        mtInstructionActualRepository.instructionLimitActualPropertyBatchGet(tenantId, instructionIds);

        Map<String, List<MtInstructionActualVO2>> instructionActualMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtInstructionActualVO2s)) {
            // a) 获取输出参数（可能为多组列表）
            instructionActualMap = mtInstructionActualVO2s.stream()
                            .collect(Collectors.groupingBy(MtInstructionActualVO2::getInstructionId));
        }


        List<String> instructionStatusList = new ArrayList<>();
        List<String> sourceInstructionIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            instructionStatusList = mtInstructions.stream().map(MtInstruction::getInstructionStatus)
                            .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
            sourceInstructionIdList = mtInstructions.stream().map(MtInstruction::getSourceInstructionId)
                            .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        }

        // 第三步 校验指令状态是否合规 这一步调整到第二步获取输出参数后面
        if (CollectionUtils.isEmpty(instructionStatusList) || instructionStatusList.stream()
                        .filter(t -> !"RELEASED".equalsIgnoreCase(t) && !"COMPLETED_CANCEL".equalsIgnoreCase(t))
                        .collect(Collectors.toList()).size() > 0) {
            throw new MtException("MT_INSTRUCTION_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0032",
                                            "INSTRUCTION", "instructionId ",
                                            "【API:instructionExecuteStatusBatchVerify】"));
        }

        if (CollectionUtils.isNotEmpty(sourceInstructionIdList)) {
            for (String instructionId : sourceInstructionIdList) {
                MtInstruction mtInstruction = instructionPropertyGet(tenantId, instructionId);

                if (null != mtInstruction && !"COMPLETED".equalsIgnoreCase(mtInstruction.getInstructionStatus())) {
                    throw new MtException("MT_INSTRUCTION_0010",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0010",
                                                    "INSTRUCTION", "【API:instructionExecuteStatusBatchVerify】"));
                }
            }
        }

        // 执行物料批校验 第五步，放到for循环外，避免多次循环
        List<MtInstructionDetailVO3> mtInstructionDetailVO3s = new ArrayList<>();
        for (String instructionId : instructionIds) {
            MtInstructionDetailVO3 detailVO3 = new MtInstructionDetailVO3();
            detailVO3.setInstructionId(instructionId);
            mtInstructionDetailVO3s.add(detailVO3);
        }

        // b) 计算可执行数,调用API{uomConversion}
        for (MtInstructionVO8 dto : dtos) {
            // 一个指令只会对应一个MtInstruction,这里取第一个即可
            MtInstruction mtInstruction = new MtInstruction();
            if (MapUtils.isNotEmpty(instructionMap)) {
                mtInstruction = instructionMap.get(dto.getInstructionId()).get(0);
            }

            List<MtInstructionActualVO2> actualVO2s = new ArrayList<>();
            if (MapUtils.isNotEmpty(instructionActualMap)) {
                actualVO2s = instructionActualMap.get(dto.getInstructionId());
            }

            BigDecimal sumTargetValue = BigDecimal.ZERO;
            BigDecimal executableQty;
            if (CollectionUtils.isNotEmpty(actualVO2s)) {

                for (MtInstructionActualVO2 actualVO2 : actualVO2s) {
                    MtUomVO1 transferUomVO = new MtUomVO1();
                    transferUomVO.setSourceUomId(actualVO2.getUomId());
                    transferUomVO.setSourceValue(actualVO2.getActualQty());
                    transferUomVO.setTargetUomId(mtInstruction.getUomId());
                    transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

                    if (null != transferUomVO && null != transferUomVO.getTargetValue()) {
                        // 对同一指令id的qty求和
                        sumTargetValue = sumTargetValue.add(BigDecimal.valueOf(transferUomVO.getTargetValue()));
                    }
                }
            }
            // 定义executableQty=quantity（第一步输出参数quantity）-sumTargetValue
            executableQty = BigDecimal.valueOf(mtInstruction.getQuantity()).subtract(sumTargetValue);


            // 第四步，若输入参数materialMessageList有值
            List<MtInstructionVO3.MaterialMessageList> materialMessageList = dto.getMaterialMessageList();
            List<String> materialIds;

            BigDecimal sumQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(materialMessageList)) {
                // ii.获取materialMessageList列表中所有materialId；
                materialIds = materialMessageList.stream().map(MtInstructionVO3.MaterialMessageList::getMaterialId)
                                .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
                for (MtInstructionVO3.MaterialMessageList messageList : materialMessageList) {
                    sumQty = sumQty.add(BigDecimal.valueOf(messageList.getQty()));
                }

                MtInstruction finalMtInstruction = mtInstruction;

                // 将ii中materialId与i获取的material对比，若相同则通过校验
                if (materialIds.stream().filter(t -> t.equals(finalMtInstruction.getMaterialId()))
                                .collect(Collectors.toList()).size() == 0) {
                    throw new MtException("MT_INSTRUCTION_0047",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0047",
                                                    "INSTRUCTION", "", "【API:instructionExecuteStatusBatchVerify】"));
                }

                // c)执行数量校验
                if ("Y".equalsIgnoreCase(dto.getQtyLimitFlag())) {
                    if (executableQty.compareTo(sumQty) < 0) {
                        throw new MtException("MT_INSTRUCTION_0048",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0048", "INSTRUCTION",
                                                        "【API:instructionExecuteQtyBatchVerify】"));
                    }
                }

                // d)执行库位校验
                // i.来源库位校验
                // 获取输入来源库位集合
                List<String> inputFromLocatorIds = materialMessageList.stream()
                                .map(MtInstructionVO3.MaterialMessageList::getFromLocatorId).distinct()
                                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(inputFromLocatorIds)) {
                    List<String> locatorIdFroms = locatorIdFromsMap.get(dto.getInstructionId());
                    if (!locatorIdFroms.containsAll(inputFromLocatorIds)) {
                        throw new MtException("MT_INSTRUCTION_0049",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0049", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionExecuteLocatorBatchVerify】"));
                    }
                }

                // ii.目标库位校验
                // 获取输入目标库位集合
                List<String> inputToLocatorIds = materialMessageList.stream()
                                .map(MtInstructionVO3.MaterialMessageList::getToLocatorId).distinct()
                                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(inputToLocatorIds)) {
                    List<String> locatorIdTos = locatorIdTosMap.get(dto.getInstructionId());
                    if (!locatorIdTos.containsAll(inputToLocatorIds)) {
                        throw new MtException("MT_INSTRUCTION_0050",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0050", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionExecuteLocatorBatchVerify】"));
                    }
                }
            }

            // 第五步，若输入参数materialLotMessageList有值
            List<MtInstructionVO3.MaterialLotList> materialLotMessageList = dto.getMaterialLotMessageList();
            if (CollectionUtils.isNotEmpty(materialLotMessageList)) {

                // 执行物料批校验
                List<MtInstructionDetailVO4> detailVO4s = mtInstructionDetailRepository
                                .propertyLimitInstructionDetailBatchQuery(tenantId, mtInstructionDetailVO3s);
                if (CollectionUtils.isEmpty(detailVO4s)) {
                    throw new MtException("MT_INSTRUCTION_0023",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0023",
                                                    "INSTRUCTION", "instructionId ",
                                                    "【API:instructionExecuteMaterialLotBatchVerify】"));
                }
                List<String> materialLotIdList = detailVO4s.stream().map(MtInstructionDetailVO4::getMaterialLotId)
                                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    // 执行物料校验 获取第一步 对应instructionId的materialId
                    String materialId = mtInstruction.getMaterialId();
                    List<MtMaterialLot> mtMaterialLots =
                                    mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);

                    if (CollectionUtils.isEmpty(mtMaterialLots)
                                    || !mtMaterialLots.stream().map(MtMaterialLot::getMaterialId)
                                                    .collect(Collectors.toList()).contains(materialId)) {
                        throw new MtException("MT_INSTRUCTION_0047",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0047", "INSTRUCTION", "materialId ",
                                                        "【API:instructionExecuteMaterialLotBatchVerify】"));
                    }

                } else {
                    List<String> materialLotIds = materialLotMessageList.stream()
                                    .map(MtInstructionVO3.MaterialLotList::getMaterialLotId)
                                    .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
                    // 取交集，结果会塞回materialLotIds集合
                    materialLotIds.retainAll(materialLotIdList);
                    if (CollectionUtils.isEmpty(materialLotIds)) {
                        throw new MtException("MT_INSTRUCTION_0051",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0051", "INSTRUCTION", "materialLotId ",
                                                        "【API:instructionExecuteMaterialLotBatchVerify】"));
                    }
                }


                // 执行数量校验 再次初始化数量
                sumTargetValue = BigDecimal.ZERO;
                if ("Y".equalsIgnoreCase(dto.getQtyLimitFlag())) {
                    for (MtInstructionVO3.MaterialLotList lotList : materialLotMessageList) {
                        MtUomVO1 transferUomVO = new MtUomVO1();
                        transferUomVO.setSourceUomId(lotList.getUomId());
                        transferUomVO.setSourceValue(lotList.getQty());
                        transferUomVO.setTargetUomId(mtInstruction.getUomId());
                        transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

                        if (null != transferUomVO && null != transferUomVO.getTargetValue()) {
                            // 对同一指令id的qty求和
                            sumTargetValue = sumTargetValue.add(BigDecimal.valueOf(transferUomVO.getTargetValue()));
                        }
                    }
                    if (executableQty.compareTo(sumTargetValue) < 0) {
                        throw new MtException("MT_INSTRUCTION_0048",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0048", "INSTRUCTION",
                                                        "【API:instructionExecuteQtyBatchVerify】"));
                    }

                }

                // d)执行库位校验
                // i.来源库位校验
                // 获取输入来源库位集合
                List<String> inputFromLocatorIds = materialLotMessageList.stream()
                                .map(MtInstructionVO3.MaterialLotList::getFromLocatorId).distinct()
                                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(inputFromLocatorIds)) {
                    List<String> locatorIdFroms = locatorIdFromsMap.get(dto.getInstructionId());
                    if (!locatorIdFroms.containsAll(inputFromLocatorIds)) {
                        throw new MtException("MT_INSTRUCTION_0049",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0049", "INSTRUCTION", "fromLocatorId",
                                                        "【API:instructionExecuteLocatorBatchVerify】"));
                    }
                }

                // ii.目标库位校验
                // 获取输入目标库位集合
                List<String> inputToLocatorIds = materialLotMessageList.stream()
                                .map(MtInstructionVO3.MaterialLotList::getToLocatorId).distinct()
                                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(inputToLocatorIds)) {
                    List<String> locatorIdTos = locatorIdTosMap.get(dto.getInstructionId());
                    if (!locatorIdTos.containsAll(inputToLocatorIds)) {
                        throw new MtException("MT_INSTRUCTION_0050",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0050", "INSTRUCTION", "toLocatorId",
                                                        "【API:instructionExecuteLocatorBatchVerify】"));
                    }
                }
            }
        }

    }

    /**
     * instructionBatchExecute-指令批量执行
     *
     * @author chuang.yang
     * @date 2020/1/10
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.instruction.domain.vo.MtInstructionVO22>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtInstructionVO22> instructionBatchExecute(Long tenantId, MtInstructionVO21 dto) {
        // 1. 验证参数有效性
        if (dto == null) {
            return Collections.emptyList();
        }

        String eventRequestId = dto.getEventRequestId();
        List<MtInstructionVO20> instructionMessageList = dto.getInstructionMessageList();
        if (CollectionUtils.isEmpty(instructionMessageList)) {
            return Collections.emptyList();
        }

        Optional<MtInstructionVO20> any = instructionMessageList.stream()
                        .filter(t -> StringUtils.isEmpty(t.getInstructionId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API:instructionBatchExecute】"));
        }

        // 记录返回数据
        List<MtInstructionVO22> resultList = new ArrayList<>();

        // 2. 批量获取指令信息
        List<String> instructionIds = instructionMessageList.stream().map(MtInstructionVO20::getInstructionId)
                        .collect(Collectors.toList());

        List<MtInstruction> mtInstructionList = this.instructionPropertyBatchGet(tenantId, instructionIds);
        if (CollectionUtils.isEmpty(mtInstructionList) || instructionIds.size() != mtInstructionList.size()) {
            throw new MtException("MT_INSTRUCTION_0006",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                            "INSTRUCTION", instructionIds.toString(), "【API:instructionBatchExecute】"));
        }

        // 转为Map数据
        Map<String, MtInstruction> mtInstructionMap =
                        mtInstructionList.stream().collect(Collectors.toMap(t -> t.getInstructionId(), t -> t));

        // 记录指令类型和对应的事件ID关系
        Map<String, String> instructionTypeEventMap = new HashMap<>(10);

        // 根据eoId是否为空分组
        List<MtInstructionVO23> emptyEoInstructionList = new ArrayList<>(instructionMessageList.size());
        List<MtInstructionVO23> notEmptyEoInstructionList = new ArrayList<>(instructionMessageList.size());

        for (MtInstructionVO20 mtInstructionVO20 : instructionMessageList) {
            int notEmptyCount = 0;
            if (CollectionUtils.isNotEmpty(mtInstructionVO20.getMaterialMessageList())) {
                for (MtInstructionVO20.MaterialMessage materialMessage : mtInstructionVO20.getMaterialMessageList()) {
                    if (StringUtils.isEmpty(materialMessage.getMaterialId())) {
                        throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_INSTRUCTION_0001", "INSTRUCTION",
                                        "materialMessageList:materialId", "【API:instructionBatchExecute】"));
                    }

                    if (materialMessage.getQty() == null) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "materialMessageList:qty",
                                                        "【API:instructionBatchExecute】"));
                    }
                }
                notEmptyCount++;
            }

            if (CollectionUtils.isNotEmpty(mtInstructionVO20.getMaterialLotMessageList())) {
                for (MtInstructionVO20.MaterialLotMessage materialLotMessage : mtInstructionVO20
                                .getMaterialLotMessageList()) {
                    if (StringUtils.isEmpty(materialLotMessage.getMaterialLotId())) {
                        throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_INSTRUCTION_0001", "INSTRUCTION",
                                        "materialLotMessageList:materialLotId", "【API:instructionBatchExecute】"));
                    }

                    if (materialLotMessage.getQty() == null) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION",
                                                        "materialLotMessageList:qty", "【API:instructionBatchExecute】"));
                    }

                    if (StringUtils.isEmpty(materialLotMessage.getUomId())) {
                        throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_INSTRUCTION_0001", "INSTRUCTION", "materialLotMessageList:uomId",
                                        "【API:instructionBatchExecute】"));
                    }
                }
                notEmptyCount++;
            }

            MtInstruction mtInstruction = mtInstructionMap.get(mtInstructionVO20.getInstructionId());
            if (mtInstruction != null) {
                // 记录指令类型不重复
                if (instructionTypeEventMap.get(mtInstruction.getInstructionType()) == null) {
                    instructionTypeEventMap.put(mtInstruction.getInstructionType(), "");
                }

                // 记录批量执行数据：按eo是否为空划分
                MtInstructionVO23 mtInstructionVO23 = new MtInstructionVO23();
                mtInstructionVO23.setInstructionId(mtInstructionVO20.getInstructionId());
                mtInstructionVO23.setMaterialId(mtInstruction.getMaterialId());
                mtInstructionVO23.setMaterialMessageList(mtInstructionVO20.getMaterialMessageList());
                mtInstructionVO23.setMaterialLotMessageList(mtInstructionVO20.getMaterialLotMessageList());
                if (StringUtils.isEmpty(mtInstruction.getEoId())) {
                    // 不为空的参数 list 有且只有一个
                    if (notEmptyCount != 1) {
                        throw new MtException("MT_INSTRUCTION_0061", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_INSTRUCTION_0061", "INSTRUCTION",
                                        "materialLotMessageList、materialMessageList", "【API:instructionBatchExecute】"));
                    }

                    emptyEoInstructionList.add(mtInstructionVO23);
                } else {
                    mtInstructionVO23.setEoId(mtInstruction.getEoId());
                    notEmptyEoInstructionList.add(mtInstructionVO23);
                }
            }
        }

        // 4. 创建事件 事件类型即为指令类型
        for (Map.Entry<String, String> entry : instructionTypeEventMap.entrySet()) {
            String eventTypeCode = entry.getKey();

            // 生成事件
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode(eventTypeCode);
            mtEventCreateVO.setEventRequestId(eventRequestId);
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

            // 回写事件ID
            entry.setValue(eventId);
        }

        // 处理指令eo为空的数据
        if (CollectionUtils.isNotEmpty(emptyEoInstructionList)) {
            // 3. eoId 为空的需要验证
            // 批量获取指令实绩数据
            List<String> emptyEoInstructionIds = emptyEoInstructionList.stream()
                            .map(MtInstructionVO23::getInstructionId).collect(Collectors.toList());
            List<MtInstructionActualVO2> instructionActualVO2s = mtInstructionActualRepository
                            .instructionLimitActualPropertyBatchGet(tenantId, emptyEoInstructionIds);

            // instructionId - sumActualQty Map数据
            Map<String, BigDecimal> instructionSumActualQtyMap = null;
            Map<String, List<MtInstructionActualDetailVO2>> instructionDetailMap = null;
            if (CollectionUtils.isNotEmpty(instructionActualVO2s)) {
                // 根据指令ID，汇总实绩数量
                instructionSumActualQtyMap = instructionActualVO2s.stream().collect(Collectors.groupingBy(
                                t -> t.getInstructionId(),
                                CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getActualQty()))));

                // 批量获取实绩明细数据
                List<MtInstructionActualDetailVO2> detailList = mtInstructionActualDetailRepository
                                .instructionLimitActualDetailBatchQuery(tenantId, emptyEoInstructionIds);
                if (CollectionUtils.isNotEmpty(detailList)) {
                    instructionDetailMap =
                                    detailList.stream().collect(Collectors.groupingBy(t -> t.getInstructionId()));
                }
            }

            // 5. eoId 为空，以传入的物料和物料批的数量作为更新依据
            List<String> materialIds = emptyEoInstructionList.stream().map(MtInstructionVO23::getMaterialId)
                            .collect(Collectors.toList());
            List<MtMaterialVO> mtMaterialVOList = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
            if (CollectionUtils.isEmpty(mtMaterialVOList)) {
                throw new MtException("MT_INSTRUCTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0013", "INSTRUCTION", "【API:instructionBatchExecute】"));
            }

            // 转为Map数据
            Map<String, String> materialUomMap = mtMaterialVOList.stream()
                            .collect(Collectors.toMap(t -> t.getMaterialId(), t -> t.getPrimaryUomId()));

            // 执行参数准备
            List<MtInstructionActualVO4> mtInstructionActualVO4s = new ArrayList<>();

            // 记录需要执行创建指令实绩明细的参数
            List<MtInstructionVO24> instructionMaterialLotList = new ArrayList<>();

            // 赋值执行参数
            for (MtInstructionVO23 instructionVO23 : emptyEoInstructionList) {
                if (MapUtils.isNotEmpty(instructionSumActualQtyMap)) {
                    // 获取指令对应实绩汇总数量
                    BigDecimal sumActualQty = instructionSumActualQtyMap.get(instructionVO23.getInstructionId());

                    // 汇总数量是否大于0
                    boolean isBigThanZero = BigDecimal.ZERO.compareTo(sumActualQty) < 0;

                    // 当前指令是否有明细
                    boolean isHasNoDetail = true;
                    if (MapUtils.isNotEmpty(instructionDetailMap)) {
                        List<MtInstructionActualDetailVO2> detailList =
                                        instructionDetailMap.get(instructionVO23.getInstructionId());
                        if (CollectionUtils.isNotEmpty(detailList)) {
                            isHasNoDetail = false;
                        }
                    }

                    if (isBigThanZero && isHasNoDetail
                                    && CollectionUtils.isEmpty(instructionVO23.getMaterialMessageList())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "materialMessageList",
                                                        "【API:instructionBatchExecute】"));
                    }

                    if (!isHasNoDetail && CollectionUtils.isEmpty(instructionVO23.getMaterialLotMessageList())) {
                        throw new MtException("MT_INSTRUCTION_0001",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INSTRUCTION_0001", "INSTRUCTION", "materialLotMessageList",
                                                        "【API:instructionBatchExecute】"));
                    }
                }

                String mPrimaryUomId = materialUomMap.get(instructionVO23.getMaterialId());
                if (StringUtils.isEmpty(mPrimaryUomId)) {
                    throw new MtException("MT_INSTRUCTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_INSTRUCTION_0013", "INSTRUCTION", "【API:instructionBatchExecute】"));
                }

                // 获取查询的指令信息
                MtInstruction mtInstruction = mtInstructionMap.get(instructionVO23.getInstructionId());

                // 根据指令类型，获取对应生成的事件ID
                String eventId = instructionTypeEventMap.get(mtInstruction.getInstructionType());

                MtInstructionActualVO4 mtInstructionActualVO4 = new MtInstructionActualVO4();
                mtInstructionActualVO4.setInstructionId(mtInstruction.getInstructionId());
                mtInstructionActualVO4.setInstructionType(mtInstruction.getInstructionType());
                mtInstructionActualVO4.setBusinessType(mtInstruction.getBusinessType());
                mtInstructionActualVO4.setMaterialId(mtInstruction.getMaterialId());
                mtInstructionActualVO4.setUomId(mPrimaryUomId);
                mtInstructionActualVO4.setSourceOrderType(mtInstruction.getSourceOrderType());
                mtInstructionActualVO4.setSourceOrderId(mtInstruction.getSourceOrderId());
                mtInstructionActualVO4.setFromSiteId(mtInstruction.getFromSiteId());
                mtInstructionActualVO4.setToSiteId(mtInstruction.getToSiteId());
                mtInstructionActualVO4.setCostCenterId(mtInstruction.getCostCenterId());
                mtInstructionActualVO4.setSupplierId(mtInstruction.getSupplierId());
                mtInstructionActualVO4.setSupplierSiteId(mtInstruction.getSupplierSiteId());
                mtInstructionActualVO4.setCustomerId(mtInstruction.getCustomerId());
                mtInstructionActualVO4.setCustomerSiteId(mtInstruction.getCustomerSiteId());
                mtInstructionActualVO4.setFromOwnerType(mtInstruction.getFromOwnerType());
                mtInstructionActualVO4.setToOwnerType(mtInstruction.getToOwnerType());
                mtInstructionActualVO4.setEventId(eventId);

                // 获取输入参数列表
                List<MtInstructionVO20.MaterialLotMessage> materialLotMessageList =
                                instructionVO23.getMaterialLotMessageList();
                List<MtInstructionVO20.MaterialMessage> materialMessageList = instructionVO23.getMaterialMessageList();

                if (CollectionUtils.isNotEmpty(materialMessageList)) {
                    // 根据来源库位和目标库位分组汇总数量
                    Map<FromToLocatorTuple, BigDecimal> fromToLocatorActualQtyMap = materialMessageList.stream()
                                    .collect(Collectors.groupingBy(
                                                    t -> new FromToLocatorTuple(
                                                                    StringUtils.isEmpty(t.getFromLocatorId())
                                                                                    ? mtInstruction.getFromLocatorId()
                                                                                    : t.getFromLocatorId(),
                                                                    StringUtils.isEmpty(t.getToLocatorId())
                                                                                    ? mtInstruction.getToLocatorId()
                                                                                    : t.getToLocatorId()),
                                                    CollectorsUtil.summingBigDecimal(
                                                                    c -> BigDecimal.valueOf(c.getQty()))));

                    // 填充执行参数
                    for (Map.Entry<FromToLocatorTuple, BigDecimal> entry : fromToLocatorActualQtyMap.entrySet()) {
                        FromToLocatorTuple entryKey = entry.getKey();
                        mtInstructionActualVO4.setFromLocatorId(entryKey.fromLocatorId);
                        mtInstructionActualVO4.setToLocatorId(entryKey.toLocatorId);
                        mtInstructionActualVO4.setActualQty(entry.getValue().doubleValue());
                        mtInstructionActualVO4s.add(mtInstructionActualVO4);
                    }
                }

                if (CollectionUtils.isNotEmpty(materialLotMessageList)) {
                    // 根据来源库位和目标库位分组汇总数量
                    // 需要单位转换
                    Map<FromToLocatorTuple, BigDecimal> fromToLocatorActualQtyMap =
                                    materialLotMessageList.stream().collect(Collectors.groupingBy(t -> {
                                        // 如果来源库位和目标库位未输入，则取指令对应的来源库位和目标库位
                                        if (StringUtils.isEmpty(t.getFromLocatorId())) {
                                            t.setFromLocatorId(mtInstruction.getFromLocatorId());
                                        }

                                        if (StringUtils.isEmpty(t.getToLocatorId())) {
                                            t.setToLocatorId(mtInstruction.getToLocatorId());
                                        }

                                        // 记录需要创建执行明细数据的参数
                                        MtInstructionVO24 instructionVO24 = new MtInstructionVO24();
                                        instructionVO24.setInstructionId(mtInstruction.getInstructionId());
                                        instructionVO24.setMaterialLotId(t.getMaterialLotId());
                                        instructionVO24.setActualQty(t.getQty());
                                        instructionVO24.setContainerId(t.getContainerId());
                                        instructionVO24.setFromLocatorId(t.getFromLocatorId());
                                        instructionVO24.setToLocatorId(t.getToLocatorId());
                                        instructionVO24.setUomId(t.getUomId());
                                        instructionMaterialLotList.add(instructionVO24);

                                        return new FromToLocatorTuple(t.getFromLocatorId(), t.getToLocatorId());
                                    }, CollectorsUtil.summingBigDecimal(c -> {
                                        // 数量转换
                                        MtUomVO1 mtUomVO1 = new MtUomVO1();
                                        mtUomVO1.setSourceUomId(c.getUomId());
                                        mtUomVO1.setSourceValue(c.getQty());
                                        mtUomVO1.setTargetUomId(mPrimaryUomId);
                                        MtUomVO1 result = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                                        return BigDecimal.valueOf(result.getTargetValue());
                                    })));

                    // 填充执行参数
                    for (Map.Entry<FromToLocatorTuple, BigDecimal> entry : fromToLocatorActualQtyMap.entrySet()) {
                        FromToLocatorTuple entryKey = entry.getKey();
                        mtInstructionActualVO4.setFromLocatorId(entryKey.fromLocatorId);
                        mtInstructionActualVO4.setToLocatorId(entryKey.toLocatorId);
                        mtInstructionActualVO4.setActualQty(entry.getValue().doubleValue());
                        mtInstructionActualVO4s.add(mtInstructionActualVO4);
                    }
                }

                // 记录返回结果
                MtInstructionVO22 mtInstructionVO22 = new MtInstructionVO22();
                mtInstructionVO22.setInstructionId(mtInstruction.getInstructionId());
                mtInstructionVO22.setEventId(eventId);
                resultList.add(mtInstructionVO22);
            }

            // 6. 执行指令实绩批量更新
            List<MtInstructionActualVO5> instructionActualVO5s = mtInstructionActualRepository
                            .instructionActualBatchUpdate(tenantId, mtInstructionActualVO4s);

            // 指令ID-实绩ID关系 Map数据
            Map<String, String> instructionActualMap = instructionActualVO5s.stream()
                            .collect(Collectors.toMap(t -> t.getInstructionId(), t -> t.getActualId()));

            // 指令ID-指令实绩明细 Map数据
            Map<String, MtInstructionActualDetailVO3> actualDetailVO3Map = null;

            if (CollectionUtils.isNotEmpty(instructionMaterialLotList)) {
                List<MtInstructionActualDetailVO4> actualDetailMessageList =
                                new ArrayList<>(instructionMaterialLotList.size());

                // 7. 创建实绩明细记录
                for (MtInstructionVO24 mtInstructionVO24 : instructionMaterialLotList) {
                    // 获取指令对应实绩ID
                    String actualId = instructionActualMap.get(mtInstructionVO24.getInstructionId());

                    MtInstructionActualDetailVO4 actualDetailVO4 = new MtInstructionActualDetailVO4();
                    actualDetailVO4.setActualId(actualId);
                    actualDetailVO4.setActualQty(mtInstructionVO24.getActualQty());
                    actualDetailVO4.setContainerId(mtInstructionVO24.getContainerId());
                    actualDetailVO4.setFromLocatorId(mtInstructionVO24.getFromLocatorId());
                    actualDetailVO4.setToLocatorId(mtInstructionVO24.getToLocatorId());
                    actualDetailVO4.setMaterialLotId(mtInstructionVO24.getMaterialLotId());
                    actualDetailVO4.setUomId(mtInstructionVO24.getUomId());
                    actualDetailMessageList.add(actualDetailVO4);
                }

                // 执行创建实绩明细
                List<MtInstructionActualDetailVO3> instructionActualDetails = mtInstructionActualDetailRepository
                                .instructionActualDetailBatchCreate(tenantId, actualDetailMessageList);
                // 转成Map数据
                actualDetailVO3Map = instructionActualDetails.stream()
                                .collect(Collectors.toMap(t -> t.getActualId(), t -> t));

            }

            // 整理返回结果
            for (MtInstructionVO22 instructionVO22 : resultList) {
                // 获取指令对应实绩ID
                String actualId = instructionActualMap.get(instructionVO22.getInstructionId());

                // 获取实绩对应实绩明细
                if (MapUtils.isNotEmpty(actualDetailVO3Map)) {
                    MtInstructionActualDetailVO3 actualDetailVO3 = actualDetailVO3Map.get(actualId);;
                    if (actualDetailVO3 == null) {
                        actualDetailVO3 = new MtInstructionActualDetailVO3();
                        actualDetailVO3.setActualId(actualId);
                    }

                    instructionVO22.setActualDetail(actualDetailVO3);
                } else {
                    MtInstructionActualDetailVO3 actualDetailVO3 = new MtInstructionActualDetailVO3();
                    actualDetailVO3.setActualId(actualId);
                    instructionVO22.setActualDetail(actualDetailVO3);
                }
            }
        }

        // 处理指令eo不为空的数据
        if (CollectionUtils.isNotEmpty(notEmptyEoInstructionList)) {
            // 9. 批量获取eo信息
            List<String> eoIds = notEmptyEoInstructionList.stream().map(MtInstructionVO23::getEoId).distinct()
                            .collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isEmpty(mtEos) || eoIds.size() != mtEos.size()) {
                throw new MtException("MT_INSTRUCTION_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0006",
                                                "INSTRUCTION", "eo:" + eoIds.toString(),
                                                "【API:instructionBatchExecute】"));
            }

            Optional<MtEo> anyQtyEmpty = mtEos.stream().filter(
                            t -> t.getQty() == null || BigDecimal.valueOf(t.getQty()).compareTo(BigDecimal.ZERO) <= 0)
                            .findAny();
            if (anyQtyEmpty.isPresent()) {
                throw new MtException("MT_INSTRUCTION_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_INSTRUCTION_0035", "INSTRUCTION", "【API:instructionBatchExecute】"));
            }

            // 记录返回数据
            List<MtInstructionVO22> tempResultList = new ArrayList<>();

            // eoId-eo Map
            Map<String, MtEo> mtEoMap = mtEos.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));

            // 执行参数准备
            List<MtInstructionActualVO4> mtInstructionActualVO4s = new ArrayList<>();

            // 赋值执行参数
            for (MtInstructionVO23 instructionVO23 : notEmptyEoInstructionList) {

                // 获取查询的指令信息
                MtInstruction mtInstruction = mtInstructionMap.get(instructionVO23.getInstructionId());

                // 根据指令类型，获取对应生成的事件ID
                String eventId = instructionTypeEventMap.get(mtInstruction.getInstructionType());

                // 获取 eo
                MtEo mtEo = mtEoMap.get(mtInstruction.getEoId());

                MtInstructionActualVO4 mtInstructionActualVO4 = new MtInstructionActualVO4();
                mtInstructionActualVO4.setInstructionId(mtInstruction.getInstructionId());
                mtInstructionActualVO4.setInstructionType(mtInstruction.getInstructionType());
                mtInstructionActualVO4.setBusinessType(mtInstruction.getBusinessType());
                mtInstructionActualVO4.setEoId(mtInstruction.getEoId());
                mtInstructionActualVO4.setSourceOrderType(mtInstruction.getSourceOrderType());
                mtInstructionActualVO4.setSourceOrderId(mtInstruction.getSourceOrderId());
                mtInstructionActualVO4.setFromSiteId(mtInstruction.getFromSiteId());
                mtInstructionActualVO4.setToSiteId(mtInstruction.getToSiteId());
                mtInstructionActualVO4.setFromLocatorId(mtInstruction.getFromLocatorId());
                mtInstructionActualVO4.setToLocatorId(mtInstruction.getToLocatorId());
                mtInstructionActualVO4.setFromOwnerType(mtInstruction.getFromOwnerType());
                mtInstructionActualVO4.setToOwnerType(mtInstruction.getToOwnerType());
                mtInstructionActualVO4.setActualQty(mtEo.getQty());
                mtInstructionActualVO4.setEventId(eventId);
                mtInstructionActualVO4s.add(mtInstructionActualVO4);

                // 记录返回结果
                MtInstructionVO22 mtInstructionVO22 = new MtInstructionVO22();
                mtInstructionVO22.setInstructionId(mtInstruction.getInstructionId());
                mtInstructionVO22.setEventId(eventId);
                tempResultList.add(mtInstructionVO22);
            }

            // 6. 执行指令实绩批量更新
            List<MtInstructionActualVO5> instructionActualVO5s = mtInstructionActualRepository
                            .instructionActualBatchUpdate(tenantId, mtInstructionActualVO4s);

            // 指令ID-实绩ID关系 Map数据
            Map<String, String> instructionActualMap = instructionActualVO5s.stream()
                            .collect(Collectors.toMap(t -> t.getInstructionId(), t -> t.getActualId()));


            // 整理返回结果
            for (MtInstructionVO22 instructionVO22 : tempResultList) {
                // 获取指令对应实绩ID
                String actualId = instructionActualMap.get(instructionVO22.getInstructionId());

                // 获取实绩对应实绩明细
                MtInstructionActualDetailVO3 actualDetailVO3 = new MtInstructionActualDetailVO3();
                actualDetailVO3.setActualId(actualId);
                instructionVO22.setActualDetail(actualDetailVO3);
            }

            resultList.addAll(tempResultList);
        }

        return resultList;
    }

    /**
     * 来源库位目标库位分组依据-内部类
     *
     * @author chuang.yang
     * @date 2020/1/14
     * @return
     */
    private static class FromToLocatorTuple {
        private String fromLocatorId;
        private String toLocatorId;

        public FromToLocatorTuple(String fromLocatorId, String toLocatorId) {
            this.fromLocatorId = fromLocatorId;
            this.toLocatorId = toLocatorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            FromToLocatorTuple that = (FromToLocatorTuple) o;
            return Objects.equals(fromLocatorId, that.fromLocatorId) && Objects.equals(toLocatorId, that.toLocatorId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fromLocatorId, toLocatorId);
        }
    }
}
