package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeMaterialLotNcLoad;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.method.domain.entity.MtBomComponent;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 不良作业平台-mapper
 * @author: chaonan.hu@hand-china.com 2020-06-30 10:58:36
 **/
public interface HmeNcDisposePlatformMapper {

    /**
     * @Description 根据工位查询更多不良类型组
     * @param workcellId 工位Id
     * @param description 不良类型组描述
     * @return java.util.List<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO8>
     * @auther chaonan.hu
     * @date 2020/6/30
     */
    List<HmeNcDisposePlatformDTO8> getProcessNcCodeTypes(@Param("workcellId") String workcellId, @Param("description") String description,
                                                         @Param("componentRequired") String componentRequired);

    /**
     * @Description 根据工位查询不良类型组
     * @param workcellId 工位Id
     * @return java.util.List<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO8>
     * @auther chaonan.hu
     * @date 2020/7/21
     */
    List<HmeNcDisposePlatformDTO8> getProcessNcCodeTypes2(@Param("workcellId") String workcellId,
                                                          @Param("componentRequired") String componentRequired);

    /**
     * @Description 查询其他工位
     * @param dto
     * @return java.util.List<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO6>
     * @auther chaonan.hu
     * @date 2020/6/30
     */
    List<HmeNcDisposePlatformDTO12> getOtherWorkcell(@Param("dto") HmeNcDisposePlatformDTO10 dto);

