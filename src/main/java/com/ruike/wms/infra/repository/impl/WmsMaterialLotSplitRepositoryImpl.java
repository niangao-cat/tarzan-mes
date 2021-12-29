package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsMaterialLotSplitDTO;
import com.ruike.wms.domain.repository.WmsMaterialLotSplitRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO3;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO3;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * WmsMaterialLotSplitRepositoryImpl
 *
 * @author: chaonan.hu@hand-china.com 2020-09-07 15:25:13
 **/
@Component
public class WmsMaterialLotSplitRepositoryImpl implements WmsMaterialLotSplitRepository {

    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @ProcessLovValue
    public WmsMaterialLotSplitVO3 scanSourceBarcode(Long tenantId, MtMaterialLot mtMaterialLot) {
        WmsMaterialLotSplitVO3 result = new WmsMaterialLotSplitVO3();
        //物料批
        result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        result.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        //物料
        result.setMaterialId(mtMaterialLot.getMaterialId());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
        result.setMaterialCode(mtMaterial.getMaterialCode());
        result.setMaterialName(mtMaterial.getMaterialName());
        //物料版本
        List<MtExtendAttrVO> materialVersion = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
            setAttrName("MATERIAL_VERSION");
        }});
        if(CollectionUtils.isNotEmpty(materialVersion) && StringUtils.isNotEmpty(materialVersion.get(0).getAttrValue())){
            result.setMaterialVersion(materialVersion.get(0).getAttrValue());
        }
        //主单位
        result.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
        result.setPrimaryUomCode(mtUom.getUomCode());
        result.setPrimaryUomQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
        //质量状态
        result.setQualityStatus(mtMaterialLot.getQualityStatus());
        //状态
        List<MtExtendAttrVO> statusList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
            setAttrName("STATUS");
        }});
        if(CollectionUtils.isNotEmpty(statusList) && StringUtils.isNotEmpty(statusList.get(0).getAttrValue())){
            result.setStatus(statusList.get(0).getAttrValue());
        }
        //批次
        result.setLot(mtMaterialLot.getLot());
        //供应商
        if(StringUtils.isNotEmpty(mtMaterialLot.getSupplierId())){
            MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(mtMaterialLot.getSupplierId());
            result.setSupplierId(mtSupplier.getSupplierId());
            result.setSupplierName(mtSupplier.getSupplierName());
        }
        //供应商批次
        List<MtExtendAttrVO> supplierLotList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
            setAttrName("SUPPLIER_LOT");
        }});
        if(CollectionUtils.isNotEmpty(supplierLotList) && StringUtils.isNotEmpty(supplierLotList.get(0).getAttrValue())){
            result.setSupplierLot(supplierLotList.get(0).getAttrValue());
        }
        return result;
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public List<WmsMaterialLotSplitVO3> splitBarcode(Long tenantId, WmsMaterialLotSplitDTO dto) {
        //事件请求ID
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_LOT_SPLIT");
        //事件ID
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_SPLIT");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //获取前台传入的扫描条码数量
        int size = 0;
        if(CollectionUtils.isNotEmpty(dto.getMaterialLotCodeList())){
            size = dto.getMaterialLotCodeList().size();
        }
        //调用{ materialLotSplit }进行物料批拆分
        List<String> targetMaterialLotIdList = new ArrayList<>();
        for (int i = 0; i < dto.getTotal(); i++) {
            MtMaterialVO3 mtMaterialVO3 = new MtMaterialVO3();
            mtMaterialVO3.setSourceMaterialLotId(dto.getSourceMaterialLotId());
            mtMaterialVO3.setSplitPrimaryQty(dto.getQty().doubleValue());
            mtMaterialVO3.setEventRequestId(eventRequestId);
            if(i < size){
                mtMaterialVO3.setSplitMaterialLotCode(dto.getMaterialLotCodeList().get(i));
            }
            String targetMaterialLotId = mtMaterialLotRepository.materialLotSplit(tenantId, mtMaterialVO3);
            targetMaterialLotIdList.add(targetMaterialLotId);
        }
        ////2020-10-10 11:27 add by chaonan.hu for yiwei.zhou 当拆分完原始条码的数量为0时，将条码失效
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getSourceMaterialLotId());
        if(mtMaterialLot.getPrimaryUomQty() == 0){
            MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
            mtMaterialLotVo2.setEventId(eventId);
            mtMaterialLotVo2.setMaterialLotId(dto.getSourceMaterialLotId());
            mtMaterialLotVo2.setEnableFlag(HmeConstants.ConstantValue.NO);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2, HmeConstants.ConstantValue.NO);
        }
        //调用{ materialLotLimitAttrQuery}获取原条码扩展属性，复制到拆分条码扩展属性上
        MtMaterialLot sourceMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getSourceMaterialLotId());
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(sourceMaterialLot.getMaterialLotId());
        }});
        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
        //原条码上是否有ORIGINAL_ID这个扩展属性标识
        boolean flag = false;
        for (MtExtendAttrVO mtExtendAttrVO:mtExtendAttrVOS) {
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(mtExtendAttrVO.getAttrName());
            if("ORIGINAL_ID".equals(mtExtendAttrVO.getAttrName())){
                mtExtendVO5.setAttrValue(sourceMaterialLot.getMaterialLotId());
                flag = true;
            }else{
                mtExtendVO5.setAttrValue(mtExtendAttrVO.getAttrValue());
            }
            mtExtendVO5s.add(mtExtendVO5);
        }
        if(!flag){
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("ORIGINAL_ID");
            mtExtendVO5.setAttrValue(sourceMaterialLot.getMaterialLotId());
            mtExtendVO5s.add(mtExtendVO5);
        }
        for (String targetMaterialLotId:targetMaterialLotIdList) {
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", targetMaterialLotId, eventId, mtExtendVO5s);
        }
        //封装返回结果
        List<WmsMaterialLotSplitVO3> resultList = new ArrayList<>();
        //物料
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(sourceMaterialLot.getMaterialId());
        //物料版本
        String materialVersion = null;
        List<MtExtendAttrVO> materialVersionList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            setAttrName("MATERIAL_VERSION");
        }});
        if(CollectionUtils.isNotEmpty(materialVersionList) && StringUtils.isNotEmpty(materialVersionList.get(0).getAttrValue())){
            materialVersion = materialVersionList.get(0).getAttrValue();
        }
        //主单位
        MtUom mtUom = mtUomRepository.selectByPrimaryKey(sourceMaterialLot.getPrimaryUomId());
        //状态
        String status = null;
        List<MtExtendAttrVO> statusList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            setAttrName("STATUS");
        }});
        if(CollectionUtils.isNotEmpty(statusList) && StringUtils.isNotEmpty(statusList.get(0).getAttrValue())){
            status = statusList.get(0).getAttrValue();
        }
        for (String targetMaterialLotId:targetMaterialLotIdList) {
            MtMaterialLot targetMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(targetMaterialLotId);
            WmsMaterialLotSplitVO3 result = new WmsMaterialLotSplitVO3();
            result.setMaterialLotId(targetMaterialLot.getMaterialLotId());
            result.setMaterialLotCode(targetMaterialLot.getMaterialLotCode());
            result.setMaterialId(mtMaterial.getMaterialId());
            result.setMaterialCode(mtMaterial.getMaterialCode());
            result.setMaterialName(mtMaterial.getMaterialName());
            result.setMaterialVersion(materialVersion);
            result.setPrimaryUomId(targetMaterialLot.getPrimaryUomId());
            result.setPrimaryUomCode(mtUom.getUomCode());
            result.setPrimaryUomQty(new BigDecimal(targetMaterialLot.getPrimaryUomQty()));
            result.setQualityStatus(targetMaterialLot.getQualityStatus());
            if(StringUtils.isNotEmpty(status)){
                result.setStatus(status);
            }
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public void materialLotMfFlagVerify(Long tenantId, MtMaterialLot materialLot){
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_material_lot_attr");
        extendVO.setKeyId(materialLot.getMaterialLotId());
        extendVO.setAttrName("MF_FLAG");
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
        if(CollectionUtils.isNotEmpty(attrVOList) && WmsConstant.CONSTANT_Y.equals(attrVOList.get(0).getAttrValue())){
            throw new MtException("WMS_DISTRIBUTION_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0003", "WMS", materialLot.getMaterialLotCode()));
        }
    }

    @Override
    public void materialLotVfVerify(Long tenantId, MtMaterialLot materialLot) {
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("VIRTUAL_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())){
            throw new MtException("WMS_INV_TRANSFER_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0051", "WMS", materialLot.getMaterialLotCode()));
        }
    }
}