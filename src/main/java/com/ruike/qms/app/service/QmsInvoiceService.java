package com.ruike.qms.app.service;


import com.ruike.qms.api.dto.QmsInvoiceAssemblyReturnDTO;
import com.ruike.qms.api.dto.QmsInvoiceDataQueryDTO;
import com.ruike.qms.api.dto.QmsInvoiceDataReturnDTO;
import com.ruike.qms.domain.vo.QmsOutsourceInvoiceVO;

/**
 * 外协发货单应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-06-10 10:08:46
 */
public interface QmsInvoiceService {
    /**
     * 订单组件信息查询
     *
     * @param tenantId      租户ID
     * @param instructionId 订单组件ID
     * @return com.ruike.qms.api.dto.QmsInvoiceAssemblyReturnDTO
     * @author chaonan.hu 2020/6/10
     */
    QmsInvoiceAssemblyReturnDTO assemblyDataForUi(Long tenantId, String instructionId);

    /**
     * 外协发货单信息预生成
     *
     * @param tenantId 租户ID
     * @param queryDTO 查询参数
     * @return com.ruike.qms.api.dto.QmsInvoiceDataReturnDTO
     * @author chaonan.hu 2020/6/10
     */
    QmsInvoiceDataReturnDTO invoiceDataForUi(Long tenantId, QmsInvoiceDataQueryDTO queryDTO);

    /**
     * 外协发货单创建
     *
     * @param tenantId
     * @param invoiceVO
     * @author sanfeng.zhang@hand-china.com 2020/10/23 18:24
     * @return com.ruike.qms.api.dto.QmsInvoiceDataReturnDTO
     */
    QmsInvoiceDataReturnDTO outsourceInvoiceQuery(Long tenantId, QmsOutsourceInvoiceVO invoiceVO);

    /**
     * 发货单创建
     *
     * @param tenantId 租户ID
     * @param dto      创建数据
     * @author chaonan.hu 2020/6/11
     */
    void invoiceCreate(Long tenantId, QmsInvoiceDataReturnDTO dto);
}
