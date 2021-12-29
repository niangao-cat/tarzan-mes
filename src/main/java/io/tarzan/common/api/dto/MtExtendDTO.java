package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtExtendVO5;

/**
 * @Author: chuang.yang
 * @Date: 2019/6/14 15:18
 * @Description:
 */
public class MtExtendDTO implements Serializable {
    private static final long serialVersionUID = -6632323678747492011L;

    @ApiModelProperty(value = "主表名")
    private String tableName;
    @ApiModelProperty(value = "主表主键ID")
    private String keyId;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "扩展属性集合")
    private List<MtExtendVO5> attrs;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtExtendVO5> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendVO5> attrs) {
        this.attrs = attrs;
    }
}
