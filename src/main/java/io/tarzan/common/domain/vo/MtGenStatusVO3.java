package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenStatusVO3 implements Serializable {
    
    private static final long serialVersionUID = 8614379235625299526L;

    @ApiModelProperty("通用状态主键")
    private String genStatusId;
    
    @ApiModelProperty(value = "服务包")
    private String module;
    
    @ApiModelProperty(value = "服务包（描述）")
    private String moduleDesc;
    
    @ApiModelProperty(value = "状态组编码")
    private String statusGroup;
    
    @ApiModelProperty(value = "状态编码")
    private String statusCode;
    
    @ApiModelProperty(value = "备注")
    private String description;
    
    @ApiModelProperty(value = "默认状态，Y/N")
    private String defaultFlag;
    
    @ApiModelProperty(value = "关联对象")
    private String relationTable;
    
    @ApiModelProperty(value = "初始数据标识")
    private String initialFlag;
    
    @ApiModelProperty(value = "顺序")
    private Double sequence;
    
    public String getGenStatusId() {
        return genStatusId;
    }

    public void setGenStatusId(String genStatusId) {
        this.genStatusId = genStatusId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
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

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getRelationTable() {
        return relationTable;
    }

    public void setRelationTable(String relationTable) {
        this.relationTable = relationTable;
    }

    public String getInitialFlag() {
        return initialFlag;
    }

    public void setInitialFlag(String initialFlag) {
        this.initialFlag = initialFlag;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }
    
}
