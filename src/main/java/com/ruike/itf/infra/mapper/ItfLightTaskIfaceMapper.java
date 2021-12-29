package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfLightTaskIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 亮灯指令接口表Mapper
 *
 * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
 */
public interface ItfLightTaskIfaceMapper extends BaseMapper<ItfLightTaskIface> {

    /**
     * 取消时批量更新接口表状态
     *
     * @param tenantId 租户ID
     * @param itfLightTaskIfaces
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return void
     */
    void updateTaskStatus(@Param("tenantId")Long tenantId,
                      @Param("userId")Long userId,
                      @Param("nowDate")Date nowDate,
                      @Param("itfLightTaskIfaces")List<ItfLightTaskIface> itfLightTaskIfaces);

    /**
     * 根据taskNum查询要传输的数据
     *
     * @param tenantId 租户ID
     * @param taskNumList 任务号集合
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return ItfLightTaskIface
     */
    List<ItfLightTaskIface> selectByTaskNum(@Param("tenantId")Long tenantId,
                                            @Param("taskNumList")List<String> taskNumList);

    /**
     * 将异常情况下的错误信息记录到接口表
     *
     * @param tenantId 租户ID
     * @param ifaceIdList 接口表ID集合
     * @param message 错误信息
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return void
     */
    void updateIfaceData(@Param("tenantId") Long tenantId,
                         @Param("ifaceIdList") List<String> ifaceIdList,
                         @Param("message") String message,
                         @Param("userId") Long userId);

    /**
     * 根据接口放回数据批量更新接口表状态
     *
     * @param tenantId 租户ID
     * @param itfLightTaskIfaceList
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return void
     */
    void updateStatusAndMsg(@Param("tenantId")Long tenantId,
                            @Param("userId")Long userId,
                            @Param("nowDate")Date nowDate,
                            @Param("itfLightTaskIfaceList") List<ItfLightTaskIface> itfLightTaskIfaceList);
}
