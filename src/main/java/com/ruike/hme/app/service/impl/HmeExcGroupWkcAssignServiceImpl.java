package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeExcGroupWkcAssignDTO;
import com.ruike.hme.app.service.HmeExcGroupWkcAssignService;
import com.ruike.hme.domain.repository.HmeExcGroupWkcAssignRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 异常收集组分配WKC关系表应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
@Service
public class HmeExcGroupWkcAssignServiceImpl implements HmeExcGroupWkcAssignService {
    @Autowired
    private HmeExcGroupWkcAssignRepository repository;

    @Override
    public void deleteForUi(Long tenantId, HmeExcGroupWkcAssignDTO dto) {
        repository.deleteByPrimaryKey(dto.getExcGroupWkcAssignId());
    }
}
