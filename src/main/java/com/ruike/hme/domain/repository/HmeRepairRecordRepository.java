package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeRepairPermitJudgeDTO;
import com.ruike.hme.domain.entity.HmeRepairRecord;
import com.ruike.hme.domain.vo.HmeRepairPermitJudgeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/14 8:41
 */
public interface HmeRepairRecordRepository extends BaseRepository<HmeRepairRecord>, AopProxy<HmeRepairRecordRepository> {
    /**
     * 查询 SN 在 工序、物料 下的返修次数记录
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return
     */
    Page<HmeRepairPermitJudgeVO> queryRepairRecordList(Long tenantId, PageRequest pageRequest, HmeRepairPermitJudgeDTO dto);

    /**
     * 继续返修
     * @param tenantId
     * @param dto
     * @return
     */
    HmeRepairRecord continueRepair(Long tenantId, HmeRepairRecord dto);

    /**
     * 停止返修
     * @param tenantId
     * @param dto
     * @return
     */
    HmeRepairRecord stopRepair(Long tenantId, HmeRepairRecord dto);
}
