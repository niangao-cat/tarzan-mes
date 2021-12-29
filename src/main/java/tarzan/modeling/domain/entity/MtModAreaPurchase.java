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
 * 区域采购属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@ApiModel("区域采购属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_mod_area_purchase")
@CustomPrimary
public class MtModAreaPurchase extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_AREA_PURCHASE_ID = "areaPurchaseId";
    public static final String FIELD_AREA_ID = "areaId";
    public static final String FIELD_INSIDE_FLAG = "insideFlag";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -3057064179719404657L;

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
    @ApiModelProperty("主键ID，标示唯一一条记录")
    @Id
    @Where
    private String areaPurchaseId;
    @ApiModelProperty(value = "区域ID，标识唯一区域", required = true)
    @NotBlank
    @Where
    private String areaId;
    @ApiModelProperty(value = "是否厂内区域，主要在采购站点下使用，如区分厂内厂外送货区域")
    @Where
    private String insideFlag;
    @ApiModelProperty(value = "厂外区域供应商")
    @Where
    private String supplierId;
    @ApiModelProperty(value = "厂外区域供应商地点")
    @Where
    private String supplierSiteId;
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
     * @return 主键ID，标示唯一一条记录
     */
    public String getAreaPurchaseId() {
        return areaPurchaseId;
    }

    public void setAreaPurchaseId(String areaPurchaseId) {
        this.areaPurchaseId = areaPurchaseId;
    }

    /**
     * @return 区域ID，标识唯一区域
     */
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 是否厂内区域，主要在采购站点下使用，如区分厂内厂外送货区域
     */
    public String getInsideFlag() {
        return insideFlag;
    }

    public void setInsideFlag(String insideFlag) {
        this.insideFlag = insideFlag;
    }

    /**
     * @return 厂外区域供应商
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 厂外区域供应商地点
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
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
