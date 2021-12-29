package com.ruike.hme.app.export;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 条码冻结单 导出服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 14:32
 */
public interface HmeFreezeDocExportService {

    /**
     * 导出
     *
     * @param tenantId    租户
     * @param request     请求
     * @param response    相应
     * @param freezeDocId 单据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 03:59:38
     */
    void export(Long tenantId, HttpServletRequest request, HttpServletResponse response, String freezeDocId);
}
