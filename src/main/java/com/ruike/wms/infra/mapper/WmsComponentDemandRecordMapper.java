package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.entity.WmsComponentDemandRecord;
import com.ruike.wms.domain.vo.WmsComponentDemandDateVO;
import com.ruike.wms.domain.vo.WmsComponentDemandSumVO;
import com.ruike.wms.domain.vo.WmsDistDemandDispatchRelVO;
import com.ruike.wms.domain.vo.WmsDistributionQtyVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 组件需求记录表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
public interface WmsComponentDemandRecordMapper extends BaseMapper<WmsComponentDemandRecord> {

    /**
     * 从派工数据查询列表
     *
     * @param tenantId     租户
     * @param woDispatchId 工单派工ID
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 02:24:24
     */
    List<WmsComponentDemandRecord> selectListFromDispatch(@Param("tenantId") Long tenantId,
                                                          @Param("woDispatchId") String woDispatchId);

    /**
     * 根据id列表批量查询
     *
     * @param tenantId 租户
     * @param idList   id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 02:24:24
     */
    List<WmsComponentDemandRecord> selectListByIds(@Param("tenantId") Long tenantId,
                                                   @Param("idList") List<String> idList);

    /**
     * 根据派工ID查询配送关系
     *
     * @param tenantId     租户
     * @param woDispatchId 派工ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistDemandDispatchRelVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 09:52:25
     */
    List<WmsDistDemandDispatchRelVO> selectRelListByDispatchId(@Param("tenantId") Long tenantId,
                                                               @Param("woDispatchId") String woDispatchId);

    /**
     * 根据日期范围查询未生成配送需求的列表
     *
     * @param tenantId  租户
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 02:26:08
     */
    List<WmsComponentDemandRecord> selectListByDateRange(@Param("tenantId") Long tenantId,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate);

    /**
     * 按照物料维度查询条码现有量
     *
     * @param tenantId 租户
     * @param siteId   站点
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 09:58:02
     */
    List<WmsDistributionQtyVO> selectBarcodeOnhandBySite(@Param("tenantId") Long tenantId,
                                                         @Param("siteId") String siteId);
    List<WmsComponentDemandRecord> selectNonCreatedListByDateRange(@Param("tenantId") Long tenantId,
                                                                   @Param("startDate") Date startDate,
                                                                   @Param("endDate") Date endDate);

    /**
     * 根据日期范围查询汇总需求
     *
     * @param tenantId    租户
     * @param workOrderId 工单ID
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 02:26:08
     */
    List<WmsComponentDemandSumVO> selectSummaryListByDateRange(@Param("tenantId") Long tenantId,
                                                               @Param("workOrderId") String workOrderId,
                                                               @Param("startDate") Date startDate,
                                                               @Param("endDate") Date endDate);

    /**
     * 以日期维度查询组件需求
     *
     * @param tenantId        租户
     * @param workOrderId     工单ID
     * @param startDate       开始日期
     * @param endDate         结束日期
     * @param materialId      物料ID
     * @param materialVersion 物料版本
     * @param workcellId      工段ID
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 02:26:08
     */
    List<WmsComponentDemandDateVO> selectRequirementWithDate(@Param("tenantId") Long tenantId,
                                                             @Param("workOrderId") String workOrderId,
                                                             @Param("materialId") String materialId,
                                                             @Param("materialVersion") String materialVersion,
                                                             @Param("workcellId") String workcellId,
                                                             @Param("startDate") Date startDate,
                                                             @Param("endDate") Date endDate);
}
