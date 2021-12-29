package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
 * 客户数据接口表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@ApiModel("客户数据接口表")

@ModifyAudit

@Table(name = "mt_customer_iface")
@CustomPrimary
public class MtCustomerIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_CODE = "customerCode";
    public static final String FIELD_CUSTOMER_NAME = "customerName";
    public static final String FIELD_CUSTOMER_NAME_ALT = "customerNameAlt";
    public static final String FIELD_CUSTOMER_TYPE = "customerType";
    public static final String FIELD_CUSTOMER_DATE_FROM = "customerDateFrom";
    public static final String FIELD_CUSTOMER_DATE_TO = "customerDateTo";
    public static final String FIELD_CUSTOMER_SITE_Number = "customerSiteNumber";
    public static final String FIELD_CUSTOMER_SITE_CODE = "customerSiteCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SITE_USE_TYPE = "siteUseType";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_PROVINCE = "province";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_CONTACT_PHONE_NUMBER = "contactPhoneNumber";
    public static final String FIELD_CONTACT_PERSON = "contactPerson";
    public static final String FIELD_SITE_DATE_FROM = "siteDateFrom";
    public static final String FIELD_SITE_DATE_TO = "siteDateTo";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -5579948923445272531L;

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
    private String ifaceId;
    @ApiModelProperty("客户Id")
    private Double customerId;
    @ApiModelProperty(value = "客户编号，Oracle将ACCOUNT_NUMBER写入", required = true)
    @NotBlank
    @Where
    private String customerCode;
    @ApiModelProperty(value = "客户名称，Oracle将PARTY_NAME写入", required = true)
    @NotBlank
    @Where
    private String customerName;
    @ApiModelProperty(value = "客户简称")
    @Where
    private String customerNameAlt;
    @ApiModelProperty(value = "客户类型", required = true)
    @NotBlank
    @Where
    private String customerType;
    @ApiModelProperty(value = "客户生效日期", required = true)
    @NotNull
    @Where
    private Date customerDateFrom;
    @ApiModelProperty(value = "客户失效日期")
    @Where
    private Date customerDateTo;
    @ApiModelProperty(value = "客户地点Id")
    @Where
    private Double customerSiteNumber;
    @ApiModelProperty(value = "客户地点编号", required = true)
    @NotBlank
    @Where
    private String customerSiteCode;
    @ApiModelProperty(value = "客户地点描述")
    @Transient
    private String description;
    @ApiModelProperty(value = "地点用途。Oracle存在bill_to 和ship_to两种", required = true)
    @NotBlank
    @Where
    private String siteUseType;
    @ApiModelProperty(value = "客户地址：Oracle将HZ_location表的location写入该字段", required = true)
    @NotBlank
    @Where
    private String address;
    @ApiModelProperty(value = "国家")
    @Where
    private String country;
    @ApiModelProperty(value = "省份")
    @Where
    private String province;
    @ApiModelProperty(value = "城市")
    @Where
    private String city;
    @ApiModelProperty(value = "联系电话")
    @Where
    private String contactPhoneNumber;
    @ApiModelProperty(value = "联系人")
    @Where
    private String contactPerson;
    @ApiModelProperty(value = "地点生效日期", required = true)
    @NotNull
    @Where
    private Date siteDateFrom;
    @ApiModelProperty(value = "地点失效日期")
    @Where
    private Date siteDateTo;
    @ApiModelProperty(value = "ERP创建日期", required = true)
    @NotNull
    @Where
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人", required = true)
    @NotNull
    @Where
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人", required = true)
    @NotNull
    @Where
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期", required = true)
    @NotNull
    @Where
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
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
    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    /**
     * @return 客户Id
     */
    public Double getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Double customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 客户编号，Oracle将ACCOUNT_NUMBER写入
     */
    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return 客户名称，Oracle将PARTY_NAME写入
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
     * @return 客户生效日期
     */
    public Date getCustomerDateFrom() {
        if (customerDateFrom != null) {
            return (Date) customerDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setCustomerDateFrom(Date customerDateFrom) {
        if (customerDateFrom == null) {
            this.customerDateFrom = null;
        } else {
            this.customerDateFrom = (Date) customerDateFrom.clone();
        }
    }

    public Date getCustomerDateTo() {
        if (customerDateTo != null) {
            return (Date) customerDateTo.clone();
        } else {
            return null;
        }
    }

    public void setCustomerDateTo(Date customerDateTo) {
        if (customerDateTo == null) {
            this.customerDateTo = null;
        } else {
            this.customerDateTo = (Date) customerDateTo.clone();
        }
    }

    /**
     * @return 客户地点Number
     */
    public Double getCustomerSiteNumber() {
        return customerSiteNumber;
    }

    public void setCustomerSiteNumber(Double customerSiteNumber) {
        this.customerSiteNumber = customerSiteNumber;
    }

    /**
     * @return 客户地点编号
     */
    public String getCustomerSiteCode() {
        return customerSiteCode;
    }

    public void setCustomerSiteCode(String customerSiteCode) {
        this.customerSiteCode = customerSiteCode;
    }

    /**
     * @return 客户地点描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 地点用途。Oracle存在bill_to 和ship_to两种
     */
    public String getSiteUseType() {
        return siteUseType;
    }

    public void setSiteUseType(String siteUseType) {
        this.siteUseType = siteUseType;
    }

    /**
     * @return 客户地址：Oracle将HZ_location表的location写入该字段
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
     * @return 省份
     */
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return 城市
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return 联系电话
     */
    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    /**
     * @return 联系人
     */
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * @return 地点生效日期
     */
    public Date getSiteDateFrom() {
        if (siteDateFrom != null) {
            return (Date) siteDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setSiteDateFrom(Date siteDateFrom) {
        if (siteDateFrom == null) {
            this.siteDateFrom = null;
        } else {
            this.siteDateFrom = (Date) siteDateFrom.clone();
        }
    }

    public Date getSiteDateTo() {
        if (siteDateTo != null) {
            return (Date) siteDateTo.clone();
        } else {
            return null;
        }
    }

    public void setSiteDateTo(Date siteDateTo) {
        if (siteDateTo == null) {
            this.siteDateTo = null;
        } else {
            this.siteDateTo = (Date) siteDateTo.clone();
        }
    }

    public Date getErpCreationDate() {
        if (erpCreationDate != null) {
            return (Date) erpCreationDate.clone();
        } else {
            return null;
        }
    }

    public void setErpCreationDate(Date erpCreationDate) {
        if (erpCreationDate == null) {
            this.erpCreationDate = null;
        } else {
            this.erpCreationDate = (Date) erpCreationDate.clone();
        }
    }

    /**
     * @return ERP创建人
     */
    public Long getErpCreatedBy() {
        return erpCreatedBy;
    }

    public void setErpCreatedBy(Long erpCreatedBy) {
        this.erpCreatedBy = erpCreatedBy;
    }

    /**
     * @return ERP最后更新人
     */
    public Long getErpLastUpdatedBy() {
        return erpLastUpdatedBy;
    }

    public void setErpLastUpdatedBy(Long erpLastUpdatedBy) {
        this.erpLastUpdatedBy = erpLastUpdatedBy;
    }

    /**
     * @return ERP最后更新日期
     */
    public Date getErpLastUpdateDate() {
        if (erpLastUpdateDate != null) {
            return (Date) erpLastUpdateDate.clone();
        } else {
            return null;
        }
    }

    public void setErpLastUpdateDate(Date erpLastUpdateDate) {
        if (erpLastUpdateDate == null) {
            this.erpLastUpdateDate = null;
        } else {
            this.erpLastUpdateDate = (Date) erpLastUpdateDate.clone();
        }
    }

    /**
     * @return 数据批次ID
     */
    public Double getBatchId() {
        return batchId;
    }

    public void setBatchId(Double batchId) {
        this.batchId = batchId;
    }

    /**
     * @return 数据处理状态，初始为N，失败为E，成功S，处理中P
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 数据处理消息返回
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
