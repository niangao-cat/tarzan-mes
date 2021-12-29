package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象类型维护-对象列 前端使用DTO
 * 
 * @author benjamin
 */
public class MtEventObjectColumnDTO implements Serializable {
    private static final long serialVersionUID = 5222782787375655208L;

    @ApiModelProperty("对象列ID")
    private String objectColumnId;

    @ApiModelProperty(value = "关联对象ID(数据修改时必输)")
    @NotBlank
    private String objectTypeId;

    @ApiModelProperty(value = "展示字段(数据修改时必输)")
    @NotBlank
    private String columnField;

    @ApiModelProperty(value = "字段类型(数据修改时必输)")
    @NotBlank
    private String columnType;

    @ApiModelProperty(value = "字段列标题")
    @MultiLanguageField
    private String columnTitle;

    @ApiModelProperty(value = "行号(数据修改时必输)")
    @NotNull
    private Long lineNumber;

    @ApiModelProperty(value = "是否事件字段(数据修改时必输)")
    @NotBlank
    private String eventFlag;

    @ApiModelProperty(value = "是否展示(数据修改时必输)")
    @NotBlank
    private String displayFlag;

    @ApiModelProperty(value = "是否有效(数据修改时必输)")
    @NotBlank
    private String enableFlag;

    private Map<String, Map<String, String>> _tls;

    public String getObjectColumnId() {
        return objectColumnId;
    }

    public void setObjectColumnId(String objectColumnId) {
        this.objectColumnId = objectColumnId;
    }

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

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
