package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 明细查询条件
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/13
 * @time 10:58
 * @version 0.0.1
 * @return
 */
@Data
public class WmsBarcodeInventoryOnHandDetailQueryDTO implements Serializable {

    private static final long serialVersionUID = -2993551043185133040L;

    @ApiModelProperty(value = "站点", required = true)
    private String siteCode;

    @ApiModelProperty(value = "仓库")
    private String warehouseCode;

    @ApiModelProperty(value = "物料")
    private String materialCode;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "行号")
    private String soLineNum;

    @ApiModelProperty(value = "版本")
    private String materialVersion;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "SAP账务处理标识")
    private String sapAccountFlag;
}
