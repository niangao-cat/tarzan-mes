package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsPqcReportDTO;
import com.ruike.qms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检报表应用服务
 *
 * @author: chaonan.hu@hand-china.com 2020/12/11 15:13:23
 **/
public interface QmsPqcReportService {

    /**
     * 巡检报表-头部数据分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/11 15:13:21
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsPqcReportVO>
     */
    Page<QmsPqcReportVO> pqcReportHeadDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest);

    /**
     * 巡检报表-明细数据分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 01:34:22
     * @return io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsPqcReportVO2>
     */
    Page<QmsPqcReportVO2> pgcReportDetailDataQuery(Long tenantId, QmsPqcReportDTO dto, PageRequest pageRequest);
    
    /**
     * 按照车间维度导出头部数据
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 01:59:28 
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO3>
     */
    List<QmsPqcReportVO3> pqcReportHeadDataExportByDepartment(Long tenantId, QmsPqcReportDTO dto);

    /**
     * 按照工序维度导出头部数据
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 01:59:28
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO3>
     */
    List<QmsPqcReportVO4> pqcReportHeadDataExportByWorkshop(Long tenantId, QmsPqcReportDTO dto);

    /**
     * 明细数据导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/12 13:29:37
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcReportVO5>
     */
    List<QmsPqcReportVO5> pgcReportDetailDataExport(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcReportDTO dto);
}
