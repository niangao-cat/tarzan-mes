package tarzan.iface.domain.entity;

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

import java.io.Serializable;

/**
 * ERP工厂与站点映射关系
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:02
 */
@ApiModel("ERP工厂与站点映射关系")

@ModifyAudit

@Table(name = "mt_site_plant_releation")
@CustomPrimary
public class MtSitePlantReleation extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_RELEATION_ID = "releationId";
    public static final String FIELD_SITE_TYPE = "siteType";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_PLANT_ID = "plantId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 1867924536282617467L;

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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String releationId;
    @ApiModelProperty(value = "站点类型:PURCHASE , SCHEDULE , MANUFACTURING", required = true)
    @NotBlank
    @Where
    private String siteType;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "工厂Id")
    @Where
    private Double plantId;
    @ApiModelProperty(value = "工厂 CODE", required = true)
    @NotBlank
    @Where
    private String plantCode;
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
     * @return 主键
     */
    public String getReleationId() {
        return releationId;
    }

    public void setReleationId(String releationId) {
        this.releationId = releationId;
    }

    /**
     * @return 站点类型:PURCHASE , SCHEDULE , MANUFACTURING
     */
    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
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
     * @return 工厂Id
     */
    public Double getPlantId() {
        return plantId;
    }

    public void setPlantId(Double plantId) {
        this.plantId = plantId;
    }

    /**
     * @return 工厂 CODE
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
