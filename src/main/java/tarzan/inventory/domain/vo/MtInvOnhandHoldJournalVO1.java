package tarzan.inventory.domain.vo;


import java.io.Serializable;

import tarzan.inventory.domain.entity.MtInvOnhandHoldJournal;

/**
 * @author Leeloing
 * @date 2019/5/13 10:34
 */
public class MtInvOnhandHoldJournalVO1 extends MtInvOnhandHoldJournal implements Serializable {

    private static final long serialVersionUID = 7223698164231656731L;

    private String materialCode;
    private String materialDesc;
    private String locatorCode;
    private String locatorDesc;
    private String orderTypeDesc;
    private String holdTypeDesc;
    private String eventType;
    private String eventTypeDesc;
    private String eventByUserName;
    private String ownerTypeDesc;
    private String ownerCode;
    private String ownerDesc;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorDesc() {
        return locatorDesc;
    }

    public void setLocatorDesc(String locatorDesc) {
        this.locatorDesc = locatorDesc;
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public String getHoldTypeDesc() {
        return holdTypeDesc;
    }

    public void setHoldTypeDesc(String holdTypeDesc) {
        this.holdTypeDesc = holdTypeDesc;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTypeDesc() {
        return eventTypeDesc;
    }

    public void setEventTypeDesc(String eventTypeDesc) {
        this.eventTypeDesc = eventTypeDesc;
    }

    public String getEventByUserName() {
        return eventByUserName;
    }

    public void setEventByUserName(String eventByUserName) {
        this.eventByUserName = eventByUserName;
    }

    public String getOwnerTypeDesc() {
        return ownerTypeDesc;
    }

    public void setOwnerTypeDesc(String ownerTypeDesc) {
        this.ownerTypeDesc = ownerTypeDesc;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerDesc() {
        return ownerDesc;
    }

    public void setOwnerDesc(String ownerDesc) {
        this.ownerDesc = ownerDesc;
    }
}
