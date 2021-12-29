package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtExtendVO9;

/**
 * @Description:
 * @Date: 2019/12/31 17:47
 * @Author: ${yiyang.xie}
 */
public class MtExtendDTO6 implements Serializable {
    private static final long serialVersionUID = -7374148936566828443L;

    @ApiModelProperty(value = "主表名")
    private String tableName;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty("扩展属性列表")
    private List<MtExtendVO9> attrPropertyList;

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

    public List<MtExtendVO9> getAttrPropertyList() {
        return attrPropertyList;
    }

    public void setAttrPropertyList(List<MtExtendVO9> attrPropertyList) {
        this.attrPropertyList = attrPropertyList;
    }
}