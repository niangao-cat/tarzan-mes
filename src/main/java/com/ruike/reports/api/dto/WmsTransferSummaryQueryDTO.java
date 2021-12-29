package com.ruike.reports.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 调拨汇总报表 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 14:43
 */
@Data
public class WmsTransferSummaryQueryDTO {
    @ApiModelProperty("调拨单号")
    private String instructionDocNum;
    @ApiModelProperty("状态")
    private String instructionDocStatus;
    @ApiModelProperty("类型")
    private String instructionDocType;
    @ApiModelProperty("制单人")
    private Long createdBy;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty("制单时间从")
    @NotNull
    private Date creationDateFrom;
    @ApiModelProperty("制单时间至")
    @NotNull
    private Date creationDateTo;

    @ApiModelProperty(value = "来源仓库")
    private String fromWarehouseId;

    @ApiModelProperty(value = "目标仓库")
    private String toWarehouseId;

    @ApiModelProperty(value = "来源货位")
    private String fromLocatorId;

    @ApiModelProperty(value = "目标货位")
    private String toLocatorId;

    @ApiModelProperty(value = "执行人")
    private String executorUserId;

    @ApiModelProperty(value = "执行时间从")
    private Date executorDateFrom;

    @ApiModelProperty(value = "执行时间至")
    private Date executorDateTo;
}
