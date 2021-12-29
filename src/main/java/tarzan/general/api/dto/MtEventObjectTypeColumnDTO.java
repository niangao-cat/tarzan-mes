package tarzan.general.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

/**
 * 对象类型维护-对象展示预览 前端使用DTO
 * 
 * @author benjamin
 */
public class MtEventObjectTypeColumnDTO implements Serializable {
    private static final long serialVersionUID = -1334927899946088251L;


    @ApiModelProperty(value = "对象类型ID", required = true)
    @NotEmpty
    private String objectTypeId;

    @ApiModelProperty(value = "对象查询语句")
    private String eventTypeQuerySql;

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getEventTypeQuerySql() {
        return eventTypeQuerySql;
    }

    public void setEventTypeQuerySql(String eventTypeQuerySql) {
        this.eventTypeQuerySql = eventTypeQuerySql;
    }
}
