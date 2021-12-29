package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtEoStepActualDTO2 implements Serializable {
    private static final long serialVersionUID = -6688508161651645582L;

    @ApiModelProperty(value = "执行作业工艺路线实绩唯一标识")
    private String eoRouterActualId;
    @ApiModelProperty(value = "步骤唯一标识")
    private String routerStepId;
    @ApiModelProperty(value = "返工步骤标识")
    private String reworkStepFlag;
    @ApiModelProperty(value = "临时返工标识")
    private String localReworkFlag;
    @ApiModelProperty(value = "前一步骤实绩")
    private String previousStepId;
    @ApiModelProperty(value = "事件")
    private String eventId;

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }

    public String getLocalReworkFlag() {
        return localReworkFlag;
    }

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
