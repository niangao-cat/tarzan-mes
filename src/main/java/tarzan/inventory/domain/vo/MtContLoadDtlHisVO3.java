package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtContLoadDtlHisVO3 implements Serializable {

    private static final long serialVersionUID = 742330909950104078L;

    private String containerLoadDetailId;// 容器装载明细ID
    private String containerId;// 容器
    private String eventId;// 事件ID
    private String eventTypeCode;// 事件类型
    private String requestTypeCode;// 事件请求编码
    private Date eventTime;// 事件时间
    private String eventBy;// 事件人
    private String loadObjectType;// 对象类型
    private String loadObjectId;// 对象值
    private Long locationRow;// 容器行
    private Long locationColumn;// 容器行
    private Long loadSequence;// 装载顺序
    private Double trxLoadQty; // 本次装载数量
    private Double loadQty; // 装载数量
    private String loadEoStepActualId; // 装载步骤

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
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

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
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

    public Double getTrxLoadQty() {
        return trxLoadQty;
    }

    public void setTrxLoadQty(Double trxLoadQty) {
        this.trxLoadQty = trxLoadQty;
    }

    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }

    public String getLoadEoStepActualId() {
        return loadEoStepActualId;
    }

    public void setLoadEoStepActualId(String loadEoStepActualId) {
        this.loadEoStepActualId = loadEoStepActualId;
    }
}
