package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsProductionRequisitionMaterialExecutionDTO;
import com.ruike.wms.api.dto.WmsProductionRequisitionMaterialExecutionDetailDTO;
import com.ruike.wms.api.dto.WmsProductionRequisitionMaterialExecutionLineDTO;

import java.util.List;

public interface WmsProductionRequisitionMaterialExecutionService {
    /**
     * @description:领料单查询
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 10:42
     */
    WmsProductionRequisitionMaterialExecutionDTO queryHead(Long tenantId, String instructionDocNum);

    /**
     * @description:条码明细
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 14:30
     */
    List<WmsProductionRequisitionMaterialExecutionLineDTO> queryBarcode(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto);

    /**
     * @description:执行
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/14 10:24
     */
    WmsProductionRequisitionMaterialExecutionDTO execute(Long tenantId, List<WmsProductionRequisitionMaterialExecutionLineDTO> dtoList, String workOrderNum,String instructionDocType);

    /**
     * @description:删除
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/14 10:24
     */
    WmsProductionRequisitionMaterialExecutionLineDTO barcodeDelete(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto);

    /**
     * @description:条码明细
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/16 10:37
     */
    List<WmsProductionRequisitionMaterialExecutionDetailDTO> materialLotCodeQuery(Long tenantId, WmsProductionRequisitionMaterialExecutionLineDTO dto);

    /**
     * @description:查询完更新
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/16 13:25
     */
    Void updateBarcode(Long tenantId, List<WmsProductionRequisitionMaterialExecutionLineDTO> dto, String barCode);

    /**
     * @description:删除之后查询明细界面行数据
     * @return:
     * @author: xiaojiang
     * @time: 2021/8/3 10:14
     */
    WmsProductionRequisitionMaterialExecutionLineDTO queryLine(Long tenantId,String instructionDocId, String instructionId);
}
