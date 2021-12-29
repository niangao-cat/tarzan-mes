package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import tarzan.iface.domain.entity.MtBomComponentIface;
import tarzan.order.domain.entity.MtWorkOrder;

/**
 * @author Leeloing
 * @date 2019/12/5 2:38 下午
 */
public class MtBomVO11 implements Serializable {
    private static final long serialVersionUID = -2265531791882362963L;
    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("装配清单名称")
    private String bomName;
    @ApiModelProperty("版本")
    private String revision;
    @ApiModelProperty("装配清单描述")
    private String description;
    @ApiModelProperty("装配清单类型")
    private String bomType;
    @ApiModelProperty("装配清单状态")
    private String bomStatus;
    @ApiModelProperty("基本数量")
    private Double primaryQty;
    @ApiModelProperty("已经下达EO标识")
    private String releasedFlag;
    @ApiModelProperty("当前版本标识")
    private String currentFlag;
    @ApiModelProperty("生效时间")
    private Date dateFrom;
    @ApiModelProperty("失效时间")
    private Date dateTo;
    @ApiModelProperty("自动升版本标识")
    private String autoRevisionFlag;
    @ApiModelProperty("是否按物料装配")
    private String assembleAsMaterialFlag;
    @ApiModelProperty("更新方式")
    private String updateMethod;
    @ApiModelProperty("物料清单行")
    private List<MtBomComponentVO7> mtBomComponentList;
    @ApiModelProperty(value = "接口对象", hidden = true)
    @JsonIgnore
    private MtBomComponentIface bomComponentIface;
    @ApiModelProperty(value = "站点Id", hidden = true)
    @JsonIgnore
    private String siteId;
    @ApiModelProperty(value = "物料Id", hidden = true)
    @JsonIgnore
    private String materialId;
    @ApiModelProperty(value = "物料站点Id", hidden = true)
    @JsonIgnore
    private String materialSiteId;
    @ApiModelProperty(value = "对象类型", hidden = true)
    @JsonIgnore
    private String bomObjectType;
    @ApiModelProperty(value = "对象编码", hidden = true)
    @JsonIgnore
    private String bomObjectCode;
    @ApiModelProperty(value = "事件ID", hidden = true)
    @JsonIgnore
    private String eventId;
    @ApiModelProperty(value = "工单", hidden = true)
    @JsonIgnore
    private MtWorkOrder workOrder;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
    }

    public String getBomStatus() {
        return bomStatus;
    }

    public void setBomStatus(String bomStatus) {
        this.bomStatus = bomStatus;
    }

    public Double getPrimaryQty() {
        return primaryQty;
    }

    public void setPrimaryQty(Double primaryQty) {
        this.primaryQty = primaryQty;
    }

    public String getReleasedFlag() {
        return releasedFlag;
    }

    public void setReleasedFlag(String releasedFlag) {
        this.releasedFlag = releasedFlag;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Date getDateFrom() {
        if (dateFrom == null) {
            return null;
        } else {
            return (Date) dateFrom.clone();
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo == null) {
            return null;
        } else {
            return (Date) dateTo.clone();
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public String getAutoRevisionFlag() {
        return autoRevisionFlag;
    }

    public void setAutoRevisionFlag(String autoRevisionFlag) {
        this.autoRevisionFlag = autoRevisionFlag;
    }

    public String getAssembleAsMaterialFlag() {
        return assembleAsMaterialFlag;
    }

    public void setAssembleAsMaterialFlag(String assembleAsMaterialFlag) {
        this.assembleAsMaterialFlag = assembleAsMaterialFlag;
    }

    public String getUpdateMethod() {
        return updateMethod;
    }

    public void setUpdateMethod(String updateMethod) {
        this.updateMethod = updateMethod;
    }

    public MtBomComponentIface getBomComponentIface() {
        return bomComponentIface;
    }

    public void setBomComponentIface(MtBomComponentIface bomComponentIface) {
        this.bomComponentIface = bomComponentIface;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    public String getBomObjectType() {
        return bomObjectType;
    }

    public void setBomObjectType(String bomObjectType) {
        this.bomObjectType = bomObjectType;
    }

    public String getBomObjectCode() {
        return bomObjectCode;
    }

    public void setBomObjectCode(String bomObjectCode) {
        this.bomObjectCode = bomObjectCode;
    }

    public List<MtBomComponentVO7> getMtBomComponentList() {
        return mtBomComponentList;
    }

    public void setMtBomComponentList(List<MtBomComponentVO7> mtBomComponentList) {
        this.mtBomComponentList = mtBomComponentList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public MtWorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(MtWorkOrder workOrder) {
        this.workOrder = workOrder;
    }
}
