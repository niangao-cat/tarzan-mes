package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModProdLineManufacturingVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -777594338350165819L;
    private String prodLineManufacturingId;
    private String prodLineId;
    private String issuedLocatorId;
    private String issuedLocatorCode;
    private String issuedLocatorName;
    private String completionLocatorId;
    private String completionLocatorCode;
    private String completionLocatorName;
    private String inventoryLocatorId;
    private String inventoryLocatorCode;
    private String inventoryLocatorName;
    private String dispatchMethod;
    private String dispatchMethodDesc;

    public String getProdLineManufacturingId() {
        return prodLineManufacturingId;
    }

    public void setProdLineManufacturingId(String prodLineManufacturingId) {
        this.prodLineManufacturingId = prodLineManufacturingId;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getIssuedLocatorCode() {
        return issuedLocatorCode;
    }

    public void setIssuedLocatorCode(String issuedLocatorCode) {
        this.issuedLocatorCode = issuedLocatorCode;
    }

    public String getIssuedLocatorName() {
        return issuedLocatorName;
    }

    public void setIssuedLocatorName(String issuedLocatorName) {
        this.issuedLocatorName = issuedLocatorName;
    }

    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
    }

    public String getCompletionLocatorCode() {
        return completionLocatorCode;
    }

    public void setCompletionLocatorCode(String completionLocatorCode) {
        this.completionLocatorCode = completionLocatorCode;
    }

    public String getCompletionLocatorName() {
        return completionLocatorName;
    }

    public void setCompletionLocatorName(String completionLocatorName) {
        this.completionLocatorName = completionLocatorName;
    }

    public String getInventoryLocatorId() {
        return inventoryLocatorId;
    }

    public void setInventoryLocatorId(String inventoryLocatorId) {
        this.inventoryLocatorId = inventoryLocatorId;
    }

    public String getInventoryLocatorCode() {
        return inventoryLocatorCode;
    }

    public void setInventoryLocatorCode(String inventoryLocatorCode) {
        this.inventoryLocatorCode = inventoryLocatorCode;
    }

    public String getInventoryLocatorName() {
        return inventoryLocatorName;
    }

    public void setInventoryLocatorName(String inventoryLocatorName) {
        this.inventoryLocatorName = inventoryLocatorName;
    }

    public String getDispatchMethod() {
        return dispatchMethod;
    }

    public void setDispatchMethod(String dispatchMethod) {
        this.dispatchMethod = dispatchMethod;
    }

    public String getDispatchMethodDesc() {
        return dispatchMethodDesc;
    }

    public void setDispatchMethodDesc(String dispatchMethodDesc) {
        this.dispatchMethodDesc = dispatchMethodDesc;
    }

}
