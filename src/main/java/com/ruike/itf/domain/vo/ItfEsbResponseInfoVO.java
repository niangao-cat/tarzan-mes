package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname ItfEsbResponseInfoVO
 * @Description ESB 接口返回报文=esbinfo
 * @Date 2020/7/30 15:14
 * @Author yuchao.wang
 */
@Data
@ApiModel("ESB接口返回报文-esbinfo")
public class ItfEsbResponseInfoVO implements Serializable {
    private static final long serialVersionUID = 2936380050193728947L;

    @ApiModelProperty("insId")
    private String insId;

    @ApiModelProperty("requestTime")
    private String requestTime;

    @ApiModelProperty("responseTime")
    private String responseTime;

    @ApiModelProperty("returnCode")
    private String returnCode;

    @ApiModelProperty("returnMsg")
    private String returnMsg;

    @ApiModelProperty("returnStatus")
    private String returnStatus;

    @ApiModelProperty("attr1")
    private String attr1;

    @ApiModelProperty("attr2")
    private String attr2;

    @ApiModelProperty("attr3")
    private String attr3;
}