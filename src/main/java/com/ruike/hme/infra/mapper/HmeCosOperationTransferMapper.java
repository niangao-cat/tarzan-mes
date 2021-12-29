package com.ruike.hme.infra.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 来料转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/12/28 17:43
 */
public interface HmeCosOperationTransferMapper {

    /**
     * 工艺找工艺步骤
     *
     * @param tenantId
     * @param routerId
     * @param operationId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/29 16:58
     */
    List<String> queryRouterStepIdByRouteAndOperation(@Param("tenantId") Long tenantId, @Param("routerId") String routerId, @Param("operationId") String operationId);

}
