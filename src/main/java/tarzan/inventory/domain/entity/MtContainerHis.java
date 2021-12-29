package tarzan.inventory.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 容器历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@ApiModel("容器历史")

@ModifyAudit

@Table(name = "mt_container_his")
@CustomPrimary
public class MtContainerHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CONTAINER_HIS_ID = "containerHisId";
    public static final String FIELD_CONTAINER_ID = "containerId";
    public static final String FIELD_CONTAINER_CODE = "containerCode";
    public static final String FIELD_CONTAINER_TYPE_ID = "containerTypeId";
    public static final String FIELD_CONTAINER_NAME = "containerName";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_RESERVED_FLAG = "reservedFlag";
    public static final String FIELD_RESERVED_OBJECT_TYPE = "reservedObjectType";
    public static final String FIELD_RESERVED_OBJECT_ID = "reservedObjectId";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_LAST_LOAD_TIME = "lastLoadTime";
    public static final String FIELD_LAST_UNLOAD_TIME = "lastUnloadTime";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CURRENT_CONTAINER_ID = "currentContainerId";
    public static final String FIELD_TOP_CONTAINER_ID = "topContainerId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 2057945876767983836L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("作为容器唯一标识，用于其他数据结构引用该容器")
    @Id
    @Where
    private String containerHisId;
    @ApiModelProperty(value = "容器ID", required = true)
    @NotBlank
    @Where
    private String containerId;
    @ApiModelProperty(value = "标识该容器的唯一编码CODE，用于可视化识别", required = true)
    @NotBlank
    @Where
    private String containerCode;
    @ApiModelProperty(value = "表示该容器所属的容器类型ID", required = true)
    @NotBlank
    @Where
    private String containerTypeId;
    @ApiModelProperty(value = "该容器的名称")
    @Where
    private String containerName;
    @ApiModelProperty(value = "对该容器的详细描述")
    @Where
    private String description;
    @ApiModelProperty(value = "描述该容器所在的站点标识ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "描述该容器所在的货位标识ID")
    @Where
    private String locatorId;
    @ApiModelProperty(value = "描述该容器的状态，包括：", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "描述容器下表示实物的所有权属于客户还是供应商，内容包括：C：客户，表示该容器表示的实物属于客户S：供应商，表示该容器表示的实物属于供应商空：表示容器表示的实物属于厂内自有")
    @Where
    private String ownerType;
    @ApiModelProperty(value = "配合所有者类型使用，描述容器表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID")
    @Where
    private String ownerId;
    @ApiModelProperty(value = "指示物料批标识实物是否被保留")
    @Where
    private String reservedFlag;
    @ApiModelProperty(
                    value = "当容器被保留时，指示容器的被保留对象的类型，内容包括：WO：该容器表示的实物被某一生产指令保留EO：该容器表示的实物被某一执行作业保留DRIVING：该容器表示的实物被某一驱动指令保留CUSTOM：该容器表示的实物被某一客户保留OO：该容器表示的实物被某一机会订单预留")
    @Where
    private String reservedObjectType;
    @ApiModelProperty(value = "当容器被保留时，指示容器的保留对象值，配合保留对象类型使用；优先考虑容器的保留和保留对象，其次考虑容器下表示实物的保留和保留对象")
    @Where
    private String reservedObjectId;
    @ApiModelProperty(value = "标识该容器临时使用的容器编码以外的可视化标识", required = true)
    @NotBlank
    @Where
    private String identification;
    @ApiModelProperty(value = "描述该容器最后一次进行装载操作的时间")
    @Where
    private Date lastLoadTime;
    @ApiModelProperty(value = "描述该容器最后一次进行卸载操作的时间")
    @Where
    private Date lastUnloadTime;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;

    @ApiModelProperty(value = "当前容器ID")
    @Where
    private String currentContainerId;

    @ApiModelProperty(value = "顶层容器ID")
    @Where
    private String topContainerId;

    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 作为容器唯一标识，用于其他数据结构引用该容器
     */
    public String getContainerHisId() {
        return containerHisId;
    }

    public void setContainerHisId(String containerHisId) {
        this.containerHisId = containerHisId;
    }

    /**
     * @return 容器ID
     */
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * @return 标识该容器的唯一编码CODE，用于可视化识别
     */
    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    /**
     * @return 表示该容器所属的容器类型ID
     */
    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    /**
     * @return 该容器的名称
     */
    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    /**
     * @return 对该容器的详细描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 描述该容器所在的站点标识ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 描述该容器所在的货位标识ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 描述该容器的状态，包括：
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 描述容器下表示实物的所有权属于客户还是供应商，内容包括：C：客户，表示该容器表示的实物属于客户S：供应商，表示该容器表示的实物属于供应商空：表示容器表示的实物属于厂内自有
     */
    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * @return 配合所有者类型使用，描述容器表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID
     */
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return 指示物料批标识实物是否被保留
     */
    public String getReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(String reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    /**
     * @return 当容器被保留时，指示容器的被保留对象的类型，内容包括：WO：该容器表示的实物被某一生产指令保留EO：该容器表示的实物被某一执行作业保留DRIVING：该容器表示的实物被某一驱动指令保留CUSTOM：该容器表示的实物被某一客户保留OO：该容器表示的实物被某一机会订单预留
     */
    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    /**
     * @return 当容器被保留时，指示容器的保留对象值，配合保留对象类型使用；优先考虑容器的保留和保留对象，其次考虑容器下表示实物的保留和保留对象
     */
    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }

    /**
     * @return 标识该容器临时使用的容器编码以外的可视化标识
     */
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

    /**
     * @return 事件ID
     */
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

    /**
     * @return
     */


    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
