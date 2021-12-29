package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeNcCheckDTO3;
import com.ruike.hme.api.dto.HmeNcDisposePlatformDTO26;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.order.domain.entity.MtWorkOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 不良审核Mapper
 *
 * @author choanan.hu@hand-china.com 2020-07-20 15:08:09
 */
public interface HmeNcCheckMapper {

    /**
     * 查询componentRequired为Y的不良代码组数据
     *
     * @param tenantId 租户Id
     * @param dto 查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcCheckVO>
     */
    List<HmeNcCheckVO> ncGroupQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeNcCheckDTO3 dto);

    /**
     * 根据工艺Id查询不良代码组数据
     *
     * @param tenantId 租户Id
     * @param dto 查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcCheckVO>
     */
    List<HmeNcCheckVO> ncGroupQueryByOperation(@Param("tenantId") Long tenantId, @Param("dto") HmeNcCheckDTO3 dto);

    /**
     * 查询不良记录下的子不良记录
     *
     * @param tenantId 租户Id
     * @param ncRecordId 不良记录Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/7 15:25:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcCheckVO2>
     */
    List<HmeNcCheckVO2> childNcRecordIdQuery(@Param("tenantId") Long tenantId, @Param("ncRecordId") String ncRecordId);

    /**
     * 根据父不良记录Id查询状态不为CANCEL的子不良记录
     *
     * @param tenantId 租户Id
     * @param parentNcRecordId 父不良记录Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/7 16:33:05
     * @return java.util.List<tarzan.actual.domain.entity.MtNcRecord>
     */
    List<MtNcRecord> childNcRecordQuery(@Param("tenantId") Long tenantId, @Param("parentNcRecordId") String parentNcRecordId);

    /**
     * 根据父不良记录Id批量查询状态不为CANCEL的子不良记录
     *
     * @param tenantId 租户Id
     * @param parentNcRecordIdList 父不良记录Id集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/7 16:33:05
     * @return java.util.List<tarzan.actual.domain.entity.MtNcRecord>
     */
    List<HmeNcCheckVO5> childNcCodeQuery(@Param("tenantId") Long tenantId, @Param("parentNcRecordIdList") List<String> parentNcRecordIdList);

    /**
     * 查询最近的备注
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param ncCodeId 不良代码组ID
     * @param rootCauseOperationId 产生问题的源工艺
     * @param eoStepActualId 工步
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/15 17:32:54
     * @return com.ruike.hme.api.dto.HmeNcDisposePlatformDTO26
     */
    HmeNcDisposePlatformDTO26 commentsQuery(@Param("tenantId") Long tenantId, @Param("materialId") String materialId,
                                            @Param("ncCodeId") String ncCodeId, @Param("rootCauseOperationId") String rootCauseOperationId,
                                            @Param("eoStepActualId") String eoStepActualId);

    /**
     * 根据eoId查询jobId
     *
     * @param tenantId 租户ID
     * @param eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/9 11:21:11
     * @return java.util.List<java.lang.String>
     */
    List<String> getJobIdByEo(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 根据eo获取在表mt_bom_component中的装配清单
     *
     * @param tenantId 租户ID
     * @param eoId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/9 14:30:15
     * @return java.util.List<tarzan.method.domain.entity.MtBomComponent>
     */
    List<MtBomComponent> eoBomComponentQuery(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 工艺路线步骤查询
     *
     * @param tenantId 租户ID
     * @param eoId
     * @param operationId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/22 10:26:54
     * @return java.lang.String
     */
    String getRouterStepId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId,
                           @Param("operationId") String operationId);


    /**
     * 判断当前条码是否投料
     *
     * @param tenantId
     * @param eoId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2020/11/25 15:49
     * @return java.lang.Integer
     */
    Integer judgeFeedByMaterialLot(@Param("tenantId") Long tenantId, @Param("eoId") String eoId, @Param("materialLotId") String materialLotId);

    /**
     * 工单总需求数量
     *
     * @param tenantId
     * @param bomComponentId
     * @author sanfeng.zhang@hand-china.com 2020/11/25 20:37
     * @return java.math.BigDecimal
     */
    BigDecimal queryWorkQtyByBomComponentId(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId);

    /**
     * 累计投料数量
     *
     * @param tenantId
     * @param bomComponentId
     * @author sanfeng.zhang@hand-china.com 2020/11/25 20:54
     * @return com.ruike.hme.domain.vo.HmeNcCheckVO3
     */
    HmeNcCheckVO3 queryAssembleQtyByBomComponentId(@Param("tenantId") Long tenantId, @Param("bomComponentId") String bomComponentId);

    /**
     * 根据工位找产线
     *
     * @param tenantId
     * @param workcellId
     * @author sanfeng.zhang@hand-china.com 2020/11/25 22:16
     * @return java.lang.String
     */
    String queryProLineByWorkcellId(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 查询工序装配
     * 
     * @param tenantId
     * @param materialId
     * @param siteId
     * @param proLineId
     * @author sanfeng.zhang@hand-china.com 2020/11/25 22:26 
     * @return java.lang.String
     */
    String queryOperationAssembleFlag(@Param("tenantId") Long tenantId, @Param("materialId") String materialId, @Param("siteId") String siteId, @Param("proLineId") String proLineId);

    /**
     * 装配实绩
     *
     * @param tenantId
     * @param materialId
     * @param eoId
     * @return tarzan.actual.domain.entity.MtEoComponentActual
     * @author sanfeng.zhang@hand-china.com 2020/11/26 9:19
     */
    List<MtEoComponentActual> queryMtEoComponentActual(@Param("tenantId") Long tenantId, @Param("materialId") String materialId, @Param("eoId") String eoId);

    /**
     * bom组件id
     *
     * @param tenantId
     * @param eoId
     * @param materialId
     * @param processId
     * @author sanfeng.zhang@hand-china.com 2020/11/26 15:25
     * @return java.lang.String
     */
    List<String> queryCurrentBomComponentId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId, @Param("materialId") String materialId, @Param("processId") String processId);

    /**
     * 替代料找主键料
     *
     * @param tenantId
     * @param bomId
     * @param materialId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/16 15:21
     */
    List<String> queryPrimaryMaterial(@Param("tenantId") Long tenantId, @Param("bomId") String bomId, @Param("materialId") String materialId);

    /**
     * 根据eoId找工艺步骤
     *
     * @param tenantId
     * @param eoId
     * @param operationId
     * @return java.util.List<tarzan.method.domain.entity.MtRouterOperation>
     * @author sanfeng.zhang@hand-china.com 2020/12/16 19:04
     */
    List<MtRouterOperation> getRouterStepByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId, @Param("operationId") String operationId);

    /**
     * 根据eoId找工艺路线步骤对应工序
     *
     * @param tenantId
     * @param eoId
     * @return java.util.List<tarzan.method.domain.entity.MtRouterOperation>
     * @author sanfeng.zhang@hand-china.com 2020/12/23 10:58
     */
    List<MtRouterOperation> queryRouterOperationListByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 根据工单的routerId找工艺路线步骤对应工序
     *
     * @param tenantId
     * @param routerId
     * @return java.util.List<tarzan.method.domain.entity.MtRouterOperation>
     * @author sanfeng.zhang@hand-china.com 2020/12/23 17:58
     */
    List<MtRouterOperation> queryWoRouterOperationListByRouterId(@Param("tenantId") Long tenantId, @Param("routerId") String routerId);

    /**
     * 工位找对应工段下的货位id
     *
     * @param tenantId
     * @param workcellId
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author sanfeng.zhang@hand-china.com 2020/12/22 15:02
     */
    List<MtModLocator> queryLocatorIdByWorkcellId(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 根据eoId找返修标识
     *
     * @param tenantId 租户id
     * @param eoId     eoId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/1/7 19:27
     */
    String queryReworkFlagByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 不良虚拟库位
     *
     * @param tenantId          租户id
     * @param materialLotId      条码
     * @author sanfeng.zhang@hand-china.com 2021/1/14 10:23
     * @return java.util.List<java.lang.String>
     */
    List<String> queryNcStorageLocator(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 批量更新不良代码
     *
     * @param tenantId
     * @param userId
     * @param ncRecordList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/29 9:40
     */
    void batchUpdateNcCodeId(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("ncRecordList") List<MtNcRecord> ncRecordList);

    /**
     * 批量查询返修标识
     *
     * @param tenantId
     * @param eoIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeBatchNcCheckVO>
     * @author sanfeng.zhang@hand-china.com 2021/1/29 15:02
     */
    List<HmeBatchNcCheckVO> batchQueryReworkFlagByEoId(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 批量工位找工段
     *
     * @param tenantId
     * @param siteId
     * @param workcellIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeBatchNcCheckVO2>
     * @author sanfeng.zhang@hand-china.com 2021/2/1 11:08
     */
    List<HmeBatchNcCheckVO2> batchQueryLineWorkcellByWorkcell(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 根据序列号找eo
     *
     * @param tenantId
     * @param loadSequence
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/3/19 8:48
     */
    List<String> queryEoIdByLoadSequence(@Param("tenantId") Long tenantId, @Param("loadSequence") String loadSequence);

    /**
     * 根据EO查询产线
     *
     * @param tenantId
     * @param eoId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/3/31 16:12
     */
    List<String> queryProLineByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 从不良代码工艺路线的关系获取工艺路线
     *
     * @param tenantId
     * @param ncGroupId
     * @param ncCodeIdList
     * @param prodLineId
     * @param deviceType
     * @param chipType
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/3/31 16:37
     */
    List<String> queryRouteIdByNcCode(@Param("tenantId") Long tenantId,
                                      @Param("ncGroupId") String ncGroupId,
                                      @Param("ncCodeIdList") List<String> ncCodeIdList,
                                      @Param("prodLineId") String prodLineId,
                                      @Param("deviceType") String deviceType,
                                      @Param("chipType") String chipType,
                                      @Param("operationId") String operationId);
    /**
     * 批量删除装载信息
     *
     * @param tenantId
     * @param ncLoadList
     * @author sanfeng.zhang@hand-china.com 2021/4/21 14:47
     * @return void
     */
    void batchDeleteLoadByIds(@Param("tenantId") Long tenantId, @Param("ncLoadList") List<String> ncLoadList);

    /**
     * 产线下虚拟货位
     *
     * @param tenantId
     * @author sanfeng.zhang@hand-china.com 2021/4/21 15:04
     * @return java.util.List<java.lang.String>
     */
    List<String> queryNcStorageLocatorByProdLine(@Param("tenantId") Long tenantId, @Param("prodLineId") String prodLineId);

    /**
     * eo的最后正常工艺
     *
     * @param tenantId
     * @param eoId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/6/23
     */
    String queryLastNonReworkOperationId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 根据EO查询工单
     *
     * @param tenantId
     * @param eoId
     * @return tarzan.order.domain.entity.MtWorkOrder
     * @author sanfeng.zhang@hand-china.com 2021/6/23
     */
    MtWorkOrder queryWorkOrderByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /**
     * 更新工艺路线
     *
     * @param tenantId
     * @param routerIdList
     * @param userId
     * @param routerName
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/6/23
     */
    void updateRouterNameByRouterIdList(@Param("tenantId") Long tenantId, @Param("routerIdList") List<String> routerIdList, @Param("userId") Long userId, @Param("routerName") String routerName);

    /**
     * 工艺路线所有工艺及步骤识别码
     * @param tenantId
     * @param routerId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcCheckVO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/23
     */
    List<HmeNcCheckVO4> queryOperationIdAndStepName(@Param("tenantId") Long tenantId, @Param("routerId") String routerId);

    /**
     * 按工艺取工单工艺路线步骤组件
     * @param tenantId
     * @param routerId
     * @param operationNameList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNcCheckVO4>
     * @author sanfeng.zhang@hand-china.com 2021/6/24
     */
    List<HmeNcCheckVO4> queryRouterOperationComponent(@Param("tenantId") Long tenantId, @Param("routerId") String routerId, @Param("operationNameList") List<String> operationNameList);
}
