package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeRepairLimitCountHis;
import com.ruike.hme.domain.vo.HmeRepairLimitCountHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 返修进站限制次数历史表表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-13 16:41:22
 */
public interface HmeRepairLimitCountHisRepository extends BaseRepository<HmeRepairLimitCountHis> {

    /**
     * 查询返修进站限制次数历史表
     * @param tenantId
     * @param pageRequest
     * @param repairLimitCountId
     * @return
     */
    Page<HmeRepairLimitCountHisVO> list(Long tenantId, PageRequest pageRequest, String repairLimitCountId);
}
