package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @description 条码库存现有量查询 返回主界面数据
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/13
 * @time 11:17
 * @version 0.0.1
 * @return
 */
@Data
public class WmsBarcodeInventoryOnHandVO implements Serializable {
    private static final long serialVersionUID = -6496026128372419979L;

    @ApiModelProperty(value = "站点")
    private String siteCode;

    @ApiModelProperty(value = "仓库")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库描述")
    private String warehouseName;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "货位描述")
    private String locatorName;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "数量")
    private Long qty;

    @ApiModelProperty(value = "SAP账务处理标识")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "sapAccountFlagMeaning")
    private String sapAccountFlag;

    @ApiModelProperty(value = "SAP账务处理标识含义")
    private String sapAccountFlagMeaning;
}
