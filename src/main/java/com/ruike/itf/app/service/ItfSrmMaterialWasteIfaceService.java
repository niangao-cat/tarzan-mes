package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.domain.entity.ItfSrmMaterialWasteIface;

import java.util.List;

/**
 * 料废调换接口记录表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
 */
public interface ItfSrmMaterialWasteIfaceService {

    /**
     * SRM系统-料废调换创建Rest接口
     *
     * @param itfSrmInstructionIface
     * @param tenantId
     * @return
     * @author jiangling.zheng@hand-china.com 2020/9/21 11:09
     */
    List<ItfSrmMaterialWasteIfaceSyncDTO> srmMaterialWasteExchangeCreate(List<ItfSrmMaterialWasteIfaceSyncDTO> itfSrmInstructionIface, Long tenantId);
}
