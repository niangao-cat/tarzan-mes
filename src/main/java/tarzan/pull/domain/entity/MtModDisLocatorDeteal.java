package tarzan.pull.domain.entity;

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
 * 配送节点表
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:42
 */
@ApiModel("配送节点表")
@ModifyAudit
@Table(name = "mt_mod_dis_locator_deteal")
@CustomPrimary
public class MtModDisLocatorDeteal extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DISTRIBUTION_LOCATOR_DETEAL_ID = "distributionLocatorDetealId";
    public static final String FIELD_LOCATOR_ORGANIZATION_REL_ID = "locatorOrganizationRelId";
    public static final String FIELD_PULL_T0_ARRIVE = "pullT0Arrive";
    public static final String FIELD_SOURCE_LOCATOR_FLAG = "sourceLocatorFlag";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键，唯一标识")
    @Id
    @Where
    private String distributionLocatorDetealId;
    @ApiModelProperty(value = "组织与库位关系ID",required = true)
    @NotBlank
    @Where
    private String locatorOrganizationRelId;
   @ApiModelProperty(value = "触发到送达的周期")    
    @Where
    private Double pullT0Arrive;
    @ApiModelProperty(value = "来源库位标识",required = true)
    @NotBlank
    @Where
    private String sourceLocatorFlag;
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
	public String getDistributionLocatorDetealId() {
		return distributionLocatorDetealId;
	}

	public void setDistributionLocatorDetealId(String distributionLocatorDetealId) {
		this.distributionLocatorDetealId = distributionLocatorDetealId;
	}
    /**
     * @return 组织与库位关系ID
     */
	public String getLocatorOrganizationRelId() {
		return locatorOrganizationRelId;
	}

	public void setLocatorOrganizationRelId(String locatorOrganizationRelId) {
		this.locatorOrganizationRelId = locatorOrganizationRelId;
	}
    /**
     * @return 触发到送达的周期
     */
	public Double getPullT0Arrive() {
		return pullT0Arrive;
	}

	public void setPullT0Arrive(Double pullT0Arrive) {
		this.pullT0Arrive = pullT0Arrive;
	}
    /**
     * @return 来源库位标识
     */
	public String getSourceLocatorFlag() {
		return sourceLocatorFlag;
	}

	public void setSourceLocatorFlag(String sourceLocatorFlag) {
		this.sourceLocatorFlag = sourceLocatorFlag;
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
