package tarzan.dispatch.domain.entity;

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
 * 调度策略与组织关系表
 *
 * @author yiyang.xie 2020-02-03 19:42:38
 */
@ApiModel("调度策略与组织关系表")
@ModifyAudit
@Table(name = "mt_dispatch_strategy_org_rel")
@CustomPrimary
public class MtDispatchStrategyOrgRel extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_STRATEGY_ORG_REL_ID = "strategyOrgRelId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_RANGE_STRATEGY = "rangeStrategy";
    public static final String FIELD_PUBLISH_STRATEGY = "publishStrategy";
    public static final String FIELD_MOVE_STRATEGY = "moveStrategy";
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
    @ApiModelProperty("主键，唯一标识")
    @Id
    @Where
    private String strategyOrgRelId;
    @ApiModelProperty(value = "组织类型", required = true)
    @NotBlank
    @Where
    private String organizationType;
    @ApiModelProperty(value = "组织ID", required = true)
    @NotBlank
    @Where
    private String organizationId;
    @ApiModelProperty(value = "范围策略", required = true)
    @NotBlank
    @Where
    private String rangeStrategy;
    @ApiModelProperty(value = "发布策略", required = true)
    @NotBlank
    @Where
    private String publishStrategy;
    @ApiModelProperty(value = "移动策略")
    @Where
    private String moveStrategy;
    @ApiModelProperty(value = "启用状态", required = true)
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
     * @return 主键，唯一标识
     */
    public String getStrategyOrgRelId() {
        return strategyOrgRelId;
    }

    public void setStrategyOrgRelId(String strategyOrgRelId) {
        this.strategyOrgRelId = strategyOrgRelId;
    }

    /**
     * @return 组织类型
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 组织ID
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 范围策略
     */
    public String getRangeStrategy() {
        return rangeStrategy;
    }

    public void setRangeStrategy(String rangeStrategy) {
        this.rangeStrategy = rangeStrategy;
    }

    /**
     * @return 发布策略
     */
    public String getPublishStrategy() {
        return publishStrategy;
    }

    public void setPublishStrategy(String publishStrategy) {
        this.publishStrategy = publishStrategy;
    }

    /**
     * @return 移动策略
     */
    public String getMoveStrategy() {
        return moveStrategy;
    }

    public void setMoveStrategy(String moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    /**
     * @return 启用状态
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
