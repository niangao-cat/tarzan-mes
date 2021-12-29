package io.tarzan.common.domain.vo;

import java.io.Serializable;

public class MtGenTypeVO implements Serializable {
    private static final long serialVersionUID = -6899034392091651681L;

    private String module;
    private String typeCode;
    private String description;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

}
