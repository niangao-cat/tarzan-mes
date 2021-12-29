package com.ruike.reports.infra.mapper;

import java.util.List;

import com.ruike.reports.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.reports.domain.vo.HmeMaterielBadDetailedVO;
import org.apache.ibatis.annotations.Param;

/**
 * 材料不良明细报表 mapper
 *
 * @author wenqiang.yin@hand-china.com 2021/02/02 12:41
 */
public interface HmeMaterielBadDetailedMapper {
    List<HmeMaterielBadDetailedVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                         @Param("dto") HmeMaterielBadDetailedDTO dto);
}
