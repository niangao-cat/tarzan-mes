package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeExceptionService;
import com.ruike.hme.domain.entity.HmeException;
import com.ruike.hme.domain.repository.HmeExceptionRepository;
import com.ruike.hme.api.dto.HmeExceptionDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 异常维护基础数据头表应用服务默认实现
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
@Service
public class HmeExceptionServiceImpl extends BaseServiceImpl<HmeException> implements HmeExceptionService {

    private final HmeExceptionRepository repository;

    @Autowired
    public HmeExceptionServiceImpl(HmeExceptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<HmeException> listForUi(Long tenantId, HmeExceptionDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> repository.exceptionUiQuery(tenantId, dto));
    }

    @Override
    public HmeException saveForUi(Long tenantId, HmeException dto) {
        return repository.exceptionBasicPropertyUpdate(tenantId, dto);
    }
}
