package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检报表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020/12/11 15:13:23
 */
public interface QmsPqcReportMapper {

    /**
     * 巡检报表-根据事业部查询头部数据
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/11 15:59:31
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportVO> pqcReportHeadDataQueryByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部+车间查询头部数据
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 10:36:24
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    List<QmsPqcReportVO> pqcReportHeadDataQueryByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-明细数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 13:29:37
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO2>
     */
    List<QmsPqcReportVO2> pgcReportDetailDataQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);


    /**
     * 巡检报表-根据事业部查询头部数据导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/11 15:59:31
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO3>
     */
    List<QmsPqcReportVO3> pqcReportHeadDataExportByDepartment(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-根据事业部+车间查询头部数据导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 10:36:24
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO4>
     */
    List<QmsPqcReportVO4> pqcReportHeadDataExportByWorkshop(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);

    /**
     * 巡检报表-明细数据导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 13:29:37
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO2>
     */
    List<QmsPqcReportVO5> pgcReportDetailDataExport(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);
}
