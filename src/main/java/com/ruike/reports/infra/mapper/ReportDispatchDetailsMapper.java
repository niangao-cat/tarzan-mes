package com.ruike.reports.infra.mapper;


import com.ruike.reports.api.dto.ReportDispatchDetailsDTO;
import com.ruike.reports.domain.vo.ReportDispatchDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ReportDispatchDetailsMapper
 * @Description 派工明细报表
 * @Author lkj
 * @Date 2020/12/15
 */
public interface ReportDispatchDetailsMapper {

    List<ReportDispatchDetailsVO> selectDispatchDetails(@Param("tenantId") String tenantId,
                                                        @Param("dto") ReportDispatchDetailsDTO dto);
}
