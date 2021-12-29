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
 * 物料供应比例明细
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@ApiModel("物料供应比例明细")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_material_supplier_per_l")
@CustomPrimary
public class MtMaterialSupplierPerL extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SUPPLIER_PERCENT_LINE_ID = "supplierPercentLineId";
    public static final String FIELD_SUPPLIER_PERCENT_HEADER_ID = "supplierPercentHeaderId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_PERCENT = "percent";
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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String supplierPercentLineId;
    @ApiModelProperty(value = "外键", required = true)
    @NotBlank
    @Where
    private String supplierPercentHeaderId;
    @ApiModelProperty(value = "供应商", required = true)
    @NotBlank
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商地点")
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "供应比例%", required = true)
    @NotNull
    @Where
    private Double percent;
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
    public String getSupplierPercentLineId() {
        return supplierPercentLineId;
    }

    public void setSupplierPercentLineId(String supplierPercentLineId) {
        this.supplierPercentLineId = supplierPercentLineId;
    }

    /**
     * @return 外键
     */
    public String getSupplierPercentHeaderId() {
        return supplierPercentHeaderId;
    }

    public void setSupplierPercentHeaderId(String supplierPercentHeaderId) {
        this.supplierPercentHeaderId = supplierPercentHeaderId;
    }

    /**
     * @return 供应商
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 供应商地点
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    /**
     * @return 供应比例%
     */
    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
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
