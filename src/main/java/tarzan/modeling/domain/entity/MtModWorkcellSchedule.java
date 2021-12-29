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
 * 工作单元计划属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@ApiModel("工作单元计划属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_workcell_schedule")
@CustomPrimary
public class MtModWorkcellSchedule extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORKCELL_SCHEDULE_ID = "workcellScheduleId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_RATE_TYPE = "rateType";
    public static final String FIELD_RATE = "rate";
    public static final String FIELD_ACTIVITY = "activity";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -4679898877939935199L;

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
    private String workcellScheduleId;
    @ApiModelProperty(value = "工作单元ID，标识唯一工作单元", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "速率类型")
    @Where
    private String rateType;
    @ApiModelProperty(value = "默认速率")
    @Where
    private Double rate;
    @ApiModelProperty(value = "开动率", required = true)
    @NotNull
    @Where
    private Double activity;
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
    public String getWorkcellScheduleId() {
        return workcellScheduleId;
    }

    public void setWorkcellScheduleId(String workcellScheduleId) {
        this.workcellScheduleId = workcellScheduleId;
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
     * @return 速率类型
     */
    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    /**
     * @return 默认速率
     */
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * @return 开动率
     */
    public Double getActivity() {
        return activity;
    }

    public void setActivity(Double activity) {
        this.activity = activity;
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
