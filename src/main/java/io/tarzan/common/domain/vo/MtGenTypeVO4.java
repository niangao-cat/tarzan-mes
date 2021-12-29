package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtGenTypeVO4 implements Serializable {
    
    private static final long serialVersionUID = 3280869389052536053L;
    
    @ApiModelProperty("通用类型主键")
    private String genTypeId;
    
    @ApiModelProperty(value = "类型组")
    private String typeGroup;
    
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    
    @ApiModelProperty(value = "备注")
    private String description;
    
    @ApiModelProperty(value =  "服务包")
    private String module;
    
    @ApiModelProperty(value =  "服务包（描述）")
    private String moduleDesc;
    
    @ApiModelProperty(value = "默认状态，Y/N")
    private String defaultFlag;
    
    @ApiModelProperty(value = "关联对象")
    private String relationTable;
    
    @ApiModelProperty(value = "初始数据标识")
    private String initialFlag;
    
    @ApiModelProperty(value = "顺序")
    private Double sequence;
    
    public String getGenTypeId() {
        return genTypeId;
    }

    public void setGenTypeId(String genTypeId) {
        this.genTypeId = genTypeId;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
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

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
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
