package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProcessGatherResultReportDto;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * HmeReportMapper
 *
 * @author: chaonan.hu@hand-china.com 2021-03-22 10:06:45
 **/
public interface HmeReportMapper {

    /**
     * 工序采集结果报表分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/22 10:35:02
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO>
     */
    List<HmeProcessGatherResultReportVO> processGatherResultReportQuery(@Param(value = "tenantId") Long tenantId,
                                                                        @Param(value = "dto") HmeProcessGatherResultReportDto dto);

    /**
     * 工序采集结果报表导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/22 01:54:02
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO2>
     */
    List<HmeProcessGatherResultReportVO2> processGatherResultReportExport(@Param(value = "tenantId") Long tenantId,
                                                                         @Param(value = "dto") HmeProcessGatherResultReportDto dto);
}
