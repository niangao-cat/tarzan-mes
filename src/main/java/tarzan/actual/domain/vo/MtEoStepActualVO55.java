package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/4 15:15
 * @Description:
 */
public class MtEoStepActualVO55 implements Serializable {
    private static final long serialVersionUID = -2862658475011946392L;

    @ApiModelProperty("工艺路线实绩唯一标识")
    private String eoRouterActualId;

    @ApiModelProperty("排队更新数量")
    private Double qty;

    @ApiModelProperty("前道步骤唯一标识")
    private String previousStepId;

    @ApiModelProperty("步骤唯一标识")
    private String routerStepId;

    @ApiModelProperty("步骤组类型标识")
    private String stepGroupFlag;

    @ApiModelProperty("步骤组类型")
    private String routerStepGroupType;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStepGroupFlag() {
        return stepGroupFlag;
    }

    public void setStepGroupFlag(String stepGroupFlag) {
        this.stepGroupFlag = stepGroupFlag;
    }

    public String getRouterStepGroupType() {
        return routerStepGroupType;
    }

    public void setRouterStepGroupType(String routerStepGroupType) {
        this.routerStepGroupType = routerStepGroupType;
    }
}
