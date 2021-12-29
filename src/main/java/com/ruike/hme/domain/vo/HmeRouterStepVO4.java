package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeRouterStepVO4
 * @Description 视图
 * @Date 2020/11/19 14:05
 * @Author yuchao.wang
 */
@Data
public class HmeRouterStepVO4 implements Serializable {
    private static final long serialVersionUID = -2652515518828618575L;

    @ApiModelProperty("步骤ID")
    private String routerStepId;

    @ApiModelProperty("步骤名称")
    private String stepName;

    @ApiModelProperty("下一步骤ID")
    private String nextStepId;

    @ApiModelProperty("下一步骤名称")
    private String nextStepName;

    @ApiModelProperty(value = "顺序")
    private Long sequence;
}