package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtBomDTO4 implements Serializable {


    private static final long serialVersionUID = -7250651869260080067L;
    private String bomId; // 装配清单ID
    private String siteId; // 站点ID
    private String bomName; // 装配清单名称
    private String revision; // 版本
    private String bomType; // 装配清单类型
    private String currentFlag; // 当前版本标识
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom; // 生效时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTo; // 失效时间
    private String description; // 装配清单描述
    private String bomStatus; // 装配清单状态
    private String releasedFlag; // 已经下达EO标识
    private Double primaryQty; // 基本数量
    private String autoRevisionFlag; // 自动升版本标识
    private String assembleAsMaterialFlag; // 按物料装配标识


    public String getBomId() {
        return bomId;
    }


    public void setBomId(String bomId) {
        this.bomId = bomId;
    }


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


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomDTO4 [bomId=");
        builder.append(bomId);
        builder.append(", siteId=");
        builder.append(siteId);
        builder.append(", bomName=");
        builder.append(bomName);
        builder.append(", revision=");
        builder.append(revision);
        builder.append(", bomType=");
        builder.append(bomType);
        builder.append(", currentFlag=");
        builder.append(currentFlag);
        builder.append(", dateFrom=");
        builder.append(dateFrom);
        builder.append(", dateTo=");
        builder.append(dateTo);
        builder.append(", description=");
        builder.append(description);
        builder.append(", bomStatus=");
        builder.append(bomStatus);
        builder.append(", releasedFlag=");
        builder.append(releasedFlag);
        builder.append(", primaryQty=");
        builder.append(primaryQty);
        builder.append(", autoRevisionFlag=");
        builder.append(autoRevisionFlag);
        builder.append(", assembleAsMaterialFlag=");
        builder.append(assembleAsMaterialFlag);
        builder.append("]");
        return builder.toString();
    }

}
