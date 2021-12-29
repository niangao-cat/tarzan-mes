package com.ruike.reports.infra.mapper;

import java.util.List;

import com.ruike.reports.api.dto.HmeCosInProductionDTO;
import com.ruike.reports.domain.vo.HmeCosInProductionVO;
import org.apache.ibatis.annotations.Param;

/**
 * COS在制报表 mapper
 *
 * @author 35113 2021/01/27 12:53
 */
public interface HmeCosInProductionMapper {

    List<HmeCosInProductionVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeCosInProductionDTO dto);
}
