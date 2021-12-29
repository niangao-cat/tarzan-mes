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
 * 生产线计划属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("生产线计划属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_prod_line_schedule")
@CustomPrimary
public class MtModProdLineSchedule extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PROD_LINE_SCHEDULE_ID = "prodLineScheduleId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_RATE_TYPE = "rateType";
    public static final String FIELD_RATE = "rate";
    public static final String FIELD_ACTIVITY = "activity";
    public static final String FIELD_DEMAND_TIME_FENCE = "demandTimeFence";
    public static final String FIELD_FIX_TIME_FENCE = "fixTimeFence";
    public static final String FIELD_FROZEN_TIME_FENCE = "frozenTimeFence";
    public static final String FIELD_FORWARD_PLANNING_TIME_FENCE = "forwardPlanningTimeFence";
    public static final String FIELD_RELEASE_TIME_FENCE = "releaseTimeFence";
    public static final String FIELD_ORDER_TIME_FENCE = "orderTimeFence";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -8045693038419449707L;

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
    @ApiModelProperty("主键ID ,表示唯一一条记录")
    @Id
    @Where
    private String prodLineScheduleId;
    @ApiModelProperty(value = "生产线ID，标识唯一一条生产线", required = true)
    @NotBlank
    @Where
    private String prodLineId;
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
    @ApiModelProperty(value = "需求时间栏(天)")
    @Where
    private Double demandTimeFence;
    @ApiModelProperty(value = "固定时间栏(天)")
    @Where
    private Double fixTimeFence;
    @ApiModelProperty(value = "冻结时间栏(天)")
    @Where
    private Double frozenTimeFence;
    @ApiModelProperty(value = "顺排时间栏(天)")
    @Where
    private Double forwardPlanningTimeFence;
    @ApiModelProperty(value = "下达时间栏(天)")
    @Where
    private Double releaseTimeFence;
    @ApiModelProperty(value = "订单时间栏(天)")
    @Where
    private Double orderTimeFence;
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
     * @return 主键ID ,表示唯一一条记录
     */
    public String getProdLineScheduleId() {
        return prodLineScheduleId;
    }

    public void setProdLineScheduleId(String prodLineScheduleId) {
        this.prodLineScheduleId = prodLineScheduleId;
    }

    /**
     * @return 生产线ID，标识唯一一条生产线
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
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
     * @return 需求时间栏(天)
     */
    public Double getDemandTimeFence() {
        return demandTimeFence;
    }

    public void setDemandTimeFence(Double demandTimeFence) {
        this.demandTimeFence = demandTimeFence;
    }

    /**
     * @return 固定时间栏(天)
     */
    public Double getFixTimeFence() {
        return fixTimeFence;
    }

    public void setFixTimeFence(Double fixTimeFence) {
        this.fixTimeFence = fixTimeFence;
    }

    /**
     * @return 冻结时间栏(天)
     */
    public Double getFrozenTimeFence() {
        return frozenTimeFence;
    }

    public void setFrozenTimeFence(Double frozenTimeFence) {
        this.frozenTimeFence = frozenTimeFence;
    }

    /**
     * @return 顺排时间栏(天)
     */
    public Double getForwardPlanningTimeFence() {
        return forwardPlanningTimeFence;
    }

    public void setForwardPlanningTimeFence(Double forwardPlanningTimeFence) {
        this.forwardPlanningTimeFence = forwardPlanningTimeFence;
    }

    /**
     * @return 下达时间栏(天)
     */
    public Double getReleaseTimeFence() {
        return releaseTimeFence;
    }

    public void setReleaseTimeFence(Double releaseTimeFence) {
        this.releaseTimeFence = releaseTimeFence;
    }

    /**
     * @return 订单时间栏(天)
     */
    public Double getOrderTimeFence() {
        return orderTimeFence;
    }

    public void setOrderTimeFence(Double orderTimeFence) {
        this.orderTimeFence = orderTimeFence;
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
