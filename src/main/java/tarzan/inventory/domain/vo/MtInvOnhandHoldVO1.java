package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author chuang.yang
 * @date 2021/4/22
 */
public class MtInvOnhandHoldVO1 implements Serializable {

    private String siteId;
    private String materialId;
    private String actualChangeLocatorId;
    private String lotCode;
    private Double actualChangeQuantity;
    private String ownerType;
    private String ownerId;
    private String holdType;
    private String orderType;
    private String orderId;

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

    public String getActualChangeLocatorId() {
        return actualChangeLocatorId;
    }

    public void setActualChangeLocatorId(String actualChangeLocatorId) {
        this.actualChangeLocatorId = actualChangeLocatorId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public Double getActualChangeQuantity() {
        return actualChangeQuantity;
    }

    public void setActualChangeQuantity(Double actualChangeQuantity) {
        this.actualChangeQuantity = actualChangeQuantity;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtInvOnhandHoldVO1 that = (MtInvOnhandHoldVO1) o;
        return Objects.equals(getSiteId(), that.getSiteId()) && Objects.equals(getMaterialId(), that.getMaterialId())
                        && Objects.equals(getActualChangeLocatorId(), that.getActualChangeLocatorId())
                        && Objects.equals(getLotCode(), that.getLotCode())
                        && Objects.equals(getActualChangeQuantity(), that.getActualChangeQuantity())
                        && Objects.equals(getOwnerType(), that.getOwnerType())
                        && Objects.equals(getOwnerId(), that.getOwnerId())
                        && Objects.equals(getHoldType(), that.getHoldType())
                        && Objects.equals(getOrderType(), that.getOrderType())
                        && Objects.equals(getOrderId(), that.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSiteId(), getMaterialId(), getActualChangeLocatorId(), getLotCode(),
                        getActualChangeQuantity(), getOwnerType(), getOwnerId(), getHoldType(), getOrderType(),
                        getOrderId());
    }
}

