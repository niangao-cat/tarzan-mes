package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.List;

public class MtExtendAttrHisDTO implements Serializable {
    private static final long serialVersionUID = 440925496974321839L;
    private String tableName;
    private List<String> eventIds;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<String> eventIds) {
        this.eventIds = eventIds;
    }
}
