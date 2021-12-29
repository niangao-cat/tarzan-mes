package com.ruike.itf.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.itf.app.service.ItfCosMonitorIfaceService;
import com.ruike.itf.domain.entity.ItfCosMonitorIface;
import com.ruike.itf.domain.repository.ItfCosMonitorIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * COS良率监控接口表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-30 14:14:20
 */
@Service
public class ItfCosMonitorIfaceServiceImpl extends BaseAppService implements ItfCosMonitorIfaceService {

    private final ItfCosMonitorIfaceRepository itfCosMonitorIfaceRepository;

    @Autowired
    public ItfCosMonitorIfaceServiceImpl(ItfCosMonitorIfaceRepository itfCosMonitorIfaceRepository) {
        this.itfCosMonitorIfaceRepository = itfCosMonitorIfaceRepository;
    }

    @Override
    public Page<ItfCosMonitorIface> list(Long tenantId, ItfCosMonitorIface itfCosMonitorIface, PageRequest pageRequest) {
        return itfCosMonitorIfaceRepository.pageAndSort(pageRequest, itfCosMonitorIface);
    }

    @Override
    public ItfCosMonitorIface detail(Long tenantId, Long cosMonitorIfaceId) {
        return itfCosMonitorIfaceRepository.selectByPrimaryKey(cosMonitorIfaceId);
    }

    @Override
    public ItfCosMonitorIface create(Long tenantId, ItfCosMonitorIface itfCosMonitorIface) {
        validObject(itfCosMonitorIface);
        itfCosMonitorIfaceRepository.insertSelective(itfCosMonitorIface);
        return itfCosMonitorIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItfCosMonitorIface update(Long tenantId, ItfCosMonitorIface itfCosMonitorIface) {
        SecurityTokenHelper.validToken(itfCosMonitorIface);
        itfCosMonitorIfaceRepository.updateByPrimaryKeySelective(itfCosMonitorIface);
        return itfCosMonitorIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(ItfCosMonitorIface itfCosMonitorIface) {
        SecurityTokenHelper.validToken(itfCosMonitorIface);
        itfCosMonitorIfaceRepository.deleteByPrimaryKey(itfCosMonitorIface);
    }
}
