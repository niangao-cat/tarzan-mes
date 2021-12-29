package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.FreezeDocRequestDTO;
import com.ruike.itf.api.dto.FreezeDocResponseDTO;
import com.ruike.itf.domain.entity.ItfFreezeDocIface;

/**
 * 条码冻结接口表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2021-03-03 10:08:00
 */
public interface ItfFreezeDocIfaceService {

    /**
     * 接口调用
     *
     * @param request 请求
     * @return java.util.List<com.ruike.itf.api.dto.FreezeDocResponseDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/3 10:36:08
     */
    FreezeDocResponseDTO invoke(FreezeDocRequestDTO request);

    /**
     * 推送
     *
     * @param tenantId 租户
     * @param iface    冻结单接口
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/3 10:36:08
     */
    void send(Long tenantId, ItfFreezeDocIface iface);
}
