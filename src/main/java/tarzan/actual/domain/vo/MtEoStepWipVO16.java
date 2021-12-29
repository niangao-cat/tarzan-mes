package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Objects;

public class MtEoStepWipVO16 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4098304288894325954L;
    private String eoStepActualId;
    private String workcellId;
    private Double queueQty;

    public MtEoStepWipVO16() {}

    public MtEoStepWipVO16(String eoStepActualId, String workcellId) {
        this.eoStepActualId = eoStepActualId;
        this.workcellId = workcellId;
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

    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEoStepWipVO16 that = (MtEoStepWipVO16) o;
        return Objects.equals(eoStepActualId, that.eoStepActualId) && Objects.equals(workcellId, that.workcellId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eoStepActualId, workcellId);
    }
}


