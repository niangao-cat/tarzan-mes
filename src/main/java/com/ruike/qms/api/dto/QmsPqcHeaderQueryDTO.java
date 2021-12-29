package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * QmsPqcHeaderQueryDTO
 * @description: 头数据查询参数
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/08 10:18
 */

@Data
public class QmsPqcHeaderQueryDTO implements Serializable {

    private static final long serialVersionUID = -461573733336524138L;

    @ApiModelProperty(value = " 事业部id")
    private String departmentId;

    @ApiModelProperty(value = " 车间Id")
    private String workshopId;

    @ApiModelProperty(value = " 生产线")
    private String prodLineId;

    @ApiModelProperty(value = "工序id")
    private String workcellId;

    @ApiModelProperty(value = "巡检单")
    private String pqcNumber;

    @ApiModelProperty(value = "单据状态")
    private String inspectionStatus;

    @ApiModelProperty(value = " 物料")
    private String materialId;

    @ApiModelProperty(value = " 产品SN")
    private String materialLotCode;

    @ApiModelProperty(value = " 工单")
    private String workOrderNum;

    @ApiModelProperty(value = " 检验员")
    private String lastUpdatedByName;

    @ApiModelProperty(value = " 检验结果")
    private String inspectionResult;
}
