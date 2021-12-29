package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import com.ruike.wms.infra.mapper.WmsMaterialLotMapper;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Component;
import tarzan.general.api.dto.MtTagGroupObjectDTO3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料批 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 17:18
 */
@Component
public class WmsMaterialLotRepositoryImpl implements WmsMaterialLotRepository {
    private final WmsMaterialLotMapper wmsMaterialLotMapper;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;

    public WmsMaterialLotRepositoryImpl(WmsMaterialLotMapper wmsMaterialLotMapper, MtExtendSettingsRepository mtExtendSettingsRepository) {
        this.wmsMaterialLotMapper = wmsMaterialLotMapper;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
    }

    @Override
    @ProcessLovValue
    public List<WmsMaterialLotAttrVO> selectListWithAttrByIds(Long tenantId, List<String> materialLotIds) {
        return wmsMaterialLotMapper.selectListWithAttrByIds(tenantId, materialLotIds);
    }

    @Override
    @ProcessLovValue
    public WmsMaterialLotAttrVO selectListWithAttrById(Long tenantId, String materialLotId) {
        List<WmsMaterialLotAttrVO> list = wmsMaterialLotMapper.selectListWithAttrByIds(tenantId, Collections.singletonList(materialLotId));
        return list.get(0);
    }

    @Override
    public WmsMaterialLotAttrVO selectWithAttrByCode(Long tenantId, String materialLotCode) {
        return wmsMaterialLotMapper.selectWithAttrByCode(tenantId, materialLotCode);
    }

    @Override
    public List<WmsMaterialLotAttrVO> batchSaveExtendAttr(Long tenantId, String eventId, List<WmsMaterialLotExtendAttrVO> materialLotList) {
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        for (WmsMaterialLotExtendAttrVO attr : materialLotList) {
            MtCommonExtendVO6 extend = new MtCommonExtendVO6();
            List<MtCommonExtendVO5> attrsList = new ArrayList<>();
            extend.setKeyId(attr.getMaterialLotId());
            if (StringUtils.isNotBlank(attr.getFreezeFlag())) {
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.FREEZE_FLAG);
                item.setAttrValue(attr.getFreezeFlag());
                attrsList.add(item);
            }
            if (StringUtils.isNotBlank(attr.getMaterialVersion())) {
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.MATERIAL_VERSION);
                item.setAttrValue(attr.getMaterialVersion());
                attrsList.add(item);
            }
            if (StringUtils.isNotBlank(attr.getMfFlag())) {
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.MF_FLAG);
                item.setAttrValue(attr.getMfFlag());
                attrsList.add(item);
            }
            if (StringUtils.isNotBlank(attr.getSoNum())) {
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.SO_NUM);
                item.setAttrValue(attr.getSoNum());
                attrsList.add(item);
            }
            if (StringUtils.isNotBlank(attr.getSoLineNum())) {
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.SO_LINE_NUM);
                item.setAttrValue(attr.getSoLineNum());
                attrsList.add(item);
            }
            if (StringUtils.isNotBlank(attr.getStatus())) {
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.STATUS);
                item.setAttrValue(attr.getStatus());
                attrsList.add(item);
            }
            if (StringUtils.isNotBlank(attr.getStocktakeFlag())) {
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.STOCKTAKE_FLAG);
                item.setAttrValue(attr.getStocktakeFlag());
                attrsList.add(item);
            }
            if(StringUtils.isNotBlank(attr.getSupplierLot())){
                MtCommonExtendVO5 item = new MtCommonExtendVO5();
                item.setAttrName(WmsMaterialLotExtendAttrVO.SUPPLIER_LOT);
                item.setAttrValue(attr.getSupplierLot());
                attrsList.add(item);
            }
            if (CollectionUtils.isNotEmpty(attrsList)) {
                extend.setAttrs(attrsList);
                attrPropertyList.add(extend);
            }
        }
        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, WmsMaterialLotExtendAttrVO.ATTR_TABLE, eventId, attrPropertyList);
        return wmsMaterialLotMapper.selectListWithAttrByIds(tenantId, materialLotList.stream().map(WmsMaterialLotExtendAttrVO::getMaterialLotId).collect(Collectors.toList()));
    }

    @Override
    public List<MtTagGroupObjectDTO3> batchProductionVersionQuery(Long tenantId, String siteId, List<String> mtMaterialIdList, List<String> materialVersionList) {
        return wmsMaterialLotMapper.batchProductionVersionQuery(tenantId, siteId, mtMaterialIdList, materialVersionList);
    }
}
