package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/7/8 15:44
 */
public class MtStocktakeActualVO1 implements Serializable {
    private static final long serialVersionUID = 4263309101959687224L;

    private String stocktakeId;
    private String materialLotId;

    /**
     * 初盘数据
     */
    private String firstcountMaterialId;
    private String firstcountUomId;
    private String firstcountLocatorId;
    private String firstcountContainerId;
    private Long firstcountLocationRow;
    private Long firstcountLocationColumn;
    private String firstcountOwnerType;
    private String firstcountOwnerId;
    private String firstcountReservedObjectTy;
    private String firstcountReservedObjectId;
    private Double firstcountQuantity;
    private String firstcountRemark;

    /**
     * 复盘数据
     */
    private String recountMaterialId;
    private String recountUomId;
    private String recountLocatorId;
    private String recountContainerId;
    private Long recountLocationRow;
    private Long recountLocationColumn;
    private String recountOwnerType;
    private String recountOwnerId;
    private String recountReservedObjectType;
    private String recountReservedObjectId;
    private Double recountQuantity;
    private String recountRemark;
    private String eventRequestId;

    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getFirstcountMaterialId() {
        return firstcountMaterialId;
    }

    public void setFirstcountMaterialId(String firstcountMaterialId) {
        this.firstcountMaterialId = firstcountMaterialId;
    }

    public String getFirstcountUomId() {
        return firstcountUomId;
    }

    public void setFirstcountUomId(String firstcountUomId) {
        this.firstcountUomId = firstcountUomId;
    }

    public String getFirstcountLocatorId() {
        return firstcountLocatorId;
    }

    public void setFirstcountLocatorId(String firstcountLocatorId) {
        this.firstcountLocatorId = firstcountLocatorId;
    }

    public String getFirstcountContainerId() {
        return firstcountContainerId;
    }

    public void setFirstcountContainerId(String firstcountContainerId) {
        this.firstcountContainerId = firstcountContainerId;
    }

    public Long getFirstcountLocationRow() {
        return firstcountLocationRow;
    }

    public void setFirstcountLocationRow(Long firstcountLocationRow) {
        this.firstcountLocationRow = firstcountLocationRow;
    }

    public Long getFirstcountLocationColumn() {
        return firstcountLocationColumn;
    }

    public void setFirstcountLocationColumn(Long firstcountLocationColumn) {
        this.firstcountLocationColumn = firstcountLocationColumn;
    }

    public String getFirstcountOwnerType() {
        return firstcountOwnerType;
    }

    public void setFirstcountOwnerType(String firstcountOwnerType) {
        this.firstcountOwnerType = firstcountOwnerType;
    }

    public String getFirstcountOwnerId() {
        return firstcountOwnerId;
    }

    public void setFirstcountOwnerId(String firstcountOwnerId) {
        this.firstcountOwnerId = firstcountOwnerId;
    }

    public String getFirstcountReservedObjectTy() {
        return firstcountReservedObjectTy;
    }

    public void setFirstcountReservedObjectTy(String firstcountReservedObjectTy) {
        this.firstcountReservedObjectTy = firstcountReservedObjectTy;
    }

    public String getFirstcountReservedObjectId() {
        return firstcountReservedObjectId;
    }

    public void setFirstcountReservedObjectId(String firstcountReservedObjectId) {
        this.firstcountReservedObjectId = firstcountReservedObjectId;
    }

    public Double getFirstcountQuantity() {
        return firstcountQuantity;
    }

    public void setFirstcountQuantity(Double firstcountQuantity) {
        this.firstcountQuantity = firstcountQuantity;
    }

    public String getRecountMaterialId() {
        return recountMaterialId;
    }

    public void setRecountMaterialId(String recountMaterialId) {
        this.recountMaterialId = recountMaterialId;
    }

    public String getRecountUomId() {
        return recountUomId;
    }

    public void setRecountUomId(String recountUomId) {
        this.recountUomId = recountUomId;
    }

    public String getRecountLocatorId() {
        return recountLocatorId;
    }

    public void setRecountLocatorId(String recountLocatorId) {
        this.recountLocatorId = recountLocatorId;
    }

    public String getRecountContainerId() {
        return recountContainerId;
    }

    public void setRecountContainerId(String recountContainerId) {
        this.recountContainerId = recountContainerId;
    }

    public Long getRecountLocationRow() {
        return recountLocationRow;
    }

    public void setRecountLocationRow(Long recountLocationRow) {
        this.recountLocationRow = recountLocationRow;
    }

    public Long getRecountLocationColumn() {
        return recountLocationColumn;
    }

    public void setRecountLocationColumn(Long recountLocationColumn) {
        this.recountLocationColumn = recountLocationColumn;
    }

    public String getRecountOwnerType() {
        return recountOwnerType;
    }

    public void setRecountOwnerType(String recountOwnerType) {
        this.recountOwnerType = recountOwnerType;
    }

    public String getRecountOwnerId() {
        return recountOwnerId;
    }

    public void setRecountOwnerId(String recountOwnerId) {
        this.recountOwnerId = recountOwnerId;
    }

    public String getRecountReservedObjectType() {
        return recountReservedObjectType;
    }

    public void setRecountReservedObjectType(String recountReservedObjectType) {
        this.recountReservedObjectType = recountReservedObjectType;
    }

    public String getRecountReservedObjectId() {
        return recountReservedObjectId;
    }

    public void setRecountReservedObjectId(String recountReservedObjectId) {
        this.recountReservedObjectId = recountReservedObjectId;
    }

    public Double getRecountQuantity() {
        return recountQuantity;
    }

    public void setRecountQuantity(Double recountQuantity) {
        this.recountQuantity = recountQuantity;
    }

    public String getFirstcountRemark() {
        return firstcountRemark;
    }

    public void setFirstcountRemark(String firstcountRemark) {
        this.firstcountRemark = firstcountRemark;
    }

    public String getRecountRemark() {
        return recountRemark;
    }

    public void setRecountRemark(String recountRemark) {
        this.recountRemark = recountRemark;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
