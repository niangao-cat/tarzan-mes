package tarzan.general.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtTagGroupAssignDTO implements Serializable {
    private static final long serialVersionUID = 4898726728049715708L;

    @ApiModelProperty("数据收集项分配收集组ID")
    private String tagGroupAssignId;
    @ApiModelProperty(value = "序号")
    @NotNull
    private Double serialNumber;
    @ApiModelProperty(value = "数据项ID")
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;
    @ApiModelProperty(value = "数据类型")
    private String valueType;
    @ApiModelProperty(value = "数据收集方式")
    private String collectionMethod;
    @ApiModelProperty(value = "允许缺失值")
    private String valueAllowMissing;
    @ApiModelProperty(value = "符合值")
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    private String falseValue;
    @ApiModelProperty(value = "最小值")
    private Double minimumValue;
    @ApiModelProperty(value = "最大值")
    private Double maximalValue;
    @ApiModelProperty(value = "计量单位Id")
    private String unit;
    @ApiModelProperty(value = "计量单位编码")
    private String uomCode;
    @ApiModelProperty(value = "计量单位描述")
    private String uomDesc;
    @ApiModelProperty(value = "必需的数据条数")
    private Long mandatoryNum;
    @ApiModelProperty(value = "可选的数据条数")
    private Long optionalNum;
    @ApiModelProperty(value = "是否启用")
    private String enableFlag;


    public String getTagGroupAssignId() {
        return tagGroupAssignId;
    }

    public void setTagGroupAssignId(String tagGroupAssignId) {
        this.tagGroupAssignId = tagGroupAssignId;
    }

    public Double getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Double serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getValueAllowMissing() {
        return valueAllowMissing;
    }

    public void setValueAllowMissing(String valueAllowMissing) {
        this.valueAllowMissing = valueAllowMissing;
    }

    public String getTrueValue() {
        return trueValue;
    }

    public void setTrueValue(String trueValue) {
        this.trueValue = trueValue;
    }

    public String getFalseValue() {
        return falseValue;
    }

    public void setFalseValue(String falseValue) {
        this.falseValue = falseValue;
    }

    public Double getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(Double minimumValue) {
        this.minimumValue = minimumValue;
    }

    public Double getMaximalValue() {
        return maximalValue;
    }

    public void setMaximalValue(Double maximalValue) {
        this.maximalValue = maximalValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public Long getMandatoryNum() {
        return mandatoryNum;
    }

    public void setMandatoryNum(Long mandatoryNum) {
        this.mandatoryNum = mandatoryNum;
    }

    public Long getOptionalNum() {
        return optionalNum;
    }

    public void setOptionalNum(Long optionalNum) {
        this.optionalNum = optionalNum;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
