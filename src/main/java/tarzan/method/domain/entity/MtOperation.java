package tarzan.method.domain.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工序
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
@ApiModel("工序")

@ModifyAudit

@MultiLanguage
@Table(name = "mt_operation")
@CustomPrimary
public class MtOperation extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_OPERATION_NAME = "operationName";
    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_CURRENT_FLAG = "currentFlag";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_OPERATION_STATUS = "operationStatus";
    public static final String FIELD_OPERATION_TYPE = "operationType";
    public static final String FIELD_SPECIAL_ROUTER_ID = "specialRouterId";
    public static final String FIELD_TOOL_TYPE = "toolType";
    public static final String FIELD_TOOL_ID = "toolId";
    public static final String FIELD_WORKCELL_TYPE = "workcellType";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_DEFAULT_NC_ID = "defaultNcId";
    public static final String FIELD_STANDARD_MAX_LOOP = "standardMaxLoop";
    public static final String FIELD_STANDARD_SPECIAL_INTRODUCTION = "standardSpecialIntroduction";
    public static final String FIELD_STANDARD_REQD_TIME_IN_PROCESS = "standardReqdTimeInProcess";
    public static final String FIELD_COMPLETE_INCONFORMITY_FLAG = "completeInconformityFlag";
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
    @ApiModelProperty("工艺唯一标识")
    @Id
    @Where
    private String operationId;
    @ApiModelProperty(value = "站点标识", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "工艺名称", required = true)
    @NotBlank
    @Where
    private String operationName;
    @ApiModelProperty(value = "工艺版本", required = true)
    @NotBlank
    @Where
    private String revision;
    @ApiModelProperty(value = "Y/N，是否当前版本")
    @Where
    private String currentFlag;
    @ApiModelProperty(value = "有效时间至", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "有效时间从")
    @Where
    private Date dateTo;
    @ApiModelProperty(value = "备注，有条件的话做成多行文本")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "工艺状态（STATUS_GROUP:OPERATION_STATUS）", required = true)
    @NotBlank
    @Where
    private String operationStatus;
    @ApiModelProperty(value = "工艺类型（TYPE_GROUP:OPERATION_TYPE）", required = true)
    @NotBlank
    @Where
    private String operationType;
    @ApiModelProperty(value = "特殊工艺路线ID")
    @Where
    private String specialRouterId;
    @ApiModelProperty(value = "工具类型")
    @Where
    private String toolType;
    @ApiModelProperty(value = "工具")
    @Where
    private String toolId;
    @ApiModelProperty(value = "工作单元类型")
    @Where
    private String workcellType;
    @ApiModelProperty(value = "工作单元")
    @Where
    private String workcellId;
    @ApiModelProperty(value = "此操作的缺省不合格代码。操作员针对 EO 编号记录不合格项时，系统会提供此代码。")
    @Where
    private String defaultNcId;
    @ApiModelProperty(value = "可以在此操作中处理一个 EO 编号的最大次数此字段不适用于已选中〖松散路线流〗复选框的路线上的车间作业控制编号。是路线上工艺 最大循环次数 的多语言")
    @Where
    private Long standardMaxLoop;
    @ApiModelProperty(value = "路线步骤的特殊指令REQUIRED_TIME_IN_PROCESS")
    @MultiLanguageField
    @Where
    private String standardSpecialIntroduction;
    @ApiModelProperty(value = "处理车间作业控制所需的占用时间或有效工作时间（按分钟计）")
    @Where
    private Double standardReqdTimeInProcess;
    @ApiModelProperty(value = "完工不一致标识")
    @Where
    private String completeInconformityFlag;
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
     * @return 工艺唯一标识
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 站点标识
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 工艺名称
     */
    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * @return 工艺版本
     */
    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * @return Y/N，是否当前版本
     */
    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    /**
     * @return 备注，有条件的话做成多行文本
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 工艺状态（STATUS_GROUP:OPERATION_STATUS）： 1.【NEW】“新建”：用户无法使用此工艺，无法将其用于工艺路线，但是可以更改此工艺记录；
     *         2.【CAN_RELEASE】“可下达”：用户可以在此工艺中处理执行作业； 3.【FREEZE】“冻结”：用户可以在此工艺中处理执行作业，但是不可更改此工艺记录；
     *         4.【ABANDON】“废弃”：用户无法使用此工艺，而且无法在此工艺中处理执行作业，同时不可更改此工艺记录；
     *         5.【HOLD】“保留”：用户无法在此工艺中处理执行作业。当保留释放后，便可继续执行； 6.【NCLIMIT】“保留未关闭NC”用户不能在此工艺处理存在未关闭NC的执行作业。
     */
    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    /**
     * @return 工艺类型（TYPE_GROUP:OPERATION_TYPE）： 1.【NORMAL】“正常”：此工艺可以应用于常规和NC类型的工艺路线；
     *         2.【SPECIAL】“特殊”：此工艺仅可用于特殊类型的工艺路线的首道步骤； 3.【TEST】“测试”：此工艺用于测试。
     */
    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
     * @return 特殊工艺路线ID
     */
    public String getSpecialRouterId() {
        return specialRouterId;
    }

    public void setSpecialRouterId(String specialRouterId) {
        this.specialRouterId = specialRouterId;
    }

    /**
     * @return 工具类型
     */
    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    /**
     * @return 工具
     */
    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    /**
     * @return 工作单元类型
     */
    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }

    /**
     * @return 工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 此操作的缺省不合格代码。操作员针对 EO 编号记录不合格项时，系统会提供此代码。
     */
    public String getDefaultNcId() {
        return defaultNcId;
    }

    public void setDefaultNcId(String defaultNcId) {
        this.defaultNcId = defaultNcId;
    }

    /**
     * @return 可以在此操作中处理一个 EO 编号的最大次数此字段不适用于已选中〖松散路线流〗复选框的路线上的车间作业控制编号。是路线上工艺 最大循环次数 的多语言
     */
    public Long getStandardMaxLoop() {
        return standardMaxLoop;
    }

    public void setStandardMaxLoop(Long standardMaxLoop) {
        this.standardMaxLoop = standardMaxLoop;
    }

    /**
     * @return 路线步骤的特殊指令REQUIRED_TIME_IN_PROCESS
     */
    public String getStandardSpecialIntroduction() {
        return standardSpecialIntroduction;
    }

    public void setStandardSpecialIntroduction(String standardSpecialIntroduction) {
        this.standardSpecialIntroduction = standardSpecialIntroduction;
    }

    /**
     * @return 处理车间作业控制所需的占用时间或有效工作时间（按分钟计）
     */
    public Double getStandardReqdTimeInProcess() {
        return standardReqdTimeInProcess;
    }

    public void setStandardReqdTimeInProcess(Double standardReqdTimeInProcess) {
        this.standardReqdTimeInProcess = standardReqdTimeInProcess;
    }

    /**
     * @return 完工不一致标识
     */
    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
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
