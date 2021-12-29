package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 16:08
 */
@Data
public class HmeFreezeDocJobVO {
    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单")
    private String workOrderNum;
    @ApiModelProperty(value = "设备")
    private String equipmentId;
    @ApiModelProperty(value = "设备编码")
    private String equipmentCode;
    @ApiModelProperty(value = "生产线")
    private String prodLineId;
    @ApiModelProperty(value = "生产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "工段")
    private String workcellId;
    @ApiModelProperty(value = "工段编码")
    private String workcellCode;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "工序编码")
    private String processCode;
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "工位编码")
    private String stationCode;
    @ApiModelProperty(value = "操作人ID")
    private Long operatedBy;
    @ApiModelProperty(value = "操作人姓名")
    private String operatedByName;
    @ApiModelProperty(value = "生产时间")
    private Date productionDate;
}
