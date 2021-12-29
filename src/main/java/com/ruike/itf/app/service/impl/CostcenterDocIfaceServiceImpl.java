package com.ruike.itf.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.itf.app.service.CostcenterDocIfaceService;
import com.ruike.itf.domain.entity.CostcenterDocIface;
import com.ruike.itf.domain.repository.CostcenterDocIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 生产领退料单接口应用服务默认实现
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-06-30 13:48:47
 */
@Service
public class CostcenterDocIfaceServiceImpl extends BaseAppService implements CostcenterDocIfaceService {

    private final CostcenterDocIfaceRepository costcenterDocIfaceRepository;

    @Autowired
    public CostcenterDocIfaceServiceImpl(CostcenterDocIfaceRepository costcenterDocIfaceRepository) {
        this.costcenterDocIfaceRepository = costcenterDocIfaceRepository;
    }

    @Override
    public Page<CostcenterDocIface> list(Long tenantId, CostcenterDocIface costcenterDocIface, PageRequest pageRequest) {
        return costcenterDocIfaceRepository.pageAndSort(pageRequest, costcenterDocIface);
    }

    @Override
    public CostcenterDocIface detail(Long tenantId, Long ifaceId) {
        return costcenterDocIfaceRepository.selectByPrimaryKey(ifaceId);
    }

    @Override
    public CostcenterDocIface create(Long tenantId, CostcenterDocIface costcenterDocIface) {
        validObject(costcenterDocIface);
        costcenterDocIfaceRepository.insertSelective(costcenterDocIface);
        return costcenterDocIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CostcenterDocIface update(Long tenantId, CostcenterDocIface costcenterDocIface) {
        SecurityTokenHelper.validToken(costcenterDocIface);
        costcenterDocIfaceRepository.updateByPrimaryKeySelective(costcenterDocIface);
        return costcenterDocIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(CostcenterDocIface costcenterDocIface) {
        SecurityTokenHelper.validToken(costcenterDocIface);
        costcenterDocIfaceRepository.deleteByPrimaryKey(costcenterDocIface);
    }
}
