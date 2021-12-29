package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.*;
import com.ruike.itf.domain.vo.ItfWoStatusSendErp;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 工单接口表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfWorkOrderIfaceService {

    /**
     * 物料同步接口
     *
     * @param dto com.ruike.itf.api.dto.ItfWorkOrderSyncParamDTO
     * @return List<com.ruike.itf.api.dto.ItfWorkOrderReturnDTO>
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:04
     */
    List<ItfWorkOrderReturnDTO> invoke(Map<String, Object> dto);

    /**
     * 工单状态回传接口
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    void erpWorkOrderStatusReturnRestProxy(Long tenantId) throws ParseException;
    /**
     * 工单状态回传接口
     *
     * @param tenantId
     * @return
     * @author kejin.liu01@hand-china.com
     */
    void erpWorkOrderStatusReturnRestProxy(Long tenantId, List<String> workOrderIds);

    /**
     * 工单降阶品结算比例接口
     *
     * @param tenantId
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com 2020/9/4 10:12
     */
    List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateRestProxy(Long tenantId, List<ErpReducedSettleRadioUpdateDTO> dto) throws Exception;
}
