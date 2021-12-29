package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.vo.*;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import org.hzero.core.base.AopProxy;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.*;

/**
 * 作业平台-公共方法
 *
 * @author yuchao.wang@hand-china.com 2020-11-03 00:04:39
 */
public interface HmeEoJobSnCommonService extends AopProxy<HmeEoJobSnCommonService> {


    /**
     *
     * @Description 根据EO批量获取最近一步工艺
     *
     * @author yuchao.wang
     * @date 2020/10/28 18:18
     * @param tenantId 租户ID
     * @param stepType 工艺步骤类型 最近一步工艺/最近正常加工一步工艺
     * @param eoIdList eoIdList
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO>
     *
     */
    Map<String, HmeRouterStepVO> batchQueryRouterStepByEoIds(Long tenantId, String stepType, List<String> eoIdList);

    /**
     *
     * @Description 批量查询条码信息并校验EO状态
     *
     * @author yuchao.wang
     * @date 2020/11/3 11:36
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    HmeEoJobSnVO16 batchQueryAndCheckMaterialLotByIdsForTime(Long tenantId, List<String> materialLotIdList);

    /**
     *
     * @Description 查询条码及工序信息
     *
     * @author yuchao.wang
     * @date 2021/1/26 17:13
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    HmeEoJobSnVO16 batchQueryAndCheckMaterialLotByIdsForTimeRework(Long tenantId, String materialLotId);

    /**
     *
     * @Description 批量查询eo下当前步骤
     *
     * @author yuchao.wang
     * @date 2020/11/3 15:52
     * @param tenantId 租户ID
     * @param eoIdList eoId
     * @param operationIdList 工艺ID
     * @return java.util.Map<java.lang.String,java.util.List<com.ruike.hme.domain.vo.HmeRouterStepVO3>>
     *
     */
    Map<String, List<HmeRouterStepVO3>> batchQueryCurrentRouterStep(Long tenantId, List<String> eoIdList, List<String> operationIdList);

    /**
     *
     * @Description 时效作业-批量查询eo下当前步骤
     *
     * @author yuchao.wang
     * @date 2020/11/3 20:25
     * @param tenantId 租户ID
     * @param eoIdList eoId
     * @param operationId 工艺ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO3>
     *
     */
    Map<String, HmeRouterStepVO3> batchQueryCurrentRouterStepForTime(Long tenantId, List<String> eoIdList, String operationId);

    /**
     * @param tenantId 租户ID
     * @param eoId     eoId
     * @return boolean
     * @Description 判断当前EO关联的工单类型 是否为售后
     * @author yuchao.wang
     * @date 2020/9/28 14:47
     */
    boolean isAfterSalesWorkOrder(Long tenantId, String eoId);

    /**
     * @param tenantId 租户ID
     * @param eoIdList eoIdList
     * @return Map<String,boolean>
     * @Description 判断EO关联的工单类型 是否为售后
     * @author penglin.sui
     * @date 2020/11/12 14:47
     */
    Map<String,HmeEoJobSnBatchVO3> batchQtyAfterSalesWorkOrder(Long tenantId, List<String> eoIdList);

    /**
     *
     * @Description 批量查询下一步骤
     *
     * @author yuchao.wang
     * @date 2020/11/3 20:16
     * @param tenantId 租户ID
     * @param routerStepIdList 步骤ID
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    Map<String, String> batchQueryNextStep(Long tenantId, List<String> routerStepIdList);

    /**
     *
     * @Description 查询下一步骤
     *
     * @author yuchao.wang
     * @date 2020/12/14 15:05
     * @param tenantId 租户ID
     * @param routerStepId 步骤ID
     * @return java.lang.String
     *
     */
    String queryNextStep(Long tenantId, String routerStepId);

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
    HmeRouterStepVO5 queryCurrentAndNextStepByCurrentId(Long tenantId, String routerStepId);

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
    HmeRouterStepVO5 queryCurrentAndNextStepByEoAndOperation(Long tenantId, String eoId, String operationId);

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
    HmeRouterStepVO5 queryCurrentStepByEoAndOperation(Long tenantId, String eoId, String operationId);

    /**
     *
     * @Description 根据eoId和工艺ID查询当前及下一步骤信息
     *
     * @author yuchao.wang
     * @date 2020/12/31 10:03
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param routerId 工艺路线ID
     * @param eoId 执行作业ID
     * @return com.ruike.hme.domain.vo.HmeRouterStepVO5
     *
     */
    HmeRouterStepVO5 queryCurrentAndNextStepForDesignedRework(Long tenantId, String operationId, String routerId, String eoId);

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
    HmeRouterStepVO5 queryPreviousStepForDesignedRework(Long tenantId, String eoId, String routerId, Date startTime);

