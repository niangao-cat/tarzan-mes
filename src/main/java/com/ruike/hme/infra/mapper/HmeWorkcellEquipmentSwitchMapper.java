package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeOpEqRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * HmeWorkcellEquipmentSwitchMapper
 *
 * @author chaonan.hu@hand-china.com 2020/09/01 10:24:23
 */
public interface HmeWorkcellEquipmentSwitchMapper {

    /**
     * 根据工艺查询绑定的设备类
     *
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 10:28:20
     * @return java.util.List<com.ruike.hme.domain.entity.HmeOpEqRel>
     */
    List<HmeOpEqRel> equipmentCategoryQuery(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);
}
