package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class MtExtendAttrDTO3 implements Serializable {
    private static final long serialVersionUID = -4321507443018524282L;
    @ApiModelProperty(value = "属性名", required = true)
    private String attrName;
    @ApiModelProperty(value = "值", required = true)
    private String attrValue;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();

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

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
