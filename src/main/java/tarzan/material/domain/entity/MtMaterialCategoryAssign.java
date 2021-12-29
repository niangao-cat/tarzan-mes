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
 * 物料类别分配
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@ApiModel("物料类别分配")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_material_category_assign")
@CustomPrimary
public class MtMaterialCategoryAssign extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_CATEGORY_ASSIGN_ID = "materialCategoryAssignId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_MATERIAL_CATEGORY_ID = "materialCategoryId";
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
    @ApiModelProperty("主键ID,标识唯一一条记录")
    @Id
    @Where
    private String materialCategoryAssignId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系", required = true)
    @NotBlank
    @Where
    private String materialSiteId;
    @ApiModelProperty(value = "物料类别", required = true)
    @NotBlank
    @Where
    private String materialCategoryId;
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
     * @return 主键ID,标识唯一一条记录
     */
    public String getMaterialCategoryAssignId() {
        return materialCategoryAssignId;
    }

    public void setMaterialCategoryAssignId(String materialCategoryAssignId) {
        this.materialCategoryAssignId = materialCategoryAssignId;
    }

    /**
     * @return 物料站点主键，标识唯一物料站点对应关系
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 物料类别
     */
    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
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
