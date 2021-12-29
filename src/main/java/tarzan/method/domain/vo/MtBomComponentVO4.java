package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class MtBomComponentVO4 implements Serializable {

    private static final long serialVersionUID = -2999442128876756815L;
    
    @ApiModelProperty(value = "主键")
    private String bomComponentId;
    
    @ApiModelProperty(value = "头表主键", required = true)
    @NotBlank
    private String bomId;
    
    @ApiModelProperty(value = "序号", required = true)
    @NotNull
    private Long lineNumber;
    
    @ApiModelProperty(value = "组件ID", required = true)
    @NotBlank
    private String materialId;
    
    @ApiModelProperty(value = "组件编码")
    private String materialCode;
    
    @ApiModelProperty(value = "组件名称")
    private String materialName;
    
    @ApiModelProperty(value = "组件单位")
    private String uomName;
    
    @ApiModelProperty(value = "组件类型", required = true)
    @NotBlank
    private String bomComponentType;
    
    @ApiModelProperty(value = "组件类型描述")
    private String bomComponentTypeDesc;
    
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    private Date dateFrom;
    
    @ApiModelProperty(value = "失效时间")
    private Date dateTo;
    
    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    private Double qty;
    
    @ApiModelProperty(value = "关键主键标识")
    private String keyMaterialFlag;
    
    @ApiModelProperty(value = "装配方式")
    private String assembleMethod;
    
    @ApiModelProperty(value = "装配方式描述")
    private String assembleMethodDesc;
    
    @ApiModelProperty(value = "按需求数量装配")
    private String assembleAsReqFlag;
    
    @ApiModelProperty(value = "损耗策略")
    private String attritionPolicy;
    
    @ApiModelProperty(value = "损耗百分比")
    private Double attritionChance;
    
    @ApiModelProperty(value = "固定损耗值")
    private Double attritionQty;
    
    @ApiModelProperty(value = "发料库位ID")
    private String issuedLocatorId;
    
    @ApiModelProperty(value = "发料库位编码")
    private String issuedLocatorCode;
    
    @ApiModelProperty(value = "发料库位描述")
    private String issuedLocatorName;
    
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
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

    public String getKeyMaterialFlag() {
        return keyMaterialFlag;
    }

    public void setKeyMaterialFlag(String keyMaterialFlag) {
        this.keyMaterialFlag = keyMaterialFlag;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }

    public String getAssembleMethodDesc() {
        return assembleMethodDesc;
    }

    public void setAssembleMethodDesc(String assembleMethodDesc) {
        this.assembleMethodDesc = assembleMethodDesc;
    }

    public String getAssembleAsReqFlag() {
        return assembleAsReqFlag;
    }

    public void setAssembleAsReqFlag(String assembleAsReqFlag) {
        this.assembleAsReqFlag = assembleAsReqFlag;
    }

    public String getAttritionPolicy() {
        return attritionPolicy;
    }

    public void setAttritionPolicy(String attritionPolicy) {
        this.attritionPolicy = attritionPolicy;
    }

    public Double getAttritionChance() {
        return attritionChance;
    }

    public void setAttritionChance(Double attritionChance) {
        this.attritionChance = attritionChance;
    }

    public Double getAttritionQty() {
        return attritionQty;
    }

    public void setAttritionQty(Double attritionQty) {
        this.attritionQty = attritionQty;
    }

    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    public String getIssuedLocatorCode() {
        return issuedLocatorCode;
    }

    public void setIssuedLocatorCode(String issuedLocatorCode) {
        this.issuedLocatorCode = issuedLocatorCode;
    }

    public String getIssuedLocatorName() {
        return issuedLocatorName;
    }

    public void setIssuedLocatorName(String issuedLocatorName) {
        this.issuedLocatorName = issuedLocatorName;
    }

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

}
