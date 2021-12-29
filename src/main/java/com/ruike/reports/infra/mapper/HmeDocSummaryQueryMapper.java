package com.ruike.reports.infra.mapper;

import com.ruike.reports.api.dto.HmeDocSummaryQueryDTO;
import com.ruike.reports.domain.vo.HmeDocSummaryQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeDocSummaryQueryMapper {

    List<HmeDocSummaryQueryVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeDocSummaryQueryDTO dto);
}
