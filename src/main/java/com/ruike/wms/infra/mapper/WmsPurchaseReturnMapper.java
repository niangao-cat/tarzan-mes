package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.WmsPurchaseReturnDetailsVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnHeadVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnLineVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnVO;
import io.choerodon.core.domain.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/9 14:50
 */
public interface WmsPurchaseReturnMapper {

    /**
     * 采购退货平台-头信息查询
     *
     * @param tenantId
     * @param returnVO
     * @author sanfeng.zhang@hand-china.com 2020/11/9 14:52
     * @return java.util.List<com.ruike.wms.domain.vo.WmsPurchaseReturnHeadVO>
     */
    List<WmsPurchaseReturnHeadVO> purchaseReturnHeaderQuery(@Param("tenantId") Long tenantId, @Param("returnVO")WmsPurchaseReturnVO returnVO);

    /**
     * 采购退货头信息查询
     *
     * @param tenantId
     * @param instructionDocId
     * @param docTypeList
     * @author sanfeng.zhang@hand-china.com 2020/11/19 14:52
     * @return com.ruike.wms.domain.vo.WmsPurchaseReturnHeadVO
     */
    WmsPurchaseReturnHeadVO selectPurchaseReturnPrintHead(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId, @Param("docTypeList") List<String> docTypeList);

    /**
     * 采购退货平台-头信息查询
     *
     * @param tenantId
     * @param sourceDocId
     * @author sanfeng.zhang@hand-china.com 2020/11/9 15:46
     * @return java.util.List<com.ruike.wms.domain.vo.WmsPurchaseReturnLineVO>
     */
    List<WmsPurchaseReturnLineVO> purchaseReturnLineQuery(@Param("tenantId") Long tenantId, @Param("sourceDocId") String sourceDocId);

    /**
     * 采购退货平台-明细信息查询
     *
     * @param tenantId
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2020/11/9 17:03
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsPurchaseReturnDetailsVO>
     */
    List<WmsPurchaseReturnDetailsVO> purchaseReturnDetailsQuery(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);
}
