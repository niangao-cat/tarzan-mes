package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.Date;

/**
 * 生产领退料单查询 : 行表数据
 *
 * @author taowen.wang@hand-china.com
 * @version 1.0
 * @date 2021/7/8 20:26
 */
@Data
public class WmsInstructionIdLineDTO {
    @ApiModelProperty(value = "行id")
    private String instructionId;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "行状态")
    @LovValue(lovCode = "WX.WMS_C/R_DOC_LINE_STATUS" , meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty(value = "行状态值集含义")
    private String instructionStatusMeaning;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "单位名称")
    private String uomId;
    @ApiModelProperty(value = "目标仓库id")
    private String fromLocatorId;
    @ApiModelProperty(value = "目标仓库id")
    private String toLocatorId;
    @ApiModelProperty(value = "制单数量")
    private String quantity;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建人")
    private String createdBy;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
    @ApiModelProperty(value = "执行人")
    private String lastUpdatedBy;
    @ApiModelProperty(value = "执行时间")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "销售订单号+行号")
    private String soLineNum;
    @ApiModelProperty(value = "预留项目号+行号")
    private String bomReserveLineNum;
    @ApiModelProperty(value = "行号")
    private String instructionLineNum;
    @ApiModelProperty(value = "执行数量")
    private String actualQty;
}
