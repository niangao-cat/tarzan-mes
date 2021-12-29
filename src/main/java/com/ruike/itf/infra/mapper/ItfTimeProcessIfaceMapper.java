package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO2;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/1 16:36
 */
public interface ItfTimeProcessIfaceMapper {

    /**
     * 工单 物料信息
     * @param tenantId
     * @param workOrderIdList
     * @return java.util.List<com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO2>
     * @author sanfeng.zhang@hand-china.com 2021/11/1
     */
    List<ItfProcessReturnIfaceVO2> queryWoInfo(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 查询条码对应EO信息
     *
     * @param tenantId
     * @param materialLotIds
     * @return java.util.List<com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO3>
     * @author sanfeng.zhang@hand-china.com 2021/11/1
     */
    List<ItfProcessReturnIfaceVO3> queryEoByMaterialLotIds(@Param("tenantId") Long  tenantId, @Param("materialLotIds") List<String> materialLotIds);
}
