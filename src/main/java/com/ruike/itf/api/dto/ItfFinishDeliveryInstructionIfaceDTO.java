package com.ruike.itf.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/16 15:49
 */
@Data
public class ItfFinishDeliveryInstructionIfaceDTO implements Serializable {

    private static final long serialVersionUID = -8918006592903391253L;

    @ApiModelProperty(value = "主键")
    @JsonIgnore
    private String ifaceId;
    @ApiModelProperty(value = "单据号")
    @JsonIgnore
    private String docNum;
    @ApiModelProperty(value = "行号")
    @JsonIgnore
    private String docLineNum;
    @ApiModelProperty(value = "出库任务号")
    private String taskNum;
    @ApiModelProperty(value = "指令单据id")
    private String instructionDocId;
    @ApiModelProperty(value = "指令id")
    private String instructionId;
    @ApiModelProperty(value = "物料号")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "出口号")
    private String exitNum;
    @ApiModelProperty(value = "仓库")
    private String warehouseCode;
    @ApiModelProperty(value = "任务状态")
    private String taskStatus;
    @ApiModelProperty(value = "物料批清单")
    private List<String> materialLotCodeList;
}
