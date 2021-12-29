package io.tarzan.common.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2020/9/28 11:00
 */
public class MtCommonExtendVO5 implements Serializable {

	private static final long serialVersionUID = -7479956733866222958L;
	@ApiModelProperty(value = "扩展属性")
	private String attrName;
	@ApiModelProperty(value = "扩展属性值")
	private String attrValue;
	@ApiModelProperty(value = "多语言标志", hidden = true)
	@JsonIgnore
	private String tlFlag;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getTlFlag() {
		return tlFlag;
	}

	public void setTlFlag(String tlFlag) {
		this.tlFlag = tlFlag;
	}
}
