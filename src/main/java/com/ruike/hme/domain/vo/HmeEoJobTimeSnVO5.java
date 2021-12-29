package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobTimeSnVO5 implements Serializable {

    private static final long serialVersionUID = 8413264507759397220L;

    @ApiModelProperty("实验代码")
    private String labCode;
    @ApiModelProperty("备注")
    private String routerStepRemark;
}
