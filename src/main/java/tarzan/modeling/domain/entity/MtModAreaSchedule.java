package tarzan.modeling.domain.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 区域计划属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("区域计划属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_area_schedule")
@CustomPrimary
public class MtModAreaSchedule extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_AREA_SCHEDULE_ID = "areaScheduleId";
    public static final String FIELD_AREA_ID = "areaId";
    public static final String FIELD_PLAN_START_TIME = "planStartTime";
    public static final String FIELD_DEMAND_TIME_FENCE = "demandTimeFence";
    public static final String FIELD_FIX_TIME_FENCE = "fixTimeFence";
    public static final String FIELD_FROZEN_TIME_FENCE = "frozenTimeFence";
    public static final String FIELD_FORWARD_PLANNING_TIME_FENCE = "forwardPlanningTimeFence";
    public static final String FIELD_RELEASE_TIME_FENCE = "releaseTimeFence";
    public static final String FIELD_ORDER_TIME_FENCE = "orderTimeFence";
    public static final String FIELD_BASIC_ALGORITHM = "basicAlgorithm";
    public static final String FIELD_FOLLOW_AREA_ID = "followAreaId";
    public static final String FIELD_PROD_LINE_RULE = "prodLineRule";
    public static final String FIELD_PHASE_TYPE = "phaseType";
    public static final String FIELD_PLANNING_BASE = "planningBase";
    public static final String FIELD_DELAY_TIME_FENCE = "delayTimeFence";
    public static final String FIELD_RELEASE_CONCURRENT_RULE = "releaseConcurrentRule";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 418160027888291230L;

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
    @ApiModelProperty("主键ID，标示唯一一条记录")
    @Id
    @Where
    private String areaScheduleId;
    @ApiModelProperty(value = "区域ID，标识唯一区域", required = true)
    @NotBlank
    @Where
    private String areaId;
    @ApiModelProperty(value = "计划滚动起始时间")
    @Where
    private Date planStartTime;
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
    @ApiModelProperty(value = "基础排程算法")
    @Where
    private String basicAlgorithm;
    @ApiModelProperty(value = "跟随区域")
    @Where
    private String followAreaId;
    @ApiModelProperty(value = "选线规则")
    @Where
    private String prodLineRule;
    @ApiModelProperty(value = "区间类型")
    @Where
    private String phaseType;
    @ApiModelProperty(value = "排程类型")
    @Where
    private String planningBase;
    @ApiModelProperty(value = "实际延迟时间")
    @Where
    private Double delayTimeFence;
    @ApiModelProperty(value = "关联下达策略")
    @Where
    private String releaseConcurrentRule;
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
     * @return 主键ID，标示唯一一条记录
     */
    public String getAreaScheduleId() {
        return areaScheduleId;
    }

    public void setAreaScheduleId(String areaScheduleId) {
        this.areaScheduleId = areaScheduleId;
    }

    /**
     * @return 区域ID，标识唯一区域
     */
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 计划滚动起始时间
     */
    public Date getPlanStartTime() {
        if (planStartTime != null) {
            return (Date) planStartTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
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
     * @return 基础排程算法
     */
    public String getBasicAlgorithm() {
        return basicAlgorithm;
    }

    public void setBasicAlgorithm(String basicAlgorithm) {
        this.basicAlgorithm = basicAlgorithm;
    }

    /**
     * @return 跟随区域
     */
    public String getFollowAreaId() {
        return followAreaId;
    }

    public void setFollowAreaId(String followAreaId) {
        this.followAreaId = followAreaId;
    }

    /**
     * @return 选线规则
     */
    public String getProdLineRule() {
        return prodLineRule;
    }

    public void setProdLineRule(String prodLineRule) {
        this.prodLineRule = prodLineRule;
    }

    /**
     * @return 区间类型
     */
    public String getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(String phaseType) {
        this.phaseType = phaseType;
    }

    /**
     * @return 排程类型
     */
    public String getPlanningBase() {
        return planningBase;
    }

    public void setPlanningBase(String planningBase) {
        this.planningBase = planningBase;
    }

    /**
     * @return 实际延迟时间
     */
    public Double getDelayTimeFence() {
        return delayTimeFence;
    }

    public void setDelayTimeFence(Double delayTimeFence) {
        this.delayTimeFence = delayTimeFence;
    }

    /**
     * @return 关联下达策略
     */
    public String getReleaseConcurrentRule() {
        return releaseConcurrentRule;
    }

    public void setReleaseConcurrentRule(String releaseConcurrentRule) {
        this.releaseConcurrentRule = releaseConcurrentRule;
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
