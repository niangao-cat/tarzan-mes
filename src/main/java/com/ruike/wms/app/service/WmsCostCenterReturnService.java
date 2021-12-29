package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import org.hzero.core.base.AopProxy;

import java.util.List;


public interface WmsCostCenterReturnService extends AopProxy<WmsCostCenterReturnService> {

    /**
     * 成本中心退料单扫描条码
     *
     * @param tenantId          租户ID
     * @param instructionDocNum 退料单编号
     * @return com.ruike.wms.api.dto.WmsMaterialReturnScanDTO
     * @author wenzhang.yu 2020/4/20 11:59
     **/
    WmsMaterialReturnScanDTO returnCodeScan(Long tenantId, String instructionDocNum ,String instructionDocId);

    /**
     * 成本中心退料单扫描实物条码
     *
     * @param tenantId         租户ID
     * @param barCode          条码
     * @param instructionDocId 送货单ID
     * @param locatorCode      条码货位编码
     * @param barCodeList      已扫描条码List
     * @return com.ruike.wms.api.dto.WmsMaterialReturnScanDTO2
     * @author wenzhang.yu 2020/4/20 15:55
     **/
    WmsMaterialReturnScanDTO2 returnMaterialCodeScan(Long tenantId, String barCode, String instructionDocId, String locatorCode, List<WmsMaterialReturnScanDTO2> barCodeList, List<WmsMaterialReturnScanLineDTO> docLineList);

    /**
     * 确认执行
     *
     * @param tenantId 租户ID
     * @param dto      退料确认数据
     * @return WmsMaterialReturnScanDTO
     * @author wenzhang.yu 2020/4/21 19:15
     **/
    WmsMaterialReturnScanDTO returnCodeConfirm(Long tenantId, WmsMaterialReturnConfirmDTO dto);

    /**
     * 明细查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.wms.api.dto.WmsMaterialReturnDetailDTO2>
     * @author wenzhang.yu 2020/4/23 9:55
     **/
    List<WmsMaterialReturnDetailDTO2> docDetailQuery(Long tenantId, WmsMaterialReturnDetailDTO dto);

    WmsMaterialReturnScanLocatorDTO returnLocatorScan(Long tenantId, String locatorCode, List<WmsMaterialReturnScanDTO2> barcodeList, List<WmsMaterialReturnScanLineDTO> docLineList);

}
