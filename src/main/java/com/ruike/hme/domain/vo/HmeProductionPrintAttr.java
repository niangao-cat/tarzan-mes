package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author wengang,qiang@hand-china.com 2021/09/27 21:59
 */
@Data
public class HmeProductionPrintAttr implements Serializable {


    private static final long serialVersionUID = -32988620223130268L;

    @ApiModelProperty(value = "扩展表属性值")
    private String attrValue;

    @ApiModelProperty(value = "扩展表属性名")
    private String attrName;
}
