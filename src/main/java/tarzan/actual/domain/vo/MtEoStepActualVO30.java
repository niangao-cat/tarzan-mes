package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/10 18:00
 * @Author: ${yiyang.xie}
 */
public class MtEoStepActualVO30 implements Serializable {
    private static final long serialVersionUID = 2360676547485526764L;
    @ApiModelProperty("执行作业步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("工艺路线实绩ID")
    private String eoRouterActualId;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("步骤识别码")
    private String stepName;
    @ApiModelProperty("方式标识")
    private String bypassedFlag;
    @ApiModelProperty("返工标识")
    private String reworkStepFlag;
    @ApiModelProperty("重做标识")
    private String localReworkFlag;
    @ApiModelProperty("前道步骤ID")
    private String previousStepId;
    @ApiModelProperty("排队时间从")
    private Date queueDateFrom;
    @ApiModelProperty("排队时间至")
    private Date queueDateTo;
    @ApiModelProperty("运行时间从")
    private Date workingDateFrom;
    @ApiModelProperty("运行时间至")
    private Date workingDateTo;
    @ApiModelProperty("完成时间从")
    private Date completedDateFrom;
    @ApiModelProperty("完成时间至")
    private Date completedDateTo;
    @ApiModelProperty("完成暂存时间从")
    private Date completePendingDateFrom;
    @ApiModelProperty("完成暂停时间至")
    private Date completePendingDateTo;
    @ApiModelProperty("说明")
    private String specialInstruction;
    @ApiModelProperty("状态")
    private String status;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
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

    public String getBypassedFlag() {
        return bypassedFlag;
    }

    public void setBypassedFlag(String bypassedFlag) {
        this.bypassedFlag = bypassedFlag;
    }

    public String getReworkStepFlag() {
        return reworkStepFlag;
    }

    public void setReworkStepFlag(String reworkStepFlag) {
        this.reworkStepFlag = reworkStepFlag;
    }

    public String getLocalReworkFlag() {
        return localReworkFlag;
    }

    public void setLocalReworkFlag(String localReworkFlag) {
        this.localReworkFlag = localReworkFlag;
    }

    public String getPreviousStepId() {
        return previousStepId;
    }

    public void setPreviousStepId(String previousStepId) {
        this.previousStepId = previousStepId;
    }

    public Date getQueueDateFrom() {
        if (queueDateFrom != null) {
            return (Date) queueDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setQueueDateFrom(Date queueDateFrom) {
        if (queueDateFrom == null) {
            this.queueDateFrom = null;
        } else {
            this.queueDateFrom = (Date) queueDateFrom.clone();
        }
    }

    public Date getQueueDateTo() {
        if (queueDateTo != null) {
            return (Date) queueDateTo.clone();
        } else {
            return null;
        }
    }

    public void setQueueDateTo(Date queueDateTo) {
        if (queueDateTo == null) {
            this.queueDateTo = null;
        } else {
            this.queueDateTo = (Date) queueDateTo.clone();
        }
    }

    public Date getWorkingDateFrom() {
        if (workingDateFrom != null) {
            return (Date) workingDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setWorkingDateFrom(Date workingDateFrom) {
        if (workingDateFrom == null) {
            this.workingDateFrom = null;
        } else {
            this.workingDateFrom = (Date) workingDateFrom.clone();
        }
    }

    public Date getWorkingDateTo() {
        if (workingDateTo != null) {
            return (Date) workingDateTo.clone();
        } else {
            return null;
        }
    }

    public void setWorkingDateTo(Date workingDateTo) {
        if (workingDateTo == null) {
            this.workingDateTo = null;
        } else {
            this.workingDateTo = (Date) workingDateTo.clone();
        }
    }

    public Date getCompletedDateFrom() {
        if (completedDateFrom != null) {
            return (Date) completedDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setCompletedDateFrom(Date completedDateFrom) {
        if (completedDateFrom == null) {
            this.completedDateFrom = null;
        } else {
            this.completedDateFrom = (Date) completedDateFrom.clone();
        }
    }

    public Date getCompletedDateTo() {
        if (completedDateTo != null) {
            return (Date) completedDateTo.clone();
        } else {
            return null;
        }
    }

    public void setCompletedDateTo(Date completedDateTo) {
        if (completedDateTo == null) {
            this.completedDateTo = null;
        } else {
            this.completedDateTo = (Date) completedDateTo.clone();
        }
    }

    public Date getCompletePendingDateFrom() {
        if (completePendingDateFrom != null) {
            return (Date) completePendingDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setCompletePendingDateFrom(Date completePendingDateFrom) {
        if (completePendingDateFrom == null) {
            this.completePendingDateFrom = null;
        } else {
            this.completePendingDateFrom = (Date) completePendingDateFrom.clone();
        }
    }

    public Date getCompletePendingDateTo() {
        if (completePendingDateTo != null) {
            return (Date) completePendingDateTo.clone();
        } else {
            return null;
        }
    }

    public void setCompletePendingDateTo(Date completePendingDateTo) {
        if (completePendingDateTo == null) {
            this.completePendingDateTo = null;
        } else {
            this.completePendingDateTo = (Date) completePendingDateTo.clone();
        }
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
