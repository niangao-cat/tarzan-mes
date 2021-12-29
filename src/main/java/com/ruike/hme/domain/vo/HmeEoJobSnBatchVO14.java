package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO14 implements Serializable {
    private static final long serialVersionUID = 5430422141463216220L;
    @ApiModelProperty(value = "删除标识")
    private String deleteFlag;
    @ApiModelProperty(value = "当前条码物料匹配的组件")
    HmeEoJobSnBatchVO4 component;
    @ApiModelProperty(value = "打印标识")
    private String printFlag;
    @ApiModelProperty(value = "子条码")
    private String subCode;
}
