package com.ruike.hme.app.service.impl;

import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.hme.app.service.HmeNameplatePrintRelHeaderHisService;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelHeaderHis;
import com.ruike.hme.domain.repository.HmeNameplatePrintRelHeaderHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 铭牌打印内部识别码对应关系头历史表应用服务默认实现
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:12
 */
@Service
public class HmeNameplatePrintRelHeaderHisServiceImpl extends BaseAppService implements HmeNameplatePrintRelHeaderHisService {

    private final HmeNameplatePrintRelHeaderHisRepository hmeNameplatePrintRelHeaderHisRepository;

    @Autowired
    public HmeNameplatePrintRelHeaderHisServiceImpl(HmeNameplatePrintRelHeaderHisRepository hmeNameplatePrintRelHeaderHisRepository) {
        this.hmeNameplatePrintRelHeaderHisRepository = hmeNameplatePrintRelHeaderHisRepository;
    }

    @Override
    public Page<HmeNameplatePrintRelHeaderHis> list(Long tenantId, HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis, PageRequest pageRequest) {
        return hmeNameplatePrintRelHeaderHisRepository.pageAndSort(pageRequest, hmeNameplatePrintRelHeaderHis);
    }

    @Override
    public HmeNameplatePrintRelHeaderHis detail(Long tenantId, Long nameplateHeaderHisId) {
        return hmeNameplatePrintRelHeaderHisRepository.selectByPrimaryKey(nameplateHeaderHisId);
    }

    @Override
    public HmeNameplatePrintRelHeaderHis create(Long tenantId, HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis) {
        validObject(hmeNameplatePrintRelHeaderHis);
        hmeNameplatePrintRelHeaderHisRepository.insertSelective(hmeNameplatePrintRelHeaderHis);
        return hmeNameplatePrintRelHeaderHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeNameplatePrintRelHeaderHis update(Long tenantId, HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis) {
        SecurityTokenHelper.validToken(hmeNameplatePrintRelHeaderHis);
        hmeNameplatePrintRelHeaderHisRepository.updateByPrimaryKeySelective(hmeNameplatePrintRelHeaderHis);
        return hmeNameplatePrintRelHeaderHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis) {
        SecurityTokenHelper.validToken(hmeNameplatePrintRelHeaderHis);
        hmeNameplatePrintRelHeaderHisRepository.deleteByPrimaryKey(hmeNameplatePrintRelHeaderHis);
    }
}
