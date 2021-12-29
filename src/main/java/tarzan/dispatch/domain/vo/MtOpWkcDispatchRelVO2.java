package tarzan.dispatch.domain.vo;

import java.io.Serializable;

public class MtOpWkcDispatchRelVO2 implements Serializable {
    private static final long serialVersionUID = 5449830497652958774L;

    private String workcellId; // 工作单元
    private Long priority; // 优先级
    private String operationWkcDispatchRelId; // 工艺和工作单元关系ID

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getOperationWkcDispatchRelId() {
        return operationWkcDispatchRelId;
    }

    public void setOperationWkcDispatchRelId(String operationWkcDispatchRelId) {
        this.operationWkcDispatchRelId = operationWkcDispatchRelId;
    }
}
