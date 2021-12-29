package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeReportSetup;
import com.ruike.hme.domain.vo.HmeReportSetupVO2;
import com.ruike.hme.domain.vo.HmeReportSetupVO4;
import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 看板配置基础数据表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-10-22 09:43:00
 */
public interface HmeReportSetupMapper extends BaseMapper<HmeReportSetup> {

    /**
     * 看板基础数据列表
     *
     * @param tenantId          租户id
     * @param reportType        看板类型
     * @author sanfeng.zhang@hand-china.com 2020/10/22 15:09
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReportSetupVO2>
     */
    List<HmeReportSetupVO2> queryReportSetupsList(@Param("tenantId") Long tenantId, @Param("reportType") String reportType);

    /**
     * 查询站点名称(多语言)
     *
     * @param tenantId              租户id
     * @param siteId                站点
     * @author sanfeng.zhang@hand-china.com 2020/10/22 17:30
     * @return java.util.List<io.tarzan.common.domain.vo.MtExtendVO5>
     */
    List<MtExtendVO5> querySiteName(@Param("tenantId") Long tenantId, @Param("siteId") String siteId);

    /**
     * 获取站点下看板基础数据
     * 
     * @param tenantId              租户id
     * @param siteId                站点
     * @author sanfeng.zhang@hand-china.com 2020/10/22 17:44 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReportSetupVO4>
     */
    List<HmeReportSetupVO4> queryReportSetupsListOfSite(@Param("tenantId") Long tenantId, @Param("siteId") String siteId);

    /**
     * 批量查询本月累计达成数量
     *
     * @param tenantId              租户id
     * @param siteId                站点
     * @param prodLineIdList        产线
     * @param startTime             开始时间
     * @param endTime               结束时间
     * @author sanfeng.zhang@hand-china.com 2020/10/22 22:06
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReportSetupVO4>
     */
    List<HmeReportSetupVO4> queryBatchCompletedQty(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("prodLineIdList") List<String> prodLineIdList, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 批量查询本月累计派工数量
     *
     * @param tenantId
     * @param prodLineIdList
     * @param workcellIdList
     * @param startTime
     * @param endTime
     * @author sanfeng.zhang@hand-china.com 2020/10/22 22:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReportSetupVO4>
     */
    List<HmeReportSetupVO4> queryBatchDispatchQty(@Param("tenantId") Long tenantId, @Param("prodLineIdList") List<String> prodLineIdList, @Param("workcellIdList") List<String> workcellIdList, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 批量查询今日派工
     *
     * @param tenantId
     * @param prodLineIdList
     * @param siteId
     * @param startTime
     * @param endTime
     * @author sanfeng.zhang@hand-china.com 2020/10/22 22:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReportSetupVO4>
     */
    List<HmeReportSetupVO4> queryBatchDailyCompletedQty(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("prodLineIdList") List<String> prodLineIdList, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
