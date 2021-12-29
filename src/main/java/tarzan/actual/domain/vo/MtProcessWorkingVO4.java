package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiao tang
 * @date 2020-02-12 18:07
 */
public class MtProcessWorkingVO4 implements Serializable {
    
    private static final long serialVersionUID = 3174059217416767051L;

    
    @ApiModelProperty(value = "排队数量", required = true)
    private Double queueQty;
    
    @ApiModelProperty(value = "正在加工的数量", required = true)
    private Double workingQty;
    
    @ApiModelProperty(value = "完成暂存数量", required = true)
    private Double completePendingQty;
    
    @ApiModelProperty(value = "完成的数量", required = true)
    private Double completedQty;
    
    @ApiModelProperty(value = "报废数量", required = true)
    private Double scrappedQty;
    
    @ApiModelProperty(value = "保留数量", required = true)
    private Double holdQty;

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
