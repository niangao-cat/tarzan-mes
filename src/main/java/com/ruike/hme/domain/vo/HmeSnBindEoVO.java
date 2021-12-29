package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.domain.entity.MtEo;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/14 18:38
 */
@Data
public class HmeSnBindEoVO extends MtEo implements Serializable {

    private static final long serialVersionUID = 4399268028964094917L;

    @ApiModelProperty(value = "编码")
    private String materialLotCode;

    @ApiModelProperty(value = "编码规则")
    private String ruleCode;
}
