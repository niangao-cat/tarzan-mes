package com.ruike.hme.app.service.impl;

import java.util.List;

import com.ruike.hme.api.dto.HmeExcGroupRouterDTO;
import com.ruike.hme.app.service.HmeExcGroupRouterService;
import com.ruike.hme.domain.entity.HmeExcGroupRouter;
import com.ruike.hme.domain.repository.HmeExcGroupRouterRepository;
import com.ruike.hme.domain.vo.HmeExceptionGroupVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 异常收集组异常反馈路线表应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
@Service
public class HmeExcGroupRouterServiceImpl implements HmeExcGroupRouterService {
    @Autowired
    private HmeExcGroupRouterRepository repository;

    @Override
    public Page<HmeExcGroupRouter> listForUi(Long tenantId, String assignId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> repository.queryRouterByAssignId(tenantId, assignId));
    }

    @Override
    public void deleteForUi(Long tenantId, HmeExcGroupRouterDTO dto) {
        repository.deleteById(tenantId, dto);
    }
}
