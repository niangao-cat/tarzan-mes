package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/23 16:31
 */
@Data
public class HmeNcCheckVO4 implements Serializable {

    @ApiModelProperty("步骤识别码")
    private String stepName;

    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerOperationId;

    @ApiModelProperty("工艺")
    private String operationId;

    @ApiModelProperty("工艺编码")
    private String operationName;

    @ApiModelProperty("组件")
    private String bomComponentId;

    @ApiModelProperty("序号")
    private Long sequence;

    @ApiModelProperty("有效性")
    private String enableFlag;
}
