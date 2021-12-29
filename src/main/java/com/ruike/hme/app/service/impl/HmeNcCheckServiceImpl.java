package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeNcCheckDTO2;
import com.ruike.hme.api.dto.HmeNcCheckDTO4;
import com.ruike.hme.app.service.HmeNcCheckService;
import com.ruike.hme.domain.repository.HmeNcCheckRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 不良申请单审核应用服务默认实现
 * @author: chaonan.hu@hand-china.com 2020-07-03 10:14:52
 **/
@Service
public class HmeNcCheckServiceImpl implements HmeNcCheckService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeNcCheckRepository hmeNcCheckRepository;

    @Override
    public void ncCheck(Long tenantId, HmeNcCheckDTO2 dto) {
        if(CollectionUtils.isEmpty(dto.getNcCodeIdList())){
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "不良代码", ""));
        }
        if (StringUtils.isEmpty(dto.getProcessMethod())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "处理方式", ""));
        }
        if (StringUtils.isEmpty(dto.getNcGroupId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "不良代码组", ""));
        }
        hmeNcCheckRepository.ncCheck(tenantId, dto);
    }

    @Override
    public void batchCheckSubmit(Long tenantId, HmeNcCheckDTO4 dto) {
        if (CollectionUtils.isEmpty(dto.getNcCodeIdList())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "不良代码", ""));
        }
        if (StringUtils.isEmpty(dto.getProcessMethod())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "处理方式", ""));
        }
        if (StringUtils.isEmpty(dto.getNcGroupId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "不良代码组", ""));
        }
        hmeNcCheckRepository.batchCheckSubmit(tenantId, dto);
    }
}
