package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/10 12:27
 */
@Data
public class WmsPurchaseReturnScanVO implements Serializable {

    private static final long serialVersionUID = 3549418994280173865L;

    @ApiModelProperty(value = "条码")
    private String barcode;

    @ApiModelProperty(value = "条码类型")
    private String barcodeType;

    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "物料批信息")
    private List<WmsPurchaseReturnCodeVO> materialLotList;
}
