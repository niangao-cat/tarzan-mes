package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/22 9:34
 */
@Data
public class HmeCosMaterialReturnVO4 implements Serializable {

    private static final long serialVersionUID = 5296987633242606835L;

    @ApiModelProperty(value = "物料id")
    private String materialLotId;
    @ApiModelProperty(value = "物料编码")
    private String materialLotCode;
}
