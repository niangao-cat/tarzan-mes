package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO;

import java.util.List;

/**
 * 出货单明细 Service
 *
 * @author faming.yang@hand-china.com 2021-07-15 18:31
 */
public interface WmsSoDeliveryDetailService {
    void delete(Long tenantId, List<WmsSoDeliveryDetailVO> voList, Double lineActualQty, Double lineDemandQty, String instructionDocId);
}
