package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.RkTableSummaryInfoMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.RkTableSummaryInfo;
import com.ruike.hme.domain.repository.RkTableSummaryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 大表信息汇总统计数据 资源库实现
 *
 * @author yuchao.wang@hand-china.com 2020-10-03 14:47:56
 */
@Component
public class RkTableSummaryInfoRepositoryImpl extends BaseRepositoryImpl<RkTableSummaryInfo> implements RkTableSummaryInfoRepository {

    @Autowired
    private RkTableSummaryInfoMapper tableSummaryInfoMapper;

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
    @Override
    public Page<RkTableSummaryInfo> queryReport(Long tenantId, PageRequest pageRequest, RkTableSummaryInfo rkTableSummaryInfo) {
        return PageHelper.doPageAndSort(pageRequest, () -> tableSummaryInfoMapper.queryReport(tenantId, rkTableSummaryInfo));
    }
}
