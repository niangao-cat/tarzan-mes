package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 12:47
 */
public interface WmsProductReceiptMapper {

    /**
     * 入库单-头查询
     *
     * @param tenantId      租户id
     * @param reqVO         参数
     * @author sanfeng.zhang@hand-china.com 2020/9/10 12:53
     * @return java.util.List<com.ruike.wms.domain.vo.WmsReceiptDocVO>
     */
    List<WmsReceiptDocVO> receiptDocQuery(@Param("tenantId") Long tenantId, @Param("reqVO") WmsReceiptDocReqVO reqVO);

    /**
     * 入库单-行查询
     *
     * @param tenantId                  租户id
     * @param instructionIdList         参数
     * @author sanfeng.zhang@hand-china.com 2020/9/10 15:58
     * @return java.util.List<com.ruike.wms.domain.vo.WmsReceiptLineVO>
     */
    List<WmsReceiptLineVO> receiptDocLineQuery(@Param("tenantId") Long tenantId, @Param("instructionIdList") List<String> instructionIdList);

    /**
     * 入库单-行明细查询
     *
     * @param tenantId      租户id
     * @param reqVO         参数
     * @author sanfeng.zhang@hand-china.com 2020/9/10 18:26
     * @return java.util.List<com.ruike.wms.domain.vo.WmsReceiptDetailVO>
     */
    List<WmsReceiptDetailVO> receiptDocLineDetail(@Param("tenantId") Long tenantId, @Param("reqVO") WmsReceiptDetailReqVO reqVO);

    /**
     * 入库单-行打印数据查询
     *
     * @param tenantId                  租户id
     * @param instructionDocId          参数
     * @author yifan.xiong@hand-china.com 2020-9-22 15:23:57
     * @return java.util.List<com.ruike.wms.domain.vo.WmsReceiptLinePrintVO>
     */
    List<WmsReceiptLinePrintVO> receiptLinePrintQuery(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 入库单-头打印数据查询
     *
     * @param tenantId                  租户id
     * @param instructionDocId          参数
     * @author sanfeng.zhang@hand-china.com 2020/9/10 15:58
     * @return com.ruike.wms.domain.vo.WmsReceiptDocVO
     */
    WmsReceiptDocVO receiptDocPrintQuery(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 查询入库单条码
     * @param tenantId
     * @param instructionDocIds
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/5/25
     */
    List<String> queryMaterialLotIdList(@Param("tenantId") Long tenantId, @Param("instructionDocIds") List<String> instructionDocIds);
}
