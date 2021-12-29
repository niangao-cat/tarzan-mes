package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfWoSnRelIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * Sn同步接口Mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 04:25:28
 */
public interface ItfWoSnRelIfaceMapper extends BaseMapper<ItfWoSnRelIface> {

    /**
     * 接口数据查询
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.itf.domain.vo.ItfWoSnRelVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 04:27:09
     */
    List<ItfWoSnRelIface> selectInterfaceData(@Param("tenantId") Long tenantId);

    /**
     *
     * @Description 回写接口表时查询原有数据
     *
     * @author yuchao.wang
     * @date 2020/7/30 17:16
     * @param itfWoSnRelIface
     * @return com.ruike.itf.domain.entity.ItfWoSnRelIface
     *
     */
    ItfWoSnRelIface queryIfaceForReturnWrite(ItfWoSnRelIface itfWoSnRelIface);

    /**
     *
     * @Description 批量新增接口表数据
     *
     * @author yuchao.wang
     * @date 2020/8/4 8:24
     * @param woSnIfaceList 新增数据列表
     * @return void
     *
     */
    void batchInsert(@Param(value = "woSnIfaceList") List<ItfWoSnRelIface> woSnIfaceList);

    /**
     *
     * @Description 批量更新接口表数据
     *
     * @author yuchao.wang
     * @date 2020/8/4 9:14
     * @param woSnIfaceList 更新数据列表
     * @return void
     *
     */
    void batchUpdate(@Param(value = "userId") Long userId,
                     @Param(value = "woSnIfaceList") List<ItfWoSnRelIface> woSnIfaceList);

    /**
     *
     * @Description 批量更新接口表数据状态
     *
     * @author yuchao.wang
     * @date 2020/8/4 11:13
     * @param userId 用户ID
     * @param status 状态
     * @param woSnIfaceIdList 更新数据列表
     * @return void
     *
     */
    void batchUpdateStatus(@Param(value = "userId") Long userId,
                           @Param(value = "status") String status,
                           @Param(value = "woSnIfaceIdList") List<String> woSnIfaceIdList);
}
