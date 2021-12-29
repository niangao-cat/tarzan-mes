package tarzan.general.domain.vo;

import java.io.Serializable;

public class MtEventObjectColumnVO implements Serializable {

    private static final long serialVersionUID = 8400145536115637549L;
    private String objectTypeId;
    private String columnField;
    private String columnType;
    private String columnTitle;
    private Long lineNumber;
    private String kidFlag; // 是否对象主键字段
    private String eventFlag;
    private String displayFlag;
    private String enableFlag;

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getColumnField() {
        return columnField;
    }

    public void setColumnField(String columnField) {
        this.columnField = columnField;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getEventFlag() {
        return eventFlag;
    }

    public void setEventFlag(String eventFlag) {
        this.eventFlag = eventFlag;
    }

    public String getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(String displayFlag) {
        this.displayFlag = displayFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getKidFlag() {
        return kidFlag;
    }

    public void setKidFlag(String kidFlag) {
        this.kidFlag = kidFlag;
    }

}
