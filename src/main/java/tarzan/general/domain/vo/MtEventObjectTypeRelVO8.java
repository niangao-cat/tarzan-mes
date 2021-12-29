package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEventObjectTypeRelVO8 implements Serializable {
    private static final long serialVersionUID = -6063286129284155519L;

    private String eventId;
    private String eventTypeCode;
    private String objectTypeCode;
    private String objectDescription;
    private List<MtEventObjectColumnValueVO> columnValueList;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }

    public List<MtEventObjectColumnValueVO> getColumnValueList() {
        return columnValueList;
    }

    public void setColumnValueList(List<MtEventObjectColumnValueVO> columnValueList) {
        this.columnValueList = columnValueList;
    }
}
