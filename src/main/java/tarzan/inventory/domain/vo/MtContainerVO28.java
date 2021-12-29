package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/18 15:18
 */
public class MtContainerVO28 implements Serializable {

    private static final long serialVersionUID = -3967918595981321397L;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("容器类型ID")
    private String containerTypeId;
    @ApiModelProperty("容器类型编码")
    private String containerTypeCode;
    @ApiModelProperty("容器类型描述")
    private String containerTypeDescription;
    @ApiModelProperty("容器名称")
    private String contanierName;
    @ApiModelProperty("容器描述")
    private String description;
    @ApiModelProperty("容器所在站点")
    private String siteId;
    @ApiModelProperty("容器所在站点编码")
    private String siteCode;
    @ApiModelProperty("容器所在站点描述")
    private String siteName;
    @ApiModelProperty("容器所在库位")
    private String locatorId;
    @ApiModelProperty("容器所在库位编码")
    private String locatorCode;
    @ApiModelProperty("容器所在库位描述")
    private String locatorName;
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
    @ApiModelProperty("容器最后一次装载事件")
    private Date lastLoadTime;
    @ApiModelProperty("容器最后一次卸载时间")
    private Date lastUnloadTime;
    @ApiModelProperty(value = "当前容器ID")
    private String currentContainerId;
    @ApiModelProperty(value = "顶层容器ID")
    private String topContainerId;
    @ApiModelProperty(value = "最新一次新增历史表的主键")
    private String latestHisId;

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

    public String getContainerTypeCode() {
        return containerTypeCode;
    }

    public void setContainerTypeCode(String containerTypeCode) {
        this.containerTypeCode = containerTypeCode;
    }

    public String getContainerTypeDescription() {
        return containerTypeDescription;
    }

    public void setContainerTypeDescription(String containerTypeDescription) {
        this.containerTypeDescription = containerTypeDescription;
    }

    public String getContanierName() {
        return contanierName;
    }

    public void setContanierName(String contanierName) {
        this.contanierName = contanierName;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
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

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
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
        if (lastLoadTime != null) {
            return (Date) lastLoadTime.clone();
        } else {
            return null;
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
        if (lastUnloadTime != null) {
            return (Date) lastUnloadTime.clone();
        } else {
            return null;
        }
    }

    public void setLastUnloadTime(Date lastUnloadTime) {
        if (lastUnloadTime == null) {
            this.lastUnloadTime = null;
        } else {
            this.lastUnloadTime = (Date) lastUnloadTime.clone();
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

    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
    }
}
