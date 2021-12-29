package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.vo.HmeCosWireBondVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HmeCosWireBondMapper {

    List<HmeCosWireBondVO> siteOutDateNullQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                @Param("operationId") String operationId);

    /**
     * 根据物料批和工艺查询作业类型为‘COS_WIRE_BOND’的出站时间为空的记录
     *
     * @param tenantId
     * @param materialLotId
     * @param operationId
     * @author yifan.xiong@hand-china.com 2020-9-18 13:57:52
     * @return java.util.List<java.lang.String>
     */
    List<String> siteOutDateNullQuery1(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                                       @Param("operationId") String operationId);

    /**
     * 来料信息记录查询
     *
     * @param tenantId 租户Id
     * @param workcellId 工位Id
     * @param operationId 工艺Id
     * @param equipmentId 设备Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/24 08:01:06
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     */
    HmeCosOperationRecord recordQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                      @Param("operationId") String operationId, @Param("workOrderId") String workOrderId,
                                      @Param("waferNum") String waferNum, @Param("equipmentId") String equipmentId);

    /**
     * 来料信息记录查询
     *
     * @param tenantId 租户Id
     * @param workcellId 工位Id
     * @param operationId 工艺Id
     * @param equipmentId 设备Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/24 08:01:06
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     */
    HmeCosOperationRecord recordQuery2(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                       @Param("operationId") String operationId, @Param("workOrderId") String workOrderId,
                                       @Param("waferNum") String waferNum, @Param("equipmentId") String equipmentId);

    /**
     * 工艺步骤id
     *
     * @param tenantId    租户id
     * @param workOrderId 工单
     * @param operationId 工艺
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/11 12:30
     */
    List<String> queryRouterStepIdByWorkOrderIdAndOperationId(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId, @Param("operationId") String operationId);
}
