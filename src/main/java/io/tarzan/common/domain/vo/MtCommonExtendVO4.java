package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/11 3:38 上午
 */
public class MtCommonExtendVO4 implements Serializable {
    private static final long serialVersionUID = 1675096641173723771L;
    @ApiModelProperty(value = "扩展属性")
    private String attrName;
    @ApiModelProperty(value = "扩展属性值")
    private String attrValue;

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

    public MtCommonExtendVO4() {}

    public MtCommonExtendVO4(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtCommonExtendVO4 that = (MtCommonExtendVO4) o;
        return Objects.equals(getAttrName(), that.getAttrName()) && Objects.equals(getAttrValue(), that.getAttrValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttrName(), getAttrValue());
    }
}
