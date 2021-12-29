package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;
import com.ruike.wms.api.dto.WmsMaterialLotSplitDTO;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO2;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO3;

import java.util.List;

/**
 * @description: 条码拆分service
 *
 * @author: chaonan.hu@hand-china.com 2020-09-07 15:22:37
 **/
public interface WmsMaterialLotSplitService {

    /**
     * 扫描原始实物条码
     * 
     * @param tenantId 租户ID
     * @param materialLotCode 原始实物条码
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/7 15:55:27
     * @return com.ruike.wms.domain.vo.WmsMaterialLotSplitVO
     */
    WmsMaterialLotSplitVO3 scanSourceBarcode(Long tenantId, String materialLotCode);

    /**
     * 扫描条码编码
     *
     * @param tenantId 租户ID
     * @param materialLotCode 条码编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/7 16:35:04
     * @return com.ruike.wms.domain.vo.WmsMaterialLotSplitVO2
     */
    WmsMaterialLotSplitVO2 scanBarcode(Long tenantId, String materialLotCode);

    /**
     * 拆分条码
     * 
     * @param tenantId 租户ID
     * @param dto 拆分信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/7 16:49:03
     * @return com.ruike.wms.api.dto.WmsMaterialLotSplitDTO
     */
    List<WmsMaterialLotSplitVO3> splitBarcode(Long tenantId, WmsMaterialLotSplitDTO dto);
}