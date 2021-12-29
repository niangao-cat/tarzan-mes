package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class MtBomDTO5 implements Serializable {
    
    private static final long serialVersionUID = -3057813809921739348L;

    @ApiModelProperty(value = "主键")
    private String bomId;
    
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank
    private String bomName;
    
    @ApiModelProperty(value = "版本", required = true)
    @NotBlank
    private String revision;
    
    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "类型", required = true)
    @NotBlank
    private String bomType;
    
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank
    private String bomStatus;
    
    @ApiModelProperty(value = "基本数量", required = true)
    @NotNull
    private Double primaryQty;
    
    @ApiModelProperty(value = "EO下达标识")
    private String releasedFlag;
    
    @ApiModelProperty(value = "当前版本")
    private String currentFlag;
    
    @ApiModelProperty(value = "生效时间从", required = true)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom;
    
    @ApiModelProperty(value = "生效时间至")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTo;
    
    @ApiModelProperty(value = "自动升版本")
    private String autoRevisionFlag;
    
    @ApiModelProperty(value = "按物料装配")
    private String assembleAsMaterialFlag;
    
    @ApiModelProperty(value = "来源装配清单主键")
    private String copiedFromBomId;
    
    @Transient
    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;
    
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

    public String getCopiedFromBomId() {
        return copiedFromBomId;
    }

    public void setCopiedFromBomId(String copiedFromBomId) {
        this.copiedFromBomId = copiedFromBomId;
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

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
