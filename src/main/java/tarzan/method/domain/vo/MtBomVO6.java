package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MtBomVO6 implements Serializable {
    private static final long serialVersionUID = 1975596710592573049L;

    private String bomId;
    private String bomName;
    private String revision;
    private String description;
    private String bomType;
    private String bomStatus;
    private Double primaryQty;
    private String copiedFromBomId;
    private String releasedFlag;
    private String currentFlag;
    private Date dateFrom;
    private Date dateTo;
    private String autoRevisionFlag;
    private String assembleAsMaterialFlag;

    private List<MtBomComponentVO7> mtBomComponentList;

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

    public List<MtBomComponentVO7> getMtBomComponentList() {
        return mtBomComponentList;
    }

    public void setMtBomComponentList(List<MtBomComponentVO7> mtBomComponentList) {
        this.mtBomComponentList = mtBomComponentList;
    }
}
