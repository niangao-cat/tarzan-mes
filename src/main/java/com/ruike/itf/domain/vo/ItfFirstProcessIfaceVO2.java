package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:09
 */
@Data
public class ItfFirstProcessIfaceVO2 implements Serializable {

    private static final long serialVersionUID = -3799574637690222249L;

    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;
}
