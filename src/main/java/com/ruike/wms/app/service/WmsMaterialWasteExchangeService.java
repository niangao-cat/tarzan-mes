package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO2;
import com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO3;

import java.util.List;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/5/7 14:50
 * @Version 1.0
 */
public interface WmsMaterialWasteExchangeService {
    /**
     * 实物条码  扫码查询
     *
     * @param tenantId 1租户
     * @param barCode  2 实物条码
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialWasteExchangeDTO>
     * @author tong.li 2020/5/7 15:14
     */
    WmsMaterialWasteExchangeDTO2 containerOrMaterialLotQuery(Long tenantId, String barCode);


    /**
     * 料废调换执行
     *
     * @param tenantId 租户ID
     * @param lineList 行数据
     * @author tong.li 2020/5/8 11:28
     */
    void execute(Long tenantId, List<WmsMaterialWasteExchangeDTO3> lineList);


}
