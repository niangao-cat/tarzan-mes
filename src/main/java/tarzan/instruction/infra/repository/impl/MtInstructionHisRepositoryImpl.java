package tarzan.instruction.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionHis;
import tarzan.instruction.domain.repository.MtInstructionHisRepository;
import tarzan.instruction.domain.vo.MtInstructionHisVO;
import tarzan.instruction.domain.vo.MtInstructionHisVO1;
import tarzan.instruction.domain.vo.MtInstructionHisVO2;
import tarzan.instruction.infra.mapper.MtInstructionHisMapper;

/**
 * 仓储物流指令内容历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@Component
public class MtInstructionHisRepositoryImpl extends BaseRepositoryImpl<MtInstructionHis>
                implements MtInstructionHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtInstructionHisMapper mtLogisticInstructionHisMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveLogisticInstructionHistory(Long tenantId, MtInstruction mtInstruction, String eventId) {

        MtInstructionHis mtInstructionHis = new MtInstructionHis();
        BeanUtils.copyProperties(mtInstruction, mtInstructionHis);

        mtInstructionHis.setEventId(eventId);
        mtInstructionHis.setTenantId(tenantId);
        self().insertSelective(mtInstructionHis);
        return mtInstructionHis.getInstructionHisId();
    }

    @Override
    public List<MtInstructionHisVO> eventLimitInstructionHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "eventId", "【API：eventLimitInstructionHisQuery】"));
        }
        List<String> eventIdList = Collections.singletonList(eventId);
        return mtLogisticInstructionHisMapper.selectByEventIdList(tenantId, eventIdList);
    }

    @Override
    public List<MtInstructionHisVO> eventLimitInstructionHisBatchQuery(Long tenantId, List<String> eventIdList) {
        if (CollectionUtils.isEmpty(eventIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "eventIdList", "【API：eventLimitInstructionHisBatchQuery】"));
        }

        return mtLogisticInstructionHisMapper.selectByEventIdList(tenantId, eventIdList);
    }

    @Override
    public List<MtInstructionHisVO> instructionLimitHisQuery(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API：instructionLimitHisQuery】"));
        }

        return mtLogisticInstructionHisMapper.selectByInstructionId(tenantId, instructionId);
    }

    @Override
    public List<MtInstructionHis> propertyLimitInstructionHisQuery(Long tenantId,
                    MtInstructionHisVO1 mtInstructionHis) {
        return mtLogisticInstructionHisMapper.selectPropertyLimit(tenantId, mtInstructionHis);
    }

    @Override
    public MtInstructionHisVO2 instructionLatestHisGet(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "instructionId", "【API：instructionLatestHisGet】"));
        }
        return mtLogisticInstructionHisMapper.selectRecent(tenantId, instructionId);
    }

}
