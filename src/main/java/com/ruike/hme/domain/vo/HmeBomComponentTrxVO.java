package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * bom用于事务处理行
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 16:09
 */
@Data
public class HmeBomComponentTrxVO {
    @ApiModelProperty("bom行ID")
    private String bomComponentId;
    @ApiModelProperty("bom行ID")
    private String materialId;
    @ApiModelProperty("bom用量")
    private Double bomQty;
    @ApiModelProperty("预留/需求的编号")
    private String bomReserveNum;
    @ApiModelProperty("预留/需求的项目编号")
    private String bomReserveLineNum;
    @ApiModelProperty("销售订单号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("已投数量")
    private Double assembleQty;
    @ApiModelProperty("净需求数量")
    private Double demandQty;
    @ApiModelProperty("联产品行号")
    private String productLineNum;
}
