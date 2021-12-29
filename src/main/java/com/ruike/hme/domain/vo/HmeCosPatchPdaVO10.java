package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chaonan.hu@hand-china.com 2021/11/08 14:06
 */
@Data
public class HmeCosPatchPdaVO10 implements Serializable {
    private static final long serialVersionUID = -3336419545576921612L;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("实验代码备注")
    private String labRemark;
}
