package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtInvOnhandHoldVO4 implements Serializable {
    private static final long serialVersionUID = 2784588877759751684L;
    private String siteId; // 站点ID
    private String materialId; // 物料ID
    private String locatorId; // 库位ID
    private String lotCode; // 批次CODE
    private Double releaseQuantity; // 保留数量
    private String holdType; // 保留类型（手工/指令）
    private String orderType; // 指令类型
    private String orderId; // 指令ID
    private String ownerType; // 2019.7.10增加"所有者类型"
    private String ownerId; // 2019.7.10增加"所有者ID"

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public Double getReleaseQuantity() {
        return releaseQuantity;
    }

    public void setReleaseQuantity(Double releaseQuantity) {
        this.releaseQuantity = releaseQuantity;
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
