package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class MtBomSubstituteVO9 implements Serializable {

    private static final long serialVersionUID = 7922543797837984335L;
    
    @ApiModelProperty(value = "主键")
    private String bomSubstituteId;
    
    @ApiModelProperty(value = "替代组主键",required = true)
    @NotBlank
    private String bomSubstituteGroupId;
    
    @ApiModelProperty(value = "替代物料ID",required = true)
    @NotBlank
    private String materialId;
    
    @ApiModelProperty(value = "替代物料编码")
    private String materialCode;
    
    @ApiModelProperty(value = "替代物料描述")
    private String materialName;
    
    @ApiModelProperty(value = "替代值",required = true)
    @NotNull
    private Double substituteValue;
    
    @ApiModelProperty(value = "替代用量",required = true)
    @NotNull
    private Double substituteUsage;
    
    @ApiModelProperty(value = "生效时间",required = true)
    @NotNull
    private Date dateFrom;
    
    @ApiModelProperty(value = "失效时间")
    private Date dateTo;

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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Double getSubstituteValue() {
        return substituteValue;
    }

    public void setSubstituteValue(Double substituteValue) {
        this.substituteValue = substituteValue;
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

    public Double getSubstituteUsage() {
        return substituteUsage;
    }

    public void setSubstituteUsage(Double substituteUsage) {
        this.substituteUsage = substituteUsage;
    }
    

}
