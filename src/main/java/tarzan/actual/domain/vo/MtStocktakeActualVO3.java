package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/7/9 14:05
 */
public class MtStocktakeActualVO3 implements Serializable {
    private static final long serialVersionUID = -2738746704789271943L;

    private String adjustMaterialid;
    private String adjustUomId;
    private String adjustLocatorid;
    private String adjustContainerid;
    private Long adjustcountLocationRow;
    private Long adjustcountLocationColumn;
    private String adjustOwnerType;
    private String adjustOwnerId;
    private String adjustReservedObjectType;
    private String adjustReservedObjectId;
    private Double adjustQuantity;
    private String adjustRemark;

    public String getAdjustMaterialid() {
        return adjustMaterialid;
    }

    public void setAdjustMaterialid(String adjustMaterialid) {
        this.adjustMaterialid = adjustMaterialid;
    }

    public String getAdjustUomId() {
        return adjustUomId;
    }

    public void setAdjustUomId(String adjustUomId) {
        this.adjustUomId = adjustUomId;
    }

    public String getAdjustLocatorid() {
        return adjustLocatorid;
    }

    public void setAdjustLocatorid(String adjustLocatorid) {
        this.adjustLocatorid = adjustLocatorid;
    }

    public String getAdjustContainerid() {
        return adjustContainerid;
    }

    public void setAdjustContainerid(String adjustContainerid) {
        this.adjustContainerid = adjustContainerid;
    }

    public Long getAdjustcountLocationRow() {
        return adjustcountLocationRow;
    }

    public void setAdjustcountLocationRow(Long adjustcountLocationRow) {
        this.adjustcountLocationRow = adjustcountLocationRow;
    }

    public Long getAdjustcountLocationColumn() {
        return adjustcountLocationColumn;
    }

    public void setAdjustcountLocationColumn(Long adjustcountLocationColumn) {
        this.adjustcountLocationColumn = adjustcountLocationColumn;
    }

    public String getAdjustOwnerType() {
        return adjustOwnerType;
    }

    public void setAdjustOwnerType(String adjustOwnerType) {
        this.adjustOwnerType = adjustOwnerType;
    }

    public String getAdjustOwnerId() {
        return adjustOwnerId;
    }

    public void setAdjustOwnerId(String adjustOwnerId) {
        this.adjustOwnerId = adjustOwnerId;
    }

    public String getAdjustReservedObjectType() {
        return adjustReservedObjectType;
    }

    public void setAdjustReservedObjectType(String adjustReservedObjectType) {
        this.adjustReservedObjectType = adjustReservedObjectType;
    }

    public String getAdjustReservedObjectId() {
        return adjustReservedObjectId;
    }

    public void setAdjustReservedObjectId(String adjustReservedObjectId) {
        this.adjustReservedObjectId = adjustReservedObjectId;
    }

    public Double getAdjustQuantity() {
        return adjustQuantity;
    }

    public void setAdjustQuantity(Double adjustQuantity) {
        this.adjustQuantity = adjustQuantity;
    }

    public String getAdjustRemark() {
        return adjustRemark;
    }

    public void setAdjustRemark(String adjustRemark) {
        this.adjustRemark = adjustRemark;
    }
}
