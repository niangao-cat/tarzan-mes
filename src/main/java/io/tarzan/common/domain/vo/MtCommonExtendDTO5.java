package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/11 4:41 上午
 */
public class MtCommonExtendDTO5 implements Serializable {
    private static final long serialVersionUID = 5345701765547134802L;
    @ApiModelProperty(value = "主表名")
    private String tableName;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty("扩展属性列表")
    private List<MtCommonExtendVO7> attrPropertyList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtCommonExtendVO7> getAttrPropertyList() {
        return attrPropertyList;
    }

    public void setAttrPropertyList(List<MtCommonExtendVO7> attrPropertyList) {
        this.attrPropertyList = attrPropertyList;
    }
}
