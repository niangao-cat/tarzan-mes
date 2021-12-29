package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/9 15:32
 */
@Data
public class WmsPurchaseReturnLineVO implements Serializable {

    private static final long serialVersionUID = 6030000838522415804L;

    @ApiModelProperty(value = "行id")
    private String instructionId;

    @ApiModelProperty(value = "行号")
    private String lineNumber;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "退货数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位id")
    private String uomId;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "行状态")
    @LovValue(lovCode = "MT.INSTRUCTION_DOC_TYPE", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty(value = "行状态含义")
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "退货仓库名称")
    private String locatorName;

    @ApiModelProperty(value = "退货仓库编码")
    private String locatorCode;

    @ApiModelProperty(value = "执行数量")
    private String actualQty;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "销售订单")
    private String deliveryNumber;

}
