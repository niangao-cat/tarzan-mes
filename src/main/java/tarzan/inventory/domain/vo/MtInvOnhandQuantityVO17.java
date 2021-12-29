package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtInvOnhandQuantityVO17 implements Serializable {

    private static final long serialVersionUID = -254045927526510246L;
    @ApiModelProperty("站点ID")
    private  String siteId;

    @ApiModelProperty("物料ID")
    private  String materialId;

    @ApiModelProperty("实绩库位ID")
    private  String actualLocatorId;

    @ApiModelProperty("批次编码")
    private String lotCode;

    @ApiModelProperty("所有者类型")
    private String ownerType;

    @ApiModelProperty("所有者ID")
    private  String ownerId;

    @ApiModelProperty("实绩变更数量")
    private Double actualChangeQuantity;

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

    public void setActualLocatorId( String actualLocatorId) {
        this.actualLocatorId = actualLocatorId;
    }

    public Double getActualChangeQuantity() {
        return actualChangeQuantity;
    }

    public void setActualChangeQuantity(Double actualChangeQuantity) {
        this.actualChangeQuantity = actualChangeQuantity;
    }

    public MtInvOnhandQuantityVO17( String siteId,  String materialId,  String actualLocatorId, String lotCode, String ownerType,  String ownerId) {
        this.siteId = siteId;
        this.materialId = materialId;
        this.actualLocatorId = actualLocatorId;
        this.lotCode = lotCode;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
    }

    public MtInvOnhandQuantityVO17() {
    }

    public  String getActualLocatorId() {
        return actualLocatorId;
    }

}
