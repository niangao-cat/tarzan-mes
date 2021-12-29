package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsPurchaseOrderDTO;
import com.ruike.wms.domain.vo.WmsPurchaseOrderVO;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.domain.vo.MtInstructionReturnVO;

import java.util.List;

/**
 * 采购订单接收服务层
 *
 * @author han.zhang 2020/03/19 11:03
 */
public interface WmsPurchaseOrderService {
    /**
     * 采购订单头信息查询
     *
     * @param condition   查询条件
     * @param pageRequest 页码
     * @param tenantId    租户ID
     * @return java.util.List<tarzan.inventory.domain.vo.PurchaseOrderVO>
     * @author han.zhang 2020-03-19 11:09
     */
    List<WmsPurchaseOrderVO> selectHeadData(WmsPurchaseOrderDTO condition, PageRequest pageRequest, Long tenantId);

    /**
     * 采购订单行信息查询
     *
     * @param sourceInstructionId 头id
     * @param pageRequest         页码
     * @return java.lang.Object
     * @author han.zhang 2020-03-19 16:03
     */
    List<MtInstructionReturnVO> selectLineData(String sourceInstructionId, PageRequest pageRequest);
}