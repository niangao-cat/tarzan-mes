package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtContLoadDtlHisVO2 implements Serializable {

    private static final long serialVersionUID = 6552642166993222695L;

    private String containerLoadDetailId;// 容器装载明细
    private String containerId;// 容器
    private String loadObjectType;// 对象类型
    private String loadObjectId;// 对象ID
    private Long locationRow;// 容器行
    private Long locationColumn;// 容器列
    private Long loadSequence;// 装载顺序
    private String eventId;// 事件ID
    private String eventBy;// 事件人
    private Date eventTimeFrom;// 事件时间从
    private Date eventTimeTo;// 事件时间到

    public String getContainerLoadDetailId() {
        return containerLoadDetailId;
    }

    public void setContainerLoadDetailId(String containerLoadDetailId) {
        this.containerLoadDetailId = containerLoadDetailId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    public Long getLoadSequence() {
        return loadSequence;
    }

    public void setLoadSequence(Long loadSequence) {
        this.loadSequence = loadSequence;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

    public Date getEventTimeFrom() {
        if (eventTimeFrom != null) {
            return (Date) eventTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setEventTimeFrom(Date eventTimeFrom) {
        if (eventTimeFrom == null) {
            this.eventTimeFrom = null;
        } else {
            this.eventTimeFrom = (Date) eventTimeFrom.clone();
        }
    }

    public Date getEventTimeTo() {
        if (eventTimeTo != null) {
            return (Date) eventTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setEventTimeTo(Date eventTimeTo) {
        if (eventTimeTo == null) {
            this.eventTimeTo = null;
        } else {
            this.eventTimeTo = (Date) eventTimeTo.clone();
        }
    }
}
