package tarzan.inventory.domain.vo;

import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

@ExcelSheet(title = "库存查询")
public class MtInvOnhandQuantityVO4 implements Serializable {

    private static final long serialVersionUID = -1224310800563514315L;

    private String materialId;
    @ExcelColumn(title = "物料编码", order = 0)
    private String materialCode;
    @ExcelColumn(title = "物料描述", order = 1)
    private String materialDesc;

    private String locatorId;
    @ExcelColumn(title = "库位编码", order = 2)
    private String locatorCode;
    @ExcelColumn(title = "库位描述", order = 3)
    private String locatorDesc;

    private String siteId;
    private String siteCode;

    @ExcelColumn(title = "库存", order = 5)
    private Double onhandQty;
    @ExcelColumn(title = "预留库存", order = 7)
    private Double holdQty;
    @ExcelColumn(title = "可用库存", order = 6)
    private Double availableQty;

    @ExcelColumn(title = "批次", order = 4)
    private String lotCode;

    private String ownerType;
    private String ownerId;
    @ExcelColumn(title = "所有者编码", order = 9)
    private String ownerCode;
    @ExcelColumn(title = "所有者类型", order = 8)
    private String ownerTypeDesc;
    private String ownerDesc;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorDesc() {
        return locatorDesc;
    }

    public void setLocatorDesc(String locatorDesc) {
        this.locatorDesc = locatorDesc;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Double getOnhandQty() {
        return onhandQty;
    }

    public void setOnhandQty(Double onhandQty) {
        this.onhandQty = onhandQty;
    }

    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }

    public Double getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(Double availableQty) {
        this.availableQty = availableQty;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerTypeDesc() {
        return ownerTypeDesc;
    }

    public void setOwnerTypeDesc(String ownerTypeDesc) {
        this.ownerTypeDesc = ownerTypeDesc;
    }

    public String getOwnerDesc() {
        return ownerDesc;
    }

    public void setOwnerDesc(String ownerDesc) {
        this.ownerDesc = ownerDesc;
    }
}
