package com.ruike.reports.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标准件检验结果查询条件
 *
 * @author wenqiang.yin@hand-china.com 2021/02/04 8:36
 */
@Data
public class HmeSsnInspectResultDTO implements Serializable {

    private static final long serialVersionUID = -3804250225402018307L;

    @ApiModelProperty("标准件编码")
    private List<String> standardSnCodeList;
    @ApiModelProperty("物料编码")
    private List<String> materialIdList;
    @ApiModelProperty("芯片类型")
    private String cosType;
    @ApiModelProperty("工作方式")
    private String workWay;
    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("工序")
    private List<String> processIdList;
    @ApiModelProperty("工段")
    private String lineWorkcellId;
    @ApiModelProperty("工位编码")
    private String workcellId;
    @ApiModelProperty("检验结果")
    private String result;
    @ApiModelProperty("检验人")
    private List<Long> createdByList;
    @ApiModelProperty("校验开始时间")
    private String shiftDateFrom;
    @ApiModelProperty("校验结束时间")
    private String shiftDateTo;
}
