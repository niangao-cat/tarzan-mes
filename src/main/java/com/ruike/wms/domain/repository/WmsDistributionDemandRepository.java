package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;
import com.ruike.wms.domain.vo.WmsDistributionDemandExportVO;
import com.ruike.wms.domain.vo.WmsDistributionDemandVO;
import com.ruike.wms.domain.vo.WmsDistributionQtyVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.wms.domain.entity.WmsDistributionDemand;

import java.time.LocalDate;
import java.util.List;


/**
 * 配送需求表资源库
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 11:09:51
 */
public interface WmsDistributionDemandRepository extends BaseRepository<WmsDistributionDemand> {
    /**
     * 根据条件查询列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页条件
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemand>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 04:22:18
     */
    Page<WmsDistributionDemandVO> selectListByCondition(Long tenantId, WmsDistDemandQueryDTO dto, PageRequest pageRequest);

    /**
     * 根据唯一条件查询ID
     *
     * @param demand 唯一条件
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/2 04:31:53
     */
    List<WmsDistributionDemand> selectListByUniqueCondition(WmsDistributionDemand demand);

    /**
     * 根据日期范围导出列表
     *
     * @param tenantId   租户
     * @param startDate  开始日期
     * @param demandDate 需求日期
     * @param endDate    结束日期
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDemandExportVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 03:52:53
     */
    List<WmsDistributionDemandExportVO> selectExportListByDateRange(Long tenantId, LocalDate startDate, LocalDate demandDate, LocalDate endDate, WmsDistDemandQueryDTO dto);

    /**
     * 批量查询库存现有量
     *
     * @param tenantId 租户
     * @param siteId   站点
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 04:33:46
     */
    List<WmsDistributionQtyVO> selectBatchInventoryQty(Long tenantId, String siteId);

    /**
     * 批量查询线边库存
     *
     * @param tenantId       租户
     * @param siteId         站点
     * @param workcellIdList 工段ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionQtyVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 02:17:38
     */
    List<WmsDistributionQtyVO> selectWorkcellQtyBatch(Long tenantId,
                                                      String siteId,
                                                      List<String> workcellIdList);

}
