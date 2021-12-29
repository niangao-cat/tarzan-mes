package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsProductionDetailDeleteDTO;
import com.ruike.wms.domain.vo.*;

import java.util.List;

/**
 * 生产退料应用服务
 *
 * @author li.zhang 2021/07/13 9:50
 */
public interface WmsProductionReturnService {

    /**
     * 根据退料单号查询相关信息
     *
     * @param tenantId          租户
     * @param instructionDocNum 退货号
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    WmsProductionReturnVO docScan(Long tenantId, String instructionDocNum,String instructionDocId);

    /**
     * 根据物料批条码查询相关信息
     *
     * @param tenantId          租户
     * @param materialLotCode 物料批条码
     * @param instructionDocId 物料批条码
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionMaterialReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    WmsProductionMaterialReturnVO materialDocScan(Long tenantId, String materialLotCode, String instructionDocId, List<WmsProductionReturnInstructionVO> instructionList,WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO, String locatorId);

    /**
     * 更改数量
     *
     * @param tenantId          租户
     * @param changeQty 更改的数量
     * @param wmsProductionMaterialReturnVO 扫描的物料批信息
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionQtyChangeVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    WmsProductionQtyChangeVO qtyChange(Long tenantId, Double changeQty, WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO);

    /**
     * 货位扫描
     *
     * @param tenantId     租户
     * @param locatorCode 货位编码
     * @param wmsProductionMaterialReturnVO 扫描的物料批信息
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionLocatorDocReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    WmsProductionLocatorDocReturnVO locatorDocScan(Long tenantId, String locatorCode, WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO);

    /**
     * 明细
     *
     * @param tenantId     租户
     * @param wmsProductionReturnInstructionVO 指令行信息
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionDetailVO>
     * @author li.zhang13@hand-china.com 2021/07/13
     */
    WmsProductionDetailVO docDetailQuery(Long tenantId, WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO);

    /**
     * 明细删除
     *
     * @param tenantId     租户
     * @param wmsProductionReturnInstructionVO 指令行信息
     * @param wmsProductionReturnInstructionDetailVOList 被选中的行明细信息
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionDetailDeleteVO>
     * @author li.zhang13@hand-china.com 2021/07/14
     */
    WmsProductionDetailDeleteVO docDetailDelete(Long tenantId, WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO, List<WmsProductionReturnInstructionDetailVO> wmsProductionReturnInstructionDetailVOList);

    /**
     * 执行
     *
     * @param tenantId     租户
     * @param wmsProductionReturnVO 整个单据信息
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductionDetailDeleteVO>
     * @author li.zhang13@hand-china.com 2021/07/14
     */
    void execute(Long tenantId, WmsProductionReturnVO wmsProductionReturnVO);

    /**
     * 退出时判断
     *
     * @param tenantId          租户
     * @param wmsProductionReturnVO
     * @author li.zhang13@hand-china.com 2021/08/04
     */
    WmsProductExitVO exitJudge(Long tenantId, WmsProductionReturnVO wmsProductionReturnVO);

    /**
     * 退出时删除不完整的明细
     *
     * @param tenantId          租户
     * @param wmsProductionReturnVO
     * @author li.zhang13@hand-china.com 2021/08/04
     */
    void exitDelete(Long tenantId, WmsProductionReturnVO wmsProductionReturnVO);
}
