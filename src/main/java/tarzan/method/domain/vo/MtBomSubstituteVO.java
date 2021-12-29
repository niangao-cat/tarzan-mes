package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtBomSubstituteVO implements Serializable {

    private static final long serialVersionUID = 8792337465602260761L;
    private String bomId;
    private String bomComponentId;
    private String bomSubstituteGroupId;
    private String materialId;
    private Date dateFrom;
    private Date dateTo;
    private Double substituteValue;
    private Double substituteUsage;
    private String onlyAvailableFlag;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
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

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateFrom() {
        if (this.dateFrom == null) {
            return null;
        }
        return (Date) this.dateFrom.clone();
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public Date getDateTo() {
        if (this.dateTo == null) {
            return null;
        }
        return (Date) this.dateTo.clone();
    }

    public Double getSubstituteValue() {
        return substituteValue;
    }

    public void setSubstituteValue(Double substituteValue) {
        this.substituteValue = substituteValue;
    }

    public String getOnlyAvailableFlag() {
        return onlyAvailableFlag;
    }

    public void setOnlyAvailableFlag(String onlyAvailableFlag) {
        this.onlyAvailableFlag = onlyAvailableFlag;
    }

    public Double getSubstituteUsage() {
        return substituteUsage;
    }

    public void setSubstituteUsage(Double substituteUsage) {
        this.substituteUsage = substituteUsage;
    }

}
