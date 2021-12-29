package tarzan.inventory.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtInvOnhandQuantityVO11
 * @description
 * @date 2019年09月26日 19:54
 */
public class MtInvOnhandQuantityVO11 implements Serializable {
    private static final long serialVersionUID = 3467754809736431432L;
    private  String siteId; // 站点ID
    private String siteCode; // 站点编码
    private String siteName; // 站点描述
    private  String materialId; // 物料ID
    private String materialCode; // 物料编码
    private String materialName; // 物料描述
    private  String locatorId; // 库位ID
    private String locatorCode; // 库位编码
    private String locatorName; // 库位描述
    private String lotCode; // 批次CODE
    private String ownerType; // 所有者类型
    private  String ownerId; // 所有者ID
    private Double onhandQuantity; // 变化后库存数量

    public  String getSiteId() {
        return siteId;
    }

    public void setSiteId( String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public  String getMaterialId() {
        return materialId;
    }

    public void setMaterialId( String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public  String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId( String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
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

    public Double getOnhandQuantity() {
        return onhandQuantity;
    }

    public void setOnhandQuantity(Double onhandQuantity) {
        this.onhandQuantity = onhandQuantity;
    }
}
