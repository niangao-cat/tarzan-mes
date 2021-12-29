package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeRepairPermitJudgeDTO;
import com.ruike.hme.app.service.HmeRepairRecordService;
import com.ruike.hme.domain.entity.HmeRepairRecord;
import com.ruike.hme.domain.repository.HmeRepairRecordRepository;
import com.ruike.hme.domain.vo.HmeRepairPermitJudgeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/14 8:40
 */
@Service
public class HmeRepairRecordServiceImpl implements HmeRepairRecordService {

    @Autowired
    private HmeRepairRecordRepository repairRepository;

    @Override
    public Page<HmeRepairPermitJudgeVO> queryList(Long tenantId, PageRequest pageRequest, HmeRepairPermitJudgeDTO dto) {
        return repairRepository.queryRepairRecordList(tenantId, pageRequest, dto);
    }

    @Override
    public HmeRepairRecord continueRepair(Long tenantId, HmeRepairRecord dto) {
        return repairRepository.continueRepair(tenantId, dto);
    }

    @Override
    public HmeRepairRecord stopRepair(Long tenantId, HmeRepairRecord dto) {
        return repairRepository.stopRepair(tenantId, dto);
    }
}
