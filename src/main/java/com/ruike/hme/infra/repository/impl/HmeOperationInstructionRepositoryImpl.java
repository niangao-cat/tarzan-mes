package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeOperationInstructionMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeOperationInstruction;
import com.ruike.hme.domain.repository.HmeOperationInstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 作业指导 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-10-20 16:07:50
 */
@Component
public class HmeOperationInstructionRepositoryImpl extends BaseRepositoryImpl<HmeOperationInstruction> implements HmeOperationInstructionRepository {

    @Autowired
    private HmeOperationInstructionMapper hmeOperationInsMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public List<HmeOperationInstruction> operationInsQuery(Long tenantId, String insHeaderId) {
        return hmeOperationInsMapper.operationInsQuery(tenantId, insHeaderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeOperationInstruction operationInsUpdate(Long tenantId, HmeOperationInstruction operationIns) {
        if (Objects.isNull(operationIns)) {
            throw new MtException("HME_OPERATION_INS_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OPERATION_INS_0001", "HME"));
        }
//        if (StringUtils.isEmpty(operationIns.getMaterialId()) && StringUtils.isEmpty(operationIns.getMaterialCategoryId())) {
//            throw new MtException("HME_OPERATION_INS_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_OPERATION_INS_0002", "HME"));
//        }
        if (StringUtils.isEmpty(operationIns.getOperationInsId())){
            self().insertSelective(operationIns);
        } else {
            hmeOperationInsMapper.updateByPrimaryKeySelective(operationIns);
        }
        return operationIns;
    }
}
