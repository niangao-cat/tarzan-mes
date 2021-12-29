package com.ruike.wms.domain.service;

import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 配送需求 导出服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 15:04
 */
public interface WmsDistributionDemandExportService {

    /**
     * 导出配送需求
     *
     * @param request  servlet请求
     * @param response servlet响应
     * @param tenantId 租户
     * @param dto   条件
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 03:00:14
     */
    void export(HttpServletRequest request, HttpServletResponse response, Long tenantId, WmsDistDemandQueryDTO dto);
}
