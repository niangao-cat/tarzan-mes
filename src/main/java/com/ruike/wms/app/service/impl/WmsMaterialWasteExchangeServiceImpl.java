package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.app.service.ItfSrmMaterialWasteIfaceService;
import com.ruike.itf.domain.repository.ItfSrmMaterialWasteIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.wms.api.dto.WmsCostCtrMaterialDTO6;
import com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO2;
import com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO3;
import com.ruike.wms.app.service.WmsMaterialWasteExchangeService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsMaterialWasteExchangeMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDetailVO2;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static io.tarzan.common.domain.util.MtBaseConstants.INSTRUCTION_TYPE.TRANSFER_OVER_LOCATOR;
import static io.tarzan.common.domain.util.MtBaseConstants.NO;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/5/7 14:50
 * @Version 1.0
 */
@Service
public class WmsMaterialWasteExchangeServiceImpl implements WmsMaterialWasteExchangeService {

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private WmsMaterialWasteExchangeMapper wmsMaterialWasteExchangeMapper;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtInstructionDocRepository instructionDocRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private MtInstructionDetailRepository mtInstructionDetailRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfSrmMaterialWasteIfaceRepository itfSrmMaterialWasteIfaceRepository;

    @Autowired
    private ItfSrmMaterialWasteIfaceService itfSrmMaterialWasteIfaceService;


