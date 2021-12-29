package tarzan.general.domain.entity;

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
 * 用户组织关系表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:40
 */
@ApiModel("用户组织关系表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_user_organization")
@CustomPrimary
public class MtUserOrganization extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_USER_ORGANIZATION_ID = "userOrganizationId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_DEFAULT_ORGANIZATION_FLAG = "defaultOrganizationFlag";
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
    @ApiModelProperty("用户组织关系ID")
    @Id
    @Where
    private String userOrganizationId;
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull
    @Where
    private Long userId;
    @ApiModelProperty(value = "用户关联组织类型", required = true)
    @NotBlank
    @Where
    private String organizationType;
    @ApiModelProperty(value = "用户关联组织对象ID", required = true)
    @NotBlank
    @Where
    private String organizationId;
    @ApiModelProperty(value = "默认组织标识")
    @Where
    private String defaultOrganizationFlag;
    @ApiModelProperty(value = "有效性", required = true)
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
     * @return 用户组织关系ID
     */
    public String getUserOrganizationId() {
        return userOrganizationId;
    }

    public void setUserOrganizationId(String userOrganizationId) {
        this.userOrganizationId = userOrganizationId;
    }

    /**
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return 用户关联组织类型
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 用户关联组织对象ID
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 默认组织标识
     */
    public String getDefaultOrganizationFlag() {
        return defaultOrganizationFlag;
    }

    public void setDefaultOrganizationFlag(String defaultOrganizationFlag) {
        this.defaultOrganizationFlag = defaultOrganizationFlag;
    }

    /**
     * @return 有效性
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
