package tarzan.actual.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtInstructionActualHis;
import tarzan.actual.domain.repository.MtInstructionActualHisRepository;
import tarzan.actual.domain.vo.MtInstructionActualHisVO;
import tarzan.actual.infra.mapper.MtInstructionActualHisMapper;

/**
 * 指令实绩汇总历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtInstructionActualHisRepositoryImpl extends BaseRepositoryImpl<MtInstructionActualHis>
                implements MtInstructionActualHisRepository {

    @Autowired
    private MtInstructionActualHisMapper mtInstructionActualHisMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public MtInstructionActualHisVO instructionActualLatestHisGet(Long tenantId, String actualId) {
        if (StringUtils.isEmpty(actualId)) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "actualId", "【API:instructionActualLatestHisGet】"));
        }
        return mtInstructionActualHisMapper.selectRecent(tenantId, actualId);
    }
}
