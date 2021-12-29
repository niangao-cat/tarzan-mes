package tarzan.method.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 不良代码组
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@ApiModel("不良代码组")

@ModifyAudit

@MultiLanguage
@Table(name = "mt_nc_group")
@CustomPrimary
public class MtNcGroup extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NC_GROUP_ID = "ncGroupId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_NC_GROUP_CODE = "ncGroupCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_CLOSURE_REQUIRED = "closureRequired";
    public static final String FIELD_CONFIRM_REQUIRED = "confirmRequired";
    public static final String FIELD_AUTO_CLOSE_INCIDENT = "autoCloseIncident";
    public static final String FIELD_AUTO_CLOSE_PRIMARY = "autoClosePrimary";
    public static final String FIELD_CAN_BE_PRIMARY_CODE = "canBePrimaryCode";
    public static final String FIELD_VALID_AT_ALL_OPERATIONS = "validAtAllOperations";
    public static final String FIELD_ALLOW_NO_DISPOSITION = "allowNoDisposition";
    public static final String FIELD_COMPONENT_REQUIRED = "componentRequired";
    public static final String FIELD_DISPOSITION_GROUP_ID = "dispositionGroupId";
    public static final String FIELD_MAX_NC_LIMIT = "maxNcLimit";
    public static final String FIELD_SECONDARY_CODE_SP_INSTR = "secondaryCodeSpInstr";
    public static final String FIELD_SECONDARY_REQD_FOR_CLOSE = "secondaryReqdForClose";
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
    @ApiModelProperty("唯一标识：表ID，主键，供其他表做外键")
    @Id
    @Where
    private String ncGroupId;
    @ApiModelProperty(value = "站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "不良代码组编码", required = true)
    @NotBlank
    @Where
    private String ncGroupCode;
    @ApiModelProperty(value = "描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "优先级:编号越大，优先级越高", required = true)
    @NotNull
    @Where
    private Long priority;
    @ApiModelProperty(value = "是否需要被关闭", required = true)
    @NotBlank
    @Where
    private String closureRequired;
    @ApiModelProperty(value = "是否需要复核", required = true)
    @NotBlank
    @Where
    private String confirmRequired;
    @ApiModelProperty(value = "自动关闭事故", required = true)
    @NotBlank
    @Where
    private String autoCloseIncident;
    @ApiModelProperty(value = "自动关闭主代码", required = true)
    @NotBlank
    @Where
    private String autoClosePrimary;
    @ApiModelProperty(value = "可以是主代码", required = true)
    @NotBlank
    @Where
    private String canBePrimaryCode;
    @ApiModelProperty(value = "对所有工艺有效", required = true)
    @NotBlank
    @Where
    private String validAtAllOperations;
    @ApiModelProperty(value = "允许无处置", required = true)
    @NotBlank
    @Where
    private String allowNoDisposition;
    @ApiModelProperty(value = "是否需要记录组件", required = true)
    @NotBlank
    @Where
    private String componentRequired;
    @ApiModelProperty(value = "处置组")
    @Where
    private String dispositionGroupId;
    @ApiModelProperty(value = "最大限制值", required = true)
    @NotNull
    @Where
    private Long maxNcLimit;
    @ApiModelProperty(value = "次级代码特殊指令")
    @Where
    private String secondaryCodeSpInstr;
    @ApiModelProperty(value = "需要次级代码才能关闭", required = true)
    @NotBlank
    @Where
    private String secondaryReqdForClose;

    @ApiModelProperty(value = "是否启用（Y/N）", required = true)
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
     * @return 唯一标识：表ID，主键，供其他表做外键
     */
    public String getNcGroupId() {
        return ncGroupId;
    }

    public void setNcGroupId(String ncGroupId) {
        this.ncGroupId = ncGroupId;
    }

    /**
     * @return 站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 不良代码组编码
     */
    public String getNcGroupCode() {
        return ncGroupCode;
    }

    public void setNcGroupCode(String ncGroupCode) {
        this.ncGroupCode = ncGroupCode;
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
     * @return 优先级:编号越大，优先级越高
     */
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    /**
     * @return 是否需要被关闭
     */
    public String getClosureRequired() {
        return closureRequired;
    }

    public void setClosureRequired(String closureRequired) {
        this.closureRequired = closureRequired;
    }

    /**
     * @return 是否需要复核
     */
    public String getConfirmRequired() {
        return confirmRequired;
    }

    public void setConfirmRequired(String confirmRequired) {
        this.confirmRequired = confirmRequired;
    }

    /**
     * @return 自动关闭事故
     */
    public String getAutoCloseIncident() {
        return autoCloseIncident;
    }

    public void setAutoCloseIncident(String autoCloseIncident) {
        this.autoCloseIncident = autoCloseIncident;
    }

    /**
     * @return 自动关闭主代码
     */
    public String getAutoClosePrimary() {
        return autoClosePrimary;
    }

    public void setAutoClosePrimary(String autoClosePrimary) {
        this.autoClosePrimary = autoClosePrimary;
    }

    /**
     * @return 可以是主代码
     */
    public String getCanBePrimaryCode() {
        return canBePrimaryCode;
    }

    public void setCanBePrimaryCode(String canBePrimaryCode) {
        this.canBePrimaryCode = canBePrimaryCode;
    }

    /**
     * @return 对所有工艺有效
     */
    public String getValidAtAllOperations() {
        return validAtAllOperations;
    }

    public void setValidAtAllOperations(String validAtAllOperations) {
        this.validAtAllOperations = validAtAllOperations;
    }

    /**
     * @return 允许无处置
     */
    public String getAllowNoDisposition() {
        return allowNoDisposition;
    }

    public void setAllowNoDisposition(String allowNoDisposition) {
        this.allowNoDisposition = allowNoDisposition;
    }

    /**
     * @return 是否需要记录组件
     */
    public String getComponentRequired() {
        return componentRequired;
    }

    public void setComponentRequired(String componentRequired) {
        this.componentRequired = componentRequired;
    }

    /**
     * @return 处置组
     */
    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    /**
     * @return 最大限制值
     */
    public Long getMaxNcLimit() {
        return maxNcLimit;
    }

    public void setMaxNcLimit(Long maxNcLimit) {
        this.maxNcLimit = maxNcLimit;
    }

    /**
     * @return 次级代码特殊指令
     */
    public String getSecondaryCodeSpInstr() {
        return secondaryCodeSpInstr;
    }

    public void setSecondaryCodeSpInstr(String secondaryCodeSpInstr) {
        this.secondaryCodeSpInstr = secondaryCodeSpInstr;
    }

    /**
     * @return 需要次级代码才能关闭
     */
    public String getSecondaryReqdForClose() {
        return secondaryReqdForClose;
    }

    public void setSecondaryReqdForClose(String secondaryReqdForClose) {
        this.secondaryReqdForClose = secondaryReqdForClose;
    }

    /**
     * @return 是否启用（Y/N）
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
