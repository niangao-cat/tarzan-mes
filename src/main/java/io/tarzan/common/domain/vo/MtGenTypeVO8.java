package io.tarzan.common.domain.vo;

import java.io.Serializable;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/17 10:05
 */
public class MtGenTypeVO8 implements Serializable {

    private static final long serialVersionUID = -7151729637949919156L;
    private String relationTable;// 关联表
    private Double sequence;// 顺序
    private String typeGroup;// 类型组编码
    private String defaultFlag;// 默认标识
    private String genTypeId;// 通用类型ID
    private String module;// 服务包
    private String description;// 类型描述
    private String initialFlag;// 初始数据标识
    private String typeCode;// 类型组编码

    public String getRelationTable() {
        return relationTable;
    }

    public void setRelationTable(String relationTable) {
        this.relationTable = relationTable;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getGenTypeId() {
        return genTypeId;
    }

    public void setGenTypeId(String genTypeId) {
        this.genTypeId = genTypeId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInitialFlag() {
        return initialFlag;
    }

    public void setInitialFlag(String initialFlag) {
        this.initialFlag = initialFlag;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
