package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 拦截例外放行表
 *
 * @author wengang.qiang@hand-chian.com 2021/09/08 14:45
 */
@Data
public class HmeInterceptReleaseVO implements Serializable {

    private static final long serialVersionUID = -3572074976882602398L;

    @ApiModelProperty(value = "条码id")
    private String materialLotId;
    @ApiModelProperty(value = "放行SN")
    private String materialLotCode;
    @ApiModelProperty(value = "放行人id")
    private Long releaseBy;
    @ApiModelProperty(value = "放行人姓名")
    private String releaseByName;


}
