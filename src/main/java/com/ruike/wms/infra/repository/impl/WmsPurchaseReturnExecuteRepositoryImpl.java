package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.wms.api.dto.WmsPoDeliveryConfirmDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryDetailDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryScanDTO2;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsPurchaseReturnExecuteRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsPurchaseReturnExecuteMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.actual.domain.vo.MtInstructionActualVO1;
import tarzan.actual.infra.mapper.MtInstructionActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO10;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购退货-执行
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 19:55
 */
@Component
public class WmsPurchaseReturnExecuteRepositoryImpl implements WmsPurchaseReturnExecuteRepository {

    @Autowired
    private WmsPurchaseReturnExecuteMapper wmsPurchaseReturnExecuteMapper;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtInstructionActualMapper mtInstructionActualMapper;

    @Autowired
    private WmsCommonApiService commonApiService;

    @Autowired
    private MtEventRequestRepository eventRequestRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Override
    @ProcessLovValue(targetField = {"","linePurchaseList",""})
    public WmsPurchaseReturnExecuteDocVO scanInstructionDocNum(Long tenantId, String instructionDocNum) {
        //校验参数
        if(StringUtils.isBlank(instructionDocNum)){
            throw new MtException("WMS_INV_TRANSFER_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0028", "WMS", "instructionDocNum"));
        }
        List<LovValueDTO> poTypeList = lovAdapter.queryLovValue("WMS.PO.TYPE", tenantId);
        List<String> typeList = poTypeList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        List<MtInstructionDoc> instructionDocList = mtInstructionDocRepository.selectByCondition(Condition.builder(MtInstructionDoc.class)
                .andWhere(Sqls.custom().andEqualTo(MtInstructionDoc.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtInstructionDoc.FIELD_INSTRUCTION_DOC_NUM, instructionDocNum)
                        .andIn(MtInstructionDoc.FIELD_INSTRUCTION_DOC_TYPE, typeList)).build());
        if(CollectionUtils.isEmpty(instructionDocList)){
            throw new MtException("WMS_PUT_IN_STOCK_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PUT_IN_STOCK_002", "WMS"));
        }
        //校验状态
        if(!WmsConstant.InstructionStatus.RELEASED.equals(instructionDocList.get(0).getInstructionDocStatus())){
            throw new MtException("MT_INVENTORY_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0023", "INVENTORY"));
        }

        WmsPurchaseReturnExecuteDocVO docVO = new WmsPurchaseReturnExecuteDocVO();
        BeanUtils.copyProperties(instructionDocList.get(0), docVO);
        //供应商
        MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(docVO.getSupplierId());
        if(mtSupplier != null){
            docVO.setSupplierName(mtSupplier.getSupplierName());
            docVO.setSupplierCode(mtSupplier.getSupplierCode());
        }

        //查询行信息
        List<MtInstruction> instructionList = mtInstructionRepository.select(new MtInstruction() {{
            setTenantId(tenantId);
            setSourceDocId(docVO.getInstructionDocId());
            setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
        }});

        List<WmsPurchaseLineVO> linePurchaseList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(instructionList)){
            List<String> instructionIdList = instructionList.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_instruction_attr");
            mtExtendVO1.setKeyIdList(instructionIdList);
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 extend1 = new MtExtendVO5();
            extend1.setAttrName("PO_RETURN_FLAG");
            attrs.add(extend1);
            mtExtendVO1.setAttrs(attrs);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            List<MtExtendAttrVO1> attrList = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getAttrValue(), "X")).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(attrList)){
                throw new MtException("RK_INVENTORY_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "RK_INVENTORY_0054", "INVENTORY"));
            }
            //筛选退货的行
            List<String> instructionIds = attrList.stream().map(MtExtendAttrVO1::getKeyId).distinct().collect(Collectors.toList());

            linePurchaseList = wmsPurchaseReturnExecuteMapper.queryPurchaseLineInfo(tenantId, instructionIds);
            for (WmsPurchaseLineVO lineVO : linePurchaseList) {
                List<WmsPurchaseCodeDetailsVO> lineVOList = wmsPurchaseReturnExecuteMapper.queryBarcodeListByLineId(tenantId, lineVO.getInstructionId());
                lineVO.setMaterialLotList(lineVOList);
            }
        }
        docVO.setLinePurchaseList(linePurchaseList);
        return docVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue(targetField = {"", "materialLotList",""})
    public WmsPurchaseReturnScanVO scanMaterialLotCode(Long tenantId, WmsPoDeliveryScanDTO2 dto2) {
        WmsPurchaseReturnScanVO returnScanVO = new WmsPurchaseReturnScanVO();
        if(StringUtils.isNotBlank(dto2.getBarcode())){
            dto2.setMaterialLotCode(dto2.getBarcode());
        }
        returnScanVO.setBarcode(dto2.getMaterialLotCode());
        if(StringUtils.isBlank(dto2.getMaterialLotCode())){
            throw new MtException("WMS_INV_TRANSFER_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0028", "WMS", "materialLotCode"));
        }
        if(StringUtils.isBlank(dto2.getInstructionDocNum())){
            throw new MtException("WMS_INV_TRANSFER_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0028", "WMS", "instructionDocNum"));
        }
        //判断条码为实物条码/容器条码
        List<MtContainer> containerList = mtContainerRepository.select(new MtContainer() {{
            setTenantId(tenantId);
            setContainerCode(dto2.getMaterialLotCode());
        }});
        //物料批集合
        List<String> mtMaterialLotIdList = new ArrayList<>();
        //找到则为容器条码
        if(CollectionUtils.isNotEmpty(containerList)){
            //校验容器是否存在上层容器
            MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
            contLoadDtlVo5.setLoadObjectId(containerList.get(0).getContainerId());
            contLoadDtlVo5.setLoadObjectType(HmeConstants.LoadTypeCode.CONTAINER);
            List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
            if(CollectionUtils.isNotEmpty(containerIdList)){
                throw new MtException("WMS_COST_CENTER_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0029", "WMS", dto2.getMaterialLotCode()));
            }
            //获取容器条码下所有实物条码
            MtContLoadDtlVO10 dtlVO10 = new MtContLoadDtlVO10();
            dtlVO10.setContainerId(containerList.get(0).getContainerId());
            dtlVO10.setAllLevelFlag(WmsConstant.CONSTANT_Y);
            List<MtContLoadDtlVO4> mtContLoadDtlVO4List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, dtlVO10);
            if(CollectionUtils.isEmpty(mtContLoadDtlVO4List)){
                throw new MtException("WMS_DISTRIBUTION_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0018", "WMS", dto2.getMaterialLotCode()));
            }
            List<String> materialLotIdList = mtContLoadDtlVO4List.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
            mtMaterialLotIdList.addAll(materialLotIdList);
            returnScanVO.setBarcodeType("CONTAINER");

        }else {
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.select(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto2.getMaterialLotCode());
            }});
            if(CollectionUtils.isNotEmpty(materialLotList)){
                mtMaterialLotIdList.add(materialLotList.get(0).getMaterialLotId());

                //条码是否存在上层容器
                MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
                contLoadDtlVo5.setLoadObjectId(materialLotList.get(0).getMaterialLotId());
                contLoadDtlVo5.setLoadObjectType("MATERIAL_LOT");
                List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
                if(CollectionUtils.isNotEmpty(containerIdList)){
                    throw new MtException("WMS_COST_CENTER_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0030", "WMS", materialLotList.get(0).getMaterialLotCode()));
                }
                returnScanVO.setBarcodeType("MATERIAL_LOT");
                returnScanVO.setPrimaryUomQty(BigDecimal.valueOf(materialLotList.get(0).getPrimaryUomQty()));
            }else {
                throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0006", "WMS", dto2.getMaterialLotCode()));
            }
        }
        List<WmsPurchaseReturnCodeVO> codeVOList = wmsPurchaseReturnExecuteMapper.queryPurchaseMaterialLotList(tenantId, mtMaterialLotIdList);
        List<MtInstructionDoc> mtInstructionDocList = mtInstructionDocRepository.select(new MtInstructionDoc() {{
            setTenantId(tenantId);
            setInstructionDocNum(dto2.getInstructionDocNum());
        }});
        Double totalQty = codeVOList.stream().map(WmsPurchaseReturnCodeVO::getPrimaryUomQty).mapToDouble(Double::doubleValue).sum();
        returnScanVO.setPrimaryUomQty(BigDecimal.valueOf(totalQty));
        List<MtInstruction> lineList = mtInstructionRepository.select(new MtInstruction() {{
            setTenantId(tenantId);
            setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
            setSourceDocId(mtInstructionDocList.get(0).getInstructionDocId());
        }});
        //验证条码
        this.verifyMaterialLotCode(tenantId, codeVOList, lineList, dto2.getInstructionDocNum());
        //行状态
        List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("Z_INSTRUCTION_DOC_STATUS", tenantId);
        for (WmsPurchaseReturnCodeVO wmsPurchaseReturnCodeVO : codeVOList) {
            //创建事件
            MtEventCreateVO eventCreateIn = new MtEventCreateVO();
            eventCreateIn.setEventTypeCode("MATERIALLOT_SCAN");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateIn);
            //物料加版本找到条码对应的行
            List<WmsPurchaseLineVO> lineVO = wmsPurchaseReturnExecuteMapper.queryLineByBarcodeAndDocNum(tenantId, wmsPurchaseReturnCodeVO.getMaterialLotId(), dto2.getInstructionDocNum());
            if(CollectionUtils.isEmpty(lineVO) || lineVO.size() > 1){
                throw new MtException("WMS_DISTRIBUTION_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_009", "WMS", wmsPurchaseReturnCodeVO.getMaterialLotCode()));
            }
            List<MtInstructionActual> actualList = mtInstructionActualRepository.instructionLimitActualBatchGet(tenantId, Collections.singletonList(lineVO.get(0).getInstructionId()));

            //若执行数量超发 则报错
            if(lineVO.get(0).getQuantity().compareTo(lineVO.get(0).getActualQty().add(BigDecimal.valueOf(wmsPurchaseReturnCodeVO.getPrimaryUomQty())).doubleValue()) < 0){
                throw new MtException("WMS_DISTRIBUTION_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_011", "WMS"));
            }
            //校验行状态
            if(!WmsConstant.InstructionStatus.RELEASED.equals(lineVO.get(0).getInstructionStatus())){
                Optional<LovValueDTO> statusFirst = valueDTOList.stream().filter(dto -> StringUtils.equals(lineVO.get(0).getInstructionStatus(), dto.getValue())).findFirst();
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setTableName("mt_instruction_attr");
                mtExtendVO.setKeyId(lineVO.get(0).getInstructionId());
                mtExtendVO.setAttrName("INSTRUCTION_LINE_NUM");
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                String lineNum = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                throw new MtException("WMS_STOCKTAKE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_STOCKTAKE_001", "WMS", lineVO.get(0).getInstructionNum(), lineNum, statusFirst.isPresent() ? statusFirst.get().getMeaning() : ""));
            }
            //指令执行
            MtInstructionActualVO mtInstructionVO1 = new MtInstructionActualVO();
            MtInstructionActualVO1 mtInstructionActualVO1;
            BigDecimal actualQty = BigDecimal.valueOf(wmsPurchaseReturnCodeVO.getPrimaryUomQty()).add(lineVO.get(0).getActualQty());
            mtInstructionVO1.setEventId(eventId);
            mtInstructionVO1.setActualQty(wmsPurchaseReturnCodeVO.getPrimaryUomQty());
            if(CollectionUtils.isNotEmpty(actualList)){
                mtInstructionVO1.setActualId(actualList.get(0).getActualId());
                mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionVO1);
            }else {
                mtInstructionVO1.setInstructionId(lineVO.get(0).getInstructionId());
                mtInstructionVO1.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
                mtInstructionVO1.setMaterialId(wmsPurchaseReturnCodeVO.getMaterialId());
                mtInstructionVO1.setUomId(wmsPurchaseReturnCodeVO.getPrimaryUomId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(wmsPurchaseReturnCodeVO.getLocatorId());
                if(mtModLocator != null){
                    mtInstructionVO1.setToLocatorId(mtModLocator.getParentLocatorId());
                }
                mtInstructionVO1.setToSiteId(wmsPurchaseReturnCodeVO.getSiteId());
                mtInstructionVO1.setSourceOrderType(mtInstructionDocList.get(0).getInstructionDocType());
                mtInstructionVO1.setSourceOrderId(mtInstructionDocList.get(0).getInstructionDocId());
                mtInstructionVO1.setSupplierId(wmsPurchaseReturnCodeVO.getSupplierId());
                mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionVO1);
            }
            //更新明细
            MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
            actualDetail.setActualId(mtInstructionActualVO1.getActualId());
            actualDetail.setMaterialLotId(wmsPurchaseReturnCodeVO.getMaterialLotId());
            actualDetail.setUomId(wmsPurchaseReturnCodeVO.getPrimaryUomId());
            actualDetail.setActualQty(wmsPurchaseReturnCodeVO.getPrimaryUomQty());
            if(CollectionUtils.isNotEmpty(containerList)){
                actualDetail.setContainerId(containerList.get(0).getContainerId());
                wmsPurchaseReturnCodeVO.setContainerId(containerList.get(0).getContainerId());
            }
            actualDetail.setFromLocatorId(wmsPurchaseReturnCodeVO.getLocatorId());
            mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, actualDetail);

            //条码更新
            List<MtExtendVO5> mtExtendList = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("STATUS");
            mtExtendVO5.setAttrValue("SCANNED");
            mtExtendList.add(mtExtendVO5);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                {
                    setKeyId(wmsPurchaseReturnCodeVO.getMaterialLotId());
                    setEventId(eventId);
                    setAttrs(mtExtendList);
                }
            });
            //退货采购订单行更新
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            BigDecimal quantity = lineVO.get(0).getQuantity() != null ? BigDecimal.valueOf(lineVO.get(0).getQuantity()) : BigDecimal.ZERO;
            if (actualQty.compareTo(BigDecimal.ZERO) >= 0 && actualQty.compareTo(quantity) < 0 ) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
            }else if (actualQty.compareTo(quantity) == 0) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
            }
            mtInstructionVO.setInstructionId(lineVO.get(0).getInstructionId());
            mtInstructionVO.setEventId(eventId);
            mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
        }
        returnScanVO.setMaterialLotList(codeVOList);
        return returnScanVO;
    }

    /**
     * 若行状态全部完成则更新头
     *
     * @param tenantId
     * @param mtInstructionDoc
     * @author sanfeng.zhang@hand-china.com 2020/11/18 15:36
     * @return void
     */
    private void updateInstructionDocComplete(Long tenantId, MtInstructionDoc mtInstructionDoc){
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(mtInstructionDoc.getInstructionDocId());
        mtInstructionVO10.setInstructionType("RECEIVE_FROM_SUPPLIER");
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_instruction_attr");
        mtExtendVO1.setKeyIdList(instructionIdList);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName("PO_RETURN_FLAG");
        attrs.add(extend1);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        List<MtExtendAttrVO1> attrList = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getAttrValue(), "X")).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(attrList)){
            throw new MtException("RK_INVENTORY_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "RK_INVENTORY_0054", "INVENTORY"));
        }
        //筛选退货的行
        List<String> instructionIds = attrList.stream().map(MtExtendAttrVO1::getKeyId).distinct().collect(Collectors.toList());
        List<MtInstruction> instructionList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIds);
        if(CollectionUtils.isNotEmpty(instructionList)){
            Optional<MtInstruction> firstOpt = instructionList.stream().filter(dto -> !StringUtils.equals(dto.getInstructionStatus(), WmsConstant.InstructionStatus.COMPLETED)).findFirst();
            if(!firstOpt.isPresent()){
                mtInstructionDoc.setInstructionDocStatus(WmsConstant.InstructionStatus.COMPLETED);
            }else {
                mtInstructionDoc.setInstructionDocStatus(WmsConstant.InstructionStatus.RELEASED);
            }
            mtInstructionDocRepository.updateByPrimaryKeySelective(mtInstructionDoc);
        }
    }

    @Override
    @ProcessLovValue(targetField = {"","lineList",""})
    public WmsPurchaseReturnExecuteDetailsVO queryCodeDetails(Long tenantId, String instructionId) {
        //查询头信息
        List<WmsPurchaseLineVO> wmsPurchaseLineVOS = wmsPurchaseReturnExecuteMapper.queryPurchaseLineInfo(tenantId, Collections.singletonList(instructionId));
        WmsPurchaseReturnExecuteDetailsVO detailsVO = new WmsPurchaseReturnExecuteDetailsVO();
        BeanUtils.copyProperties(wmsPurchaseLineVOS.get(0), detailsVO);
        detailsVO.setDocStatus(wmsPurchaseLineVOS.get(0).getInstructionStatus());
        detailsVO.setInstructionStatusMeaning(wmsPurchaseLineVOS.get(0).getInstructionStatusMeaning());

        //查询物料批信息
        List<WmsPurchaseCodeDetailsVO> lineVOList = wmsPurchaseReturnExecuteMapper.queryBarcodeListByLineId(tenantId, instructionId);
        detailsVO.setLineList(lineVOList);
        return detailsVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsPurchaseLineVO deleteCodeDetails(Long tenantId, List<WmsPoDeliveryDetailDTO> dtoList) {
        WmsPurchaseLineVO lineVO = new WmsPurchaseLineVO();
        for (WmsPoDeliveryDetailDTO wmsPoDeliveryDetailDTO : dtoList) {
            //创建事件
            MtEventCreateVO eventCreateIn = new MtEventCreateVO();
            eventCreateIn.setEventTypeCode("MATERIALLOT_SCAN_CANCEL");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateIn);
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.select(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(wmsPoDeliveryDetailDTO.getMaterialLotCode());
            }});
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_material_lot_attr");
            mtExtendVO.setKeyId(materialLotList.get(0).getMaterialLotId());
            mtExtendVO.setAttrName("STATUS");
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if(CollectionUtils.isNotEmpty(attrVOList)){
                if(!WmsConstant.MaterialLotStatus.SCANNED.equals(attrVOList.get(0).getAttrValue())){
                    throw new MtException("WMS_DISTRIBUTION_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_010", "WMS"));
                }
            }
            List<MtInstructionActual> instructionActualList = mtInstructionActualRepository.select(new MtInstructionActual() {{
                setTenantId(tenantId);
                setInstructionId(wmsPoDeliveryDetailDTO.getInstructionId());
            }});
            BigDecimal actualQty = BigDecimal.valueOf(instructionActualList.get(0).getActualQty()).subtract(BigDecimal.valueOf(materialLotList.get(0).getPrimaryUomQty()));
            MtInstructionActualVO mtInstructionVO1 = new MtInstructionActualVO();
            mtInstructionVO1.setActualId(instructionActualList.get(0).getActualId());
            mtInstructionVO1.setActualQty(BigDecimal.ZERO.subtract(BigDecimal.valueOf(materialLotList.get(0).getPrimaryUomQty())).doubleValue());
            mtInstructionVO1.setEventId(eventId);
            mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionVO1);
            //删除明细数据
            MtInstructionActualDetail mtInstructionActualDetail2 = new MtInstructionActualDetail();
            mtInstructionActualDetail2.setTenantId(tenantId);
            mtInstructionActualDetail2.setActualId(instructionActualList.get(0).getActualId());
            mtInstructionActualDetail2.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
            mtInstructionActualDetailRepository.delete(mtInstructionActualDetail2);
            //条码更新
            List<MtExtendVO5> mtExtendList = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("STATUS");
            mtExtendVO5.setAttrValue("INSTOCK");
            mtExtendList.add(mtExtendVO5);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                {
                    setKeyId(materialLotList.get(0).getMaterialLotId());
                    setEventId(eventId);
                    setAttrs(mtExtendList);
                }
            });
            //退货采购订单行更新
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(wmsPoDeliveryDetailDTO.getInstructionId());
            BigDecimal quantity = mtInstruction.getQuantity() != null ? BigDecimal.valueOf(mtInstruction.getQuantity()) : BigDecimal.ZERO;
            if (actualQty.compareTo(BigDecimal.ZERO) >= 0 && actualQty.compareTo(quantity) < 0) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
            }else if (actualQty.compareTo(quantity) == 0) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
            }
            if (StringUtils.isNotEmpty(mtInstructionVO.getInstructionStatus())) {
                List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.DELIVERY_DOC_LINE.STATUS", mtInstructionVO.getInstructionStatus());
                lineVO.setInstructionStatusMeaning(lovList.get(0).getMeaning());
                lineVO.setInstructionStatus(mtInstructionVO.getInstructionStatus());
            }
            mtInstructionVO.setInstructionId(mtInstruction.getInstructionId());
            mtInstructionVO.setEventId(eventId);
            mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
        }
        return lineVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsPoDeliveryConfirmDTO purchaseReturnExecute(Long tenantId, WmsPoDeliveryConfirmDTO confirmDTO) {
        //根据单据信息
        MtInstructionDoc instructionDoc = mtInstructionDocRepository.selectOne(new MtInstructionDoc() {{
            setInstructionDocNum(confirmDTO.getInstructionDocNum());
            setTenantId(tenantId);
        }});
        String docId = instructionDoc.getInstructionDocId();

        //校验条码状态
        Long codeCount = wmsPurchaseReturnExecuteMapper.queryScannedMaterialLot(tenantId, docId);
        if(codeCount.compareTo(0L) <= 0){
            throw new MtException("WMS_DISTRIBUTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0013", "WMS"));
        }
        //校验单据
        Long qtyCount = wmsPurchaseReturnExecuteMapper.queryActualQtyCount(tenantId, docId);
        if(qtyCount.compareTo(0L) <= 0){
            throw new MtException("WMS_DISTRIBUTION_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_008", "WMS"));
        }

        //不允许部分退货
        Boolean flag = this.queryAllReturn(tenantId, docId);
        if(flag){
            throw new MtException("WMS_DISTRIBUTION_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_012", "WMS"));
        }

        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(docId);
        mtInstructionVO10.setInstructionType("RECEIVE_FROM_SUPPLIER");
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        List<MtInstruction> instructionList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);

        //请求事件
        String eventRequestId = eventRequestRepository.eventRequestCreate(tenantId, "RETURN_TO_SUPPLIER");
        // 创建eventId
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode("RETURN_TO_SUPPLIER");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = new ArrayList<>();
        for (MtInstruction mtInstruction : instructionList) {
            List<WmsPurchaseCodeDetailsVO> lineVOList = wmsPurchaseReturnExecuteMapper.queryBarcodeListByLineId(tenantId, mtInstruction.getInstructionId());
            List<WmsPurchaseCodeDetailsVO> scannedCodeList = lineVOList.stream().filter(dto -> StringUtils.equals(dto.getCodeStatus(), "SCANNED")).collect(Collectors.toList());
            List<WmsObjectTransactionRequestVO> transactionList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(scannedCodeList)){
                List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
                for (WmsPurchaseCodeDetailsVO detailsVO : scannedCodeList) {
                    //记录采购退货事务
                    WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                    requestVO.setTransactionTypeCode("WMS_RETURN_TO_SUPP");
                    requestVO.setEventId(eventId);
                    requestVO.setMaterialLotId(detailsVO.getMaterialLotId());
                    requestVO.setMaterialId(detailsVO.getMaterialId());
                    requestVO.setTransactionQty(detailsVO.getPrimaryUomQty());
                    requestVO.setLotNumber(detailsVO.getLot());
                    requestVO.setTransactionUom(detailsVO.getUomCode());
                    requestVO.setTransactionTime(new Date());
                    requestVO.setTransactionReasonCode("采购退货");
                    requestVO.setPlantId(detailsVO.getSiteId());
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(detailsVO.getLocatorId());
                    requestVO.setWarehouseId(mtModLocator != null ? mtModLocator.getParentLocatorId() : "");
                    requestVO.setLocatorId(detailsVO.getLocatorId());
                    requestVO.setSourceDocType(instructionDoc.getInstructionDocType());
                    requestVO.setSourceDocId(instructionDoc.getInstructionDocId());
                    requestVO.setSourceDocLineId(mtInstruction.getInstructionId());
                    requestVO.setPoId(instructionDoc.getInstructionDocId());
                    requestVO.setPoLineId(mtInstruction.getInstructionId());
                    requestVO.setContainerId(detailsVO.getContainerId());
                    //查询移动类型
                    List<WmsTransactionType> transactionTypes = wmsTransactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class)
                            .andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, requestVO.getTransactionTypeCode())
                            ).build());
                    if (CollectionUtils.isNotEmpty(transactionTypes)) {
                        requestVO.setMoveType(transactionTypes.get(0).getMoveType());
                    }
                    transactionList.add(requestVO);

                    //批量更新条码状态
                    MtCommonExtendVO6 vo6 = new MtCommonExtendVO6();
                    vo6.setKeyId(detailsVO.getMaterialLotId());
                    List<MtCommonExtendVO5> attrs = new ArrayList<>();
                    MtCommonExtendVO5 vo5 = new MtCommonExtendVO5();
                    vo5.setAttrName("STATUS");
                    vo5.setAttrValue("SHIPPED");
                    attrs.add(vo5);
                    vo6.setAttrs(attrs);
                    attrPropertyList.add(vo6);
                }
                if(CollectionUtils.isNotEmpty(transactionList)){
                    //保存采购退货事务
                    List<WmsObjectTransactionResponseVO> transactionResponseVOList = wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionList);
                    if(CollectionUtils.isNotEmpty(transactionResponseVOList)){
                        wmsObjectTransactionResponseVOS.addAll(transactionResponseVOList);
                    }
                }

                // 调用{ sequenceLimitMaterialLotBatchConsume }进行消耗
                MtMaterialLotVO15 mtMaterialLotVO15 = new MtMaterialLotVO15();
                mtMaterialLotVO15.setAllConsume(WmsConstant.CONSTANT_Y);
                mtMaterialLotVO15.setInstructionDocId(docId);
                mtMaterialLotVO15.setEventRequestId(eventRequestId);
                mtMaterialLotVO15.setMtMaterialLotSequenceList(scannedCodeList.stream()
                        .map(detail -> {
                            MtMaterialLotVO16 mtMaterialLotVO161 = new MtMaterialLotVO16();
                            mtMaterialLotVO161.setMaterialLotId(detail.getMaterialLotId());
                            return mtMaterialLotVO161;
                        }).collect(Collectors.toList()));
                mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsume(tenantId, mtMaterialLotVO15);


                //批量更新扩展字段
                if(CollectionUtils.isNotEmpty(attrPropertyList)){
                    mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
                }

                //退货采购订单行更新
                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                BigDecimal actualQty = scannedCodeList.get(0).getActualQty();
                BigDecimal quantity = mtInstruction.getQuantity() != null ? BigDecimal.valueOf(mtInstruction.getQuantity()) : BigDecimal.ZERO;
                if (actualQty.compareTo(BigDecimal.ZERO) >= 0 && actualQty.compareTo(quantity) < 0 ) {
                    mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
                }else if (actualQty.compareTo(quantity) == 0) {
                    mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
                }
                mtInstructionVO.setInstructionId(mtInstruction.getInstructionId());
                mtInstructionVO.setEventId(eventId);
                mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
            }
        }

        //单据状态更新
        this.updateInstructionDocComplete(tenantId, instructionDoc);
        //调用实时接口
        if(CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS)){
            itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
        }
        return confirmDTO;
    }

    @Override
    public Boolean queryAllReturn(Long tenantId, String instructionDocId) {
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(instructionDocId);
        mtInstructionVO10.setInstructionType("RECEIVE_FROM_SUPPLIER");
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_instruction_attr");
        mtExtendVO1.setKeyIdList(instructionIdList);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName("PO_RETURN_FLAG");
        attrs.add(extend1);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        List<MtExtendAttrVO1> attrList = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getAttrValue(), "X")).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(attrList)){
            throw new MtException("RK_INVENTORY_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "RK_INVENTORY_0054", "INVENTORY"));
        }
        //筛选退货的行
        List<String> instructionIds = attrList.stream().map(MtExtendAttrVO1::getKeyId).distinct().collect(Collectors.toList());
        List<MtInstruction> instructionList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIds);

        Boolean result = false;
        for (MtInstruction mtInstruction : instructionList) {
            Boolean flag = verifyInstructionDoc(tenantId, mtInstruction);
            if(!flag){
                result = true;
            }
        }
        return result;
    }

    private Boolean verifyInstructionDoc(Long tenantId, MtInstruction mtInstruction){
        //校验执行数量
        List<MtInstructionActual> actualList = mtInstructionActualRepository.select(new MtInstructionActual() {{
            setTenantId(tenantId);
            setInstructionId(mtInstruction.getInstructionId());
            setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
        }});
        BigDecimal actualQty = BigDecimal.ZERO;
        BigDecimal quantity = mtInstruction.getQuantity() != null ? BigDecimal.valueOf(mtInstruction.getQuantity()) : BigDecimal.ZERO;
        if(CollectionUtils.isNotEmpty(actualList)){
            if(actualList.get(0).getActualQty() != null){
                actualQty = BigDecimal.valueOf(actualList.get(0).getActualQty());
            }
        }
        if(actualQty.compareTo(quantity) != 0){
            return false;
        }
        return true;
    }

    private void verifyMaterialLotCode(Long tenantId, List<WmsPurchaseReturnCodeVO> codeVOList, List<MtInstruction> lineList, String docNum){
        List<String> lineIdList = lineList.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
        List<String> scanCodeList = new ArrayList<>();

        //查询行号
        List<MtExtendAttrVO1> attrVO1List = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(lineList)){
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_instruction_attr");
            mtExtendVO1.setKeyIdList(lineIdList);
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 vo5 = new MtExtendVO5();
            vo5.setAttrName("INSTRUCTION_LINE_NUM");
            mtExtendVO5List.add(vo5);
            MtExtendVO5 attrVO5 = new MtExtendVO5();
            attrVO5.setAttrName("MATERIAL_VERSION");
            mtExtendVO5List.add(attrVO5);
            MtExtendVO5 soNumAttr = new MtExtendVO5();
            soNumAttr.setAttrName("SO_NUM");
            mtExtendVO5List.add(soNumAttr);
            MtExtendVO5 soLineNumAttr = new MtExtendVO5();
            soLineNumAttr.setAttrName("SO_LINE_NUM");
            mtExtendVO5List.add(soLineNumAttr);
            mtExtendVO1.setAttrs(mtExtendVO5List);
            attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

            //查询已扫描的条码
            scanCodeList = wmsPurchaseReturnExecuteMapper.queryScanCodeList(tenantId, lineIdList);
        }
        Map<String, List<MtExtendAttrVO1>> attrMap = attrVO1List.stream().collect(Collectors.groupingBy(dto -> dto.getKeyId() + "_" + dto.getAttrName()));
        List<String> locatorIdList = codeVOList.stream().map(WmsPurchaseReturnCodeVO::getParentLocatorId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<MtModLocator> locatorList = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIdList);
        List<LovValueDTO> codeStatusList = lovAdapter.queryLovValue("MT.MTLOT.STATUS", tenantId);
        for (WmsPurchaseReturnCodeVO code : codeVOList) {
            //校验有效性
            if(!WmsConstant.CONSTANT_Y.equals(code.getEnableFlag())){
                throw new MtException("WMS_DISTRIBUTION_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0005", "WMS", code.getMaterialLotCode()));
            }
            //校验冻结
            if(WmsConstant.CONSTANT_Y.equals(code.getFreezeFlag())){
                throw new MtException("WMS_COST_CENTER_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0025", "WMS", code.getMaterialLotCode()));
            }
            //校验条码是否盘点停用
            if(WmsConstant.CONSTANT_Y.equals(code.getStocktakeFlag())){
                throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0034", "WMS", code.getMaterialLotCode()));
            }
            //校验在制品
            if(WmsConstant.CONSTANT_Y.equals(code.getMfFlag())){
                throw new MtException("WMS_DISTRIBUTION_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0003", "WMS", code.getMaterialLotCode()));
            }
            //校验条码是否已扫描
            if(scanCodeList.contains(code.getMaterialLotId())){
                throw new MtException("WMS_DISTRIBUTION_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0006", "WMS", code.getMaterialLotCode()));
            }

            //物料加版本找到条码对应的行
            List<WmsPurchaseLineVO> lineVO = wmsPurchaseReturnExecuteMapper.queryLineByBarcodeAndDocNum(tenantId, code.getMaterialLotId(), docNum);
            if(CollectionUtils.isEmpty(lineVO) || lineVO.size() > 1){
                throw new MtException("WMS_DISTRIBUTION_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_009", "WMS", code.getMaterialLotCode()));
            }
            //校验条码物料与单据行物料是否一致
            if(!StringUtils.equals(lineVO.get(0).getMaterialId(), code.getMaterialId())){
                Optional<MtExtendAttrVO1> lineNumFirst = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), lineVO.get(0).getInstructionId()) && StringUtils.equals(dto.getAttrName(), "INSTRUCTION_LINE_NUM")).findFirst();
                throw new MtException("WMS_DISTRIBUTION_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0007", "WMS", code.getMaterialLotCode(), lineNumFirst.isPresent() ? lineNumFirst.get().getAttrValue() : ""));
            }
            //校验条码货位
            if(!StringUtils.equals(lineVO.get(0).getToLocatorId(), code.getParentLocatorId())){
                throw new MtException("RK_INVENTORY_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "RK_INVENTORY_0052", "INVENTORY", code.getMaterialLotCode()));
            }
            //校验供应商
            if(!StringUtils.equals(lineVO.get(0).getSupplierId(), code.getSupplierId())){
                throw new MtException("RK_INVENTORY_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "RK_INVENTORY_0055", "INVENTORY"));
            }
            //校验物料版本
            Optional<WmsPurchaseLineVO> mvFirst = lineVO.stream().filter(dto -> {
                List<MtExtendAttrVO1> extendAttrVO1List = attrMap.get(dto.getInstructionId() + "_" + "MATERIAL_VERSION");
                if(CollectionUtils.isNotEmpty(extendAttrVO1List)){
                    String lineMv = extendAttrVO1List.get(0).getAttrValue();
                    String codeMv = StringUtils.isBlank(code.getMaterialVersion()) ? "" : code.getMaterialVersion();
                    if(!StringUtils.equals(lineMv, codeMv)){
                        return true;
                    }
                }
                return false;
            }).findFirst();
            if(mvFirst.isPresent()){
                Optional<MtExtendAttrVO1> lineNumFirst = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), mvFirst.get().getInstructionId()) && StringUtils.equals(dto.getAttrName(), "INSTRUCTION_LINE_NUM")).findFirst();
                throw new MtException("WMS_DISTRIBUTION_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0008", "WMS", code.getMaterialLotCode(), lineNumFirst.isPresent() ? lineNumFirst.get().getAttrValue() : ""));
            }
            //校验条码单位与单据行单位是否一致
            if(!StringUtils.equals(lineVO.get(0).getUomId(), code.getPrimaryUomId())){
                throw new MtException("WMS_DISTRIBUTION_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0004", "WMS", code.getMaterialLotCode()));
            }
            //校验条码状态是否为已入库 INSTOCK
            if(!WmsConstant.MaterialLotStatus.INSTOCK.equals(code.getCodeStatus())){
                Optional<LovValueDTO> first = codeStatusList.stream().filter(dto -> StringUtils.equals(dto.getValue(), code.getCodeStatus())).findFirst();
                throw new MtException("WMS_DISTRIBUTION_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_DISTRIBUTION_0010", "WMS", code.getMaterialLotCode(), first.isPresent() ? first.get().getMeaning() : ""));
            }
            //校验线边仓
            Optional<MtModLocator> parentLocatorfirst = locatorList.stream().filter(dto -> StringUtils.equals(dto.getLocatorId(), code.getParentLocatorId())).findFirst();
            if(parentLocatorfirst.isPresent()){
                if(StringUtils.equals(parentLocatorfirst.get().getLocatorType(), "14") && StringUtils.equals(parentLocatorfirst.get().getLocatorCategory(), "AREA")){
                    throw new MtException("WMS_DISTRIBUTION_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_0012", "WMS", code.getMaterialLotCode()));
                }
            }
            //校验数量
            if(code.getPrimaryUomQty() == null || code.getPrimaryUomQty().compareTo(0D) <= 0){
                throw new MtException("WMS_PUT_IN_STOCK_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PUT_IN_STOCK_013", "WMS", code.getMaterialLotCode()));
            }
        }
    }
}
