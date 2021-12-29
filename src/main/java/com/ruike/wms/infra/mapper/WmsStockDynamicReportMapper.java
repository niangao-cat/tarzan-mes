package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsStockDynamicReportDTO;
import com.ruike.wms.domain.vo.WmsStockDynamicReportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/28 9:28
 */
public interface WmsStockDynamicReportMapper {

    List<WmsStockDynamicReportVO> queryListIn(@Param("tenantId") Long tenantId,
                                              @Param("dto") WmsStockDynamicReportDTO dto);

    List<WmsStockDynamicReportVO> queryListOut(@Param("tenantId") Long tenantId,
                                               @Param("dto") WmsStockDynamicReportDTO dto);
}
