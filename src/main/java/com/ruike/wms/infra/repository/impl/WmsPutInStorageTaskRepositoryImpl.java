package com.ruike.wms.infra.repository.impl;

import cn.hutool.core.util.NumberUtil;
import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;
import com.ruike.wms.api.dto.WmsLocatorPutInStorageDTO;
import com.ruike.wms.api.dto.WmsMaterialLotLineDetailDTO;
import com.ruike.wms.api.dto.WmsPutInStorageDTO2;
import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.WmsBarCodeIdentifyRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsPutInStorageTaskRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import com.ruike.wms.infra.mapper.WmsPutInStorageTaskMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.util.StringUtil;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;
import tarzan.modeling.domain.vo.MtModLocatorVO1;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ????????????????????? ???????????????
 *
 * @author liyuan.lv@hand-china.com 2020-04-06 20:58:44
 */
@Slf4j
@Component
public class WmsPutInStorageTaskRepositoryImpl extends BaseRepositoryImpl<WmsPutInStorageTask> implements WmsPutInStorageTaskRepository {

    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private WmsBarCodeIdentifyRepository wmsBarCodeIdentifyRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;


    @Autowired
    private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;
    @Autowired
    private WmsPutInStorageTaskMapper mapper;
    @Autowired
    private MessageClient messageClient;

    @Override
    @ProcessLovValue
    public Page<WmsMaterialLotLineDetailDTO> queryDetail(Long tenantId, String instructionNum, PageRequest pageRequest) {
        Page<WmsMaterialLotLineDetailDTO> materialLotPutInStorageHipsList = new Page<>();

        // ????????????????????????????????????ID??????
        List<String> materialLotIdList = mapper.queryInstruction(tenantId, instructionNum);
        if (materialLotIdList.size() > 0) {
            materialLotPutInStorageHipsList =
                    PageHelper.doPageAndSort(pageRequest, () -> mapper.queryDetailByMaterialLotIds(materialLotIdList));
        }
        return materialLotPutInStorageHipsList;
    }

    @Override
    @ProcessLovValue
    public List<WmsInstructionLineVO> queryInstructionLineByLotIds(Long tenantId, List<String> strings) {
        return mapper.queryMaterialLotLineByLotIds(tenantId, strings);
    }

    @Override
    @ProcessLovValue
    public List<WmsInstructionLineVO> queryInstructionLine(Long tenantId, String instructionDocId) {
        WmsInstructionVO4 wmsInstructionVO4 = new WmsInstructionVO4();
        wmsInstructionVO4.setInstructionDocId(instructionDocId);
        List<WmsInstructionLineVO> instructionLineVOList = mapper.queryInstructionLine(tenantId, wmsInstructionVO4);
        for (WmsInstructionLineVO line : instructionLineVOList) {
            HashMap map = mapper.countNum(line.getInstructionNum());
            line.setSumCount(new BigDecimal(map.get("SUM_COUNT").toString()));
            line.setSumStockInCount(new BigDecimal(map.get("SUM_STOCK_IN_COUNT").toString()));
        }
        return instructionLineVOList;
    }

    @Override
    public WmsInstructionVO handleData(Long tenantId, String instructionDocNum, WmsInstructionVO instructionVO,
                                       List<WmsInstructionLineVO> lineList, String enableDocFlag, String putInFlag) {

        if (StringUtils.isNotBlank(instructionDocNum) && CollectionUtils.isEmpty(lineList)) {
            throw new MtException("WMS_PUT_IN_STOCK_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PUT_IN_STOCK_002", "WMS"));
        }

