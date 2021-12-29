package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeRepairPermitJudgeDTO;
import com.ruike.hme.domain.entity.HmeRepairRecord;
import com.ruike.hme.domain.repository.HmeRepairRecordRepository;
import com.ruike.hme.domain.vo.HmeRepairPermitJudgeVO;

import com.ruike.hme.infra.mapper.HmeRepairRecordMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


/**
 * @author yaqiong.zhou@raycus.com 2021/9/14 8:41
 */
@Component
public class HmeRepairRecordRepositoryImpl extends BaseRepositoryImpl<HmeRepairRecord> implements HmeRepairRecordRepository {

    @Autowired
    private HmeRepairRecordMapper repairMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @ProcessLovValue
    public Page<HmeRepairPermitJudgeVO> queryRepairRecordList(Long tenantId, PageRequest pageRequest, HmeRepairPermitJudgeDTO dto) {
        return PageHelper.doPage(pageRequest, ()->repairMapper.queryRepairRecordList(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeRepairRecord continueRepair(Long tenantId, HmeRepairRecord dto) {
        if (Objects.isNull(dto.getPermitCount())) {
            throw new MtException("HME_REPAIR_LIMIT_COUNT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_LIMIT_COUNT_004", "HME"));
        }
        if (dto.getPermitCount() <= 0L){
            throw new MtException("HME_REPAIR_LIMIT_COUNT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_LIMIT_COUNT_005", "HME"));
        }
        // 返修总次数 大于 准许返修总次数
        if (Objects.nonNull(dto.getPermitRepairCount()) && dto.getRepairCount() > dto.getPermitRepairCount()) {
            dto.setPermitRepairCount(dto.getRepairCount());
        }
        if (Objects.isNull(dto.getPermitRepairCount())) {
            dto.setPermitRepairCount(dto.getPermitCount());
        } else {
            Long permitRecordCount = dto.getPermitCount() + dto.getPermitRepairCount();
            dto.setPermitRepairCount(permitRecordCount);
        }
        //放行次数
        if (Objects.isNull(dto.getPassCount())) {
            dto.setPassCount(1L);
        }else {
            Long passCount = dto.getPassCount() +1;
            dto.setPassCount(passCount);
        }
        dto.setStatus("UNDEFINED");
        repairMapper.updateByPrimaryKeySelective(dto);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeRepairRecord stopRepair(Long tenantId, HmeRepairRecord dto) {
        //设置返修状态为 停止返修
        dto.setStatus("STOP");
        dto.setPermitCount(0L);
        repairMapper.updateByPrimaryKeySelective(dto);
        return dto;
    }
}
