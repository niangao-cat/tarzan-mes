package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 生产流转查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@Data
public class WmsProductionFlowQueryReportDTO implements Serializable {

    private static final long serialVersionUID = -3860294946861908550L;

    @ApiModelProperty(value = "生产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "工段编码")
    private List<String> workcellList;

    @ApiModelProperty(value = "工序编码")
    private List<String> workshopList;

    @ApiModelProperty(value = "工位编码")
    private List<String> workproList;

    @ApiModelProperty(value = "设备编码")
    private String department;

    @ApiModelProperty(value = "工单号")
    private List<String> workOrderList;

    @ApiModelProperty(value = "产品编码")
    private List<String> productionList;

    @ApiModelProperty(value = "产品序列号")
    private List<String> prodSerialList;

    @ApiModelProperty(value = "实验代码")
    private String identification;

    @ApiModelProperty(value = "是否不良")
    private String badFlag;

    @ApiModelProperty(value = "是否返修")
    private String reFlag;

    @ApiModelProperty(value = "班次日期")
    private String shiftDate;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "进站人")
    private String userInId;

    @ApiModelProperty(value = "出站人")
    private String userOutId;

    @ApiModelProperty(value = "作业平台类型")
    private String workType;

    @ApiModelProperty(value = "加工开始时间起")
    private String workStartFrom;

    @ApiModelProperty(value = "加工开始时间至")
    private String workStartTo;

    @ApiModelProperty(value = "加工结束时间起")
    private String workEndFrom;

    @ApiModelProperty(value = "加工结束时间至")
    private String workEndTo;



}
