package tarzan.method.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工艺路线步骤历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("工艺路线步骤历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_step_his")
@CustomPrimary
public class MtRouterStepHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_STEP_HIS_ID = "routerStepHisId";
    public static final String FIELD_ROUTER_STEP_ID = "routerStepId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_STEP_NAME = "stepName";
    public static final String FIELD_ROUTER_STEP_TYPE = "routerStepType";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_COPRODUCT_FLAG = "coproductFlag";
    public static final String FIELD_QUEUE_DECISION_TYPE = "queueDecisionType";
    public static final String FIELD_ENTRY_STEP_FLAG = "entryStepFlag";
    public static final String FIELD_KEY_STEP_FLAG = "keyStepFlag";
    public static final String FIELD_COPIED_FROM_ROUTER_STEP_ID = "copiedFromRouterStepId";
    public static final String FIELD_EVENT_ID = "eventId";
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
    @ApiModelProperty("工艺路线步骤历史唯一性标识")
    @Id
    @Where
    private String routerStepHisId;
    @ApiModelProperty(value = "工艺路线步骤唯一性标识", required = true)
    @NotBlank
    @Where
    private String routerStepId;
    @ApiModelProperty(value = "工艺路线标识", required = true)
    @NotBlank
    @Where
    private String routerId;
    @ApiModelProperty(value = "步骤识别码", required = true)
    @NotBlank
    @Where
    private String stepName;
    @ApiModelProperty(value = "步骤类型", required = true)
    @NotBlank
    @Where
    private String routerStepType;
    @ApiModelProperty(value = "大致的执行顺序，实际的顺序看ROUTER_NEXT_STEP", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "工艺路线步骤描述")
    @Where
    private String description;
    @ApiModelProperty(value = "是否产生联产品")
    @Where
    private String coproductFlag;
    @ApiModelProperty(value = "路径选择策略", required = true)
    @NotBlank
    @Where
    private String queueDecisionType;
    @ApiModelProperty(value = "入口步骤")
    @Where
    private String entryStepFlag;
    @ApiModelProperty(value = "是否关键步骤")
    @Where
    private String keyStepFlag;
    @ApiModelProperty(value = "复制来源 ROUTER_STEP_ID")
    @Where
    private String copiedFromRouterStepId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 工艺路线步骤历史唯一性标识
     */
    public String getRouterStepHisId() {
        return routerStepHisId;
    }

    public void setRouterStepHisId(String routerStepHisId) {
        this.routerStepHisId = routerStepHisId;
    }

    /**
     * @return 工艺路线步骤唯一性标识
     */
    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    /**
     * @return 工艺路线标识
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 步骤识别码
     */
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    /**
     * @return 步骤类型
     */
    public String getRouterStepType() {
        return routerStepType;
    }

    public void setRouterStepType(String routerStepType) {
        this.routerStepType = routerStepType;
    }

    /**
     * @return 大致的执行顺序，实际的顺序看ROUTER_NEXT_STEP
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 工艺路线步骤描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 是否产生联产品
     */
    public String getCoproductFlag() {
        return coproductFlag;
    }

    public void setCoproductFlag(String coproductFlag) {
        this.coproductFlag = coproductFlag;
    }

    /**
     * @return 路径选择策略
     */
    public String getQueueDecisionType() {
        return queueDecisionType;
    }

    public void setQueueDecisionType(String queueDecisionType) {
        this.queueDecisionType = queueDecisionType;
    }

    /**
     * @return 入口步骤
     */
    public String getEntryStepFlag() {
        return entryStepFlag;
    }

    public void setEntryStepFlag(String entryStepFlag) {
        this.entryStepFlag = entryStepFlag;
    }

    /**
     * @return 是否关键步骤
     */
    public String getKeyStepFlag() {
        return keyStepFlag;
    }

    public void setKeyStepFlag(String keyStepFlag) {
        this.keyStepFlag = keyStepFlag;
    }

    /**
     * @return 复制来源 ROUTER_STEP_ID
     */
    public String getCopiedFromRouterStepId() {
        return copiedFromRouterStepId;
    }

    public void setCopiedFromRouterStepId(String copiedFromRouterStepId) {
        this.copiedFromRouterStepId = copiedFromRouterStepId;
    }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
