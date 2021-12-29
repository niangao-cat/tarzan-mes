package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModProductionLineVO1 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8363167784415013496L;

    private String prodLineCode; // 生产线编号
    private String prodLineName; // 生产线名称
    private String description; // 生产线描述
    private String prodLineType; // 生产线类型
    private String supplierId; // 供应商ID
    private String supplierSiteId; // 供应商地点ID
    private String enableFlag; // 是否有效
    private String prodLineCategory; // 生产线分类


    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProdLineType() {
        return prodLineType;
    }

    public void setProdLineType(String prodLineType) {
        this.prodLineType = prodLineType;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getProdLineCategory() {
        return prodLineCategory;
    }

    public void setProdLineCategory(String prodLineCategory) {
        this.prodLineCategory = prodLineCategory;
    }

}
