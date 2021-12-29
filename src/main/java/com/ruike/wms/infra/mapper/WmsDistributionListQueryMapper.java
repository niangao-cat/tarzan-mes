package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配送单查询 Mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/3 03:33:26
 */
public interface WmsDistributionListQueryMapper {
    /**
     * @param vo
     * @return List<HmeDistributionListQueryVO>
     * @Description 配送单头查询
     * @Date 2020-9-2 15:53:08
     * @Created by yifan.xiong
     */
    List<WmsDistributionListQueryVO> selectDistributionDoc(WmsDistributionListQueryVO vo);

    /**
     * @param sourceDocId
     * @return List<HmeDistributionListQueryVO1>
     * @Description 配送单行查询
     * @Date 2020-9-2 15:53:08
     * @Created by yifan.xiong
     */
    List<WmsDistributionListQueryVO1> selectDistribution(@Param(value = "tenantId") Long tenantId, @Param("sourceDocId") String sourceDocId);

    /**
     * @param instructionId
     * @return List<HmeDistributionListQueryVO2>
     * @Description 配送单明细查询
     * @Date 2020-9-3 09:49:32
     * @Created by yifan.xiong
     */
    List<WmsDistributionListQueryVO2> selectDistributionDtl(@Param(value = "tenantId") Long tenantId, @Param("instructionId") String instructionId);

    /**
     * @param instructionDocId
     * @return WmsDeliveryPrintVO
     * @Description 送货单打印头信息查询
     * @Date 2020-9-7 14:38:14
     * @Created by yifan.xiong
     */
    WmsDeliveryPrintVO selectDeliveryPrintHead(@Param(value = "tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * @param instructionDocId
     * @return WmsDeliveryPrintVO
     * @Description 送货单打印行信息查询
     * @Date 2020-9-7 14:38:14
     * @Created by yifan.xiong
     */
    List<WmsDeliveryPrintVO1> selectDeliveryPrintLine(@Param(value = "tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 批量获取线边仓库存
     *
     * @param tenantId
     * @param siteId
     * @param workCellId
     * @param materialIdList
     * @return java.util.List<WmsBatchDistributionDemandVO>
     * @author sanfeng.zhang@hand-china.com 2020/10/20 16:20
     */
    List<WmsBatchDistributionDemandVO> selectBatchInStockByDemand(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("workCellId") String workCellId, @Param("materialIdList") List<String> materialIdList);

    /**
     * 仓库库存
     *
     * @param tenantId
     * @param siteId
     * @param materialIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsBatchDistributionDemandVO>
     * @author sanfeng.zhang@hand-china.com 2020/10/20 16:58
     */
    List<WmsBatchDistributionDemandVO> selectBatchInventoryQtyByDemand(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("materialIdList") List<String> materialIdList);

    /**
     * 查询补料单行
     *
     * @param tenantId  租户
     * @param docIdList 配送单ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReplenishmentLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/26 07:03:50
     */
    List<WmsReplenishmentLineVO> selectReplenishLineByDocIds(@Param("tenantId") Long tenantId, @Param("docIdList") List<String> docIdList);

    /**
     * 配送单导出
     *
     * @param tenantId
     * @param instructionDocIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDocVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<WmsDistributionDocVO> instructionDocExport(@Param("tenantId") Long tenantId, @Param("instructionDocIdList") List<String> instructionDocIdList);
}
