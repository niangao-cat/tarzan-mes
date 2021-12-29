package com.ruike.reports.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 配送综合查询报表 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 16:21
 */
@Data
public class WmsDistributionGeneralQueryDTO {
    @ApiModelProperty("配送单号")
    private String instructionDocNum;

    @ApiModelProperty("配送单状态")
    private String instructionDocStatus;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("工段ID")
    private String workcellId;

    @ApiModelProperty("目标仓库")
    private String toWarehouseId;

    @ApiModelProperty("是否补料")
    private String replenishmentFlag;

    @ApiModelProperty("补料单号")
    private String replenishmentListNum;

    @ApiModelProperty("是否备齐")
    private String suiteFlag;

    @ApiModelProperty("制单人")
    private Long createdBy;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty("制单时间从")
    private Date creationDateFrom;

    @ApiModelProperty("制单时间至")
    private Date creationDateTo;
}
