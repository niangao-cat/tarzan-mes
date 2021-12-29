package com.ruike.hme.infra.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/26 10:38
 */
public interface HmeRepairSnBindMapper {

    /**
     * 查运行的新EO
     * @param tenantId
     * @param reworkMaterialLot
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/11/25
     */
    List<String> queryWorkingEoByOldMaterialLotCode(@Param("tenantId") Long tenantId, @Param("reworkMaterialLot") String reworkMaterialLot);
}
