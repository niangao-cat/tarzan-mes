package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class MtNumrangeObjectColumnDTO3 implements Serializable {

    private static final long serialVersionUID = -8160082792707296252L;
    @ApiModelProperty(value = "编码对象Id", required = true)
    private String objectId;

    @ApiModelProperty(value = "属性参数Id", required = false)
    private String objectColumnId;

    @ApiModelProperty(value = "属性参数编码", required = true)
    private String objectColumnCode;

    @ApiModelProperty(value = "属性参数含义", required = true)
    private String objectColumnName;

    @ApiModelProperty(value = "启用状态", required = true)
    private String enableFlag;

    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;

    @ApiModelProperty(value = "类型组")
    private String typeGroup;

    @ApiModelProperty(value = "服务包")
    private String module;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectColumnId() {
        return objectColumnId;
    }

    public void setObjectColumnId(String objectColumnId) {
        this.objectColumnId = objectColumnId;
    }

    public String getObjectColumnCode() {
        return objectColumnCode;
    }

    public void setObjectColumnCode(String objectColumnCode) {
        this.objectColumnCode = objectColumnCode;
    }

    public String getObjectColumnName() {
        return objectColumnName;
    }

    public void setObjectColumnName(String objectColumnName) {
        this.objectColumnName = objectColumnName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
