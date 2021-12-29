package com.ruike.hme.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.RkTableSummaryInfo;

/**
 * 大表信息汇总统计数据资源库
 *
 * @author yuchao.wang@hand-china.com 2020-10-03 14:47:56
 */
public interface RkTableSummaryInfoRepository extends BaseRepository<RkTableSummaryInfo> {

    /**
     *
     * @Description 报表查询
     *
     * @author yuchao.wang
     * @date 2020/10/3 15:10
     * @param tenantId 租户ID
     * @param pageRequest 分页参数
     * @param rkTableSummaryInfo 参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.RkTableSummaryInfo>
     *
     */
    Page<RkTableSummaryInfo> queryReport(Long tenantId, PageRequest pageRequest, RkTableSummaryInfo rkTableSummaryInfo);
}
