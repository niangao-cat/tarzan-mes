package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeResponseDTO;
import com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeSnDTO;

/**
 * @Classname HmeEoJobFirstProcessUpgradeService
 * @Description PDA首序SN升级
 * @Date 2020/9/3 10:59
 * @Author yuchao.wang
 */
public interface HmeEoJobFirstProcessUpgradeService {


    /**
     *
     * @Description 基座条码扫描
     *
     * @author yuchao.wang
     * @date 2020/9/3 11:16
     * @param tenantId 租户ID
     * @param barcode 条码
     * @return com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeResponseDTO
     *
     */
    HmeEoJobFirstProcessUpgradeResponseDTO baseBarcodeScan(Long tenantId, String barcode);

    /**
     *
     * @Description SN升级
     *
     * @author yuchao.wang
     * @date 2020/9/3 16:48
     * @param tenantId 租户ID
     * @param upgradeSnDTO 参数
     * @return void
     *
     */
    void snUpgrade(Long tenantId, HmeEoJobFirstProcessUpgradeSnDTO upgradeSnDTO);
}
