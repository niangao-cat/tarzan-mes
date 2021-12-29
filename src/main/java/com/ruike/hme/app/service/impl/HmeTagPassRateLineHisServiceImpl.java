package com.ruike.hme.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.hme.app.service.HmeTagPassRateLineHisService;
import com.ruike.hme.domain.entity.HmeTagPassRateLineHis;
import com.ruike.hme.domain.repository.HmeTagPassRateLineHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 偏振度和发散角良率维护行历史表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:38
 */
@Service
public class HmeTagPassRateLineHisServiceImpl extends BaseAppService implements HmeTagPassRateLineHisService {

    private final HmeTagPassRateLineHisRepository hmeTagPassRateLineHisRepository;

    @Autowired
    public HmeTagPassRateLineHisServiceImpl(HmeTagPassRateLineHisRepository hmeTagPassRateLineHisRepository) {
        this.hmeTagPassRateLineHisRepository = hmeTagPassRateLineHisRepository;
    }

    @Override
    public Page<HmeTagPassRateLineHis> list(Long tenantId, HmeTagPassRateLineHis hmeTagPassRateLineHis, PageRequest pageRequest) {
        return hmeTagPassRateLineHisRepository.pageAndSort(pageRequest, hmeTagPassRateLineHis);
    }

    @Override
    public HmeTagPassRateLineHis detail(Long tenantId, Long lineHisId) {
        return hmeTagPassRateLineHisRepository.selectByPrimaryKey(lineHisId);
    }

    @Override
    public HmeTagPassRateLineHis create(Long tenantId, HmeTagPassRateLineHis hmeTagPassRateLineHis) {
        validObject(hmeTagPassRateLineHis);
        hmeTagPassRateLineHisRepository.insertSelective(hmeTagPassRateLineHis);
        return hmeTagPassRateLineHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeTagPassRateLineHis update(Long tenantId, HmeTagPassRateLineHis hmeTagPassRateLineHis) {
        SecurityTokenHelper.validToken(hmeTagPassRateLineHis);
        hmeTagPassRateLineHisRepository.updateByPrimaryKeySelective(hmeTagPassRateLineHis);
        return hmeTagPassRateLineHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(HmeTagPassRateLineHis hmeTagPassRateLineHis) {
        SecurityTokenHelper.validToken(hmeTagPassRateLineHis);
        hmeTagPassRateLineHisRepository.deleteByPrimaryKey(hmeTagPassRateLineHis);
    }
}
