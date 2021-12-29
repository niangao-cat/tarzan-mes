package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtInvOnhandQuantityVO13 implements Serializable {

    private static final long serialVersionUID = -254045927526510246L;
    @ApiModelProperty("站点ID")
    private  String siteId;

    @ApiModelProperty("物料ID")
    private  String materialId;

    @ApiModelProperty("库位ID")
    private  String locatorId;

    @ApiModelProperty("批次编码")
    private String lotCode;

    @ApiModelProperty("所有者类型")
    private String ownerType;

    @ApiModelProperty("所有者ID")
    private  String ownerId;

    @ApiModelProperty("变更数量")
    private Double changeQuantity;

    public  String getSiteId() {
        return siteId;
    }

    public void setSiteId( String siteId) {
        this.siteId = siteId;
    }

    public  String getMaterialId() {
        return materialId;
    }

    public void setMaterialId( String materialId) {
        this.materialId = materialId;
    }

    public  String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId( String actualLocatorId) {
        this.locatorId = actualLocatorId;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public  String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId( String ownerId) {
        this.ownerId = ownerId;
    }

    public Double getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(Double changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    public MtInvOnhandQuantityVO13( String siteId,  String materialId,  String locatorId, String lotCode, String ownerType,  String ownerId) {
        this.siteId = siteId;
        this.materialId = materialId;
        this.locatorId = locatorId;
        this.lotCode = lotCode;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
    }

    public MtInvOnhandQuantityVO13() {
    }
}