    /**
     * @param tenantId 1
     * @param barCode  2
     * @return : com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO
     * @Description: ????????????  ????????????
     * @author: tong.li
     * @date 2020/5/7 15:23
     * @version 1.0
     */
    @Override
    @ProcessLovValue(targetField = {"", "materialInfoList"})
    public WmsMaterialWasteExchangeDTO2 containerOrMaterialLotQuery(Long tenantId, String barCode) {

        // ??????????????????????????????
        WmsCostCtrMaterialDTO6 materialLotDto = materialLotIdQuery(tenantId, barCode);
        String codeType = materialLotDto.getCodeType();
        List<String> materialLotIds = materialLotDto.getMaterialLotIds();

        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotIds.get(0));
        //??????API????????????SAP????????????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG);
        List<MtExtendAttrVO> materialLotAttrVO = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(materialLotAttrVO) && materialLotAttrVO.get(0).getAttrValue().equals(HmeConstants.ConstantValue.NO)) {
            throw new MtException("HME_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_MATERIAL_LOT_0003", "HME", barCode));
        }
        //???????????????????????????
        WmsMaterialWasteExchangeDTO2 nowScanRrturn = materialLotQuery(tenantId, codeType, barCode, materialLotIds);

        return nowScanRrturn;
    }

    /**
     * @param tenantId 1
     * @param lineList 2
     * @return : void
     * @Description: ????????????  ??????
     * @author: tong.li
     * @date 2020/5/8 11:34
     * @version 1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(Long tenantId, List<WmsMaterialWasteExchangeDTO3> lineList) {
        //????????? ????????????
        //List<WmsMaterialWasteExchangeDTO3> lineList = dto.getLineList();
        lineList.forEach(e -> {
            //????????????
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_material_lot_attr");
            extendVO.setKeyId(e.getMaterialLotId());
            extendVO.setAttrName("STATUS");
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            String lotStatus = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (!StringUtils.equals(lotStatus, "INSTOCK")) {
                throw new MtException("MT_INVENTORY_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0026", "INVENTORY"));
            }

            if (StringUtils.isBlank(e.getSupplierId())) {
                e.setSupplierId("");
            }
        });
        Map<String, List<WmsMaterialWasteExchangeDTO3>> listMap = lineList.stream().collect(Collectors.groupingBy(WmsMaterialWasteExchangeDTO3::getSupplierId));
        //?????????????????????Set??????
        Set<String> set = listMap.keySet();
        Iterator<String> it = set.iterator();

        //????????????????????????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "SUPPLIER_EXCHANGE_SEND");

        //??????????????????????????????
        MtEventCreateVO eventCreateOut = new MtEventCreateVO();
        eventCreateOut.setEventTypeCode("SUPPLIER_EXCHANGE_OUT");
        eventCreateOut.setEventRequestId(eventRequestId);
        String eventIdOut = mtEventRepository.eventCreate(tenantId, eventCreateOut);

        //??????????????????????????????
        MtEventCreateVO eventCreateIn = new MtEventCreateVO();
        eventCreateIn.setEventTypeCode("SUPPLIER_EXCHANGE_IN");
        eventCreateIn.setParentEventId(eventIdOut);
        String eventIdIn = mtEventRepository.eventCreate(tenantId, eventCreateIn);

        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            //??????get??????????????????????????????
            List<WmsMaterialWasteExchangeDTO3> valueList = listMap.get(key);
            //????????????????????????
            MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
            mtInstructionDoc2.setInstructionDocStatus("NEW");
            mtInstructionDoc2.setInstructionDocType("SUPPLIER_EXCHANGE_DOC");
            mtInstructionDoc2.setSiteId(valueList.get(0).getSiteId());
            mtInstructionDoc2.setSupplierId(valueList.get(0).getSupplierId());
            mtInstructionDoc2.setPersonId(DetailsHelper.getUserDetails().getUserId());
            mtInstructionDoc2.setRemark("????????????????????????");

            MtInstructionDocVO3 mtInstructionDoc3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, WmsConstant.CONSTANT_N);

            int num = 10;

            //?????????+????????????+????????????????????????????????????
            //2021-03-24 edit by chaonan.hu for kang.wang ?????????+????????????????????????????????????,???????????????????????????????????????????????????
            Map<String, List<WmsMaterialWasteExchangeDTO3>> lineMap = valueList.stream().collect(Collectors.groupingBy(dto3 -> dto3.getMaterialId() + "_" + dto3.getParentLocatorId()));
            //?????????????????????Set??????
            Set<String> setLine = lineMap.keySet();

            // ??????????????????????????????
            List<MtMaterialLotVO20> materialLotList = lineMap.values().stream().map(t -> new MtMaterialLotVO20() {{
                setMaterialLotId(t.get(0).getMaterialLotId());
                setEnableFlag(NO);
            }}).collect(Collectors.toList());
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventIdIn, NO);
            List<MtMaterialLotVO20> updateMaterialLotList = new ArrayList<>();
            Iterator<String> itLine = setLine.iterator();
            while (itLine.hasNext()) {
                String keyLine = itLine.next();
                List<WmsMaterialWasteExchangeDTO3> tempList = lineMap.get(keyLine);
                WmsMaterialWasteExchangeDTO3 wmsMaterialWasteExchangeDTO3 = tempList.get(0);
                BigDecimal sumQty = BigDecimal.valueOf(0);
                for (int i = 0; i < tempList.size(); i++) {
                    sumQty = sumQty.add(tempList.get(i).getCumulativeQty());
                }

                //????????????????????????
                MtInstructionVO createVO = new MtInstructionVO();
                createVO.setSourceDocId(mtInstructionDoc3.getInstructionDocId());
                createVO.setInstructionStatus("NEW");
                createVO.setSiteId(wmsMaterialWasteExchangeDTO3.getSiteId());
                createVO.setMaterialId(wmsMaterialWasteExchangeDTO3.getMaterialId());
                createVO.setUomId(wmsMaterialWasteExchangeDTO3.getPrimaryUomId());
                createVO.setQuantity(sumQty.doubleValue());
                createVO.setFromSiteId(wmsMaterialWasteExchangeDTO3.getSiteId());
                createVO.setFromLocatorId(wmsMaterialWasteExchangeDTO3.getParentLocatorId());
                createVO.setToSiteId(wmsMaterialWasteExchangeDTO3.getSiteId());
                //?????????24-??????????????????locator_id
                List<MtModLocator> locatorList = wmsMaterialWasteExchangeMapper.queryLocatorIdBySiteAndCode(tenantId, wmsMaterialWasteExchangeDTO3.getSiteId(), "24");
                if (CollectionUtils.isEmpty(locatorList)) {
                    throw new MtException("WMS_COST_CENTER_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0050", "WMS"));
                }

                createVO.setToLocatorId(locatorList.get(0).getLocatorId());
                createVO.setSupplierId(wmsMaterialWasteExchangeDTO3.getSupplierId());
                createVO.setInstructionType(TRANSFER_OVER_LOCATOR);

                //?????????????????????
                MtInstructionVO6 mtInstruction6 = mtInstructionRepository.instructionUpdate(tenantId, createVO, WmsConstant.CONSTANT_N);

                //?????????????????? MATERIAL_VERSION
//                List<MtExtendVO5> listM = new ArrayList<>();
//                MtExtendVO5 mtExtendVO5M = new MtExtendVO5();
//                mtExtendVO5M.setAttrName("MATERIAL_VERSION");
//                mtExtendVO5M.setAttrValue(wmsMaterialWasteExchangeDTO3.getMaterialVersion());
//                listM.add(mtExtendVO5M);
//                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstruction6.getInstructionId(), "", listM);


                //?????????????????? TO_LOCATOR_ID
                List<MtExtendVO5> listT = new ArrayList<>();
                MtExtendVO5 mtExtendVO5T = new MtExtendVO5();
                mtExtendVO5T.setAttrName("TO_LOCATOR_ID");

                MtModLocator mtModLocator = new MtModLocator();
                mtModLocator.setLocatorType("25");
                mtModLocator.setParentLocatorId(locatorList.get(0).getLocatorId());
                mtModLocator.setTenantId(tenantId);
                List<MtModLocator> mtModLocatorList = mtModLocatorRepository.select(mtModLocator);
                if (CollectionUtils.isEmpty(mtModLocatorList)) {
                    throw new MtException("WMS_COST_CENTER_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0051", "WMS", locatorList.get(0).getLocatorId(), "25"));
                }
                mtExtendVO5T.setAttrValue(mtModLocatorList.get(0).getLocatorId());
                listT.add(mtExtendVO5T);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstruction6.getInstructionId(), "", listT);

                //?????????????????? INSTRUCTION_LINE_NUM
                List<MtExtendVO5> listI = new ArrayList<>();
                MtExtendVO5 mtExtendVO5I = new MtExtendVO5();
                mtExtendVO5I.setAttrName("INSTRUCTION_LINE_NUM");
                mtExtendVO5I.setAttrValue(String.valueOf(num));
                listI.add(mtExtendVO5I);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstruction6.getInstructionId(), "", listI);
                num += 10;

                for (int i = 0; i < tempList.size(); i++) {
                    List<WmsObjectTransactionRequestVO> wmsObjectTransactionRequestVOS = loopExecute(tempList.get(i), eventIdOut, eventIdIn, mtInstructionDoc3, tenantId, mtInstruction6, mtModLocatorList.get(0).getLocatorId(), createVO.getToLocatorId());
                    //??????API {materialLotUpdate} ????????????
                    MtMaterialLotVO20 updateSn = new MtMaterialLotVO20();
                    updateSn.setMaterialLotId(tempList.get(i).getMaterialLotId());
                    updateSn.setLocatorId(createVO.getToLocatorId());
                    updateSn.setEnableFlag(WmsConstant.CONSTANT_N);
                    updateSn.setPrimaryUomQty(0d);
                    updateMaterialLotList.add(updateSn);
                    objectTransactionRequestList.addAll(wmsObjectTransactionRequestVOS);
                }

                //???????????????????????????????????????
                MtInstructionDetailVO2 mtInstructionDetailVO2 = new MtInstructionDetailVO2();
                mtInstructionDetailVO2.setInstructionId(mtInstruction6.getInstructionId());

                mtInstructionDetailVO2.setMaterialLotIdList(tempList.stream().map(WmsMaterialWasteExchangeDTO3::getMaterialLotId).collect(Collectors.toList()));
                mtInstructionDetailRepository.instructionDetailCreate(tenantId, mtInstructionDetailVO2);
            }
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateMaterialLotList, eventIdOut, NO);
        }
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);

    }

    /**
     * @param tenantId 1
     * @param barCode  2
     * @return : com.ruike.wms.api.dto.WmsCostCtrMaterialDTO6
     * @Description: ?????????????????????????????????ID?????????
     * @author: tong.li
     * @date 2020/5/7 18:49
     * @version 1.0
     */
    private WmsCostCtrMaterialDTO6 materialLotIdQuery(Long tenantId, String barCode) {
        //1. ??????????????????????????????????????????
        MtContainerVO13 containerVo13 = new MtContainerVO13();
        containerVo13.setContainerCode(barCode);
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, containerVo13);
        List<String> materialLotIds = null;
        String codeType = null;
        if (!CollectionUtils.isEmpty(containerIds)) {
            MtContLoadDtlVO10 contLoadDtlVO10 = new MtContLoadDtlVO10();

            // ??????????????????
            contLoadDtlVO10.setAllLevelFlag(WmsConstant.CONSTANT_Y);
            contLoadDtlVO10.setContainerId(containerIds.get(0));
            List<MtContLoadDtlVO4> contLoadDtls =
                    mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlVO10);

            if (CollectionUtils.isEmpty(contLoadDtls)) {
                throw new MtException("WMS_COST_CENTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0005", "WMS", barCode, ""));
            }
            codeType = "CONTAINER";
            materialLotIds = contLoadDtls.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
        } else {
            // ??????????????????????????????
            MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
            materialLotVo3.setMaterialLotCode(barCode);
            materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(materialLotIds)) {
                // ????????????????????????
                throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0006", "WMS", barCode, ""));
            }
            codeType = "MATERIAL_LOT";
        }
        WmsCostCtrMaterialDTO6 materialLotDto = new WmsCostCtrMaterialDTO6();
        materialLotDto.setCodeType(codeType);
        materialLotDto.setMaterialLotIds(materialLotIds);
        return materialLotDto;
    }

    /**
     * @param tenantId       1
     * @param codeType       2
     * @param barCode        3
     * @param materialLotIds 4
     * @return : com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO
     * @Description: ??????????????????????????????
     * @author: tong.li
     * @date 2020/5/7 18:28
     * @version 1.0
     */
    private WmsMaterialWasteExchangeDTO2 materialLotQuery(Long tenantId, String codeType, String barCode,
                                                          List<String> materialLotIds) {

        //???????????????ID???????????????????????????
        List<WmsMaterialWasteExchangeDTO2> mtMaterialLots = wmsMaterialWasteExchangeMapper.selectMaterialLotCondition(tenantId, materialLotIds);


        // ????????????
        BigDecimal cumulativeQty = BigDecimal.valueOf(0);

        //??????????????????
        int codeQty = 0;

        for (WmsMaterialWasteExchangeDTO2 materialLot : mtMaterialLots) {
            //????????????
            if (!WmsConstant.CONSTANT_Y.equalsIgnoreCase(materialLot.getEnableFlag())) {
                throw new MtException("WMS_SUPPLIER_EXCHANGE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_SUPPLIER_EXCHANGE_001", "WMS", materialLot.getMaterialLotCode()));
            }
            if (WmsConstant.CONSTANT_Y.equalsIgnoreCase(materialLot.getFreezeFlag())) {
                throw new MtException("WMS_SUPPLIER_EXCHANGE_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_SUPPLIER_EXCHANGE_002", "WMS", materialLot.getMaterialLotCode()));
            }

            //?????????
            if (StringUtils.isBlank(materialLot.getSupplierId())) {
                throw new MtException("WMS_SUPPLIER_EXCHANGE_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_SUPPLIER_EXCHANGE_003", "WMS", materialLot.getMaterialLotCode()));
            }

            //2020-12-01 add by chaonan.hu for kang.wang
            //?????????????????????????????????????????????????????????OK?????????????????????NG,??????????????????Y??????????????????
            if (!"OK".equals(materialLot.getQualityStatus())) {
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
                mtMaterialLotAttrVO2.setAttrName("NC_SUPPLIER_REPLACEMENT");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if (CollectionUtils.isEmpty(mtExtendAttrVOS) || !"Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                    throw new MtException("WMS_SUPPLIER_EXCHANGE_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_SUPPLIER_EXCHANGE_006", "WMS", materialLot.getMaterialLotCode(), materialLot.getQualityStatus()));
                }
            }

            //????????????
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_material_lot_attr");
            extendVO.setKeyId(materialLot.getMaterialLotId());
            extendVO.setAttrName("STATUS");
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            String lotStatus = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (!StringUtils.equals(lotStatus, "INSTOCK")) {
                throw new MtException("MT_INVENTORY_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0026", "INVENTORY"));
            }

            //????????????&??????
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
            mtExtendVO1.setKeyIdList(Collections.singletonList(materialLot.getMaterialLotId()));
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 extend1 = new MtExtendVO5();
            extend1.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
            attrs.add(extend1);
            MtExtendVO5 extend2 = new MtExtendVO5();
            extend2.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
            attrs.add(extend2);
            mtExtendVO1.setAttrs(attrs);
            List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            if (CollectionUtils.isNotEmpty(extendAttrList)) {
                String soNum = "";
                String soLineNum = "";
                for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrList) {
                    switch (mtExtendAttrVO1.getAttrName()) {
                        case WmsConstant.MaterialLotAttr.SO_NUM:
                            soNum = mtExtendAttrVO1.getAttrValue();
                            break;
                        case WmsConstant.MaterialLotAttr.SO_LINE_NUM:
                            soLineNum = mtExtendAttrVO1.getAttrValue();
                            break;
                    }
                }

                if (StringUtils.isNotBlank(soNum) || StringUtils.isNotBlank(soLineNum)) {
                    throw new MtException("WMS_SUPPLIER_EXCHANGE_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_SUPPLIER_EXCHANGE_005", "WMS", materialLot.getMaterialLotCode()));
                }
            }

            BigDecimal tempQty = materialLot.getPrimaryUomQty();
            cumulativeQty = cumulativeQty.add(tempQty);
            codeQty++;
        }
        WmsMaterialWasteExchangeDTO2 materialLotFirst = mtMaterialLots.get(0);

        materialLotFirst.setCumulativeQty(cumulativeQty);
        materialLotFirst.setCodeQty(codeQty);
        materialLotFirst.setBarCode(barCode);
        materialLotFirst.setCodeType(codeType);
        if (StringUtils.isNotBlank(materialLotFirst.getLocatorId())) {
            List<MtModLocator> mtModLocatorList = wmsMaterialWasteExchangeMapper.queryParentLocatorByLocatorId(tenantId, materialLotFirst.getLocatorId(), "");
            if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
                materialLotFirst.setParentLocatorId(mtModLocatorList.get(0).getLocatorId());
                materialLotFirst.setParentLocatorCode(mtModLocatorList.get(0).getLocatorCode());
            }
        }

        List<WmsMaterialWasteExchangeDTO3> lineList = new ArrayList<>();
        WmsMaterialWasteExchangeDTO3 line = new WmsMaterialWasteExchangeDTO3();
        BeanUtils.copyProperties(materialLotFirst, line);
        lineList.add(line);
        materialLotFirst.setLineList(lineList);
        return materialLotFirst;
    }


    /**
     * @param dto3
     * @return : void
     * @Description: ??????????????????????????????????????????????????????
     * @author: tong.li
     * @date 2020/5/8 16:07
     * @version 1.0
     */
    private List<WmsObjectTransactionRequestVO> loopExecute(WmsMaterialWasteExchangeDTO3 dto3, String eventIdOut, String eventIdIn, MtInstructionDocVO3 mtInstructionDocVO3, Long tenantId, MtInstructionVO6 mtInstructionVO6, String locatorId, String parentLocatorId) {
        //??????1:?????????????????????-??????????????????

        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();

        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
        objectTransactionVO.setTransactionTypeCode("WMS_WAREHOUSE_TRAN");
        objectTransactionVO.setEventId(eventIdIn);
        objectTransactionVO.setMaterialLotId(dto3.getMaterialLotId());
        objectTransactionVO.setMaterialId(dto3.getMaterialId());
        objectTransactionVO.setMaterialCode(dto3.getMaterialCode());
        objectTransactionVO.setTransactionQty(dto3.getCumulativeQty());
        objectTransactionVO.setLotNumber(dto3.getLot());
        objectTransactionVO.setTransferLotNumber("20100101");
        objectTransactionVO.setTransactionUom(dto3.getPrimaryUomCode());
        objectTransactionVO.setTransactionTime(new Date());
        objectTransactionVO.setTransactionReasonCode("??????????????????");
        objectTransactionVO.setPlantId(dto3.getSiteId());
        String plantCode = wmsMaterialWasteExchangeMapper.queryPlantCode(tenantId, dto3.getSiteId());
        objectTransactionVO.setPlantCode(plantCode);
        objectTransactionVO.setBarcode(dto3.getBarCode());
        List<MtModLocator> mtModLocatorList = wmsMaterialWasteExchangeMapper.queryParentLocatorByLocatorId(tenantId, dto3.getLocatorId(), "");
        objectTransactionVO.setWarehouseId(CollectionUtils.isNotEmpty(mtModLocatorList) ? mtModLocatorList.get(0).getLocatorId() : "");
        objectTransactionVO.setLocatorId(dto3.getLocatorId());
        objectTransactionVO.setTransferPlantId(dto3.getSiteId());

        objectTransactionVO.setTransferWarehouseId(parentLocatorId);
        objectTransactionVO.setTransferLocatorId(locatorId);
        objectTransactionVO.setSupplierCode(dto3.getSupplierCode());
        MtInstructionDoc mtInstructionDoc = instructionDocRepository.instructionDocPropertyGet(tenantId, mtInstructionDocVO3.getInstructionDocId());
        objectTransactionVO.setSourceDocId(mtInstructionDoc.getInstructionDocId());
        objectTransactionVO.setSourceDocLineId(mtInstructionVO6.getInstructionId());
        WmsTransactionType transactionType = new WmsTransactionType();
        transactionType.setTransactionTypeCode("WMS_WAREHOUSE_TRAN");
        List<WmsTransactionType> typeList = wmsTransactionTypeRepository.select(transactionType);
        objectTransactionVO.setMoveType(CollectionUtils.isEmpty(typeList) ? "" : typeList.get(0).getMoveType());
        objectTransactionVO.setRemark("??????????????????");

        objectTransactionRequestList.add(objectTransactionVO);
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        String interfaceFlag = lovValueDTOS.get(0).getMeaning();

        //??????2 ?????????????????????????????????????????????
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(dto3.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(dto3.getLocatorId());
        mtInvOnhandQuantityVO9.setMaterialId(dto3.getMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(dto3.getCumulativeQty().doubleValue());
        mtInvOnhandQuantityVO9.setLotCode(dto3.getLot());
        mtInvOnhandQuantityVO9.setEventId(eventIdOut);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        //??????3 ????????????????????????????????????????????????
        MtInvOnhandQuantityVO9 addQty = new MtInvOnhandQuantityVO9();
        addQty.setSiteId(dto3.getSiteId());
        addQty.setLocatorId(locatorId);
        addQty.setMaterialId(dto3.getMaterialId());
        addQty.setChangeQuantity(dto3.getCumulativeQty().doubleValue());
        addQty.setEventId(eventIdIn);
        addQty.setLotCode("20100101");
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto3.getMaterialLotId());
        if (StringUtils.isBlank(mtMaterialLot.getSupplierId())) {
            throw new MtException("WMS_SUPPLIER_EXCHANGE_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_SUPPLIER_EXCHANGE_003", "WMS", mtMaterialLot.getMaterialLotCode()));
        }
        addQty.setOwnerId(mtMaterialLot.getSupplierId());
        addQty.setOwnerType("SI");
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, addQty);
        // ?????????????????????????????????
        if ("Y".equals(interfaceFlag)) {
            try {
                String siteId = addQty.getSiteId();
                String materialId = addQty.getMaterialId();
                String locatorId1 = addQty.getLocatorId();
                String ownerId = addQty.getOwnerId();
                List<ItfSrmMaterialWasteIfaceSyncDTO> syncDTOS = itfSrmMaterialWasteIfaceRepository.selectSrmMaterialWaste(tenantId, siteId, materialId, locatorId1, ownerId);
                itfSrmMaterialWasteIfaceService.srmMaterialWasteExchangeCreate(syncDTOS, tenantId);
            } catch (CommonException e) {
                e.printStackTrace();
            }
        }
        //??????4 : ????????????
        List<MtExtendVO5> listW = new ArrayList<>();
        MtExtendVO5 mtExtendVO5W = new MtExtendVO5();
        mtExtendVO5W.setAttrName("WAREHOUSE_ID");
        mtExtendVO5W.setAttrValue(parentLocatorId);
        listW.add(mtExtendVO5W);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto3.getMaterialLotId(), "", listW);

        return objectTransactionRequestList;
    }
}
