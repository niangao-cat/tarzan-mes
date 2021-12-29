package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 记录锁定表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-09-12 11:33:17
 */
@Service
public class HmeObjectRecordLockServiceImpl implements HmeObjectRecordLockService {

    @Override
    public HmeObjectRecordLock getRecordLock(Long tenantId, HmeObjectRecordLockDTO dto) {
        HmeObjectRecordLock recordLock = new HmeObjectRecordLock();
        recordLock.setTenantId(tenantId);
        recordLock.setFunctionName(dto.getFunctionName());
        recordLock.setDeviceCode(dto.getDeviceCode());
        recordLock.setObjectType(dto.getObjectType());
        recordLock.setObjectRecordId(dto.getObjectRecordId());
        recordLock.setObjectRecordCode(dto.getObjectRecordCode());
        return recordLock;
    }
}
