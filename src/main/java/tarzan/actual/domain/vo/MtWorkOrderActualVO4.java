package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtWorkOrderActualVO4 implements Serializable {

    private static final long serialVersionUID = 4165578224913027576L;
    private String workOrderActualId; // 主键
    private String workOrderId; // EO主键，标识唯一EO
    private String eventId; // 事件Id
    private Double releasedQty; // 已下达数量（生成EO的数量）
    private Double completedQty; // 已完成数量
    private Double scrappedQty; // 已报废数量
    private Double holdQty; // 已保留数量
    private String actualStartDate; // 实际开始时间 格式：(yyyy-MM-dd HH:mm:ss)
    private String actualEndDate; // 实际完成时间 格式：(yyyy-MM-dd HH:mm:ss)

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(String actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }
}
