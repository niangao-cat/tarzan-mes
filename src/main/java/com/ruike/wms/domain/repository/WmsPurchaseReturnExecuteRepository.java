package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsPoDeliveryConfirmDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryDetailDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryScanDTO2;
import com.ruike.wms.domain.vo.WmsPurchaseLineVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnExecuteDetailsVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnExecuteDocVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnScanVO;

import java.util.List;

/**
 * 采购退货-执行
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 19:54
 */
public interface WmsPurchaseReturnExecuteRepository {

    /**
     * 扫描采购订单
     *
     * @param tenantId
     * @param instructionDocNum
     * @author sanfeng.zhang@hand-china.com 2020/11/9 20:41
     * @return com.ruike.wms.domain.vo.WmsPurchaseReturnExecuteDocVO
     */
    WmsPurchaseReturnExecuteDocVO scanInstructionDocNum(Long tenantId, String instructionDocNum);

    /**
     * 扫描条码
     *
     * @param tenantId
     * @param dto2
     * @author sanfeng.zhang@hand-china.com 2020/11/10 12:28
     * @return com.ruike.wms.domain.vo.WmsPurchaseReturnScanVO
     */
    WmsPurchaseReturnScanVO scanMaterialLotCode(Long tenantId, WmsPoDeliveryScanDTO2 dto2);

    /**
     * 明细查询
     *
     * @param tenantId
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2020/11/10 20:54
     * @return com.ruike.wms.domain.vo.WmsPurchaseReturnExecuteDetailsVO
     */
    WmsPurchaseReturnExecuteDetailsVO queryCodeDetails(Long tenantId, String instructionId);

    /**
     * 明细页面-删除
     *
     * @param tenantId
     * @param dtoList
     * @author sanfeng.zhang@hand-china.com 2020/11/10 21:32
     * @return com.ruike.wms.domain.vo.WmsPurchaseLineVO
     */
    WmsPurchaseLineVO deleteCodeDetails(Long tenantId, List<WmsPoDeliveryDetailDTO> dtoList);

    /**
     * 采购退货执行
     *
     * @param tenantId
     * @param confirmDTO
     * @author sanfeng.zhang@hand-china.com 2020/11/11 2:07
     * @return com.ruike.wms.api.dto.WmsPoDeliveryConfirmDTO
     */
    WmsPoDeliveryConfirmDTO purchaseReturnExecute(Long tenantId, WmsPoDeliveryConfirmDTO confirmDTO);

    /**
     * 查询是否全部退货
     *
     * @param tenantId
     * @param instructionDocId
     * @author sanfeng.zhang@hand-china.com 2020/11/11 15:03
     * @return java.lang.Boolean
     */
    Boolean queryAllReturn(Long tenantId, String instructionDocId);
}
