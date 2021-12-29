package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeBatchLoadContainerService;
import com.ruike.hme.domain.repository.HmeBatchLoadContainerRepository;
import com.ruike.hme.domain.vo.HmeLoadContainerVO;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * HmeBatchLoadContainerServiceImpl
 * 批量完工装箱 应用服务实现
 *
 * @author Deng xu
 * @date 2020/6/5 9:53
 */
@Service
public class HmeBatchLoadContainerServiceImpl implements HmeBatchLoadContainerService {

    @Autowired
    private HmeBatchLoadContainerRepository repository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public HmeLoadContainerVO scanOuterContainer(Long tenantId, String outerContainerCode) {
        return repository.scanOuterContainer(tenantId,outerContainerCode);
    }

    @Override
    public HmeLoadContainerVO scanContainer(Long tenantId ,HmeLoadContainerVO loadContainerVO) {
        return repository.scanContainer(tenantId,loadContainerVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeLoadContainerVO unloadContainer(Long tenantId, HmeLoadContainerVO loadContainer) {
        repository.unloadContainer(tenantId,loadContainer);
        return loadContainer;
    }

    @Override
    public HmeLoadContainerVO loadContainer(Long tenantId, HmeLoadContainerVO loadContainerVO) {
        repository.loadContainer(tenantId,loadContainerVO);
        return loadContainerVO;
    }

    @Override
    public HmeLoadContainerVO verifyContainer(Long tenantId, HmeLoadContainerVO loadContainerVO) {
        return repository.verifyContainer(tenantId, loadContainerVO);
    }

    @Override
    public HmeLoadContainerVO unloadOriginalContainer(Long tenantId, HmeLoadContainerVO loadContainerVO) {
        return repository.unloadOriginalContainer(tenantId, loadContainerVO);
    }

}
