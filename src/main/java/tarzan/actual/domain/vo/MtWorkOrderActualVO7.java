package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Date: 2019/9/27 18:00
 * @Author: ${yiyang.xie}
 */
public class MtWorkOrderActualVO7 implements Serializable {
    private static final long serialVersionUID = 7850892933550675149L;
    /**
     * 生产指令实绩ID
     */
    private String workOrderActualId;
    /**
     * 生产指令ID
     */
    private String workOrderId;
    /**
     * 生产指令编码
     */
    private String workOrderNum;
    /**
     * 累计下达数量
     */
    private Double releasedQty;
    /**
     * 累计完成数量
     */
    private Double completedQty;
    /**
     * 累计报废数量
     */
    private Double scrappedQty;
    /**
     * 保留数量
     */
    private Double holdQty;
    /**
     * 实绩开始时间
     */
    private Date actualStartDate;
    /**
     * 实绩结束时间
     */
    private Date actualEndDate;

    public String getWorkOrderActualId() {
        return workOrderActualId;
    }

    public void setWorkOrderActualId(String workOrderActualId) {
        this.workOrderActualId = workOrderActualId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public Double getReleasedQty() {
        return releasedQty;
    }

    public void setReleasedQty(Double releasedQty) {
        this.releasedQty = releasedQty;
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

    public Date getActualStartDate() {
        if (actualStartDate != null) {
            return (Date) actualStartDate.clone();
        } else {
            return null;
        }
    }

    public void setActualStartDate(Date actualStartDate) {
        if (actualStartDate == null) {
            this.actualStartDate = null;
        } else {
            this.actualStartDate = (Date) actualStartDate.clone();
        }
    }

    public Date getActualEndDate() {
        if (actualEndDate != null) {
            return (Date) actualEndDate.clone();
        } else {
            return null;
        }
    }

    public void setActualEndDate(Date actualEndDate) {
        if (actualEndDate == null) {
            this.actualEndDate = null;
        } else {
            this.actualEndDate = (Date) actualEndDate.clone();
        }
    }
}
