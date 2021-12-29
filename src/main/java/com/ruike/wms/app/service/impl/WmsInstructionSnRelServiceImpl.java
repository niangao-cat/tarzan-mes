package com.ruike.wms.app.service.impl;

import com.ruike.wms.app.service.WmsInstructionSnRelService;
import com.ruike.wms.domain.entity.WmsInstructionSnRel;
import com.ruike.wms.domain.repository.WmsInstructionSnRelRepository;
import org.hzero.core.base.BaseAppService;

import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 单据SN指定表应用服务默认实现
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:07:45
 */
@Service
public class WmsInstructionSnRelServiceImpl extends BaseAppService implements WmsInstructionSnRelService {

    private final WmsInstructionSnRelRepository wmsInstructionSnRelRepository;

    @Autowired
    public WmsInstructionSnRelServiceImpl(WmsInstructionSnRelRepository wmsInstructionSnRelRepository) {
        this.wmsInstructionSnRelRepository = wmsInstructionSnRelRepository;
    }

    @Override
    public Page<WmsInstructionSnRel> list(Long tenantId, WmsInstructionSnRel wmsInstructionSnRel, PageRequest pageRequest) {
        return wmsInstructionSnRelRepository.pageAndSort(pageRequest, wmsInstructionSnRel);
    }

    @Override
    public WmsInstructionSnRel detail(Long tenantId, Long wmsInstructionSnRelId) {
        return wmsInstructionSnRelRepository.selectByPrimaryKey(wmsInstructionSnRelId);
    }

    @Override
    public WmsInstructionSnRel create(Long tenantId, WmsInstructionSnRel wmsInstructionSnRel) {
        validObject(wmsInstructionSnRel);
        wmsInstructionSnRelRepository.insertSelective(wmsInstructionSnRel);
        return wmsInstructionSnRel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsInstructionSnRel update(Long tenantId, WmsInstructionSnRel wmsInstructionSnRel) {
        SecurityTokenHelper.validToken(wmsInstructionSnRel);
        wmsInstructionSnRelRepository.updateByPrimaryKeySelective(wmsInstructionSnRel);
        return wmsInstructionSnRel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(WmsInstructionSnRel wmsInstructionSnRel) {
        SecurityTokenHelper.validToken(wmsInstructionSnRel);
        wmsInstructionSnRelRepository.deleteByPrimaryKey(wmsInstructionSnRel);
    }
}
