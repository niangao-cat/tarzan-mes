package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;

/**
 * 记录锁定表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-09-12 11:33:17
 */
public interface HmeObjectRecordLockService {

    /**
     * 数据封装
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/9/12 17:42
     * @return com.ruike.hme.domain.entity.HmeObjectRecordLock
     */

    HmeObjectRecordLock getRecordLock(Long tenantId, HmeObjectRecordLockDTO dto);

}
