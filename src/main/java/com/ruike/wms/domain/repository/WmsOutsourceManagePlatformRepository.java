package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
* @Classname WmsOutsourceManagePlatformRepository
* @Description 外协管理平台
* @Date  2020/6/11 19:12
* @Created by Deng xu
*/
public interface WmsOutsourceManagePlatformRepository  {

    /**
    * @Description: 外协管理平台-查询单据头信息
    * @author: Deng Xu
    * @date 2020/6/11 20:46
    * @param tenantId 租户ID
    * @param condition 查询条件
    * @param pageRequest 分页信息
    * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    Page<WmsOutsourceOrderHeadVO> listHeadForUi(Long tenantId , WmsOutsourceOrderHeadVO condition , PageRequest pageRequest);

    /**
    * @Description: 外协管理平台-根据单据头ID查询行信息
    * @author: Deng Xu
    * @date 2020/6/12 9:48
    * @param tenantId 租户ID
    * @param sourceDocId 单据头ID
    * @param pageRequest 分页信息
    * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
    * @version 1.0
    */
    Page<WmsOutsourceOrderLineVO> listLineForUi(Long tenantId , String sourceDocId , PageRequest pageRequest);

    /**
     * 行明细
     *
     * @param tenantId
     * @param lineId
     * @param pageRequest
     * @author sanfeng.zhang@hand-china.com 2020/9/30 13:44
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderDetailsVO>
     */
    Page<WmsOutsourceOrderDetailsVO> listLineDetailForUi(Long tenantId , String lineId , PageRequest pageRequest);

    /**
    * @Description: 外协管理平台-退货单创建-查询头行信息
    * @author: Deng Xu
    * @date 2020/6/16 11:04
    * @param tenantId 租户ID
    * @param sourceDocId 外协发货单头ID
    * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    WmsOutsourceOrderHeadVO listLineForCreateReturnDoc(Long tenantId , String sourceDocId  );

    /**
    * @Description: 外协管理平台-退货单创建-查询单号
    * @author: Deng Xu
    * @date 2020/7/2 11:39
    * @param tenantId 租户ID
    * @return : com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
    * @version 1.0
    */
    WmsOutsourceOrderHeadVO createHeadDoc(Long tenantId );

    /**
    * @Description: 外协管理平台-退货单创建
    * @author: Deng Xu
    * @date 2020/6/16 13:43
    * @param tenantId 租户ID
    * @param createVo 创建VO
    * @return : com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
    * @version 1.0
    */
    WmsOutsourceOrderHeadVO createReturnDoc(Long tenantId , WmsOutsourceOrderHeadVO createVo);

    /**
     * 外协管理平台-补料单创建头查询
     *
     * @param tenantId
     * @param sourceDocId
     * @author sanfeng.zhang@hand-china.com 2020/10/26 14:36
     * @return com.ruike.wms.domain.vo.WmsReplenishmentOrderVO
     */
    WmsReplenishmentOrderVO createHeaderQuery(Long tenantId, String sourceDocId);

    /**
     * 创建补料单
     *
     * @param tenantId
     * @param wmsReplenishmentOrderVO
     * @author sanfeng.zhang@hand-china.com 2020/10/26 16:12
     * @return com.ruike.wms.domain.vo.WmsReplenishmentOrderVO
     */
    WmsReplenishmentOrderVO createReplenishment(Long tenantId, WmsReplenishmentOrderVO wmsReplenishmentOrderVO);

    /**
     * 关闭送货单
     *
     * @param tenantId
     * @param wmsInstructionVO2
     * @author sanfeng.zhang@hand-china.com 2020/10/26 17:47
     * @return com.ruike.wms.domain.vo.WmsInstructionVO2
     */
    WmsInstructionVO2 closeReturnDoc(Long tenantId, WmsInstructionVO2 wmsInstructionVO2);

    /**
     * 查询库存量
     *
     * @param tenantId
     * @param lineVO
     * @author sanfeng.zhang@hand-china.com 2020/10/29 20:11
     * @return
     */
    WmsReplenishmentOrderLineVO queryInventoryQuantity(Long tenantId, WmsReplenishmentOrderLineVO lineVO);

    /**
     * 外协发料单打印
     * 
     * @param tenantId
     * @param sourceDocIdList
     * @param response
     * @author sanfeng.zhang@hand-china.com 2020/10/3 11:31 
     * @return void
     */
    void outsourceCreatePdf(Long tenantId, List<String> sourceDocIdList, HttpServletResponse response);

    /**
     * 外协管理平台-导出
     *
     * @param tenantId
     * @param headVO
     * @author sanfeng.zhang@hand-china.com 2020/11/11 22:13
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutsourceExportVO>
     */
    List<WmsOutsourceExportVO> inventoryExcelExport(Long tenantId, WmsOutsourceOrderHeadVO headVO);
}
