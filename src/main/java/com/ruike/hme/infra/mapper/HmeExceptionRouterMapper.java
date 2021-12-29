package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeExceptionRouter;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 异常反馈路线基础数据表Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
public interface HmeExceptionRouterMapper extends BaseMapper<HmeExceptionRouter> {

    List<HmeExceptionRouter> selectRouterByExceptionId(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "exceptionId") String exceptionId);
}
