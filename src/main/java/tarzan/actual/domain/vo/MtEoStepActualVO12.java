package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEoStepActualVO12 implements Serializable {
    private static final long serialVersionUID = -4473843997696658794L;

    private String eoId;
    private String routerId;
    private String operationId; // 标准作业（工艺）主键，表示唯一标准作业（工艺
    private String stepName; // EO步骤名称
    private String routerStepId; // 步骤ID（对于特殊操作步骤ID就是操作ID）
    private List<String> status;
    private String eoRouterActualId;
    private String eoStepActualId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }
}
