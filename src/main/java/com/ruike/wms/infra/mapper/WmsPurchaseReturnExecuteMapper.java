package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.WmsPurchaseCodeDetailsVO;
import com.ruike.wms.domain.vo.WmsPurchaseLineVO;
import com.ruike.wms.domain.vo.WmsPurchaseReturnCodeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购退货-执行
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/9 19:55
 */
public interface WmsPurchaseReturnExecuteMapper {

    /**
     * 查询退货行信息
     *
     * @param tenantId
     * @param instructionIdList
     * @author sanfeng.zhang@hand-china.com 2020/11/10 9:38
     * @return java.util.List<com.ruike.wms.domain.vo.WmsPurchaseLineVO>
     */
    List<WmsPurchaseLineVO> queryPurchaseLineInfo(@Param("tenantId") Long tenantId, @Param("instructionIdList") List<String> instructionIdList);

    /**
     * 查询物料批的信息
     *
     * @param tenantId
     * @param mtMaterialLotIdList
     * @author sanfeng.zhang@hand-china.com 2020/11/10 14:16
     * @return java.util.List<com.ruike.wms.domain.vo.WmsPurchaseReturnCodeVO>
     */
    List<WmsPurchaseReturnCodeVO> queryPurchaseMaterialLotList(@Param("tenantId") Long tenantId, @Param("mtMaterialLotIdList") List<String> mtMaterialLotIdList);

    /**
     * 查询扫描的条码
     *
     * @param tenantId
     * @param lineIdList
     * @author sanfeng.zhang@hand-china.com 2020/11/10 16:09
     * @return java.util.List<java.lang.String>
     */
    List<String> queryScanCodeList(@Param("tenantId") Long tenantId, @Param("lineIdList") List<String> lineIdList);

    /**
     * 查询条码对应的行
     *
     * @param tenantId
     * @param materialLotId
     * @param InstructionDocNum
     * @author sanfeng.zhang@hand-china.com 2020/11/10 20:40
     * @return java.util.List<com.ruike.wms.domain.vo.WmsPurchaseLineVO>
     */
    List<WmsPurchaseLineVO> queryLineByBarcodeAndDocNum(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId, @Param("instructionDocNum") String InstructionDocNum);

    /**
     * 查询行下物料批信息
     *
     * @param tenantId
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2020/11/10 21:14
     * @return java.util.List<com.ruike.wms.domain.vo.WmsPurchaseCodeDetailsVO>
     */
    List<WmsPurchaseCodeDetailsVO> queryBarcodeListByLineId(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);

    /**
     * 查询已扫描条码数
     *
     * @param tenantId
     * @param instructionDocId
     * @author sanfeng.zhang@hand-china.com 2020/11/11 11:12
     * @return java.lang.Long
     */
    Long queryScannedMaterialLot(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 查询指令实绩
     *
     * @param tenantId
     * @param instructionDocId
     * @author sanfeng.zhang@hand-china.com 2020/11/12 19:05
     * @return java.lang.Long
     */
    Long queryActualQtyCount(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);
}
