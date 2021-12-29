package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO13 implements Serializable {
    private static final long serialVersionUID = 7991571898572048826L;

    /// stepActual
    private String eoStepActualId;
    private String operationId; // 标准作业（工艺）主键，表示唯一标准作业（工艺
    private String stepName; // EO步骤名称
    private String routerStepId; // 步骤ID（对于特殊操作步骤ID就是操作ID）
    private Long sequence;
    private Double stepCompletedQty;

    /// routerActual
    private String eoId;
    private String routerId;
    private String sourceEoStepActualId;
    private String eoRouterActualId;
    private Double qty;


    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getStepCompletedQty() {
        return stepCompletedQty;
    }

    public void setStepCompletedQty(Double stepCompletedQty) {
        this.stepCompletedQty = stepCompletedQty;
    }
}
