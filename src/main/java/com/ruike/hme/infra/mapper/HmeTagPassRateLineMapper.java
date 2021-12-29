package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeTagPassRateLine;
import com.ruike.hme.domain.vo.HmeTagPassRateLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 偏振度和发散角良率维护行表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:37
 */
public interface HmeTagPassRateLineMapper extends BaseMapper<HmeTagPassRateLine> {
    /**
     * 偏振度和发散角良率维护行表查询
     *
     * @param tenantId 租户id
     * @param heardId  头id
     * @return
     */
    List<HmeTagPassRateLineVO> queryTagPassRateLine(@Param("tenantId") Long tenantId, @Param("heardId") String heardId);

}
