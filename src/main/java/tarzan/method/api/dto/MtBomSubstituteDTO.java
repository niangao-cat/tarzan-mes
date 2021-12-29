package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtBomSubstituteDTO implements Serializable {


    private static final long serialVersionUID = 3374049073511979045L;
    private String bomSubstituteId;// 替代项属性ID
    private String bomSubstituteGroupId;//    BOM替代组ID
    private String materialId;//  物料ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom;//    生效时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTo;//  失效时间
    private Double substituteValue;// 替代值
    private Double substituteUsage;// 替代单位用量

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomSiteAssigndsfsd [bomSubstituteId=");
        builder.append(bomSubstituteId);
        builder.append(", bomSubstituteGroupId=");
        builder.append(bomSubstituteGroupId);
        builder.append(", materialId=");
        builder.append(materialId);
        builder.append(", dateFrom=");
        builder.append(dateFrom);
        builder.append(", dateTo=");
        builder.append(dateTo);
        builder.append(", substituteValue=");
        builder.append(substituteValue);
        builder.append(", substituteUsage=");
        builder.append(substituteUsage);
        builder.append("]");
        return builder.toString();
    }
    
}
