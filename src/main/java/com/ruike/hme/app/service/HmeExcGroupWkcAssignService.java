package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeExcGroupWkcAssignDTO;

/**
 * 异常收集组分配WKC关系表应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
public interface HmeExcGroupWkcAssignService {
    /**
     * 删除异常收集组WKC关系
     * @param tenantId
     * @param dto
     */
    void deleteForUi(Long tenantId, HmeExcGroupWkcAssignDTO dto);
}
