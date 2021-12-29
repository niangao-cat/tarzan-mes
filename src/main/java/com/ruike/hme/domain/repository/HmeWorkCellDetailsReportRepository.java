package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtModLocator;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工位产量明细报表
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:45
 */
public interface HmeWorkCellDetailsReportRepository {

    /**
     * 工位产量明细报表查询
     *
     * @param tenantId    租户di
     * @param reportVO    报表参数
     * @param pageRequest 分页参数
     * @return : 报表数据
     * @author sanfeng.zhang
     * @date 2020/7/8 16:11
     **/
    Page<HmeWorkCellDetailsReportVO2> queryWorkCellReportList(Long tenantId, HmeWorkCellDetailsReportVO reportVO, PageRequest pageRequest);

    /**
     * 根据organizationId获取生产线描述
     *
     * @param organizationId 组织ID
     * @param tenantId       租户ID
     * @return : java.lang.String
     * @author sanfeng.zhang
     * @date 2020/7/8 18:28
     **/
    String queryProductionLineName(Long tenantId, String organizationId);


    /**
     * 根据organizationId获取工段描述
     *
     * @param organizationId 组织ID
     * @param tenantId       租户ID
     * @param workcellType   类型
     * @return : java.lang.String
     * @author sanfeng.zhang
     * @date 2020/7/8 18:29
     **/
    String queryLineWorkcellName(Long tenantId, String organizationId, String workcellType);


    /**
     * 工段LOV
     *
     * @param tenantId      租户ID
     * @param hmeWorkCellVO 工段查询参数
     * @param pageRequest   分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWorkCellVO>
     * @author sanfeng.zhang
     * @date 2020/7/10 10:13
     **/
    Page<HmeWorkCellVO> workCellUiQuery(Long tenantId, HmeWorkCellVO hmeWorkCellVO, PageRequest pageRequest);

    /**
     * 工序采集项报表查询
     *
     * @param tenantId    租户ID
     * @param reportVO    查询参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProcessReportVo2>
     * @author sanfeng.zhang
     * @date 2020/7/13 15:14
     **/
    Page<HmeProcessReportVo2> queryProcessReportList(Long tenantId, HmeProcessReportVo reportVO, PageRequest pageRequest);

    /**
     * 工序采集项报表导出
     *
     * @param tenantId
     * @param reportVO
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/11/6 10:58
     */
    void queryProcessReportExport(Long tenantId, HmeProcessReportVo reportVO, HttpServletResponse response);

    /**
     * 查询采集项job详情列表
     *
     * @param tenantId    租户
     * @param jobId       job
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProcessJobDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 02:03:14
     */
    Page<HmeProcessJobDetailVO> pagedJobDetail(Long tenantId,
                                               String jobId,
                                               PageRequest pageRequest);

    /**
     * 异常信息查看报表
     *
     * @param tenantId    租户ID
     * @param reportVO    查询参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     * @author sanfeng.zhang
     * @date 020/7/14 15:37
     **/
    Page<HmeExceptionReportVO2> queryExceptionReportList(Long tenantId, HmeExceptionReportVO reportVO, PageRequest pageRequest);

    /**
     * 获取父层库位id
     *
     * @param tenantId  租户id
     * @param locatorId 仓位id
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author sanfeng.zhang@hand-china.com 2020/8/14 10:41
     */
    List<MtModLocator> queryParentLocatorId(Long tenantId, String locatorId);
}
