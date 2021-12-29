package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterOpComponentVO implements Serializable {

    private static final long serialVersionUID = -3778089454272563529L;
    private String routerOperationComponentId;
    private String routerOperationId;
    private String bomComponentId;
    private Long sequence;
    private Double qty;
    private Double perQty;
    private String routerStepId;

    public String getRouterOperationComponentId() {
        return routerOperationComponentId;
    }

    public void setRouterOperationComponentId(String routerOperationComponentId) {
        this.routerOperationComponentId = routerOperationComponentId;
    }

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPerQty() {
        return perQty;
    }

    public void setPerQty(Double perQty) {
        this.perQty = perQty;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }
}
