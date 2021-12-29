package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualVO7
 * @description
 * @date 2019年10月10日 14:53
 */
public class MtEoStepWorkcellActualVO7 implements Serializable {
    private static final long serialVersionUID = 3927419174425881389L;

    private String eoStepWorkcellActualId;// EO步骤工作单元实绩ID
    private String eoStepActualId;// EO步骤实绩ID
    private String workcellId;// 工作单元ID
    private Date queueDateFrom;// 排队时间从
    private Date queueDateTo;// 排队时间至
    private Date workingDateFrom;// 运行时间从
    private Date workingDateTo;// 运行时间至
    private Date completedDateFrom;// 完成时间从
    private Date completedDateTo;// 完成时间至
    private Date completePendingDateFrom;// 完成暂存时间从
    private Date completePendingDateTo;// 完成暂停时间至

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
