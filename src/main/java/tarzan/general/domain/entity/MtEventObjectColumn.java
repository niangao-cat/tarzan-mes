package tarzan.general.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象列定义
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@ApiModel("对象列定义")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_event_object_column")
@CustomPrimary
public class MtEventObjectColumn extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OBJECT_COLUMN_ID = "objectColumnId";
    public static final String FIELD_OBJECT_TYPE_ID = "objectTypeId";
    public static final String FIELD_COLUMN_FIELD = "columnField";
    public static final String FIELD_COLUMN_TYPE = "columnType";
    public static final String FIELD_COLUMN_TITLE = "columnTitle";
    public static final String FIELD_LINE_NUMBER = "lineNumber";
    public static final String FIELD_KID_FLAG = "kidFlag";
    public static final String FIELD_EVENT_FLAG = "eventFlag";
    public static final String FIELD_DISPLAY_FLAG = "displayFlag";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("对象列ID")
    @Id
    @Where
    private String objectColumnId;
    @ApiModelProperty(value = "关联对象ID", required = true)
    @NotBlank
    @Where
    private String objectTypeId;
    @ApiModelProperty(value = "展示字段", required = true)
    @NotBlank
    @Where
    private String columnField;
    @ApiModelProperty(value = "字段类型", required = true)
    @NotBlank
    @Where
    private String columnType;
    @ApiModelProperty(value = "字段列标题")
    @MultiLanguageField
    @Where
    private String columnTitle;
    @ApiModelProperty(value = "行号", required = true)
    @NotNull
    @Where
    private Long lineNumber;
    @ApiModelProperty(value = "是否事件字段", required = true)
    @NotBlank
    @Where
    private String eventFlag;
    @ApiModelProperty(value = "是否展示", required = true)
    @NotBlank
    @Where
    private String displayFlag;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 对象列ID
     */
    public String getObjectColumnId() {
        return objectColumnId;
    }

    public void setObjectColumnId(String objectColumnId) {
        this.objectColumnId = objectColumnId;
    }

    /**
     * @return 关联对象ID
     */
    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    /**
     * @return 展示字段
     */
    public String getColumnField() {
        return columnField;
    }

    public void setColumnField(String columnField) {
        this.columnField = columnField;
    }

    /**
     * @return 字段类型
     */
    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    /**
     * @return 字段列标题
     */
    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    /**
     * @return 行号
     */
    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }


    /**
     * @return 是否事件字段
     */
    public String getEventFlag() {
        return eventFlag;
    }

    public void setEventFlag(String eventFlag) {
        this.eventFlag = eventFlag;
    }

    /**
     * @return 是否展示
     */
    public String getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(String displayFlag) {
        this.displayFlag = displayFlag;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
