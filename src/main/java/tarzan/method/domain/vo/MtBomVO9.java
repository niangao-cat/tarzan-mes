package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtBomVO9 implements Serializable {

    private static final long serialVersionUID = 8226963516907020110L;
    
    @ApiModelProperty(value = "主键")
    private String bomId;
    
    @ApiModelProperty(value = "名称")
    private String bomName;
    
    @ApiModelProperty(value = "版本")
    private String revision;
    
    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "类型")
    private String bomType;
    
    @ApiModelProperty(value = "类型描述")
    private String bomTypeDesc;
    
    @ApiModelProperty(value = "状态")
    private String bomStatus;
    
    @ApiModelProperty(value = "状态描述")
    private String bomStatusDesc;
    
    @ApiModelProperty(value = "基本数量")
    private Double primaryQty;
    
    @ApiModelProperty(value = "EO下达标识")
    private String releasedFlag;
    
    @ApiModelProperty(value = "当前版本")
    private String currentFlag;
    
    @ApiModelProperty(value = "生效时间从")
    private Date dateFrom;
    
    @ApiModelProperty(value = "生效时间至")
    private Date dateTo;
    
    @ApiModelProperty(value = "自动升版本")
    private String autoRevisionFlag;
    
    @ApiModelProperty(value = "按物料装配")
    private String assembleAsMaterialFlag;
    
    @ApiModelProperty(value = "来源装配清单主键")
    private String copiedFromBomId;
    
    @ApiModelProperty(value = "来源装配清单名称")
    private String copiedFromBomName;
    
    @ApiModelProperty(value = "来源装配清单版本")
    private String copiedFromBomRevision;
    
    @ApiModelProperty(value = "来源装配清单类型")
    private String copiedFromBomType;
    
    @ApiModelProperty(value = "组件行")
    private List<MtBomComponentVO14> componentList;
    

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

    public String getBomTypeDesc() {
        return bomTypeDesc;
    }

    public void setBomTypeDesc(String bomTypeDesc) {
        this.bomTypeDesc = bomTypeDesc;
    }

    public String getBomStatus() {
        return bomStatus;
    }

    public void setBomStatus(String bomStatus) {
        this.bomStatus = bomStatus;
    }

    public String getBomStatusDesc() {
        return bomStatusDesc;
    }

    public void setBomStatusDesc(String bomStatusDesc) {
        this.bomStatusDesc = bomStatusDesc;
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
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
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
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
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

    public String getCopiedFromBomId() {
        return copiedFromBomId;
    }

    public void setCopiedFromBomId(String copiedFromBomId) {
        this.copiedFromBomId = copiedFromBomId;
    }

    public String getCopiedFromBomName() {
        return copiedFromBomName;
    }

    public void setCopiedFromBomName(String copiedFromBomName) {
        this.copiedFromBomName = copiedFromBomName;
    }

    public String getCopiedFromBomRevision() {
        return copiedFromBomRevision;
    }

    public void setCopiedFromBomRevision(String copiedFromBomRevision) {
        this.copiedFromBomRevision = copiedFromBomRevision;
    }

    public String getCopiedFromBomType() {
        return copiedFromBomType;
    }

    public void setCopiedFromBomType(String copiedFromBomType) {
        this.copiedFromBomType = copiedFromBomType;
    }

    public List<MtBomComponentVO14> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<MtBomComponentVO14> componentList) {
        this.componentList = componentList;
    }



}
