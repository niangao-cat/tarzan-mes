package com.ruike.itf.app.service;

import com.ruike.itf.domain.entity.ItfLogisticsServiceReceIface;

import java.util.List;

/**
 * 售后信息回传ERP接口记录表应用服务
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 10:49:32
 */
public interface ItfLogisticsServiceReceIfaceService {

    /**
     *
     * 售后信息回传ERP
     * @param dto
     * @author kejin.liu01@hand-china.com 2020/9/2 10:52
     * @return 返回错误数据，可查看长度确认是否发送成功，存在message字段，有错误信息的存在，也可查看接口记录表itf_logistics_service_rece_iface
     */
    List<ItfLogisticsServiceReceIface> sendErpLogisticsMsg (List<ItfLogisticsServiceReceIface> dto,Long tenantId) ;

}
