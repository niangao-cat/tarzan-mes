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
 * 处置组
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@ApiModel("处置组")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_disposition_group")
@CustomPrimary
public class MtDispositionGroup extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DISPOSITION_GROUP_ID = "dispositionGroupId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_DISPOSITION_GROUP = "dispositionGroup";
    public static final String FIELD_DESCRIPTION = "description";
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
    private String dispositionGroupId;
    @ApiModelProperty(value = "生产站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "处置组", required = true)
    @NotBlank
    @Where
    private String dispositionGroup;
    @ApiModelProperty(value = "描述")
    @MultiLanguageField
    @Where
    private String description;
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
    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    /**
     * @return 生产站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 处置组
     */
    public String getDispositionGroup() {
        return dispositionGroup;
    }

    public void setDispositionGroup(String dispositionGroup) {
        this.dispositionGroup = dispositionGroup;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
