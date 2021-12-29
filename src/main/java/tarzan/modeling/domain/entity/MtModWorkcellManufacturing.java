package tarzan.modeling.domain.entity;

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
 * 工作单元生产属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@ApiModel("工作单元生产属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_workcell_manufacturing")
@CustomPrimary
public class MtModWorkcellManufacturing extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORKCELL_MANUFACTURING_ID = "workcellManufacturingId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_FORWARD_SHIFT_NUMBER = "forwardShiftNumber";
    public static final String FIELD_BACKWARD_SHIFT_NUMBER = "backwardShiftNumber";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -5118779860202487254L;

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
    private String workcellManufacturingId;
    @ApiModelProperty(value = "工作单元ID，标识唯一工作单元", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "可向前操作的班次数")
    @Where
    private Long forwardShiftNumber;
    @ApiModelProperty(value = "可向后操作的班次数")
    @Where
    private Long backwardShiftNumber;
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
    public String getWorkcellManufacturingId() {
        return workcellManufacturingId;
    }

    public void setWorkcellManufacturingId(String workcellManufacturingId) {
        this.workcellManufacturingId = workcellManufacturingId;
    }

    /**
     * @return 工作单元ID，标识唯一工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 可向前操作的班次数
     */
    public Long getForwardShiftNumber() {
        return forwardShiftNumber;
    }

    public void setForwardShiftNumber(Long forwardShiftNumber) {
        this.forwardShiftNumber = forwardShiftNumber;
    }

    /**
     * @return 可向后操作的班次数
     */
    public Long getBackwardShiftNumber() {
        return backwardShiftNumber;
    }

    public void setBackwardShiftNumber(Long backwardShiftNumber) {
        this.backwardShiftNumber = backwardShiftNumber;
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
