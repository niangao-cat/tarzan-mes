package com.ruike.hme.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.hme.app.service.HmeTagPassRateHeaderHisService;
import com.ruike.hme.domain.entity.HmeTagPassRateHeaderHis;
import com.ruike.hme.domain.repository.HmeTagPassRateHeaderHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 偏振度和发散角良率维护头历史表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:36
 */
@Service
public class HmeTagPassRateHeaderHisServiceImpl extends BaseAppService implements HmeTagPassRateHeaderHisService {

    private final HmeTagPassRateHeaderHisRepository hmeTagPassRateHeaderHisRepository;

    @Autowired
    public HmeTagPassRateHeaderHisServiceImpl(HmeTagPassRateHeaderHisRepository hmeTagPassRateHeaderHisRepository) {
        this.hmeTagPassRateHeaderHisRepository = hmeTagPassRateHeaderHisRepository;
    }

    @Override
    public Page<HmeTagPassRateHeaderHis> list(Long tenantId, HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis, PageRequest pageRequest) {
        return hmeTagPassRateHeaderHisRepository.pageAndSort(pageRequest, hmeTagPassRateHeaderHis);
    }

    @Override
    public HmeTagPassRateHeaderHis detail(Long tenantId, Long headerHisId) {
        return hmeTagPassRateHeaderHisRepository.selectByPrimaryKey(headerHisId);
    }

    @Override
    public HmeTagPassRateHeaderHis create(Long tenantId, HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis) {
        validObject(hmeTagPassRateHeaderHis);
        hmeTagPassRateHeaderHisRepository.insertSelective(hmeTagPassRateHeaderHis);
        return hmeTagPassRateHeaderHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeTagPassRateHeaderHis update(Long tenantId, HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis) {
        SecurityTokenHelper.validToken(hmeTagPassRateHeaderHis);
        hmeTagPassRateHeaderHisRepository.updateByPrimaryKeySelective(hmeTagPassRateHeaderHis);
        return hmeTagPassRateHeaderHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis) {
        SecurityTokenHelper.validToken(hmeTagPassRateHeaderHis);
        hmeTagPassRateHeaderHisRepository.deleteByPrimaryKey(hmeTagPassRateHeaderHis);
    }
}
