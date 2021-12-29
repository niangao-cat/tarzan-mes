package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeChipLabCodeInputDTO;
import com.ruike.hme.domain.vo.HmeChipLabCodeInputVO;

/**
 * 芯片实验代码录入应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-11-01 11:04:12
 */
public interface HmeChipLabCodeInputService {

    /**
     * 扫描盒子号
     *
     * @param tenantId
     * @param barcode
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/1 11:38:38
     * @return com.ruike.hme.domain.vo.HmeChipLabCodeInputVO
     */
    HmeChipLabCodeInputVO scanBarCode(Long tenantId, String barcode);
    
    /**
     * 确认
     * 
     * @param tenantId 租户ID
     * @param dto 确认数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/1 03:03:12 
     * @return void
     */
    void confirm(Long tenantId, HmeChipLabCodeInputDTO dto);
}
