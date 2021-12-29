package tarzan.actual.domain.entity;

import java.io.Serializable;

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
 * 执行作业在制品
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
@ApiModel("执行作业在制品")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_eo_step_wip")
@CustomPrimary
public class MtEoStepWip extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_STEP_WIP_ID = "eoStepWipId";
    public static final String FIELD_EO_STEP_ACTUAL_ID = "eoStepActualId";
    public static final String FIELD_QUEUE_QTY = "queueQty";
    public static final String FIELD_WORKING_QTY = "workingQty";
    public static final String FIELD_COMPLETE_PENDING_QTY = "completePendingQty";
    public static final String FIELD_COMPLETED_QTY = "completedQty";
    public static final String FIELD_SCRAPPED_QTY = "scrappedQty";
    public static final String FIELD_HOLD_QTY = "holdQty";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -1486291948501282639L;

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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String eoStepWipId;
    @ApiModelProperty(value = "EO步骤实绩主键，表示唯一EO工艺路线步骤执行实绩", required = true)
    @NotBlank
    @Where
    private String eoStepActualId;
    @ApiModelProperty(value = "排队数量", required = true)
    @NotNull
    @Where
    private Double queueQty;
    @ApiModelProperty(value = "正在加工的数量", required = true)
    @NotNull
    @Where
    private Double workingQty;
    @ApiModelProperty(value = "完成暂存数量", required = true)
    @NotNull
    @Where
    private Double completePendingQty;
    @ApiModelProperty(value = "完成的数量", required = true)
    @NotNull
    @Where
    private Double completedQty;
    @ApiModelProperty(value = "报废数量", required = true)
    @NotNull
    @Where
    private Double scrappedQty;
    @ApiModelProperty(value = "保留数量", required = true)
    @NotNull
    @Where
    private Double holdQty;
    @ApiModelProperty(value = "EO所在的工作单元的工作单元")
    @Where
    private String workcellId;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getEoStepWipId() {
        return eoStepWipId;
    }

    public void setEoStepWipId(String eoStepWipId) {
        this.eoStepWipId = eoStepWipId;
    }

    /**
     * @return EO步骤实绩主键，表示唯一EO工艺路线步骤执行实绩
     */
    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    /**
     * @return 排队数量
     */
    public Double getQueueQty() {
        return queueQty;
    }

    public void setQueueQty(Double queueQty) {
        this.queueQty = queueQty;
    }

    /**
     * @return 正在加工的数量
     */
    public Double getWorkingQty() {
        return workingQty;
    }

    public void setWorkingQty(Double workingQty) {
        this.workingQty = workingQty;
    }

    /**
     * @return 完成暂存数量
     */
    public Double getCompletePendingQty() {
        return completePendingQty;
    }

    public void setCompletePendingQty(Double completePendingQty) {
        this.completePendingQty = completePendingQty;
    }

    /**
     * @return 完成的数量
     */
    public Double getCompletedQty() {
        return completedQty;
    }

    public void setCompletedQty(Double completedQty) {
        this.completedQty = completedQty;
    }

    /**
     * @return 报废数量
     */
    public Double getScrappedQty() {
        return scrappedQty;
    }

    public void setScrappedQty(Double scrappedQty) {
        this.scrappedQty = scrappedQty;
    }

    /**
     * @return 保留数量
     */
    public Double getHoldQty() {
        return holdQty;
    }

    public void setHoldQty(Double holdQty) {
        this.holdQty = holdQty;
    }

    /**
     * @return EO所在的工作单元的工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
