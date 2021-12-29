package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/11 3:53 上午
 */
public class MtCommonExtendAttrHisVO6 implements Serializable {
    private static final long serialVersionUID = 4858435188618679304L;

    @ApiModelProperty("主表的历史表主键ID")
    private String hisKeyId;
    @ApiModelProperty("扩展表主键ID")
    private String attrId;
    @ApiModelProperty("扩展表的历史表主键ID")
    private String attrHisId;
    @ApiModelProperty("扩展属性名称")
    private String attrName;
    @ApiModelProperty("扩展属性值")
    private String attrValue;
    @ApiModelProperty("事件Id")
    private String eventId;
    @ApiModelProperty("cid")
    private String cid;

    public String getHisKeyId() {
        return hisKeyId;
    }

    public void setHisKeyId(String hisKeyId) {
        this.hisKeyId = hisKeyId;
    }

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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAttrHisId() {
        return attrHisId;
    }

    public void setAttrHisId(String attrHisId) {
        this.attrHisId = attrHisId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
}
