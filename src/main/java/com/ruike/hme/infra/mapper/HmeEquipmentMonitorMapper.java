package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeExcWkcRecord;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 设备状态监控平台-mapper
 *
 * @author chaonan.hu@hand-china.com 2020-07-16 20:24:36
 **/
public interface HmeEquipmentMonitorMapper {

    /**
     * 设备状态一览查询
     *
     * @param tenantId  租户
     * @param stationId 工位
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO4>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 20:32:49
     */
    List<HmeEquipmentMonitorVO4> equipmentStatusQuery(@Param("tenantId") Long tenantId,
                                                      @Param("stationId") String stationId);

    /**
     * 根据工段查询班次开始时间
     *
     * @param tenantId   租户
     * @param workcrllId 工作单元
     * @return java.util.Date
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 20:55:07
     */
    Date shiftStartTimeQuery(@Param("tenantId") Long tenantId,
                             @Param("workcrllId") String workcrllId);

    /**
     * 设备异常记录查询
     *
     * @param tenantId    租户
     * @param closeTime   关闭时间
     * @param equipmentId 设备
     * @param workcrllId  工作单元
     * @return java.util.List<com.ruike.hme.domain.entity.HmeExcWkcRecord>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/16 21:16:49
     */
    List<HmeExcWkcRecord> equExcRecordQuery(@Param("tenantId") Long tenantId, @Param("closeTime") Date closeTime,
                                            @Param("equipmentId") String equipmentId, @Param("workcrllId") String workcrllId);

    /**
     * 根据设备ID、工位ID查询故障设备数量
     *
     * @param tenantId    租户
     * @param equipmentId 设备ID
     * @param workcrllId  工作单元
     * @return java.lang.Long
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/17 10:34:52
     */
    Long errorEquipmentCount(@Param("tenantId") Long tenantId, @Param("equipmentId") String equipmentId,
                             @Param("workcrllId") String workcrllId);

    /**
     * 当月异常停机TOP10设备查询
     *
     * @param tenantId 租户ID
     * @param equipmentId 设备ID
     * @param creationDateFrom 当月第一天
     * @param creationDateTo 当月最后一天
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO7>
     */
    List<HmeEquipmentMonitorVO7> downEquipmentQuery(@Param("tenantId") Long tenantId, @Param("equipmentId") String equipmentId,
                                                    @Param("creationDateFrom") Date creationDateFrom, @Param("creationDateTo") Date creationDateTo);

    /**
     * 30天内异常历史
     * 
     * @param tenantId 租户ID
     * @param equipmentId 设备ID
     * @param creationDateFrom 30天前
     * @param creationDateTo 当前日期
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 03:59:43 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO10>
     */
    List<HmeEquipmentMonitorVO10> exceptionHistoryQuery(@Param("tenantId") Long tenantId, @Param("equipmentId") String equipmentId,
                                                        @Param("creationDateFrom") Date creationDateFrom, @Param("creationDateTo") Date creationDateTo);

    /**
     * 同异常类型最近三次
     * 
     * @param tenantId 租户ID
     * @param equipmentId 设备ID
     * @param exceptionGroupId 异常类型ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/22 04:31:35 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentMonitorVO11>
     */
    List<HmeEquipmentMonitorVO11> sameExceptionTypeQuery(@Param("tenantId") Long tenantId, @Param("equipmentId") String equipmentId,
                                                         @Param("exceptionGroupId") String exceptionGroupId);

    /**
     * 部门
     *
     * @param tenantId
     * @param siteId
     * @param areaCategory
     * @return
     */
    List<HmeEquipmentMonitorVO> departmentDataQuery(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("areaCategory") String areaCategory);
}
