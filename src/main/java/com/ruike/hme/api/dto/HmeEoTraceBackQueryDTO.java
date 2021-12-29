package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品追溯
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
@Data
public class HmeEoTraceBackQueryDTO implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "序号")
    private Long lineNum;
    @ApiModelProperty(value = "JOB ID")
    private String jobId;
    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "EO编码")
    private String eoNum;
    @ApiModelProperty(value = "工序ID")
    private String parentWorkcellId;
    @ApiModelProperty(value = "工序编码")
    private String parentWorkcellCode;
    @ApiModelProperty(value = "工序名称")
    private String parentWorkcellName;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位编码")
    private String workcellCode;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;
    @ApiModelProperty(value = "加工开始时间")
    private Date siteInDate;
    @ApiModelProperty(value = "加工结束时间")
    private Date siteOutDate;
    @ApiModelProperty(value = "加工时长")
    private BigDecimal processTime;
    @ApiModelProperty(value = "进站人ID")
    private Long createdBy;
    @ApiModelProperty(value = "进站人")
    private String createUserName;
    @ApiModelProperty(value = "出站人ID")
    private Long operatorId;
    @ApiModelProperty(value = "出站人")
    private String operatorUserName;
    @ApiModelProperty(value = "是否异常出站")
    private String isAbnormalOutSite;

    @ApiModelProperty(value = "是否返修")
    private String isReworkFlag;
    @ApiModelProperty(value = "是否返修含义")
    private String isRework;
    @ApiModelProperty(value = "不良信息标识")
    private Boolean ncInfoFlag;
    @ApiModelProperty(value = "作业平台类型")
    @LovValue(value = "HME.JOB_TYPE", meaningField = "jobTypeMeaning")
    private String jobType;
    @ApiModelProperty(value = "作业平台类型含义")
    private String jobTypeMeaning;
    @ApiModelProperty(value = "是否异常出站")
    private String exceptionFlag;
    @ApiModelProperty(value = "是否异常出站")
    private String exceptionFlagMeaning;
    @ApiModelProperty("采集头ID")
    private String collectHeaderId;
}
