package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: chuang.yang
 * @Date: 2019/4/17 15:24
 * @Description:
 */
public class MtContainerAttrHisVO2 implements Serializable {
    private static final long serialVersionUID = -5360468286124638834L;

    private String containerId; // 容器ID
    private String attrName; // 拓展属性名
    private String eventId; // 事件ID
    private String eventBy; // 事件人
    private Date eventTimeFrom; // 事件时间从
    private Date eventTimeTo; // 事件时间到

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
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
