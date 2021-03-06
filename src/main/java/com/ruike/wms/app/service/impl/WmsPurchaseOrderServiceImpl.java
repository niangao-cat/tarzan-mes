package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsPurchaseOrderDTO;
import com.ruike.wms.app.service.WmsPurchaseOrderService;
import com.ruike.wms.domain.vo.WmsPurchaseOrderVO;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import com.ruike.wms.infra.mapper.WmsPurchaseOrderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInstructionReturnVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: tarzan-mes
 * @description: ???????????????????????????
 * @author: han.zhang
 * @create: 2020/03/19 11:04
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class WmsPurchaseOrderServiceImpl implements WmsPurchaseOrderService {
    @Autowired
    MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private WmsPurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    MtInstructionRepository mtInstructionRepository;
    @Autowired
    MtMaterialMapper mtMaterialMapper;
    @Autowired
    MtMaterialRepository mtMaterialRepository;
    @Autowired
    MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtUomRepository mtUomRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;
    /**
     * @Description ???????????????????????????
     * @param condition ????????????
     * @param pageRequest ??????
     * @return java.util.List<tarzan.inventory.domain.vo.PurchaseOrderVO>
     * @Date 2020-03-19 11:09
     * @Author han.zhang
     */
    @Override
    public List<WmsPurchaseOrderVO> selectHeadData(WmsPurchaseOrderDTO condition, PageRequest pageRequest, Long tenantId) {
        List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("WMS.PO.TYPE",tenantId);
        List<String> value = poTypeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        Page<WmsPurchaseOrderVO> base =
            PageHelper.doPage(pageRequest,() -> purchaseOrderMapper.selectPoDataByCondition(condition , value));
        for (WmsPurchaseOrderVO wmsPurchaseOrderVO:base) {
            //????????????????????????????????????????????????
            String meaning = lovAdapter.queryLovMeaning("WMS.INSTRUCTION_DOC_TYPE", tenantId, wmsPurchaseOrderVO.getInstructionDocType());
            wmsPurchaseOrderVO.setInstructionDocTypeMeaning(meaning);
        }


        return base;
    }

    /**
     * @Description ???????????????????????????
     * @param sourceInstructionId ???id
     * @param pageRequest ??????
     * @return java.lang.Object
     * @Date 2020-03-19 16:03
     * @Author han.zhang
     */
    @Override
    @ProcessLovValue
    public List<MtInstructionReturnVO> selectLineData(String sourceInstructionId, PageRequest pageRequest) {
        //???????????????ID???????????????ID??????
        Page<MtInstruction> mtInstructions = PageHelper.doPage(pageRequest, () -> purchaseOrderMapper.queryMtInstructionList(sourceInstructionId));

        List<MtInstructionReturnVO> mtInstructionReturnVOS = new ArrayList<>(mtInstructions.getContent().size());

        //????????????
        List<String> typeList = new ArrayList<>();
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.DELIVERY_TICKET_QUERY", tenantId);
        List<String> valueList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        if(org.apache.commons.collections4.CollectionUtils.isEmpty(valueList)){
            typeList.add("DELIVERY_DOC");
        }else {
            typeList.addAll(valueList);
        }
        //????????????
        mtInstructions.getContent().stream().forEach(mtInstructions1 -> {
            //???????????????vo???
            MtInstructionReturnVO vo = new MtInstructionReturnVO();
            BeanUtils.copyProperties(mtInstructions1,vo);
            //????????????id??????????????????
            MtMaterial mtMaterial = new MtMaterial();
            mtMaterial.setMaterialId(mtInstructions1.getMaterialId());
            mtMaterial = mtMaterialMapper.selectByPrimaryKey(mtMaterial);
            //??????api????????????
            MtMaterialVO1 mtMaterialVO = this.mtMaterialRepository.materialUomGet(tenantId,
                    mtInstructions1.getMaterialId());
            if(!Objects.isNull(mtMaterialVO)){
                vo.setMaterialCode(mtMaterial.getMaterialCode());
                vo.setMaterialName(mtMaterial.getMaterialName());
            }

            MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtInstructions1.getUomId());
            if(mtUom != null){
                vo.setPrimaryUomCode(mtUom.getUomCode());
            }

            //????????????
            List<MtModLocator> mtModLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, Collections.singletonList(mtInstructions1.getToLocatorId()));
            if (CollectionUtils.isNotEmpty(mtModLocators)) {
                vo.setLocatorName(mtModLocators.get(0).getLocatorName());
            }
            //????????????????????????????????????
            //??????????????????
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mtInstructions1.getInstructionId()), "INSTRUCTION_LINE_NUM", "MATERIAL_VERSION", "SO_NUM", "SO_LINE_NUM", "PO_TYPE", "SAMPLE_FLAG"));
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s) {
//                //???????????????
//                if("RECEIVED_QTY".equals(extendAttr.getAttrName())){
//                    vo.setReceivedQty(extendAttr.getAttrValue());
//                }
                //????????????????????????????????????????????????
//                if("QUANTITY_ORDERED".equals(extendAttr.getAttrName())){
//                    vo.setQuantityOrdered(extendAttr.getAttrValue());
//                }
                //????????????
                if("INSTRUCTION_LINE_NUM".equals(extendAttr.getAttrName())){
                    vo.setInstructionLineNum(extendAttr.getAttrValue());
                }
                //??????????????????
                if("MATERIAL_VERSION".equals(extendAttr.getAttrName())){
                    vo.setMaterialVersion(extendAttr.getAttrValue());
                }
                //??????????????????
                if("SO_NUM".equals(extendAttr.getAttrName())){
                    vo.setSoNum(extendAttr.getAttrValue());
                }
                //?????????????????????
                if("SO_LINE_NUM".equals(extendAttr.getAttrName())){
                    vo.setSoLineNum(extendAttr.getAttrValue());
                }
                //???????????????
                if("PO_TYPE".equals(extendAttr.getAttrName())){
                    vo.setPoLineType(extendAttr.getAttrValue());
                }
                //????????????
                if("SAMPLE_FLAG".equals(extendAttr.getAttrName())){
                    vo.setSampleFlag(extendAttr.getAttrValue());
                }
            }

            /**
             * ?????????????????????????????????????????????????????? by sanfeng.zhang 2020-09-07
             */
            //???????????????
            Double executeQty = wmsPoDeliveryRelMapper.selectPoQuantityOfReceiveComplete("", mtInstructions1.getInstructionId(), typeList);
            //??????????????????
            Double completeQty = wmsPoDeliveryRelMapper.selectPoQuantityOfComplete("", mtInstructions1.getInstructionId());
            //NG??????
            Double ngQty = wmsPoDeliveryRelMapper.selectNgQty(tenantId, mtInstructions1.getInstructionId());
            //????????????
            Double stockQty = wmsPoDeliveryRelMapper.selectPoQuantityOfStockInComplete("", mtInstructions1.getInstructionId());

            vo.setReceivedQty(BigDecimal.ZERO.add(BigDecimal.valueOf(completeQty)).add(BigDecimal.valueOf(stockQty)).subtract(BigDecimal.valueOf(ngQty)).setScale(2).toString());
            vo.setQuantityOrdered(String.valueOf(executeQty));

            //????????????????????? = ?????????????????? ??? ??????????????? - ??????????????? - ??????????????????
            vo.setAvailableOrderQuantity(BigDecimal.valueOf(vo.getQuantity()).subtract(BigDecimal.valueOf(executeQty)).subtract(BigDecimal.valueOf(completeQty)).subtract(BigDecimal.valueOf(stockQty)).add(BigDecimal.valueOf(ngQty)).setScale(2));
            mtInstructionReturnVOS.add(vo);
        });

        Page<MtInstructionReturnVO> result = new Page<MtInstructionReturnVO>();
        result.setTotalPages(mtInstructions.getTotalPages());
        result.setTotalElements(mtInstructions.getTotalElements());
        result.setNumberOfElements(mtInstructions.getNumberOfElements());
        result.setSize(mtInstructions.getSize());
        result.setNumber(mtInstructions.getNumber());
        result.setContent(mtInstructionReturnVOS);

        return result;
    }

}