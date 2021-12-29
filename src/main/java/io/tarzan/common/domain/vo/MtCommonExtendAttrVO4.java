package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/11 3:52 上午
 */
public class MtCommonExtendAttrVO4 implements Serializable {
    private static final long serialVersionUID = 6743014675936274009L;

    @ApiModelProperty("主表主键Id")
    private String keyId;
    @ApiModelProperty("扩展表主键Id")
    private String attrId;
    @ApiModelProperty("扩展字段值")
    private String attrValue;
    @ApiModelProperty("扩展字段名")
    private String attrName;
    @ApiModelProperty("cid")
    private String cid;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
