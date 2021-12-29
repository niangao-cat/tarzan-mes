package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsPoDeliveryRelDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryScanLineReturnDTO;
import com.ruike.wms.api.dto.WmsPoDeliveryScanReturnDTO;
import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtInstructionActual;

import java.math.BigDecimal;
import java.util.List;

/**
 * 送货单行与采购订单行关系表Mapper
 *
 * @author han.zhang03@hand-china.com 2020-03-27 18:46:38
 */
public interface WmsPoDeliveryRelMapper extends BaseMapper<WmsPoDeliveryRel> {
    /**
     * 查询送货单
     *
     * @param tenantId
     * @param mtPoDeliveryVO
     * @return MtPoDeliveryVO2
     */
    List<WmsPoDeliveryVO2> selectPoDeliveryByCondition(@Param(value = "tenantId") Long tenantId, WmsPoDeliveryVO mtPoDeliveryVO);

    List<String> selectPoNumbers(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsPoDeliveryVO mtPoDeliveryVO);

    /**
     * @param poId     采购订单头Id
     * @param poLineId 采购订单行id
     * @return java.lang.Double
     * @Description 查询 关联该采购订单的状态为RELEASED的送货单的制单数量之和
     * @Date 2020-04-27 20:45
     * @Author han.zhang
     */
    Double selectPoQuantity(@Param(value = "poId") String poId, @Param(value = "poLineId") String poLineId);

    /**
     * @param tenantId 1
     * @param dto      2
     * @return : java.lang.String
     * @Description: 在wms_material_lot_doc_rel表查询批次
     * @author: tong.li
     * @date 2020/6/10 20:01
     * @version 1.0
     */
    String selectLot(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") WmsPoDeliveryRelDTO dto);

    /**
     * @param poId     采购订单头Id
     * @param poLineId 采购订单行id
     * @return java.lang.Double
     * @Description 查询关联该采购订单的状态为下达-RELEASED和接收执行-RECEIVE_EXECUTE和接收完成RECEIVE_COMPLETE的送货单的制单数量之和
     * @Date 2020-08-27 11:16
     * @Author penglin.sui
     */
    Double selectPoQuantityOfReleaseReceive(@Param(value = "poId") String poId, @Param(value = "poLineId") String poLineId);

    /**
     * @param poId     采购订单头Id
     * @param poLineId 采购订单行id
     * @return java.lang.Double
     * @Description 查询关联该采购订单的状态为下达-RELEASED和接收执行-RECEIVE_EXECUTE和接收完成RECEIVE_COMPLETE的送货单的制单数量之和
     * @Date 2020-08-27 11:16
     * @Author penglin.sui
     */
    Double selectPoQuantityOfStockInComplete(@Param(value = "poId") String poId, @Param(value = "poLineId") String poLineId);

    /**
     * 已制单数量
     *
     * @param poId     采购订单头Id
     * @param poLineId 购订单行id
     * @param typeList 单据类型
     * @return java.lang.Double
     * @author sanfeng.zhang@hand-china.com 2020/9/7 16:15
     */
    Double selectPoQuantityOfReceiveComplete(@Param(value = "poId") String poId, @Param(value = "poLineId") String poLineId, @Param("typeList") List<String> typeList);

    /**
     * 状态为RECEIVE_COMPLETE-接收完成的送货单
     *
     * @param poId     采购订单头Id
     * @param poLineId 购订单行id
     * @return java.lang.Double
     * @author sanfeng.zhang@hand-china.com 2020/9/7 16:15
     */
    Double selectPoQuantityOfComplete(@Param(value = "poId") String poId, @Param(value = "poLineId") String poLineId);


    /**
     * @param tenantId              租户ID
     * @param deliveryDocId         头ID
     * @param deliveryDocLineIdList 行ID列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsPoDeliveryRel>
     * @Description 查询接收数量
     * @author yuchao.wang
     * @date 2020/8/31 14:18
     */
    List<WmsPoDeliveryRel> queryReceiveQty(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "deliveryDocId") String deliveryDocId,
                                           @Param(value = "deliveryDocLineIdList") List<String> deliveryDocLineIdList);

