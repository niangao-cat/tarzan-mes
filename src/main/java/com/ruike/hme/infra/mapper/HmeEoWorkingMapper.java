package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeEoWorkingDTO;
import com.ruike.hme.domain.vo.HmeEoWorkingVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序在制查询Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-01-213 11:35:12
 */
public interface HmeEoWorkingMapper {

    /**
     * 工序在制查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/13 11:39:36
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoWorkingVO2>
     */
    List<HmeEoWorkingVO2> queryForEoWorkingNew(@Param("tenantId") Long tenantId, @Param("dto") HmeEoWorkingDTO dto);
}
