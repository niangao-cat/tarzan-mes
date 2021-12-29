package com.ruike.itf.app.service;

/**
 * SAP 工单SN码关系接口应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
public interface ItfWoSnRelIfaceService {

    /**
     * 接口表执行成功状态
     */
    String STATUS_SUCCESS = "S";

    /**
     * 接口表执行失败状态
     */
    String STATUS_ERROR = "E";

    /**
     * 接口表初始状态状态
     */
    String STATUS_NORMAL = "N";

    /**
     * 接口表执行中状态
     */
    String STATUS_PROCESS = "P";

    /**
     * HTTP请求成功
     */
    Integer HTTP_STATUS_OK = 200;

    /**
     * ESB接口成功状态码
     */
    String ESB_STATUS_SUCCESS = "S00001";

    /**
     * 工单SN码关系同步接口
     *
     * @param tenantId 租户
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 04:47:25
     */
    void invoke(Long tenantId);
}
