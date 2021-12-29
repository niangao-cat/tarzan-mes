package com.ruike.itf.app.service;

import com.ruike.itf.domain.vo.ServiceTransferIfaceInvokeVO;

import java.util.List;

/**
 * 售后大仓回调应用服务
 *
 * @author yonghui.zhu@hand-china.com 2021-04-01 14:05:32
 */
public interface ItfServiceTransferIfaceService {

    void invoke(List<ServiceTransferIfaceInvokeVO> list);
}
