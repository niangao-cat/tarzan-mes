package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.app.service.ItfSrmMaterialWasteIfaceService;
import com.ruike.itf.domain.repository.ItfSrmMaterialWasteIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.entity.QmsQcDocMaterialLotRel;
import com.ruike.qms.domain.repository.QmsIqcHeaderRepository;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsQcDocMaterialLotRelMapper;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.WmsMaterialPostingRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsPoDeliveryRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsMaterialPostingMapper;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import com.ruike.wms.infra.mapper.WmsPutInStorageTaskMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * WmsMaterialPostingRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/06/13 15:47
 */
@Component
@Slf4j
public class WmsMaterialPostingRepositoryImpl implements WmsMaterialPostingRepository {

    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private WmsMaterialPostingMapper wmsMaterialPostingMapper;
    @Autowired
    private WmsPutInStorageTaskMapper wmsPutInStorageTaskMapper;

    @Autowired
    private QmsIqcHeaderRepository qmsIqcHeaderRepository;
    @Autowired
    private WmsPoDeliveryRepository wmsPoDeliveryRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private ItfSrmMaterialWasteIfaceRepository itfSrmMaterialWasteIfaceRepository;

    @Autowired
    private ItfSrmMaterialWasteIfaceService itfSrmMaterialWasteIfaceService;

    @Autowired
    private QmsQcDocMaterialLotRelMapper qmsQcDocMaterialLotRelMapper;


