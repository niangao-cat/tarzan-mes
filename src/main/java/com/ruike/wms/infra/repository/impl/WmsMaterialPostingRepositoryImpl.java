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
     * 执行校验
     *
     * @param tenantId      租户
     * @param line          行数据
     * @param materialSumVO 总数
     * @param actualVO      实际
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/17 02:28:57
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<QmsQcDocMaterialLotRel> processValidate(Long tenantId, WmsInstructionLineVO line, WmsMaterialLotLineVO materialSumVO, WmsMaterialLotLineVO actualVO) {
        List<QmsQcDocMaterialLotRel> qmsQcDocMaterialLotRelList = new ArrayList<>();
        //二次检验单数量
        BigDecimal secondQty = null;
        // 二次检验 条码汇总数量
        BigDecimal secondMaterialLotQty = null;
        // 是否免检标识为N，则需要进行检验
        if (WmsConstant.CONSTANT_N.equals(line.getExemptionFlag())) {
            QmsIqcHeader qmsIqcHeader = qmsIqcHeaderRepository.selectByPrimaryKey(line.getIqcHeaderId());
            String inspectionStatus = qmsIqcHeader != null ? qmsIqcHeader.getInspectionStatus() : "";
            if (!WmsConstant.InstructionStatus.COMPLETED.equals(inspectionStatus)) {
                // 该单据对应的检验单未完成，不可过账！
                throw new MtException("WMS_MATERIAL_POSTING_002", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_002", "WMS"));
            } else {
                if (HmeConstants.ConstantValue.NG.equals(line.getInspectionResult())) {
                    if (QmsConstants.FinalDecision.TH.equals(line.getFinalDecision())) {
                        // 该单据的检验结果不合格、且采取退货处理，不可过账
                        throw new MtException("WMS_MATERIAL_POSTING_003", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_003", "WMS"));
                    }

                    if (QmsConstants.FinalDecision.TX.equals(line.getFinalDecision())) {
                        // 获取二次检验单
                        QmsIqcHeader iqcCondition = new QmsIqcHeader();
                        iqcCondition.setTenantId(tenantId);
                        iqcCondition.setDocHeaderId(line.getIqcHeaderId());
                        iqcCondition.setInspectionType("SECOND_INSPECTION");
                        QmsIqcHeader qmsIqcHeaderSecond = qmsIqcHeaderRepository.selectOne(iqcCondition);

                        if (!WmsConstant.InstructionStatus.COMPLETED.equals(qmsIqcHeaderSecond.getInspectionStatus())) {
                            // 该单据关联的二次检验单未完成，不可过账！
                            throw new MtException("WMS_MATERIAL_POSTING_004",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_MATERIAL_POSTING_004", "WMS"));
                        } else {
                            // 二次检验单结果为NG 且为退货
                            if (HmeConstants.ConstantValue.NG.equals(qmsIqcHeaderSecond.getInspectionResult()) && QmsConstants.FinalDecision.TH.equals(qmsIqcHeaderSecond.getFinalDecision())) {
                                // 该单据关联的二次检验单判定不合格且为退货，不可过账！
                                throw new MtException("WMS_MATERIAL_POSTING_005",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "WMS_MATERIAL_POSTING_005", "WMS"));
                            }
                        }
                        secondQty = qmsIqcHeaderSecond.getQuantity() != null ? qmsIqcHeaderSecond.getQuantity() : BigDecimal.ZERO;
                        // 根据首次质检单ID查找二次送检条码 对数量进行汇总
                        List<QmsQcDocMaterialLotRel> materialLotRelList = qmsQcDocMaterialLotRelMapper.querySecondMaterialLot(tenantId, line.getIqcHeaderId());
                        Double materialLotQuantity = materialLotRelList.stream().map(QmsQcDocMaterialLotRel::getQuantity).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                        secondMaterialLotQty = materialLotQuantity != null ? BigDecimal.valueOf(materialLotQuantity) : BigDecimal.ZERO;
                        qmsQcDocMaterialLotRelList = materialLotRelList;
                    }
                }
            }
        }

        // 无论是否免检，均执行以下校验(前端会缓存数据, 查询判断)
        MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(line.getInstructionId());
        String instructionStatus = mtInstruction != null ? mtInstruction.getInstructionStatus() : "";
        if (!WmsConstant.InstructionStatus.COMPLETED.equals(instructionStatus)
                || !WmsConstant.InstructionStatus.RELEASED.equals(line.getTransOverInstructionStatus())) {
            // 单据状态错误，不允许过账！
            throw new MtException("WMS_MATERIAL_POSTING_006", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_006", "WMS"));
        }
        // 二次送检时 取汇总的送检条码数量做比较
        BigDecimal materialPrimaryUomQty;
        if (secondMaterialLotQty != null) {
            materialPrimaryUomQty = secondMaterialLotQty;
        } else {
            materialPrimaryUomQty = Optional.ofNullable(materialSumVO.getPrimaryUomQty()).orElse(BigDecimal.ZERO);
        }
        BigDecimal exchangedQty = Optional.ofNullable(actualVO.getExchangedQty()).orElse(BigDecimal.ZERO);

        //首次检验取实际接收数量+料废调换;二次检验取检验单的物料数量
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

        //获取NG数量
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_instruction_attr");
        extendVO.setAttrName("NG_QTY");
        extendVO.setKeyId(line.getInstructionId());
        // 找到实际存在的属性值
        List<MtExtendAttrVO> attrValueList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
        BigDecimal ngQty = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(attrValueList)) {
            if (StringUtils.isNotBlank(attrValueList.get(0).getAttrValue())) {
                ngQty = BigDecimal.valueOf(Double.valueOf(attrValueList.get(0).getAttrValue()));
            }
        }

        if (materialPrimaryUomQty.compareTo(actualQty.subtract(ngQty)) != 0) {
            // 单据过账数量与条码数量不匹配，不允许过账！
            throw new MtException("WMS_MATERIAL_POSTING_007", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_007", "WMS"));
        }
        return qmsQcDocMaterialLotRelList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public WmsMaterialPostingEventVO createEvent(Long tenantId, String locatorId) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_RECEIPT");
        // 料废调换事件生成
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
            // 	请先勾选需要过账的物料
            throw new MtException("WMS_MATERIAL_POSTING_001", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_001", "WMS"));
        }
        List<WmsObjectTransactionResponseVO> responseVOArrayList = new ArrayList<>();

        Map<String, WmsTransactionTypeDTO> trxMap = wmsTransactionTypeRepository.getAllTransactionType(tenantId);

        // 查询出单据下对应的 TRANSFER_OVER_LOCATOR 的数据
        List<String> idList = dtoList.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList());
        List<WmsInstructionLineVO> tolList = wmsMaterialPostingMapper.selectTolInstructionByRsfIds(tenantId, idList);
        Map<String, String> tolMap = tolList.stream().collect(Collectors.toMap(WmsInstructionLineVO::getTransOverInstructionId, WmsInstructionLineVO::getInstructionId));

        for (WmsInstructionLineVO line : dtoList) {
            // 条码实物数量
            WmsMaterialLotLineVO materialSumVO = wmsMaterialPostingMapper.selectMaterialLotQty(tenantId, line.getInstructionId());
            WmsMaterialLotLineVO actualVO = wmsMaterialPostingMapper.selectInstructionActualQty(tenantId, line.getInstructionId());

            // 执行校验逻辑 返回二次送检条码清单 为空 则不为二次质检单
            List<QmsQcDocMaterialLotRel> materialLotRelList = this.processValidate(tenantId, line, materialSumVO, actualVO);

            // 获取事件请求
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

            //料废调换
            List<WmsExchangeLineVO> exchangeLineList = wmsPutInStorageTaskMapper.queryExchangeInstructionWithSite(
                    tenantId, line.getSupplierId(), line.getMaterialId(), line.getMaterialVersion(), line.getSiteId());
            //筛选出料废数量跟执行数量不一致的
            exchangeLineList = exchangeLineList.stream().filter(dto -> dto.getActualQty().compareTo(dto.getExecuteQty()) != 0).collect(Collectors.toList());
            double exchangeActualQty = exchangeLineList.stream().map(WmsExchangeLineVO::getActualQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
            double exchangeExecuteQty = exchangeLineList.stream().map(WmsExchangeLineVO::getExecuteQty).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
            BigDecimal subNum = BigDecimal.valueOf(exchangeExecuteQty).subtract(BigDecimal.valueOf(exchangeActualQty));
            if(line.getExchangedQty().compareTo(subNum) > 0){
                throw new MtException("WMS_MATERIAL_POSTING_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_MATERIAL_POSTING_010", "WMS", line.getInstructionDocNum(), subNum.toString()));
            }
            for (WmsMaterialLotLineVO materialLine : materialLotVOList) {
                // 采购订单关系Map
                Map<String, BigDecimal> poRelMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

                //获取最新的实物数量
                actualVO = wmsMaterialPostingMapper.selectInstructionActualQty(tenantId, line.getInstructionId());

                // 初始化剩余条码数量，默认等于当前实物条码数量
                BigDecimal materialLotRemainQty = materialLine.getPrimaryUomQty();
                if (WmsConstant.DocType.DELIVERY_DOC.equals(line.getInstructionDocType()) || WmsConstant.DocType.SRM_SUPP_EXCH_DOC.equals(line.getInstructionDocType()) || WmsConstant.DocType.OUTSOURCING_DELIVERY_DOC.equals(line.getInstructionDocType())) {
                    // 单据类型为DELIVERY_DOC-送货单
                    if (actualVO.getStockInExchangedQty().compareTo(line.getExchangedQty()) < 0) {
                        if (CollectionUtils.isEmpty(exchangeLineList)) {
                            // 当前供应商（显示供应商code）的物料（显示物料号）无料废调换需求，请确认！
                            MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(line.getSupplierId());
                            throw new MtException("WMS_MATERIAL_POSTING_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_MATERIAL_POSTING_008", "WMS", mtSupplier.getSupplierCode(), line.getMaterialCode()));
                        } else {
                            // 事务数量
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

                                //为零标识当前条码都做了料废调换 则跳出循环
                                if (exchangeRemainQty.compareTo(BigDecimal.ZERO) == 0) {
                                    break;
                                }
                                //若料废调换行完成则跳过
                                MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(exchangeLine.getInstructionId());
                                if(StringUtils.equals(mtInstruction.getInstructionStatus(), WmsConstant.InstructionStatus.COMPLETED)){
                                    continue;
                                }
                                // 1.1.1）、获取当前料废调换指令行对应的指令实绩数量
                                List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, exchangeLine.getInstructionId());
                                BigDecimal actualQty = BigDecimal.ZERO;
                                if (CollectionUtils.isNotEmpty(mtInstructionActualList)) {
                                    actualQty = mtInstructionActualList.stream().map(rec -> BigDecimal.valueOf(rec.getActualQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
                                }
                                exchangeLine.setActualQty(actualQty);

                                //2020/9/9 add by sanfeng.zhang 料废调换单行的制单数量 换成 已发出数量
                                BigDecimal executeQty = BigDecimal.ZERO;
                                MtExtendSettings reworkAttr = new MtExtendSettings();
                                reworkAttr.setAttrName("EXECUTE_QTY");
                                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsMapper.attrPropertyQuery(
                                        tenantId, "mt_instruction_attr", "INSTRUCTION_ID",
                                        exchangeLine.getInstructionId(), Collections.singletonList(reworkAttr));

                                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                                    executeQty = BigDecimal.valueOf(Double.valueOf(mtExtendAttrVOS.get(0).getAttrValue()));
                                }
                                // 送货单行的料废调换数量-已执行料废调换数量>=条码实物数量
                                if ((line.getExchangedQty().subtract(actualVO.getStockInExchangedQty())).compareTo(materialLine.getPrimaryUomQty()) >= 0) {

                                    if (exchangeRemainQty.compareTo(BigDecimal.ZERO) <= 0) {
                                        if (exchangeRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                            exchangeRemainQty = materialLine.getPrimaryUomQty();
                                        } else {
                                            break;
                                        }
                                    }

                                    materialLotRemainQty = BigDecimal.ZERO;

                                    // 料废调换单行的发出数量-已执行数量（扩展字段）>=条码数量
                                    if (executeQty.subtract(exchangeLine.getActualQty()).compareTo(exchangeRemainQty) >= 0) {
                                        // 事务数量=条码实物数量
                                        transactionQty = exchangeRemainQty;
                                        exchangeRemainQty = BigDecimal.ZERO;
                                    } else {
                                        transactionQty = executeQty.subtract(exchangeLine.getActualQty());
                                        exchangeRemainQty = exchangeRemainQty.subtract(transactionQty);
                                        if (exchangeRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                            exchangeRemainQty = BigDecimal.ZERO;
                                        }
                                    }

                                    // 记录子库存转移事务
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

                                    //根据toLocatorId找到父层仓库
                                    objectTransactionVO.setWarehouseId(locatorList.get(0).getParentLocatorId());

                                    objectTransactionVO.setTransactionQty(transactionQty);
                                    objectTransactionVO.setSourceDocId(exchangeLine.getInstructionDocId());
                                    objectTransactionVO.setSourceDocLineId(exchangeLine.getInstructionId());
                                    objectTransactionVO.setSourceDocNum(exchangeLine.getInstructionDocNum());
                                    objectTransactionVO.setSourceDocLineNum(exchangeLine.getInstructionNum());
                                    objectTransactionVO.setSourceDocType(exchangeLine.getInstructionDocType());
                                    objectTransactionVO.setTransactionReasonCode("过账平台料费调换");
                                    // 查询来源库位
                                    MtModLocator locator = new MtModLocator();
                                    locator.setLocatorId(exchangeLine.getLocatorId());
                                    locator = mtModLocatorRepository.selectByPrimaryKey(locator);
                                    if (Objects.nonNull(locator)) {
                                        materialLine.setExchangeLocatorId(exchangeLine.getLocatorId());
                                        materialLine.setExchangeLocatorCode(locator.getLocatorCode());
                                    }
                                    // 查询目标库位
                                    MtModLocator transLocator = new MtModLocator();
                                    transLocator.setLocatorId(materialLine.getTransferLocatorId());
                                    transLocator = mtModLocatorRepository.selectByPrimaryKey(transLocator);
                                    if (Objects.nonNull(transLocator)) {
                                        materialLine.setTransferLocatorCode(transLocator.getLocatorCode());
                                    }

                                    wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO,
                                            materialLine, objectTransactionRequestList);


                                    // 料废调换单执行
                                    MtInstructionVO3 executeVO = new MtInstructionVO3();
                                    executeVO.setEventRequestId(eventRequest.getEventRequestId());
                                    executeVO.setInstructionId(exchangeLine.getInstructionId());
                                    // 料废调换执行消息列表
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

                                    // 判断是否还有剩余料废调换数量，没有则退出循环
                                    if (exchangeRemainQty.compareTo(BigDecimal.ZERO) <= 0) {
                                        if (exchangeRemainQty.compareTo(BigDecimal.ZERO) < 0) {
                                            exchangeRemainQty = line.getExchangedQty()
                                                    .subtract(actualVO.getStockInExchangedQty());
                                        } else {
                                            break;
                                        }
                                    }

                                    // 计算物料行的条码实物剩余数量，进入步骤2
                                    materialLotRemainQty = materialLine.getPrimaryUomQty().subtract(line.getExchangedQty().subtract(actualVO.getStockInExchangedQty()));

                                    // 料废调换单行的制单数量-已执行数量（扩展字段）<条码数量
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

                                    // 记录子库存转移事务
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
                                    //根据toLocatorId找到父层仓库
                                    objectTransactionVO.setWarehouseId(locatorList.get(0).getParentLocatorId());

                                    objectTransactionVO.setSoNum(materialLine.getSoNum());
                                    objectTransactionVO.setSoLineNum(materialLine.getSoLineNum());
                                    objectTransactionVO.setTransactionQty(transactionQty);
                                    objectTransactionVO.setSourceDocId(exchangeLine.getInstructionDocId());
                                    objectTransactionVO.setSourceDocLineId(exchangeLine.getInstructionId());
                                    objectTransactionVO.setSourceDocNum(exchangeLine.getInstructionDocNum());
                                    objectTransactionVO.setSourceDocLineNum(exchangeLine.getInstructionNum());
                                    objectTransactionVO.setSourceDocType(exchangeLine.getInstructionDocType());
                                    objectTransactionVO.setTransactionReasonCode("过账平台料费调换");
                                    // 查询来源库位
                                    MtModLocator locator = new MtModLocator();
                                    locator.setLocatorId(exchangeLine.getLocatorId());
                                    locator = mtModLocatorRepository.selectByPrimaryKey(locator);
                                    if (Objects.nonNull(locator)) {
                                        materialLine.setExchangeLocatorId(exchangeLine.getLocatorId());
                                        materialLine.setExchangeLocatorCode(locator.getLocatorCode());
                                    }
                                    // 查询目标库位
                                    MtModLocator transLocator = new MtModLocator();
                                    transLocator.setLocatorId(materialLine.getTransferLocatorId());
                                    transLocator = mtModLocatorRepository.selectByPrimaryKey(transLocator);
                                    if (Objects.nonNull(transLocator)) {
                                        materialLine.setTransferLocatorCode(transLocator.getLocatorCode());
                                    }

                                    wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO,
                                            materialLine, objectTransactionRequestList);

                                    // 料废调换单执行
                                    MtInstructionVO3 executeVO = new MtInstructionVO3();
                                    executeVO.setEventRequestId(eventRequest.getEventRequestId());
                                    executeVO.setInstructionId(exchangeLine.getInstructionId());
                                    // 料废调换执行消息列表
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

                                //记录料废调换事务
                                List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                                if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS)) {
                                    responseVOArrayList.addAll(wmsObjectTransactionResponseVOS);
                                }
                                objectTransactionRequestList = new ArrayList<>();

                                // 料废调换单指令完成
                                BigDecimal newActualQty = exchangeLine.getActualQty().add(transactionQty);

                                if (newActualQty.compareTo(exchangeLine.getQuantity()) == 0) {
                                    mtInstructionRepository.instructionComplete(tenantId,
                                            exchangeLine.getInstructionId(), eventRequest.getEventRequestId());

                                    //判断所有行都为完成 则更新头为完成
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

                                // 扣减料废调换仓库存现有量
                                MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                                reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                                reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                                //获取26的货位
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
                                // 发送SRM接口
                                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
                                if (org.apache.commons.collections.CollectionUtils.isEmpty(lovValueDTOS)) {
                                    throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
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
                                // 增加目标仓库库存现有量
                                MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                                updateOnHandVO.setSiteId(line.getSiteId());
                                updateOnHandVO.setMaterialId(line.getMaterialId());
                                //获取编码对应的id(扩展字段存的编码)
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

                                //更新实绩STOCK_IN_EXCHANGEED_QTY
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

                                        //更新实绩表的扩展字段（产品api存在缺陷 更新事件一样 历史会报错索引错误）
                                        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
                                        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
                                        BigDecimal total = qty.add(transactionQty);
                                        String attrValue = "";
                                        if (total != null) {
                                            attrValue = total.toString();
                                        }
                                        wmsMaterialPostingMapper.updateStockInExchangeedQty(tenantId, actualList.get(0).getActualId(), userId, "STOCK_IN_EXCHANGEED_QTY", attrValue);
                                    } else {
                                        //如果没有值 则插入数据
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

                            // 更新条码库位，和条码扩展字段
                            MtMaterialLotVO2 mtMaterialLot = new MtMaterialLotVO2();
                            mtMaterialLot.setEventId(eventRequest.getExchangeOutEventId());
                            mtMaterialLot.setMaterialLotId(materialLine.getMaterialLotId());
                            mtMaterialLot.setLocatorId(materialLine.getActualLocatorId());
                            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLot,
                                    HmeConstants.ConstantValue.NO);

                        }

                    }
                }
                // 步骤2、 入库上架
                // 入库上架事件生成
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
                //若全部料废调换  不做入库上架
                if (materialLotRemainQty.compareTo(BigDecimal.ZERO) > 0) {

                    // 1.2.2)、记录入库指令单执行实绩
                    MtInstructionVO3 executeVO = new MtInstructionVO3();
                    executeVO.setEventRequestId(eventRequest.getEventRequestId());
                    executeVO.setInstructionId(line.getTransOverInstructionId());
                    // 料废调换执行消息列表
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

                    // 1.2.3)、判断送货单入库指令是否可以完成
                    // 1.2.3.1)、获取当前调拨指令行对应的指令实绩数量
                    List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository
                            .instructionLimitActualPropertyGet(tenantId, line.getTransOverInstructionId());
                    if (CollectionUtils.isEmpty(mtInstructionActualList)) {
                        // 当前料废调换指令没有对应的指令实绩数据
                        throw new MtException("WMS_MATERIAL_POSTING_009", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "WMS_MATERIAL_POSTING_009", "WMS"));
                    }
                    BigDecimal actualQty = BigDecimal.ZERO;
                    for (MtInstructionActual mtInstructionActual : mtInstructionActualList) {
                        actualQty = actualQty.add(BigDecimal.valueOf(mtInstructionActual.getActualQty()));
                    }
                    // 1.2.3.2)、指令实绩数量满足，则指令完成
                    if (line.getActualReceiveQty().equals(actualQty)) {
                        mtInstructionRepository.instructionComplete(tenantId, line.getTransOverInstructionId(),
                                eventRequest.getEventRequestId());
                    }

                    // 1.2.4)、将执行入库上架的数量分配至勾选送货单行关联的每一采购订单行
                    List<WmsPutInStorageVO> poLineList = wmsPutInStorageTaskMapper.queryPoLinesByInstructionId(tenantId,
                            line.getInstructionDocId(), line.getInstructionId());
                    List<WmsPutInStorageVO> poDistList = new ArrayList<>();
                    Boolean flag = false;
                    for (WmsPutInStorageVO poLine : poLineList) {
                        // 判断是否还有条码实物剩余数量，没有则退出循环
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

                            // 扣减条码来源仓库存现有量
                            MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                            reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                            reduceUpdateOnHandVO.setMaterialId(materialLine.getMaterialId());
                            reduceUpdateOnHandVO.setLocatorId(materialLine.getMaterialLotLocatorId());
                            reduceUpdateOnHandVO.setLotCode(materialLine.getLot());
                            reduceUpdateOnHandVO.setEventId(putInStorageOutEventId);
                            reduceUpdateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                            // 增加入库上架目标仓库库存现有量
                            MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                            updateOnHandVO.setSiteId(line.getSiteId());
                            updateOnHandVO.setMaterialId(materialLine.getMaterialId());
                            updateOnHandVO.setLocatorId(materialLine.getTransferLocatorId());
                            updateOnHandVO.setLotCode(materialLine.getLot());
                            updateOnHandVO.setEventId(putInStorageInEventId);
                            updateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                            flag = true;

                            // 统计po行更新
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
                        // 更新条码数据
                        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                            {
                                setEventId(putInStorageOutEventId);
                                setMaterialLotId(materialLine.getMaterialLotId());
                                setLocatorId(materialLine.getTransferLocatorId()); // 目标货位
                                setInLocatorTime(currentDate);
                            }
                        }, "N");
                    }

                    // 更新po行的数量
                    poRelMap.forEach((poDeliveryRelId, poInStockQty) -> {
                        WmsPoDeliveryRel poDeliveryRel = wmsPoDeliveryRepository.selectByPrimaryKey(poDeliveryRelId);
                        poDeliveryRel.setPoStockInQty(poInStockQty);
                        wmsPoDeliveryRepository.updateByPrimaryKey(poDeliveryRel);
                    });

                    // 1.2.5)、记录子库存转移事务
                    // 如果存在采购订单，需要根据采购单拆分事务
                    WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                    objectTransactionVO.setEventId(putInStorageInEventId);
                    objectTransactionVO.setTransactionTime(currentDate);

                    switch (line.getInstructionDocType()) {
                        case WmsConstant.DocType.DELIVERY_DOC:
                            objectTransactionVO.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_STOCK_IN);
                            objectTransactionVO.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_STOCK_IN).getMoveType());
                            objectTransactionVO.setTransactionReasonCode("过账平台入库上架");
                            break;
                        case WmsConstant.DocType.OUTSOURCING_DELIVERY_DOC:
                            objectTransactionVO.setTransactionTypeCode(WmsConstant.TransactionTypeCode.WMS_STOCK_IN);
                            objectTransactionVO.setMoveType(trxMap.get(WmsConstant.TransactionTypeCode.WMS_STOCK_IN).getMoveType());
                            objectTransactionVO.setTransactionReasonCode("过账平台外协送货单入库上架");
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

                    //获取编码对应的id(扩展字段存的编码)
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

                    // 查询库位编码
                    MtModLocator locator = new MtModLocator();
                    locator.setLocatorId(materialLine.getTransferLocatorId());
                    locator = mtModLocatorRepository.selectByPrimaryKey(locator);
                    materialLine.setTransferLocatorCode(locator.getLocatorCode());

                    // 根据PO拆分事务
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

                    //记录入库上架事务
                    List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

                    if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS)) {
                        responseVOArrayList.addAll(wmsObjectTransactionResponseVOS);
                    }
                    objectTransactionRequestList = new ArrayList<>();

                    // 更新物料行的物料批状态为INSTOCK-已入库
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
                    //条码全部做了料废调换 更新状态为已入库
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
                    // 步骤 1.3)、执行检验报废逻辑
                    //2020/9/9 add by sanfeng.zhang 检验报废数量获取逻辑修改
                    BigDecimal scrapQty = wmsMaterialPostingMapper.queryScrapQty(tenantId, line.getInstructionId(), materialLine.getMaterialLotId());
                    if (BigDecimal.ZERO.compareTo(scrapQty) < 0) {
                        String inspectScrapEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                            {
                                setEventRequestId(eventRequest.getEventRequestId());
                                setLocatorId(materialLine.getTransferLocatorId());
                                setEventTypeCode("INSPECT_SCRAP");
                            }
                        });

                        //检验报废条码带销售订单即soNum、soLineNum不为空自动转非限制
                        if (StringUtils.isNotBlank(materialLine.getSoNum()) && StringUtils.isNotBlank(materialLine.getSoLineNum())) {
                            //自动转非限制事务
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
                            normalTransaction.setTransactionReasonCode("过账平台检验报废自动转非限制");
                            normalTransaction.setWarehouseId(line.getWarehouseId());
                            normalTransaction.setSoNum(materialLine.getSoNum());
                            normalTransaction.setSoLineNum(materialLine.getSoLineNum());

                            wmsObjectTransactionRepository.addObjectTransaction(tenantId, normalTransaction, materialLine,
                                    objectTransactionRequestList);
                            //检验报废掉事务记录
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
                        transactionVO.setTransactionReasonCode("过账平台检验报废");
                        transactionVO.setWarehouseId(line.getWarehouseId());
                        wmsObjectTransactionRepository.addObjectTransaction(tenantId, transactionVO, materialLine,
                                objectTransactionRequestList);
                        //检验报废掉事务记录
                        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS1 = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                        if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS1)) {
                            responseVOArrayList.addAll(wmsObjectTransactionResponseVOS1);
                        }

                        objectTransactionRequestList = new ArrayList<>();

                        // 更新条码数据
                        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                            {
                                setEventId(inspectScrapEventId);
                                setMaterialLotId(materialLine.getMaterialLotId());
                                setPrimaryUomQty(materialLine.getPrimaryUomQty()
                                        .subtract(scrapQty).doubleValue());
                            }
                        }, "N");

                        // 扣减条码来源仓库存现有量
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
                        //BomUsage为零 不走以下逻辑
                        if(StringUtils.isNotBlank(outsourcingLine.getBomUsage()) && BigDecimal.valueOf(Double.valueOf(outsourcingLine.getBomUsage())).compareTo(BigDecimal.ZERO) > 0){
                            // 扣减外协供应商库存现有量
                            MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                            reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                            reduceUpdateOnHandVO.setMaterialId(outsourcingLine.getMaterialId());
                            //找到当前站点下库位类型为  外协仓  LOCATOR_TYPE = 20 ,库位下LOCATOR_TYPE为20的货位ID
                            List<MtModLocator> mtModLocatorList = wmsMaterialPostingMapper.queryOutsourceLocator(tenantId, line.getSiteId());
                            if (CollectionUtils.isEmpty(mtModLocatorList) || mtModLocatorList.size() > 1) {
                                throw new MtException("MT_INVENTORY_0037",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0037",
                                                "INVENTORY"));
                            }

                            reduceUpdateOnHandVO.setLocatorId(mtModLocatorList.get(0).getLocatorId());
                            reduceUpdateOnHandVO.setEventId(putInStorageOutEventId);
                            //获取行的指令数量
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

                            //外协组件扣减事务记录
                            List<WmsObjectTransactionResponseVO> outSourceTransaction = this.createOutSourceTransaction(tenantId, putInStorageInEventId, outsourcingLine, materialLine, total, mtModLocatorList.get(0), line, trxMap, wmsPoDeliveryRels);
                            if (CollectionUtils.isNotEmpty(outSourceTransaction)) {
                                responseVOArrayList.addAll(outSourceTransaction);
                            }
                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);
                        }
                    }
                }
            }
            //TRANSFER_OVER_LOCATOR类型指令行状态更新为完成
            MtInstruction tolInstruction = new MtInstruction();
            tolInstruction.setInstructionId(tolMap.get(line.getInstructionId()));
            tolInstruction.setInstructionStatus("COMPLETED");
            mtInstructionRepository.updateOptional(tolInstruction, MtInstruction.FIELD_INSTRUCTION_STATUS);

            //更新上架任务的状态
            List<WmsPutInStorageTask> wmsPutInStorageTaskList = wmsPutInStorageTaskMapper.select(new WmsPutInStorageTask(){{
                setTenantId(tenantId);
                setInstructionDocId(line.getInstructionDocId());
                setInstructionId(line.getInstructionId());
            }});
            if(CollectionUtils.isNotEmpty(wmsPutInStorageTaskList)){
                WmsPutInStorageTask wmsPutInStorageTask = wmsPutInStorageTaskList.get(0);
                wmsPutInStorageTask.setTaskStatus("STOCKED");
                //制单数量 根据头主键、物料及行号找TRANSFER_OVER_LOCATOR的行 取实绩的ActualQty
                BigDecimal actualQty = wmsMaterialPostingMapper.queryActualQty(tenantId, line.getInstructionDocId(), line.getMaterialId(), line.getInstructionLineNum());
                BigDecimal exchangedQty = wmsMaterialPostingMapper.queryExchangedQtyByLineId(tenantId, line.getInstructionId());
                //执行数量 取制单数量加料废调换数量
                wmsPutInStorageTask.setExecuteQty(actualQty.add(exchangedQty));
                wmsPutInStorageTaskMapper.updateByPrimaryKeySelective(wmsPutInStorageTask);
            }
        }
        //指令行都为完成 更新头的状态为STOCK_IN_COMPLETE
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

        // 发送SAP 过账
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        String interfaceFlag = lovValueDTOS.get(0).getMeaning();
        if (YES.equals(interfaceFlag)) {
            if (CollectionUtils.isNotEmpty(responseVOArrayList)) {
                List<String> transactionIdList = responseVOArrayList.stream().map(WmsObjectTransactionResponseVO::getTransactionId).collect(Collectors.toList());
                List<WmsObjectTransaction> wmsObjectTransactions = wmsObjectTransactionRepository.byIds(transactionIdList);
                // 排序
                List<WmsObjectTransaction> newList = new ArrayList<>();

                String[] transactionType = {"311", "101", "543"};
                log.info("过账事物总条数:{}", wmsObjectTransactions.size());
                for (String str : transactionType) {
                    for (int i = 0; i < wmsObjectTransactions.size(); i++) {
                        if (str.equals(wmsObjectTransactions.get(i).getMoveType())) {
                            if ("311".equals(str) && Strings.isNotBlank(wmsObjectTransactions.get(i).getSpecStockFlag())) {
                                log.info("311和状态为tSpecStockFlag的:{}", wmsObjectTransactions.get(i));
                                continue;
                            }
                            log.info(str + "：{}", wmsObjectTransactions.get(i));
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
        requestVO.setTransactionReasonCode("过账平台外协过账组件扣减");
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
