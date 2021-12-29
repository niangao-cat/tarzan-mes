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
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.entity.MtInstructionDocHis;
import tarzan.instruction.domain.repository.MtInstructionDocHisRepository;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO;
import tarzan.instruction.domain.vo.MtInstructionDocHIsVO1;
import tarzan.instruction.domain.vo.MtInstructionDocHisVO2;
import tarzan.instruction.infra.mapper.MtInstructionDocHisMapper;

/**
 * 指令单据头历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@Component
public class MtInstructionDocHisRepositoryImpl extends BaseRepositoryImpl<MtInstructionDocHis>
                implements MtInstructionDocHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtInstructionDocHisMapper mtInstructionDocHisMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveLogisticInstructionDocHistory(Long tenantId, MtInstructionDoc mtInstructionDoc, String eventId) {

        MtInstructionDocHis mtLogisticInstructionDocHis = new MtInstructionDocHis();
        BeanUtils.copyProperties(mtInstructionDoc, mtLogisticInstructionDocHis);

        mtLogisticInstructionDocHis.setEventId(eventId);
        mtLogisticInstructionDocHis.setTenantId(tenantId);

        self().insertSelective(mtLogisticInstructionDocHis);
        return mtLogisticInstructionDocHis.getInstructionDocHisId();
    }

    @Override
    public List<MtInstructionDocHisVO> eventLimitInstructionDocHisQuery(Long tenantId, String eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "eventId", "【API:eventLimitInstructionDocHisQuery】"));
        }

        return mtInstructionDocHisMapper.selectByEventIdList(tenantId, Collections.singletonList(eventId));
    }

    @Override
    public List<MtInstructionDocHisVO> eventLimitInstructionDocHisBatchQuery(Long tenantId, List<String> eventIdList) {
        if (CollectionUtils.isEmpty(eventIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "eventIdList",
                                            "【API:eventLimitInstructionDocHisBatchQuery】"));
        }

        return mtInstructionDocHisMapper.selectByEventIdList(tenantId, eventIdList);
    }

    @Override
    public List<MtInstructionDocHisVO> instructionDocHisPropertyQuery(Long tenantId,
                    MtInstructionDocHis mtLogisticInstructionDocHis) {
        boolean emptyCheck = StringUtils.isEmpty(mtLogisticInstructionDocHis.getInstructionDocId())
                        && StringUtils.isEmpty(mtLogisticInstructionDocHis.getInstructionDocHisId());
        if (emptyCheck) {
            throw new MtException("MT_INSTRUCTION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0003",
                                            "INSTRUCTION", "instructionDocId", "instructionDocHisId",
                                            "【API:instructionDocHisPropertyQuery】"));
        }

        MtInstructionDocHis queryDoc = new MtInstructionDocHis();
        queryDoc.setInstructionDocId(StringUtils.isNotEmpty(mtLogisticInstructionDocHis.getInstructionDocId())
                        ? mtLogisticInstructionDocHis.getInstructionDocId()
                        : null);
        queryDoc.setInstructionDocHisId(StringUtils.isNotEmpty(mtLogisticInstructionDocHis.getInstructionDocHisId())
                        ? mtLogisticInstructionDocHis.getInstructionDocHisId()
                        : null);
        queryDoc.setEventId(StringUtils.isNotEmpty(mtLogisticInstructionDocHis.getEventId())
                        ? mtLogisticInstructionDocHis.getEventId()
                        : null);

        return mtInstructionDocHisMapper.selectByProperty(tenantId, queryDoc);
    }

    @Override
    public List<MtInstructionDocHisVO> instructionDocLimitHisQuery(Long tenantId,
                    MtInstructionDocHis mtLogisticInstructionDocHis) {
        if (StringUtils.isEmpty(mtLogisticInstructionDocHis.getInstructionDocId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId", "【API:instructionDocLimitHisQuery】"));
        }

        MtInstructionDocHis queryDocHis = new MtInstructionDocHis();
        queryDocHis.setInstructionDocId(mtLogisticInstructionDocHis.getInstructionDocId());

        return instructionDocHisPropertyQuery(tenantId, queryDocHis);
    }

    @Override
    public List<MtInstructionDocHis> propertyLimitInstructionDocHisQuery(Long tenantId,
                    MtInstructionDocHIsVO1 mtInstructionDocHIsVO1) {
        mtInstructionDocHIsVO1.setTenantId(tenantId);
        return mtInstructionDocHisMapper.selectPropertyLimit(tenantId, mtInstructionDocHIsVO1);
    }

    @Override
    public MtInstructionDocHisVO2 instructionDocLatestHisGet(Long tenantId, String instructionDocId) {
        if (StringUtils.isEmpty(instructionDocId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionDocId", "【API:instructionDocLatestHisGet】"));
        }
        return mtInstructionDocHisMapper.selectRecent(tenantId, instructionDocId);
    }

}
