package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.material.domain.entity.MtMaterialSite;

import java.util.Date;
import java.util.List;

/**
 * 工序作业平台-SN作业Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 00:04:39
 */
public interface HmeEoJobSnMapper extends BaseMapper<HmeEoJobSn> {
    /**
     * 工序在制查询
     *
     * @param tenantId         租户id
     * @param productionLineId 产品线ID
     * @param workcellId       工序ID
     * @return List<HmeEoJobWipVO2>
     */
    List<HmeEoJobWipVO2> queryEoWorkingByWkc(@Param("tenantId") Long tenantId,
                                             @Param("productionLineId") String productionLineId,
                                             @Param("workcellId") String workcellId);

    /**
     * 工序完工装箱-查询工位信息
     *
     * @param tenantId 租户id
     * @param dto      查询参数
     * @return List<HmeCompleteBoxingVO2>
     */
    List<HmeCompleteBoxingVO2> queryForWorkcell(@Param("tenantId") Long tenantId,
                                                @Param("dto") HmeCompleteBoxingVO dto);

    /**
     * 工序完工装箱-根据装箱号查询物料信息
     *
     * @param tenantId      租户id
     * @param materialLotId 物料批ID
     * @return HmeEoJobSnVO5
     */
    HmeEoJobSnVO5 queryMaterialByLotId(@Param("tenantId") Long tenantId,
                                       @Param("materialLotId") String materialLotId);

    /**
     * 工序完工装箱-根据装箱号查询物料信息
     *
     * @param tenantId 租户id
     * @param dto 传入参数
     * @return HmeEoJobSnVO5
     */
    List<HmeEoJobSnVO5> queryMaterialByLot(@Param("tenantId") Long tenantId,
                                     @Param("dto") HmeEoJobSnBatchVO2 dto);

    /**
     *
     * @Description 工序作业平台进站查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/12/14 13:55
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @param siteId 站点ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO5
     *
     */
    HmeEoJobSnVO5 queryMaterialLotInfoForSingle(@Param("tenantId") Long tenantId,
                                                @Param("materialLotId") String materialLotId,
                                                @Param("siteId") String siteId);

    /**
     *
     * @Description 自制件返修进站查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/12/15 11:07
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @param siteId 站点ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO5
     *
     */
    HmeEoJobSnVO5 queryMaterialLotInfoForRework(@Param("tenantId") Long tenantId,
                                                @Param("materialLotId") String materialLotId,
                                                @Param("siteId") String siteId);