    /**
     *
     * @Description 根据ID批量查询EO信息 以及EO对应的WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/5 16:53
     * @param tenantId 租户ID
     * @param eoIdList eoIdList
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    HmeEoJobSnVO16 batchQueryEoAndWoInfoById(Long tenantId, List<String> eoIdList);

    /**
     *
     * @Description 根据eoId查询EO/WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:37
     * @param tenantId 租户ID
     * @param eoStepId 步骤ID
     * @param eoIdList eoId
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO16
     *
     */
    HmeEoJobSnVO16 batchQueryEoAndWoInfoWithComponentById(Long tenantId, String eoStepId, List<String> eoIdList);

    /**
     *
     * @Description 批量查询条码返修标识
     *
     * @author yuchao.wang
     * @date 2020/11/6 15:52
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return java.util.Map<java.lang.String,java.lang.String>
     *
     */
    Map<String, String> batchQueryMaterialLotReworkFlag(Long tenantId, List<String> materialLotIdList);

    /**
     *
     * @Description 时效作业平台批量查询条码返修标识
     *
     * @author yuchao.wang
     * @date 2020/12/21 15:52
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    List<HmeMaterialLotVO3> batchQueryMaterialLotReworkFlagForTime(Long tenantId, List<String> materialLotIdList);

    /**
     * 
     * @Description 批量执行订单完成
     * 
     * @author yuchao.wang
     * @date 2020/11/5 19:09
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param eoIdList eoIdList
     * @param eoMap eo信息
     * @return void
     * 
     */
    void siteOutBatchComplete(Long tenantId, String workcellId, List<String> eoIdList, Map<String, HmeEoVO4> eoMap);

    /**
     *
     * @Description 执行订单完成
     *
     * @author yuchao.wang
     * @date 2021/1/27 14:52
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param eo eo信息
     * @return void
     *
     */
    void siteOutComplete(Long tenantId, String workcellId, HmeEoVO4 eo);

    /**
     *
     * @Description 批量出站主程序
     *
     * @author yuchao.wang
     * @date 2020/11/5 20:36
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     *
     */
    List<WmsObjectTransactionResponseVO> batchMainOutSite(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16);

