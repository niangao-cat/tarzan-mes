package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtContainerVO13 implements Serializable {
    private static final long serialVersionUID = -752195622425784791L;

    @ApiModelProperty("容器主键ID")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("容器类型ID")
    private String containerTypeId;
    @ApiModelProperty("容器名称")
    private String containerName;
    @ApiModelProperty("容器描述")
    private String description;
    @ApiModelProperty("容器所在站点")
    private String siteId;
    @ApiModelProperty("容器所在库位")
    private String locatorId;
    @ApiModelProperty("容器当前状态")
    private String status;
    @ApiModelProperty("容器内实物所有者类型")
    private String ownerType;
    @ApiModelProperty("容器内所有者")
    private String ownerId;
    @ApiModelProperty("容器内实物是否被预留")
    private String reservedFlag;
    @ApiModelProperty("容器内实物预留对象类型")
    private String reservedObjectType;
    @ApiModelProperty("容器内实物预留对象")
    private String reservedObjectId;
    @ApiModelProperty("容器标识")
    private String identification;
    @ApiModelProperty("容器最后一次装载时间从")
    private Date lastLoadTimeFrom;
    @ApiModelProperty("容器最后一次装载时间到")
    private Date lastLoadTimeTo;
    @ApiModelProperty("容器最后一次卸载时间从")
    private Date lastUnloadTimeFrom;
    @ApiModelProperty("容器最后一次卸载时间到")
    private Date lastUnloadTimeTo;
    @ApiModelProperty("当前容器ID")
    private String currentContainerId;
    @ApiModelProperty("顶层容器ID")
    private String topContainerId;


    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(String reservedFlag) {
        this.reservedFlag = reservedFlag;
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

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public Date getLastLoadTimeFrom() {
        if (lastLoadTimeFrom != null) {
            return (Date) lastLoadTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setLastLoadTimeFrom(Date lastLoadTimeFrom) {
        if (lastLoadTimeFrom == null) {
            this.lastLoadTimeFrom = null;
        } else {
            this.lastLoadTimeFrom = (Date) lastLoadTimeFrom.clone();
        }
    }

    public Date getLastLoadTimeTo() {
        if (lastLoadTimeTo != null) {
            return (Date) lastLoadTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setLastLoadTimeTo(Date lastLoadTimeTo) {
        if (lastLoadTimeTo == null) {
            this.lastLoadTimeTo = null;
        } else {
            this.lastLoadTimeTo = (Date) lastLoadTimeTo.clone();
        }
    }

    public Date getLastUnloadTimeFrom() {
        if (lastUnloadTimeFrom != null) {
            return (Date) lastUnloadTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setLastUnloadTimeFrom(Date lastUnloadTimeFrom) {
        if (lastUnloadTimeFrom == null) {
            this.lastUnloadTimeFrom = null;
        } else {
            this.lastUnloadTimeFrom = (Date) lastUnloadTimeFrom.clone();
        }
    }

    public Date getLastUnloadTimeTo() {
        if (lastUnloadTimeTo != null) {
            return (Date) lastUnloadTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setLastUnloadTimeTo(Date lastUnloadTimeTo) {
        if (lastUnloadTimeTo == null) {
            this.lastUnloadTimeTo = null;
        } else {
            this.lastUnloadTimeTo = (Date) lastUnloadTimeTo.clone();
        }
    }

    public String getCurrentContainerId() {
        return currentContainerId;
    }

    public void setCurrentContainerId(String currentContainerId) {
        this.currentContainerId = currentContainerId;
    }

    public String getTopContainerId() {
        return topContainerId;
    }

    public void setTopContainerId(String topContainerId) {
        this.topContainerId = topContainerId;
    }
}
