package tarzan.material.domain.entity;

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
 * 物料类别站点分配
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@ApiModel("物料类别站点分配")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_material_category_site")
@CustomPrimary
public class MtMaterialCategorySite extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_CATEGORY_SITE_ID = "materialCategorySiteId";
    public static final String FIELD_MATERIAL_CATEGORY_ID = "materialCategoryId";
    public static final String FIELD_SITE_ID = "siteId";
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
    @ApiModelProperty("主键，标识唯一一条记录")
    @Id
    @Where
    private String materialCategorySiteId;
    @ApiModelProperty(value = "物料类别，来自物料类别表，对应一个物料分类", required = true)
    @NotBlank
    @Where
    private String materialCategoryId;
    @ApiModelProperty(value = "站点，来自站点表，对应唯一站点", required = true)
    @NotBlank
    @Where
    private String siteId;
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
     * @return 主键，标识唯一一条记录
     */
    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
    }

    /**
     * @return 物料类别，来自物料类别表，对应一个物料分类
     */
    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    /**
     * @return 站点，来自站点表，对应唯一站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