    /**
     *
     * @Description 时效返修平台入口步骤加工完成
     *
     * @author yuchao.wang
     * @date 2021/2/1 14:25
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @return void
     *
     */
    void batchMainOutSiteForTimeReworkEntryStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16);

    /**
     *
     * @Description 完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:37
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO18 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO17
     *
     */
    HmeEoJobSnVO17 batchMainOutSiteForDoneStep(Long tenantId,
                                               HmeEoJobSnVO3 dto,
                                               HmeEoJobSnVO18 hmeEoJobSnVO18);

    /**
     *
     * @Description 非完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:38
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO18 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO17
     *
     */
    HmeEoJobSnVO17 batchMainOutSiteForNormalStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO18 hmeEoJobSnVO18);

    /**
     *
     * @Description 统一发送实时接口
     *
     * @author yuchao.wang
     * @date 2020/11/6 21:43
     * @param tenantId 租户ID
     * @param transactionResponseList 事件
     * @return void
     *
     */
    void sendTransactionInterface(Long tenantId, List<WmsObjectTransactionResponseVO> transactionResponseList);

    /**
     *
     * @Description 工段完工数据统计-批量工序作业平台/时效工序作业平台
     *
     * @author yuchao.wang
     * @date 2020/11/7 14:02
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @return void
     *
     */
    void batchWkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16);

    /**
     *
     * @Description 批量创建工序作业
     *
     * @author penglin.sui
     * @date 2020/11/12 23:36
     * @param tenantId 租户ID
     * @param dtoList 参数
     * @return void
     *
     */
    void batchCreateSnJob(Long tenantId, List<HmeEoJobSnVO3> dtoList);

    /**
     *
     * @Description 批量查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/11/17 17:27
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeMaterialLotVO3>
     *
     */
    Map<String, HmeMaterialLotVO3> batchQueryMaterialLotInfo(Long tenantId, List<String> materialLotIdList);

    /**
     *
     * @Description 判断作业下是否有为空的数据采集项结果
     *
     * @author yuchao.wang
     * @date 2020/11/17 20:29
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobIdList 作业ID
     * @return boolean
     *
     */
    boolean hasMissingValue(Long tenantId, String workcellId, List<String> jobIdList);

    /**
     *
     * @Description 查询作业下是否有为空的数据采集项结果
     *
     * @author yuchao.wang
     * @date 2020/11/17 20:29
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobIdList 作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     *
     */
    List<HmeEoJobDataRecordVO> hasMissingValueTag(Long tenantId, String workcellId, List<String> jobIdList);

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
    boolean hasOtherOperationJob(Long tenantId, String operationId, List<String> materialLotIds);

    /**
     *
     * @Description 是否容器出站
     *
     * @author yuchao.wang
     * @date 2020/11/17 21:41
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @return boolean
     *
     */
    boolean isContainerOutSite(Long tenantId, String workcellId);

    /**
     *
     * @Description 容器出站
     *
     * @author yuchao.wang
     * @date 2020/11/17 22:24
     * @param tenantId 租户ID
     * @param hmeEoJobSnVO16 参数
     * @param dto 参数
     * @return void
     *
     */
    void batchContainerOutSite(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 批量查询当前步骤及下一步骤
     *
     * @author yuchao.wang
     * @date 2020/11/19 14:10
     * @param tenantId 租户ID
     * @param routerStepIdList 步骤ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeRouterStepVO4>
     *
     */
    Map<String, HmeRouterStepVO4> batchQueryCurrentAndNextStep(Long tenantId, List<String> routerStepIdList);

    /**
     * 获取时效时长
     *
     * @param tenantId 租户Id
     * @param siteId 站点ID
     * @param materialId 物料ID
     * @return String
     */
    String getAvailableTime(Long tenantId, String siteId , String materialId);

    /**
     * 获取截至时间
     *
     * @param tenantId 租户Id
     * @param availableTime 时效时长
     * @param materialLotId 条码ID
     * @return String
     */
    String getDeadLineDate(Long tenantId, String availableTime , String materialLotId);

    /**
     * 查询工单组件清单
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return List<HmeEoJobSnBatchVO4>
     */
    List<HmeEoJobSnBatchVO4> selectWoBomComponent(Long tenantId, HmeEoJobSnVO22 dto);

    /**
     * 查询EO组件清单
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return List<HmeEoJobSnBatchVO4>
     */
    List<HmeEoJobSnBatchVO4> selectEoBomComponent(Long tenantId, HmeEoJobSnBatchDTO dto);

    /**
     * 查询虚拟件组件
     *
     * @param tenantId 租户Id
     * @param materialLotId 条码ID
     * @param workcellId 工位ID
     * @return HmeEoJobSnBatchVO22
     */
    HmeEoJobSnBatchVO22 selectVritualComponent(Long tenantId , String materialLotId , String workcellId);

    /**
     *
     * @Description 根据eoId查询EO/WO信息
     *
     * @author yuchao.wang
     * @date 2020/11/21 14:38
     * @param tenantId 租户ID
     * @param eoStepId eoStepId
     * @param eoId eoId
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    HmeEoJobSnSingleBasicVO queryEoAndWoInfoWithComponentByEoId(Long tenantId, String eoStepId, String eoId);

    /**
     *
     * @Description 指定工艺路线返修-根据eoId查询EO/WO信息
     *
     * @author yuchao.wang
     * @date 2021/1/4 15:52
     * @param tenantId 租户ID
     * @param eoStepId eoStepId
     * @param eoId eoId
     * @param eoRouterBomRel 关系数据
     * @return com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO
     *
     */
    HmeEoJobSnSingleBasicVO queryEoAndWoInfoWithCompByEoIdForDesignedRework(Long tenantId, String eoStepId, String eoId, HmeEoRouterBomRelVO eoRouterBomRel);

    /**
     *
     * @Description 查询条码信息
     *
     * @author yuchao.wang
     * @date 2020/11/21 15:03
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @return com.ruike.hme.domain.vo.HmeMaterialLotVO3
     *
     */
    HmeMaterialLotVO3 queryMaterialLotInfo(Long tenantId, String materialLotId);

    /**
     *
     * @Description 单件出站主程序
     *
     * @author yuchao.wang
     * @date 2020/11/21 18:05
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     *
     */
    List<WmsObjectTransactionResponseVO> mainOutSite(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     * 单件出站主程序（不自动判断不良 直接发起返修）
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnSingleBasic
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/18
     */
    List<WmsObjectTransactionResponseVO> mainOutSite2(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     *
     * @Description 完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/21 22:42
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     *
     */
    List<WmsObjectTransactionResponseVO> mainOutSiteForDoneStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     *
     * @Description 非完工步骤出站
     *
     * @author yuchao.wang
     * @date 2020/11/22 0:45
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    void mainOutSiteForNormalStep(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     *
     * @Description 工段完工数据统计-工序作业平台/PDA工序作业平台
     *
     * @author yuchao.wang
     * @date 2020/11/22 11:35
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    void wkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     *
     * @Description 返修作业平台出站-非完工步骤出站
     *
     * @author yuchao.wang
     * @date 2021/1/7 20:44
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    void mainOutSiteForNormalStepForReworkProcess(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     *
     * @Description 单件返修出站主程序
     *
     * @author yuchao.wang
     * @date 2020/12/15 15:28
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    void mainOutSiteForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     *
     * @Description 单件指定工艺路线返修出站主程序
     *
     * @author yuchao.wang
     * @date 2021/1/4 20:42
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnSingleBasic 参数
     * @return void
     *
     */
    void mainOutSiteForDesignedReworkComplete(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     * 单件指定工艺路线返修出站主程序(不自动判断不良 直接发起返修)
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnSingleBasic
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/8/18
     */
    void mainOutSiteForDesignedReworkComplete2(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);

    /**
     *
     * @Description 时效作业平台查询已进站的作业
     *
     * @author yuchao.wang
     * @date 2020/12/23 17:13
     * @param tenantId 租户ID
     * @param isRework 是否返修 true:是 false:否
     * @param operationId 工艺ID
     * @param eoIdList eoIdList
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    List<HmeEoJobSn> queryEoJobSnForTimeInSite(Long tenantId, boolean isRework, String operationId, List<String> eoIdList);

    /**
     *
     * @Description 时效作业-继续返修
     *
     * @author yuchao.wang
     * @date 2020/12/24 11:19
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO24 参数
     * @return void
     *
     */
    void batchMainOutSiteForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO24 hmeEoJobSnVO24);

    /**
     *
     * @Description 时效作业-指定工艺路线返修-加工完成
     *
     * @author yuchao.wang
     * @date 2021/1/8 10:24
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO24 参数
     * @return void
     *
     */
    void batchMainOutSiteForDesignedReworkComplete(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO24 hmeEoJobSnVO24);

    /**
     *
     * @Description 查询虚拟件组件
     *
     * @author penglin.sui
     * @date 2020/01/06 10:51
     * @param tenantId 租户ID
     * @param componentList 虚拟件/虚拟件组件
     * @return HmeEoJobSnBatchVO16
     *
     */
    HmeEoJobSnBatchVO16 selectVirtualComponent(Long tenantId,List<HmeEoJobSnBatchVO4> componentList);

    /**
     *
     * @Description 创建工序作业-单件作业平台
     *
     * @author yuchao.wang
     * @date 2020/12/31 14:58
     * @param tenantId 租户ID
     * @param bomId bomId
     * @param snLine 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO2
     *
     */
    HmeEoJobSnVO2 createSnJobForSingle(Long tenantId, String bomId, HmeEoJobSnVO3 snLine);

    /**
     *
     * @Description 判断条码是否为物料批条码或者容器条码
     *
     * @author yuchao.wang
     * @date 2021/1/15 10:35
     * @param tenantId 租户ID
     * @param code 条码
     * @return boolean
     *
     */
    boolean codeIsMaterialLotOrContainer(Long tenantId, String code);


    /**
     *
     * @Description 查询工艺扩展属性BOM_FLAG
     *
     * @author yuchao.wang
     * @date 2021/1/29 10:46
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return boolean BOM_FLAG=Y:true 否则:false
     *
     */
    boolean queryOperationBomFlag(Long tenantId, String operationId);

    /**
     *
     * @Description 查询不良的数据采集项
     *
     * @author penglin.sui
     * @date 2021/3/15 14:19
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobIdList 工序作业ID
     * @return boolean BOM_FLAG=Y:true 否则:false
     *
     */
    List<HmeEoJobDataRecordVO2> queryNgDataRecord(Long tenantId, String workcellId, List<String> jobIdList);

    /**
     * 客户机内部订单不存在整机产出，因此单独进行内部订单调拨接口回传不走完工报工
     *
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnSingleBasic
     * @param splitRecord
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/2 15:18
     */
    List<WmsObjectTransactionResponseVO> mainOutSiteForInternalReworkProcess(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeServiceSplitRecord splitRecord);

    /**
     * 进站校验工位绑定设备
     *
     * @param tenantId
     * @param operationId
     * @param workcellId
     * @return void
     * @author penglin.sui@hand-china.com 2021/5/25 10:00
     */
    void workcellBindEquipmentValidate(Long tenantId, String operationId , String workcellId);
    /**
     *
     * @Description 校验数据采集项不良判定
     *
     * @author yuchao.wang
     * @date 2021/1/25 13:47
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param eoJobDataRecordList 数据采集项结果
     * @param processNcInfo 工序不良信息
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    HmeEoJobSn dataRecordProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo);

    /**
     *
     * @Description 校验数据采集项不良判定-老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 13:46
     * @param tenantId 租户ID
     * @param materialLotCode 条码
     * @param eoJobDataRecordList 数据采集项结果
     * @param processNcInfo 工序不良信息
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    HmeEoJobSn dataRecordAgeingProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo);

    /**
     *
     * @Description 根据工艺判断是器件不良还是老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 10:12
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return boolean true:老化不良 false:器件不良
     *
     */
    boolean isAgeingNc(Long tenantId, String operationId);

    /**
     * 是否首序
     * @param tenantId
     * @param operationId
     * @return boolean
     * @author sanfeng.zhang@hand-china.com 2021/8/26
     */
    boolean isFirstProcess(Long tenantId, String operationId);

    /**
     * 是否是新条码
     *
     * @param tenantId
     * @param snNum
     * @author sanfeng.zhang@hand-china.com 2021/9/14 18:09
     * @return boolean
     */
    boolean isNewMaterialLot(Long tenantId, String snNum);

    /**
     * 旧条码的返修标识和进站标识
     *
     * @param tenantId
     * @param eoId
     * @author sanfeng.zhang@hand-china.com 2021/9/14 19:23
     * @return java.util.List<io.tarzan.common.domain.vo.MtExtendAttrVO>
     */
    List<MtExtendAttrVO> queryOldCodeAttrList(Long tenantId, String eoId);

    /**
     *
     * @Description 根据工艺判断是否是器件不良
     *
     * @author sanfeng.zhang
     * @date 2021/2/4 10:12
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return boolean true:是 false:不是
     *
     */
    boolean isDeviceNc(Long tenantId, String operationId);

    /**
     * 该工位对应的工序是否为最近正常工序
     * @param tenantId
     * @param eoId
     * @param workcellId
     * @return boolean
     * @author sanfeng.zhang@hand-china.com 2021/8/18
     */
    boolean isNearNormalProcess(Long tenantId, String eoId, String workcellId);

    /**
     *
     * @Description 根据工艺判断是否是器反射镜良
     *
     * @author sanfeng.zhang
     * @date 2021/2/4 10:12
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return boolean true:是 false:不是
     *
     */
    boolean isReflectorNc(Long tenantId, String operationId);

    /**
     * 校验数据采集项不良判定
     * @param tenantId
     * @param materialLotCode
     * @param eoJobDataRecordList
     * @param processNcInfo
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     * @author sanfeng.zhang@hand-china.com 2021/8/23
     */
    HmeEoJobSn dataRecordReflectorProcessNcValidate(Long tenantId, String materialLotCode, List<HmeEoJobDataRecord> eoJobDataRecordList, HmeProcessNcHeaderVO2 processNcInfo);
    /**
     * 拦截校验
     *
     * @param tenantId
     * @param workcellId
     * @param mtMaterialLotList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 10:58
     * @return void
     */
    void interceptValidate(Long tenantId, String workcellId, List<MtMaterialLot> mtMaterialLotList);

    /**
     * 工序-采集项强校验标识
     *
     * @param tenantId
     * @param workcellId
     * @return boolean
     * @author sanfeng.zhang@hand-china.com 2021/10/19
     */
    boolean queryProcessValidateFlag(Long tenantId, String workcellId);

    /***
     * 旧条码找新条码
     *
     * @param tenantId
     * @param materialLotCode
     * @return java.lang.Boolean
     * @author sanfeng.zhang@hand-china.com 2021/11/25
     */
    Boolean isBindMoreWorkingEo(Long tenantId, String materialLotCode);
}
