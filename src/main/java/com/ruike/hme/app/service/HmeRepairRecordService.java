package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeRepairPermitJudgeDTO;
import com.ruike.hme.domain.entity.HmeRepairRecord;
import com.ruike.hme.domain.vo.HmeRepairPermitJudgeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/14 8:39
 */
public interface HmeRepairRecordService {
    Page<HmeRepairPermitJudgeVO> queryList(Long tenantId, PageRequest pageRequest, HmeRepairPermitJudgeDTO dto);

    HmeRepairRecord continueRepair(Long tenantId, HmeRepairRecord dto);

    HmeRepairRecord stopRepair(Long tenantId, HmeRepairRecord dto);
}
