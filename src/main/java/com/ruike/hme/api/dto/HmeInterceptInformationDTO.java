package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * description
 *
 * @author wengang.qiang@hand-china.com 2021/09/07 16:28
 */
@Data
public class HmeInterceptInformationDTO implements Serializable {

    private static final long serialVersionUID = 864945150689497995L;

    @ApiModelProperty(value = "拦截单号")
    private String interceptNum;
    @ApiModelProperty(value = "拦截维度")
    private String dimension;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "拦截人")
    private Long interceptBy;
    @ApiModelProperty(value = "拦截时间从")
    private Date interceptDateFrom;
    @ApiModelProperty(value = "拦截时间到")
    private Date interceptDateTo;
    @ApiModelProperty(value = "拦截工序id")
    private String workcellId;
    @ApiModelProperty(value = "拦截对象")
    private String interceptObject;
    @ApiModelProperty(value = "例外放行SN")
    private String materialLotCode;


}
