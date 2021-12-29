package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 产线日明细报表Mapper
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:20
 */
public interface HmeProLineDetailsMapper {

    /***
     * @Description 车间区域信息 查询
     * @return java.util.List<tarzan.modeling.domain.entity.MtModArea>
     * @auther chaonan.hu
     * @date 2020/7/7
     */
    List<MtModArea> queryModAreaList();

    /***
     * @Description 产线日明细信息 查询
     *
     * @param tenantId 租户id
     * @param params 查询条件
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     * @auther chaonan.hu
     * @date 2020/7/7
     */
    List<HmeProductionLineDetailsDTO> queryDetails(@Param("tenantId") Long tenantId,@Param("params") HmeProductionLineDetailsVO params);

    List<MtModWorkcell> selectWorkcells(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "workcellIds") List<String> workcellIds);

    /**
     * 功能描述: 在制查询报表 查询
     *
     * @param siteId     工厂
     * @param prodLineId 产线编号
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionQueryDTO>
     * @author bao.xu@hand-china.com 2020/7/13 11:32
     */
    List<HmeProductionQueryDTO> queryProductDetails(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("prodLineId") String prodLineId,
                                                    @Param("productType") String productType, @Param("productClassification") String productClassification,
                                                    @Param("productCode") String productCode, @Param("productModel") String productModel);

    /**
     * 查询待上线数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectQueueNumByMaterialList(@Param("tenantId") Long tenantId,
                                               @Param("prodLineId") String prodLineId,
                                               @Param("siteId") String siteId,
                                               @Param("materialIdList") List<String> materialIdList);

    /**
     * 查询未入库库存数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectUnCountByMaterialList(@Param("tenantId") Long tenantId,
                                              @Param("prodLineId") String prodLineId,
                                              @Param("materialIdList") List<String> materialIdList);

    MtModWorkcell queryWorkcellsByTypeStation(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellId") String workcellId);

    /**
     * Description: 运行数量和完成数量 查询
     *
     * @param tenantId
     * @param workcellIds
     * @param materialId
     * @return 汇总结果
     * @author bao.xu@hand-china.com 2020/7/14 11:13
     */
    List<Map<String, Object>> queryWorkingQTYAndCompletedQTY(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "workcellIds") List<String> workcellIds, @Param(value = "materialId") String materialId);

    /**
     * 批量查询运行数量和完成数量 查询
     *
     * @param tenantId
     * @param prodLineId
     * @param siteId
     * @param materialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/8 19:05
     */
    List<HmeProductDetailsVO> batchQueryWorkingQTYAndCompletedQTY(@Param(value = "tenantId") Long tenantId,
                                                                  @Param("siteId") String siteId,
                                                                  @Param(value = "prodLineId") String prodLineId,
                                                                  @Param(value = "materialIdList") List<String> materialIdList);

    /**
     * Description: 工序运行数量和完成数量 查询
     *
     * @param tenantId
     * @param workcellIds
     * @param materialId
     * @return 汇总结果
     * @author bao.xu@hand-china.com 2020/7/14 11:13
     */
    List<Map<String, Object>> queryWorkingQTYAndCompletedQTYByProcess(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "workcellIds") List<String> workcellIds, @Param(value = "materialId") String materialId);

    /**
     * 在制报表-eo信息(运行)
     *
     * @param tenantId         租户id
     * @param workcellId       工序id
     * @param materialId       物料id
     * @param eoIdentification eo标识
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:12
     */
    List<HmeProductEoInfoVO> queryProductEoListByRun(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "workcellId") String workcellId, @Param(value = "materialId") String materialId, @Param("eoIdentification") String eoIdentification, @Param("siteId") String siteId);

    /**
     * 在制报表-eo信息（库存）
     *
     * @param tenantId          租户id
     * @param workcellId        工序id
     * @param materialId        物料id
     * @param eoIdentification  eo标识
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:12
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    List<HmeProductEoInfoVO> queryProductEoListByFinish(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "workcellId") String workcellId, @Param(value = "materialId") String materialId, @Param("eoIdentification") String eoIdentification, @Param("siteId") String siteId);

    /**
     * 在制报表-eo信息（待上线）
     *
     * @param tenantId         租户id
     * @param productionLineId 产线id
     * @param materialId       物料id
     * @param eoIdentification eo标识
     * @param siteId           站点id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:10
     */
    List<HmeProductEoInfoVO> queryProductEoListByQueueQty(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "productionLineId") String productionLineId, @Param(value = "materialId") String materialId, @Param("eoIdentification") String eoIdentification, @Param("siteId") String siteId);

    /**
     * 在制报表-eo信息（未入库）
     *
     * @param tenantId
     * @param productionLineId
     * @author sanfeng.zhang@hand-china.com 2020/9/4 15:28
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    List<HmeProductEoInfoVO> queryProductEoListByNoCount(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "productionLineId") String productionLineId, @Param(value = "materialId") String materialId);

    /**
     * id查询工序信息
     *
     * @param tenantId      租户id
     * @param processIds     工位id
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:09
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> queryProcessInfoListByIds(@Param(value = "tenantId") Long tenantId,@Param(value = "processIds") String processIds);


    /**
     * 获取最小的开班时间
     *
     * @param tenantId          租户id
     * @param shiftDate         班次时间
     * @param lineWorkcellId    工段
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:08
     * @return java.util.Date
     */
    Date queryMinShiftStart(@Param(value = "tenantId") Long tenantId,@Param(value = "shiftDate") String shiftDate,@Param("lineWorkcellId") String lineWorkcellId);


    /**
     * 获取最大的结班时间
     *
     * @param tenantId          租户id
     * @param shiftDate         班次时间
     * @param lineWorkcellId    工段
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:08
     * @return java.util.Date
     */
    List<Date> queryMaxShiftEnd(@Param(value = "tenantId") Long tenantId,@Param(value = "shiftDate") String shiftDate,@Param("lineWorkcellId") String lineWorkcellId);


    /**
     * 取首道/末道工序
     *
     * @param tenantId          租户id
     * @param processIdList     工序id
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:07
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> queryFirstAndEndProcess(@Param(value = "tenantId") Long tenantId,@Param(value = "processIdList") List<String> processIdList);


    /**
     * 获取工序的物料批Id
     *
     * @param tenantId          租户id
     * @param workOrderId       生产指令
     * @param materialId        物料id
     * @param processIdList     工序
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:07
     * @return java.lang.Integer
     */
    List<String> queryProcessQty(@Param(value = "tenantId") Long tenantId,@Param(value = "workOrderId") String workOrderId,@Param(value = "materialId") String materialId,@Param(value = "processIdList") List<String> processIdList,@Param(value = "siteInDateFrom") Date siteInDateFrom,@Param(value = "siteInDateTo") Date siteInDateTo);


    /**
     * 产量日明细-班次
     *
     * @param tenantId      租户id
     * @param params        查询参数
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:06
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     */
    List<HmeProductionLineDetailsDTO> queryProductShiftList(@Param("tenantId") Long tenantId,@Param("params") HmeProductionLineDetailsVO params);


    /**
     * 获取Eo的返修标识
     *
     * @param tenantId              租户id
     * @param eoIdentification      eo标识
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:05
     * @return java.lang.String
     */
    HmeProductEoInfoVO queryReworkFlag(@Param("tenantId") Long tenantId,@Param("eoIdentification") String eoIdentification);

    /**
     * 投产信息
     *
     * @param tenantId          租户id
     * @param materialId        物料id
     * @param workOrderId       生产指令
     * @param workcellIdList    工位
     * @param shiftStartTime    开始时间
     * @param shiftEndTime      结班时间
     * @author sanfeng.zhang@hand-china.com 2020/7/31 16:29
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    List<HmeProductEoInfoVO> queryProductProcessEoList(@Param(value = "tenantId") Long tenantId,@Param(value = "materialId") String materialId,@Param("workOrderId") String workOrderId,@Param(value = "workcellIdList") List<String> workcellIdList, @Param("shiftStartTime") String shiftStartTime , @Param("shiftEndTime") String shiftEndTime);

    /**
     * 工段查询产线 车间数据
     *
     * @param tenantId
     * @param lineWorkcellId
     * @author sanfeng.zhang@hand-china.com 2020/8/3 11:11
     * @return com.ruike.hme.api.dto.HmeProductionLineDetailsDTO
     */
    HmeProductionLineDetailsDTO queryLineWorkcellUpIdInfo(@Param(value = "tenantId") Long tenantId,@Param(value = "lineWorkcellId") String lineWorkcellId);

    /**
     * 根据工单查询工序
     *
     * @param tenantId
     * @param workOrderId
     * @author sanfeng.zhang@hand-china.com 2020/8/4 11:31
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessInfoVO>
     */
    List<HmeProcessInfoVO> queryProcessByWorkOderId(@Param(value = "tenantId") Long tenantId,@Param(value = "workOrderId") String workOrderId);

    /**
     * 产线下工序列表
     *
     * @param tenantId
     * @param proLineId
     * @author sanfeng.zhang@hand-china.com 2020/8/4 12:29
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> queryOrderProcessListByProLineId(@Param(value = "tenantId") Long tenantId,@Param(value = "proLineId") String proLineId);

    /**
     * 批量更新EO
     *
     * @param tenantId      租户id
     * @param eoList        Eo列表
     * @param userId
     * @author sanfeng.zhang@hand-china.com 2020/9/14 20:33
     * @return void
     */
    void batchUpdateMtEo(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("eoList") List<MtEo> eoList);

    /**
     * 查询工段工序工位
     *
     * @param tenantId 租户id
     * @param params
     * @return java.util.List<String>
     * @author 2020-9-16 14:44:10 yifan.xiong
     */
    List<String> queryWorkcellIdList(@Param("tenantId") Long tenantId, @Param("params") MtModOrganizationVO2 params);

    /**
     * 批量查询返修状态
     *
     * @param tenantId
     * @param eoIdentificationList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/2 16:42
     */
    List<HmeProductEoInfoVO> batchReworkFlagQuery(@Param("tenantId") Long tenantId, @Param("eoIdentificationList") List<String> eoIdentificationList);
}
