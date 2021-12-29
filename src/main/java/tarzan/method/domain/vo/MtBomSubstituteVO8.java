package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtBomSubstituteVO8 implements Serializable {

    private static final long serialVersionUID = 4429104617080831198L;
    private String bomSubstituteId;
    private String bomSubstituteGroupId;
    private String materialId;
    private Double substituteValue;
    private Double substituteUsage;
    private Date dateFrom;
    private Date dateTo;
    private String copiedFromSubstituteId;

    public String getBomSubstituteId() {
        return bomSubstituteId;
    }

    public void setBomSubstituteId(String bomSubstituteId) {
        this.bomSubstituteId = bomSubstituteId;
    }

    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getSubstituteValue() {
        return substituteValue;
    }

    public void setSubstituteValue(Double substituteValue) {
        this.substituteValue = substituteValue;
    }

    public Double getSubstituteUsage() {
        return substituteUsage;
    }

    public void setSubstituteUsage(Double substituteUsage) {
        this.substituteUsage = substituteUsage;
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

    public String getCopiedFromSubstituteId() {
        return copiedFromSubstituteId;
    }

    public void setCopiedFromSubstituteId(String copiedFromSubstituteId) {
        this.copiedFromSubstituteId = copiedFromSubstituteId;
    }
}
