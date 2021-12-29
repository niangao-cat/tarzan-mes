package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeAreaThroughRateDetails;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 制造部直通率看板Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-06-24 21:17:01
 */
public interface HmeAreaThroughRateDetailsMapper extends BaseMapper<HmeAreaThroughRateDetails> {

    /**
     * 看板制造部
     * @param tenantId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    List<String> queryKanbanAreaList(@Param("tenantId") Long tenantId);

    /**
     * 根据部门查找产线
     * @param tenantId
     * @param areaId
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    List<String> queryProdLineByAreaId(@Param("tenantId") Long tenantId, @Param("areaId") String areaId, @Param("siteId") String siteId);

    /**
     * 根据产线取产品组
     * @param tenantId
     * @param prodLineList
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO10>
     * @author sanfeng.zhang@hand-china.com 2021/6/1
     */
    List<HmeMakeCenterProduceBoardVO10> queryProductionGroupByProdLineId(@Param("tenantId") Long tenantId, @Param("prodLineList") List<String> prodLineList, @Param("siteId") String siteId);

    /**
     * 获取当天在做的工单
     * @param tenantId
     * @param currentStartDate
     * @param currentEndDate
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/4
     */
    List<String> queryCurrentWorkOrderIdList(@Param("tenantId") Long tenantId, @Param("currentStartDate") String currentStartDate, @Param("currentEndDate") String currentEndDate);

    /**
     * 批量查询看板行信息
     *
     * @param tenantId
     * @param centerKanbanLineByHeaderIds
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO12>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO12> batchQueryCenterKanbanLineByHeaderIds(@Param("tenantId") Long tenantId, @Param("centerKanbanLineByHeaderIds") List<String> centerKanbanLineByHeaderIds);

    /**
     * 工单查询工序
     *
     * @param tenantId
     * @param siteId
     * @param workOrderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO11>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO11> batchQueryProcessByWorkOrders(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 根据工单和工序查询eo
     *
     * @param tenantId
     * @param allWorkOrderIdList
     * @param processIds
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO19> queryEoListByWorkOrderAndWorkcell(@Param("tenantId") Long tenantId, @Param("allWorkOrderIdList") List<String> allWorkOrderIdList, @Param("processIds") List<String> processIds, @Param("siteId") String siteId, @Param("currentStartDate") String currentStartDate, @Param("currentEndDate") String currentEndDate);

    /**
     * 根据工单和工序查询返修的eo
     * @param tenantId
     * @param eoIdList
     * @param processIds
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     * @author sanfeng.zhang@hand-china.com 2021/6/9
     */
    List<HmeMakeCenterProduceBoardVO19> batchQueryReworkRecordEoList(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList, @Param("processIds") List<String> processIds, @Param("siteId") String siteId);

    /**
     * 根据工单和工序查询COSSN数量和出站数量
     *
     * @param tenantId
     * @param allWorkOrderIdList
     * @param processIds
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/6/22 0:45
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     */
    List<HmeMakeCenterProduceBoardVO19> queryCosQtyByWorkOrderAndProcess(@Param("tenantId") Long tenantId, @Param("allWorkOrderIdList") List<String> allWorkOrderIdList, @Param("processIds") List<String> processIds, @Param("siteId") String siteId);

    /**
     * 查询当天不良数
     *
     * @param tenantId
     * @author sanfeng.zhang@hand-china.com 2021/6/22 1:14
     * @return java.lang.Integer
     */
    Integer queryCosNcRecordNum(@Param("tenantId") Long tenantId);

    /**
     * COS测试工序的SN数量
     *
     * @param tenantId
     * @param workOrderIdList
     * @author sanfeng.zhang@hand-china.com 2021/6/22 1:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO19>
     */
    List<HmeMakeCenterProduceBoardVO19> queryCos015SnQty(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 批量删除
     * @param tenantId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    void batchDeleteThroughRate(Long tenantId);

    /**
     * 条码复检合格数
     *
     * @param tenantId
     * @param materialLotIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO20>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    List<HmeMakeCenterProduceBoardVO20> queryReInspectionOkQty(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);
}
