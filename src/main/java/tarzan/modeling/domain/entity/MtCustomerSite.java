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
 * 客户地点
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
@ApiModel("客户地点")

@ModifyAudit

@MultiLanguage
@Table(name = "mt_customer_site")
@CustomPrimary
public class MtCustomerSite extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CUSTOMER_SITE_ID = "customerSiteId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_SITE_CODE = "customerSiteCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SITE_USE_TYPE = "siteUseType";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_PROVINCE = "province";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_COUNTY = "county";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_PERSON = "person";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_SOURCE_IDENTIFICATION_ID = "sourceIdentificationId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -7721887729484020651L;

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
    private String customerSiteId;
    @ApiModelProperty(value = "客户ID", required = true)
    @NotBlank
    @Where
    private String customerId;
    @ApiModelProperty(value = "客户地点编码", required = true)
    @NotBlank
    @Where
    private String customerSiteCode;
    @ApiModelProperty(value = "客户地点名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "客户地点类型")
    @Where
    private String siteUseType;
    @ApiModelProperty(value = "国家")
    @MultiLanguageField
    @Where
    private String country;
    @ApiModelProperty(value = "省")
    @MultiLanguageField
    @Where
    private String province;
    @ApiModelProperty(value = "市")
    @MultiLanguageField
    @Where
    private String city;
    @ApiModelProperty(value = "区")
    @MultiLanguageField
    @Where
    private String county;
    @ApiModelProperty(value = "详细地址")
    @MultiLanguageField
    @Where
    private String address;
    @ApiModelProperty(value = "电话")
    @Where
    private String phone;
    @ApiModelProperty(value = "联系人")
    @MultiLanguageField
    @Where
    private String person;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效时间")
    @Where
    private Date dateTo;
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
    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    /**
     * @return 客户ID
     */
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 客户地点编码
     */
    public String getCustomerSiteCode() {
        return customerSiteCode;
    }

    public void setCustomerSiteCode(String customerSiteCode) {
        this.customerSiteCode = customerSiteCode;
    }

    /**
     * @return 客户地点名称
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 客户地点类型
     */
    public String getSiteUseType() {
        return siteUseType;
    }

    public void setSiteUseType(String siteUseType) {
        this.siteUseType = siteUseType;
    }

    /**
     * @return 国家
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return 省
     */
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return 市
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return 区
     */
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * @return 详细地址
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return 电话
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return 联系人
     */
    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
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
