package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

public class MtBomComponentVO14 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3662889128933414450L;

    @ApiModelProperty(value = "行主键")
    private String bomComponentId;

    @ApiModelProperty(value = "排序号")
    private Long lineNumber;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(hidden = true)
    private String materialId;

    @ApiModelProperty(value = "组件编码")
    private String materialCode;

    @ApiModelProperty(value = "组件名称")
    private String materialName;

    @ApiModelProperty(value = "组件单位")
    private String uomName;

    @ApiModelProperty(value = "生效时间")
    private Date dateFrom;

    @ApiModelProperty(value = "生效时间")
    private Date dateTo;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(hidden = true)
    private String bomComponentType;

    @ApiModelProperty(value = "组件类型")
    private String bomComponentTypeDesc;

    @ApiModelProperty(value = "数量")
    private Double qty;

    @ApiModelProperty(value = "组件版本/卸货点")
    private String bomVersion;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
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

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public String getBomComponentTypeDesc() {
        return bomComponentTypeDesc;
    }

    public void setBomComponentTypeDesc(String bomComponentTypeDesc) {
        this.bomComponentTypeDesc = bomComponentTypeDesc;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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

    public String getBomVersion() {
        return bomVersion;
    }

    public void setBomVersion(String bomVersion) {
        this.bomVersion = bomVersion;
    }
}
