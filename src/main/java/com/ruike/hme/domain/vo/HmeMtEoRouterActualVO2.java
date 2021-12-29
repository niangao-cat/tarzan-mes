package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

public class HmeMtEoRouterActualVO2 implements Serializable {
    private static final long serialVersionUID = -8424595841677786659L;

    private List<String> eoIdList;
    private String operationId;
    private String stepName;
    private String routerStepId;
    private String sourceOperationId;
    private String sourceStepName;
    private String sourceRouterStepId;

    public List<String> getEoIdList() {
        return eoIdList;
    }

    public void setEoIdList(List<String> eoIdList) {
        this.eoIdList = eoIdList;
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

    public String getSourceOperationId() {
        return sourceOperationId;
    }

    public void setSourceOperationId(String sourceOperationId) {
        this.sourceOperationId = sourceOperationId;
    }

    public String getSourceStepName() {
        return sourceStepName;
    }

    public void setSourceStepName(String sourceStepName) {
        this.sourceStepName = sourceStepName;
    }

    public String getSourceRouterStepId() {
        return sourceRouterStepId;
    }

    public void setSourceRouterStepId(String sourceRouterStepId) {
        this.sourceRouterStepId = sourceRouterStepId;
    }
}
