package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Date: 2019/9/27 17:56
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtWorkOrderActualVO6 implements Serializable {
    private static final long serialVersionUID = -1409136908780593951L;
    /**
     * 生产指令ID
     */
    private String workOrderId;
    /**
     * 生产指令实绩ID
     */
    private String workOrderActualId;
    /**
     * 实绩开始时间从
     */
    private Date actualStartDateFrom;
    /**
     * 实绩开始时间至
     */
    private Date actualStartDateTo;
    /**
     * 实绩结束时间从
     */
    private Date actualEndDateFrom;
    /**
     * 实绩结束时间至
     */
    private Date actualEndDateTo;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderActualId() {
        return workOrderActualId;
    }

    public void setWorkOrderActualId(String workOrderActualId) {
        this.workOrderActualId = workOrderActualId;
    }

    public Date getActualStartDateFrom() {
        if (actualStartDateFrom != null) {
            return (Date) actualStartDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setActualStartDateFrom(Date actualStartDateFrom) {
        if (actualStartDateFrom == null) {
            this.actualStartDateFrom = null;
        } else {
            this.actualStartDateFrom = (Date) actualStartDateFrom.clone();
        }
    }

    public Date getActualStartDateTo() {
        if (actualStartDateTo != null) {
            return (Date) actualStartDateTo.clone();
        } else {
            return null;
        }
    }

    public void setActualStartDateTo(Date actualStartDateTo) {
        if (actualStartDateTo == null) {
            this.actualStartDateTo = null;
        } else {
            this.actualStartDateTo = (Date) actualStartDateTo.clone();
        }
    }

    public Date getActualEndDateFrom() {
        if (actualEndDateFrom != null) {
            return (Date) actualEndDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setActualEndDateFrom(Date actualEndDateFrom) {
        if (actualEndDateFrom == null) {
            this.actualEndDateFrom = null;
        } else {
            this.actualEndDateFrom = (Date) actualEndDateFrom.clone();
        }
    }

    public Date getActualEndDateTo() {
        if (actualEndDateTo != null) {
            return (Date) actualEndDateTo.clone();
        } else {
            return null;
        }
    }

    public void setActualEndDateTo(Date actualEndDateTo) {
        if (actualEndDateTo == null) {
            this.actualEndDateTo = null;
        } else {
            this.actualEndDateTo = (Date) actualEndDateTo.clone();
        }
    }
}