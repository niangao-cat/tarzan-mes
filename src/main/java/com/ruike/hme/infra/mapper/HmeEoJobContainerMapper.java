package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEoJobContainer;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 工序作业平台-容器Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-03-23 12:48:53
 */
public interface HmeEoJobContainerMapper extends BaseMapper<HmeEoJobContainer> {

    /**
     *
     * @Description 根据容器ID删除非当前工位的数据
     *
     * @author yuchao.wang
     * @date 2020/9/12 11:41
     * @param tenantId 租户ID
     * @param containerId 容器ID
     * @param workcellId 工位ID
     * @return int
     *
     */
    int deleteNotCurrentWkcData(@Param("tenantId") Long tenantId,
                                 @Param("containerId") String containerId,
                                 @Param("workcellId") String workcellId);

    /**
     *
     * @Description 根据JobContainerId批量出站
     *
     * @author yuchao.wang
     * @date 2021/1/18 9:55
     * @param tenantId 租户
     * @param userId 用户ID
     * @param jobContainerIdList jobContainerIdList
     * @return void
     *
     */
    int batchOutSite(@Param("tenantId") Long tenantId,
                     @Param("userId") Long userId,
                     @Param("jobContainerIdList") List<String> jobContainerIdList);

}
