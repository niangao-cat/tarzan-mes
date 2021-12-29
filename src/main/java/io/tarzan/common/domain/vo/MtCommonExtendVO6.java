package io.tarzan.common.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2020/9/28 11:01
 */
public class MtCommonExtendVO6 implements Serializable {
	private static final long                    serialVersionUID = 5152052959935219400L;
	@ApiModelProperty(value = "主表主键ID")
	private              String                  keyId;
	@ApiModelProperty(value = "扩展字段属性列表")
	private              List<MtCommonExtendVO5> attrs;

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public List<MtCommonExtendVO5> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<MtCommonExtendVO5> attrs) {
		this.attrs = attrs;
	}
}
