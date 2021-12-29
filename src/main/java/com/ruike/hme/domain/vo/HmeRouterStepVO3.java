package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * HmeRouterStepVO
 *
 * @author yuchao.wang@hand-china.com 2020/03/18 0:08
 */
@Data
public class HmeRouterStepVO3 implements Serializable {

    private static final long serialVersionUID = -5336296296776077877L;

    @ApiModelProperty("步骤ID")
    private String routerStepId;
    @ApiModelProperty("步骤名称")
    private String stepName;
    @ApiModelProperty("步骤顺序")
    private Long sequence;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("步骤类型")
    private String routerStepType;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "eoID")
    private String eoId;
    @ApiModelProperty(value = "是否入口步骤")
    private String entryStepFlag;
    @ApiModelProperty(value = "步骤描述")
    private String description;
    @ApiModelProperty(value = "实验代码")
    private String labCode;
    @ApiModelProperty(value = "步骤备注")
    private String routerStepRemark;
}
