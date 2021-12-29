package tarzan.modeling.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
@ApiModel("供应商")

@ModifyAudit

@MultiLanguage
@Table(name = "mt_supplier")
@CustomPrimary
public class MtSupplier extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_NAME = "supplierName";
    public static final String FIELD_SUPPLIER_NAME_ALT = "supplierNameAlt";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_SUPPLIER_TYPE = "supplierType";
    public static final String FIELD_SOURCE_IDENTIFICATION_ID = "sourceIdentificationId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -5349332025811528299L;

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
    @ApiModelProperty("唯一性主键标识")
    @Id
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商编码", required = true)
    @NotBlank
    @Where
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String supplierName;
    @ApiModelProperty(value = "供应商简称")
    @MultiLanguageField
    @Where
    private String supplierNameAlt;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效时间")
    @Where
    private Date dateTo;
    @ApiModelProperty(value = "供应商类型")
    @Where
    private String supplierType;
    @ApiModelProperty(value = "外部来源标识Id")
    @Where
    private Double sourceIdentificationId;
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
     * @return 唯一性主键标识
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 供应商编码
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * @return 供应商名称
     */
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return 供应商简称
     */
    public String getSupplierNameAlt() {
        return supplierNameAlt;
    }

    public void setSupplierNameAlt(String supplierNameAlt) {
        this.supplierNameAlt = supplierNameAlt;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    /**
     * @return 供应商类型
     */
    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    /**
     * @return 外部来源标识Id
     */
    public Double getSourceIdentificationId() {
        return sourceIdentificationId;
    }

    public void setSourceIdentificationId(Double sourceIdentificationId) {
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
