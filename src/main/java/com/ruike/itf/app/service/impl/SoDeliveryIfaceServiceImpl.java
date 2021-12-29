package com.ruike.itf.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.itf.app.service.SoDeliveryIfaceService;
import com.ruike.itf.domain.entity.SoDeliveryIface;
import com.ruike.itf.domain.repository.SoDeliveryIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 销售发退货单接口表应用服务默认实现
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-02 13:41:40
 */
@Service
public class SoDeliveryIfaceServiceImpl extends BaseAppService implements SoDeliveryIfaceService {

    private final SoDeliveryIfaceRepository soDeliveryIfaceRepository;

    @Autowired
    public SoDeliveryIfaceServiceImpl(SoDeliveryIfaceRepository soDeliveryIfaceRepository) {
        this.soDeliveryIfaceRepository = soDeliveryIfaceRepository;
    }

    @Override
    public Page<SoDeliveryIface> list(Long tenantId, SoDeliveryIface soDeliveryIface, PageRequest pageRequest) {
        return soDeliveryIfaceRepository.pageAndSort(pageRequest, soDeliveryIface);
    }

    @Override
    public SoDeliveryIface detail(Long tenantId, Long ifaceId) {
        return soDeliveryIfaceRepository.selectByPrimaryKey(ifaceId);
    }

    @Override
    public SoDeliveryIface create(Long tenantId, SoDeliveryIface soDeliveryIface) {
        validObject(soDeliveryIface);
        soDeliveryIfaceRepository.insertSelective(soDeliveryIface);
        return soDeliveryIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SoDeliveryIface update(Long tenantId, SoDeliveryIface soDeliveryIface) {
        SecurityTokenHelper.validToken(soDeliveryIface);
        soDeliveryIfaceRepository.updateByPrimaryKeySelective(soDeliveryIface);
        return soDeliveryIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(SoDeliveryIface soDeliveryIface) {
        SecurityTokenHelper.validToken(soDeliveryIface);
        soDeliveryIfaceRepository.deleteByPrimaryKey(soDeliveryIface);
    }
}
