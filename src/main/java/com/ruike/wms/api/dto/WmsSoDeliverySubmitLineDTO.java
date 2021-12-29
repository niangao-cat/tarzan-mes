package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 16:38
 */
@Data
public class WmsSoDeliverySubmitLineDTO {
    @ApiModelProperty("行号")
    private String instructionLineNum;
    @ApiModelProperty("主键ID ,表示唯一一条记录")
    private String instructionId;
    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;
    @ApiModelProperty(value = "单位ID", required = true)
    private String uomId;
    @ApiModelProperty(value = "订单ID")
    private String sourceOrderId;
    @ApiModelProperty(value = "订单行id")
    private String sourceOrderLineId;
    @ApiModelProperty(value = "来源站点id", required = true)
    private String fromSiteId;
    @ApiModelProperty(value = "来源库位id")
    private String fromLocatorId;
    @ApiModelProperty(value = "执行数量", required = true)
    private BigDecimal actualQty;
    @ApiModelProperty(value = "制单数量", required = true)
    private BigDecimal quantity;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "允差下限")
    private BigDecimal toleranceLowerLimit;
    @ApiModelProperty(value = "允差上限")
    private BigDecimal toleranceUpperLimit;
}
