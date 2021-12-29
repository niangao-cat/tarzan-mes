package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeRepairLimitCountHis;
import com.ruike.hme.domain.vo.HmeRepairLimitCountHisVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 返修进站限制次数历史表表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-13 16:41:22
 */
public interface HmeRepairLimitCountHisMapper extends BaseMapper<HmeRepairLimitCountHis> {

    /**
     * 查询返修进站限制次数历史表
     * @param tenantId
     * @param repairLimitCountId
     * @return
     */
    List<HmeRepairLimitCountHisVO> list(@Param("tenantId") Long tenantId, @Param("repairLimitCountId") String repairLimitCountId);
}
