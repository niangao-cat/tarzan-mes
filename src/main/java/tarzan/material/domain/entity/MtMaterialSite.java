package tarzan.material.domain.entity;

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
 * 物料站点分配
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@ApiModel("物料站点分配")

@ModifyAudit

@Table(name = "mt_material_site")
@CustomPrimary
public class MtMaterialSite extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_SOURCE_IDENTIFICATION_ID = "sourceIdentificationId";
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
    private String materialSiteId;
    @ApiModelProperty(value = "物料，来自物料表，对应唯一物料", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "站点，来自站点表，对应唯一站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "来源标识Id")
    @Where
    private String sourceIdentificationId;
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
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 物料，来自物料表，对应唯一物料
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 来源标识Id
     */
    public String getSourceIdentificationId() {
        return sourceIdentificationId;
    }

    public void setSourceIdentificationId(String sourceIdentificationId) {
        this.sourceIdentificationId = sourceIdentificationId;
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
