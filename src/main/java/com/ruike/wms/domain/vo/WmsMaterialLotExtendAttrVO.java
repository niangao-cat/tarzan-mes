package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料批拓展字段
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 11:06
 */
@Data
public class WmsMaterialLotExtendAttrVO {
    public static final String ATTR_TABLE = "mt_material_lot_attr";

    public static final String MATERIAL_VERSION = "MATERIAL_VERSION";
    public static final String STATUS = "STATUS";
    public static final String SO_NUM = "SO_NUM";
    public static final String SO_LINE_NUM = "SO_LINE_NUM";
    public static final String FREEZE_FLAG = "FREEZE_FLAG";
    public static final String STOCKTAKE_FLAG = "STOCKTAKE_FLAG";
    public static final String MF_FLAG = "MF_FLAG";
    public static final String SUPPLIER_LOT = "SUPPLIER_LOT";

    @ApiModelProperty("唯一标识")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "条码状态")
    private String status;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "销售订单行")
    private String soLineNum;
    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "在制品标志")
    private String mfFlag;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
}