    /**
     * @param tenantId          租户ID
     * @param instructionType   指令移动类型
     * @param instructionIdList 来源指令id列表
     * @return java.util.List<tarzan.actual.domain.entity.MtInstructionActual>
     * @Description 查询实绩数量
     * @author yuchao.wang
     * @date 2020/8/31 14:42
     */
    List<MtInstructionActual> queryActualQty(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "instructionType") String instructionType,
                                             @Param(value = "instructionIdList") List<String> instructionIdList);

    /**
     * @param updateList 参数
     * @return void
     * @Description 批量更新接收数量
     * @author yuchao.wang
     * @date 2020/8/31 16:08
     */
    void batchUpdateReceivedQty(@Param(value = "userId") Long userId, @Param(value = "updateList") List<WmsPoDeliveryRel> updateList);

    /**
     * 根据行Id查询明细数据
     *
     * @param tenantId      租户ID
     * @param instructionId 行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDeliveryDocVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/10 14:05:42
     */
    List<WmsDeliveryDocVO> detailQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionId") String instructionId);

    /**
     * 查询现存量
     *
     * @param tenantId   租户id
     * @param materialId 物料
     * @param supplierId 供应商
     * @param locatorId  货位
     * @param lotCode    批次
     * @return java.lang.Double
     * @author sanfeng.zhang@hand-china.com 2020/9/15 18:55
     */
    BigDecimal propertyLimitSumOnhandQtyGet(@Param(value = "tenantId") Long tenantId, @Param(value = "materialId") String materialId, @Param(value = "supplierId") String supplierId, @Param(value = "locatorId") String locatorId, @Param("lotCode") String lotCode);

    /**
     * 制单数量
     *
     * @param tenantId
     * @param instructionIdList
     * @return java.math.BigDecimal
     * @author sanfeng.zhang@hand-china.com 2020/9/15 19:14
     */
    BigDecimal queryModeQty(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionIdList") List<String> instructionIdList);

    /**
     * 执行数量
     *
     * @param tenantId
     * @param actualIdList
     * @return java.math.BigDecimal
     * @author sanfeng.zhang@hand-china.com 2020/9/15 19:15
     */
    BigDecimal queryExchangedQty(@Param(value = "tenantId") Long tenantId, @Param(value = "actualIdList") List<String> actualIdList);

    /**
     * 根据头id查询行id
     *
     * @param tenantId  租户id
     * @param docIdList 头id
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/9/17 9:54
     */
    List<String> propertyInstructionListQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "docIdList") List<String> docIdList, @Param(value = "materialId") String materialId, @Param(value = "materialVersion") String materialVersion);

    /**
     * 实绩actualId
     *
     * @param tenantId
     * @param docIdList
     * @param materialId
     * @param materialVersion
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/9/17 10:03
     */
    List<String> propertyActualListQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "docIdList") List<String> docIdList, @Param(value = "materialId") String materialId, @Param(value = "materialVersion") String materialVersion);

    /**
     * 根据单据号查询单据信息
     *
     * @param tenantId          租户
     * @param instructionDocNum 单据号
     * @return com.ruike.wms.api.dto.WmsPoDeliveryScanReturnDTO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/1 04:05:21
     */
    WmsPoDeliveryScanReturnDTO selectDocInfoByNum(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionDocNum") String instructionDocNum);

    /**
     * 根据单据ID查询单据行详情
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.api.dto.WmsPoDeliveryScanReturnDTO.WmsPoDeliveryScanLineReturnDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/1 04:35:17
     */
    List<WmsPoDeliveryScanLineReturnDTO> selectLineByDocId(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionDocId") String instructionDocId);

    /**
     * 获取免检标识
     *
     * @param tenantId
     * @param siteId
     * @param materialId
     * @author sanfeng.zhang@hand-china.com 2020/10/12 19:49
     * @return java.util.List<java.lang.String>
     */
    List<String> getFreeCheckFlag(@Param(value = "tenantId") Long tenantId, @Param(value = "siteId") String siteId, @Param(value = "materialId") String materialId);

    /**
     * 根据条码和单据号查询送货单详情
     *
     * @param tenantId          租户
     * @param instructionDocNum 送货单号
     * @param materialLotId     条码ID
     * @return com.ruike.wms.domain.vo.WmsInstructionAttrVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/2 11:25:23
     */
    WmsInstructionAttrVO selectLineByBarcodeAndDocNum(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionDocNum") String instructionDocNum, @Param(value = "materialLotId") String materialLotId);


    /**
     * 查询送货单物料批信息
     *
     * @param tenantId
     * @param docLineId
     * @author sanfeng.zhang@hand-china.com 2020/10/12 16:01
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotLineVO>
     */
    List<WmsMaterialLotLineVO> instructionMaterialLotQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "docLineId") String docLineId);

    /**
     * 查询质检单未NG的数量
     *
     * @param tenantId
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2020/11/4 21:24
     * @return java.lang.Double
     */
    Double selectNgQty(@Param("tenantId") Long tenantId, @Param(value = "instructionId") String instructionId);

    List<String> queryLocatorBySite(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("locatorType") String locatorType);

    List<WmsPoDeliveryRelVO> selectByDeliveryDocLineId(@Param("tenantId")Long tenantId,
                                                     @Param("instructionIdList")List<String> instructionIdList);
}
