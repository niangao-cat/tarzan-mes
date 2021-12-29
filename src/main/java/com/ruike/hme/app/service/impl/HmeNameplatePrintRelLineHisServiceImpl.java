package com.ruike.hme.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.hme.app.service.HmeNameplatePrintRelLineHisService;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelLineHis;
import com.ruike.hme.domain.repository.HmeNameplatePrintRelLineHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 铭牌打印内部识别码对应关系行历史表应用服务默认实现
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:14
 */
@Service
public class HmeNameplatePrintRelLineHisServiceImpl extends BaseAppService implements HmeNameplatePrintRelLineHisService {

    private final HmeNameplatePrintRelLineHisRepository hmeNameplatePrintRelLineHisRepository;

    @Autowired
    public HmeNameplatePrintRelLineHisServiceImpl(HmeNameplatePrintRelLineHisRepository hmeNameplatePrintRelLineHisRepository) {
        this.hmeNameplatePrintRelLineHisRepository = hmeNameplatePrintRelLineHisRepository;
    }

    @Override
    public Page<HmeNameplatePrintRelLineHis> list(Long tenantId, HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis, PageRequest pageRequest) {
        return hmeNameplatePrintRelLineHisRepository.pageAndSort(pageRequest, hmeNameplatePrintRelLineHis);
    }

    @Override
    public HmeNameplatePrintRelLineHis detail(Long tenantId, Long nameplateLineHisId) {
        return hmeNameplatePrintRelLineHisRepository.selectByPrimaryKey(nameplateLineHisId);
    }

    @Override
    public HmeNameplatePrintRelLineHis create(Long tenantId, HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis) {
        validObject(hmeNameplatePrintRelLineHis);
        hmeNameplatePrintRelLineHisRepository.insertSelective(hmeNameplatePrintRelLineHis);
        return hmeNameplatePrintRelLineHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeNameplatePrintRelLineHis update(Long tenantId, HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis) {
        SecurityTokenHelper.validToken(hmeNameplatePrintRelLineHis);
        hmeNameplatePrintRelLineHisRepository.updateByPrimaryKeySelective(hmeNameplatePrintRelLineHis);
        return hmeNameplatePrintRelLineHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(HmeNameplatePrintRelLineHis hmeNameplatePrintRelLineHis) {
        SecurityTokenHelper.validToken(hmeNameplatePrintRelLineHis);
        hmeNameplatePrintRelLineHisRepository.deleteByPrimaryKey(hmeNameplatePrintRelLineHis);
    }
}
