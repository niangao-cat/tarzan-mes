package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualVO13
 * @description
 * @date 2019年12月17日 10:02
 */
public class MtEoStepWorkcellActualVO13 implements Serializable {
    private static final long serialVersionUID = 470628212417550592L;

    @ApiModelProperty(value = "EO步骤工作单元实绩ID列表")
    private List<String> eoStepWorkcellActualIdList = new ArrayList<>();
    @ApiModelProperty(value = "EO步骤实绩ID列表")
    private List<String> eoStepActualIdList = new ArrayList<>();
    @ApiModelProperty(value = "工作单元ID列表")
    private List<String> workcellIdList;
    @ApiModelProperty(value = "排队时间从")
    private Date queueDateFrom;
    @ApiModelProperty(value = "排队时间至")
    private Date queueDateTo;
    @ApiModelProperty(value = "运行时间从")
    private Date workingDateFrom;
    @ApiModelProperty(value = "运行时间至")
    private Date workingDateTo;
    @ApiModelProperty(value = "完成时间从")
    private Date completedDateFrom;
    @ApiModelProperty(value = "完成时间至")
    private Date completedDateTo;
    @ApiModelProperty(value = "完成暂存时间从")
    private Date completePendingDateFrom;
    @ApiModelProperty(value = "完成暂停时间至")
    private Date completePendingDateTo;

    public List<String> getEoStepWorkcellActualIdList() {
        return eoStepWorkcellActualIdList;
    }

    public void setEoStepWorkcellActualIdList(List<String> eoStepWorkcellActualIdList) {
        this.eoStepWorkcellActualIdList = eoStepWorkcellActualIdList;
    }

    public List<String> getEoStepActualIdList() {
        return eoStepActualIdList;
    }

    public void setEoStepActualIdList(List<String> eoStepActualIdList) {
        this.eoStepActualIdList = eoStepActualIdList;
    }

    public List<String> getWorkcellIdList() {
        return workcellIdList;
    }

    public void setWorkcellIdList(List<String> workcellIdList) {
        this.workcellIdList = workcellIdList;
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
}
