package tarzan.modeling.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 区域配送属性
 *
 * @author yiyang.xie 2020-02-04 11:36:01
 */
@ApiModel("区域配送属性")
@ModifyAudit
@Table(name = "mt_mod_area_distribution")
@CustomPrimary
public class MtModAreaDistribution extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_AREA_DISTRIBUTION_ID = "areaDistributionId";
    public static final String FIELD_AREA_ID = "areaId";
    public static final String FIELD_DISTRIBUTION_MODE = "distributionMode";
    public static final String FIELD_PULL_TIME_INTERVAL_FLAG = "pullTimeIntervalFlag";
    public static final String FIELD_DISTRIBUTION_CYCLE = "distributionCycle";
    public static final String FIELD_BUSINESS_TYPE = "businessType";
    public static final String FIELD_INSTRUCT_CREATED_BY_EO = "instructCreatedByEo";
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
    @ApiModelProperty("主键，唯一标识")
    @Id
    @Where
    private String areaDistributionId;
    @ApiModelProperty(value = "区域ID，标识唯一区域", required = true)
    @NotBlank
    @Where
    private String areaId;
    @ApiModelProperty(value = "配送模式：定时补货拉动、定量补货拉动、按订单补货拉动、定时顺序拉动", required = true)
    @NotBlank
    @Where
    private String distributionMode;
    @ApiModelProperty(value = "是否启用拉动时段（按订单补货以及定时顺序拉动启用，其它模式不启用）")
    @Where
    private String pullTimeIntervalFlag;
    @ApiModelProperty(value = "配送周期T")
    @Where
    private Double distributionCycle;
    @ApiModelProperty(value = "指令业务类型")
    @Where
    private String businessType;
    @ApiModelProperty(value = "指令按照EO做拆分")
    @Where
    private String instructCreatedByEo;
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
     * @return 主键，唯一标识
     */
    public String getAreaDistributionId() {
        return areaDistributionId;
    }

    public void setAreaDistributionId(String areaDistributionId) {
        this.areaDistributionId = areaDistributionId;
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
     * @return 配送模式：定时补货拉动、定量补货拉动、按订单补货拉动、定时顺序拉动
     */
    public String getDistributionMode() {
        return distributionMode;
    }

    public void setDistributionMode(String distributionMode) {
        this.distributionMode = distributionMode;
    }

    /**
     * @return 是否启用拉动时段（按订单补货以及定时顺序拉动启用，其它模式不启用）
     */
    public String getPullTimeIntervalFlag() {
        return pullTimeIntervalFlag;
    }

    public void setPullTimeIntervalFlag(String pullTimeIntervalFlag) {
        this.pullTimeIntervalFlag = pullTimeIntervalFlag;
    }

    /**
     * @return 配送周期T
     */
    public Double getDistributionCycle() {
        return distributionCycle;
    }

    public void setDistributionCycle(Double distributionCycle) {
        this.distributionCycle = distributionCycle;
    }

    /**
     * @return 指令业务类型
     */
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * @return 指令按照EO做拆分
     */
    public String getInstructCreatedByEo() {
        return instructCreatedByEo;
    }

    public void setInstructCreatedByEo(String instructCreatedByEo) {
        this.instructCreatedByEo = instructCreatedByEo;
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
