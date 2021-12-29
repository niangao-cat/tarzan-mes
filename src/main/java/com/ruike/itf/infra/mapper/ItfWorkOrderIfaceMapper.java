package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfRoutingOperationIface;
import com.ruike.itf.domain.entity.ItfWorkOrderIface;
import com.ruike.itf.domain.vo.ItfWoStatusSendErp;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.method.domain.entity.MtRouter;
import tarzan.modeling.domain.entity.MtModProductionLine;

import java.util.Date;
import java.util.List;

/**
 * 工单接口表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfWorkOrderIfaceMapper extends BaseMapper<ItfWorkOrderIface> {

    /**
     * 根据批次日期删除之前的批次
     *
     * @param tableName
     * @param batchDate
     */
    void deleteBatchDate(@Param(value = "tableName") String tableName,
                         @Param(value = "batchDate") String batchDate);

    /**
     * 批量工单导入Iface
     *
     * @param tableName
     * @param woIfaceList
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-16 15:28
     */
    void batchInsertWorkOrderIface(@Param(value = "tableName") String tableName,
                                   @Param(value = "woIfaceList") List<ItfWorkOrderIface> woIfaceList);

    /**
     * 批量工单导入
     *
     * @param tableName
     * @param woIfaceList
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-16 15:28
     */
    void batchInsertWorkOrder(@Param(value = "tableName") String tableName,
                              @Param(value = "woIfaceList") List<ItfWorkOrderIface> woIfaceList);

    /**
     * 查询产线
     *
     * @param tenantId
     * @param materialId
     * @param plantCode
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-16 15:28
     */
    List<String> selectProdLineCode(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "materialId") String materialId,
                                    @Param(value = "plantCode") String plantCode);

    /**
     * 查询产线
     *
     * @param tenantId
     * @param operationName
     * @param plantCode
     * @return
     * @author jiangling.zheng@hand-china.com 2020-07-16 15:28
     */
    String selectOperationDesc(@Param(value = "tenantId") Long tenantId,
                               @Param(value = "operationName") String operationName,
                               @Param(value = "plantCode") String plantCode);

    /**
     * @param tenantId
     * @param plantCode
     * @param workOrderNum
     * @return
     * @author jiangling.zheng@hand-china.com 2020/7/28 16:05
     */
    String selectWoStatus(@Param(value = "tenantId") Long tenantId,
                          @Param(value = "plantCode") String plantCode,
                          @Param(value = "workOrderNum") String workOrderNum);

    /**
     * 根据最后修改时间查询状态
     *
     * @param lastUpdate
     * @return
     * @author jiangling.zheng@hand-china.com 2020/7/28 16:05
     */
    List<ItfWoStatusSendErp> selectWoStatusByLastUpdate(@Param("lastUpdate") Date lastUpdate);

    /**
     * 根据bomName 查询bomId
     *
     * @param workOrderNums
     * @return
     */
    List<String> selectBomId(@Param("workOrderNums") List<String> workOrderNums);

    /**
     * 根据bomName和BOM类型 查询bomId
     *
     * @param workOrderNums
     * @return
     */
    List<String> selectBomId2(@Param("workOrderNums") List<String> workOrderNums);


    /**
     * 根据bomIds 查询bomComponentId
     *
     * @param bomIds
     * @return
     */
    List<String> selectBomComponentId(@Param("bomIds") List<String> bomIds);

    /**
     * 根据bomComponentId 查询BomSubstituteGroupId
     *
     * @param bomComponentIds
     * @return
     */
    List<String> selectBomSubstituteGroupId(@Param("bomComponentIds") List<String> bomComponentIds);

    /**
     * 删除表数据
     *
     * @param sql
     */
    void deleteBomRelTable(@Param("sql") String sql);

    List<String> selectRouterStepId(@Param("workOrderNums") List<String> workOrderNums);

    List<String> selectRouterStepId2(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "routers") List<MtRouter> routers);

    List<ItfWoStatusSendErp> selectWoStatusByIds(@Param("workOrderIds") List<String> workOrderIds);

    /**
     * 查询默认产线
     *
     * @param tenantId  租户
     * @param plantCode 站点
     * @param workshop  部门
     * @return tarzan.modeling.domain.entity.MtModProductionLine
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/30 10:09:40
     */
    MtModProductionLine selectDefaultProdLine(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "plantCode") String plantCode,
                                              @Param(value = "workshop") String workshop);
}
