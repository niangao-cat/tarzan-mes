package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtBomVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8522070567617842958L;

    private String siteId; // 站点ID（生产类型）

    private String bomName; // 物料清单名称

    private String revision; // 版本

    private String bomType; // BOM类型

    private String currentFlag; // 当前版本

    private Date dateFrom; // 生效时间

    private Date dateTo; // 失效生产

    private String description; // 物料清单描述

    private String bomStatus; // 物料清单状态

    private String copiedFromBomId; // 引用的源物料清单ID

    private String releasedFlag; // 已经下达EO标识

    private Double primaryQty; // 基本数量

    private String onlyAvailableFlag;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    private String autoRevisionFlag;
    private String assembleAsMaterialFlag;

    public Date getDateFrom() {
        if (this.dateFrom == null) {
            return null;
        }
        return (Date) this.dateFrom.clone();
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (this.dateTo == null) {
            return null;
        }
        return (Date) this.dateTo.clone();
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBomStatus() {
        return bomStatus;
    }

    public void setBomStatus(String bomStatus) {
        this.bomStatus = bomStatus;
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

    public Double getPrimaryQty() {
        return primaryQty;
    }

    public void setPrimaryQty(Double primaryQty) {
        this.primaryQty = primaryQty;
    }

    public String getOnlyAvailableFlag() {
        return onlyAvailableFlag;
    }

    public void setOnlyAvailableFlag(String onlyAvailableFlag) {
        this.onlyAvailableFlag = onlyAvailableFlag;
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

}
