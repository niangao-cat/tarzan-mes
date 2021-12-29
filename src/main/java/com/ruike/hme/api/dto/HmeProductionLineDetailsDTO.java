package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: tarzan-mes->HmeProductionLineDetailsDTO
 * @description: 产量日明细报表查询返回DTO
 * @author: bao.xu 2020-07-07 09:30:15
 **/
@Data
public class HmeProductionLineDetailsDTO implements Serializable {

    private static final long serialVersionUID = 8961993516462708571L;

    @ApiModelProperty(value = "生产线编号")
    private String productionLineId;
    @ApiModelProperty(value = "生产线")
    private String production;
    @ApiModelProperty(value = "车间编码")
    private String workshopId;
    @ApiModelProperty(value = "车间")
    private String workshopName;
    @ApiModelProperty(value = "工段Id")
    private String lineWorkcellId;
    @ApiModelProperty(value = "工段")
    private String lineWorkcellName;
    @ApiModelProperty(value = "班次工段Id")
    private String shiftWorkcellId;
    @ApiModelProperty(value = "班次id")
    private String shiftId;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = "MM月dd日")
    private Date shiftDate;
    @ApiModelProperty(value = "班次时间")
    private String shiftDateStr;
    @ApiModelProperty(value = "开班时间")
    private Date shiftStartTime;
    @ApiModelProperty(value = "结班时间")
    private Date shiftEndTime;
    @ApiModelProperty(value = "生产指令Id")
    private String workOrderId;
    @ApiModelProperty(value = "不良数")
    private BigDecimal ncNumber;
    @ApiModelProperty(value = "计划开始时间")
    private String createData;
    @ApiModelProperty(value = "产品料号")
    private String productionNum;
    @ApiModelProperty(value = "产品描述")
    private String productionDes;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;
    @ApiModelProperty(value = "投产（首道）")
    private Integer putData;
    @ApiModelProperty(value = "完工（末道）")
    private Integer finishedData;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection1;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection1Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection2;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection2Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection3;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection3Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection4;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection4Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection5;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection5Repair;
    @ApiModelProperty(value = "光学工段1")
    private String opticalSection6;
    @ApiModelProperty(value = "光学工段1返修")
    private String opticalSection6Repair;
    @ApiModelProperty(value = "首道工序")
    private String firstProcessId;
    @ApiModelProperty(value = "末道工序")
    private String endProcessId;
}
