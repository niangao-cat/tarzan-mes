package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;
import com.ruike.wms.domain.entity.WmsDistributionDemand;
import com.ruike.wms.domain.vo.WmsDistributionDemandQueryVO;
import com.ruike.wms.domain.vo.WmsDistributionDemandVO;
import com.ruike.wms.domain.vo.WmsDistributionDemandQtyVO;
import com.ruike.wms.domain.vo.WmsDistributionQtyVO;
import com.ruike.wms.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 配送需求表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
public interface WmsDistributionDemandMapper extends BaseMapper<WmsDistributionDemand> {
    /**
     * 根据条件查询列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemand>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 04:22:18
     */
    List<WmsDistributionDemandVO> selectListByCondition(@Param("tenantId") Long tenantId, @Param("dto") WmsDistDemandQueryDTO dto);

    /**
     * 根据班次查询数量
     *
     * @param tenantId 租户
     * @param vo       条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDemandQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 02:17:38
     */
    List<WmsDistributionDemandQtyVO> selectQtyByShift(@Param("tenantId") Long tenantId, @Param("vo") WmsDistributionDemandQueryVO vo);

    /**
     * 根据需求查询现有量
     *
     * @param tenantId 租户
     * @param vo       条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDemandQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 02:17:38
     */
    BigDecimal selectInStockByDemand(@Param("tenantId") Long tenantId, @Param("vo") WmsDistributionDemandQueryVO vo);

    /**
     * 根据需求查询仓库库存
     *
     * @param tenantId 租户
     * @param vo       条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDemandQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 02:17:38
     */
    BigDecimal selectInventoryQtyByDemand(@Param("tenantId") Long tenantId, @Param("vo") WmsDistributionDemandQueryVO vo);

    /**
     * 根据唯一条件查询ID
     *
     * @param demand 唯一条件
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/2 04:31:53
     */
    List<WmsDistributionDemand> selectListByUniqueCondition(WmsDistributionDemand demand);

    /**
     * 批量查询线边库存
     *
     * @param tenantId       租户
     * @param siteId         站点
     * @param workcellIdList 工段
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 02:17:38
     */
    List<WmsDistributionQtyVO> selectWorkcellQtyBatch(@Param("tenantId") Long tenantId
            , @Param("siteId") String siteId
            , @Param("workcellIdList") List<String> workcellIdList);

    /**
     * 根据日期范围导出列表
     *
     * @param tenantId  租户
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param demandDate 需求日期
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDemandExportVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 03:52:53
     */
    List<WmsDistributionDemandExportVO> selectExportListByDateRange(@Param("tenantId") Long tenantId,
                                                                    @Param("startDate") LocalDate startDate,
                                                                    @Param("demandDate") LocalDate demandDate,
                                                                    @Param("endDate") LocalDate endDate,
                                                                    @Param("dto") WmsDistDemandQueryDTO dto);

    /**
     * 批量查询库存现有量
     *
     * @param tenantId 租户
     * @param siteId   站点
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 04:33:46
     */
    List<WmsDistributionQtyVO> selectBatchInventoryQty(@Param("tenantId") Long tenantId, @Param("siteId") String siteId);

    /**
     * 查询配送的已配送数量和库存数量
     *
     * @param tenantId
     * @param demandIdList
     * @author sanfeng.zhang@hand-china.com 2021/4/27 16:25
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDemandQtyVO>
     */
    List<WmsDistributionDemandQtyVO> selectQtyByDemandId(@Param("tenantId") Long tenantId, @Param("demandIdList") List<String> demandIdList);

    /**
     * 汇总物料派工数量
     * @param tenantId
     * @param docCreateSumVOS
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author sanfeng.zhang@hand-china.com 2021/6/30
     */
    List<WmsDistributionQtyVO> summaryMaterialDispatchQty(@Param("tenantId") Long tenantId, @Param("docCreateSumVOS") Set<WmsDistributionDocCreateSumVO> docCreateSumVOS);

    /**
     * 实际班次
     * @param tenantId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/6/30
     */
    List<String> queryCurrentWkcShift(@Param("tenantId") Long tenantId);

    /**
     * 查询job
     * @param tenantId
     * @param wkcShiftIdList
     * @param workOrderList
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/7/1
     */
    List<String> queryJobIdList(@Param("tenantId") Long tenantId, @Param("wkcShiftIdList") List<String> wkcShiftIdList, @Param("workOrderList") List<String> workOrderList);

    /** 
     * 物料汇总投料数量
     * @param tenantId
     * @param jobIdList
     * @param materialIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/1
     */
    List<WmsDistributionQtyVO> queryReleaseQtyByMaterialAndJob(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList, @Param("materialIdList") List<String> materialIdList);

    /**
     * 获取配送工单
     * @param tenantId
     * @param docCreateSumVOS
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/7/1
     */
    List<String> queryWorkOrderIdByDemandList(@Param("tenantId") Long tenantId, @Param("docCreateSumVOS") Set<WmsDistributionDocCreateSumVO> docCreateSumVOS);
}
