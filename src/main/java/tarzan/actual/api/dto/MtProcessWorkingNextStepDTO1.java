package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-13 8:42
 */
public class MtProcessWorkingNextStepDTO1 implements Serializable {
    private static final long serialVersionUID = 3790360028790051982L;

    @ApiModelProperty("工艺步骤ID")
    private String routerStepId;

    @ApiModelProperty("步骤识别码")
    private String stepName;

    @ApiModelProperty("步骤描述")
    private String description;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
