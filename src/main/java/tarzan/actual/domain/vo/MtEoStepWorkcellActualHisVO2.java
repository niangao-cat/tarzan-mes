package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualHisVO2
 * @description
 * @date 2019年12月04日 14:29
 */
public class MtEoStepWorkcellActualHisVO2 implements Serializable {
    private static final long serialVersionUID = 557835355716315685L;

    @ApiModelProperty(value = "执行作业工作单元实绩历史")
    private String eoStepWorkcellActualHisId;
    @ApiModelProperty(value = "执行作业工作单元实绩")
    private String eoStepWorkcellActualId;
    @ApiModelProperty(value = "步骤实际")
    private String eoStepActualId;
    @ApiModelProperty(value = "排队数量")
    private Double queueQty;
    @ApiModelProperty(value = "正在加工的数量")
    private Double workingQty;
    @ApiModelProperty(value = "完成暂存数量")
    private Double completePendingQty;
    @ApiModelProperty(value = "报废数量")
    private Double scrappedQty;
    @ApiModelProperty(value = "EO最近一次置于排队的时间")
    private Date queueDate;
    @ApiModelProperty(value = "EO最近一次置于运行的时间")
    private Date workingDate;
    @ApiModelProperty(value = "EO最近一次置于完成暂存的时间")
    private Date completePendingDate;
    @ApiModelProperty(value = "EO最近一次置于完成的时间")
    private Date completedDate;
    @ApiModelProperty(value = "EO在此步骤的工作单元")
    private String workcellId;
    // 增加参数
    @ApiModelProperty(value = "工作单元编码")
    private String workcellCode;
    @ApiModelProperty(value = "工作单元描述")
    private String workcellName;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "事件描述")
    private String eventTypeCode;


    @ApiModelProperty(value = "事件人")
    private Long eventBy;
    @ApiModelProperty(value = "事件时间")
    private Date eventTime;

    @ApiModelProperty(value = "排队数量")
    private Double trxQueueQty;
    @ApiModelProperty(value = "正在加工的数量")
    private Double trxWorkingQty;
    @ApiModelProperty(value = "完成的数量")
    private Double trxCompletedQty;
    @ApiModelProperty(value = "报废数量")
    private Double trxScrappedQty;
    @ApiModelProperty(value = "完成暂存数量")
    private Double trxCompletePendingQty;

    public String getEoStepWorkcellActualHisId() {
        return eoStepWorkcellActualHisId;
    }

    public void setEoStepWorkcellActualHisId(String eoStepWorkcellActualHisId) {
        this.eoStepWorkcellActualHisId = eoStepWorkcellActualHisId;
    }

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

    public Date getQueueDate() {
        if (queueDate != null) {
            return (Date) queueDate.clone();
        } else {
            return null;
        }
    }

    public void setQueueDate(Date queueDate) {
        if (queueDate == null) {
            this.queueDate = null;
        } else {
            this.queueDate = (Date) queueDate.clone();
        }
    }

    public Date getWorkingDate() {
        if (workingDate != null) {
            return (Date) workingDate.clone();
        } else {
            return null;
        }
    }

    public void setWorkingDate(Date workingDate) {
        if (workingDate == null) {
            this.workingDate = null;
        } else {
            this.workingDate = (Date) workingDate.clone();
        }
    }

    public Date getCompletePendingDate() {
        if (completePendingDate != null) {
            return (Date) completePendingDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletePendingDate(Date completePendingDate) {
        if (completePendingDate == null) {
            this.completePendingDate = null;
        } else {
            this.completePendingDate = (Date) completePendingDate.clone();
        }
    }

    public Date getCompletedDate() {
        if (completedDate != null) {
            return (Date) completedDate.clone();
        } else {
            return null;
        }
    }

    public void setCompletedDate(Date completedDate) {
        if (completedDate == null) {
            this.completedDate = null;
        } else {
            this.completedDate = (Date) completedDate.clone();
        }
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    public Date getEventTime() {
        if (eventTime != null) {
            return (Date) eventTime.clone();
        } else {
            return null;
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }

    public Double getTrxQueueQty() {
        return trxQueueQty;
    }

    public void setTrxQueueQty(Double trxQueueQty) {
        this.trxQueueQty = trxQueueQty;
    }

    public Double getTrxWorkingQty() {
        return trxWorkingQty;
    }

    public void setTrxWorkingQty(Double trxWorkingQty) {
        this.trxWorkingQty = trxWorkingQty;
    }

    public Double getTrxCompletedQty() {
        return trxCompletedQty;
    }

    public void setTrxCompletedQty(Double trxCompletedQty) {
        this.trxCompletedQty = trxCompletedQty;
    }

    public Double getTrxScrappedQty() {
        return trxScrappedQty;
    }

    public void setTrxScrappedQty(Double trxScrappedQty) {
        this.trxScrappedQty = trxScrappedQty;
    }

    public Double getTrxCompletePendingQty() {
        return trxCompletePendingQty;
    }

    public void setTrxCompletePendingQty(Double trxCompletePendingQty) {
        this.trxCompletePendingQty = trxCompletePendingQty;
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

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }
}
