package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeExcGroupRouter;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 异常收集组异常反馈路线表Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
public interface HmeExcGroupRouterMapper extends BaseMapper<HmeExcGroupRouter> {
    /**
     * 通过分配ID查询路线
     *
     * @param tenantId 租户ID
     * @param assignId 分配ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeExcGroupRouter>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:39:14
     */
    List<HmeExcGroupRouter> selectRouterByAssignId(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "assignId") String assignId);
}
