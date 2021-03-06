package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO2;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO3;
import com.ruike.hme.domain.repository.HmeTimeMaterialSplitRepository;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO3;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO3;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.math.BigDecimal;
import java.util.*;

/**
 * ?????????????????????????????????
 *
 * @author chaonan.hu@hand-china.com 2020-09-12 11:17:08
 */
@Component
public class HmeTimeMaterialSplitRepositoryImpl implements HmeTimeMaterialSplitRepository {

    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Override
    public HmeTimeMaterialSplitVO scanBarcode(Long tenantId, MtMaterialLot mtMaterialLot,
                                              String dateTimeFromStr, String dateTimeToStr) {
        HmeTimeMaterialSplitVO result = new HmeTimeMaterialSplitVO();
        result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        result.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        result.setMaterialId(mtMaterialLot.getMaterialId());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
        result.setMaterialCode(mtMaterial.getMaterialCode());
        result.setMaterialName(mtMaterial.getMaterialName());
        result.setPrimaryUomQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
        if(StringUtils.isNotEmpty(mtMaterialLot.getPrimaryUomId())){
            result.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
            result.setPrimaryUomCode(mtUom.getUomCode());
        }
        result.setLot(mtMaterialLot.getLot());
        if(StringUtils.isNotEmpty(mtMaterialLot.getSupplierId())){
            result.setSupplierId(mtMaterialLot.getSupplierId());
            MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(mtMaterialLot.getSupplierId());
            result.setSupplierName(mtSupplier.getSupplierName());
        }
        // 20210330 add by sanfeng.zhang for kang.wang ??????????????????????????? ?????????????????????
        List<MtMaterialSite> materialSiteList = mtMaterialSiteRepository.select(new MtMaterialSite() {{
            setTenantId(tenantId);
            setMaterialId(mtMaterialLot.getMaterialId());
            setSiteId(mtMaterialLot.getSiteId());
            setEnableFlag(HmeConstants.ConstantValue.YES);
        }});
        if (CollectionUtils.isNotEmpty(materialSiteList)) {
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 attrOne = new MtExtendVO5();
            attrOne.setAttrName("attribute9");
            attrs.add(attrOne);
            MtExtendVO5 attrTwo = new MtExtendVO5();
            attrTwo.setAttrName("attribute10");
            attrs.add(attrTwo);
            List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1() {{
                setKeyIdList(Collections.singletonList(materialSiteList.get(0).getMaterialSiteId()));
                setTableName("mt_material_site_attr");
                setAttrs(attrs);
            }});
            for (MtExtendAttrVO1 mtExtendAttrVO1 : extendAttrVO1List) {
                if ("attribute9".equals(mtExtendAttrVO1.getAttrName())) {
                    result.setOpenEffectiveTime(mtExtendAttrVO1.getAttrValue());
                }
                if ("attribute10".equals(mtExtendAttrVO1.getAttrName())) {
                    result.setOpenEffectiveUom(mtExtendAttrVO1.getAttrValue());
                }
            }
        }
        // 20210330 add by sanfeng.zhang for kang.wang ????????????????????????????????? ??????????????????????????????????????????????????????????????????????????? ????????????+?????????????????????
        Date nowDate = CommonUtils.currentTimeGet();
        if(StringUtils.isNotEmpty(dateTimeFromStr)){
            result.setDateTimeFrom(dateTimeFromStr);
        } else {
            result.setDateTimeFrom(DateUtil.date2String(nowDate, "yyyy-MM-dd HH:mm:ss"));
        }
        if(StringUtils.isNotEmpty(dateTimeToStr)){
            result.setDateTimeTo(dateTimeToStr);
        } else {
            // ????????????
            Date dateTimeFrom = DateUtil.string2Date(result.getDateTimeFrom(), "yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(result.getOpenEffectiveTime()) && StringUtils.isNotBlank(result.getOpenEffectiveUom())) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateTimeFrom);
                Integer openEffectiveTime = Integer.valueOf(result.getOpenEffectiveTime());
                //2020-09-18 edit by chaonan.hu for yiwei.zhou ???????????????????????????????????????
                if("MIN".equalsIgnoreCase(result.getOpenEffectiveUom())){
                    calendar.add(Calendar.MINUTE, openEffectiveTime);
                }else if("H".equalsIgnoreCase(result.getOpenEffectiveUom())){
                    calendar.add(Calendar.HOUR, openEffectiveTime);
                }else if("D".equalsIgnoreCase(result.getOpenEffectiveUom())){
                    calendar.add(Calendar.DATE, openEffectiveTime);
                }
                Date dateTo = calendar.getTime();
                result.setDateTimeTo(DateUtil.date2String(dateTo, "yyyy-MM-dd HH:mm:ss"));
            }
        }
        //2020/10/10 add by sanfeng.zhang for wangcan ???????????????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName(WmsConstant.MaterialLotAttr.SUPPLIER_LOT);
        List<MtExtendAttrVO> attrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(attrVOList)) {
            result.setSupplierLot(attrVOList.get(0).getAttrValue());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeTimeMaterialSplitVO timeSubmit(Long tenantId, HmeTimeMaterialSplitDTO dto) {
        //??????????????????????????????????????????+????????????????????????????????????
        Date dateFrom = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom);
        //2020-09-18 edit by chaonan.hu for yiwei.zhou ???????????????????????????????????????
        if("MIN".equals(dto.getTimeUom())){
            calendar.add(Calendar.MINUTE, dto.getMinute());
        }else if("H".equals(dto.getTimeUom())){
            calendar.add(Calendar.HOUR, dto.getMinute());
        }else if("D".equals(dto.getTimeUom())){
            calendar.add(Calendar.DATE, dto.getMinute());
        }
        Date dateTo = calendar.getTime();
        String dateFromStr = DateUtil.date2String(dateFrom, "yyyy-MM-dd HH:mm:ss");
        String dateToStr = DateUtil.date2String(dateTo, "yyyy-MM-dd HH:mm:ss");
        //??????API{ materialLotLimitAttrUpdate }????????????????????????
        MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
        //??????ID
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("VALID_TIME_SETTING");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        mtMaterialLotAttrVO3.setEventId(eventId);
        mtMaterialLotAttrVO3.setMaterialLotId(dto.getMaterialLotId());
        List<MtExtendVO5> attrList = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("ENABLE_DATE");
        mtExtendVO5.setAttrValue(dateFromStr);
        attrList.add(mtExtendVO5);
        MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
        mtExtendVO52.setAttrName("DEADLINE_DATE");
        mtExtendVO52.setAttrValue(dateToStr);
        attrList.add(mtExtendVO52);
        mtMaterialLotAttrVO3.setAttr(attrList);
        mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
        //??????????????????
        HmeTimeMaterialSplitVO result = new HmeTimeMaterialSplitVO();
        result.setMaterialLotId(dto.getMaterialLotId());
        result.setDateTimeFrom(dateFromStr);
        result.setDateTimeTo(dateToStr);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeTimeMaterialSplitVO2 confirm(Long tenantId, HmeTimeMaterialSplitDTO2 dto) {
        //????????????ID
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_LOT_SPLIT");
        //??????ID
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_SPLIT");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //??????{ materialLotSplit }?????????????????????
        MtMaterialVO3 mtMaterialVO3 = new MtMaterialVO3();
        mtMaterialVO3.setSourceMaterialLotId(dto.getSourceMaterialLotId());
        mtMaterialVO3.setSplitPrimaryQty(dto.getQty().doubleValue());
        mtMaterialVO3.setEventRequestId(eventRequestId);
        if(StringUtils.isNotEmpty(dto.getTargetMaterialLotCode())){
            mtMaterialVO3.setSplitMaterialLotCode(dto.getTargetMaterialLotCode());
        }
        String targetMaterialLotId = mtMaterialLotRepository.materialLotSplit(tenantId, mtMaterialVO3);
        ////2020-10-10 11:27 add by chaonan.hu for yiwei.zhou ????????????????????????????????????0?????????????????????
        MtMaterialLot sourceMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getSourceMaterialLotId());
        if(sourceMaterialLot.getPrimaryUomQty() == 0){
            MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
            mtMaterialLotVo2.setEventId(eventId);
            mtMaterialLotVo2.setMaterialLotId(dto.getSourceMaterialLotId());
            mtMaterialLotVo2.setEnableFlag(HmeConstants.ConstantValue.NO);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2, HmeConstants.ConstantValue.NO);
        }
        //??????{ materialLotLimitAttrQuery}??????????????????????????????????????????????????????????????????
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(sourceMaterialLot.getMaterialLotId());
        }});
        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        //2020-09-18 edit by chaonan.hu for yiwei.zhou ???????????????????????????????????????
        if("MIN".equals(dto.getTimeUom())){
            calendar.add(Calendar.MINUTE, dto.getMinute());
        }else if("H".equals(dto.getTimeUom())){
            calendar.add(Calendar.HOUR, dto.getMinute());
        }else if("D".equals(dto.getTimeUom())){
            calendar.add(Calendar.DATE, dto.getMinute());
        }
        Date dateTo = calendar.getTime();
        //?????????????????????
        Date sourceDateTo = null;
        //?????????????????????ORIGINAL_ID????????????????????????
        boolean flag = false;
        //?????????????????????ENABLE_DATE????????????????????????
        boolean enableDateFlag = false;
        //?????????????????????DEADLINE_DATE????????????????????????
        boolean deadlineDateFlag = false;
        //?????????????????????SUPPLIER_LOT????????????????????????
        boolean supplierLotFlag = false;
        // 20210330 add by sanfeng.zhang for kang.wang ???????????????????????????????????? ??????????????????(??????????????????????????????)
        this.updateSourceCodeAttr(tenantId, dto, mtExtendAttrVOS);
        for (MtExtendAttrVO mtExtendAttrVO:mtExtendAttrVOS) {
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(mtExtendAttrVO.getAttrName());
            if("ORIGINAL_ID".equals(mtExtendAttrVO.getAttrName())){
                mtExtendVO5.setAttrValue(sourceMaterialLot.getMaterialLotId());
                flag = true;
            }else if("ENABLE_DATE".equals(mtExtendAttrVO.getAttrName())){
                mtExtendVO5.setAttrValue(DateUtil.date2String(nowDate, "yyyy-MM-dd HH:mm:ss"));
                enableDateFlag = true;
            }else if("DEADLINE_DATE".equals(mtExtendAttrVO.getAttrName())){
                //2020-09-18 edit by chaonan.hu for yiwei.zhou ????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if (StringUtils.isBlank(mtExtendAttrVO.getAttrValue())) {
                    sourceDateTo = DateUtil.string2Date(dto.getDateTimeTo(), "yyyy-MM-dd HH:mm:ss");
                } else {
                    sourceDateTo = DateUtil.string2Date(mtExtendAttrVO.getAttrValue(), "yyyy-MM-dd HH:mm:ss");
                }
                if(sourceDateTo != null && dateTo.compareTo(sourceDateTo) > 0){
                    dateTo = sourceDateTo;
                }
                mtExtendVO5.setAttrValue(DateUtil.date2String(dateTo, "yyyy-MM-dd HH:mm:ss"));
                deadlineDateFlag = true;
            }else if("SUPPLIER_LOT".equals(mtExtendAttrVO.getAttrName())){
                mtExtendVO5.setAttrValue(dto.getSupplierLot());
                supplierLotFlag = true;
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
        if(!enableDateFlag){
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("ENABLE_DATE");
            mtExtendVO5.setAttrValue(DateUtil.date2String(nowDate, "yyyy-MM-dd HH:mm:ss"));
            mtExtendVO5s.add(mtExtendVO5);
        }
        if(!deadlineDateFlag){
            // 20210331 add by sanfeng.zhang for kang.wang ?????????????????????????????????, ????????????????????????????????? ??????????????????????????????
            sourceDateTo = DateUtil.string2Date(dto.getDateTimeTo(), "yyyy-MM-dd HH:mm:ss");
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("DEADLINE_DATE");
            if (sourceDateTo != null && dateTo.compareTo(sourceDateTo) > 0) {
                dateTo = sourceDateTo;
            }
            mtExtendVO5.setAttrValue(DateUtil.date2String(dateTo, "yyyy-MM-dd HH:mm:ss"));
            mtExtendVO5s.add(mtExtendVO5);
        }
        //?????????????????????
        if(!supplierLotFlag){
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("SUPPLIER_LOT");
            mtExtendVO5.setAttrValue(dto.getSupplierLot());
            mtExtendVO5s.add(mtExtendVO5);
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", targetMaterialLotId, eventId, mtExtendVO5s);
        //??????????????????
        HmeTimeMaterialSplitVO2 result = new HmeTimeMaterialSplitVO2();
        result.setTargetMaterialLotId(targetMaterialLotId);
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(targetMaterialLotId);
        result.setTargetMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        result.setTargetDateTimeFrom(nowDate);
        result.setTargetDateTimeTo(dateTo);
        result.setSupplierLot(dto.getSupplierLot());
        return result;
    }

    private void updateSourceCodeAttr(Long tenantId, HmeTimeMaterialSplitDTO2 dto, List<MtExtendAttrVO> mtExtendAttrVOS) {
        String enableDate = "";
        String deadlineDate = "";
        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOS) {
            switch (mtExtendAttrVO.getAttrName()) {
                case "ENABLE_DATE" :
                    enableDate = mtExtendAttrVO.getAttrValue();
                    break;
                case "DEADLINE_DATE":
                    deadlineDate = mtExtendAttrVO.getAttrValue();
                    break;
            }
        }
        List<MtExtendVO5> attrList = new ArrayList<>();
        if (StringUtils.isBlank(enableDate)) {
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("ENABLE_DATE");
            mtExtendVO5.setAttrValue(dto.getDateTimeFrom());
            attrList.add(mtExtendVO5);
        }
        if (StringUtils.isBlank(deadlineDate)) {
            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("DEADLINE_DATE");
            mtExtendVO52.setAttrValue(dto.getDateTimeTo());
            attrList.add(mtExtendVO52);
        }
        if (CollectionUtils.isNotEmpty(attrList)) {
            //??????ID
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("VALID_TIME_SETTING");
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            //??????API{ materialLotLimitAttrUpdate }????????????????????????
            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
            mtMaterialLotAttrVO3.setEventId(eventId);
            mtMaterialLotAttrVO3.setMaterialLotId(dto.getSourceMaterialLotId());
            mtMaterialLotAttrVO3.setAttr(attrList);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
        }
    }

}
