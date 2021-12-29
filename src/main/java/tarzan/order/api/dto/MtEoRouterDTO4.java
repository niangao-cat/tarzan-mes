package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 3:58 下午
 */
public class MtEoRouterDTO4 implements Serializable {
    private static final long serialVersionUID = 8178223709566332346L;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty("工作单元编码")
    private String workcellCode;
    @ApiModelProperty("工作单元名称")
    private String workcellName;
    @ApiModelProperty("排队数量")
    private Double queueQty;
    @ApiModelProperty("累计排队数量")
    private Double totalQueueQty;
    @ApiModelProperty("正在加工数量")
    private Double workingQty;
    @ApiModelProperty("累计正在加工数量")
    private Double totalWorkingQty;
    @ApiModelProperty("完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty("累计完成暂存数量")
    private Double totalCompletePendingQty;
    @ApiModelProperty("完成数量")
    private Double completeQty;
    @ApiModelProperty("累计完成数量")
    private Double totalCompleteQty;
    @ApiModelProperty("报废数量")
    private Double scrappedQty;
    @ApiModelProperty("累计报废数量")
    private Double totalScrappedQty;
    @ApiModelProperty("保留数量")
    private Double holdQty;


    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
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

    public Double getCompleteQty() {
        return completeQty;
    }

    public void setCompleteQty(Double completeQty) {
        this.completeQty = completeQty;
    }

    public Double getTotalQueueQty() {
        return totalQueueQty;
    }

    public void setTotalQueueQty(Double totalQueueQty) {
        this.totalQueueQty = totalQueueQty;
    }

    public Double getTotalWorkingQty() {
        return totalWorkingQty;
    }

    public void setTotalWorkingQty(Double totalWorkingQty) {
        this.totalWorkingQty = totalWorkingQty;
    }

    public Double getTotalCompletePendingQty() {
        return totalCompletePendingQty;
    }

    public void setTotalCompletePendingQty(Double totalCompletePendingQty) {
        this.totalCompletePendingQty = totalCompletePendingQty;
    }

    public Double getTotalCompleteQty() {
        return totalCompleteQty;
    }

    public void setTotalCompleteQty(Double totalCompleteQty) {
        this.totalCompleteQty = totalCompleteQty;
    }

    public Double getTotalScrappedQty() {
        return totalScrappedQty;
    }

    public void setTotalScrappedQty(Double totalScrappedQty) {
        this.totalScrappedQty = totalScrappedQty;
    }
}
