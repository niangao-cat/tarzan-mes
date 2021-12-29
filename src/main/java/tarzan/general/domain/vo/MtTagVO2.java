package tarzan.general.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtTagVO2
 * @description
 * @date 2019年09月18日 15:17
 */
public class MtTagVO2 implements Serializable {
    private static final long serialVersionUID = 1032459465424876292L;

    @ApiModelProperty(value = "数据项编码", required = true)
    private String tagCode;
    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否启用", required = true)
    private String enableFlag;
    @ApiModelProperty(value = "数据收集方式")
    private String collectionMethod;
    @ApiModelProperty(value = "数据类型", required = true)
    private String valueType;
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
    @ApiModelProperty(value = "允许缺失值")
    private String valueAllowMissing;
    @ApiModelProperty(value = "必需的数据条数")
    private Long mandatoryNum;
    @ApiModelProperty(value = "可选的数据条数")
    private Long optionalNum;
    @ApiModelProperty(value = "转化API_ID")
    private String apiId;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getCollectionMethod() {
        return collectionMethod;
    }

    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
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

    public String getValueAllowMissing() {
        return valueAllowMissing;
    }

    public void setValueAllowMissing(String valueAllowMissing) {
        this.valueAllowMissing = valueAllowMissing;
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

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}
