package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.RkTableSummaryInfo;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 大表信息汇总统计数据Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-10-03 14:47:56
 */
public interface RkTableSummaryInfoMapper extends BaseMapper<RkTableSummaryInfo> {

    /**
     *
     * @Description 报表查询
     *
     * @author yuchao.wang
     * @date 2020/10/3 15:10
     * @param tenantId 租户ID
     * @param rkTableSummaryInfo 参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.RkTableSummaryInfo>
     *
     */
    List queryReport(@Param("tenantId") Long tenantId, @Param("dto") RkTableSummaryInfo rkTableSummaryInfo);
}
