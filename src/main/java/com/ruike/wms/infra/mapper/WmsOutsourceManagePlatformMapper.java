package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.entity.MtInstruction;

import java.math.BigDecimal;
import java.util.List;

/**
* @Classname WmsOutsourceManagePlatformMapper
* @Description 外协管理平台 mapper
* @Date  2020/6/11 19:50
* @Created by Deng xu
*/
public interface WmsOutsourceManagePlatformMapper {

    /**
    * @Description: 查询外协单头信息
    * @author: Deng Xu
    * @date 2020/6/11 20:43
    * @param condition 查询条件
    * @return : java.util.List<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
    * @version 1.0
    */
    List<WmsOutsourceOrderHeadVO> queryOutsourceDoc(WmsOutsourceOrderHeadVO condition);

    /**
    * @Description: 查询外协单行信息
    * @author: Deng Xu
    * @date 2020/6/12 10:24
    * @param condition 查询条件
    * @return : java.util.List<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
    * @version 1.0
    */
    List<WmsOutsourceOrderLineVO> queryLineByDocId(WmsOutsourceOrderLineVO condition);

    /**
    * @Description: 查询采购订单行号
    * @author: Deng Xu
    * @date 2020/6/12 10:53
    * @param condition 查询条件（单头ID、行ID）
    * @return : java.util.List<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
    * @version 1.0
    */
    List<WmsOutsourceOrderLineVO> queryPoNum(WmsOutsourceOrderLineVO condition);

    /**
    * @Description: 退货单创建-查询行信息
    * @author: Deng Xu
    * @date 2020/6/16 11:07
    * @param condition 查询条件
    * @return : java.util.List<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
    * @version 1.0
    */
    List<WmsOutsourceOrderLineVO> listLineForCreateReturnDoc(WmsOutsourceOrderLineVO condition);

    /**
    * @Description: 通过站点查询外协仓的库存
    * @author: Deng Xu
    * @date 2020/7/1 14:01
    * @param condition 查询条件（物料、站点、供应商、租户ID）
    * @return : java.math.BigDecimal
    * @version 1.0
    */
    BigDecimal queryMaterialLocatorQty(WmsOutsourceOrderHeadVO condition);

    /**
    * @Description: 通过物料查询状态为下达的外协退料单的该物料的制单数量
    * @author: Deng Xu
    * @date 2020/7/1 13:49
    * @param tenantId 租户ID
    * @param materialId 物料ID
    * @return : java.math.BigDecimal
    * @version 1.0
    */
    BigDecimal queryMaterialReleasedQty(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "materialId") String materialId);

    /**
     * 查询采购订单行号
     *
     * @param tenantId
     * @param instructionDocId              头id
     * @author sanfeng.zhang@hand-china.com 2020/9/30 12:34
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
     */
    List<WmsOutsourceOrderLineVO> queryPoNumByDocId(@Param(value = "tenantId") Long tenantId,@Param(value = "instructionDocId") String instructionDocId);

    /**
     * 库存数量
     *
     * @param tenantId
     * @param lineIdList
     * @author sanfeng.zhang@hand-china.com 2020/10/26 12:29
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInventoryVO>
     */
    List<WmsInventoryVO> batchQueryLineInventoryQty(@Param(value = "tenantId") Long tenantId, @Param("lineIdList") List<String> lineIdList);

    /**
     * 仓库库存量
     *
     * @param tenantId
     * @param deliveryWarehouseId
     * @author sanfeng.zhang@hand-china.com 2020/10/29 20:04
     * @return
     */
    List<WmsReplenishmentOrderLineVO> queryInventoryQty(@Param("tenantId") Long tenantId, @Param("deliveryWarehouseId") String deliveryWarehouseId);

    /**
     * 外协补料行信息
     *
     * @param tenantId
     * @param instructionDocId
     * @param lineIdList
     * @author sanfeng.zhang@hand-china.com 2020/10/26 15:10
     * @return java.util.List<com.ruike.wms.domain.vo.WmsReplenishmentOrderLineVO>
     */
    List<WmsReplenishmentOrderLineVO> queryReplenishmentOrderLine(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionDocId") String instructionDocId, @Param(value = "lineIdList") List<String> lineIdList);

    /**
     * 查询单据下已扫描条码数量
     *
     * @param tenantId
     * @param instructionDocId
     * @author sanfeng.zhang@hand-china.com 2020/10/26 18:48
     * @return java.lang.Integer
     */
    Integer queryScannedMaterialLot(@Param(value = "tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 超发库存
     *
     * @param tenantId
     * @param sourceDocId
     * @param materialId
     * @param materialVersion
     * @author sanfeng.zhang@hand-china.com 2020/10/26 19:17
     * @return java.util.List<tarzan.instruction.domain.entity.MtInstruction>
     */
    List<MtInstruction> queryOutsourceInstruction(@Param(value = "tenantId") Long tenantId, @Param("sourceDocId") String sourceDocId,  @Param("materialId") String materialId, @Param("materialVersion") String materialVersion);

    /**
     * 行明细
     *
     * @param tenantId
     * @param lineId
     * @author sanfeng.zhang@hand-china.com 2020/9/30 13:49
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutsourceOrderDetailsVO>
     */
    List<WmsOutsourceOrderDetailsVO> listLineDetailForUi(@Param(value = "tenantId") Long tenantId, @Param(value = "lineId") String lineId);

    /**
     * 查询外协管理平台-导出数据
     *
     * @param tenantId
     * @param condition
     * @author sanfeng.zhang@hand-china.com 2020/11/12 9:16
     * @return java.util.List<com.ruike.wms.domain.vo.WmsOutsourceExportVO>
     */
    List<WmsOutsourceExportVO> queryInventoryExportInfo(@Param(value = "tenantId") Long tenantId, @Param("condition") WmsOutsourceOrderHeadVO condition);
}
