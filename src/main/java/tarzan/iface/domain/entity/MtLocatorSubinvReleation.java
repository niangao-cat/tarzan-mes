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
 * 子库存与库位对应关系
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:44
 */
@ApiModel("子库存与库位对应关系")

@ModifyAudit

@Table(name = "mt_locator_subinv_releation")
@CustomPrimary
public class MtLocatorSubinvReleation extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_RELEATION_ID = "releationId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_SUBINV = "subinv";
    public static final String FIELD_SUBINV_LOCATOR = "subinvLocator";
    public static final String FIELD_SUBINV_LOCATOR_ID = "subinvLocatorId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 6485262146976865766L;

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
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "库位ID（存储）", required = true)
    @NotBlank
    @Where
    private String locatorId;
    @ApiModelProperty(value = "工厂 CODE", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "子库存", required = true)
    @NotBlank
    @Where
    private String subinv;
    @ApiModelProperty(value = "ERP货位代码")
    @Where
    private String subinvLocator;
    @ApiModelProperty(value = "ERP货位ID")
    @Where
    private String subinvLocatorId;
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
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 库位ID（存储）
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
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
     * @return 子库存
     */
    public String getSubinv() {
        return subinv;
    }

    public void setSubinv(String subinv) {
        this.subinv = subinv;
    }

    /**
     * @return ERP货位代码
     */
    public String getSubinvLocator() {
        return subinvLocator;
    }

    public void setSubinvLocator(String subinvLocator) {
        this.subinvLocator = subinvLocator;
    }
    /**
     * @return ERP货位ID
     */
    public String getSubinvLocatorId() {
        return subinvLocatorId;
    }

    public void setSubinvLocatorId(String subinvLocatorId) {
        this.subinvLocatorId = subinvLocatorId;
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
