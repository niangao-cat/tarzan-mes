package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/07 15:46
 */
@Data
public class WmsProductReturnVO2 implements Serializable {

    private static final long serialVersionUID = 6531141133289685971L;

    @ApiModelProperty("单据行ID")
    private String instructionId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("版本")
    private String materialVersion;
    @ApiModelProperty("行号")
    private String salesOrderLineNum;
    @ApiModelProperty("需求数量")
    private Double demondQty;
    @ApiModelProperty("执行数量")
    private Double actualQty;
    @ApiModelProperty("累计数量")
    private Double scanQty;
    @ApiModelProperty("实绩条码个数")
    private Double serialAccount;
    @ApiModelProperty("扫描条码个数")
    private Double scanSerialAccount;
    @ApiModelProperty("目标仓库Id")
    private String toLocatorId;
    @ApiModelProperty("目标仓库")
    private String toLocator;
    @ApiModelProperty("状态")
    private String salesOrderLineStatusId;
    @ApiModelProperty("状态意义")
    private String salesOrderLineStatus;
    @ApiModelProperty("销售订单")
    private String salesOrderNum;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("目标来源Id")
    private String toSiteId;
}