        if (CollectionUtils.isEmpty(lineList)) {
            throw new MtException("WMS_PUT_IN_STOCK_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PUT_IN_STOCK_003", "WMS"));
        }

        if (!WmsConstant.CONSTANT_Y.equals(putInFlag)) {
            if (StringUtil.isNotEmpty(instructionVO.getLocatorCode())) {
                lineList = lineList.stream().filter(line -> line.getLocatorCode().equals(instructionVO.getLocatorCode())).collect(Collectors.toList());
            }
        }

        for (WmsInstructionLineVO line : lineList) {

            List<MtExtendAttrVO1> mtExtendAttrVO1s = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_attr", Collections.singletonList(line.getInstructionId()), "EXCHANGE_QTY", "STOCK_IN_EXCHANGED_QTY", "EXCHANGED_QTY", "RECEIVED_QTY", "ACTUAL_RECEIVE_QTY", "STOCK_IN_QTY", "MATERIAL_VERSION"));
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s) {
                boolean attrBlankFlag = StringUtils.isBlank(extendAttr.getAttrValue());
                switch (extendAttr.getAttrName()) {
                    // ??????????????????
                    case "EXCHANGE_QTY":
                        line.setExchangeQty(attrBlankFlag ? null : new BigDecimal(extendAttr.getAttrValue()));
                        break;
                    // ?????????????????????
                    case "STOCK_IN_EXCHANGED_QTY":
                        line.setStockInExchangedQty(attrBlankFlag ? null : new BigDecimal(extendAttr.getAttrValue()));
                        break;
                    // ?????????????????????
                    case "EXCHANGED_QTY":
                        line.setExchangedQty(attrBlankFlag ? null : new BigDecimal(extendAttr.getAttrValue()));
                        break;
                    // ???????????????
                    case "RECEIVED_QTY":
                        line.setReceivedQty(attrBlankFlag ? null : new BigDecimal(extendAttr.getAttrValue()));
                        break;
                    // ??????????????????
                    case "ACTUAL_RECEIVE_QTY":
                        line.setActualReceiveQty(attrBlankFlag ? null : new BigDecimal(extendAttr.getAttrValue()));
                        break;
                    // ?????????????????????
                    case "STOCK_IN_QTY":
                        line.setStockInQty(attrBlankFlag ? null : new BigDecimal(extendAttr.getAttrValue()));
                        break;
                    // ????????????
                    case "MATERIAL_VERSION":
                        line.setMaterialVersion(attrBlankFlag ? null : extendAttr.getAttrValue());
                        break;
                    default:
                        break;
                }
            }

            if (StringUtils.isNotBlank(line.getMaterialLotCode())) {
                if (!WmsConstant.CONSTANT_Y.equals(putInFlag)) {
                    ////?????????enableFlag=Y?????????????????????MT_MATERIAL_LOT_0015???
                    if (!WmsConstant.CONSTANT_Y.equalsIgnoreCase(line.getEnableFlag())) {
                        throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", line.getMaterialLotCode()));
                    }

                    //????????????????????????qualityStatus?????? ???enableFlag???
                    //????????????OK????????????RELEASED???????????????????????????????????????WMS_PUT_IN_STOCK_004???????????????${1}???????????????${2}?????????????????????????????????${1}= ???????????????CODE??????${2}= ii????????????qualityStatus???
                    if (!"OK".equalsIgnoreCase(line.getQualityStatus())) {
                        throw new MtException("WMS_PUT_IN_STOCK_004", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "WMS_PUT_IN_STOCK_004", "WMS", line.getBarCode(), line.getQualityStatusMeaning()));
                    }

                    //??????????????????,????????????TO_ACCEPT???????????????????????????????????????Z_MATERIAL_LOT_0004???
                    if (!"TO_ACCEPT".equalsIgnoreCase(line.getMaterialLotStatus())) {
                        throw new MtException("WMS_PUT_IN_STOCK_005", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "WMS_PUT_IN_STOCK_005", "WMS", line.getMaterialLotCode(), line.getMaterialLotStatusMeaning()));
                    }

                    if (!StringUtils.isEmpty(instructionDocNum)) {
                        if (!instructionDocNum.equals(line.getInstructionDocNum())) {
                            throw new MtException("WMS_PUT_IN_STOCK_006",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_PUT_IN_STOCK_006",
                                            "WMS", line.getMaterialLotCode(), instructionDocNum));
                        }
                    }

                    // ???????????????instructionDocNum??????????????????true
                    if (!lineList.get(0).getInstructionDocNum().equals(line.getInstructionDocNum())) {
                        throw new MtException("WMS_PUT_IN_STOCK_007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_PUT_IN_STOCK_007",
                                        "WMS"));
                    }

                    if ("container".equalsIgnoreCase(instructionVO.getWmsCodeIdentifyDTO().getCodeType())) {
                        line.setContainerId(instructionVO.getWmsCodeIdentifyDTO().getCodeId());
                        line.setContainerCode(instructionVO.getWmsCodeIdentifyDTO().getCode());
                    }
                }
            }

        }
        instructionVO.setLocatorId(lineList.get(0).getLocatorId());
        instructionVO.setSiteId(lineList.get(0).getSiteId());
        instructionVO.setSiteCode(lineList.get(0).getSiteCode());
        instructionVO.setSiteName(lineList.get(0).getSiteName());
        instructionVO.setSupplierId(lineList.get(0).getSupplierId());
        instructionVO.setSupplierCode(lineList.get(0).getSupplierCode());
        instructionVO.setSupplierName(lineList.get(0).getSupplierName());
        instructionVO.setRemark(lineList.get(0).getRemark());
        instructionVO.setUomCode(lineList.get(0).getUomCode());
        instructionVO.setInstructionDocId(lineList.get(0).getInstructionDocId());
        instructionVO.setInstructionDocNum(lineList.get(0).getInstructionDocNum());
        instructionVO.setInstructionDocStatus(lineList.get(0).getInstructionDocStatus());

        List<WmsInstructionLineVO> instructionLineVOList = queryInstructionLine(tenantId, lineList.get(0).getInstructionDocId());
        if (WmsConstant.CONSTANT_Y.equals(enableDocFlag)) {
            instructionVO.setOrderLineList(instructionLineVOList);
        } else {
            List<String> instructionIds = lineList.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList());
            List<WmsInstructionLineVO> newInstructionLineVOList =
                    instructionLineVOList.stream().filter(line -> instructionIds.contains(line.getInstructionId())).collect(Collectors.toList());
            instructionVO.setOrderLineList(newInstructionLineVOList);
        }

        instructionVO.setDetailLineList(lineList);
        return instructionVO;
    }

    @Override
    public WmsLocatorPutInStorageDTO getLocator(Long tenantId, WmsPutInStorageDTO2 dto) {
        MtModLocatorVO1 condition = new MtModLocatorVO1();
        WmsLocatorPutInStorageDTO response = new WmsLocatorPutInStorageDTO();

        condition.setEnableFlag(WmsConstant.CONSTANT_Y);
        condition.setLocatorCode(dto.getLocatorCode());
        condition.setLocatorCategory("INVENTORY");
        List<String> subLocatorIdList = mtModLocatorRepository.propertyLimitLocatorQuery(tenantId, condition);
        if (subLocatorIdList == null || subLocatorIdList.size() == 0) {
            throw new MtException("WMS_PUT_IN_STOCK_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PUT_IN_STOCK_001", "WMS", dto.getLocatorCode()));
        }
        // ??????????????????
        MtModLocator modLocator = mtModLocatorRepository.selectOne(new MtModLocator() {
            {
                setLocatorId(subLocatorIdList.get(0));
            }
        });

       /* List<String> locatorIdList =
                mtModLocatorRepository.parentLocatorQuery(tenantId, subLocatorIdList.get(0), "TOP");*/
        MtModLocatorOrgRelVO2 mtModLocatorOrgRelVO2 = new MtModLocatorOrgRelVO2();
        mtModLocatorOrgRelVO2.setLocatorId(subLocatorIdList.get(0));
        mtModLocatorOrgRelVO2.setOrganizationType("SITE");
        List<MtModLocatorOrgRelVO3> siteIdList =
                mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, mtModLocatorOrgRelVO2);

        response.setLocatorName(modLocator.getLocatorName());
        response.setLocatorCode(dto.getLocatorCode());
        response.setLocatorId(subLocatorIdList.get(0));
        response.setSiteId(siteIdList.get(0).getOrganizationId());
        // ??????????????????
        MtModSite modSite = mtModSiteRepository.selectOne(new MtModSite() {
            {
                setSiteId(siteIdList.get(0).getOrganizationId());
            }
        });
        response.setSiteCode(modSite.getSiteCode());
        response.setSiteName(modSite.getSiteName());
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue(targetField = {"", "orderLineList", "detailLineList"})
    @Override
    public WmsInstructionVO putInStorage(Long tenantId, WmsInstructionVO3 dto) {
        List<WmsMaterialLotLineVO> lineList = dto.getDetailLineList();
        if (CollectionUtils.isEmpty(lineList)) {
            throw new MtException("WMS_PUT_IN_STOCK_008",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_PUT_IN_STOCK_008",
                            "WMS"));
        }

        if (Objects.isNull(lineList.get(0).getTransferLocatorId())) {
            throw new MtException("WMS_PUT_IN_STOCK_009",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_PUT_IN_STOCK_009",
                            "WMS"));
        }

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_RECEIPT");
        // ????????????????????????
        String exchangeOutEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventRequestId(eventRequestId);
            setLocatorId(lineList.get(0).getMaterialLotLocatorId());
            setEventTypeCode("SUPPLIER_EXCHANGE_RECE_OUT");
        }});
        String exchangeInEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventRequestId(eventRequestId);
            setParentEventId(exchangeOutEventId);
            setLocatorId(lineList.get(0).getTransferLocatorId());
            setEventTypeCode("SUPPLIER_EXCHANGE_RECE_IN");
        }});
        // ????????????????????????
        String putInStorageOutEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventRequestId(eventRequestId);
            setLocatorId(lineList.get(0).getTransferLocatorId());
            setEventTypeCode("MATERIAL_RECEIPT_OUT");
        }});
        String putInStorageInEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventRequestId(eventRequestId);
            setParentEventId(putInStorageOutEventId);
            setLocatorId(lineList.get(0).getTransferLocatorId());
            setEventTypeCode("MATERIAL_RECEIPT_IN");
        }});

        Date currentDate = new Date(System.currentTimeMillis());
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();

        Map<String, Double> lineStockInQtyMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        for (WmsMaterialLotLineVO line : lineList) {
            WmsCodeIdentifyDTO codeIdentifyDTO = wmsBarCodeIdentifyRepository.codeIdentify(tenantId, line.getMaterialLotCode());

            // ??????????????????
            BigDecimal materialLotRemainQty = line.getPrimaryUomQty();

            // ?????????????????????????????????????????????????????????????????????????????? 1
            if (line.getExchangedQty().compareTo(line.getStockInExchangedQty()) != 0) {
                // ?????? 1??? ??????????????????
                List<WmsExchangeLineVO> exchangeLineList =
                        mapper.queryExchangeInstruction(tenantId, line.getSupplierId(), line.getMaterialId(), line.getMaterialVersion());

                if (CollectionUtils.isNotEmpty(exchangeLineList)) {
                    // ????????????
                    BigDecimal transactionQty;
                    BigDecimal exchangeRemainQty = BigDecimal.valueOf(-1.0);
                    int exchangeCompleteCount = 0;
                    for (WmsExchangeLineVO exchangeLine : exchangeLineList) {
                        List<MtExtendAttrVO1> putInStorageAttrVO = extendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1("mt_instruction_attr", Collections.singletonList(exchangeLine.getInstructionId()), "EXECUTE_QTY"));
                        for (MtExtendAttrVO1 extendAttr :
                                putInStorageAttrVO) {
                            // ????????????????????????
                            if ("EXECUTE_QTY".equals(extendAttr.getAttrName())) {
                                exchangeLine.setExecuteQty(new BigDecimal(extendAttr.getAttrValue()));
                            }
                        }

                        // ?????????????????????????????????-???????????????????????????>=??????????????????
                        if ((line.getExchangedQty().subtract(line.getStockInExchangedQty())).compareTo(line.getPrimaryUomQty()) >= 0) {

                            if (exchangeRemainQty.compareTo(BigDecimal.valueOf(0)) <= 0) {
                                if (exchangeRemainQty.compareTo(BigDecimal.valueOf(0)) < 0) {
                                    exchangeRemainQty = line.getPrimaryUomQty();
                                } else {
                                    break;
                                }
                            }

                            // ?????????????????????????????????-?????????????????????????????????>=????????????
                            if (exchangeLine.getQuantity().subtract(exchangeLine.getExecuteQty()).compareTo(exchangeRemainQty) >= 0) {
                                //????????????=??????????????????
                                transactionQty = exchangeRemainQty;
                            } else {
                                transactionQty = exchangeLine.getQuantity().subtract(exchangeLine.getExecuteQty());
                                exchangeRemainQty = exchangeRemainQty.subtract(transactionQty);
                                if (exchangeRemainQty.compareTo(BigDecimal.valueOf(0)) < 0) {
                                    exchangeRemainQty = BigDecimal.valueOf(0.0);
                                }
                            }
                            // ???????????????????????????
                            WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                            objectTransactionVO.setEventId(exchangeInEventId);
                            objectTransactionVO.setTransactionTypeCode("GENERAL_TSFTXN");
                            objectTransactionVO.setTransactionTime(currentDate);
                            objectTransactionVO.setTransactionQty(transactionQty);
                            objectTransactionVO.setSourceDocId(exchangeLine.getInstructionDocId());
                            objectTransactionVO.setSourceDocLineId(exchangeLine.getInstructionId());
                            objectTransactionVO.setSourceDocNum(exchangeLine.getInstructionDocNum());
                            objectTransactionVO.setSourceDocLineNum(exchangeLine.getInstructionNum());
                            objectTransactionVO.setSourceDocType(exchangeLine.getInstructionDocType());
                            objectTransactionVO.setRemark("??????????????????");

                            wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO, line, objectTransactionRequestList);

                            BigDecimal newExecuteQty = exchangeLine.getExecuteQty().add(transactionQty);
                            String exchangeStatus;
                            if (newExecuteQty.equals(exchangeLine.getQuantity())) {
                                exchangeStatus = "COMPLETE";
                                exchangeCompleteCount++;
                            } else {
                                exchangeStatus = "EXECUTE";
                            }

                            MtInstructionVO mtInstructionVO7 = new MtInstructionVO();
                            mtInstructionVO7.setInstructionId(exchangeLine.getInstructionId());
                            mtInstructionVO7.setEventId(exchangeOutEventId);
                            mtInstructionVO7.setInstructionStatus(exchangeStatus);
                            mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO7, "N");

                            // ?????????????????????, ??????????????????????????????
                            List<MtExtendVO5> exchangeExtList = new ArrayList<>();
                            MtExtendVO5 executeQtyAttr = new MtExtendVO5();
                            executeQtyAttr.setAttrName("EXECUTE_QTY");
                            executeQtyAttr.setAttrValue(String.valueOf(newExecuteQty));
                            exchangeExtList.add(executeQtyAttr);
                            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                                setKeyId(exchangeLine.getInstructionId());
                                setEventId(exchangeOutEventId);
                                setAttrs(exchangeExtList);
                            }});

                            // ????????????????????????????????????
                            MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                            reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                            reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                            reduceUpdateOnHandVO.setLocatorId(line.getMaterialLotLocatorId());
                            reduceUpdateOnHandVO.setLotCode(line.getLot());
                            reduceUpdateOnHandVO.setEventId(exchangeOutEventId);
                            reduceUpdateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                            // ?????????????????????????????????
                            MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                            updateOnHandVO.setSiteId(line.getSiteId());
                            updateOnHandVO.setMaterialId(line.getMaterialId());
                            updateOnHandVO.setLocatorId(line.getTransferLocatorId());
                            updateOnHandVO.setLotCode(line.getLot());
                            updateOnHandVO.setEventId(exchangeInEventId);
                            updateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                        } else {

                            // ??????????????????????????????????????????????????????????????????
                            if (exchangeRemainQty.compareTo(BigDecimal.valueOf(0)) <= 0) {
                                if (exchangeRemainQty.compareTo(BigDecimal.valueOf(0)) < 0) {
                                    exchangeRemainQty = line.getExchangedQty().subtract(line.getStockInExchangedQty());
                                } else {
                                    break;
                                }
                            }

                            // ?????????????????????????????????????????????????????????2
                            materialLotRemainQty = line.getPrimaryUomQty().subtract(line.getExchangedQty()).subtract(line.getStockInExchangedQty());

                            // ?????????????????????????????????-?????????????????????????????????<????????????
                            if ((exchangeLine.getQuantity().subtract(exchangeLine.getExecuteQty())).compareTo(exchangeRemainQty) >= 0) {
                                transactionQty = exchangeRemainQty;
                            } else {
                                transactionQty = exchangeLine.getExecuteQty().subtract(line.getStockInExchangedQty());
                                exchangeRemainQty = exchangeRemainQty.subtract(transactionQty);
                                if (exchangeRemainQty.compareTo(BigDecimal.valueOf(0)) < 0) {
                                    exchangeRemainQty = BigDecimal.valueOf(0.0);
                                }
                            }
                            // ???????????????????????????
                            WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                            objectTransactionVO.setEventId(exchangeInEventId);
                            objectTransactionVO.setTransactionTypeCode("GENERAL_TSFTXN");
                            objectTransactionVO.setTransactionTime(currentDate);
                            objectTransactionVO.setTransactionQty(transactionQty);
                            objectTransactionVO.setSourceDocId(exchangeLine.getInstructionDocId());
                            objectTransactionVO.setSourceDocLineId(exchangeLine.getInstructionId());
                            objectTransactionVO.setSourceDocNum(exchangeLine.getInstructionDocNum());
                            objectTransactionVO.setSourceDocLineNum(exchangeLine.getInstructionNum());
                            objectTransactionVO.setSourceDocType(exchangeLine.getInstructionDocType());
                            objectTransactionVO.setRemark("??????????????????");

                            wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO, line, objectTransactionRequestList);

                            BigDecimal newExecuteQty = exchangeLine.getExecuteQty().add(transactionQty);
                            String exchangeStatus;
                            if (newExecuteQty.equals(exchangeLine.getQuantity())) {
                                exchangeStatus = "COMPLETE";
                                exchangeCompleteCount++;
                            } else {
                                exchangeStatus = "EXECUTE";
                            }

                            MtInstructionVO mtInstructionVO7 = new MtInstructionVO();
                            mtInstructionVO7.setInstructionId(exchangeLine.getInstructionId());
                            mtInstructionVO7.setEventId(exchangeOutEventId);
                            mtInstructionVO7.setInstructionStatus(exchangeStatus);
                            mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO7, "N");

                            // ?????????????????????, ??????????????????????????????
                            List<MtExtendVO5> exchangeExtList = new ArrayList<>();
                            MtExtendVO5 executeQtyAttr = new MtExtendVO5();
                            executeQtyAttr.setAttrName("EXECUTE_QTY");
                            executeQtyAttr.setAttrValue(String.valueOf(newExecuteQty));
                            exchangeExtList.add(executeQtyAttr);
                            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                                setKeyId(exchangeLine.getInstructionId());
                                setEventId(exchangeOutEventId);
                                setAttrs(exchangeExtList);
                            }});
                            // ????????????????????????????????????
                            MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                            reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                            reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                            reduceUpdateOnHandVO.setLocatorId(line.getMaterialLotLocatorId());
                            reduceUpdateOnHandVO.setLotCode(line.getLot());
                            reduceUpdateOnHandVO.setEventId(exchangeOutEventId);
                            reduceUpdateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                            // ?????????????????????????????????
                            MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                            updateOnHandVO.setSiteId(line.getSiteId());
                            updateOnHandVO.setMaterialId(line.getMaterialId());
                            updateOnHandVO.setLocatorId(line.getTransferLocatorId());
                            updateOnHandVO.setLotCode(line.getLot());
                            updateOnHandVO.setEventId(exchangeInEventId);
                            updateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                            //
                        }
                    }

                    String exchangeDocStatus;
                    if (exchangeCompleteCount == exchangeLineList.size()) {
                        exchangeDocStatus = "COMPLETE";
                    } else {
                        exchangeDocStatus = "EXECUTE";
                    }
                    MtInstructionDocDTO2 mtInstructionDocVO = new MtInstructionDocDTO2();
                    mtInstructionDocVO.setInstructionDocId(exchangeLineList.get(0).getInstructionDocId());
                    mtInstructionDocVO.setEventId(exchangeOutEventId);
                    mtInstructionDocVO.setInstructionDocStatus(exchangeDocStatus);

                    mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocVO, "N");

                }

                //
                BigDecimal newExchangedQty = line.getExchangedQty().add(line.getPrimaryUomQty());
                // ???????????????????????????????????? ??????????????????????????????????????????
                List<MtExtendVO5> exchangeExtList = new ArrayList<>();
                MtExtendVO5 exchangedQtyAttr = new MtExtendVO5();
                exchangedQtyAttr.setAttrName("EXCHANGED_QTY");
                exchangedQtyAttr.setAttrValue(String.valueOf(newExchangedQty));
                exchangeExtList.add(exchangedQtyAttr);
                mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                    setKeyId(line.getInstructionId());
                    setEventId(exchangeOutEventId);
                    setAttrs(exchangeExtList);
                }});
            }

            // ?????? 2 ??????????????????
            Double totalStockInQty = lineStockInQtyMap.get(line.getInstructionId());
            if (totalStockInQty == null) {
                totalStockInQty = 0.0D;
            }
            List<MtExtendAttrVO1> lineAttrVO = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_attr", Collections.singletonList(line.getInstructionId()), "STOCK_IN_QTY"));
            Optional<MtExtendAttrVO1> stockInAttr = lineAttrVO.stream().filter(mtExtendAttrVO1 -> "STOCK_IN_QTY".equals(mtExtendAttrVO1.getAttrName())).findFirst();
            stockInAttr.ifPresent(mtExtendAttrVO1 -> line.setStockInQty(new BigDecimal(mtExtendAttrVO1.getAttrValue())));
            // ???????????????0
            if (line.getStockInQty() == null) {
                line.setStockInQty(BigDecimal.valueOf(0.0));
            }

            List<WmsPutInStorageVO> poLineList = mapper.queryPoLinesByInstructionId(tenantId, line.getInstructionDocId(), line.getInstructionId());

            for (WmsPutInStorageVO poLine : poLineList) {
                // ??????????????????????????????????????????????????????????????????
                if (materialLotRemainQty.compareTo(BigDecimal.valueOf(0)) == 0) {
                    break;
                }

                if (poLine.getQuantity() - poLine.getPoStockInQty() > 0) {
                    poLine.setOrderRemainQty(poLine.getQuantity() - poLine.getPoStockInQty());
                    BigDecimal transactionQty;
                    if (materialLotRemainQty.compareTo(BigDecimal.valueOf(poLine.getOrderRemainQty())) <= 0) {
                        transactionQty = materialLotRemainQty;
                    } else {
                        transactionQty = BigDecimal.valueOf(poLine.getOrderRemainQty());
                        materialLotRemainQty = materialLotRemainQty.subtract(transactionQty);
                        if (materialLotRemainQty.compareTo(BigDecimal.valueOf(0)) < 0) {
                            materialLotRemainQty = BigDecimal.valueOf(0.0);
                        }
                    }

                    // ??????????????????
                    WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                    objectTransactionVO.setEventId(putInStorageInEventId);
                    objectTransactionVO.setTransactionTypeCode("GENERAL_STKTXN");
                    objectTransactionVO.setTransactionTime(currentDate);
                    objectTransactionVO.setTransactionQty(transactionQty);
                    objectTransactionVO.setSourceDocId(poLine.getPoId());
                    objectTransactionVO.setSourceDocLineId(poLine.getPoLineId());
                    objectTransactionVO.setSourceDocNum(poLine.getInstructionDocNum());
                    objectTransactionVO.setSourceDocLineNum(poLine.getInstructionNum());
                    objectTransactionVO.setSourceDocType(poLine.getInstructionDocType());
                    objectTransactionVO.setRemark("????????????");

                    wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO, line, objectTransactionRequestList);

                    // ??????????????????
                    mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                        setEventId(putInStorageOutEventId);
                        setMaterialLotId(codeIdentifyDTO.getCodeId());
                        setLocatorId(line.getTransferLocatorId()); // ????????????
                        setPrimaryUomQty(line.getPrimaryUomQty().doubleValue());
                        setInLocatorTime(currentDate);
                    }}, "N");

                    BigDecimal newStockInQty = line.getStockInQty().add(transactionQty);
                    BigDecimal newPoLineStockInQty = BigDecimal.valueOf(poLine.getPoStockInQty()).add(transactionQty);


                    // ??????????????????????????????????????????
                    WmsPoDeliveryRel poDeliveryRel = new WmsPoDeliveryRel();
                    poDeliveryRel.setDeliveryDocId(line.getInstructionDocId());
                    poDeliveryRel.setDeliveryDocLineId(line.getInstructionId());
                    poDeliveryRel.setPoId(poLine.getPoId());
                    poDeliveryRel.setPoLineId(poLine.getPoLineId());
                    List<WmsPoDeliveryRel> wmsPoDeliveryRelList = wmsPoDeliveryRelMapper.select(poDeliveryRel);
                    if (CollectionUtils.isNotEmpty(wmsPoDeliveryRelList)) {
                        WmsPoDeliveryRel currentPoDeliveryRel = wmsPoDeliveryRelList.get(0);
                        currentPoDeliveryRel.setPoStockInQty(newPoLineStockInQty);

                        wmsPoDeliveryRelMapper.updateByPrimaryKey(currentPoDeliveryRel);
                    }

                    // ????????????????????????????????????
                    MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                    reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                    reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                    reduceUpdateOnHandVO.setLocatorId(line.getMaterialLotLocatorId());
                    reduceUpdateOnHandVO.setLotCode(line.getLot());
                    reduceUpdateOnHandVO.setEventId(putInStorageOutEventId);
                    reduceUpdateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                    // ?????????????????????????????????????????????
                    MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                    updateOnHandVO.setSiteId(line.getSiteId());
                    updateOnHandVO.setMaterialId(line.getMaterialId());
                    updateOnHandVO.setLocatorId(line.getTransferLocatorId());
                    updateOnHandVO.setLotCode(line.getLot());
                    updateOnHandVO.setEventId(putInStorageInEventId);
                    updateOnHandVO.setChangeQuantity(transactionQty.doubleValue());

                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                    totalStockInQty += newStockInQty.doubleValue();
                }
            }

            // ????????????????????????????????????INSTOCK-?????????
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName("STATUS");
            statusAttr.setAttrValue(WmsConstant.InstructionStatus.INSTOCK);
            mtExtendVO5List.add(statusAttr);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                setKeyId(line.getMaterialLotId());
                setEventId(putInStorageOutEventId);
                setAttrs(mtExtendVO5List);
            }});

            // ?????? 3???????????????????????????
            // ????????????????????????????????????????????????0???????????????????????? 3
            if (line.getInspectScrapQty().compareTo(BigDecimal.valueOf(0)) > 0) {

                String inspectScrapEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventRequestId(eventRequestId);
                    setLocatorId(lineList.get(0).getTransferLocatorId());
                    setEventTypeCode("INSPECT_SCRAP");
                }});

                WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
                objectTransactionVO.setEventId(inspectScrapEventId);
                objectTransactionVO.setTransactionTypeCode("GENERAL_SCRAPTXN");
                objectTransactionVO.setTransactionTime(currentDate);
                objectTransactionVO.setTransactionQty(line.getInspectScrapQty());
                objectTransactionVO.setSourceDocId(line.getInstructionDocId());
                objectTransactionVO.setSourceDocLineId(line.getInstructionId());
                objectTransactionVO.setSourceDocNum(line.getInstructionDocNum());
                objectTransactionVO.setSourceDocLineNum(line.getInstructionNum());
                objectTransactionVO.setSourceDocType(line.getInstructionDocType());
                objectTransactionVO.setCostCenterCode("-1");
                objectTransactionVO.setRemark("????????????");
                wmsObjectTransactionRepository.addObjectTransaction(tenantId, objectTransactionVO, line, objectTransactionRequestList);

                // ????????????????????????????????????
                MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                reduceUpdateOnHandVO.setLocatorId(line.getMaterialLotLocatorId());
                reduceUpdateOnHandVO.setLotCode(line.getLot());
                reduceUpdateOnHandVO.setEventId(inspectScrapEventId);
                reduceUpdateOnHandVO.setChangeQuantity(line.getInspectScrapQty().doubleValue());

                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                // ??????????????????
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setEventId(inspectScrapEventId);
                    setMaterialLotId(codeIdentifyDTO.getCodeId());
                    setPrimaryUomQty(line.getPrimaryUomQty().subtract(line.getInspectScrapQty()).doubleValue());
                }}, "N");

            }

            lineStockInQtyMap.put(line.getInstructionId(), totalStockInQty);

        }

        // ??????????????????ID???????????????????????????????????????????????????????????????
        Map<String, List<WmsMaterialLotLineVO>> instructionLineCollect = lineList.stream().collect(Collectors.groupingBy(WmsMaterialLotLineVO::getInstructionId));

        for (String instructionId : instructionLineCollect.keySet()) {
            if (instructionId != null) {
                WmsInstructionVO4 wmsInstructionVO4 = new WmsInstructionVO4();
                wmsInstructionVO4.setInstructionId(instructionId);
                List<WmsInstructionLineVO> instructionLineVO = mapper.queryInstructionLine(tenantId, wmsInstructionVO4);
                WmsInstructionLineVO line = instructionLineVO.get(0);
                List<MtExtendAttrVO1> mtExtendAttrVO1s = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                        new MtExtendVO1("mt_instruction_attr", Collections.singletonList(line.getInstructionId()), "STOCK_IN_EXCHANGED_QTY", "ACTUAL_RECEIVE_QTY"));
                for (MtExtendAttrVO1 extendAttr :
                        mtExtendAttrVO1s) {
                    // ?????????????????????
                    if ("STOCK_IN_EXCHANGED_QTY".equals(extendAttr.getAttrName())) {
                        line.setStockInExchangedQty(new BigDecimal(extendAttr.getAttrValue()));
                    }

                    // ??????????????????
                    if ("ACTUAL_RECEIVE_QTY".equals(extendAttr.getAttrName())) {
                        line.setActualReceiveQty(new BigDecimal(extendAttr.getAttrValue()));
                    }
                }
                Double currentTotalStockInQty = lineStockInQtyMap.get(line.getInstructionId());
                // ??????????????????????????????????????????????????????
                List<MtExtendVO5> lineStockInExtList = new ArrayList<>();
                MtExtendVO5 lineStockInQtyAttr = new MtExtendVO5();
                lineStockInQtyAttr.setAttrName("STOCK_IN_QTY");
                lineStockInQtyAttr.setAttrValue(String.valueOf(currentTotalStockInQty));
                lineStockInExtList.add(lineStockInQtyAttr);
                mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                    setKeyId(line.getInstructionId());
                    setEventId(putInStorageInEventId);
                    setAttrs(lineStockInExtList);
                }});

                if (Objects.nonNull(line.getActualReceiveQty())) {

                    // ?????????????????????????????????????????????????????????
                    MtInstructionVO instructionVO = new MtInstructionVO();
                    instructionVO.setInstructionId(line.getInstructionId());
                    instructionVO.setEventId(putInStorageInEventId);
                    BigDecimal stockInExchangedQty = line.getStockInExchangedQty();
                    if (Objects.isNull(stockInExchangedQty)) {
                        stockInExchangedQty = BigDecimal.valueOf(0.0);
                    }
                    // ?????????????????????+???????????????????????????????????????????????????
                    BigDecimal resultQty = NumberUtil.add(stockInExchangedQty, currentTotalStockInQty);
                    if (line.getActualReceiveQty().equals(resultQty)) {
                        instructionVO.setInstructionStatus("STOCK_IN_COMPLETE");
                    } else {
                        instructionVO.setInstructionStatus("STOCK_IN_EXECUTE");
                    }
                    mtInstructionRepository.instructionUpdate(tenantId, instructionVO, "N");

                    WmsPutInStorageTask condition = new WmsPutInStorageTask();
                    condition.setTenantId(tenantId);
                    condition.setInstructionDocId(line.getInstructionDocId());
                    condition.setInstructionId(line.getInstructionId());
                    WmsPutInStorageTask putInStorageTask = this.selectOne(condition);

                    if (line.getActualReceiveQty().equals(currentTotalStockInQty)) {
                        putInStorageTask.setTaskStatus("STOCKED");
                    } else {
                        putInStorageTask.setTaskStatus("STOCKING");
                    }

                    mapper.updateByPrimaryKey(putInStorageTask);
                }
            }
        }

        // ??????????????????ID???????????????????????????????????????????????????
        Map<String, List<WmsMaterialLotLineVO>> instructionDocCollect = lineList.stream().collect(Collectors.groupingBy(WmsMaterialLotLineVO::getInstructionDocId));

        for (String instructionDocId : instructionDocCollect.keySet()) {
            if (instructionDocId != null) {
                int executeInsCount = mapper.executeInstructionCount(tenantId, instructionDocId);
                MtInstructionDocDTO2 instructionDocVO = new MtInstructionDocDTO2();
                instructionDocVO.setInstructionDocId(instructionDocId);
                instructionDocVO.setEventId(exchangeOutEventId);
                if (executeInsCount == 0) {
                    instructionDocVO.setInstructionDocStatus("STOCK_IN_COMPLETE");
                } else {
                    instructionDocVO.setInstructionDocStatus("STOCK_IN_EXECUTE");
                }
                mtInstructionDocRepository.instructionDocUpdate(tenantId, instructionDocVO, "N");
            }
        }

        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

        // ??????????????????
        List<String> materialLotIdList = lineList.stream().map(WmsMaterialLotLineVO::getMaterialLotId).collect(Collectors.toList());
        List<WmsInstructionLineVO> newLineList = queryInstructionLineByLotIds(tenantId, materialLotIdList);
        WmsInstructionVO instructionVO = new WmsInstructionVO();
        instructionVO.setLocatorCode(lineList.get(0).getMaterialLotLocatorCode());

        /**
         * ???????????????????????????????????????????????? by han.zhang 2020-05-15
         */
        messageClient.sendToAll(WmsConstant.REVEIVED_BOARD_THIRTY_MATRIAL_QUANTITY_UPDATED, MtBaseConstants.YES);
        return handleData(tenantId, lineList.get(0).getInstructionDocNum(), instructionVO, newLineList, dto.getEnableDocFlag(), WmsConstant.CONSTANT_Y);
    }

}
