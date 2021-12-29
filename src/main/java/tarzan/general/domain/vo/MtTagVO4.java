package tarzan.general.domain.vo;

import java.io.Serializable;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/17 11:19
 */
public class MtTagVO4 implements Serializable {

	private static final long serialVersionUID = -8454901307340985153L;
	private String collectionMethod;// 收集方法
	private String tagCode;// 数据项编码
	private String tagId;// 数据项ID
	private String valueType;// 数据类型
	private String remark;// 备注
	private String valueAllowMissing;// 允许缺失值
	private String enableFlag;// 是否有效
	private String apiId;// 转换API
	private String tagDescription;// 数据项描述
	private Double maximalValue;//最大值
	private String trueValue;//符合值
	private Long mandatoryNum;//必需的条目数
	private Double minimumValue;//最小值
	private String falseValue;//不符合值
	private String unit;//计量单位
	private Long optionalNum;//可选的条目数
	private String uomCode;//计量单位编码
	private String uomName;//计量单位名称

	public String getCollectionMethod() {
		return collectionMethod;
	}

	public void setCollectionMethod(String collectionMethod) {
		this.collectionMethod =collectionMethod;
	}

	public String getTagCode() {
		return tagCode;
	}

	public void setTagCode(String tagCode) {
		this.tagCode =tagCode;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId =tagId;
	}

	public Double getMaximalValue() {
		return maximalValue;
	}

	public void setMaximalValue(Double maximalValue) {
		this.maximalValue =maximalValue;
	}

	public String getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(String trueValue) {
		this.trueValue =trueValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark =remark;
	}

	public Long getMandatoryNum() {
		return mandatoryNum;
	}

	public void setMandatoryNum(Long mandatoryNum) {
		this.mandatoryNum =mandatoryNum;
	}

	public Double getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Double minimumValue) {
		this.minimumValue =minimumValue;
	}

	public String getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(String falseValue) {
		this.falseValue =falseValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit =unit;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType =valueType;
	}

	public String getValueAllowMissing() {
		return valueAllowMissing;
	}

	public void setValueAllowMissing(String valueAllowMissing) {
		this.valueAllowMissing =valueAllowMissing;
	}

	public Long getOptionalNum() {
		return optionalNum;
	}

	public void setOptionalNum(Long optionalNum) {
		this.optionalNum =optionalNum;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag =enableFlag;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId =apiId;
	}

	public String getTagDescription() {
		return tagDescription;
	}

	public void setTagDescription(String tagDescription) {
		this.tagDescription =tagDescription;
	}

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}
}