package tarzan.method.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 子步骤
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:23:13
 */
@ApiModel("子步骤")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_substep")
@CustomPrimary
public class MtSubstep extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SUBSTEP_ID = "substepId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SUBSTEP_NAME = "substepName";
    public static final String FIELD_SUBSTEP_TYPE = "substepType";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_LONG_DESCRIPTION = "longDescription";
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
    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @Where
    private String substepId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "子步骤", required = true)
    @NotBlank
    @Where
    private String substepName;
    @ApiModelProperty(value = "子步骤类型", required = true)
    @NotBlank
    @Where
    private String substepType;
    @ApiModelProperty(value = "子步骤描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "子步骤长描述")
    @MultiLanguageField
    @Where
    private String longDescription;
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
     * @return 表ID，主键，供其他表做外键
     */
    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 子步骤
     */
    public String getSubstepName() {
        return substepName;
    }

    public void setSubstepName(String substepName) {
        this.substepName = substepName;
    }

    /**
     * @return 子步骤类型，包括： Normal：该子步骤是正常且必须完成的； Optional：该子步骤是可选则完成的，有可能被略过； Critical：该子步骤是关键且必须完成的。
     */
    public String getSubstepType() {
        return substepType;
    }

    public void setSubstepType(String substepType) {
        this.substepType = substepType;
    }

    /**
     * @return 子步骤描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 子步骤长描述
     */
    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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
