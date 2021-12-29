package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/9/3 16:25
 */
public class MtInvOnhandQuantityVO12 implements Serializable {
    private static final long serialVersionUID = 2088845463858813742L;
    @ApiModelProperty("站点ID")
    private  String siteId;
    @ApiModelProperty("物料ID")
    private  String materialId;
    @ApiModelProperty("货位ID")
    private  String locatorId;
    @ApiModelProperty("货位编码")
    private String lotCode;
    @ApiModelProperty("所有者类型")
    private String ownerType;
    @ApiModelProperty("所有者ID")
    private  String ownerId;

    @ApiModelProperty("汇总数量")
    private BigDecimal sumAvailableQty;

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

    public void setLocatorId( String locatorId) {
        this.locatorId = locatorId;
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

    public BigDecimal getSumAvailableQty() {
        return sumAvailableQty;
    }

    public void setSumAvailableQty(BigDecimal sumAvailableQty) {
        this.sumAvailableQty = sumAvailableQty;
    }
}
