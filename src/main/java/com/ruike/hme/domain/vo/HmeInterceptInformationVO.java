package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * description
 *
 * @author wengang.qiang@hand-china.com 2021/09/07 15:46
 */
@Data
public class HmeInterceptInformationVO implements Serializable {

    private static final long serialVersionUID = 5688650853874541442L;

    @ApiModelProperty(value = "拦截表主键")
    private String interceptId;
    @ApiModelProperty(value = "拦截单号")
    private String interceptNum;
    @ApiModelProperty(value = "拦截维度")
    @LovValue(lovCode = "HME.INTERCEPT_DIMENSION", meaningField = "dimensionMeaning")
    private String dimension;
    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME.INTERCEPT_STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "拦截消息")
    private String remark;
    @ApiModelProperty(value = "拦截人id")
    private Long interceptBy;
    @ApiModelProperty(value = "拦截人姓名")
    private String interceptByName;
    @ApiModelProperty(value = "拦截时间")
    private Date interceptDate;
    @ApiModelProperty(value = "拦截维度描述")
    private String dimensionMeaning;
    @ApiModelProperty(value = "状态描述")
    private String statusMeaning;
}
