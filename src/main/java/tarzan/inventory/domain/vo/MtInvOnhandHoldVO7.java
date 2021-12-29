package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtInvOnhandHoldVO7 extends MtInvOnhandHoldVO10 implements Serializable {


    private static final long serialVersionUID = 7668800172631301900L;

    private Double quantity;
    private String eventId;
    private String ownerType; // 2019.7.10增加"所有者类型"
    private String ownerId; // 2019.7.10增加"所有者ID"

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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
