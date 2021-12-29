package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosTestPassRateHis;
import com.ruike.hme.domain.vo.HmeCosTestPassRateHisVO;
import feign.Param;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * COS测试良率维护历史表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:40
 */
public interface HmeCosTestPassRateHisMapper extends BaseMapper<HmeCosTestPassRateHis> {
    /**
     * COS测试良率维护历史表基础查询
     *
     * @param tenantId 租户id
     * @param testId   主表id
     * @return
     */
    List<HmeCosTestPassRateHisVO> queryHmeCosTestPassRateHis(@Param("tenantId") Long tenantId, @Param("testId") String testId);
}
