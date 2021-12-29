package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtContainerVO12 implements Serializable {


    private static final long serialVersionUID = 274474379404589126L;
    @ApiModelProperty("作为容器唯一标识，用于其他数据结构引用该容器")
    private String containerId;
    @ApiModelProperty(value = "标识该容器的唯一编码CODE，用于可视化识别", required = true)
    private String containerCode;
    @ApiModelProperty(value = "表示该容器所属的容器类型ID", required = true)
    private String containerTypeId;
    @ApiModelProperty(value = "该容器的名称")
    private String containerName;
    @ApiModelProperty(value = "对该容器的详细描述")
    private String description;
    @ApiModelProperty(value = "描述该容器所在的站点标识ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "描述该容器所在的货位标识ID")
    private String locatorId;
    @ApiModelProperty(value = "描述该容器的状态，包括：", required = true)
    private String status;
    @ApiModelProperty(value = "描述容器下表示实物的所有权属于客户还是供应商，内容包括：C：客户，表示该容器表示的实物属于客户S：供应商，表示该容器表示的实物属于供应商；空：表示容器表示的实物属于厂内自有")
    private String ownerType;
    @ApiModelProperty(value = "配合所有者类型使用，描述容器表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID")
    private String ownerId;
    @ApiModelProperty(value = "指示物料批标识实物是否被保留")
    private String reservedFlag;
    @ApiModelProperty(
                    value = "当容器被保留时，指示容器的被保留对象的类型，内容包括：WO：该容器表示的实物被某一生产指令保留EO：该容器表示的实物被某一执行作业保留DRIVING：该容器表示的实物被某一驱动指令保留CUSTOM：该容器表示的实物被某一客户保留OO：该容器表示的实物被某一机会订单预留")
    private String reservedObjectType;
    @ApiModelProperty(value = "当容器被保留时，指示容器的保留对象值，配合保留对象类型使用；优先考虑容器的保留和保留对象，其次考虑容器下表示实物的保留和保留对象")
    private String reservedObjectId;
    @ApiModelProperty(value = "标识该容器临时使用的容器编码以外的可视化标识", required = true)
    private String identification;
    @ApiModelProperty(value = "描述该容器最后一次进行装载操作的时间")
    private Date lastLoadTime;
    @ApiModelProperty(value = "描述该容器最后一次进行卸载操作的时间")
    private Date lastUnloadTime;
    @ApiModelProperty(value = "当前容器ID")
    private String currentContainerId;
    @ApiModelProperty(value = "顶层容器ID")
    private String topContainerId;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "外部输入编码")
    private String outsideNum;
    @ApiModelProperty(value = "传入值参数列表")
    private List<String> incomingValueList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }

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
}
