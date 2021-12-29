package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeOperationInstructionService;
import com.ruike.hme.domain.entity.HmeOperationInstruction;
import com.ruike.hme.domain.repository.HmeOperationInstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 作业指导应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-10-20 16:07:50
 */
@Service
public class HmeOperationInstructionServiceImpl implements HmeOperationInstructionService {

    @Autowired
    private HmeOperationInstructionRepository hmeOperationInsRepository;

    @Override
    public Page<HmeOperationInstruction> listForUi(Long tenantId, String insHeaderId, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> hmeOperationInsRepository.operationInsQuery(tenantId, insHeaderId));
    }

    @Override
    public List<HmeOperationInstruction> saveForUi(Long tenantId, List<HmeOperationInstruction> hmeOperationInsList) {
        for (HmeOperationInstruction hmeOperationInstruction : hmeOperationInsList) {
            hmeOperationInsRepository.operationInsUpdate(tenantId, hmeOperationInstruction);
        }
        return hmeOperationInsList;
    }
}