    /**
     *
     * @Description 时效返修批量查询条码信息
     *
     * @author yuchao.wang
     * @date 2021/1/27 10:48
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialLotIdList 条码ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnVO5>
     *
     */
    List<HmeEoJobSnVO5> batchQueryMaterialLotInfoForRework(@Param("tenantId") Long tenantId,
                                                           @Param("siteId") String siteId,
                                                           @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     *
     * @Description 时效作业平台根据条码ID批量查询物料信息
     *
     * @author yuchao.wang
     * @date 2020/11/3 10:48
     * @param tenantId 租户id
     * @param materialLotIdList 物料批ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO5
     *
     */
    List<HmeEoJobSnVO5> batchQueryMaterialLotByIdsForTime(@Param("tenantId") Long tenantId,
                                                          @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     *
     * @Description 时效作业平台根据条码ID查询物料信息
     *
     * @author yuchao.wang
     * @date 2021/1/26 17:37
     * @param tenantId 租户id
     * @param materialLotId 物料批ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO5
     *
     */
    HmeEoJobSnVO5 batchQueryMaterialLotByIdsForTimeRework(@Param("tenantId") Long tenantId,
                                                          @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 时效作业平台根据条码ID查询物料信息
     *
     * @author yuchao.wang
     * @date 2021/1/26 17:37
     * @param tenantId 租户id
     * @param materialLotId 物料批ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO5
     *
     */
    HmeEoJobSnVO5 batchQueryMaterialLotByIdsForTimeRework2(@Param("tenantId") Long tenantId,
                                                          @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 查询eo下当下步骤信息
     *
     * @author yuchao.wang
     * @date 2020/11/3 15:48
     * @param tenantId 租户ID
     * @param eoIdList eoId
     * @param operationIdList 工艺ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO3>
     *
     */
    List<HmeRouterStepVO3> batchQueryCurrentRouterStep(@Param("tenantId") Long tenantId,
                                                       @Param("eoIdList") List<String> eoIdList,
                                                       @Param("operationIdList") List<String> operationIdList);

    /**
     *
     * @Description 时效作业-查询eo下当下步骤信息
     *
     * @author yuchao.wang
     * @date 2020/11/3 15:48
     * @param tenantId 租户ID
     * @param eoIdList eoId
     * @param operationId 工艺ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO3>
     *
     */
    List<HmeRouterStepVO3> batchQueryCurrentRouterStepForTime(@Param("tenantId") Long tenantId,
                                                              @Param("eoIdList") List<String> eoIdList,
                                                              @Param("operationId") String operationId);

    /**
     * 查询工单
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @param userId   用户ID
     * @return List<HmeWorkOrderVO>
     */
    List<HmeWorkOrderVO> workOrderQuery(@Param("tenantId") Long tenantId,
                                        @Param("userId") Long userId,
                                        @Param("dto") HmeWorkOrderVO dto);

    /**
     * 查询预装物料
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @param siteId   地点ID
     * @return List<HmePrepareMaterialVO>
     */
    List<HmePrepareMaterialVO> materialQuery(@Param("tenantId") Long tenantId,
                                             @Param("siteId") String siteId,
                                             @Param("dto") HmeWorkOrderVO dto);

    /**
     * 物料预装平台-已预装的物料数量查询
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return HmePrepareMaterialVO
     */
    HmePrepareMaterialVO selectPreparedQtyByMaterialId(@Param("tenantId") Long tenantId,
                                                       @Param("dto") HmeWorkOrderVO dto);

    /**
     * 预装序列物料投料查询
     *
     * @param tenantId   租户ID
     * @param siteId     地点ID
     * @param materialCode 物料编码
     * @return List<HmePrepareMaterialVO>
     */
    List<HmePrepareMaterialVO> prepareEoJobMaterialQuery(@Param("tenantId") Long tenantId,
                                                         @Param("siteId") String siteId,
                                                         @Param("workOrderId") String workOrderId,
                                                         @Param("materialCode") String materialCode);

    /**
     * 预装批次物料投料查询
     *
     * @param tenantId   租户ID
     * @param siteId     地点ID
     * @param materialCode 物料编码
     * @return List<HmePrepareMaterialVO>
     */
    List<HmePrepareMaterialVO> prepareEoJobLotMaterialQuery(@Param("tenantId") Long tenantId,
                                                            @Param("siteId") String siteId,
                                                            @Param("workOrderId") String workOrderId,
                                                            @Param("materialCode") String materialCode);

    /**
     * 预装时效物料投料查询
     *
     * @param tenantId   租户ID
     * @param siteId     地点ID
     * @param materialCode 物料编码
     * @return List<HmePrepareMaterialVO>
     */
    List<HmePrepareMaterialVO> prepareEoJobTimeMaterialQuery(@Param("tenantId") Long tenantId,
                                                             @Param("siteId") String siteId,
                                                             @Param("workOrderId") String workOrderId,
                                                             @Param("materialCode") String materialCode);

    /**
     * 预装时效物料投料查询
     *
     * @param tenantId   租户ID
     * @param siteId     地点ID
     * @param workOrderId 工单ID
     * @param materialCode 物料编码
     * @return List<HmePrepareMaterialVO>
     */
    List<HmePrepareMaterialVO> prepareEoJobMaterialQuery2(@Param("tenantId") Long tenantId,
                                                          @Param("siteId") String siteId,
                                                          @Param("workOrderId") String workOrderId,
                                                          @Param("materialCode") String materialCode);

    /**
     * 根据EO获取当前准备加工的工艺步骤
     *
     * @param tenantId 租户ID
     * @param eoId     EO
     * @return HmeRouterStepVO
     */
    HmeRouterStepVO selectNearStepByEoId(@Param("tenantId") Long tenantId,
                                         @Param("eoId") String eoId);

    /**
     *
     * @Description 根据EO批量查询所有工艺步骤
     *
     * @author yuchao.wang
     * @date 2020/10/28 16:38
     * @param tenantId 租户ID
     * @param eoIdList EO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO>
     *
     */
    List<HmeRouterStepVO> batchSelectStepByEoIds(@Param("tenantId") Long tenantId,
                                                 @Param("eoIdList") List<String> eoIdList);

    /**
     * 根据EO获取当前准备加工的工艺步骤
     *
     * @param tenantId 租户ID
     * @param eoId     EO
     * @return HmeRouterStepVO
     */
    HmeRouterStepVO selectNormalStepByEoId(@Param("tenantId") Long tenantId,
                                           @Param("eoId") String eoId);

    /**
     * 根据EO获取当前准备加工的工艺步骤
     *
     * @param tenantId 租户ID
     * @param eoIdList EO
     * @return HmeRouterStepVO
     */
    List<HmeRouterStepVO> batchSelectNormalStepByEoIds(@Param("tenantId") Long tenantId,
                                                       @Param("eoIdList") List<String> eoIdList);

    /**
     * 时效工序作业平台-根据SN号查询SN容器信息
     * @param tenantId 租户id
     * @param materialLotIds 物料批ID列表
     * @return HmeEoJobTimeSnVO3
     */
    List<HmeEoJobTimeSnVO3> selectSnLineByMaterialLotIds(@Param("tenantId") Long tenantId,
                                                         @Param("materialLotIds") List<String> materialLotIds);
    /**
     * 时效工序作业平台-根据工位获取设备信息
     * @param tenantId 租户id
     * @param workcellId 工位Id
     * @return HmeEquipmentVO2
     */
    List<HmeEquipmentVO2> selectEquipmentByWorkcellId(@Param("tenantId") Long tenantId,
                                                      @Param("workcellId") String workcellId);

    /**
     * 时效工序作业平台-获取最新的eo-sn作业记录
     * @param tenantId 租户id
     * @param hmeEoJobSn eo-sn作业记录
     * @return HmeEoJobSn
     */
    HmeEoJobSnVO selectLastestJobSn(@Param("tenantId") Long tenantId, @Param("hmeEoJobSn") HmeEoJobSn hmeEoJobSn);

    /**
     * 时效工序作业平台-获取最新的eo-sn作业记录
     * @param tenantId 租户id
     * @param eoId eoId
     * @return HmeEoJobSn
     */
    HmeEoJobSnVO selectLastestJobSnOfEo(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 时效工序作业平台-获取最新的eo-sn作业记录
     * @param tenantId 租户id
     * @param eoIds eoIds
     * @return HmeEoJobSn
     */
    List<HmeEoJobSnVO> selectLastestJobSnOfEoBatch(@Param("tenantId") Long tenantId, @Param("eoIds") List<String> eoIds);

    /**
     * 获取最新未出站sn作业记录
     * @param tenantId 租户id
     * @param hmeEoJobSn sn作业记录
     * @return HmeEoJobSn
     */
    HmeEoJobSn selectLastestJobSnOfEoWkc(@Param("tenantId") Long tenantId, @Param("hmeEoJobSn") HmeEoJobSn hmeEoJobSn);

    /**
     *
     * @Description 根据material_lot_id+工艺ID+'作业平台类型等于COS_ FETCH _IN'，查询表中是否存在出站时间为空的记录
     *
     * @author yuchao.wang
     * @date 2020/8/18 11:19
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param operationId 工艺ID
     * @return java.lang.Integer
     *
     */
    Integer checkGettingChipFlag(@Param("tenantId") Long tenantId,
                                 @Param("materialLotId") String materialLotId,
                                 @Param("operationId") String operationId);

    /**
     *
     * @Description 根据material_lot_id+工艺ID+'作业平台类型等于CHIP_NUM_ENTERING'，查询表中是否存在出站时间为空的记录
     *
     * @author yifan.xiong
     * @date 2020-9-1 18:42:25
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @param operationId 工艺ID
     * @return java.lang.Integer
     *
     */
    Integer checkChipInputFlag(@Param("tenantId") Long tenantId,
                                 @Param("materialLotId") String materialLotId,
                                 @Param("operationId") String operationId);

    /**
     *
     * @Description 出站确认查询来源条码
     *
     * @author yuchao.wang
     * @date 2020/8/18 20:28
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @param workcellId 工位ID
     * @param operationId 工艺ID
     * @return java.lang.String
     *
     */
    String querySourceMaterialLotIdForSiteOut(@Param("tenantId") Long tenantId,
                                              @Param("workOrderId") String workOrderId,
                                              @Param("workcellId") String workcellId,
                                              @Param("operationId") String operationId);

    /**
     *
     * @Description 根据来源作业ID查询未完工的出站作业
     *
     * @author yuchao.wang
     * @date 2020/8/19 16:40
     * @param tenantId 租户ID
     * @param sourceJobId 来源作业ID
     * @param jobType 作业类型
     * @return java.lang.String
     *
     */
    List<HmeEoJobSn> queryEoJobSnBySourceJobId(@Param("tenantId") Long tenantId,
                                               @Param("sourceJobId") String sourceJobId,
                                               @Param("jobType") String jobType);
    /**
     *
     * @Description 来料目检查询最近一条未出站的数据
     *
     * @author yuchao.wang
     * @date 2020/8/24 15:25
     * @param tenantId 租户ID
     * @param hmeEoJobSn 参数
     * @return java.lang.String
     *
     */
    HmeEoJobSn queryLastEoJobId(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSn hmeEoJobSn);

    /**
     *
     * @Description 根据eoJobSnId查询物料批信息
     *
     * @author yuchao.wang
     * @date 2020/8/25 9:22
     * @param tenantId 租户ID
     * @param sourceJobId sourceJobId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO>
     *
     */
    List<HmeMaterialLotVO> queryMaterialLotByJobId(@Param("tenantId") Long tenantId, @Param("sourceJobId") String sourceJobId);

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     * @param domains 新增数据列表
     * @return void
     *
     */
    void batchInsert(@Param("domains") List<HmeEoJobSn> domains);

    /**
     *
     * @Description 根据ID查询是否有已经出站的数据
     *
     * @author yuchao.wang
     * @date 2020/8/31 18:30
     * @param tenantId 租户ID
     * @param jobId ID
     * @return java.lang.Integer
     *
     */
    Integer checkSiteOutById(@Param("tenantId") Long tenantId,
                             @Param("jobId") String jobId);

    /**
     *
     * @param tenantId 租户id
     * @param workcellId 工位Id
     * @return List<HmeCosChipInputVO>
     */
    List<HmeCosChipInputVO> prepareCosInputQuery(@Param("tenantId") Long tenantId,
                                                 @Param("workcellId") String workcellId,
                                                 @Param("materialLotId") String materialLotId,
                                                 @Param("operationId") String operationId);

    /**
     *
     * @Description 首序作业平台-工单号查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 13:54
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkOrderVO>
     *
     */
    List<HmeWorkOrderVO> workOrderQueryForFirst(@Param("tenantId") Long tenantId,
                                                @Param("dto") HmeWoLovQueryDTO dto);

    /**
     *
     * @Description 首序作业平台-EO查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 16:30
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     *
     */
    List<HmeEoVO> eoQueryForFirst(@Param("tenantId") Long tenantId,
                                  @Param("dto") HmeEoLovQueryDTO dto);

    /**
     *
     * @Description 首序作业平台-物料批查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 19:51
     * @param tenantId 租户ID
     * @param materialLotIds 物料批ID集合
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO2>
     *
     */
    List<HmeMaterialLotVO2> materialLotLovQueryForFirst(@Param("tenantId") Long tenantId,
                                                        @Param("materialLotIds") List<String> materialLotIds,
                                                        @Param("dto") HmeMaterialLotLovQueryDTO dto);

    /**
     *
     * @Description 根据EOID查询EO编号及相关首序信息
     *
     * @author yuchao.wang
     * @date 2020/9/3 14:41
     * @param tenantId 租户ID
     * @param eoId eoId
     * @return com.ruike.hme.domain.vo.HmeEoVO2
     *
     */
    HmeEoVO2 queryEoActualByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);


    /**
     *
     * @Description 首序SN升级-查询要更新的EoJobSn
     *
     * @author yuchao.wang
     * @date 2020/9/3 18:46
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.lang.String
     *
     */
    String queryJobIdFirstProcessSnUpgrade(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobFirstProcessUpgradeSnDTO dto);

    /**
     *
     * @Description 根据在制记录查询未打印的数量
     *
     * @author yuchao.wang
     * @date 2020/9/7 18:58
     * @param tenantId 租户ID
     * @param sourceJobId 在制记录ID
     * @return java.lang.Long
     *
     */
    Long queryNotPrintQtyBySourceJobId(@Param("tenantId") Long tenantId,
                                       @Param("sourceJobId") String sourceJobId);

    /**
     *
     * @Description 查询未完工的其他工艺步骤对应的工段
     *
     * @author yuchao.wang
     * @date 2020/9/21 12:24
     * @param tenantId 租户ID
     * @param eoId EOID
     * @param siteId 站点ID
     * @return java.util.List<java.lang.String>
     *
     */
    List<HmeEoJobSnDTO3> queryUnfinishedSection(@Param("tenantId") Long tenantId,
                                        @Param("eoId") String eoId,
                                        @Param("siteId") String siteId);

    /**
     *
     * @Description 批量查询未完工的其他工艺步骤对应的工段
     *
     * @author yuchao.wang
     * @date 2020/10/22 18:07
     * @param tenantId 租户ID
     * @param eoIdList EOID
     * @param siteId 站点ID
     * @return java.util.List<java.lang.String>
     *
     */
    List<HmeEoJobSnDTO3> batchQueryUnfinishedSection(@Param("tenantId") Long tenantId,
                                                 @Param("eoIdList") List<String> eoIdList,
                                                 @Param("siteId") String siteId);

    /**
     * 获取最新未出站sn作业记录
     * @param tenantId 租户id
     * @param materialLotId 条码ID
     * @return HmeEoJobSn
     */
    MtNcRecord selectLastestNcRecord(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 根据物料批ID查询未出站的EoJobSn
     *
     * @author yuchao.wang
     * @date 2020/9/27 14:06
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    List<HmeEoJobSn> queryEoJobSnByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 查询当前EO关联的工单类型
     *
     * @author yuchao.wang
     * @date 2020/9/28 14:41
     * @param tenantId 租户ID
     * @param eoId eoId
     * @return java.lang.String
     *
     */
    String queryWorkOrderTypeByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     *
     * @Description 查询EO关联的工单类型
     *
     * @author penglin.sui
     * @date 2020/11/12 21:25
     * @param tenantId 租户ID
     * @param eoIdList eoIdList
     * @return List<HmeEoJobSnBatchVO3>
     *
     */
    List<HmeEoJobSnBatchVO3> batchQueryWorkOrderTypeByEoId(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 首序作业平台-查询工单 站点下默认EO
     *
     * @author yuchao.wang
     * @date 2020/10/9 23:05
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoVO
     *
     */
    HmeEoVO defaultEoQueryForFirst(@Param("tenantId") Long tenantId,
                                   @Param("dto") HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 库位LOV
     *
     * @author penglin.sui
     * @date 2020/10/10 11:41
     * @param tenantId 租户ID
     * @param dto 传入参数
     * @return List<HmeModLocatorVO>
     *
     */
    List<HmeModLocatorVO> queryLocator(@Param("tenantId") Long tenantId, @Param("dto") HmeModLocatorVO dto);

    /**
     *
     * @Description 库位现有量
     *
     * @author penglin.sui
     * @date 2020/10/10 11:40
     * @param tenantId 租户ID
     * @param dto 传入参数
     * @return List<HmeLocatorOnhandQuantityVO>
     *
     */
    List<HmeLocatorOnhandQuantityVO> queryLocatorOnhandQuantity(@Param("tenantId") Long tenantId, @Param("dto") HmeLocatorOnhandQuantityDTO dto);

    /**
     *
     * @Description 虚拟件组件
     *
     * @author penglin.sui
     * @date 2020/10/10 11:40
     * @param tenantId 租户ID
     * @param bomComponentIdList 组件ID
     * @return List<String>
     *
     */
    List<String> queryVirtualBomComponent(@Param("tenantId") Long tenantId,@Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     *
     * @Description 获取物料站点属性
     *
     * @author yuchao.wang
     * @date 2020/10/9 23:05
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialId 物料ID
     * @return MtMaterialBasic
     *
     */
    MtMaterialBasic queryMaterialBasic(@Param("tenantId") Long tenantId,
                                       @Param("siteId") String siteId,
                                       @Param("materialId") String materialId);

    /**
     *
     * @Description 反冲料
     *
     * @author penglin.sui
     * @date 2020/10/10 11:40
     * @param tenantId 租户ID
     * @param dto 传入参数
     * @return List<String>
     *
     */
    List<HmeBackFlushVO> queryBackFlush(@Param("tenantId") Long tenantId,@Param("dto") HmeBackFlushDTO dto);

    /**
     *
     * @Description 获取物料站点
     *
     * @author penglin.sui
     * @date 2020/10/22 10:25
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param materialIdList 物料ID
     * @return List<MtMaterialSite>
     *
     */
    List<MtMaterialSite> queryMaterialSite(@Param("tenantId") Long tenantId,
                                           @Param("siteId") String siteId,
                                           @Param("materialIdList") List<String> materialIdList);

    /**
     *
     * @Description 获取扩展属性
     *
     * @author penglin.sui
     * @date 2020/10/22 10:53
     * @param tenantId 租户ID
     * @param tableName 表名
     * @param keyId 主键ID
     * @param keyIdList 主键ID
     * @param attrNameList 属性名
     * @return List<MtExtendAttrVO1>
     *
     */
    List<MtExtendAttrVO1> queryExtendAttr(@Param("tenantId") Long tenantId,
                                          @Param("tableName") String tableName,
                                          @Param("keyId") String keyId,
                                          @Param("keyIdList") List<String> keyIdList,
                                          @Param("attrNameList") List<String> attrNameList);

    /**
     *
     * @Description 获取物料
     *
     * @author penglin.sui
     * @date 2020/10/22 10:25
     * @param tenantId 租户ID
     * @param materialIdList 物料ID
     * @return List<MtMaterialSite>
     *
     */
    List<HmeEoJobSnMaterialUomVO> queryMaterialUom(@Param("tenantId") Long tenantId,
                                                   @Param("materialIdList") List<String> materialIdList);

    /**
     * 改造查询空串
     *
     * @Author peng.yuan
     * @Date 2020/10/28 20:06
     * @param mtEoStepWip :租户ID
     * @param mtEoStepWip :
     * @return java.util.List<tarzan.actual.domain.entity.MtEoStepWip>
     */
    List<MtEoStepWip> selectForEoWkcAndStepWipQuery(@Param(value = "tenantId") Long tenantId,
                                                    @Param("dtoList") List<MtEoStepWip> mtEoStepWip);

    /**
     *
     * @Description 查询工位下是否有某作业类型未出站数据 true:有未出站数据 false:没有
     *
     * @author yuchao.wang
     * @date 2020/10/26 15:07
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobType 作业类型
     * @return boolean
     *
     */
    Integer checkNotSiteOutByWkcId(@Param("tenantId") Long tenantId,
                                   @Param("workcellId") String workcellId,
                                   @Param("jobType") String jobType);

    /**
     *
     * @Description 查询当前EO关联的工单类型
     *
     * @author penglin.sui
     * @date 2020/10/12 20:18
     * @param tenantId 租户ID
     * @param dto 传入参数
     * @return HmeEoJobSn
     *
     */
    HmeEoJobSn queryEoJobSn(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSn dto);

    /**
     *
     * @Description 根据ID批量查询EO信息
     *
     * @author yuchao.wang
     * @date 2020/11/5 16:53
     * @param tenantId 租户ID
     * @param eoIdList eoIdList
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeEoVO4>
     *
     */
    List<HmeEoVO4> batchQueryEoInfoById(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 根据ID批量查询EO信息
     *
     * @author yuchao.wang
     * @date 2020/11/5 16:53
     * @param tenantId 租户ID
     * @param eoIdList eoIdList
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeEoVO4>
     *
     */
    List<HmeEoVO4> batchQueryEoInfoWithComponentById(@Param("tenantId") Long tenantId, @Param("eoStepId") String eoStepId, @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 指定工艺路线返修-根据ID查询EO信息
     *
     * @author yuchao.wang
     * @date 2021/1/4 15:59
     * @param tenantId 租户ID
     * @param eoStepId eoStepId
     * @param eoId eoId
     * @param bomId bomId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO4>
     *
     */
    List<HmeEoVO4> batchQueryEoInfoWithCompByIdForDesignedRework(@Param("tenantId") Long tenantId,
                                                                 @Param("eoStepId") String eoStepId,
                                                                 @Param("eoId") String eoId,
                                                                 @Param("bomId") String bomId);

    /**
     *
     * @Description 根据ID批量查询WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/5 18:49
     * @param tenantId 租户ID
     * @param woIdList woIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkOrderVO2>
     *
     */
    List<HmeWorkOrderVO2> batchQueryWoInfoById(@Param("tenantId") Long tenantId, @Param("woIdList") List<String> woIdList);

    /**
     *
     * @Description 根据ID批量查询WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/5 18:49
     * @param tenantId 租户ID
     * @param woIdList woIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkOrderVO2>
     *
     */
    List<HmeWorkOrderVO2> batchQueryWoInfoWithComponentById(@Param("tenantId") Long tenantId, @Param("woIdList") List<String> woIdList);

    /**
     *
     * @Description 根据JobId批量出站
     *
     * @author yuchao.wang
     * @date 2020/11/5 19:43
     * @param tenantId 租户
     * @param userId 用户ID
     * @param jobIdList jobIdList
     * @return void
     *
     */
    int batchOutSite(@Param("tenantId") Long tenantId,
                     @Param("userId") Long userId,
                     @Param("jobIdList") List<String> jobIdList);

    /**
     * @Description     根据JobId批量出站
     * @param tenantId  租户
     * @param userId    用户ID
     * @param jobIdList jobIdList
     * @param remark    备注
     * @return
     */
    int batchOutSite2(@Param("tenantId") Long tenantId,
                      @Param("userId") Long userId,
                      @Param("jobIdList") List<String> jobIdList,
                      @Param("remark") String remark);

    /**
     *
     * @Description
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:03
     * @param tenantId 租户ID
     * @param eoIds eoId
     * @param materialIds 物料ID
     * @param attrName 扩展属性名
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO4>
     *
     */
    List<HmeEoVO4> batchQueryBomComponentAttrByEo(@Param("tenantId") Long tenantId,
                                                  @Param("eoIds") List<String> eoIds,
                                                  @Param("materialIds") List<String> materialIds,
                                                  @Param("attrName") String attrName);

    /**
     *
     * @Description 根据工单和EO状态批量查询EO信息
     *
     * @author yuchao.wang
     * @date 2020/11/7 12:22
     * @param tenantId 租户ID
     * @param eoStatus EO状态
     * @param woIdList 工单ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO4>
     *
     */
    List<HmeEoVO4> batchQueryEoInfoByWoId(@Param("tenantId") Long tenantId,
                                          @Param("eoStatus") String eoStatus,
                                          @Param("woIdList") List<String> woIdList);

    /**
     *
     * @Description 根据EOID查询bom扩展属性值
     *
     * @author yuchao.wang
     * @date 2020/11/4 20:20
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param materialId 物料ID
     * @param attrName 属性名
     * @return java.lang.String
     *
     */
    String queryBomComponentAttrByEoId(@Param("tenantId") Long tenantId,
                                       @Param("eoId") String eoId,
                                       @Param("materialId") String materialId,
                                       @Param("attrName") String attrName);

    /**
     *
     * @Description 查询不良工位对应的工序
     *
     * @author yuchao.wang
     * @date 2020/11/10 10:38
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return tarzan.actual.domain.entity.MtNcRecord
     *
     */
    HmeNcRecordVO selectLastestNcRecordProcess(@Param("tenantId")Long tenantId,
                                               @Param("materialLotId") String materialLotId,
                                               @Param("workcellId") String workcellId);

    /**
     *
     * @Description 查询不良工位对应的工序-新版
     *
     * @author yuchao.wang
     * @date 2021/1/11 9:56
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param workcellId 当前工位ID
     * @return com.ruike.hme.domain.vo.HmeNcRecordVO
     *
     */
    HmeNcRecordVO selectLastNcRecordProcessForSingle(@Param("tenantId")Long tenantId,
                                                     @Param("eoId") String eoId,
                                                     @Param("workcellId") String workcellId);

    /**
     *
     * @Description 根据EO批量查询步骤信息
     *
     * @author penglin.sui
     * @date 2020/11/12 19:29
     * @param tenantId 租户ID
     * @param eoIdList EOID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnRouterStepVO5>
     *
     */
    List<HmeEoJobSnRouterStepVO5> batchQueryRouterStepOfEoIds(@Param("tenantId") Long tenantId,
                                                              @Param("eoIdList") List<String> eoIdList);

    /**
     * 查询投料升级信息
     *
     * @param tenantId     租户
     * @param materialCode 物料
     * @return com.ruike.hme.domain.vo.HmeUpgradeMaterialVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/19 04:20:25
     */
    HmeUpgradeMaterialVO selectMaterialUpgradeInfo(@Param("tenantId") Long tenantId,
                                                   @Param("materialCode") String materialCode);

    /**
     * 查询Eo产线
     *
     * @param tenantId
     * @param materialLotId
     * @author jiangling.zheng@hand-china.com 2020/11/26 20:20
     * @return java.lang.String
     */
    String selectEoProdLine(@Param("tenantId") Long tenantId,
                            @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 根据ID批量查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/11/17 17:41
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    List<HmeMaterialLotVO3> batchQueryMaterialLotInfoById(@Param("tenantId") Long tenantId,
                                                          @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     *
     * @Description 时效作业平台根据ID批量查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/11/17 17:41
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    List<HmeMaterialLotVO3> batchQueryMaterialLotInfoForTime(@Param("tenantId") Long tenantId,
                                                             @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     *
     * @Description
     *
     * @author yuchao.wang
     * @date 2020/11/17 20:30
     * @param tenantId
     * @param workcellId
     * @param jobIdList
     * @return int
     *
     */
    int queryValueMissingCount(@Param("tenantId") Long tenantId,
                               @Param("workcellId") String workcellId,
                               @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description
     *
     * @author penglin.sui
     * @date 2021/4/29 14:45
     * @param tenantId
     * @param workcellId
     * @param jobIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     *
     */
    List<HmeEoJobDataRecordVO> queryValueMissingTag(@Param("tenantId") Long tenantId,
                                                    @Param("workcellId") String workcellId,
                                                    @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 判断数据采集项是否不良
     *
     * @author penglin.sui
     * @date 2021/3/12 10:23
     * @param tenantId
     * @param workcellId
     * @param jobIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     *
     */
    List<HmeEoJobDataRecordVO2> queryValueNgDataRecord(@Param("tenantId") Long tenantId,
                                                       @Param("workcellId") String workcellId,
                                                       @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 判断是否存在其他工艺下的作业
     *
     * @author yuchao.wang
     * @date 2020/11/17 21:07
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialLotIds 条码ID
     * @return boolean
     *
     */
    int queryOtherOperationJobCount(@Param("tenantId") Long tenantId,
                                    @Param("operationId") String operationId,
                                    @Param("materialLotIds") List<String> materialLotIds);

    /**
     *
     * @Description 查询wkc扩展属性CONTAINER_OUT
     *
     * @author yuchao.wang
     * @date 2020/11/17 21:44
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @return java.lang.String
     *
     */
    String queryWkcContainerOutValue(@Param("tenantId") Long tenantId,
                                     @Param("workcellId") String workcellId);

    /**
     *
     * @Description 根据JobId批量出站
     *
     * @author yuchao.wang
     * @date 2020/11/19 1:22
     * @param tenantId 租户
     * @param userId 用户ID
     * @param snLineList 作业列表
     * @return void
     *
     */
    void batchOutSiteWithMaterialLot(@Param("tenantId") Long tenantId,
                                     @Param("userId") Long userId,
                                     @Param("snLineList") List<HmeEoJobSnVO3> snLineList);

    /**
     *
     * @Description 批量查询当前步骤及下一步骤
     *
     * @author yuchao.wang
     * @date 2020/11/19 14:10
     * @param tenantId 租户ID
     * @param routerStepIdList 步骤ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO4>
     *
     */
    List<HmeRouterStepVO4> batchQueryCurrentAndNextStep(@Param("tenantId") Long tenantId,
                                                        @Param("routerStepIdList") List<String> routerStepIdList);

    /**
     *
     * @Description 批量查询SN/LOT/TIME料投料数量
     *
     * @author yuchao.wang
     * @date 2020/11/22 16:45
     * @param tenantId 租户ID
     * @param jobIdList 作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobReleaseMaterialVO>
     *
     */
    List<HmeEoJobReleaseMaterialVO> batchQueryMaterialReleaseInfoByJobId(@Param("tenantId") Long tenantId,
                                                                         @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 根据主料组件ID查询所有替代料组件
     *
     * @author yuchao.wang
     * @date 2020/11/25 17:01
     * @param tenantId 租户ID
     * @param eoStepId 步骤ID
     * @param bomComponentIdList 主料组件ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeBomComponentVO2>
     *
     */
    List<HmeBomComponentVO> batchQuerySubstituteMaterialComponent(@Param("tenantId") Long tenantId,
                                                                  @Param("eoStepId") String eoStepId,
                                                                  @Param("bomComponentIdList") List<String> bomComponentIdList);

    /**
     *
     * @Description 根据EO批量查询步骤信息
     *
     * @author penglin.sui
     * @date 2020/11/12 19:29
     * @param tenantId 租户ID
     * @param eoIdList EOID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnRouterStepVO5>
     *
     */
    List<HmeEoBomVO> selectEoBom(@Param("tenantId") Long tenantId,
                                 @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 查询工单信息
     *
     * @author penglin.sui
     * @date 2020/11/19 21:45
     * @param tenantId 租户ID
     * @param workOrderIdList 工单ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnVO19>
     *
     */
    List<HmeEoJobSnVO19> selectWoProdLine(@Param("tenantId") Long tenantId,
                                          @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     *
     * @Description 根据ID查询容器信息
     *
     * @author yuchao.wang
     * @date 2020/12/3 15:23
     * @param tenantId 租户ID
     * @param containerId 容器ID
     * @return com.ruike.hme.domain.vo.HmeContainerVO
     *
     */
    HmeContainerVO queryContainerInfo(@Param("tenantId") Long tenantId,
                                      @Param("containerId") String containerId);

    /**
     *
     * @Description 查询容器当前装载明细
     *
     * @author yuchao.wang
     * @date 2020/12/3 22:08
     * @param tenantId 租户ID
     * @param containerId 容器ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeContainerDetailVO>
     *
     */
    List<HmeContainerDetailVO> queryContainerDetails(@Param("tenantId") Long tenantId,
                                                     @Param("containerId") String containerId);

    /**
     *
     * @Description 查询主料的全局替代料的装配实绩
     *
     * @author yuchao.wang
     * @date 2020/12/4 15:09
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param eoStepId 步骤ID
     * @param materialIdList 主料ID
     * @param eoIdList EOId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoComponentActualVO>
     *
     */
    List<HmeEoComponentActualVO> batchQueryEoActualForGlobalMaterial(@Param("tenantId") Long tenantId,
                                                                     @Param("operationId") String operationId,
                                                                     @Param("eoStepId") String eoStepId,
                                                                     @Param("materialIdList") List<String> materialIdList,
                                                                     @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 查询主料的全局替代料的装配实绩
     *
     * @author yuchao.wang
     * @date 2020/12/9 15:42
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param eoStepId 步骤ID
     * @param materialIdList 主料ID
     * @param eoId EOId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoComponentActualVO>
     *
     */
    List<HmeEoComponentActualVO> queryEoActualForGlobalMaterial(@Param("tenantId") Long tenantId,
                                                                @Param("operationId") String operationId,
                                                                @Param("eoStepId") String eoStepId,
                                                                @Param("materialIdList") List<String> materialIdList,
                                                                @Param("eoId") String eoId);

    /**
     *
     * @Description 查询返修中最近一步正常加工步骤名
     *
     * @author yuchao.wang
     * @date 2020/12/9 16:35
     * @param tenantId 租户ID
     * @param eoId
     * @return java.lang.String
     *
     */
    String queryNormalStepNameForRework(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     *
     * @Description 查询SN进站信息
     *
     * @author penglin.sui
     * @date 2020/12/4 16:54
     * @param tenantId 租户ID
     * @param jobType 平台类型ID
     * @param materialLotId 条码ID
     * @param workcellId 工位ID
     * @param isRework 是否返修
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    HmeEoJobSnVO23 selectInSiteSn(@Param("tenantId") Long tenantId,
                                  @Param("jobType") String jobType,
                                  @Param("materialLotId") String materialLotId,
                                  @Param("workcellId") String workcellId,
                                  @Param("isRework") boolean isRework);

    /**
     *
     * @Description 单件进站查询做业记录
     *
     * @author yuchao.wang
     * @date 2020/12/8 16:07
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param eoId eoId
     * @param isRework 是否返修
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO23
     *
     */
    List<HmeEoJobSnVO23> selectEoJobSnForInSite(@Param("tenantId") Long tenantId,
                                                @Param("operationId") String operationId,
                                                @Param("eoId") String eoId,
                                                @Param("isRework") boolean isRework);

    /**
     *
     * @Description 根据步骤ID查询当前及下一步骤信息
     *
     * @author yuchao.wang
     * @date 2020/12/17 16:42
     * @param tenantId 租户ID
     * @param routerStepId 步骤ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    HmeRouterStepVO5 queryCurrentAndNextStepByCurrentId(@Param("tenantId") Long tenantId,
                                                        @Param("routerStepId") String routerStepId);

    /**
     *
     * @Description 根据eoId和工艺ID查询当前及下一步骤信息
     *
     * @author yuchao.wang
     * @date 2020/12/17 17:03
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param operationId 工艺ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    HmeRouterStepVO5 queryCurrentAndNextStepByEoAndOperation(@Param("tenantId") Long tenantId,
                                                             @Param("eoId") String eoId,
                                                             @Param("operationId") String operationId);

    /**
     *
     * @Description 根据eoId和工艺ID查询当前步骤信息
     *
     * @author yuchao.wang
     * @date 2021/1/27 15:35
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param operationId 工艺ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    HmeRouterStepVO5 queryCurrentStepByEoAndOperation(@Param("tenantId") Long tenantId,
                                                      @Param("eoId") String eoId,
                                                      @Param("operationId") String operationId);

    /**
     *
     * @Description 根据eoId和工艺ID查询当前及下一步骤信息
     *
     * @author yuchao.wang
     * @date 2020/12/31 10:03
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param routerId 工艺路线ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO5>
     *
     */
    List<HmeRouterStepVO5> queryCurrentAndNextStepForDesignedRework(@Param("tenantId") Long tenantId,
                                                                    @Param("operationId") String operationId,
                                                                    @Param("routerId") String routerId);

    /**
     *
     * @Description 根据eoId和工艺路线ID查询上一道步骤
     *
     * @author yuchao.wang
     * @date 2021/2/2 10:03
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param routerId 工艺路线ID
     * @param startTime 发起时间
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    HmeRouterStepVO5 queryPreviousStepForDesignedRework(@Param("tenantId") Long tenantId,
                                                        @Param("eoId") String eoId,
                                                        @Param("routerId") String routerId,
                                                        @Param("startTime") Date startTime);

    /**
     *
     * @Description 单件进站查询作业信息
     *
     * @author yuchao.wang
     * @date 2020/12/17 19:23
     * @param tenantId 租户ID
     * @param jobId 作业Id
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO
     *
     */
    HmeEoJobSnVO queryEoJobSnInfoForInSiteQuery(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     *
     * @Description 时效作业平台查询已进站的作业
     *
     * @author yuchao.wang
     * @date 2020/12/23 18:19
     * @param tenantId 租户ID
     * @param isRework 是否返修 true:是 false:否
     * @param operationId 工艺ID
     * @param eoIdList eoIdList
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    List<HmeEoJobSn> queryEoJobSnForTimeInSite(@Param("tenantId") Long tenantId,
                                               @Param("isRework") boolean isRework,
                                               @Param("operationId") String operationId,
                                               @Param("eoId") String eoId,
                                               @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 根据EO查询作业记录总数
     *
     * @author yuchao.wang
     * @date 2021/1/5 20:27
     * @param tenantId 租户ID
     * @param eoId eoId
     * @return int
     *
     */
    int queryEoJobSnCountByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     *
     * @Description 根据EO 物料查EO对应的组件
     *
     * @author yuchao.wang
     * @date 2021/1/6 10:07
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param materialId 物料ID
     * @param eoStepId eoStepId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeBomComponentVO3>
     *
     */
    List<HmeBomComponentVO3> queryEoComponentByEoIdAndMaterialId(@Param("tenantId") Long tenantId,
                                                                 @Param("eoId") String eoId,
                                                                 @Param("materialId") String materialId,
                                                                 @Param("eoStepId") String eoStepId);

    /**
     *
     * @Description 进站投料查询条码信息
     *
     * @author yuchao.wang
     * @date 2021/1/6 13:44
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.vo.HmeMaterialLotVO4
     *
     */
    HmeMaterialLotVO4 queryMaterialLotInfoForInSiteRelease(@Param("tenantId") Long tenantId,
                                                           @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 查询条码维护了几条返修EO
     *
     * @author yuchao.wang
     * @date 2021/2/1 15:29
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @return int
     *
     */
    int queryReworkEoCount(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     *
     * @Description 查询EO组件清单 用于加载要投料数据
     *
     * @author yuchao.wang
     * @date 2021/1/2 22:11
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param bomId bomId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeBomComponentVO2>
     *
     */
    List<HmeBomComponentVO2> queryEoComponentForMaterialInSite(@Param("tenantId") Long tenantId,
                                                               @Param("siteId") String siteId,
                                                               @Param("bomId") String bomId);

    /**
     *
     * @Description 查询物料站点扩展属性
     *
     * @author penglin.sui
     * @date 2021/2/4 11:30
     * @param tenantId 租户ID
     * @param materialIdList 物料ID
     * @param siteId 站点ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO24
     *
     */
    List<HmeEoJobSnVO25> queryMaterialSiteAttr(@Param("tenantId") Long tenantId,
                                               @Param("materialIdList") List<String> materialIdList,
                                               @Param("siteId") String siteId);

    String queryFacMaterialId(@Param("tenantId") Long tenantId,
                            @Param("siteId") String siteId,
                            @Param("eoId") String eoId,
                            @Param("operationName") String operationName);
    /**
     *
     * @Description 在条码表和容器表查询编码是否存在
     *
     * @author yuchao.wang
     * @date 2021/1/15 10:38
     * @param tenantId 租户ID
     * @param code 条码
     * @return java.lang.Integer
     *
     */
    Integer queryCountFromMaterialLotAndContainerByCode(@Param("tenantId") Long tenantId, @Param("code") String code);

    /**
     *
     * @Description 查询工艺扩展属性BOM_FLAG
     *
     * @author yuchao.wang
     * @date 2021/1/29 10:46
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return java.lang.String
     *
     */
    String queryOperationBomFlag(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);

    /**
     *
     * @Description 查询最大步骤
     *
     * @author penglin.sui
     * @date 2021/3/12 13:57
     * @param tenantId 租户ID
     * @param dto
     * @return int
     *
     */
    int queryReworkMaxEoStepNum(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSn dto);
    /**
     * 查询最大eoStepNum
     *
     * @param tenantId
     * @param workcellId
     * @param eoId
     * @param materialLotId
     * @param reworkFlag
     * @param jobType
     * @param operationId
     * @author sanfeng.zhang@hand-china.com 2021/3/10 16:29
     * @return java.lang.Integer
     */
    Integer queryMaxEoStepNum(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId, @Param("eoId") String eoId, @Param("materialLotId") String materialLotId, @Param("reworkFlag") String reworkFlag, @Param("jobType") String jobType, @Param("operationId") String operationId);

    /**
     * 判断EO是否已进站
     *
     * @param tenantId
     * @param eoId
     * @author penglin.sui@hand-china.com 2021/3/23 13:53
     * @return java.lang.Integer
     */
    Integer queryHaveInSiteCount(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 批量判断EO是否已进站
     *
     * @param tenantId
     * @param eoIdList
     * @author penglin.sui@hand-china.com 2021/4/9 10:59
     * @return java.lang.Integer
     */
    Integer batchQueryHaveInSiteCount(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     *
     * @Description 查询工艺扩展属性AGEING_FLAG
     *
     * @author yuchao.wang
     * @date 2021/2/4 10:24
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return java.lang.String
     *
     */
    String queryOperationAgeingFlag(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);

    /**
     *
     * @Description 根据工位查询工序
     *
     * @author yuchao.wang
     * @date 2021/2/4 11:04
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param workcellId 工位ID
     * @return java.lang.String
     *
     */
    String queryWkcStation(@Param("tenantId") Long tenantId,
                           @Param("siteId") String siteId,
                           @Param("workcellId") String workcellId);

    /**
     *
     * @Description 根据工位查询工序
     *
     * @author yuchao.wang
     * @date 2021/2/4 11:04
     * @param tenantId 租户ID
     * @param eoId EOID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    List<HmeEoJobSn> selectEoStepOfEo(@Param("tenantId") Long tenantId,
                                      @Param("eoId") String eoId);

    /**
     * 首序标识
     *
     * @param tenantId
     * @param operationId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/8/26
     */
    String queryFirstProcessFlag(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);
    /**
     * 查询最新一条eo信息
     *
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnVO5>
     * @author sanfeng.zhang@hand-china.com 2021/8/13
     */
    List<HmeEoJobSnVO5> queryLastEoByReworkMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 自制件返修进站查询旧条码信息
     *
     * @param tenantId
     * @param materialLotId
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO5
     * @author sanfeng.zhang@hand-china.com 2021/8/13
     */
    HmeEoJobSnVO5 queryMaterialLotInfoForRework2(@Param("tenantId") Long tenantId,
                                                 @Param("materialLotId") String materialLotId,
                                                 @Param("siteId") String siteId);

    /**
     *
     * @Description 自制件返修进站查询原条码信息
     *
     * @author yuchao.wang
     * @date 2020/12/15 11:07
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @param siteId 站点ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO5
     *
     */
    HmeEoJobSnVO5 queryMaterialLotInfoForRework3(@Param("tenantId") Long tenantId,
                                                @Param("materialLotId") String materialLotId,
                                                @Param("siteId") String siteId);
    /**
     *
     * @Description 查询工艺扩展属性TEST_FLAG
     *
     * @author sanfeng.zhang
     * @date 2021/2/4 10:24
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return java.lang.String
     *
     */
    String queryOperationDeviceFlag(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);

    /**
     * 查询最近正常工序
     * @param tenantId
     * @param eoId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/8/18
     */
    String queryNearNormalProcess(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 判断工位是否在工序下
     * @param tenantId
     * @param processId
     * @param workcellId
     * @return java.lang.Long
     * @author sanfeng.zhang@hand-china.com 2021/8/18
     */
    Long judgeProcessConformity(@Param("tenantId") Long tenantId, @Param("processId") String processId, @Param("workcellId") String workcellId);

    /**
     * 查询工艺扩展属性REFLECTOR_FLAG
     *
     * @param tenantId
     * @param operationId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/8/23
     */
    String queryOperationReflectorFlag(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);

    /**
     * 查询工序不良数据项信息
     *
     * @param tenantId
     * @param processNcHeaderId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/23
     */
    List<HmeProcessNcVO> queryProcessNcTagList(@Param("tenantId") Long tenantId, @Param("processNcHeaderId") String processNcHeaderId);

    /**
     * 工序不良头
     * @param tenantId
     * @param cosModel
     * @param materialId
     * @param operationId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeProcessNcHeader>
     * @author sanfeng.zhang@hand-china.com 2021/8/23
     */
    List<HmeProcessNcHeader> queryProcessNcHeader(@Param("tenantId") Long tenantId, @Param("cosModel") String cosModel, @Param("materialId") String materialId, @Param("operationId") String operationId);
}
