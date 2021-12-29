package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtInvOnhandHoldVO8 extends MtInvOnhandHoldVO10 implements Serializable {


    private static final long serialVersionUID = -6507164375816742929L;
    private Double holdQuantity;
    private String eventId; // 事件Id
    private String ownerType; // 2019.7.10增加"所有者类型"
    private String ownerId; // 2019.7.10增加"所有者ID"

    public Double getHoldQuantity() {
        return holdQuantity;
    }

    public void setHoldQuantity(Double holdQuantity) {
        this.holdQuantity = holdQuantity;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

}
