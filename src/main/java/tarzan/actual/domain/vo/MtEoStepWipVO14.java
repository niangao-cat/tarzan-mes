package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtEoStepWipVO14 implements Serializable {

    private static final long serialVersionUID = 6085336258428225235L;
    @ApiModelProperty(value = "EO步骤实绩主键")
    private String eoStepActualId;
    @ApiModelProperty(value = "EO所在的工作单元的工作单元")
    private String workcellId;
    @ApiModelProperty(value = "排队数量")
    private Double queueQty;
    @ApiModelProperty(value = "正在加工的数量")
    private Double workingQty;
    @ApiModelProperty(value = "完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty(value = "完成的数量")
    private Double completedQty;
    @ApiModelProperty(value = "报废数量")
    private Double scrappedQty;
    @ApiModelProperty(value = "保留数量")
    private Double holdQty;


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

    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }
}


