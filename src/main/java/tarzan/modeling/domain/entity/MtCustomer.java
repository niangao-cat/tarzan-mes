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
 * 客户
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
@ApiModel("客户")

@ModifyAudit

@MultiLanguage
@Table(name = "mt_customer")
@CustomPrimary
public class MtCustomer extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_CODE = "customerCode";
    public static final String FIELD_CUSTOMER_NAME = "customerName";
    public static final String FIELD_CUSTOMER_NAME_ALT = "customerNameAlt";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_CUSTOMER_TYPE = "customerType";
    public static final String FIELD_SOURCE_IDENTIFICATION_ID = "sourceIdentificationId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 1797998666187688664L;

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
    private String customerId;
    @ApiModelProperty(value = "客户编码", required = true)
    @NotBlank
    @Where
    private String customerCode;
    @ApiModelProperty(value = "客户名称", required = true)
    @MultiLanguageField
    @NotBlank
    @Where
    private String customerName;
    @ApiModelProperty(value = "客户简称")
    @MultiLanguageField
    @Where
    private String customerNameAlt;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效时间")
    @Where
    private Date dateTo;
    @ApiModelProperty(value = "客户类型")
    @Where
    private String customerType;
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
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 客户编码
     */
    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return 客户名称
     */
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return 客户简称
     */
    public String getCustomerNameAlt() {
        return customerNameAlt;
    }

    public void setCustomerNameAlt(String customerNameAlt) {
        this.customerNameAlt = customerNameAlt;
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
     * @return 客户类型
     */
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
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
