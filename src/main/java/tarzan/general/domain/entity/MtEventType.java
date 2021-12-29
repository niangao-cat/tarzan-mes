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
 * 事件类型定义
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@ApiModel("事件类型定义")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_event_type")
@CustomPrimary
public class MtEventType extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EVENT_TYPE_ID = "eventTypeId";
    public static final String FIELD_EVENT_TYPE_CODE = "eventTypeCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_DEFAULT_EVENT_TYPE_FLAG = "defaultEventTypeFlag";
    public static final String FIELD_ONHAND_CHANGE_FLAG = "onhandChangeFlag";
    public static final String FIELD_ONHAND_CHANGE_TYPE = "onhandChangeType";
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
    @ApiModelProperty("事件类型ID")
    @Id
    @Where
    private String eventTypeId;
    @ApiModelProperty(value = "事件类型编码", required = true)
    @NotBlank
    @Where
    private String eventTypeCode;
    @ApiModelProperty(value = "描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "系统初始事件类型标识", required = true)
    @NotBlank
    @Where
    private String defaultEventTypeFlag;
    @ApiModelProperty(value = "是否影响现有量标识", required = true)
    @NotBlank
    @Where
    private String onhandChangeFlag;
    @ApiModelProperty(value = "现有量变化类型（增加/减少）")
    @Where
    private String onhandChangeType;
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
     * @return 事件类型ID
     */
    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    /**
     * @return 事件类型编码
     */
    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
     * @return 系统初始事件类型标识
     */
    public String getDefaultEventTypeFlag() {
        return defaultEventTypeFlag;
    }

    public void setDefaultEventTypeFlag(String defaultEventTypeFlag) {
        this.defaultEventTypeFlag = defaultEventTypeFlag;
    }

    /**
     * @return 是否影响现有量标识
     */
    public String getOnhandChangeFlag() {
        return onhandChangeFlag;
    }

    public void setOnhandChangeFlag(String onhandChangeFlag) {
        this.onhandChangeFlag = onhandChangeFlag;
    }

    /**
     * @return 现有量变化类型（增加/减少）
     */
    public String getOnhandChangeType() {
        return onhandChangeType;
    }

    public void setOnhandChangeType(String onhandChangeType) {
        this.onhandChangeType = onhandChangeType;
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
