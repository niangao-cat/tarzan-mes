package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Tangxiao
 */
public class MtEoRouterActualVO55 implements Serializable {
    private static final long serialVersionUID = -1179371521976758701L;
    @ApiModelProperty("当前工艺路线")
    private String currentRouterId;
    @ApiModelProperty("EO工艺路线完成标识")
    private String eoRouterCompletedFlag;
    @ApiModelProperty("主工艺路线标识")
    private String primaryRouterFlag;
    @ApiModelProperty(value = "执行作业工艺路线步骤实绩唯一标识")
    private String eoStepActualId;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "wkcId")
    private String workcellId;


    public String getCurrentRouterId() {
        return currentRouterId;
    }

    public void setCurrentRouterId(String currentRouterId) {
        this.currentRouterId = currentRouterId;
    }

    public String getEoRouterCompletedFlag() {
        return eoRouterCompletedFlag;
    }

    public void setEoRouterCompletedFlag(String eoRouterCompletedFlag) {
        this.eoRouterCompletedFlag = eoRouterCompletedFlag;
    }

    public String getPrimaryRouterFlag() {
        return primaryRouterFlag;
    }

    public void setPrimaryRouterFlag(String primaryRouterFlag) {
        this.primaryRouterFlag = primaryRouterFlag;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

}


