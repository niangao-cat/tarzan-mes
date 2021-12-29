package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * Created by cb on 2020-10-26.
 */
public class MtEoStepActualVO47 implements Serializable {

    private static final long serialVersionUID = 3717438789640034674L;
    private String eoStepActualId;
    private String workcellId;
    private Double queueQty;
    private Double workingQty;
    private Double completePendingQty;
    private Double completedQty;
    private Double scrappedQty;
    private String allUpdateFlag;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }

    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }

    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }

    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    public String getAllUpdateFlag() {
        return allUpdateFlag;
    }

    public void setAllUpdateFlag(String allUpdateFlag) {
        this.allUpdateFlag = allUpdateFlag;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
