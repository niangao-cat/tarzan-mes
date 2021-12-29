package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosPatchPdaDTO5;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.vo.HmeCosPatchPdaVO5;
import com.ruike.hme.domain.vo.HmeCosPatchPdaVO9;
import org.apache.ibatis.annotations.Param;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;

import java.math.BigDecimal;
import java.util.List;

/**
 * COS贴片平台-mapper
 *
 * @author chaonan.hu@hand-china.com 2020-08-24 17:18:12
 **/
public interface HmeCosPatchPdaMapper {

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
                                            @Param("operationId") String operationId, @Param("equipmentId") String equipmentId);

    /**
     * 根据物料批和工艺查询作业类型为‘COS_PASTER_IN’的出站时间为空的记录
     *
     * @param tenantId
     * @param materialLotId
     * @param operationId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/24 08:03:15
     * @return java.util.List<java.lang.String>
     */
    List<String> siteOutDateNullQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                                      @Param("operationId") String operationId);

    /**
     * 根据工位+工艺+工单+作业平台类型为‘COS_PASTER_IN’的出站时间为空的记录
     * 
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param workOrderId 工单ID
     * @param operationId 工艺ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/2 12:38:48 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosPatchPdaVO5>
     */
    List<HmeCosPatchPdaVO5> siteOutDateNullQuery2(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                  @Param("workOrderId") String workOrderId, @Param("operationId") String operationId);

    /**
     * 来料信息记录查询
     *
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param workOrderId 工单ID
     * @param operationId 工艺ID
     * @param workcellId 工位ID
     * @param wafer
     * @param equipmentId 设备ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/24 08:48:34
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     */
    HmeCosOperationRecord cosOpRecordQuery(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                           @Param("workOrderId") String workOrderId, @Param("operationId") String operationId,
                                           @Param("workcellId") String workcellId, @Param("wafer") String wafer,
                                           @Param("equipmentId") String equipmentId);

    /**
     * 工艺路线步骤查询
     * 
     * @param tenantId 租户ID
     * @param routerId 工艺ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/25 06:41:04 
     * @return java.util.List<tarzan.method.domain.entity.MtRouterOperation>
     */
    List<MtRouterOperation> routerOperationQuery(@Param("tenantId") Long tenantId, @Param("routerId") String routerId);

    /**
     * 装配清单行数据查询
     * 
     * @param tenantId 租户ID
     * @param routerOperationId 工艺路线步骤Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/25 06:56:31
     * @return java.util.List<tarzan.method.domain.entity.MtBomComponent>
     */
    List<MtBomComponent> bomComponentQuery(@Param("tenantId") Long tenantId, @Param("routerOperationId") String routerOperationId);

    /**
     * 装配清单行数据查询-限定组件物料为批次物料
     *
     * @param tenantId 租户ID
     * @param routerOperationId 工艺路线步骤Id
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/31 21:17:01
     * @return java.util.List<tarzan.method.domain.entity.MtBomComponent>
     */
    List<MtBomComponent> bomComponentQuery2(@Param("tenantId") Long tenantId, @Param("routerOperationId") String routerOperationId,
                                            @Param("siteId") String siteId, @Param("itemGroupList") List<String> itemGroupList);

    /**
     * 获取进站时间最近行项目的物料批Id
     *
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @param operationId 工艺ID
     * @param workcellId 工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/26 11:14:39
     * @return java.lang.String
     */
    String getMaterialLotId(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId,
                            @Param("operationId") String operationId, @Param("workcellId") String workcellId);

    /**
     * 根据工单+工艺+WKC+Wafer+设备查询剩余芯片数大于0的在制记录
     * 
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/27 10:56:50 
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     */
    HmeCosOperationRecord opRecordQuery(@Param("tenantId") Long tenantId, @Param("operationId") String operationId,
                               @Param("dto") HmeCosPatchPdaDTO5 dto);

    /**
     * 根据在制记录ID查询下面未出站数据的物料批数量
     *
     * @param tenantId 租户ID
     * @param sourceJobId 在制记录ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/27 11:18:18
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     */
    BigDecimal getNoSiteOutMaLotQty(@Param("tenantId") Long tenantId, @Param("sourceJobId") String sourceJobId);

    /**
     * 根据在制记录ID查询下面已出站和未出站数据的所有数据
     *
     * @param tenantId 租户ID
     * @param sourceJobId 在制记录ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/8 09:48:34
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     */
    List<HmeEoJobSn> getEoJobSnData(@Param("tenantId") Long tenantId, @Param("sourceJobId") String sourceJobId);

    /**
     * 根据eoId、SUB_ROUTER_FLAG != Y 在mt_eo_router_actual表中获取EO_ROUTER_ACTUAL_ID
     *
     * @param tenantId 租户ID
     * @param eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/27 15:01:19
     * @return java.lang.String
     */
    String getEoStepActualId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 查询工单的默认发料库位
     * 
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/29 11:59:23 
     * @return java.lang.String
     */
    String getIssuedLocatorId(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

    /**
     * 查询可新增数量
     * 
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param operationId 工艺ID
     * @param jobType 作业平台类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/24 10:56:36 
     * @return java.math.BigDecimal
     */
    BigDecimal getAddQty(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                         @Param("operationId") String operationId, @Param("wafer") String wafer,
                         @Param("jobType") String jobType);

    /**
     * 查询工单贴片完成数
     * 
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param workOrderId 工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/1 15:20:15
     * @return java.math.BigDecimal
     */
    BigDecimal getAchieveQty(@Param("tenantId") Long tenantId, @Param("operationId") String operationId,
                             @Param("workOrderId") String workOrderId);

    /**
     * 根据工段查询默认存储库位
     * 
     * @param tenantId 租户ID
     * @param wkcLineId 工段ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/3 10:19:21 
     * @return java.util.List<java.lang.String>
     */
    List<String> defaultStorageLocatorQuery(@Param("tenantId") Long tenantId, @Param("wkcLineId") String wkcLineId);

    /**
     * 根据产线ID查询产线简码
     *
     * @param tenantId 租户ID
     * @param prodLineId 产线ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/3 11:47:18
     * @return java.lang.String
     */
    String getProdLineShortName(@Param("tenantId") Long tenantId, @Param("prodLineId") String prodLineId);

    /**
     * 根据BOM组件ID查询工单替代料
     *
     * @param tenantId 租户ID
     * @param bomComponentId BOM组件物料ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/15 10:55:41
     * @return java.util.List<java.lang.String>
     */
    List<String> getWorkOrderSubstituteMaterial(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId);

    /**
     * 根据BOM组件上的物料+站点查询全局替代料
     * 
     * @param tenantId 租户ID
     * @param materialId BOM组件物料ID
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/15 11:07:18 
     * @return java.util.List<java.lang.String>
     */
    List<String> getGlobalSubstituteMaterial(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                             @Param("siteId") String siteId);

    /**
     * 工位绑定批次物料查询
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param materialIdList 批次物料ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/15 03:42:30
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobLotMaterial>
     */
    List<HmeEoJobLotMaterial> eoJobLotMaterialQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                    @Param("materialIdList") List<String> materialIdList);

    /**
     * 根据工位+工艺+工单+作业平台类型为‘COS_PASTER_IN’查询进出站记录
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param workOrderId 工单ID
     * @param operationId 工艺ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 17:42:23
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosPatchPdaVO5>
     */
    List<HmeCosPatchPdaVO5> eoJobSnDataQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                  @Param("workOrderId") String workOrderId, @Param("operationId") String operationId);

    /**
     * 根据Bom组件ID查询实际装配数量
     *
     * @param tenantId 租户ID
     * @param bomComponentId Bom组件ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/16 20:36:34
     * @return java.lang.Double
     */
    double assembleQtySum(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId);

    /**
     * 根据物料ID和工单ID查询投料数量之和
     * 
     * @param tenantId 租户ID
     * @param materialIdList 物料ID集合
     * @param workOrderId 工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/9 10:39:54 
     * @return java.math.BigDecimal
     */
    BigDecimal getAssembleQtySum(@Param("tenantId") Long tenantId, @Param("materialIdList") List<String> materialIdList,
                                 @Param("workOrderId") String workOrderId);

    /**
     * 根据BomId获取所有的Bom组件物料Id
     *
     * @param tenantId 租户ID
     * @param bomId BomId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/9 13:56:48
     * @return java.util.List<java.lang.String>
     */
    List<String> getMaterialIdListByBomId(@Param("tenantId") Long tenantId, @Param("bomId") String bomId);

    /**
     * 根据替代料组查询其下的所有替代料
     *
     * @param tenantId 租户ID
     * @param substituteGroup 替代料组ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/01/08 10:30:46
     * @return java.util.List<java.lang.String>
     */
    List<String> getSubstituteMaterialByGroup(@Param("tenantId") Long tenantId, @Param("substituteGroup") String substituteGroup);

    /**
     * 工单替代料查询主键料
     *
     * @param tenantId 租户ID
     * @param bomComponentId 替代料的Bom组件
     * @param bomId 工单bomId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/01/08 10:57:34
     * @return java.util.List<java.lang.String>
     */
    List<String> getPrimaryMaterialByWo(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId,
                                    @Param("bomId") String bomId);

    /**
     * 根据库位ID查询子库位
     * 
     * @param tenantId 租户ID
     * @param locatorId 库位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/8 17:44:15
     * @return java.util.List<java.lang.String>
     */
    List<String> getSubLocatorByLocatorId(@Param("tenantId") Long tenantId, @Param("locatorId") String locatorId);

    /**
     * 还原出站记录
     *
     * @param tenantId
     * @param userId
     * @param jobIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/25 23:59
     * @return void
     */
    void recallEoJobSn(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("jobIdList") List<String> jobIdList);

    /**
     * COS履历
     *
     * @param tenantId
     * @param materialLotId
     * @param workcellId
     * @param workOrderId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeLoadJob>
     * @author sanfeng.zhang@hand-china.com 2021/9/26  
     */
    List<HmeLoadJob> queryLoadJob(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId, @Param("workcellId") String workcellId, @Param("workOrderId") String workOrderId);

    /**
     * 装载信息作业对象表
     *
     * @param tenantId
     * @param loadJobIdList
     * @return java.util.List<com.ruike.hme.domain.entity.HmeLoadJobObject>
     * @author sanfeng.zhang@hand-china.com 2021/9/26
     */
    List<HmeLoadJobObject> queryLoadJobObject(Long tenantId, List<String> loadJobIdList);

    /**
     * 批量删除履历
     * @param tenantId
     * @param userId
     * @param deleteLoadJobList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/9/26
     */
    void myBatchDeleteLoadJob(Long tenantId, Long userId, List<HmeLoadJob> deleteLoadJobList);

    /**
     * 批量删除
     *
     * @param tenantId
     * @param userId
     * @param deleteLoadJobObjectList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/9/26
     */
    void myBatchDeleteLoadObject(Long tenantId, Long userId, List<HmeLoadJobObject> deleteLoadJobObjectList);

    /**
     * 清空热沉信息
     * @param tenantId
     * @param userId
     * @param hmeMaterialLotLoads
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/9/26
     */
    void myBatchCleanHotSink(Long tenantId, Long userId, List<HmeMaterialLotLoad> hmeMaterialLotLoads);

    /**
     * 根据物料批ID查询实验代码、备注扩展属性
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/27 11:50:39
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO9
     */
    List<HmeCosPatchPdaVO9> labCodeAndRemarkAttrQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据物料批ID查询实验代码扩展属性
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/27 03:24:39
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosPatchPdaVO9>
     */
    List<HmeCosPatchPdaVO9> materialLotAttrQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList,
                                             @Param("attrName") String attrName);

    /**
     * 根据物料批ID查询实验代码、实验代码备注、工单扩展属性
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/9 11:31:08
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosPatchPdaVO9>
     */
    List<HmeCosPatchPdaVO9> labCodeRemarkAndWoAttrQuery(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据物料批ID查询loadSequence
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/9 03:22:23
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> loadSequenceQueryByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据jobId查询实验代码以及备注
     *
     * @param tenantId 租户ID
     * @param jobId jobId
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/9 04:12:04
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO9
     */
    HmeCosPatchPdaVO9 labCodeAndRemarkQueryByJobId(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);
}