    /**
     * @Description 根据序列号查询工步
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<java.lang.String>
     * @auther chaonan.hu
     * @date 2020/7/1
     */
    List<String> getRouterStep(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     * @Description 根据工艺和工步查询材料清单
     * @param tenantId 租户Id
     * @param operationId 工艺Id
     * @param routerStepId 工步Id
     * @return java.util.List<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO23>
     * @auther chaonan.hu
     * @date 2020/7/1
     */
    List<HmeNcDisposePlatformDTO23> getMaterialData(@Param("tenantId") Long tenantId, @Param("operationId") String operationId,
                                                    @Param("routerStepId") String routerStepId);

    /**
     * @Description 根据物料查询物料批类型
     * @param tenantId 租户Id
     * @param materialId 物料Id
     * @param siteId 站点Id
     * @return java.lang.String
     * @auther chaonan.hu
     * @date 2020/7/1
     */
    String getAttrValue(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                        @Param("siteId") String siteId);

    /**
     * @Description 材料清单分页查询
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.api.dto.HmeNcDisposePlatformDTO13>
     * @auther chaonan.hu
     * @date 2020/7/2
     */
    List<HmeNcDisposePlatformDTO13> materialDataPageQuery(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                          @Param("userId") String userId, @Param("dto") HmeNcDisposePlatformDTO dto);

    /**
     * 不良代码记录条件查询
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeNcDisposePlatformDTO2> ncRecordQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeNcCheckDTO dto);

    /**
     * 查询出站日期
     *
     * @param tenantId 租户ID
     * @param materiaLotlId 物料批ID
     * @return java.util.Date
     */
    Date siteOutDateQuery(@Param("tenantId") Long tenantId, @Param("materiaLotlId") String materiaLotlId);

    /**
     * 查询最近的备注
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param ncCodeId 不良代码组ID
     * @param rootCauseOperationId 产生问题的源工艺
     * @param eoStepActualId 工步
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/15 17:03:54
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO26
     */
    HmeNcDisposePlatformDTO26 commentsQuery(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                            @Param("ncCodeId") String ncCodeId, @Param("rootCauseOperationId") String rootCauseOperationId,
                                            @Param("eoStepActualId") String eoStepActualId);

    /**
     * 查询物料批下未出站的数据
     *
     * @param tenantId
     * @param materiaLotlId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/25 09:55:03
     * @return java.util.List<java.lang.String>
     */
    List<String> getNoSiteOutJobId(@Param("tenantId") Long tenantId, @Param("materiaLotlId") String materiaLotlId);

    /**
     * 根据工序ID查询表mt_operation_wkc_dispatch_rel中优先级最高的operation_id
     *
     * @param tenantId 租户ID
     * @param workcellId 工序ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 17:29:56
     * @return java.lang.String
     */
    String getOperationId(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 根据扫描SN的ID、工艺ID查询表hme_eo_job_sn中的jobId
     *
     * @param tenantId 租户ID
     * @param materiaLotlId 扫描SN的ID
     * @param operationId 工艺ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 17:36:02
     * @return java.util.List<java.lang.String>
     */
    List<String> getJobIdList(@Param("tenantId") Long tenantId, @Param("materiaLotlId") String materiaLotlId,
                              @Param("operationId") String operationId);

    /**
     * 序列号投料记录查询
     *
     * @param tenantId 租户Id
     * @param jobId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 18:43:41
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO5>
     */
    List<HmeNcDisposePlatformVO5> snMaterialQuery(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     * 报废数量、申请数量查询
     *
     * @param tenantId 租户ID
     * @param materiaLotlId 物料批ID
     * @param operationId 工艺ID
     * @param ncStatus 不良状态
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 07:48:17 
     * @return java.math.BigDecimal
     */
    BigDecimal qtyQuery(@Param("tenantId") Long tenantId, @Param("materiaLotlId") String materiaLotlId,
                        @Param("operationId") String operationId, @Param("ncStatus") String ncStatus );

    /**
     * 批次/时效投料记录查询
     *
     * @param tenantId 租户ID
     * @param jobId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 20:11:06
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO5>
     */
    List<HmeNcDisposePlatformVO5> timeMaterialQuery(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     * 材料报废记录查询
     *
     * @param tenantId 租户ID
     * @param eoId
     * @param workcellId 工位ID
     * @param operationId 工艺ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/27 20:54:19
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO3>
     */
    List<HmeNcDisposePlatformVO3> ncMaterialQuery(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                                                  @Param("workcellId") String workcellId, @Param("operationId") String operationId,
                                                  @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据工段查询关联的区域库位
     *
     * @param tenantId 租户ID
     * @param organizationId 工段ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/28 10:46:38 
     * @return java.util.List<java.lang.String>
     */
    List<String> areaLocatorQuery(@Param("tenantId") Long tenantId, @Param("organizationId") String organizationId);

    /**
     * 根据物料批查询未关闭的材料不良记录
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/5 14:45:45
     * @return long
     */
    long getTotalByMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 材料清单报废数量查询
     *
     * @param tenantId 租户ID
     * @param materiaLotlId 物料批ID
     * @param operationId 工艺ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/13 01:41:46
     * @return java.math.BigDecimal
     */
    BigDecimal scrapQtyQuery(@Param("tenantId") Long tenantId, @Param("materiaLotlId") String materiaLotlId,
                             @Param("operationId") String operationId);

    /**
     * 材料清单未投料情况下待审核数量查询
     *
     * @param tenantId 租户ID
     * @param materiaLotlId 物料批ID
     * @param eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/20 11:28:34
     * @return java.math.BigDecimal
     */
    BigDecimal waitAuditQtyQuery(@Param("tenantId") Long tenantId, @Param("materiaLotlId") String materiaLotlId,
                                 @Param("eoId") String eoId);

    /**
     * 材料清单投料情况下待审核数量查询
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param eoId eoID
     * @param materialLotIdList 未投料的物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/23 16:24:46
     * @return java.math.BigDecimal
     */
    BigDecimal waitAuditQtyReleaseQuery(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                        @Param("eoId") String eoId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据EoId和物料Id查询到对应的Bom组件
     *
     * @param tenantId 租户ID
     * @param eoId
     * @param materialId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/16 16:54:14
     * @return java.lang.String
     */
    List<String> getBomComponentIdByMaterial(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                                             @Param("materialId") String materialId);

    /**
     * 根据BOMComponentId查询物料的投料数量和
     *
     * @param tenantId 租户ID
     * @param bomComponentId Bom组件ID
     * @param workOrderId 工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/16 17:22:37
     * @return java.math.BigDecimal
     */
    BigDecimal getAssembleQtySum(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId,
                                 @Param("workOrderId") String workOrderId);

    /**
     * 根据material_id、eo_id并且assemble_qty>0查找表mt_eo_component_actual第一条数据的operation_id
     *
     * @param tenantId
     * @param materialId
     * @param eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/16 19:53:57
     * @return tarzan.actual.domain.entity.MtEoComponentActual
     */
    MtEoComponentActual eoComponentActualQuery(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                               @Param("eoId") String eoId);

    /**
     * 材料清单-装配实绩表投料记录查询
     *
     * @param tenantId 租户ID
     * @param eoId
     * @param workcellId 工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/23 14:51:22
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4>
     */
    List<HmeNcDisposePlatformVO4> eoComponentActualAssembleQuery(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                                                                 @Param("workcellId") String workcellId);

    /**
     * 材料清单-不良申请物料条码待审核记录
     *
     * @param tenantId 租户ID
     * @param eoId
     * @param workcellId 工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/23 15:20:59
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4>
     */
    List<HmeNcDisposePlatformVO4> ncMaterialLotQuery(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                                                     @Param("workcellId") String workcellId);

    /**
     * 根据jobId、条码Id、条码投料量大于0查询eo批次物料投料记录
     *
     * @param tenantId 租户ID
     * @param jobId
     * @param materialLotId 条码Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/29 13:51:25
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial>
     */
    List<HmeEoJobSnLotMaterial> eoJobSnLotMaterialQuery(@Param("tenantId") Long tenantId, @Param("jobId") String jobId,
                                                        @Param("materialLotId") String materialLotId);

    /**
     * 根据eoId、ROUTER_OPERATION_ID查询ROUTER_STEP_ID
     *
     * @param tenantId
     * @param eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/29 15:01:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO7>
     */
    String routerStepIdQuery(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                             @Param("operationId") String operationId);

    /**
     * 根据WoId查询到对应line_number和material_id下的bomComponentId
     *
     * @param tenantId 租户Id
     * @param workOrderId woId
     * @param lineNumber 行号
     * @param materialId 物料Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/29 16:44:51
     * @return java.lang.String
     */
    String woBomComponentIdQuery(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId,
                                 @Param("lineNumber") Long lineNumber, @Param("materialId") String materialId);

    /**
     * 根据物料ID、工单ID查询已投料数量
     *
     * @param tenantId 租户ID
     * @param materialIdList 物料ID集合
     * @param workOrderId 工单ID
     * @param routerStepId 工步ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/14 15:32:13
     * @return java.math.BigDecimal
     */
    BigDecimal getAssembleQty(@Param("tenantId") Long tenantId, @Param("materialIdList") List<String> materialIdList,
                              @Param("workOrderId") String workOrderId, @Param("routerStepId") String routerStepId);

    /**
     * 根据物料ID、工单ID查询已报废数量
     *
     * @param tenantId 租户ID
     * @param materialIdList 物料ID集合
     * @param workOrderId 工单ID
     * @param routerStepId 工步ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/14 17:09:13
     * @return java.math.BigDecimal
     */
    BigDecimal getScrapQty(@Param("tenantId") Long tenantId, @Param("materialIdList") List<String> materialIdList,
                           @Param("workOrderId") String workOrderId, @Param("routerStepId") String routerStepId);

    /**
     * 根据替代料查询主键料
     *
     * @param tenantId 租户ID
     * @param bomComponentId 替代料的Bom组件
     * @param bomId 序列号对应eo的bomId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/14 20:42:37
     * @return java.util.List<java.lang.String>
     */
    List<String> getPrimaryMaterial(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId,
                                    @Param("bomId") String bomId);

    /**
     * 根据工段查询线边仓
     *
     * @param tenantId 租户ID
     * @param wkcLineId 工段ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/15 11:23:24
     * @return java.util.List<java.lang.String>
     */
    List<String> defaultStorageLocatorQuery(@Param("tenantId") Long tenantId, @Param("wkcLineId") String wkcLineId);

    /**
     * 根据替代料组查询其下的所有替代料
     *
     * @param tenantId 租户ID
     * @param substituteGroup 替代料组ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/16 10:02:13
     * @return java.util.List<java.lang.String>
     */
    List<String> getSubstituteMaterialByGroup(@Param("tenantId") Long tenantId, @Param("substituteGroup") String substituteGroup);

    /**
     * 根据eoId查询router_step_id
     *
     * @param tenantId 租户ID
     * @param eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/16 10:27:20
     * @return java.util.List<java.lang.String>
     */
    List<String> getRouterStepByEo(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 根据woId查询router_step_id
     *
     * @param tenantId 租户ID
     * @param woId 工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/23 19:02:21
     * @return java.util.List<java.lang.String>
     */
    List<String> getRouterStepByWo(@Param("tenantId") Long tenantId, @Param("woId") String woId);

    /**
     * 根据主键料bom查询工单替代料
     *
     * @param tenantId 租户ID
     * @param bomComponentId Bom组件ID
     * @param bomId BomID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/23 09:50:30
     * @return java.util.List<java.lang.String>
     */
    List<String> getWoSubstituteByPrimary(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId,
                                          @Param("bomId") String bomId);

    /**
     * 根据主键料查询全局替代料
     *
     * @param tenantId
     * @param materialId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/23 09:59:13
     * @return java.util.List<java.lang.String>
     */
    List<String> getGlobalSubstituteByPrimary(@Param("tenantId") Long tenantId, @Param("materialId") String materialId);

    /**
     * 根据库位Id查询子层库位
     *
     * @param tenantId 租户ID
     * @param locatorId 库位Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/7 11:32:17
     * @return java.util.List<java.lang.String>
     */
    List<String> getSubLocatorByLocatorId(@Param("tenantId") Long tenantId, @Param("locatorId") String locatorId);

    /**
     * 根据条码查询不良虚拟库位
     *
     * @param tenantId      租户ID
     * @param materialLotId 条码ID
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/7 14:23:41
     */
    List<String> getNcStorageByLocatorId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * COS条码信息
     *
     * @param tenantId      租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.vo.HmeNcDisposePlatformVO7
     * @author sanfeng.zhang@hand-china.com 2021/3/1 15:28
     */
    HmeNcDisposePlatformVO7 queryMaterialLotInfo(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 条码未关闭的不良记录数量
     *
     * @param tenantId
     * @param materialLotId
     * @return java.math.BigDecimal
     * @author sanfeng.zhang@hand-china.com 2021/3/1 15:54
     */
    BigDecimal queryOpenNcRecordQty(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 批量更新装载信息
     *
     * @param tenantId
     * @param userId
     * @param materialLotLoadList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/2 9:55
     */
    void batchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("materialLotLoadList") List<HmeMaterialLotLoad> materialLotLoadList);

    /**
     * 根据EO和物料查询装配清单信息
     *
     * @param tenantId
     * @param eoId
     * @param materialId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/3/3 19:53
     */
    List<String> queryBomComponentByEoAndCode(@Param("tenantId") Long tenantId, @Param("eoId") String eoId, @Param("materialId") String materialId);

    /**
     * 根据序列找未关闭的记录
     *
     * @param tenantId
     * @param loadSequence
     * @author sanfeng.zhang@hand-china.com 2021/3/4 18:34
     * @return java.util.List<java.lang.String>
     */
    List<String> queryNcRecordByLoadSequence(@Param("tenantId") Long tenantId, @Param("loadSequence") String loadSequence);

    /**
     * 根据EO和条码查询装载信息
     *
     * @param tenantId
     * @param eoId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2021/3/4 21:10
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> queryMaterialLotLoadInfo(@Param("tenantId") Long tenantId, @Param("eoId") String eoId, @Param("materialLotId") String materialLotId);

    /**
     * 根据EO和条码查询不良装载信息
     *
     * @param tenantId
     * @param eoId
     * @param materialLotId
     * @param loadSequence
     * @author sanfeng.zhang@hand-china.com 2021/3/4 21:17
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotNcLoad>
     */
    List<HmeMaterialLotNcLoad> queryMaterialLotNcLoadInfo(@Param("tenantId") Long tenantId, @Param("eoId") String eoId, @Param("materialLotId") String materialLotId, @Param("loadSequence") String loadSequence);

    /**
     * 数据采集数据查询
     * 
     * @param tenantId 租户ID
     * @param eoId eoID
     * @param workcellId 工位ID
     * @param tagCode 数据采集项编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/18 11:24:18 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcDisposePlatformVO8>
     */
    List<HmeNcDisposePlatformVO8> eoJobDataRecordQuery(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                                                       @Param("workcellId") String workcellId, @Param("tagCode") String tagCode);

    /**
     * 查询最近的job数据
     * @param tenantId
     * @param eoId
     * @param workcellId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/5/25
     */
    String queryLatestJobIdByEoAndWorkcell(@Param("tenantId") Long tenantId, @Param("eoId") String eoId, @Param("workcellId") String workcellId);

    /**
     * 当前工位
     *
     * @param tenantId
     * @param materialLotId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/11/12
     */
    String queryCurrentWorkcellId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 查询工单
     *
     * @param tenantId
     * @param eoIdList
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/11/12
     */
    List<HmeNcDisposePlatformVO10> queryWoOfEo(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);
}
