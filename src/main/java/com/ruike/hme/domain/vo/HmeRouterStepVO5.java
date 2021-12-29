package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeRouterStepVO5
 * @Description 视图
 * @Date 2020/11/19 14:05
 * @Author yuchao.wang
 */
@Data
public class HmeRouterStepVO5 implements Serializable {
    private static final long serialVersionUID = -6689010535190482225L;

    @ApiModelProperty("步骤ID")
    private String routerStepId;

    @ApiModelProperty("步骤名称")
    private String stepName;

    @ApiModelProperty(value = "顺序")
    private Long sequence;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "入口步骤标识")
    private String entryStepFlag;

    @ApiModelProperty(value = "步骤类型")
    private String routerStepType;

    @ApiModelProperty("下一步骤ID")
    private String nextStepId;

    @ApiModelProperty("下一步骤名称")
    private String nextStepName;

    @ApiModelProperty("下一步骤顺序")
    private Long nextSequence;

    @ApiModelProperty("下一步骤描述")
    private String nextDescription;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("eoID")
    private String eoId;

    @ApiModelProperty("routerID")
    private String routerId;

    @ApiModelProperty("routerDoneStepId")
    private String routerDoneStepId;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("备注")
    private String routerStepRemark;
}