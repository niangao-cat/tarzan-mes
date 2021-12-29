package com.ruike.hme.app.service.impl;

import java.util.List;

import com.ruike.hme.api.dto.HmeExceptionRouterDTO;
import com.ruike.hme.app.service.HmeExceptionRouterService;
import com.ruike.hme.domain.entity.HmeExceptionRouter;
import com.ruike.hme.domain.repository.HmeExceptionRouterRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 异常反馈路线基础数据表应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
@Service
public class HmeExceptionRouterServiceImpl extends BaseServiceImpl<HmeExceptionRouter> implements HmeExceptionRouterService {

    private final HmeExceptionRouterRepository repository;

    @Autowired
    public HmeExceptionRouterServiceImpl(HmeExceptionRouterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<HmeExceptionRouter> listForUi(Long tenantId, String exceptionId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> repository.queryRouterByExceptionId(tenantId, exceptionId));
    }

    @Override
    public List<HmeExceptionRouter> batchSaveForUi(Long tenantId, List<HmeExceptionRouter> dtoList) {
        return repository.routerBatchUpdate(tenantId, dtoList);
    }

    @Override
    public void deleteForUi(Long tenantId, HmeExceptionRouterDTO dto) {
        repository.deleteById(tenantId, dto);
    }
}