    @Override
    public Page<WmsInstructionLineVO> materialPostingQuery(Long tenantId, WmsMaterialPostingVO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> wmsMaterialPostingMapper.selectInstructionByCondition(tenantId, dto));
    }

    @Override
    public List<WmsInstructionLineVO> selectTransInstructionByIdList(Long tenantId, List<String> idList) {
        return wmsMaterialPostingMapper.selectTransInstructionByIdList(tenantId, idList);
    }

    @Override
    public List<WmsDeliveryPoRelVo> selectPoByDeliveryId(Long tenantId, String deliveryId) {
        return wmsMaterialPostingMapper.selectPoByDeliveryId(tenantId, deliveryId);
    }

    @Override
    public Map<String, List<WmsDeliveryPoRelVo>> selectPoByDeliveryIdList(Long tenantId, List<String> idList) {
        List<WmsDeliveryPoRelVo> list = wmsMaterialPostingMapper.selectPoByDeliveryIdList(tenantId, idList);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>(0);
        }
        return list.stream().collect(Collectors.groupingBy(WmsDeliveryPoRelVo::getDeliveryId));
    }

    @Override
    @ProcessLovValue
    public List<WmsMaterialLotLineVO> detailQuery(Long tenantId, String instructionId) {
        return wmsMaterialPostingMapper
                .selectMaterialLotByInstructionId(tenantId, instructionId);
    }

    /**
     * ????????????
     *
     * @param tenantId      ??????
     * @param line          ?????????
     * @param materialSumVO ??????
     * @param actualVO      ??????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/17 02:28:57
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<QmsQcDocMaterialLotRel> processValidate(Long tenantId, WmsInstructionLineVO line, WmsMaterialLotLineVO materialSumVO, WmsMaterialLotLineVO actualVO) {
        List<QmsQcDocMaterialLotRel> qmsQcDocMaterialLotRelList = new ArrayList<>();
        //?????????????????????
        BigDecimal secondQty = null;
        // ???????????? ??????????????????
        BigDecimal secondMaterialLotQty = null;
        // ?????????????????????N????????????????????????
        if (WmsConstant.CONSTANT_N.equals(line.getExemptionFlag())) {
            QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectByPrimaryKey(line.getIqcHeaderId());
            String inspectionStatus = qmsIqcHeader != null ? qmsIqcHeader.getInspectionStatus() : "";
            if (!WmsConstant.InstructionStatus.COMPLETED.equals(inspectionStatus)) {
                // ??????????????????????????????????????????????????????
                throw new MtException("WMS_MATERIAL_POSTING_002", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_002", "WMS"));
            } else {
                if (HmeConstants.ConstantValue.NG.equals(line.getInspectionResult())) {
                    if (QmsConstants.FinalDecision.TH.equals(line.getFinalDecision())) {
                        // ????????????????????????????????????????????????????????????????????????
                        throw new MtException("WMS_MATERIAL_POSTING_003", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_003", "WMS"));
                    }

                    if (QmsConstants.FinalDecision.TX.equals(line.getFinalDecision())) {
                        // ?????????????????????
                        QmsIqcHeader iqcCondition = new QmsIqcHeader();
                        iqcCondition.setTenantId(tenantId);
                        iqcCondition.setDocHeaderId(line.getIqcHeaderId());
                        iqcCondition.setInspectionType("SECOND_INSPECTION");
                        QmsIqcHeader qmsIqcHeaderSecond = qmsIqcHeaderRepository.selectOne(iqcCondition);

                        if (!WmsConstant.InstructionStatus.COMPLETED.equals(qmsIqcHeaderSecond.getInspectionStatus())) {
                            // ????????????????????????????????????????????????????????????
                            throw new MtException("WMS_MATERIAL_POSTING_004",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_MATERIAL_POSTING_004", "WMS"));
                        } else {
                            // ????????????????????????NG ????????????
                            if (HmeConstants.ConstantValue.NG.equals(qmsIqcHeaderSecond.getInspectionResult()) && QmsConstants.FinalDecision.TH.equals(qmsIqcHeaderSecond.getFinalDecision())) {
                                // ??????????????????????????????????????????????????????????????????????????????
                                throw new MtException("WMS_MATERIAL_POSTING_005",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "WMS_MATERIAL_POSTING_005", "WMS"));
                            }
                        }
                        secondQty = qmsIqcHeaderSecond.getQuantity() != null ? qmsIqcHeaderSecond.getQuantity() : BigDecimal.ZERO;
                        // ?????????????????????ID???????????????????????? ?????????????????????
                        List<QmsQcDocMaterialLotRel> materialLotRelList = qmsQcDocMaterialLotRelMapper.querySecondMaterialLot(tenantId, line.getIqcHeaderId());
                        Double materialLotQuantity = materialLotRelList.stream().map(QmsQcDocMaterialLotRel::getQuantity).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        secondMaterialLotQty = materialLotQuantity != null ? BigDecimal.valueOf(materialLotQuantity) : BigDecimal.ZERO;
                        qmsQcDocMaterialLotRelList = materialLotRelList;
                    }
                }
            }
        }

        // ??????????????????????????????????????????(?????????????????????, ????????????)
        MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(line.getInstructionId());
        String instructionStatus = mtInstruction != null ? mtInstruction.getInstructionStatus() : "";
        if (!WmsConstant.InstructionStatus.COMPLETED.equals(instructionStatus)
                || !WmsConstant.InstructionStatus.RELEASED.equals(line.getTransOverInstructionStatus())) {
            // ???????????????????????????????????????
            throw new MtException("WMS_MATERIAL_POSTING_006", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_006", "WMS"));
        }
        // ??????????????? ???????????????????????????????????????
        BigDecimal materialPrimaryUomQty;
        if (secondMaterialLotQty != null) {
            materialPrimaryUomQty = secondMaterialLotQty;
        } else {
            materialPrimaryUomQty = Optional.ofNullable(materialSumVO.getPrimaryUomQty()).orElse(BigDecimal.ZERO);
        }
        BigDecimal exchangedQty = Optional.ofNullable(actualVO.getExchangedQty()).orElse(BigDecimal.ZERO);

        //?????????????????????????????????+????????????;???????????????????????????????????????
        BigDecimal actualQty;
        if (secondQty != null) {
            actualQty = secondQty;
        } else {
            List<MtInstructionActual> actualList = mtInstructionActualRepository.select(new MtInstructionActual() {{
                setTenantId(tenantId);
                setInstructionId(mtInstruction.getInstructionId());
            }});
            Double sum = actualList.stream().map(MtInstructionActual::getActualQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
            BigDecimal actualReceiveQty = sum != null ? BigDecimal.valueOf(sum) : BigDecimal.ZERO;
            actualQty = actualReceiveQty.add(exchangedQty);
        }

        //??????NG??????
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_instruction_attr");
        extendVO.setAttrName("NG_QTY");
        extendVO.setKeyId(line.getInstructionId());
        // ??????????????????????????????
        List<MtExtendAttrVO> attrValueList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
        BigDecimal ngQty = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(attrValueList)) {
            if (StringUtils.isNotBlank(attrValueList.get(0).getAttrValue())) {
                ngQty = BigDecimal.valueOf(Double.valueOf(attrValueList.get(0).getAttrValue()));
            }
        }

        if (materialPrimaryUomQty.compareTo(actualQty.subtract(ngQty)) != 0) {
            // ???????????????????????????????????????????????????????????????
            throw new MtException("WMS_MATERIAL_POSTING_007", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_007", "WMS"));
        }
        return qmsQcDocMaterialLotRelList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsMaterialPostingEventVO createEvent(Long tenantId, String locatorId) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_RECEIPT");
        // ????????????????????????
        String exchangeOutEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(eventRequestId);
                setLocatorId(locatorId);
                setEventTypeCode("SUPPLIER_EXCHANGE_RECE_OUT");
            }
        });
        String exchangeInEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
            {
                setEventRequestId(eventRequestId);
                setParentEventId(exchangeOutEventId);
                setLocatorId(locatorId);
                setEventTypeCode("SUPPLIER_EXCHANGE_RECE_IN");
            }
        });
        return new WmsMaterialPostingEventVO(eventRequestId, exchangeOutEventId, exchangeInEventId);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    @ProcessLovValue
    public List<WmsInstructionLineVO> executePosting(Long tenantId, List<WmsInstructionLineVO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            // 	?????????????????????????????????
            throw new MtException("WMS_MATERIAL_POSTING_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_001", "WMS"));
        }
        List<WmsObjectTransactionResponseVO> responseVOArrayList = new ArrayList<>();

        Map<String, WmsTransactionTypeDTO> trxMap = wmsTransactionTypeRepository.getAllTransactionType(tenantId);

        // ??????????????????????????? TRANSFER_OVER_LOCATOR ?????????
        List<String> idList = dtoList.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList());
        List<WmsInstructionLineVO> tolList = wmsMaterialPostingMapper.selectTolInstructionByRsfIds(tenantId, idList);
        Map<String, String> tolMap = tolList.stream().collect(Collectors.toMap(WmsInstructionLineVO::getTransOverInstructionId, WmsInstructionLineVO::getInstructionId));

        for (WmsInstructionLineVO line : dtoList) {
            // ??????????????????
            WmsMaterialLotLineVO materialSumVO = wmsMaterialPostingMapper.selectMaterialLotQty(tenantId, line.getInstructionId());
            WmsMaterialLotLineVO actualVO = wmsMaterialPostingMapper.selectInstructionActualQty(tenantId, line.getInstructionId());

            // ?????????????????? ?????????????????????????????? ?????? ????????????????????????
            List<QmsQcDocMaterialLotRel> materialLotRelList = this.processValidate(tenantId, line, materialSumVO, actualVO);

            // ??????????????????
            WmsMaterialPostingEventVO eventRequest = createEvent(tenantId, line.getLocatorId());

            Date currentDate = new Date(System.currentTimeMillis());
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            List<WmsMaterialLotLineVO> materialLotVOList;
            if (CollectionUtils.isNotEmpty(materialLotRelList)) {
                List<String> secondMaterialLotIdList = materialLotRelList.stream().map(QmsQcDocMaterialLotRel::getMaterialLotId).collect(Collectors.toList());
                materialLotVOList = wmsMaterialPostingMapper.querySecondMaterialLot(tenantId, secondMaterialLotIdList);
            } else {
                materialLotVOList = wmsMaterialPostingMapper.selectMaterialLotByInstructionId(tenantId, line.getInstructionId());
            }
            Map<String, Double> lineStockInQtyMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            //????????????
            List<WmsExchangeLineVO> exchangeLineList = wmsPutInStorageTaskMapper.queryExchangeInstructionWithSite(
                    tenantId, line.getSupplierId(), line.getMaterialId(), line.getMaterialVersion(), line.getSiteId());
            //????????????????????????????????????????????????
            exchangeLineList = exchangeLineList.stream().filter(dto -> dto.getActualQty().compareTo(dto.getExecuteQty()) != 0).collect(Collectors.toList());
            double exchangeActualQty = exchangeLineList.stream().map(WmsExchangeLineVO::getActualQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
            double exchangeExecuteQty = exchangeLineList.stream().map(WmsExchangeLineVO::getExecuteQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
            BigDecimal subNum = BigDecimal.valueOf(exchangeExecuteQty).subtract(BigDecimal.valueOf(exchangeActualQty));
            if(line.getExchangedQty().compareTo(subNum) > 0){
                throw new MtException("WMS_MATERIAL_POSTING_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_MATERIAL_POSTING_010", "WMS", line.getInstructionDocNum(), subNum.toString()));
            }
            for (WmsMaterialLotLineVO materialLine : materialLotVOList) {
                // ??????????????????Map
                Map<String, BigDecimal> poRelMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

                //???????????????????????????
                actualVO = wmsMaterialPostingMapper.selectInstructionActualQty(tenantId, line.getInstructionId());

                // ??????????????????????????????????????????????????????????????????
                BigDecimal materialLotRemainQty = materialLine.getPrimaryUomQty();
                if (WmsConstant.DocType.DELIVERY_DOC.equals(line.getInstructionDocType()) || WmsConstant.DocType.SRM_SUPP_EXCH_DOC.equals(line.getInstructionDocType()) || WmsConstant.DocType.OUTSOURCING_DELIVERY_DOC.equals(line.getInstructionDocType())) {
                    // ???????????????DELIVERY_DOC-?????????
                    if (actualVO.getStockInExchangedQty().compareTo(line.getExchangedQty()) < 0) {
                        if (CollectionUtils.isEmpty(exchangeLineList)) {
                            // ?????????????????????????????????code?????????????????????????????????????????????????????????????????????
                            MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(line.getSupplierId());
                            throw new MtException("WMS_MATERIAL_POSTING_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_MATERIAL_POSTING_008", "WMS", mtSupplier.getSupplierCode(), line.getMaterialCode()));
                        } else {
                            // ????????????
                            BigDecimal transactionQty, exchangeRemainQty = BigDecimal.valueOf(-1.0);

                            List<MtExtendAttrVO1> materialLotAttrVO = mtExtendSettingsRepository.attrPropertyBatchQuery(
                                    tenantId, new MtExtendVO1("mt_material_lot_attr", Collections.singletonList(materialLine.getMaterialLotId()), "ACTUAL_LOCATOR"));
                            Optional<MtExtendAttrVO1> actualLocatorAttr = materialLotAttrVO.stream().filter(
                                    mtExtendAttr -> "ACTUAL_LOCATOR".equals(mtExtendAttr.getAttrName())).findFirst();
                            if (actualLocatorAttr.isPresent()) {
                                List<MtModLocator> locatorList = mtModLocatorRepository.select(new MtModLocator() {{
                                    setTenantId(tenantId);
                                    setLocatorCode(actualLocatorAttr.get().getAttrValue());
                                }});
                                if (CollectionUtils.isNotEmpty(locatorList)) {
                                    materialLine.setActualLocatorId(locatorList.get(0).getLocatorId());
                                }
                            }
                            for (WmsExchangeLineVO exchangeLine : exchangeLineList) {

                                //????????????????????????????????????????????? ???????????????
                                if (exchangeRemainQty.compareTo(BigDecimal.ZERO) == 0) {
                                    break;
                                }
                                //?????????????????????????????????
                                MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(exchangeLine.getInstructionId());
                                if(StringUtils.equals(mtInstruction.getInstructionStatus(), WmsConstant.InstructionStatus.COMPLETED)){
                                    continue;
                                }
                                // 1.1.1??????????????????????????????????????????????????????????????????
                                List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, exchangeLine.getInstructionId());
                                BigDecimal actualQty = BigDecimal.ZERO;
                                if (CollectionUtils.isNotEmpty(mtInstructionActualList)) {
                                    actualQty = mtInstructionActualList.stream().map(rec -> BigDecimal.valueOf(rec.getActualQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
                                }
                                exchangeLine.setActualQty(actualQty);

                                //2020/9/9 add by sanfeng.zhang ????????????????????????????????? ?????? ???????????????
                                BigDecimal executeQty = BigDecimal.ZERO;
                                MtExtendSettings reworkAttr = new MtExtendSettings();
                                reworkAttr.setAttrName("EXECUTE_QTY");
                                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsMapper.attrPropertyQuery(
                                        tenantId, "mt_instruction_attr", "INSTRUCTION_ID",
                                        exchangeLine.getInstructionId(), Collections.singletonList(reworkAttr));

                                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                                    executeQty = BigDecimal.valueOf(Double.valueOf(mtExtendAttrVOS.get(0).getAttrValue()));
                                }
                                // ?????????????????????????????????-???????????????????????????>=??????????????????
                                if ((line.getExchangedQty().subtract(actualVO.getStockInExchangedQty())).compareTo(materialLine.getPrimaryUomQty()) >= 0) {

                                    if (exchangeRemainQty.compareTo(BigDecimal.ZERO) <= 0) {
                                        if (exchangeRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                            exchangeRemainQty = materialLine.getPrimaryUomQty();
                                        } else {
                                            break;
                                        }
                                    }

                                    materialLotRemainQty = BigDecimal.ZERO;

                                    // ?????????????????????????????????-?????????????????????????????????>=????????????
                                    if (executeQty.subtract(exchangeLine.getActualQty()).compareTo(exchangeRemainQty) >= 0) {
                                        // ????????????=??????????????????
                                        transactionQty = exchangeRemainQty;
                                        exchangeRemainQty = BigDecimal.ZERO;
                                    } else {
                                        transactionQty = executeQty.subtract(exchangeLine.getActualQty());
                                        exchangeRemainQty = exchangeRemainQty.subtract(transactionQty);
                                        if (exchangeRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                            exchangeRemainQty = BigDecimal.ZERO;
                                        }
                                    }

                                    // ???????????????????????????
                                    WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                                    objectTransactionVO.setEventId(eventRequest.getExchangeInEventId());
                                    objectTransactionVO.setTransactionTypeCode("WMS_WAREHOUSE_TRAN");
                                    objectTransactionVO.setMoveType(trxMap.get("WMS_WAREHOUSE_TRAN").getMoveType());
                                    objectTransactionVO.setTransactionTime(currentDate);
                                    objectTransactionVO.setTransferLot(materialLine.getLot());
                                    objectTransactionVO.setLotCode("20100101");

                                    List<MtModLocator> locatorList = wmsPutInStorageTaskMapper.queryLocatorWithSite(tenantId, line.getSiteId(), "26");
                                    if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
                                        throw new MtException("WMS_INV_TRANSFER_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "WMS_INV_TRANSFER_0038", "WMS"));
                                    }
                                    objectTransactionVO.setLocatorId(locatorList.get(0).getLocatorId());

                                    //??????toLocatorId??????????????????
                                    objectTransactionVO.setWarehouseId(locatorList.get(0).getParentLocatorId());

                                    objectTransactionVO.setTransactionQty(transactionQty);
                                    objectTransactionVO.setSourceDocId(exchangeLine.getInstructionDocId());
                                    objectTransactionVO.setSourceDocLineId(exchangeLine.getInstructionId());
                                    objectTransactionVO.setSourceDocNum(exchangeLine.getInstructionDocNum());
                                    objectTransactionVO.setSourceDocLineNum(exchangeLine.getInstructionNum());
                                    objectTransactionVO.setSourceDocType(exchangeLine.getInstructionDocType());
                                    objectTransactionVO.setTransactionReasonCode("????????????????????????");
                                    // ??????????????????
                                    MtModLocator locator = new MtModLocator();
                                    locator.setLocatorId(exchangeLine.getLocatorId());
                                    locator = mtModLocatorRepository.selectByPrimaryKey(locator);
                                    if (Objects.nonNull(locator)) {
                                        materialLine.setExchangeLocatorId(exchangeLine.getLocatorId());
                                        materialLine.setExchangeLocatorCode(locator.getLocatorCode());
                                    }
                                    // ??????????????????
                                    MtModLocator transLocator = new MtModLocator();
                                    transLocator.setLocatorId(materialLine.getTransferLocatorId());
                                    transLocator = mtModLocatorRepository.selectByPrimaryKey(transLocator);
                                    if (Objects.nonNull(transLocator)) {
                                        materialLine.setTransferLocatorCode(transLocator.getLocatorCode());
                                    }

                                    wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO,
                                            materialLine, objectTransactionRequestList);


                                    // ?????????????????????
                                    MtInstructionVO3 executeVO = new MtInstructionVO3();
                                    executeVO.setEventRequestId(eventRequest.getEventRequestId());
                                    executeVO.setInstructionId(exchangeLine.getInstructionId());
                                    // ??????????????????????????????
                                    MtInstructionVO3.MaterialLotList materialLotMessage =
                                            new MtInstructionVO3.MaterialLotList();
                                    materialLotMessage.setMaterialLotId(materialLine.getMaterialLotId());
                                    materialLotMessage.setQty(transactionQty.doubleValue());
                                    materialLotMessage.setUomId(materialLine.getUomId());
                                    materialLotMessage.setContainerId(materialLine.getCurrentContainerId());
                                    materialLotMessage.setFromLocatorId(materialLine.getExchangeLocatorId());
                                    materialLotMessage.setToLocatorId(materialLine.getTransferLocatorId());
                                    List<MtInstructionVO3.MaterialLotList> materialLotMessageList = new ArrayList<>();
                                    materialLotMessageList.add(materialLotMessage);
                                    executeVO.setMaterialLotMessageList(materialLotMessageList);

                                    mtInstructionRepository.instructionExecute(tenantId, executeVO);

                                } else {

                                    // ??????????????????????????????????????????????????????????????????
                                    if (exchangeRemainQty.compareTo(BigDecimal.ZERO) <= 0) {
                                        if (exchangeRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                            exchangeRemainQty = line.getExchangedQty()
                                                    .subtract(actualVO.getStockInExchangedQty());
                                        } else {
                                            break;
                                        }
                                    }

                                    // ?????????????????????????????????????????????????????????2
                                    materialLotRemainQty = materialLine.getPrimaryUomQty().subtract(line.getExchangedQty().subtract(actualVO.getStockInExchangedQty()));

                                    // ?????????????????????????????????-?????????????????????????????????<????????????
                                    if ((executeQty.subtract(exchangeLine.getActualQty()))
                                            .compareTo(exchangeRemainQty) >= 0) {
                                        transactionQty = exchangeRemainQty;
                                        exchangeRemainQty = BigDecimal.ZERO;
                                    } else {
                                        transactionQty = executeQty.subtract(exchangeLine.getActualQty());
                                        exchangeRemainQty = exchangeRemainQty.subtract(transactionQty);
                                        if (exchangeRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                            exchangeRemainQty = BigDecimal.valueOf(0.0);
                                        }
                                    }

                                    // ???????????????????????????
                                    WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                                    objectTransactionVO.setEventId(eventRequest.getExchangeInEventId());
                                    objectTransactionVO.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN);
                                    objectTransactionVO.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN).getMoveType());
                                    objectTransactionVO.setTransactionTime(currentDate);
                                    objectTransactionVO.setTransferLot(materialLine.getLot());
                                    objectTransactionVO.setLotCode("20100101");

                                    List<MtModLocator> locatorList = wmsPutInStorageTaskMapper.queryLocatorWithSite(tenantId, line.getSiteId(), "26");
                                    if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
                                        throw new MtException("WMS_INV_TRANSFER_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "WMS_INV_TRANSFER_0038", "WMS"));
                                    }
                                    objectTransactionVO.setLocatorId(locatorList.get(0).getLocatorId());
                                    //??????toLocatorId??????????????????
                                    objectTransactionVO.setWarehouseId(locatorList.get(0).getParentLocatorId());

                                    objectTransactionVO.setSoNum(materialLine.getSoNum());
                                    objectTransactionVO.setSoLineNum(materialLine.getSoLineNum());
                                    objectTransactionVO.setTransactionQty(transactionQty);
                                    objectTransactionVO.setSourceDocId(exchangeLine.getInstructionDocId());
                                    objectTransactionVO.setSourceDocLineId(exchangeLine.getInstructionId());
                                    objectTransactionVO.setSourceDocNum(exchangeLine.getInstructionDocNum());
                                    objectTransactionVO.setSourceDocLineNum(exchangeLine.getInstructionNum());
                                    objectTransactionVO.setSourceDocType(exchangeLine.getInstructionDocType());
                                    objectTransactionVO.setTransactionReasonCode("????????????????????????");
                                    // ??????????????????
                                    MtModLocator locator = new MtModLocator();
                                    locator.setLocatorId(exchangeLine.getLocatorId());
                                    locator = mtModLocatorRepository.selectByPrimaryKey(locator);
                                    if (Objects.nonNull(locator)) {
                                        materialLine.setExchangeLocatorId(exchangeLine.getLocatorId());
                                        materialLine.setExchangeLocatorCode(locator.getLocatorCode());
                                    }
                                    // ??????????????????
                                    MtModLocator transLocator = new MtModLocator();
                                    transLocator.setLocatorId(materialLine.getTransferLocatorId());
                                    transLocator = mtModLocatorRepository.selectByPrimaryKey(transLocator);
                                    if (Objects.nonNull(transLocator)) {
                                        materialLine.setTransferLocatorCode(transLocator.getLocatorCode());
                                    }

                                    wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO,
                                            materialLine, objectTransactionRequestList);

                                    // ?????????????????????
                                    MtInstructionVO3 executeVO = new MtInstructionVO3();
                                    executeVO.setEventRequestId(eventRequest.getEventRequestId());
                                    executeVO.setInstructionId(exchangeLine.getInstructionId());
                                    // ??????????????????????????????
                                    MtInstructionVO3.MaterialLotList materialLotMessage =
                                            new MtInstructionVO3.MaterialLotList();
                                    materialLotMessage.setMaterialLotId(materialLine.getMaterialLotId());
                                    materialLotMessage.setQty(transactionQty.doubleValue());
                                    materialLotMessage.setUomId(materialLine.getUomId());
                                    materialLotMessage.setContainerId(materialLine.getCurrentContainerId());
                                    materialLotMessage.setFromLocatorId(materialLine.getMaterialLotLocatorId());
                                    materialLotMessage.setToLocatorId(materialLine.getTransferLocatorId());
                                    List<MtInstructionVO3.MaterialLotList> materialLotMessageList = new ArrayList<>();
                                    materialLotMessageList.add(materialLotMessage);
                                    executeVO.setMaterialLotMessageList(materialLotMessageList);

                                    mtInstructionRepository.instructionExecute(tenantId, executeVO);

                                }

                                //????????????????????????
                                List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                                if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS)) {
                                    responseVOArrayList.addAll(wmsObjectTransactionResponseVOS);
                                }
                                objectTransactionRequestList = new ArrayList<>();

                                // ???????????????????????????
                                BigDecimal newActualQty = exchangeLine.getActualQty().add(transactionQty);

                                if (newActualQty.compareTo(exchangeLine.getQuantity()) == 0) {
                                    mtInstructionRepository.instructionComplete(tenantId,
                                            exchangeLine.getInstructionId(), eventRequest.getEventRequestId());

                                    //??????????????????????????? ?????????????????????
                                    MtInstruction mtInstructionObj = mtInstructionRepository.selectByPrimaryKey(exchangeLine.getInstructionId());
                                    List<MtInstruction> instructionList = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                                            .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, mtInstructionObj.getSourceDocId())).build());
                                    Boolean flag = true;

                                    for (MtInstruction instruction : instructionList) {
                                        if (!StringUtils.equals(instruction.getInstructionStatus(), HmeConstants.EoStatus.COMPLETED)) {
                                            flag = false;
                                        }
                                    }

                                    if (flag) {
                                        MtInstructionDoc mtInstructionDoc = mtInstructionDocMapper.selectByPrimaryKey(mtInstructionObj.getSourceDocId());
                                        if (mtInstructionDoc != null) {
                                            mtInstructionDoc.setInstructionDocStatus(HmeConstants.EoStatus.COMPLETED);
                                            mtInstructionDocMapper.updateByPrimaryKeySelective(mtInstructionDoc);
                                        }
                                    }
                                }

                                // ????????????????????????????????????
                                MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                                reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                                reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                                //??????26?????????
                                List<String> locatorList = wmsPoDeliveryRelMapper.queryLocatorBySite(tenantId, line.getSiteId(), "26");
                                if (CollectionUtils.isEmpty(locatorList)) {
                                    throw new MtException("WMS_INV_TRANSFER_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_INV_TRANSFER_0038", "WMS"));
                                }
                                reduceUpdateOnHandVO.setLocatorId(locatorList.get(0));
                                reduceUpdateOnHandVO.setLotCode("20100101");
                                reduceUpdateOnHandVO.setEventId(eventRequest.getExchangeOutEventId());
                                reduceUpdateOnHandVO.setChangeQuantity(transactionQty.doubleValue());
                                reduceUpdateOnHandVO.setOwnerType("SI");
                                reduceUpdateOnHandVO.setOwnerId(line.getSupplierId());

                                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);
                                // ??????SRM??????
                                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
                                if (org.apache.commons.collections.CollectionUtils.isEmpty(lovValueDTOS)) {
                                    throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "??????????????????\n????????????Y???N???Y??????????????????N?????????????????????");
                                }
                                String interfaceFlag = lovValueDTOS.get(0).getMeaning();
                                if ("Y".endsWith(interfaceFlag)) {
                                    String siteId = reduceUpdateOnHandVO.getSiteId();
                                    String materialId = reduceUpdateOnHandVO.getMaterialId();
                                    String locatorId1 = reduceUpdateOnHandVO.getLocatorId();
                                    String ownerId = reduceUpdateOnHandVO.getOwnerId();
                                    List<ItfSrmMaterialWasteIfaceSyncDTO> syncDTOS = itfSrmMaterialWasteIfaceRepository.selectSrmMaterialWaste(tenantId, siteId, materialId, locatorId1, ownerId);
                                    itfSrmMaterialWasteIfaceService.srmMaterialWasteExchangeCreate(syncDTOS, tenantId);

                                }
                                // ?????????????????????????????????
                                MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                                updateOnHandVO.setSiteId(line.getSiteId());
                                updateOnHandVO.setMaterialId(line.getMaterialId());
                                //?????????????????????id(????????????????????????)
                                MtModLocator materialLocator = new MtModLocator();
                                materialLocator.setLocatorCode(materialLine.getActualLocatorCode());
                                materialLocator.setTenantId(tenantId);
                                List<MtModLocator> mtModLocatorList = mtModLocatorRepository.select(materialLocator);
                                if (CollectionUtils.isEmpty(mtModLocatorList)) {
                                    throw new MtException("HME_CONTAINER_IMPORT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_CONTAINER_IMPORT_006", "HME", materialLine.getMaterialLotCode()));
                                }
                                updateOnHandVO.setLocatorId(mtModLocatorList.get(0).getLocatorId());
                                updateOnHandVO.setLotCode(materialLine.getLot());
                                updateOnHandVO.setEventId(eventRequest.getExchangeInEventId());
                                updateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                                //????????????STOCK_IN_EXCHANGEED_QTY
                                MtInstructionActual instructionActual = new MtInstructionActual();
                                instructionActual.setInstructionId(line.getInstructionId());
                                instructionActual.setTenantId(tenantId);
                                List<MtInstructionActual> actualList = mtInstructionActualRepository.select(instructionActual);
                                if (CollectionUtils.isNotEmpty(actualList)) {
                                    MtExtendSettings extendSettings = new MtExtendSettings();
                                    extendSettings.setAttrName("STOCK_IN_EXCHANGEED_QTY");
                                    List<MtExtendAttrVO> attrVOList = mtExtendSettingsMapper.attrPropertyQuery(
                                            tenantId, "mt_instruction_actual_attr", "ACTUAL_ID",
                                            actualList.get(0).getActualId(), Collections.singletonList(extendSettings));

                                    if (CollectionUtils.isNotEmpty(attrVOList)) {
                                        BigDecimal qty = BigDecimal.valueOf(Double.valueOf(attrVOList.get(0).getAttrValue()));

                                        //???????????????????????????????????????api???????????? ?????????????????? ??????????????????????????????
                                        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
                                        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
                                        BigDecimal total = qty.add(transactionQty);
                                        String attrValue = "";
                                        if (total != null) {
                                            attrValue = total.toString();
                                        }
                                        wmsMaterialPostingMapper.updateStockInExchangeedQty(tenantId, actualList.get(0).getActualId(), userId, "STOCK_IN_EXCHANGEED_QTY", attrValue);
                                    } else {
                                        //??????????????? ???????????????
                                        MtExtendVO10 dto = new MtExtendVO10();
                                        dto.setKeyId(actualList.get(0).getActualId());
                                        List<MtExtendVO5> vo5List = new ArrayList<>();
                                        MtExtendVO5 vo5 = new MtExtendVO5();
                                        vo5.setAttrName("STOCK_IN_EXCHANGEED_QTY");
                                        vo5.setAttrValue(transactionQty.toString());
                                        vo5List.add(vo5);
                                        dto.setAttrs(vo5List);
                                        dto.setEventId(eventRequest.getExchangeOutEventId());
                                        mtInstructionActualRepository.instructionActualAttrPropertyUpdate(tenantId, dto);
                                    }

                                }
                            }

                            // ??????????????????????????????????????????
                            MtMaterialLotVO2 mtMaterialLot = new MtMaterialLotVO2();
                            mtMaterialLot.setEventId(eventRequest.getExchangeOutEventId());
                            mtMaterialLot.setMaterialLotId(materialLine.getMaterialLotId());
                            mtMaterialLot.setLocatorId(materialLine.getActualLocatorId());
                            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLot,
                                    HmeConstants.ConstantValue.NO);

                        }

                    }
                }
                // ??????2??? ????????????
                // ????????????????????????
                WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(materialLine.getTransferLocatorId()),
                        "WMS_INV_TRANSFER_0032", "WMS");
                String putInStorageOutEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                    {
                        setEventRequestId(eventRequest.getEventRequestId());
                        setLocatorId(materialLine.getTransferLocatorId());
                        setEventTypeCode("MATERIAL_RECEIPT_OUT");
                    }
                });
                String putInStorageInEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                    {
                        setEventRequestId(eventRequest.getEventRequestId());
                        setParentEventId(putInStorageOutEventId);
                        setLocatorId(materialLine.getTransferLocatorId());
                        setEventTypeCode("MATERIAL_RECEIPT_IN");
                    }
                });
                Double totalStockInQty = lineStockInQtyMap.get(line.getInstructionId());
                if (totalStockInQty == null) {
                    totalStockInQty = 0.0D;
                }
                BigDecimal materialLotRemainQty1 = materialLotRemainQty;
                //?????????????????????  ??????????????????
                if (materialLotRemainQty.compareTo(BigDecimal.ZERO) > 0) {

                    // 1.2.2)????????????????????????????????????
                    MtInstructionVO3 executeVO = new MtInstructionVO3();
                    executeVO.setEventRequestId(eventRequest.getEventRequestId());
                    executeVO.setInstructionId(line.getTransOverInstructionId());
                    // ??????????????????????????????
                    MtInstructionVO3.MaterialLotList materialLotMessage = new MtInstructionVO3.MaterialLotList();
                    materialLotMessage.setMaterialLotId(materialLine.getMaterialLotId());
                    materialLotMessage.setQty(materialLotRemainQty.doubleValue());
                    materialLotMessage.setUomId(materialLine.getUomId());
                    materialLotMessage.setFromLocatorId(materialLine.getMaterialLotLocatorId());
                    materialLotMessage.setToLocatorId(materialLine.getTransferLocatorId());
                    materialLotMessage.setContainerId(materialLine.getCurrentContainerId());
                    List<MtInstructionVO3.MaterialLotList> materialLotMessageList = new ArrayList<>();
                    materialLotMessageList.add(materialLotMessage);
                    executeVO.setMaterialLotMessageList(materialLotMessageList);

                    mtInstructionRepository.instructionExecute(tenantId, executeVO);

                    // 1.2.3)????????????????????????????????????????????????
                    // 1.2.3.1)?????????????????????????????????????????????????????????
                    List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository
                            .instructionLimitActualPropertyGet(tenantId, line.getTransOverInstructionId());
                    if (CollectionUtils.isEmpty(mtInstructionActualList)) {
                        // ?????????????????????????????????????????????????????????
                        throw new MtException("WMS_MATERIAL_POSTING_009", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_009", "WMS"));
                    }
                    BigDecimal actualQty = BigDecimal.ZERO;
                    for (MtInstructionActual mtInstructionActual : mtInstructionActualList) {
                        actualQty = actualQty.add(BigDecimal.valueOf(mtInstructionActual.getActualQty()));
                    }
                    // 1.2.3.2)?????????????????????????????????????????????
                    if (line.getActualReceiveQty().equals(actualQty)) {
                        mtInstructionRepository.instructionComplete(tenantId, line.getTransOverInstructionId(),
                                eventRequest.getEventRequestId());
                    }

                    // 1.2.4)??????????????????????????????????????????????????????????????????????????????????????????
                    List<WmsPutInStorageVO> poLineList = wmsPutInStorageTaskMapper.queryPoLinesByInstructionId(tenantId,
                            line.getInstructionDocId(), line.getInstructionId());
                    List<WmsPutInStorageVO> poDistList = new ArrayList<>();
                    Boolean flag = false;
                    for (WmsPutInStorageVO poLine : poLineList) {
                        // ??????????????????????????????????????????????????????????????????
                        if (materialLotRemainQty.compareTo(BigDecimal.ZERO) == 0) {
                            break;
                        }

                        if (poLine.getQuantity() - poLine.getPoStockInQty() > 0) {
                            poLine.setOrderRemainQty(poLine.getQuantity() - poLine.getPoStockInQty());
                            BigDecimal transactionQty;
                            if (materialLotRemainQty.compareTo(BigDecimal.valueOf(poLine.getOrderRemainQty())) <= 0) {
                                transactionQty = materialLotRemainQty;
                                materialLotRemainQty = BigDecimal.ZERO;
                            } else {
                                transactionQty = BigDecimal.valueOf(poLine.getOrderRemainQty());
                                materialLotRemainQty = materialLotRemainQty.subtract(transactionQty);
                                if (materialLotRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                    materialLotRemainQty = BigDecimal.ZERO;
                                }
                            }

                            // ????????????????????????????????????
                            MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                            reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                            reduceUpdateOnHandVO.setMaterialId(materialLine.getMaterialId());
                            reduceUpdateOnHandVO.setLocatorId(materialLine.getMaterialLotLocatorId());
                            reduceUpdateOnHandVO.setLotCode(materialLine.getLot());
                            reduceUpdateOnHandVO.setEventId(putInStorageOutEventId);
                            reduceUpdateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                            // ?????????????????????????????????????????????
                            MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                            updateOnHandVO.setSiteId(line.getSiteId());
                            updateOnHandVO.setMaterialId(materialLine.getMaterialId());
                            updateOnHandVO.setLocatorId(materialLine.getTransferLocatorId());
                            updateOnHandVO.setLotCode(materialLine.getLot());
                            updateOnHandVO.setEventId(putInStorageInEventId);
                            updateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                            flag = true;

                            // ??????po?????????
                            BigDecimal poStockInQty = BigDecimal.valueOf(poLine.getPoStockInQty()).add(transactionQty);

                            BigDecimal existsQty = poRelMap.get(poLine.getPoDeliveryRelId());
                            if (Objects.isNull(existsQty)) {
                                poRelMap.put(poLine.getPoDeliveryRelId(), poStockInQty);
                            } else {
                                poRelMap.put(poLine.getPoDeliveryRelId(), existsQty.add(poStockInQty));
                            }
                            WmsPutInStorageVO poDist = new WmsPutInStorageVO();
                            poDist.setPoId(poLine.getPoId());
                            poDist.setPoLineId(poLine.getPoLineId());
                            poDist.setPoDistQty(transactionQty);
                            poDistList.add(poDist);
                            BigDecimal newStockInQty = line.getStockInQty().add(transactionQty);

                            totalStockInQty += newStockInQty.doubleValue();
                        }
                    }

                    if (flag) {
                        // ??????????????????
                        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                            {
                                setEventId(putInStorageOutEventId);
                                setMaterialLotId(materialLine.getMaterialLotId());
                                setLocatorId(materialLine.getTransferLocatorId()); // ????????????
                                setInLocatorTime(currentDate);
                            }
                        }, "N");
                    }

                    // ??????po????????????
                    poRelMap.forEach((poDeliveryRelId, poInStockQty) -> {
                        WmsPoDeliveryRel poDeliveryRel = wmsPoDeliveryRepository.selectByPrimaryKey(poDeliveryRelId);
                        poDeliveryRel.setPoStockInQty(poInStockQty);
                        wmsPoDeliveryRepository.updateByPrimaryKey(poDeliveryRel);
                    });

                    // 1.2.5)??????????????????????????????
                    // ????????????????????????????????????????????????????????????
                    WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                    objectTransactionVO.setEventId(putInStorageInEventId);
                    objectTransactionVO.setTransactionTime(currentDate);

                    switch (line.getInstructionDocType()) {
                        case WmsConstant.DocType.DELIVERY_DOC:
                            objectTransactionVO.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_STOCK_IN);
                            objectTransactionVO.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_STOCK_IN).getMoveType());
                            objectTransactionVO.setTransactionReasonCode("????????????????????????");
                            break;
                        case WmsConstant.DocType.OUTSOURCING_DELIVERY_DOC:
                            objectTransactionVO.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_STOCK_IN);
                            objectTransactionVO.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_STOCK_IN).getMoveType());
                            objectTransactionVO.setTransactionReasonCode("???????????????????????????????????????");
                            break;
                        default:
                            break;
                    }

                    objectTransactionVO.setSourceDocId(line.getInstructionDocId());
                    objectTransactionVO.setSourceDocLineId(line.getInstructionId());
                    objectTransactionVO.setSourceDocNum(line.getInstructionDocNum());
                    objectTransactionVO.setSourceDocLineNum(line.getInstructionNum());
                    objectTransactionVO.setSourceDocType(line.getInstructionDocType());
                    objectTransactionVO.setTransferLot(materialLine.getLot());
                    objectTransactionVO.setWarehouseId(line.getWarehouseId());

                    //?????????????????????id(????????????????????????)
                    MtModLocator mtModLocator = new MtModLocator();
                    mtModLocator.setLocatorCode(materialLine.getActualLocatorCode());
                    mtModLocator.setTenantId(tenantId);
                    List<MtModLocator> locatorList = mtModLocatorRepository.select(mtModLocator);
                    if (org.apache.commons.collections4.CollectionUtils.isEmpty(locatorList)) {
                        throw new MtException("HME_CONTAINER_IMPORT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CONTAINER_IMPORT_006", "HME", materialLine.getMaterialLotCode()));
                    }

                    objectTransactionVO.setLocatorId(locatorList.get(0).getLocatorId());
                    objectTransactionVO.setTransferLocatorId(materialLine.getTransferLocatorId());
                    objectTransactionVO.setTransferWarehouseId(materialLine.getTransferWarehouseId());
                    objectTransactionVO.setSoNum(materialLine.getSoNum());
                    objectTransactionVO.setSoLineNum(materialLine.getSoLineNum());

                    // ??????????????????
                    MtModLocator locator = new MtModLocator();
                    locator.setLocatorId(materialLine.getTransferLocatorId());
                    locator = mtModLocatorRepository.selectByPrimaryKey(locator);
                    materialLine.setTransferLocatorCode(locator.getLocatorCode());

                    // ??????PO????????????
                    if (CollectionUtils.isEmpty(poDistList)) {
                        objectTransactionVO.setTransactionQty(materialLotRemainQty);
                        wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO, materialLine,
                                objectTransactionRequestList);
                    } else {
                        for (WmsPutInStorageVO po : poDistList) {
                            WmsDeliveryPoRelVo poNum = wmsMaterialPostingMapper.selectPoByLineId(tenantId, po.getPoLineId());
                            objectTransactionVO.setPoId(po.getPoId());
                            objectTransactionVO.setPoLineId(po.getPoLineId());
                            objectTransactionVO.setPoNum(poNum.getPoNumber());
                            objectTransactionVO.setPoLineNum(poNum.getPoLineNumber());
                            objectTransactionVO.setTransactionQty(po.getPoDistQty());
                            wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO, materialLine,
                                    objectTransactionRequestList);
                        }
                    }

                    //????????????????????????
                    List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

                    if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS)) {
                        responseVOArrayList.addAll(wmsObjectTransactionResponseVOS);
                    }
                    objectTransactionRequestList = new ArrayList<>();

                    // ????????????????????????????????????INSTOCK-?????????
                    List<MtExtendVO5> mtExtendList = new ArrayList<>();
                    MtExtendVO5 statusAttr = new MtExtendVO5();
                    statusAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);
                    statusAttr.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
                    mtExtendList.add(statusAttr);
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                        {
                            setKeyId(materialLine.getMaterialLotId());
                            setEventId(putInStorageOutEventId);
                            setAttrs(mtExtendList);
                        }
                    });
                }else {
                    //?????????????????????????????? ????????????????????????
                    List<MtExtendVO5> mtExtendList = new ArrayList<>();
                    MtExtendVO5 statusAttr = new MtExtendVO5();
                    statusAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);
                    statusAttr.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
                    mtExtendList.add(statusAttr);
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                        {
                            setKeyId(materialLine.getMaterialLotId());
                            setEventId(eventRequest.getExchangeInEventId());
                            setAttrs(mtExtendList);
                        }
                    });
                }
                if (WmsConstant.DocType.DELIVERY_DOC.equals(line.getInstructionDocType()) || WmsConstant.DocType.SRM_SUPP_EXCH_DOC.equals(line.getInstructionDocType())) {
                    // ?????? 1.3)???????????????????????????
                    //2020/9/9 add by sanfeng.zhang ????????????????????????????????????
                    BigDecimal scrapQty = wmsMaterialPostingMapper.queryScrapQty(tenantId, line.getInstructionId(), materialLine.getMaterialLotId());
                    if (BigDecimal.ZERO.compareTo(scrapQty) < 0) {
                        String inspectScrapEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                            {
                                setEventRequestId(eventRequest.getEventRequestId());
                                setLocatorId(materialLine.getTransferLocatorId());
                                setEventTypeCode("INSPECT_SCRAP");
                            }
                        });

                        //????????????????????????????????????soNum???soLineNum???????????????????????????
                        if (StringUtils.isNotBlank(materialLine.getSoNum()) && StringUtils.isNotBlank(materialLine.getSoLineNum())) {
                            //????????????????????????
                            WmsObjectTransactionVO normalTransaction = new WmsObjectTransactionVO();
                            normalTransaction.setEventId(inspectScrapEventId);
                            normalTransaction.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_SO_TO_NORMAL);
                            normalTransaction.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_SO_TO_NORMAL).getMoveType());
                            normalTransaction.setTransactionTime(currentDate);
                            normalTransaction.setTransactionQty(scrapQty);
                            normalTransaction.setTransferLot(materialLine.getLot());
                            normalTransaction.setSourceDocId(line.getInstructionDocId());
                            normalTransaction.setSourceDocLineId(line.getInstructionId());
                            normalTransaction.setSourceDocNum(line.getInstructionDocNum());
                            normalTransaction.setSourceDocLineNum(line.getInstructionNum());
                            normalTransaction.setSourceDocType(line.getInstructionDocType());
                            normalTransaction.setTransactionReasonCode("??????????????????????????????????????????");
                            normalTransaction.setWarehouseId(line.getWarehouseId());
                            normalTransaction.setSoNum(materialLine.getSoNum());
                            normalTransaction.setSoLineNum(materialLine.getSoLineNum());

                            wmsObjectTransactionRepository.addObjectTransaction(tenantId, normalTransaction, materialLine,
                                    objectTransactionRequestList);
                            //???????????????????????????
                            List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS1 = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                            if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS1)) {
                                responseVOArrayList.addAll(wmsObjectTransactionResponseVOS1);
                            }

                            objectTransactionRequestList = new ArrayList<>();
                        }

                        WmsObjectTransactionVO transactionVO = new WmsObjectTransactionVO();
                        transactionVO.setEventId(inspectScrapEventId);
                        transactionVO.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_COST_CENTER_I);
                        transactionVO.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_COST_CENTER_I).getMoveType());
                        transactionVO.setTransactionTime(currentDate);
                        transactionVO.setTransactionQty(scrapQty);
                        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.IQC_SCRAP_COSTCENTER", tenantId);
                        for (LovValueDTO lovValueDTO : lovValueDTOS) {
                            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(line.getSiteId());
                            if (mtModSite != null) {
                                if (StringUtils.equals(lovValueDTO.getValue(), mtModSite.getSiteCode())) {
                                    transactionVO.setCostCenterCode(lovValueDTO.getMeaning());
                                }
                            }
                        }
                        transactionVO.setSourceDocId(line.getInstructionDocId());
                        transactionVO.setSourceDocLineId(line.getInstructionId());
                        transactionVO.setSourceDocNum(line.getInstructionDocNum());
                        transactionVO.setSourceDocLineNum(line.getInstructionNum());
                        transactionVO.setSourceDocType(line.getInstructionDocType());
                        transactionVO.setTransactionReasonCode("????????????????????????");
                        transactionVO.setWarehouseId(line.getWarehouseId());
                        wmsObjectTransactionRepository.addObjectTransaction(tenantId, transactionVO, materialLine,
                                objectTransactionRequestList);
                        //???????????????????????????
                        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS1 = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                        if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS1)) {
                            responseVOArrayList.addAll(wmsObjectTransactionResponseVOS1);
                        }

                        objectTransactionRequestList = new ArrayList<>();

                        // ??????????????????
                        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                            {
                                setEventId(inspectScrapEventId);
                                setMaterialLotId(materialLine.getMaterialLotId());
                                setPrimaryUomQty(materialLine.getPrimaryUomQty()
                                        .subtract(scrapQty).doubleValue());
                            }
                        }, "N");

                        // ????????????????????????????????????
                        MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                        reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                        reduceUpdateOnHandVO.setMaterialId(materialLine.getMaterialId());
                        reduceUpdateOnHandVO.setLocatorId(materialLine.getTransferLocatorId());
                        reduceUpdateOnHandVO.setLotCode(materialLine.getLot());
                        reduceUpdateOnHandVO.setEventId(inspectScrapEventId);
                        reduceUpdateOnHandVO.setChangeQuantity(scrapQty.doubleValue());

                        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);
                    }
                    lineStockInQtyMap.put(line.getInstructionId(), totalStockInQty);
                } else if ("OUTSOURCING_DELIVERY_DOC".equals(line.getInstructionDocType())) {
                    List<WmsMaterialPostingVO3> outsourcingLineList =
                            wmsMaterialPostingMapper.selectOutsourcingByInstructionId(tenantId, line.getInstructionDocId(), line.getInstructionId());

                    for (WmsMaterialPostingVO3 outsourcingLine : outsourcingLineList) {
                        //BomUsage?????? ??????????????????
                        if(StringUtils.isNotBlank(outsourcingLine.getBomUsage()) && BigDecimal.valueOf(Double.valueOf(outsourcingLine.getBomUsage())).compareTo(BigDecimal.ZERO) > 0){
                            // ????????????????????????????????????
                            MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                            reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                            reduceUpdateOnHandVO.setMaterialId(outsourcingLine.getMaterialId());
                            //????????????????????????????????????  ?????????  LOCATOR_TYPE = 20 ,?????????LOCATOR_TYPE???20?????????ID
                            List<MtModLocator> mtModLocatorList = wmsMaterialPostingMapper.queryOutsourceLocator(tenantId, line.getSiteId());
                            if (CollectionUtils.isEmpty(mtModLocatorList) || mtModLocatorList.size() > 1) {
                                throw new MtException("MT_INVENTORY_0037",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0037",
                                                "INVENTORY"));
                            }

                            reduceUpdateOnHandVO.setLocatorId(mtModLocatorList.get(0).getLocatorId());
                            reduceUpdateOnHandVO.setEventId(putInStorageOutEventId);
                            //????????????????????????
                            BigDecimal quantity = BigDecimal.valueOf(1);
                            WmsPoDeliveryRel wmsPoDeliveryRel = new WmsPoDeliveryRel();
                            wmsPoDeliveryRel.setDeliveryDocId(line.getInstructionDocId());
                            wmsPoDeliveryRel.setDeliveryDocLineId(line.getInstructionId());
                            List<WmsPoDeliveryRel> wmsPoDeliveryRels = wmsPoDeliveryRelMapper.select(wmsPoDeliveryRel);
                            if (CollectionUtils.isNotEmpty(wmsPoDeliveryRels)) {
                                MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(wmsPoDeliveryRels.get(0).getPoLineId());

                                if (mtInstruction != null) {
                                    quantity = BigDecimal.valueOf(mtInstruction.getQuantity());
                                }
                            }
                            if (quantity.compareTo(BigDecimal.ZERO) == 0) {
                                quantity = BigDecimal.valueOf(1);
                            }
                            //BigDecimal total = materialLine.getPrimaryUomQty().multiply(BigDecimal.valueOf(Double.valueOf(outsourcingLine.getBomUsage())));
                            BigDecimal total = materialLotRemainQty1.multiply(BigDecimal.valueOf(Double.valueOf(outsourcingLine.getBomUsage())));
                            total = total.divide(quantity, 3, BigDecimal.ROUND_FLOOR);

                            if (total != null) {
                                reduceUpdateOnHandVO.setChangeQuantity(total.doubleValue());
                            }
                            reduceUpdateOnHandVO.setOwnerType("IIS");
                            reduceUpdateOnHandVO.setLotCode("20100101");
                            reduceUpdateOnHandVO.setOwnerId(line.getSupplierId());

                            //??????????????????????????????
                            List<WmsObjectTransactionResponseVO> outSourceTransaction = this.createOutSourceTransaction(tenantId, putInStorageInEventId, outsourcingLine, materialLine, total, mtModLocatorList.get(0), line, trxMap, wmsPoDeliveryRels);
                            if (CollectionUtils.isNotEmpty(outSourceTransaction)) {
                                responseVOArrayList.addAll(outSourceTransaction);
                            }
                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);
                        }
                    }
                }
            }
            //TRANSFER_OVER_LOCATOR????????????????????????????????????
            MtInstruction tolInstruction = new MtInstruction();
            tolInstruction.setInstructionId(tolMap.get(line.getInstructionId()));
            tolInstruction.setInstructionStatus("COMPLETED");
            mtInstructionRepository.updateOptional(tolInstruction, MtInstruction.FIELD_INSTRUCTION_STATUS);

            //???????????????????????????
            List<WmsPutInStorageTask> wmsPutInStorageTaskList = wmsPutInStorageTaskMapper.select(new WmsPutInStorageTask(){{
                setTenantId(tenantId);
                setInstructionDocId(line.getInstructionDocId());
                setInstructionId(line.getInstructionId());
            }});
            if(CollectionUtils.isNotEmpty(wmsPutInStorageTaskList)){
                WmsPutInStorageTask wmsPutInStorageTask = wmsPutInStorageTaskList.get(0);
                wmsPutInStorageTask.setTaskStatus("STOCKED");
                //???????????? ????????????????????????????????????TRANSFER_OVER_LOCATOR?????? ????????????ActualQty
                BigDecimal actualQty = wmsMaterialPostingMapper.queryActualQty(tenantId, line.getInstructionDocId(), line.getMaterialId(), line.getInstructionLineNum());
                BigDecimal exchangedQty = wmsMaterialPostingMapper.queryExchangedQtyByLineId(tenantId, line.getInstructionId());
                //???????????? ????????????????????????????????????
                wmsPutInStorageTask.setExecuteQty(actualQty.add(exchangedQty));
                wmsPutInStorageTaskMapper.updateByPrimaryKeySelective(wmsPutInStorageTask);
            }
        }
        //????????????????????? ?????????????????????STOCK_IN_COMPLETE
        List<String> instructionDocIdList = dtoList.stream().map(WmsInstructionLineVO::getInstructionDocId).distinct().collect(Collectors.toList());
        for (String docId : instructionDocIdList) {
            MtInstructionDoc mtInstructionDoc = mtInstructionDocMapper.selectByPrimaryKey(docId);

            List<MtInstruction> mtInstructionList = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                    .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, mtInstructionDoc.getInstructionDocId())).build());
            if (CollectionUtils.isNotEmpty(mtInstructionList)) {

                boolean flag = mtInstructionList.stream().allMatch(line -> StringUtils.equals(WmsConstant.InstructionStatus.COMPLETED, line.getInstructionStatus()));

                if (flag) {
                    mtInstructionDoc.setInstructionDocStatus(WmsConstant.InstructionStatus.STOCK_IN_COMPLETE);
                    mtInstructionDocMapper.updateByPrimaryKeySelective(mtInstructionDoc);
                }
            }
        }

        List<String> instructionIdList = dtoList.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList());
        WmsMaterialPostingVO resultParams = new WmsMaterialPostingVO();
        resultParams.setInstructionIds(instructionIdList);
        List<WmsInstructionLineVO> wmsInstructionLineVOS = wmsMaterialPostingMapper.selectInstructionByCondition(tenantId, resultParams);

        List<String> lineIds = wmsInstructionLineVOS.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList());
        List<WmsInstructionLineVO> transList = this.selectTransInstructionByIdList(tenantId, lineIds);
        Map<String, WmsInstructionLineVO> transMap = transList.stream().collect(Collectors.toMap(WmsInstructionLineVO::getInstructionId, rec -> rec, (key1, key2) -> key1));
        Map<String, List<WmsDeliveryPoRelVo>> poMap = this.selectPoByDeliveryIdList(tenantId, wmsInstructionLineVOS.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList()));
        wmsInstructionLineVOS.forEach(rec -> {
            rec.setInstructionStatusLov("WMS.DELIVERY_DOC_LINE_RFS.STATUS");
            WmsInstructionLineVO transOverLine = transMap.get(rec.getInstructionId());
            if (Objects.nonNull(transOverLine)) {
                rec.setTransOverInstructionId(transOverLine.getTransOverInstructionId());
                rec.setTransOverInstructionStatus(transOverLine.getTransOverInstructionStatus());
                rec.setTransOverInspectionStatus(transOverLine.getTransOverInspectionStatus());
                if (!WmsConstant.InstructionStatus.RELEASED.equals(rec.getTransOverInstructionStatus())) {
                    rec.setInstructionStatusLov("WMS.DELIVERY_DOC_LINE_TOL.STATUS");
                }
            }
            List<WmsDeliveryPoRelVo> poList = poMap.get(rec.getInstructionId());
            if (CollectionUtils.isNotEmpty(poList)) {
                String poNumber = poList.stream().map(po -> po.getPoNumber() + "-" + po.getPoLineNumber()).collect(Collectors.joining(";"));
                rec.setInstructionStatusMeaning(lovAdapter.queryLovMeaning(rec.getInstructionStatusLov(), tenantId, rec.getInstructionStatus()));
                rec.setPoNumber(poNumber);
            }
        });

        // ??????SAP ??????
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "??????????????????\n????????????Y???N???Y??????????????????N?????????????????????");
        }
        String interfaceFlag = lovValueDTOS.get(0).getMeaning();
        if (YES.equals(interfaceFlag)) {
            if (CollectionUtils.isNotEmpty(responseVOArrayList)) {
                List<String> transactionIdList = responseVOArrayList.stream().map(WmsObjectTransactionResponseVO::getTransactionId).collect(Collectors.toList());
                List<WmsObjectTransaction> wmsObjectTransactions = wmsObjectTransactionRepository.byIds(transactionIdList);
                // ??????
                List<WmsObjectTransaction> newList = new ArrayList<>();

                String[] transactionType = {"311", "101", "543"};
                log.info("?????????????????????:{}", wmsObjectTransactions.size());
                for (String str : transactionType) {
                    for (int i = 0; i < wmsObjectTransactions.size(); i++) {
                        if (str.equals(wmsObjectTransactions.get(i).getMoveType())) {
                            if ("311".equals(str) && Strings.isNotBlank(wmsObjectTransactions.get(i).getSpecStockFlag())) {
                                log.info("311????????????tSpecStockFlag???:{}", wmsObjectTransactions.get(i));
                                continue;
                            }
                            log.info(str + "???{}", wmsObjectTransactions.get(i));
                            newList.add(wmsObjectTransactions.get(i));
                        }
                    }
                }
                itfObjectTransactionIfaceService.processSummary(tenantId, interfaceFlag, newList);
            }
        }
        return wmsInstructionLineVOS;
    }

    private List<WmsObjectTransactionResponseVO> createOutSourceTransaction(Long tenantId, String eventId, WmsMaterialPostingVO3 outsourcingLine, WmsMaterialLotLineVO materialLotLine, BigDecimal total, MtModLocator mtModLocator, WmsInstructionLineVO line, Map<String, WmsTransactionTypeDTO> trxMap, List<WmsPoDeliveryRel> wmsPoDeliveryRels){
        WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
        requestVO.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_OSOURCING_COMP_DEDU);
        requestVO.setEventId(eventId);
        requestVO.setMaterialId(outsourcingLine.getMaterialId());
        requestVO.setTransactionQty(total);
        requestVO.setLotNumber("20100101");
        requestVO.setTransactionUom(outsourcingLine.getUomCode());
        requestVO.setTransactionTime(new Date());
        requestVO.setTransactionReasonCode("????????????????????????????????????");
        requestVO.setPlantId(outsourcingLine.getSiteId());
        requestVO.setWarehouseId(mtModLocator.getParentLocatorId());
        requestVO.setLocatorId(mtModLocator.getLocatorId());
        requestVO.setSupplierCode(materialLotLine.getSupplierCode());
        MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(line.getSupplierId());
        requestVO.setSupplierCode(mtSupplier != null ? mtSupplier.getSupplierCode() : "");
        requestVO.setSourceDocId(line.getInstructionDocId());
        requestVO.setSourceDocLineId(line.getInstructionId());
        requestVO.setMergeFlag(WmsConstant.CONSTANT_N);
        requestVO.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_OSOURCING_COMP_DEDU).getMoveType());
        if(CollectionUtils.isNotEmpty(wmsPoDeliveryRels)){
            WmsDeliveryPoRelVo poNum = wmsMaterialPostingMapper.selectPoByLineId(tenantId, wmsPoDeliveryRels.get(0).getPoLineId());
            requestVO.setPoId(poNum.getPoId());
            requestVO.setPoLineId(poNum.getPoLineId());
            requestVO.setPoNum(poNum.getPoNumber());
            requestVO.setPoLineNum(poNum.getPoLineNumber());
        }
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_instruction_attr");
        mtExtendVO1.setKeyIdList(Collections.singletonList(line.getInstructionId()));
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName("RSNUM");
        attrs.add(extend1);
        MtExtendVO5 extend2 = new MtExtendVO5();
        extend2.setAttrName("RSPOS");
        attrs.add(extend2);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        for (MtExtendAttrVO1 mtExtendAttrVO1 : attrVO1List) {
            switch (mtExtendAttrVO1.getAttrName()){
                case "RSNUM" : requestVO.setBomReserveNum(mtExtendAttrVO1.getAttrValue()); break;
                case "RSPOS" : requestVO.setBomReserveLineNum(mtExtendAttrVO1.getAttrValue());break;
            }
        }
        requestVO.setPoNum(wmsMaterialPostingMapper.queryPoNum(tenantId, line.getInstructionId()));
        requestVO.setPoLineNum(wmsMaterialPostingMapper.queryPoLineNum(tenantId, line.getInstructionId()));
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        objectTransactionRequestList.add(requestVO);
        return wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }
}
