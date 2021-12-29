package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtMaterialLotVO6 implements Serializable {

    private static final long serialVersionUID = -787087512298216506L;
    private String materialLotId;// 物料批ID
    private String reservedObjectType;// 预留对象类型
    private String reservedObjectId;// 预留对象值

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }
}
