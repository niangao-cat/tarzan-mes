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
 * 组织结构关系
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("组织结构关系")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_organization_rel")
@CustomPrimary
public class MtModOrganizationRel extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ORGANIZATION_REL_ID = "organizationRelId";
    public static final String FIELD_TOP_SITE_ID = "topSiteId";
    public static final String FIELD_PARENT_ORGANIZATION_TYPE = "parentOrganizationType";
    public static final String FIELD_PARENT_ORGANIZATION_ID = "parentOrganizationId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 979724613633664371L;

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
    private String organizationRelId;
    @ApiModelProperty(value = "顶层站点ID", required = true)
    @NotBlank
    @Where
    private String topSiteId;
    @ApiModelProperty(value = "父层关系对象类型", required = true)
    @NotBlank
    @Where
    private String parentOrganizationType;
    @ApiModelProperty(value = "父层关系对象", required = true)
    @NotBlank
    @Where
    private String parentOrganizationId;
    @ApiModelProperty(value = "子层对象类型", required = true)
    @NotBlank
    @Where
    private String organizationType;
    @ApiModelProperty(value = "子层对象", required = true)
    @NotBlank
    @Where
    private String organizationId;
    @ApiModelProperty(value = "父层对象下的顺序")
    @Where
    private Long sequence;
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
    public String getOrganizationRelId() {
        return organizationRelId;
    }

    public void setOrganizationRelId(String organizationRelId) {
        this.organizationRelId = organizationRelId;
    }

    /**
     * @return 顶层站点ID
     */
    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    /**
     * @return 父层关系对象类型
     */
    public String getParentOrganizationType() {
        return parentOrganizationType;
    }

    public void setParentOrganizationType(String parentOrganizationType) {
        this.parentOrganizationType = parentOrganizationType;
    }

    /**
     * @return 父层关系对象
     */
    public String getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    /**
     * @return 子层对象类型
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 子层对象
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 父层对象下的顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
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
