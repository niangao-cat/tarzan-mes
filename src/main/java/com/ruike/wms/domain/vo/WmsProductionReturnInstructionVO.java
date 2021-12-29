package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.common.query.Where;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/13 10:32
 */
@Data
public class WmsProductionReturnInstructionVO implements Serializable {

    private static final long serialVersionUID = 8025352387081351937L;

    @ApiModelProperty("单据行ID")
    private String instructionId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("行状态")
    private String instructionStatus;
    @ApiModelProperty("行状态意义")
    private String instructionStatusMeaning;
    @ApiModelProperty("行需求数量")
    private Double quantity;
    @ApiModelProperty("目标仓库Id")
    private String toLocatorId;
    @ApiModelProperty("目标仓库编码")
    private String toLocatorCode;
    @ApiModelProperty("历史执行数量（实绩数）")
    private Double actualQuantity;
    @ApiModelProperty("本次执行数量（新增实绩）")
    private Double scanQty;
    @ApiModelProperty("实绩明细中条码个数")
    private Double serialAccount;
    @ApiModelProperty("本次扫描实物条码个数")
    private Double scanSerialAccount;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("销售订单")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("单据行类型")
    private String instructionType;
    @ApiModelProperty(value = "目标站点id")
    private String toSiteId;
    @ApiModelProperty("排序号")
    private int sort;
    @ApiModelProperty(value = "推荐货位")
    private String recommendLocatorCode;
    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "亮灯状态")
    private String taskStatus;
    @ApiModelProperty(value = "接口返回状态")
    private String status;
}
