package tarzan.method.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtRouterReturnStepDTO implements Serializable {
    private static final long serialVersionUID = -4648916454810334045L;

    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerStepId;
    @ApiModelProperty("返回步骤唯一标识")
    private String routerReturnStepId;
    @ApiModelProperty(value = "返回类型")
    @NotBlank
    private String returnType;
    @ApiModelProperty(value = "返回原工艺路线的工艺")
    private String operationId;
    @ApiModelProperty(value = "返回原工艺路线的工艺名称")
    private String operationName;
    @ApiModelProperty(value = "原工艺是否完成标识")
    private String completeOriginalFlag;
    @ApiModelProperty(value = "步骤识别码")
    private String returnStepName;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterReturnStepId() {
        return routerReturnStepId;
    }

    public void setRouterReturnStepId(String routerReturnStepId) {
        this.routerReturnStepId = routerReturnStepId;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getCompleteOriginalFlag() {
        return completeOriginalFlag;
    }

    public void setCompleteOriginalFlag(String completeOriginalFlag) {
        this.completeOriginalFlag = completeOriginalFlag;
    }

    public String getReturnStepName() {
        return returnStepName;
    }

    public void setReturnStepName(String returnStepName) {
        this.returnStepName = returnStepName;
    }
}
