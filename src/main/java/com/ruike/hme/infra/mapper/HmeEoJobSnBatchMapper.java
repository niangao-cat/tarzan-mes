package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.order.domain.entity.MtEoRouter;

/**
 * 批量工序作业平台-SN作业Mapper
 *
 * @author penglin.sui@hand-china.com 2020-11-19 17:11:39
 */
public interface HmeEoJobSnBatchMapper {
    /**
     * 查询工单组件实绩
     *
     * @param tenantId 租户id
     * @param workOrderId 工单ID
     * @param materialIdList 物料ID集合
     * @param bomComponentIdList 组件ID集合
     * @return List<MtWorkOrderComponentActual>
     */
    List<MtWorkOrderComponentActual> selectWorkOrderComponentActual(@Param("tenantId") Long tenantId,
                                                                    @Param("workOrderId") String workOrderId,
                                                                    @Param("materialIdList") List<String> materialIdList,
                                                                    @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     * 查询班次
     *
     * @param tenantId 租户id
     * @param jobId 工序作业ID
     * @return List<MtWorkOrderComponentActual>
     */
    HmeEoJobSnBatchVO11 selectJobShift(@Param("tenantId") Long tenantId,
                                       @Param("jobId") String jobId);

    /**
     *
     * @Description 查询EO组件实绩
     *
     * @author penglin.sui
     * @date 2020/11/20 10:23
     * @param tenantId 租户ID
     * @param bomComponentIdList 组件ID
     * @return java.util.List<MtEoComponentActual>
     *
     */
    List<MtEoComponentActual> selectEoComponentActual(@Param("tenantId") Long tenantId,
                                                      @Param("bomComponentIdList") List<String> bomComponentIdList,
                                                      @Param("materialIdList") List<String> materialIdList,
                                                      @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 查询WO组件实绩
     *
     * @author penglin.sui
     * @date 2020/11/20 10:23
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialIdList 物料ID
     * @param workOrderIdList 工单ID
     * @return java.util.List<MtWorkOrderComponentActual>
     *
     */
    List<MtWorkOrderComponentActual> selectWoComponentActual(@Param("tenantId") Long tenantId,
                                                             @Param("operationId") String operationId,
                                                             @Param("materialIdList") List<String> materialIdList,
                                                             @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     *
     * @Description 组件清单中要投料的数据
     *
     * @author penglin.sui
     * @date 2020/11/17 14:56
     * @param tenantId 租户ID
     * @param dto 投料信息查询参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO4> selectComponentRelease(@Param("tenantId") Long tenantId,
                                                    @Param("dto") HmeEoJobSnBatchDTO dto);

    /**
     *
     * @Description 工单组件清单
     *
     * @author penglin.sui
     * @date 2020/11/17 14:56
     * @param tenantId 租户ID
     * @param dto 投料信息查询参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO4> selectWoComponent(@Param("tenantId") Long tenantId,
                                               @Param("dto") HmeEoJobSnBatchDTO dto);

    /**
     *
     * @Description 替代组下的替代料
     *
     * @author penglin.sui
     * @date 2020/11/17 14:56
     * @param tenantId 租户ID
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO5> selectComponentSubstitute(@Param("tenantId") Long tenantId,
                                                       @Param("materialIdList") List<String> materialIdList,
                                                       @Param("bomSubstituteGroupIdList") List<String> bomSubstituteGroupIdList);

    /**
     *
     * @Description 全局替代料要投料的数据
     *
     * @author penglin.sui
     * @date 2020/11/17 16:03
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO4> selectSubstituteRelease(@Param("tenantId") Long tenantId,
                                                     @Param("siteId") String siteId,
                                                     @Param("materialIdList") List<String> materialIdList);

    /**
     *
     * @Description 工单组件全局替代料
     *
     * @author penglin.sui
     * @date 2020/11/17 16:03
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO4> selectWoSubstitute(@Param("tenantId") Long tenantId,
                                                @Param("siteId") String siteId,
                                                @Param("materialIdList") List<String> materialIdList);

    /**
     *
     * @Description 组件清单已投数量
     *
     * @author penglin.sui
     * @date 2020/11/17 19:29
     * @param tenantId 租户ID
     * @param eoIdList EO ID
     * @param routerStepIdList 工艺步骤ID
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO7>
     *
     */
    List<MtEoComponentActual> selectComponentAssemble(@Param("tenantId") Long tenantId,
                                                      @Param("eoIdList") List<String> eoIdList,
                                                      @Param("routerStepIdList") List<String> routerStepIdList,
                                                      @Param("materialIdList") List<String> materialIdList);

    /**
     *
     * @Description 组件清单已投数量
     *
     * @author penglin.sui
     * @date 2020/11/17 19:29
     * @param tenantId 租户ID
     * @param eoIdList EO ID
     * @param materialIdList 物料ID
     * @param bomComponentIdList 组件ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO7> selectSubstituteAssemble(@Param("tenantId") Long tenantId,
                                                      @Param("eoIdList") List<String> eoIdList,
                                                      @Param("materialIdList") List<String> materialIdList,
                                                      @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     *
     * @Description 组件清单绑定的条码
     *
     * @author penglin.sui
     * @date 2020/11/17 19:29
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobIdList 工序作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO6>
     *
     */
    List<HmeEoJobSnBatchVO6> selectComponentMaterialLot(@Param("tenantId") Long tenantId,
                                                        @Param("workcellId") String workcellId,
                                                        @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 虚拟件组件批次/时效物料绑定的条码
     *
     * @author penglin.sui
     * @date 2021/01/04 0:17
     * @param tenantId 租户ID
     * @param materialIdList 物料ID
     * @param jobIdList 工序作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO6>
     *
     */
    List<HmeEoJobSnBatchVO6> selectLotTimeVirtualComponentMaterialLot(@Param("tenantId") Long tenantId,
                                                                      @Param("materialIdList") List<String> materialIdList,
                                                                      @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 虚拟件组件序列物料绑定的条码
     *
     * @author penglin.sui
     * @date 2021/01/04 0:18
     * @param tenantId 租户ID
     * @param materialIdList 物料ID
     * @param jobIdList 工序作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnBatchVO6>
     *
     */
    List<HmeEoJobSnBatchVO6> selectSnVirtualComponentMaterialLot(@Param("tenantId") Long tenantId,
                                                                 @Param("materialIdList") List<String> materialIdList,
                                                                 @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 虚拟件组件投料记录
     *
     * @author penglin.sui
     * @date 2020/11/22 12:12
     * @param tenantId 租户ID
     * @param materialIdList 批次/时效物料关系表ID
     * @param jobIdList 工序作业ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial>
     *
     */
    List<HmeEoJobSnLotMaterial> selectVirtualComponent(@Param("tenantId") Long tenantId,
                                                       @Param("materialIdList") List<String> materialIdList,
                                                       @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 虚拟件组件投料记录
     *
     * @author penglin.sui
     * @date 2020/11/22 12:12
     * @param tenantId 租户ID
     * @param snMaterialIdList 序列物料关系表ID
     * @param jobIdList 工序作业ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobMaterial>
     *
     */
    List<HmeEoJobMaterial> selectSnVirtualComponent(@Param("tenantId") Long tenantId,
                                                    @Param("snMaterialIdList") List<String> snMaterialIdList,
                                                    @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 虚拟件投料记录
     *
     * @author penglin.sui
     * @date 2020/11/24 17:41
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param eoIdList EO ID
     * @param jobIdList 工序作业ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSnBatchVO15>
     *
     */
    List<HmeEoJobSnBatchVO15> selectReleased(@Param("tenantId") Long tenantId,
                                             @Param("workcellId") String workcellId,
                                             @Param("eoIdList") List<String> eoIdList,
                                             @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 退料条码
     *
     * @author penglin.sui
     * @date 2020/12/12 11:08
     * @param tenantId 租户ID
     * @param materialLotCode 条码CODE
     * @return com.ruike.hme.domain.vo.HmeEoJobSnBatchVO21
     *
     */
    HmeEoJobSnBatchVO21 selectBackMaterialLot(@Param("tenantId") Long tenantId,
                                              @Param("materialLotCode") String materialLotCode);

    /**
     *
     * @Description 根据区域库位查询库存库位
     *
     * @author penglin.sui
     * @date 2020/12/12 17:46
     * @param tenantId 租户ID
     * @param areaLocatorCode 区域库位CODE
     * @return com.ruike.hme.domain.vo.HmeEoJobSnBatchVO21
     *
     */
    HmeEoJobSnBatchVO21 selectInventoryLocator(@Param("tenantId") Long tenantId,
                                               @Param("areaLocatorCode") String areaLocatorCode);

    /**
     *
     * @Description 查询EO下最新工艺路线装配清单
     *
     * @author penglin.sui
     * @date 2021/01/07 10:43
     * @param tenantId 租户ID
     * @param eoId 执行作业ID
     * @return com.ruike.hme.domain.entity.HmeEoRouterBomRel
     *
     */
    HmeEoRouterBomRel selectLastestEoRouterBomRel(@Param("tenantId") Long tenantId,
                                                  @Param("eoId") String eoId);

    /**
     *
     * @Description 查询EO工艺路线
     *
     * @author penglin.sui
     * @date 2021/01/07 11:10
     * @param tenantId 租户ID
     * @param eoId 执行作业ID
     * @return tarzan.order.domain.entity.MtEoRouter
     *
     */
    MtEoRouter selectEoRouter(@Param("tenantId") Long tenantId,
                              @Param("eoId") String eoId);

    /**
     * 查询泵浦源组合信息
     *
     * @param tenantId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2021/8/31 7:46
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpCombVO>
     */
    List<HmePumpCombVO> queryPumpCombListByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 查询泵浦源组合的电压和功率
     *
     * @param tenantId
     * @param cmbJobIds
     * @author sanfeng.zhang@hand-china.com 2021/8/31 7:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmePumpTagVO>
     */
    List<HmePumpTagVO> queryPumpTagRecordResult(@Param("tenantId") Long tenantId, @Param("cmbJobIds") List<String> cmbJobIds);

    /**
     * 查询泵浦源头信息
     *
     * @param tenantId
     * @param materialLotId
     * @param eoId
     * @author sanfeng.zhang@hand-china.com 2021/8/31 15:43
     * @return java.util.List<com.ruike.hme.domain.entity.HmePumpModPositionHeader>
     */
    List<HmePumpModPositionHeader> queryPumpPositionHeaderByBackCodeAndEoId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId, @Param("eoId") String eoId);
}
