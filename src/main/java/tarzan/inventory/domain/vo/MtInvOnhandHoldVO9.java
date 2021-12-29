package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtInvOnhandHoldVO9 extends MtInvOnhandHoldVO10 implements Serializable {


    private static final long serialVersionUID = -1842276820282536583L;
    private Double releaseQuantity;
    private String eventId; // 事件ID
    private String ownerType; // 2019.7.10增加"所有者类型"
    private String ownerId; // 2019.7.10增加"所有者ID"

    public Double getReleaseQuantity() {
        return releaseQuantity;
    }

    public void setReleaseQuantity(Double releaseQuantity) {
        this.releaseQuantity = releaseQuantity;
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
