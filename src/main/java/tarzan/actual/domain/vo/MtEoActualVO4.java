package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtEoActualVO4 implements Serializable {

    private static final long serialVersionUID = 7297746871133681425L;
    private String eoActualId; // 主键
    private String eoId; // EO主键，标识唯一EO
    private String eventId; // 事件Id
    private Double completedQty; // 累计完工数量
    private Double scrappedQty; // 累计报废数量
    private Double holdQty; // 累计保留数量
    private String actualStartTime; // 实际开始时间 格式：(yyyy-MM-dd HH:mm:ss)
    private String actualEndTime; // 实际完成时间 格式：(yyyy-MM-dd HH:mm:ss)

    public String getEoActualId() {
        return eoActualId;
    }

    public void setEoActualId(String eoActualId) {
        this.eoActualId = eoActualId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }
}
