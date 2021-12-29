package com.ruike.itf.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.itf.app.service.WcsTaskLineIfaceService;
import com.ruike.itf.domain.entity.WcsTaskLineIface;
import com.ruike.itf.domain.repository.WcsTaskLineIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 成品出库指令信息接口行表应用服务默认实现
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:04:14
 */
@Service
public class WcsTaskLineIfaceServiceImpl extends BaseAppService implements WcsTaskLineIfaceService {

    private final WcsTaskLineIfaceRepository wcsTaskLineIfaceRepository;

    @Autowired
    public WcsTaskLineIfaceServiceImpl(WcsTaskLineIfaceRepository wcsTaskLineIfaceRepository) {
        this.wcsTaskLineIfaceRepository = wcsTaskLineIfaceRepository;
    }

    @Override
    public Page<WcsTaskLineIface> list(Long tenantId, WcsTaskLineIface wcsTaskLineIface, PageRequest pageRequest) {
        return wcsTaskLineIfaceRepository.pageAndSort(pageRequest, wcsTaskLineIface);
    }

    @Override
    public WcsTaskLineIface detail(Long tenantId, Long ifaceId) {
        return wcsTaskLineIfaceRepository.selectByPrimaryKey(ifaceId);
    }

    @Override
    public WcsTaskLineIface create(Long tenantId, WcsTaskLineIface wcsTaskLineIface) {
        validObject(wcsTaskLineIface);
        wcsTaskLineIfaceRepository.insertSelective(wcsTaskLineIface);
        return wcsTaskLineIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WcsTaskLineIface update(Long tenantId, WcsTaskLineIface wcsTaskLineIface) {
        SecurityTokenHelper.validToken(wcsTaskLineIface);
        wcsTaskLineIfaceRepository.updateByPrimaryKeySelective(wcsTaskLineIface);
        return wcsTaskLineIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(WcsTaskLineIface wcsTaskLineIface) {
        SecurityTokenHelper.validToken(wcsTaskLineIface);
        wcsTaskLineIfaceRepository.deleteByPrimaryKey(wcsTaskLineIface);
    }
}
