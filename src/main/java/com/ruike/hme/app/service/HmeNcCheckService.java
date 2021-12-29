package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeNcCheckDTO2;
import com.ruike.hme.api.dto.HmeNcCheckDTO4;

/**
 * 不良申请单审核应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-07-03 10:12:23
 */
public interface HmeNcCheckService {

    /**
     * 不良审核提交
     *
     * @param tenantId 租户ID
     * @param dto      提交数据
     */
    void ncCheck(Long tenantId, HmeNcCheckDTO2 dto);

    /**
     * 批量不良审核提交
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/27 15:49
     */
    void batchCheckSubmit(Long tenantId, HmeNcCheckDTO4 dto);
}
