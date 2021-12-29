package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class WmsDeliveryPrintVO1 implements Serializable {

    private static final long serialVersionUID = 1002652888909442226L;
    @ApiModelProperty("行号")
    private String lineNum;

    @ApiModelProperty("原单单号")
    private String poNum;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("数量")
    private Double Quantity;

    @ApiModelProperty("单位")
    private String uomCode;

    @ApiModelProperty("旧物料号")
    private String oldItemCode;

    @ApiModelProperty("发货仓")
    private String locatorCode;

    public static WmsDeliveryPrintVO1 createSummary(WmsDeliveryPrintVO1 deliveryPrintVO1) {
        WmsDeliveryPrintVO1 printVO1 = new WmsDeliveryPrintVO1();
        BeanUtils.copyProperties(deliveryPrintVO1, printVO1, "poNum");
        return printVO1;
    }

}
