package tarzan.inventory.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtContainerDTO2 implements Serializable {

    private static final long serialVersionUID = 7310645967658520757L;
    @ApiModelProperty(value = "容器ID")
    private String containerId;
    @ApiModelProperty(value = "容器编码")
    private String containerCode;
    @ApiModelProperty(value = "容器类型ID")
    private String containerTypeId;
    @ApiModelProperty(value = "该容器的名称")
    private String containerName;
    @ApiModelProperty(value = "对该容器的详细描述")
    private String description;
    @ApiModelProperty(value = "描述该容器所在的站点标识ID")
    private String siteId;
    @ApiModelProperty(value = "描述该容器所在的货位标识ID")
    private String locatorId;
    @ApiModelProperty(value = "描述该容器的状态")
    private String status;
    @ApiModelProperty(value = "描述容器下表示实物的所有权属于客户还是供应商")
    private String ownerType;
    @ApiModelProperty(value = "配合所有者类型使用，描述容器表示的实物具体的所有权对象")
    private String ownerId;
    @ApiModelProperty(value = "指示物料批标识实物是否被保留")
    private String reservedFlag;
    @ApiModelProperty(value = "指示物料批标识实物是否被保留")
    private String reservedObjectType;
    @ApiModelProperty(value = "当容器被保留时，指示容器的保留对象值，配合保留对象类型使用")
    private String reservedObjectId;
    @ApiModelProperty(value = "标识该容器临时使用的容器编码以外的可视化标识")
    private String identification;
    @ApiModelProperty(value = "描述该容器最后一次进行装载操作的时间")
    private Date lastLoadTime;
    @ApiModelProperty(value = "描述该容器最后一次进行卸载操作的时间")
    private Date lastUnloadTime;
    @ApiModelProperty(value = "容器ID")
    private String eventId;
    @ApiModelProperty(value = "当前容器ID")
    private String currentContainerId;
    @ApiModelProperty(value = "顶层容器ID")
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

    public Date getLastLoadTime() {
        if (lastLoadTime == null) {
            return null;
        } else {
            return (Date) lastLoadTime.clone();
        }
    }

    public void setLastLoadTime(Date lastLoadTime) {
        if (lastLoadTime == null) {
            this.lastLoadTime = null;
        } else {
            this.lastLoadTime = (Date) lastLoadTime.clone();
        }
    }

    public Date getLastUnloadTime() {
        if (lastUnloadTime == null) {
            return null;
        } else {
            return (Date) lastUnloadTime.clone();
        }
    }

    public void setLastUnloadTime(Date lastUnloadTime) {
        if (lastUnloadTime == null) {
            this.lastUnloadTime = null;
        } else {
            this.lastUnloadTime = (Date) lastUnloadTime.clone();
        }
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
