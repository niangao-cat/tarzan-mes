package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 查询条件
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/13
 * @time 10:58
 * @version 0.0.1
 * @return
 */
@Data
public class WmsBarcodeInventoryOnHandQueryDTO implements Serializable {
    private static final long serialVersionUID = 5279529341222325082L;

    @ApiModelProperty(value = "站点主键Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "仓库主键Id")
    private String warehouseId;

    @ApiModelProperty(value = "物料主键Id")
    private String materialId;

    @ApiModelProperty(value = "货位主键Id")
    private String locatorId;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "行号")
    private String soLineNum;

    @ApiModelProperty(value = "版本")
    private String materialVersion;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "SAP账户处理标识")
    private String sapAccountFlag;
}
