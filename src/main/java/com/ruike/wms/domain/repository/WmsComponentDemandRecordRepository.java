package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.entity.WmsComponentDemandRecord;
import com.ruike.wms.domain.vo.WmsComponentDemandDateVO;
import com.ruike.wms.domain.vo.WmsComponentDemandSumVO;
import com.ruike.wms.domain.vo.WmsDistDemandDispatchRelVO;
import com.ruike.wms.domain.vo.WmsDistributionQtyVO;
import org.hzero.mybatis.base.BaseRepository;

import java.util.Date;
import java.util.List;

/**
 * 组件需求记录表资源库
 *
 * @author yonghui.zhu@hand-china.com 2020-08-24 14:00:05
 */
public interface WmsComponentDemandRecordRepository extends BaseRepository<WmsComponentDemandRecord> {

    /**
     * 从派工数据查询列表
     *
     * @param tenantId     租户
     * @param woDispatchId 工单派工ID
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 02:24:24
     */
    List<WmsComponentDemandRecord> selectListFromDispatch(Long tenantId,
                                                          String woDispatchId);

    /**
     * 根据派工ID查询配送关系
     *
     * @param tenantId     租户
     * @param woDispatchId 派工ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistDemandDispatchRelVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 09:52:25
     */
    List<WmsDistDemandDispatchRelVO> selectRelListByDispatchId(Long tenantId,
                                                               String woDispatchId);

    /**
     * 根据id列表批量查询
     *
     * @param tenantId 租户
     * @param idList   id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 02:24:24
     */
    List<WmsComponentDemandRecord> selectListByIds(Long tenantId,
                                                   List<String> idList);

    /**
     * 根据日期范围查询
     *
     * @param tenantId  租户
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 02:26:08
     */
    List<WmsComponentDemandRecord> selectNonCreatedListByDateRange(Long tenantId,
                                                                   Date startDate,
                                                                   Date endDate);

    /**
     * 根据日期范围查询
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 02:26:08
     */
    List<WmsComponentDemandSumVO> selectListByDateRange(Long tenantId,
                                                        String workOrderId,
                                                        Date startDate,
                                                        Date endDate);

    /**
     * 按照物料维度查询条码现有量
     *
     * @param tenantId 租户
     * @param siteId   站点
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 09:58:02
     */
    List<WmsDistributionQtyVO> selectBarcodeOnhandBySite(Long tenantId,
                                                         String siteId);

    /**
     * 以日期维度查询组件需求
     *
     * @param tenantId        租户
     * @param workOrderId     工单
     * @param startDate       开始日期
     * @param endDate         结束日期
     * @param materialId      物料ID
     * @param materialVersion 物料版本
     * @param workcellId      工段ID
     * @return java.util.List<com.ruike.wms.domain.entity.WmsComponentDemandRecord>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 02:26:08
     */
    List<WmsComponentDemandDateVO> selectRequirementWithDate(Long tenantId,
                                                             String workOrderId,
                                                             String materialId,
                                                             String materialVersion,
                                                             String workcellId,
                                                             Date startDate,
                                                             Date endDate);
}
