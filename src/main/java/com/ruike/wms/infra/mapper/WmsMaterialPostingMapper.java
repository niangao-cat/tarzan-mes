package com.ruike.wms.infra.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.ruike.wms.domain.vo.*;

import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

/**
 * WmsMaterialPostingMapper
 *
 * @author liyuan.lv@hand-china.com 2020/06/13 15:57
 */
public interface WmsMaterialPostingMapper {

    /**
     * 获取指令数据
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<WmsInstructionLineVO> selectInstructionByCondition(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "dto") WmsMaterialPostingVO dto);

    /**
     * 根据指令获取转移数据
     *
     * @param tenantId 租户
     * @param idList   指令ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/4 05:10:41
     */
    List<WmsInstructionLineVO> selectTransInstructionByIdList(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "idList") List<String> idList);

    /**
     * 根据送货单行获取采购订单信息
     *
     * @param tenantId   租户
     * @param deliveryId 送货单行
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDeliveryPoRelVo>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 10:01:10
     */
    List<WmsDeliveryPoRelVo> selectPoByDeliveryId(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "deliveryId") String deliveryId);

    /**
     * 根据送货单行列表获取采购订单信息
     *
     * @param tenantId 租户
     * @param idList   送货单行列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDeliveryPoRelVo>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 10:01:10
     */
    List<WmsDeliveryPoRelVo> selectPoByDeliveryIdList(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "idList") List<String> idList);

    /**
     * 获取采购订单信息
     *
     * @param tenantId 租户
     * @param poLineId 送货单行列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDeliveryPoRelVo>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 10:01:10
     */
    WmsDeliveryPoRelVo selectPoByLineId(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "poLineId") String poLineId);

    /**
     * 获取物料批数量
     *
     * @param tenantId
     * @param instructionId
     * @return
     */
    WmsMaterialLotLineVO selectMaterialLotQty(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "instructionId") String instructionId);

    /**
     * 获取指令实绩数量
     *
     * @param tenantId
     * @param instructionId
     * @return
     */
    WmsMaterialLotLineVO selectInstructionActualQty(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "instructionId") String instructionId);


    /**
     * 根据指令ID获取物料批数据
     *
     * @param tenantId      租户Id
     * @param instructionId 指令Id
     * @return 物料批数据
     */
    List<WmsMaterialLotLineVO> selectMaterialLotByInstructionId(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "instructionId") String instructionId);

    /**
     * 根据指令ID获取外协采购订单数据
     *
     * @param tenantId      租户Id
     * @param instructionId 指令Id
     * @return 外协采购订单数据
     */
    List<WmsMaterialPostingVO3> selectOutsourcingByInstructionId(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "instructionDocId") String instructionDocId,
                                                                 @Param(value = "instructionId") String instructionId);

    /**
     * 查询tol数据
     *
     * @param tenantId 租户
     * @param idList   id列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/15 04:55:58
     */
    List<WmsInstructionLineVO> selectTolInstructionByRsfIds(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "idList") List<String> idList);

    /**
     * 获取检验报废数量
     *
     * @param tenantId
     * @param instructionId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2020/9/9 15:50
     * @return java.math.BigDecimal
     */
    BigDecimal queryScrapQty(@Param(value = "tenantId") Long tenantId,
                             @Param(value = "instructionId") String instructionId,@Param(value = "materialLotId") String materialLotId);

    /**
     * 站点下库位20对应的外协货位
     *
     * @param tenantId             租户id
     * @param siteId               站点
     * @author sanfeng.zhang@hand-china.com 2020/9/12 13:37
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     */
    List<MtModLocator> queryOutsourceLocator(@Param(value = "tenantId") Long tenantId,
                                       @Param(value = "siteId") String siteId);

    /**
     * 更新实绩表扩张字段
     *
     * @param tenantId      租户id
     * @param actualId      实绩id
     * @param userId        用户id
     * @param attrValue     扩张字段的值
     * @param attrName      扩展字段
     * @author sanfeng.zhang@hand-china.com 2020/9/21 10:11
     * @return void
     */
    void updateStockInExchangeedQty(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "actualId") String actualId,
                                    @Param(value = "userId") Long userId,
                                    @Param(value = "attrName") String attrName,
                                    @Param(value = "attrValue") String attrValue);

    /**
     *  采购订单号
     *
     * @param tenantId
     * @param docLineId
     * @author sanfeng.zhang@hand-china.com 2020/10/19 14:20
     * @return java.lang.String
     */
    String queryPoNum(@Param("tenantId") Long tenantId, @Param("docLineId") String docLineId);
    
    /**
     * 采购订单行号
     * 
     * @param tenantId
     * @param docLineId
     * @author sanfeng.zhang@hand-china.com 2020/10/19 14:25 
     * @return java.lang.String
     */
    String queryPoLineNum(@Param("tenantId") Long tenantId, @Param("docLineId") String docLineId);

    /**
     * 查询制单数量(根据头主键、物料及行号找TRANSFER_OVER_LOCATOR的行 取实绩的ActualQty)
     *
     * @param tenantId
     * @param instructionDocId
     * @param materialId
     * @param InstructionLineNum
     * @author sanfeng.zhang@hand-china.com 2020/11/30 14:12
     * @return java.math.BigDecimal
     */
    BigDecimal queryActualQty(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId, @Param("materialId") String materialId, @Param("instructionLineNum") String InstructionLineNum);

    /**
     * 查询料废调换数量
     *
     * @param tenantId
     * @param instructionId
     * @return java.math.BigDecimal
     * @author sanfeng.zhang@hand-china.com 2020/11/30 14:19
     */
    BigDecimal queryExchangedQtyByLineId(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);

    /**
     * 查询二次送检条码信息
     *
     * @param tenantId
     * @param secondMaterialLotIdList
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotLineVO>
     * @author sanfeng.zhang@hand-china.com 2021/2/25 17:37
     */
    List<WmsMaterialLotLineVO> querySecondMaterialLot(@Param("tenantId") Long tenantId, @Param("secondMaterialLotIdList") List<String> secondMaterialLotIdList);
}
