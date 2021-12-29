package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO8 implements Serializable {


    private static final long serialVersionUID = -5155357023972715384L;
    private String containerId;// 容器主键ID
    private Long locationRow;// 容器行
    private Long locationColumn;// 容器列
    private String loadObjectType;// 装载对象类型
    private String loadObjectId;// 装载对象ID
    private String eventId;// 事件ID
    private Double trxLoadQty; // 本次装载数量
    private Double loadQty; // 装载数量
    private String loadEoStepActualId; // 装载步骤

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
