package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepWipVO1 implements Serializable {
    private static final long serialVersionUID = -3372303861716774054L;

    private String workcellId; // 工作单元ID
    private String eoStepActualId; // 执行作业步骤ID
    private String status; // 状态

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
