package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsPurchaseReturnDetailsVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnHeadVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnLineVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 采购退货平台
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 13:42
 */
public interface WmsPurchaseReturnRepository {

    /**
     * 采购退货平台-头信息查询
     *
     * @param tenantId
     * @param pageRequest
     * @param wmsPurchaseReturnVO
     * @author sanfeng.zhang@hand-china.com 2020/11/9 14:46
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsPurchaseReturnHeadVO>
     */
    Page<WmsPurchaseReturnHeadVO> purchaseReturnHeaderQuery(Long tenantId, PageRequest pageRequest, WmsPurchaseReturnVO wmsPurchaseReturnVO);

    /**
     * 采购退货平台-头信息查询
     *
     * @param tenantId
     * @param pageRequest
     * @param sourceDocId
     * @author sanfeng.zhang@hand-china.com 2020/11/9 15:43
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsPurchaseReturnLineVO>
     */
    Page<WmsPurchaseReturnLineVO> purchaseReturnLineQuery(Long tenantId, PageRequest pageRequest, String sourceDocId);

    /**
     * 采购退货平台-明细信息查询
     *
     * @param tenantId
     * @param pageRequest
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2020/11/9 16:44
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsPurchaseReturnDetailsVO>
     */
    Page<WmsPurchaseReturnDetailsVO> purchaseReturnDetailsQuery(Long tenantId, PageRequest pageRequest, String instructionId);

    /**
     * 打印
     *
     * @param tenantId
     * @param instructionDocIdList
     * @param httpServletResponse
     * @author sanfeng.zhang@hand-china.com 2020/11/19 14:37
     * @return void
     */
    void multiplePrint(Long tenantId, List<String> instructionDocIdList, HttpServletResponse httpServletResponse);
}
