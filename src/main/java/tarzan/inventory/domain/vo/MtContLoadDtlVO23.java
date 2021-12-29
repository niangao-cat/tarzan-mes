package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtContLoadDtlVO23 implements Serializable {

    private static final long serialVersionUID = 7806855160848787912L;
    @ApiModelProperty("容器Id")
    private String containerId;

    @ApiModelProperty("容器行")
    private Long locationRow;

    @ApiModelProperty("容器列")
    private Long locationColumn;

    @ApiModelProperty("装载对象类型")
    private String loadObjectType;

    @ApiModelProperty("装载对象ID")
    private String loadObjectId;

    @ApiModelProperty("本次装载数量")
    private Double trxLoadQty;

    @ApiModelProperty("装载数量")
    private Double loadQty;

    @ApiModelProperty("EO步骤步骤")
    private String loadEoStepWipId;

    @ApiModelProperty("顶层容器Id")
    private String topContainerId;

    @ApiModelProperty("预留对象类型")
    private String reservedObjectType;

    @ApiModelProperty("预留对象")
    private String reservedObjectId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public Double getTrxLoadQty() {
        return trxLoadQty;
    }

    public void setTrxLoadQty(Double trxLoadQty) {
        this.trxLoadQty = trxLoadQty;
    }

    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }

    public String getLoadEoStepWipId() {
        return loadEoStepWipId;
    }

    public void setLoadEoStepWipId(String loadEoStepWipId) {
        this.loadEoStepWipId = loadEoStepWipId;
    }

    public String getTopContainerId() {
        return topContainerId;
    }

    public void setTopContainerId(String topContainerId) {
        this.topContainerId = topContainerId;
    }

    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }
}
