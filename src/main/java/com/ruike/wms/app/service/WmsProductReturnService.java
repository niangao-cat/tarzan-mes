package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsMaterialDocReceviceDto;
import com.ruike.wms.domain.vo.*;

import java.util.List;

/**
 * 成品退货应用服务
 *
 * @author li.zhang13@hand-china.com 2021/07/07 14:42
 */
public interface WmsProductReturnService {

    /**
     * 根据退料单号查询相关信息
     *
     * @param tenantId          租户
     * @param instructionDocNum 退货号
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    WmsProductReturnVO docScan(Long tenantId, String instructionDocNum);

    /**
     * 根据物料批条码查询相关信息
     *
     * @param tenantId          租户
     * @param materialLotCode 物料批条码
     * @param instructionDocId 物料批条码
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialDocReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 17:02:33
     */
    WmsMaterialDocReturnVO materialDocScan(Long tenantId, String materialLotCode, String instructionDocId, List<WmsProductReturnVO2> instructionList, List<WmsProductReturnVO2> instructionselectedList, String locatorId, WmsMaterialDocReturnVO vo);

    /**
     * 更改执行数量
     *
     * @param tenantId          租户
     * @param changeQty 更改数量
     * @return java.util.List<com.ruike.wms.domain.vo.WmsQtyChangeVO>
     * @author li.zhang13@hand-china.com 2021/07/07 17:02:33
     */
    WmsQtyChangeVO qtyChange(Long tenantId, Double changeQty, WmsMaterialDocReturnVO wmsMaterialDocReturnVO);

    /**
     * 根据货位条码查询相关信息
     *
     * @param tenantId          租户
     * @param locatorCode 货物条码
     * @param wmsMaterialDocReturnVO 已扫描物料批
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialDocReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 17:02:33
     */
    WmsLocatorDocReturnVO locatorDocScan(Long tenantId, String locatorCode, WmsMaterialDocReturnVO wmsMaterialDocReturnVO);

    /**
     * 明细
     *
     * @param tenantId          租户
     * @param wmsProductReturnVO2 指令行数据
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialDocReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 17:02:33
     */
    WmsMaterialDocReturnVO2 docDetailQuery(Long tenantId, WmsProductReturnVO2 wmsProductReturnVO2);

    /**
     * 明细删除
     *
     * @param tenantId          租户
     * @param wmsMaterialDocReturnVO2 指令明细数据
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialDocReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 17:02:33
     */
    WmsMaterialDeleteReturnVO2 docDetailDelete(Long tenantId, WmsMaterialDocReturnVO2 wmsMaterialDocReturnVO2);

    /**
     * 退货执行
     *
     * @param tenantId          租户
     * @param wmsProductReturnVO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialDocReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 17:02:33
     */
    WmsProductExecuteVO execute(Long tenantId, WmsProductReturnVO wmsProductReturnVO);

    /**
     * 退出时判断
     *
     * @param tenantId          租户
     * @param wmsProductReturnVO
     * @author li.zhang13@hand-china.com 2021/07/20
     */
    WmsProductExitVO exitJudge(Long tenantId, WmsProductReturnVO wmsProductReturnVO);

    /**
     * 退出时删除不完整的明细
     *
     * @param tenantId          租户
     * @param wmsProductReturnVO
     * @author li.zhang13@hand-china.com 2021/07/20
     */
    void exitDelete(Long tenantId, WmsProductReturnVO wmsProductReturnVO);

}
