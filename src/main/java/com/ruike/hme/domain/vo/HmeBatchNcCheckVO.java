package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/29 15:01
 */
@Data
public class HmeBatchNcCheckVO implements Serializable {

    private static final long serialVersionUID = 2857349245300055924L;

    @ApiModelProperty(value = "EOID")
    private String eoId;

    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;
}
