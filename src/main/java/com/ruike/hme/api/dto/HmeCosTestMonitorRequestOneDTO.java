package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description  接口请求参数
 *
 * @author wengang.qiang@hand-chian.com 2021/10/12 19:12
 */
@Data
public class HmeCosTestMonitorRequestOneDTO implements Serializable {


    private static final long serialVersionUID = -3362833085014967695L;

    @ApiModelProperty(value = "接口表主键id")
    private List<String> cosMonitorIfaceIdList;

    @ApiModelProperty(value = "状态")
    private String docStatus;

    @ApiModelProperty(value = "状态")
    private String checkStatus;

}
