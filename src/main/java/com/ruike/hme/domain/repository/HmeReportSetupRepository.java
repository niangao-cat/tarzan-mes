package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeReportSetupVO;
import com.ruike.hme.domain.vo.HmeReportSetupVO2;
import com.ruike.hme.domain.vo.HmeReportSetupVO3;
import com.ruike.hme.domain.vo.HmeReportSetupVO4;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeReportSetup;

import java.util.List;

/**
 * 看板配置基础数据表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-10-22 09:43:00
 */
public interface HmeReportSetupRepository extends BaseRepository<HmeReportSetup>, AopProxy<HmeReportSetupRepository> {


    /**
     * 看板类型列表
     *
     * @param tenantId          租户id
     * @author sanfeng.zhang@hand-china.com 2020/10/22 11:29
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReportSetupVO>
     */
    List<HmeReportSetupVO> reportTypeList(Long tenantId);

    /**
     * 看板基础数据列表
     *
     * @param tenantId          租户id
     * @param reportType        看板类型
     * @param pageRequest       分页参数
     * @author sanfeng.zhang@hand-china.com 2020/10/22 14:34
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeReportSetupVO2>
     */
    Page<HmeReportSetupVO2> queryReportSetupsList(Long tenantId, String reportType, PageRequest pageRequest);

    /**
     * 新增&编辑基础数据
     *
     * @param tenantId          租户id
     * @param setupVO2          参数
     * @author sanfeng.zhang@hand-china.com 2020/10/22 15:38
     * @return com.ruike.hme.domain.vo.HmeReportSetupVO2
     */
    HmeReportSetupVO2 saveReportSetups(Long tenantId, HmeReportSetupVO2 setupVO2);

    /**
     * 删除基础数据
     *
     * @param tenantId          租户id
     * @param setupVO2          参数
     * @author sanfeng.zhang@hand-china.com 2020/10/22 15:50
     * @return com.ruike.hme.domain.vo.HmeReportSetupVO2
     */
    HmeReportSetupVO2 deleteReportSetups(Long tenantId, HmeReportSetupVO2 setupVO2);

    /**
     * 站点中文名
     *
     * @param tenantId
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2020/10/23 14:58
     * @return com.ruike.hme.domain.vo.HmeReportSetupVO3
     */
    HmeReportSetupVO3 querySiteName(Long tenantId, String siteId);

    /**
     * 产量可视化监控系统
     * 
     * @param tenantId          租户id
     * @param siteId            站点
     * @author sanfeng.zhang@hand-china.com 2020/10/22 16:48 
     * @return Page<com.ruike.hme.domain.vo.HmeReportSetupVO4>
     */
    Page<HmeReportSetupVO4> queryProdVisionMonitorSystem(Long tenantId, String siteId, PageRequest pageRequest);
}
