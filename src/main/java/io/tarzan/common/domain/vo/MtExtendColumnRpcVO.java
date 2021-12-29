package io.tarzan.common.domain.vo;

import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import javax.persistence.Transient;

/**
 * @author Leeloing
 * @date 2020/9/28 10:28
 */
public class MtExtendColumnRpcVO implements Serializable {

	private static final long   serialVersionUID = -2867714799005737765L;
	@ApiModelProperty("拓展字段主键")
	private              String extendId;
	@ApiModelProperty(value = "拓展表描述主键", required = true)
	private              String extendTableDescId;
	@ApiModelProperty(value = "拓展字段", required = true)
	private              String attrName;
	@ApiModelProperty(value = "拓展字段描述", required = true)
	@MultiLanguageField
	private              String attrMeaning;
	@ApiModelProperty(value = "多语言标识", required = true)
	private              String tlFlag;
	@ApiModelProperty(value = "有效性", required = true)
	private              String enableFlag;
	@ApiModelProperty(value = "顺序", required = true)
	private              Long   sequence;
	@Transient
	@ApiModelProperty(hidden = true)
	private transient    String lang;

	public String getExtendId() {
		return extendId;
	}

	public void setExtendId(String extendId) {
		this.extendId = extendId;
	}

	public String getExtendTableDescId() {
		return extendTableDescId;
	}

	public void setExtendTableDescId(String extendTableDescId) {
		this.extendTableDescId = extendTableDescId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrMeaning() {
		return attrMeaning;
	}

	public void setAttrMeaning(String attrMeaning) {
		this.attrMeaning = attrMeaning;
	}

	public String getTlFlag() {
		return tlFlag;
	}

	public void setTlFlag(String tlFlag) {
		this.tlFlag = tlFlag;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
