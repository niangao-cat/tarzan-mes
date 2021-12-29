package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoStepWorkcellActualVO3 implements Serializable {
    private static final long serialVersionUID = -7825583225716162537L;

    private String eoStepWorkcellActualId; // 主键
    private String eoStepActualId; // EO步骤实绩主键
    private String workcellId; // EO在此步骤的工作单元

    public String getEoStepWorkcellActualId() {
        return eoStepWorkcellActualId;
    }

    public void setEoStepWorkcellActualId(String eoStepWorkcellActualId) {
        this.eoStepWorkcellActualId = eoStepWorkcellActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
