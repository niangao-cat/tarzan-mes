package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.vo.HmeMtEoRouterActualVO2;
import com.ruike.hme.domain.vo.HmeMtEoRouterActualVO5;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtEoRouterActual;

public interface HmeMtEoRouterActualMapper{
    /**
     * 查询实绩
     *
     * @param tenantId 租户Id
     * @param dto 查询条件
     * @return List<MtEoRouterActualVO5>
     */
    List<HmeMtEoRouterActualVO5> eoBatchOperationLimitCurrentRouterStepGet(@Param(value = "tenantId") Long tenantId,
                                                                           @Param(value = "dto") HmeMtEoRouterActualVO2 dto);
}
