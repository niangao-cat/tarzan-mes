package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * stocktakeActualUpdate-盘点实绩创建&更新 传入参数使用VO
 * 
 * @author benjamin
 * @date 2019-07-04 14:18
 */
public class MtStocktakeActualVO implements Serializable {
    private static final long serialVersionUID = -9164286868243527197L;

    /**
     * 盘点指令ID
     */
    private String stocktakeId;
    /**
     * 物料批ID
     */
    private String materialLotId;
    /**
     * 初盘物料ID
     */
    private String firstcountMaterialId;
    /**
     * 初盘主单位ID
     */
    private String firstcountUomId;
    /**
     * 初盘货位ID
     */
    private String firstcountLocatorId;
    /**
     * 初盘容器ID
     */
    private String firstcountContainerId;
    /**
     * 初盘容器装载物料对应行
     */
    private Long firstcountLocationRow;
    /**
     * 初盘容器装载物料对应列
     */
    private Long firstcountLocationColumn;
    /**
     * 初盘所有者类型
     */
    private String firstcountOwnerType;
    /**
     * 初盘所有者ID
     */
    private String firstcountOwnerId;
    /**
     * 初盘预留类型
     */
    private String firstcountReservedObjectTy;
    /**
     * 初盘预留对象ID
     */
    private String firstcountReservedObjectId;
    /**
     * 初盘数量，物料批主单位下的数量
     */
    private Double firstcountQuantity;
    /**
     * 复盘物料ID
     */
    private String recountMaterialId;
    /**
     * 复盘主单位ID
     */
    private String recountUomId;
    /**
     * 复盘货位ID
     */
    private String recountLocatorId;
    /**
     * 复盘容器ID
     */
    private String recountContainerId;
    /**
     * 复盘容器装载物料对应行
     */
    private Long recountLocationRow;
    /**
     * 复盘容器装载物料对应列
     */
    private Long recountLocationColumn;
    /**
     * 复盘所有者类型
     */
    private String recountOwnerType;
    /**
     * 复盘所有者ID
     */
    private String recountOwnerId;
    /**
     * 复盘预留类型
     */
    private String recountReservedObjectType;
    /**
     * 复盘预留对象ID
     */
    private String recountReservedObjectId;
    /**
     * 复盘数量
     */
    private Double recountQuantity;
    /**
     * 初盘备注
     */
    private String firstcountRemark;
    /**
     * 复盘备注
     */
    private String recountRemark;
    /**
     * 调整标识
     */
    private String adjustFlag;
    /**
     * 事件ID
     */
    private String eventId;

    private String eventRequestId;

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

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

    public String getAdjustFlag() {
        return adjustFlag;
    }

    public void setAdjustFlag(String adjustFlag) {
        this.adjustFlag = adjustFlag;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
