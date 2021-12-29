package com.ruike.hme.infra.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruike.hme.domain.vo.HmeCosInspectPlatformAutoQueryInfoResponseVO;

/**
 * HmeCosInspectPlatformMapper
 *
 * @author yapeng.yao@hand-china.com 2020/08/28 09:22
 */
public interface HmeCosInspectPlatformMapper {

    /**
     *
     * @param tenantId
     * @param operationRecordId
     * @param workcellId
     * @param operationId
     * @return
     */
    List<HmeCosInspectPlatformAutoQueryInfoResponseVO> queryAutoOperationRecord(
            @Param(value = "tenantId") Long tenantId,
            @Param(value = "operationRecordId") String operationRecordId,
            @Param(value = "workcellId") String workcellId, @Param(value = "operationId") String operationId);


    /**
     * 检索按钮查询记录
     * 
     * @param tenantId
     * @param operationRecordId
     * @param workcellId
     * @param operationId
     * @param workOrderNum
     * @param wafer
     * @param equipmentId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<HmeCosInspectPlatformAutoQueryInfoResponseVO> queryOperationRecord(@Param(value = "tenantId") Long tenantId,
                                                                            @Param(value = "operationRecordId") String operationRecordId,
                                                                            @Param(value = "workcellId") String workcellId, @Param(value = "operationId") String operationId,
                                                                            @Param(value = "workOrderNum") String workOrderNum, @Param(value = "wafer") String wafer,
                                                                            @Param(value = "equipmentId") String equipmentId, @Param(value = "beginDate") String beginDate,
                                                                            @Param(value = "endDate") String endDate);

}
