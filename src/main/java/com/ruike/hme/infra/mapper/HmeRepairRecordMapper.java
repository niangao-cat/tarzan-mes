package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeRepairPermitJudgeDTO;
import com.ruike.hme.domain.entity.HmeRepairRecord;
import com.ruike.hme.domain.vo.HmeRepairPermitJudgeVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/14 8:42
 */
public interface HmeRepairRecordMapper extends BaseMapper<HmeRepairRecord> {
    /**
     * 查询 SN 在 工序、物料 下的返修次数
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeRepairPermitJudgeVO> queryRepairRecordList(@Param("tenantId") Long tenantId, @Param("dto") HmeRepairPermitJudgeDTO dto);
}
