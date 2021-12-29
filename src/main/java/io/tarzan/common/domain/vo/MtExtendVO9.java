package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-29 10:17
 */
public class MtExtendVO9 implements Serializable {
    private static final long serialVersionUID = 8956574902572024324L;
    @ApiModelProperty(value = "主表主键ID")
    private String keyId;
    @ApiModelProperty(value = "扩展字段属性列表")
    private List<MtExtendVO5> attrs;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public List<MtExtendVO5> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendVO5> attrs) {
        this.attrs = attrs;
    }
}
