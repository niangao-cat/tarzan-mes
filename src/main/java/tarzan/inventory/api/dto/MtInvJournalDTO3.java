package tarzan.inventory.api.dto;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/7/12 22:05
 */
public class MtInvJournalDTO3 implements Serializable {
    private static final long serialVersionUID = -428854264711850738L;
    private String ownerType;
    private String ownerId;

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

    public MtInvJournalDTO3(String ownerType, String ownerId) {
        this.ownerType = ownerType;
        this.ownerId = ownerId;
    }
}
