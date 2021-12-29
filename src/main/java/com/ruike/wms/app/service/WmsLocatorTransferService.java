package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsLocatorScanDTO;
import com.ruike.wms.api.dto.WmsLocatorTransferDTO;
import com.ruike.wms.api.dto.WmsMaterialLotScanDTO;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO2;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 货位扫描
 *
 * @author han.zhang 2020/05/08 10:58
 */
public interface WmsLocatorTransferService {
    /**
     * 货位扫描
     *
     * @param tenantId       租户ID
     * @param locatorScanDTO 扫描数据
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author han.zhang 2020-05-08 12:01
     */
    MtModLocator locatorScan(Long tenantId, WmsLocatorScanDTO locatorScanDTO);

    /**
     * 条码扫描
     *
     * @param tenantId              租户ID
     * @param wmsMaterialLotScanDTO 扫描数据
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author han.zhang 2020-05-08 12:01
     */
    WmsLocatorTransferVO2 materialLotScan(Long tenantId, WmsMaterialLotScanDTO wmsMaterialLotScanDTO);

    /**
     * 库位转移
     *
     * @param tenantId              租户Id
     * @param wmsLocatorTransferDTO 转移的数据
     * @return com.ruike.wms.api.dto.WmsLocatorTransferDTO
     * @author han.zhang 2020-05-18 17:18
     */
    WmsLocatorTransferDTO transfer(Long tenantId, WmsLocatorTransferDTO wmsLocatorTransferDTO);
}