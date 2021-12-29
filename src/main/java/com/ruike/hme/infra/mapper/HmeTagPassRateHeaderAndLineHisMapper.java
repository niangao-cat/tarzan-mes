package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeTagPassRateHeaderAndLineHisVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 偏振度和发散角良率维护头历史表查询
 *
 * @author wengang.qiang@hand-china.com 2021/09/14 11:55
 */
public interface HmeTagPassRateHeaderAndLineHisMapper {
    /**
     * 偏振度和发散角良率维护头历史表查询
     *
     * @param tenantId 租户id
     * @param heardId  头id
     * @return
     */
    List<HmeTagPassRateHeaderAndLineHisVO> queryTagPassRateHeaderAndLineHis(@Param("tenantId") Long tenantId, @Param("heardId") String heardId);

}
