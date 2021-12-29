package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepActualVO25 implements Serializable {
    private static final long serialVersionUID = 3151134599657028347L;

    private String eoId; // 执行作业唯一标识
    private String operationId; // 工艺
    private String stepName; // 步骤识别码
    private String routerStepId; // 步骤唯一标识
    private String sourceOperationId; // 特殊工艺路线来源工艺
    private String sourceStepName; // 特殊工艺路线来源步骤识别码
    private String sourceRouterStepId; // 特殊工艺路线步骤唯一标识
    private String ncCodeId; // 不良代码
    private String incidentNum; // 事故编码

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getIncidentNum() {
        return incidentNum;
    }

    public void setIncidentNum(String incidentNum) {
        this.incidentNum = incidentNum;
    }
}
