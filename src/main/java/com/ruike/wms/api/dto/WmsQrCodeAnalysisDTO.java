package com.ruike.wms.api.dto;

import lombok.Data;

/**
 * 二维码解析
 *
 * @author jiangling.zheng@hand-china.com 2020-10-08 14:19
 */
@Data
public class WmsQrCodeAnalysisDTO {

    private static final long serialVersionUID = 7398014942584934750L;

    private String analysisCode;
    private String materialLotCode;
    private String materialCode;
    private String materialVersion;
    private String supplierCode;
    private String quantity;
    private String supplierLot;
    private String productDate;
    private String outerBoxBarCode;

}
