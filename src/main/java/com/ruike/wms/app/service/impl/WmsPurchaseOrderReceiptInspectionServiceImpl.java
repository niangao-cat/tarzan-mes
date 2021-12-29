package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO;
import com.ruike.wms.api.dto.WmsPurchaseOrderReceiptInspectionDTO;
import com.ruike.wms.app.service.WmsPurchaseOrderReceiptInspectionService;
import com.ruike.wms.domain.vo.WmsPurchaseOrderReceiptInspectionVO;
import com.ruike.wms.infra.mapper.WmsPurchaseOrderReceiptInspectionMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.infra.mapper.MtInstructionMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author li.zhang 2021/09/09 13:43
 */
@Service
public class WmsPurchaseOrderReceiptInspectionServiceImpl implements WmsPurchaseOrderReceiptInspectionService {

    @Autowired
    private WmsPurchaseOrderReceiptInspectionMapper wmsPurchaseOrderReceiptInspectionMapper;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Override
    @ProcessLovValue
    public Page<WmsPurchaseOrderReceiptInspectionVO> queryList(Long tenantId, WmsPurchaseOrderReceiptInspectionDTO dto, PageRequest pageRequest) {
        Page<WmsPurchaseOrderReceiptInspectionVO> page =
                PageHelper.doPage(pageRequest, () -> wmsPurchaseOrderReceiptInspectionMapper.queryList(tenantId, dto));
        if(CollectionUtils.isNotEmpty(page)){
            //查询送货单状态
            List<String> deliveryInstructionDocIdList = page.stream().map(WmsPurchaseOrderReceiptInspectionVO::getDeliveryInstructionDocId).collect(Collectors.toList());
            List<MtInstruction> mtInstructions = mtInstructionMapper.selectBySourceDocId(tenantId,deliveryInstructionDocIdList);
            List<String> instructionIds = mtInstructions.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_instruction_attr");
            mtExtendVO1.setKeyIdList(instructionIds);
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("INSTRUCTION_LINE_NUM");
            attrs.add(mtExtendVO5);
            mtExtendVO1.setAttrs(attrs);
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);

            for(WmsPurchaseOrderReceiptInspectionVO wmsPurchaseOrderReceiptInspectionVO:page){
                if(StringUtils.isBlank(wmsPurchaseOrderReceiptInspectionVO.getActualReceivedDate())){
                    wmsPurchaseOrderReceiptInspectionVO.setReceivedFlag(null);
                }
                if(wmsPurchaseOrderReceiptInspectionVO.getReceivedQty().compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal qty = wmsPurchaseOrderReceiptInspectionVO.getPoStockInQty().multiply(new BigDecimal(100)).divide(wmsPurchaseOrderReceiptInspectionVO.getReceivedQty(), 2, RoundingMode.HALF_UP);
                    String qualificationRate = String.valueOf(qty) + "%";
                    wmsPurchaseOrderReceiptInspectionVO.setQualificationRate(qualificationRate);
                }
                //查询送货单行状态
                List<MtInstruction> mtInstructionList = mtInstructions.stream().filter(item ->item.getSourceDocId().equals(wmsPurchaseOrderReceiptInspectionVO.getDeliveryInstructionDocId())).collect(Collectors.toList());
                List<String> instructionIdList = mtInstructionList.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
                List<MtExtendAttrVO1> mtExtendAttrVO1List = mtExtendAttrVO1s.stream().filter(item ->instructionIdList.contains(item.getKeyId())).collect(Collectors.toList());
                List<MtExtendAttrVO1> mtExtendAttrVO1List1 = mtExtendAttrVO1List.stream().filter(item ->wmsPurchaseOrderReceiptInspectionVO.getDeliveryInstructionLineNum().equals(item.getAttrValue())).collect(Collectors.toList());
                List<String> instructionIdList1 =  mtExtendAttrVO1List1.stream().map(MtExtendAttrVO1::getKeyId).collect(Collectors.toList());
                List<MtInstruction> mtInstructionList1 = mtInstructionList.stream().filter(item ->instructionIdList1.contains(item.getInstructionId())).collect(Collectors.toList());
                if(mtInstructionList1.size() == 2){
                    if("COMPLETED".equals(mtInstructionList.get(1).getInstructionStatus())){
                        wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionLineStatus(mtInstructionList1.get(1).getInstructionStatus());
                        String deliveryInstructionDocStatusMeaning = lovAdapter.queryLovMeaning("WMS.DELIVERY_DOC_LINE_TOL.STATUS", tenantId, mtInstructionList.get(1).getInstructionStatus());
                        wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionLineStatusMeaning(deliveryInstructionDocStatusMeaning);
                    }else{
                        wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionLineStatus(mtInstructionList1.get(0).getInstructionStatus());
                        String deliveryInstructionDocStatusMeaning = lovAdapter.queryLovMeaning("WMS.DELIVERY_DOC_LINE_RFS.STATUS", tenantId, mtInstructionList.get(0).getInstructionStatus());
                        wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionLineStatusMeaning(deliveryInstructionDocStatusMeaning);
                    }
                }
            }
        }
        return page;
    }

    @Override
    @ProcessLovValue
    public List<WmsPurchaseOrderReceiptInspectionVO> export(Long tenantId, WmsPurchaseOrderReceiptInspectionDTO dto, ExportParam exportParam) {
        List<WmsPurchaseOrderReceiptInspectionVO> wmsPurchaseOrderReceiptInspectionVOS = wmsPurchaseOrderReceiptInspectionMapper.queryList(tenantId, dto);
        //查询送货单状态
        List<String> deliveryInstructionDocIdList = wmsPurchaseOrderReceiptInspectionVOS.stream().map(WmsPurchaseOrderReceiptInspectionVO::getDeliveryInstructionDocId).collect(Collectors.toList());
        List<MtInstruction> mtInstructions = mtInstructionMapper.selectBySourceDocId(tenantId,deliveryInstructionDocIdList);
        List<String> instructionIds = mtInstructions.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_instruction_attr");
        mtExtendVO1.setKeyIdList(instructionIds);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("INSTRUCTION_LINE_NUM");
        attrs.add(mtExtendVO5);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);

        for(WmsPurchaseOrderReceiptInspectionVO wmsPurchaseOrderReceiptInspectionVO:wmsPurchaseOrderReceiptInspectionVOS){
            if(StringUtils.isBlank(wmsPurchaseOrderReceiptInspectionVO.getActualReceivedDate())){
                wmsPurchaseOrderReceiptInspectionVO.setReceivedFlag(null);
            }
            if(wmsPurchaseOrderReceiptInspectionVO.getReceivedQty().compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal qty = wmsPurchaseOrderReceiptInspectionVO.getPoStockInQty().multiply(new BigDecimal(100)).divide(wmsPurchaseOrderReceiptInspectionVO.getReceivedQty(), 2, RoundingMode.HALF_UP);
                String qualificationRate = String.valueOf(qty) + "%";
                wmsPurchaseOrderReceiptInspectionVO.setQualificationRate(qualificationRate);
            }
            //查询送货单行状态
            List<MtInstruction> mtInstructionList = mtInstructions.stream().filter(item ->item.getSourceDocId().equals(wmsPurchaseOrderReceiptInspectionVO.getDeliveryInstructionDocId())).collect(Collectors.toList());
            List<String> instructionIdList = mtInstructionList.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
            List<MtExtendAttrVO1> mtExtendAttrVO1List = mtExtendAttrVO1s.stream().filter(item ->instructionIdList.contains(item.getKeyId())).collect(Collectors.toList());
            List<MtExtendAttrVO1> mtExtendAttrVO1List1 = mtExtendAttrVO1List.stream().filter(item ->wmsPurchaseOrderReceiptInspectionVO.getDeliveryInstructionLineNum().equals(item.getAttrValue())).collect(Collectors.toList());
            List<String> instructionIdList1 =  mtExtendAttrVO1List1.stream().map(MtExtendAttrVO1::getKeyId).collect(Collectors.toList());
            List<MtInstruction> mtInstructionList1 = mtInstructionList.stream().filter(item ->instructionIdList1.contains(item.getInstructionId())).collect(Collectors.toList());
            if(mtInstructionList1.size() == 2){
                if("COMPLETED".equals(mtInstructionList.get(1).getInstructionStatus())){
                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionLineStatus(mtInstructionList1.get(1).getInstructionStatus());
                    String deliveryInstructionDocStatusMeaning = lovAdapter.queryLovMeaning("WMS.DELIVERY_DOC_LINE_TOL.STATUS", tenantId, mtInstructionList.get(1).getInstructionStatus());
                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionLineStatusMeaning(deliveryInstructionDocStatusMeaning);
                }else{
                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionDocStatus(mtInstructionList1.get(0).getInstructionStatus());
                    String deliveryInstructionDocStatusMeaning = lovAdapter.queryLovMeaning("WMS.DELIVERY_DOC_LINE_RFS.STATUS", tenantId, mtInstructionList.get(0).getInstructionStatus());
                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionLineStatusMeaning(deliveryInstructionDocStatusMeaning);
                }
            }
//            MtInstruction mtInstruction = new MtInstruction();
//            mtInstruction.setTenantId(tenantId);
//            mtInstruction.setSourceDocId(wmsPurchaseOrderReceiptInspectionVO.getDeliveryInstructionDocId());
//            List<MtInstruction> mtInstructions =  mtInstructionRepository.select(mtInstruction);
//            List<String> instructionIds = mtInstructions.stream().map(MtInstruction::getInstructionId).collect(Collectors.toList());
//            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
//            mtExtendVO1.setTableName("mt_instruction_attr");
//            mtExtendVO1.setKeyIdList(instructionIds);
//            List<MtExtendVO5> attrs = new ArrayList<>();
//            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//            mtExtendVO5.setAttrName("INSTRUCTION_LINE_NUM");
//            attrs.add(mtExtendVO5);
//            mtExtendVO1.setAttrs(attrs);
//            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);
//            List<MtExtendAttrVO1> mtExtendAttrVO1List = mtExtendAttrVO1s.stream().filter(item ->wmsPurchaseOrderReceiptInspectionVO.getDeliveryInstructionLineNum().equals(item.getAttrValue())).collect(Collectors.toList());
//            List<String> instructionIdList = mtExtendAttrVO1List.stream().map(MtExtendAttrVO1::getKeyId).collect(Collectors.toList());
//            List<MtInstruction> mtInstructionList = mtInstructions.stream().filter(item ->instructionIdList.contains(item.getInstructionId())).collect(Collectors.toList());
//            if(mtInstructionList.size() == 2){
//                if("COMPLETED".equals(mtInstructionList.get(1).getInstructionStatus())){
//                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionDocStatus(mtInstructionList.get(1).getInstructionStatus());
//                    String deliveryInstructionDocStatusMeaning = lovAdapter.queryLovMeaning("WMS.DELIVERY_DOC_LINE_TOL.STATUS", tenantId, mtInstructionList.get(1).getInstructionStatus());
//                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionDocStatusMeaning(deliveryInstructionDocStatusMeaning);
//                }else{
//                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionDocStatus(mtInstructionList.get(0).getInstructionStatus());
//                    String deliveryInstructionDocStatusMeaning = lovAdapter.queryLovMeaning("WMS.DELIVERY_DOC_LINE_RFS.STATUS", tenantId, mtInstructionList.get(0).getInstructionStatus());
//                    wmsPurchaseOrderReceiptInspectionVO.setDeliveryInstructionDocStatusMeaning(deliveryInstructionDocStatusMeaning);
//                }
//            }
        }
        return wmsPurchaseOrderReceiptInspectionVOS;
    }
}
