package com.ruike.wms.app.service;

import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import com.ruike.wms.api.dto.WmsStandingWarehouseEntryReviewDTO;

import java.util.List;

public interface WmsStandingWarehouseEntryReviewService {
    /**
     * @description:条码扫描
     * @return:
     * @author: xiaojiang
     * @time: 2021/6/28 14:10
     */
    WmsStandingWarehouseEntryReviewDTO queryBarcode(Long tenantId, WmsStandingWarehouseEntryReviewDTO dto);

    List<ItfMaterialLotConfirmIfaceVO4> confirm(Long tenantId, WmsStandingWarehouseEntryReviewDTO dtoList);
}
