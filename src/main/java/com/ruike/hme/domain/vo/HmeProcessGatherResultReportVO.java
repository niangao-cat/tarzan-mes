package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeProcessGatherResultReportVO
 * 工序采集结果报表VO
 * @author: chaonan.hu@hand-china.com 2021/03/22 10:24:21
 **/
@Data
public class HmeProcessGatherResultReportVO implements Serializable {
    private static final long serialVersionUID = -5858199670540316873L;

    @ApiModelProperty(value = "SN")
    private String sn;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "机型")
    private String model;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "数据收集组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "数据收集组描述")
    private String tagGroupDescription;

    @ApiModelProperty(value = "数据收集项编码")
    private String tagCode;

    @ApiModelProperty(value = "数据收集项描述")
    private String tagDescription;

    @ApiModelProperty(value = "上限")
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "下限")
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "采集结果")
    private String result;

    @ApiModelProperty(value = "采集工序编码")
    private String processCode;

    @ApiModelProperty(value = "采集工序描述")
    private String processName;

    @ApiModelProperty(value = "采集工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "采集工位描述")
    private String workcellName;

    @ApiModelProperty(value = "采集人")
    private String realName;

    @ApiModelProperty(value = "采集时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gatherDate;

    @ApiModelProperty(value = "进站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteInDate;

    @ApiModelProperty(value = "出站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteOutDate;
}
