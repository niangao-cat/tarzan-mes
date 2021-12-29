package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class MtGenStatusDTO2 implements Serializable {

    private static final long serialVersionUID = 5078220912057790755L;

    @ApiModelProperty("通用状态主键")
    private String genStatusId;

    @ApiModelProperty(value = "状态组编码", required = true)
    @NotBlank
    private String statusGroup;

    @ApiModelProperty(value = "状态编码", required = true)
    @NotBlank
    private String statusCode;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "服务包", required = true)
    @NotBlank
    private String module;

    @ApiModelProperty(value = "顺序", required = true)
    @NotNull
    private Double sequence;

    @ApiModelProperty(value = "默认状态，Y/N", required = true)
    @NotBlank
    private String defaultFlag;

    @ApiModelProperty(value = "初始数据标识", required = true)
    @NotBlank
    private String initialFlag;

    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;

    public String getGenStatusId() {
        return genStatusId;
    }

    public void setGenStatusId(String genStatusId) {
        this.genStatusId = genStatusId;
    }

    public String getStatusGroup() {
        return statusGroup;
    }

    public void setStatusGroup(String statusGroup) {
        this.statusGroup = statusGroup;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getInitialFlag() {
        return initialFlag;
    }

    public void setInitialFlag(String initialFlag) {
        this.initialFlag = initialFlag;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

}
