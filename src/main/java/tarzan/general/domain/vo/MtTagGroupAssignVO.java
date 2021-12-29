package tarzan.general.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtTagGroupAssignVO implements Serializable {
    private static final long serialVersionUID = 6031452689145229011L;

    @ApiModelProperty(value = "数据收集组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "序号")
    private Double serialNumber;
    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
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
    @ApiModelProperty(value = "计量单位")
    private String unit;
    @ApiModelProperty(value = "必需的数据条数")
    private Long mandatoryNum;
    @ApiModelProperty(value = "可选的数据条数")
    private Long optionalNum;

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public Double getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Double serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
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
}
