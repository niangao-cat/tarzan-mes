package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeExcGroupAssign;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 异常项分配异常收集组关系表Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
public interface HmeExcGroupAssignMapper extends BaseMapper<HmeExcGroupAssign> {
    /**
     * 根据异常组ID查询
     *
     * @param tenantId         租户ID
     * @param exceptionGroupId 异常组ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeExcGroupAssign>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:38:33
     */
    List<HmeExcGroupAssign> selectByExceptionGroupId(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "exceptionGroupId") String exceptionGroupId);
}
