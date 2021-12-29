package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtEoStepActualVO22 implements Serializable {
    private static final long serialVersionUID = 4025478108098605751L;

    private String routerStepId; // 步骤唯一标识
    private String eoRouterActualId; // 工艺路线实绩唯一标识
    private String workcellId; // 工作单元唯一标识
    private Double qty; // 移出数量
    private String eventRequestId; // 事件组ID
    private String status; // 移出状态

    @ApiModelProperty("全量清除标识")
    private String allClearFlag;
    @ApiModelProperty("目标步骤ID")
    private String targetRouterStepId;

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public String getTargetRouterStepId() {
        return targetRouterStepId;
    }

    public void setTargetRouterStepId(String targetRouterStepId) {
        this.targetRouterStepId = targetRouterStepId;
    }
}
