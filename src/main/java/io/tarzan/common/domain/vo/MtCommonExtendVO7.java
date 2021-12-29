package io.tarzan.common.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2020/11/11 3:39 上午
 */
public class MtCommonExtendVO7 implements Serializable {
    private static final long serialVersionUID = -8607559917095164698L;
    @ApiModelProperty(value = "主表主键ID")
    private String keyId;
    @ApiModelProperty(value = "扩展字段属性列表")
    private List<MtCommonExtendVO4> attrs;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public List<MtCommonExtendVO4> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtCommonExtendVO4> attrs) {
        this.attrs = attrs;
    }

    public MtCommonExtendVO7() {
    }

    public MtCommonExtendVO7(String keyId, List<MtCommonExtendVO4> attrs) {
        this.keyId = keyId;
        this.attrs = attrs;
    }
}
