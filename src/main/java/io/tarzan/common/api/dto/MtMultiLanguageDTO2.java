package io.tarzan.common.api.dto;

import java.io.Serializable;

public class MtMultiLanguageDTO2 implements Serializable {
    private static final long serialVersionUID = 3375884891030164281L;
    private String code;
    private String name;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
