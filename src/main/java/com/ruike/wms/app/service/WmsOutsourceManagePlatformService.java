package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 外协管理平台 service
 *
 * @author Deng xu 2020/6/11 19:10
 */
public interface WmsOutsourceManagePlatformService {

    /**
     * 外协管理平台-查询单据头信息
     *
     * @param tenantId    租户ID
     * @param condition   查询条件
     * @param pageRequest 分页信息
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
     * @author Deng Xu 2020/6/11 20:51
     */
    Page<WmsOutsourceOrderHeadVO> listHeadForUi(Long tenantId, WmsOutsourceOrderHeadVO condition, PageRequest pageRequest);

    /**
     * 外协管理平台-根据单据头ID查询行信息
     *
     * @param tenantId    租户ID
     * @param sourceDocId 单据头ID
     * @param pageRequest 分页信息
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
     * @author Deng Xu 2020/6/12 9:46
     */
    Page<WmsOutsourceOrderLineVO> listLineForUi(Long tenantId, String sourceDocId, PageRequest pageRequest);

    /**
     * 行明细
     *
     * @param tenantId
     * @param lineId
     * @param pageRequest
     * @author sanfeng.zhang@hand-china.com 2020/9/30 13:43
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderDetailsVO>
     */
    Page<WmsOutsourceOrderDetailsVO> listLineDetailForUi(Long tenantId, String lineId, PageRequest pageRequest);


    /**
     * 外协管理平台-退货单创建-查询头行信息
     *
     * @param tenantId    租户ID
     * @param sourceDocId 外协送货单头ID
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
     */
    WmsOutsourceOrderHeadVO listLineForCreateReturnDoc(Long tenantId, String sourceDocId);

    /**
     * 外协管理平台-退货单创建-查询单号
     *
     * @param tenantId 租户ID
     * @return com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
     * @author Deng Xu 2020/7/2 11:37
     */
    WmsOutsourceOrderHeadVO createHeadDoc(Long tenantId);

    /**
     * 外协管理平台-退货单创建
     *
     * @param tenantId 租户ID
     * @param createVo 创建VO
     * @return com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
     * @author Deng Xu 2020/6/16 13:42
     */
    WmsOutsourceOrderHeadVO createReturnDoc(Long tenantId, WmsOutsourceOrderHeadVO createVo);

    /**
     * 外协管理平台-补料单创建头查询
     *
     * @param tenantId
     * @param sourceDocId
     * @author sanfeng.zhang@hand-china.com 2020/10/26 14:17
     * @return com.ruike.wms.domain.vo.WmsReplenishmentOrderVO
     */
    WmsReplenishmentOrderVO createHeaderQuery(Long tenantId, String sourceDocId);

    /**
     * 创建补料单
     *
     * @param tenantId
     * @param wmsReplenishmentOrderVO
     * @author sanfeng.zhang@hand-china.com 2020/10/26 16:05
     * @return com.ruike.wms.domain.vo.WmsReplenishmentOrderVO
     */
    WmsReplenishmentOrderVO createReplenishment(Long tenantId, WmsReplenishmentOrderVO wmsReplenishmentOrderVO);

    /**
     * 关闭外协发货单
     *
     * @param tenantId
     * @param wmsInstructionVO2
     * @author sanfeng.zhang@hand-china.com 2020/10/26 17:46
     * @return com.ruike.wms.domain.vo.WmsInstructionVO2
     */
    WmsInstructionVO2 closeReturnDoc(Long tenantId, WmsInstructionVO2 wmsInstructionVO2);

    /**
     * 外协发料单打印
     * 
     * @param tenantId
     * @param sourceDocIdList
     * @param response
     * @author sanfeng.zhang@hand-china.com 2020/10/3 11:15 
     * @return void
     */
    void outsourceCreatePdf(Long tenantId, List<String> sourceDocIdList, HttpServletResponse response);

    /**
     * 查询库存量
     *
     * @param tenantId
     * @param lineVO
     * @author sanfeng.zhang@hand-china.com 2020/10/29 20:10
     * @return
     */
    WmsReplenishmentOrderLineVO queryInventoryQuantity(Long tenantId, WmsReplenishmentOrderLineVO lineVO);

    /**
     * 外协管理平台-导出
     *
     * @param tenantId
     * @param headVO
     * @author sanfeng.zhang@hand-china.com 2020/11/11 22:11
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutsourceExportVO>
     */
    List<WmsOutsourceExportVO> inventoryExcelExport(Long tenantId, WmsOutsourceOrderHeadVO headVO);
}
