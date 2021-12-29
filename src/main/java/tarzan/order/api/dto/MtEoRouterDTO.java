package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 2:06 下午
 */
public class MtEoRouterDTO implements Serializable {
    private static final long serialVersionUID = -3843151978605995964L;
    @ApiModelProperty("步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("工艺名称")
    private String stepName;
    @ApiModelProperty("步骤顺序")
    private Long sequence;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺路线步骤描述")
    private String routerStepDesc;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("工艺描述")
    private String operationDesc;
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
    private Double holdQty;
    @ApiModelProperty("工作单元实绩")
    private List<MtEoRouterDTO4> wkcActualList;

    //2020-7-30 add by sanfeng.zhang 增加返修标识
    @ApiModelProperty("返修标识")
    private String reworkStepFlag;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getRouterStepDesc() {
        return routerStepDesc;
    }

    public void setRouterStepDesc(String routerStepDesc) {
        this.routerStepDesc = routerStepDesc;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
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

    public Double getCompleteQty() {
        return completeQty;
    }

    public void setCompleteQty(Double completeQty) {
        this.completeQty = completeQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
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

    public List<MtEoRouterDTO4> getWkcActualList() {
        return wkcActualList;
    }

    public void setWkcActualList(List<MtEoRouterDTO4> wkcActualList) {
        this.wkcActualList = wkcActualList;
    }

    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }
}
