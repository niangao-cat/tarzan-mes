package tarzan.iface.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019-10-14 19:18
 */
public class MtSapPoIfaceVO implements Serializable {
    private static final long serialVersionUID = -8113195458401134739L;
    private String plantCode;
    private String poNumber;
    private String contractNum;
    private String supplierCode;
    private String supplierSiteCode;
    private String buyerCode;
    private String poCategory;
    private String poOrderType;
    private String approvedFlag;
    private String description;
    private String currencyCode;
    private String poEnableFlag;
    private String transferPlantCode;

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierSiteCode() {
        return supplierSiteCode;
    }

    public void setSupplierSiteCode(String supplierSiteCode) {
        this.supplierSiteCode = supplierSiteCode;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public String getPoCategory() {
        return poCategory;
    }

    public void setPoCategory(String poCategory) {
        this.poCategory = poCategory;
    }

    public String getPoOrderType() {
        return poOrderType;
    }

    public void setPoOrderType(String poOrderType) {
        this.poOrderType = poOrderType;
    }

    public String getApprovedFlag() {
        return approvedFlag;
    }

    public void setApprovedFlag(String approvedFlag) {
        this.approvedFlag = approvedFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPoEnableFlag() {
        return poEnableFlag;
    }

    public void setPoEnableFlag(String poEnableFlag) {
        this.poEnableFlag = poEnableFlag;
    }

    public String getTransferPlantCode() {
        return transferPlantCode;
    }

    public void setTransferPlantCode(String transferPlantCode) {
        this.transferPlantCode = transferPlantCode;
    }

    public MtSapPoIfaceVO(String plantCode, String poNumber, String contractNum, String supplierCode,
                    String supplierSiteCode, String buyerCode, String poCategory, String poOrderType,
                    String approvedFlag, String description, String currencyCode, String poEnableFlag,
                    String transferPlantCode) {
        this.plantCode = plantCode;
        this.poNumber = poNumber;
        this.contractNum = contractNum;
        this.supplierCode = supplierCode;
        this.supplierSiteCode = supplierSiteCode;
        this.buyerCode = buyerCode;
        this.poCategory = poCategory;
        this.poOrderType = poOrderType;
        this.approvedFlag = approvedFlag;
        this.description = description;
        this.currencyCode = currencyCode;
        this.poEnableFlag = poEnableFlag;
        this.transferPlantCode = transferPlantCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MtSapPoIfaceVO that = (MtSapPoIfaceVO) o;

        if (getPlantCode() != null ? !getPlantCode().equals(that.getPlantCode()) : that.getPlantCode() != null) {
            return false;
        }
        if (getPoNumber() != null ? !getPoNumber().equals(that.getPoNumber()) : that.getPoNumber() != null) {
            return false;
        }
        if (getContractNum() != null ? !getContractNum().equals(that.getContractNum())
                        : that.getContractNum() != null) {
            return false;
        }
        if (getSupplierCode() != null ? !getSupplierCode().equals(that.getSupplierCode())
                        : that.getSupplierCode() != null) {
            return false;
        }
        if (getSupplierSiteCode() != null ? !getSupplierSiteCode().equals(that.getSupplierSiteCode())
                        : that.getSupplierSiteCode() != null) {
            return false;
        }
        if (getBuyerCode() != null ? !getBuyerCode().equals(that.getBuyerCode()) : that.getBuyerCode() != null) {
            return false;
        }
        if (getPoCategory() != null ? !getPoCategory().equals(that.getPoCategory()) : that.getPoCategory() != null) {
            return false;
        }
        if (getPoOrderType() != null ? !getPoOrderType().equals(that.getPoOrderType())
                        : that.getPoOrderType() != null) {
            return false;
        }
        if (getApprovedFlag() != null ? !getApprovedFlag().equals(that.getApprovedFlag())
                        : that.getApprovedFlag() != null) {
            return false;
        }
        if (getDescription() != null ? !getDescription().equals(that.getDescription())
                        : that.getDescription() != null) {
            return false;
        }
        if (getCurrencyCode() != null ? !getCurrencyCode().equals(that.getCurrencyCode())
                        : that.getCurrencyCode() != null) {
            return false;
        }
        if (getPoEnableFlag() != null ? !getPoEnableFlag().equals(that.getPoEnableFlag())
                        : that.getPoEnableFlag() != null) {
            return false;
        }
        return getTransferPlantCode() != null ? getTransferPlantCode().equals(that.getTransferPlantCode())
                        : that.getTransferPlantCode() == null;
    }

    @Override
    public int hashCode() {
        int result = getPlantCode() != null ? getPlantCode().hashCode() : 0;
        result = 31 * result + (getPoNumber() != null ? getPoNumber().hashCode() : 0);
        result = 31 * result + (getContractNum() != null ? getContractNum().hashCode() : 0);
        result = 31 * result + (getSupplierCode() != null ? getSupplierCode().hashCode() : 0);
        result = 31 * result + (getSupplierSiteCode() != null ? getSupplierSiteCode().hashCode() : 0);
        result = 31 * result + (getBuyerCode() != null ? getBuyerCode().hashCode() : 0);
        result = 31 * result + (getPoCategory() != null ? getPoCategory().hashCode() : 0);
        result = 31 * result + (getPoOrderType() != null ? getPoOrderType().hashCode() : 0);
        result = 31 * result + (getApprovedFlag() != null ? getApprovedFlag().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getCurrencyCode() != null ? getCurrencyCode().hashCode() : 0);
        result = 31 * result + (getPoEnableFlag() != null ? getPoEnableFlag().hashCode() : 0);
        result = 31 * result + (getTransferPlantCode() != null ? getTransferPlantCode().hashCode() : 0);
        return result;
    }
}
