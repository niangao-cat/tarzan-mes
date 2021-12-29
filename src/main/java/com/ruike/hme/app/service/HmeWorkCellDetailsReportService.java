package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 工位产量明细报表
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:42
 */
public interface HmeWorkCellDetailsReportService {

    /**
     * 工位产量明细报表
     *
     * @param tenantId    租户ID
     * @param reportVO    查询参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO2>
     * @author sanfeng.zhang 2020/7/8 16:01
     */
    Page<HmeWorkCellDetailsReportVO2> listForUi(Long tenantId, HmeWorkCellDetailsReportVO reportVO, PageRequest pageRequest);

    /**
     * 工段LOV
     *
     * @param tenantId      租户ID
     * @param hmeWorkCellVO 查询参数
     * @param pageRequest   分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWorkCellVO>
     * @author sanfeng.zhang 2020/7/10 10:11
     */
    Page<HmeWorkCellVO> workCellUiQuery(Long tenantId, HmeWorkCellVO hmeWorkCellVO, PageRequest pageRequest);

    /**
     * 工序采集项报表
     *
     * @param tenantId    租户的ID
     * @param reportVO    查询数据
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProcessReportVo2>
     * @author sanfeng.zhang 2020/7/13 15:03
     */
    Page<HmeProcessReportVo2> queryProcessReportList(Long tenantId, HmeProcessReportVo reportVO, PageRequest pageRequest);

    /**
     * 工序采集项报表导出
     *
     * @param tenantId
     * @param reportVO
     * @param response
     * @author sanfeng.zhang@hand-china.com 2020/11/6 10:57
     * @return void
     */
    void queryProcessReportExport(Long tenantId, HmeProcessReportVo reportVO, HttpServletResponse response);

    /**
     * 异常信息查看报表
     *
     * @param tenantId    租户的ID
     * @param reportVO    查询数据
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     * @author sanfeng.zhang 2020/7/14 15:03
     */
    Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest);
}
