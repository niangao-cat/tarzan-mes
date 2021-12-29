package com.ruike.itf.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.itf.app.service.WcsTaskIfaceService;
import com.ruike.itf.domain.entity.WcsTaskIface;
import com.ruike.itf.domain.repository.WcsTaskIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 成品出库指令信息接口表应用服务默认实现
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:04:14
 */
@Service
public class WcsTaskIfaceServiceImpl extends BaseAppService implements WcsTaskIfaceService {

    private final WcsTaskIfaceRepository wcsTaskIfaceRepository;

    @Autowired
    public WcsTaskIfaceServiceImpl(WcsTaskIfaceRepository wcsTaskIfaceRepository) {
        this.wcsTaskIfaceRepository = wcsTaskIfaceRepository;
    }

    @Override
    public Page<WcsTaskIface> list(Long tenantId, WcsTaskIface wcsTaskIface, PageRequest pageRequest) {
        return wcsTaskIfaceRepository.pageAndSort(pageRequest, wcsTaskIface);
    }

    @Override
    public WcsTaskIface detail(Long tenantId, Long ifaceId) {
        return wcsTaskIfaceRepository.selectByPrimaryKey(ifaceId);
    }

    @Override
    public WcsTaskIface create(Long tenantId, WcsTaskIface wcsTaskIface) {
        validObject(wcsTaskIface);
        wcsTaskIfaceRepository.insertSelective(wcsTaskIface);
        return wcsTaskIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WcsTaskIface update(Long tenantId, WcsTaskIface wcsTaskIface) {
        SecurityTokenHelper.validToken(wcsTaskIface);
        wcsTaskIfaceRepository.updateByPrimaryKeySelective(wcsTaskIface);
        return wcsTaskIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(WcsTaskIface wcsTaskIface) {
        SecurityTokenHelper.validToken(wcsTaskIface);
        wcsTaskIfaceRepository.deleteByPrimaryKey(wcsTaskIface);
    }
}
