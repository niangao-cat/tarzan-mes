package tarzan.iface.domain.entity;

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
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 供应商数据接口表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@ApiModel("供应商数据接口表")

@ModifyAudit

@Table(name = "mt_supplier_iface")
@CustomPrimary
public class MtSupplierIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_NAME = "supplierName";
    public static final String FIELD_SUPPLIER_NAME_ALT = "supplierNameAlt";
    public static final String FIELD_SUPPLIER_DATE_FROM = "supplierDateFrom";
    public static final String FIELD_SUPPLIER_DATE_TO = "supplierDateTo";
    public static final String FIELD_SUPPLIER_TYPE = "supplierType";
    public static final String FIELD_SUPPLIER_SITE_CODE = "supplierSiteCode";
    public static final String FIELD_SUPPLIER_SITE_ADDRESS = "supplierSiteAddress";
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
    private static final long serialVersionUID = -7140535750661765714L;

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
    @ApiModelProperty("供应商Id")
    @Where
    private Double supplierId;
    @ApiModelProperty(value = "供应商代码", required = true)
    @NotBlank
    @Where
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称", required = true)
    @NotBlank
    @Where
    private String supplierName;
    @ApiModelProperty(value = "供应商简称")
    @Where
    private String supplierNameAlt;
    @ApiModelProperty(value = "生效日期从", required = true)
    @NotNull
    @Where
    private Date supplierDateFrom;
    @ApiModelProperty(value = "失效日期至")
    @Where
    private Date supplierDateTo;
    @ApiModelProperty(value = "供应商类型")
    @Where
    private String supplierType;
    @ApiModelProperty(value = "供应商地点Id")
    @Where
    private Double supplierSiteId;
    @ApiModelProperty(value = "供应商地点编号")
    @Where
    private String supplierSiteCode;
    @ApiModelProperty(value = "供应商详细地址", required = true)
    @NotBlank
    @Where
    private String supplierSiteAddress;
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
     * @return 供应商Id
     */
    public Double getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Double supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 供应商代码
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

    /**
     * @return 生效日期从
     */
    public Date getSupplierDateFrom() {
        if (supplierDateFrom != null) {
            return (Date) supplierDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setSupplierDateFrom(Date supplierDateFrom) {
        if (supplierDateFrom == null) {
            this.supplierDateFrom = null;
        } else {
            this.supplierDateFrom = (Date) supplierDateFrom.clone();
        }
    }

    public Date getSupplierDateTo() {
        if (supplierDateTo != null) {
            return (Date) supplierDateTo.clone();
        } else {
            return null;
        }
    }

    public void setSupplierDateTo(Date supplierDateTo) {
        if (supplierDateTo == null) {
            this.supplierDateTo = null;
        } else {
            this.supplierDateTo = (Date) supplierDateTo.clone();
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
     * @return 供应商地点Id
     */
    public Double getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(Double supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    /**
     * @return 供应商地点编号
     */
    public String getSupplierSiteCode() {
        return supplierSiteCode;
    }

    public void setSupplierSiteCode(String supplierSiteCode) {
        this.supplierSiteCode = supplierSiteCode;
    }

    /**
     * @return 供应商详细地址
     */
    public String getSupplierSiteAddress() {
        return supplierSiteAddress;
    }

    public void setSupplierSiteAddress(String supplierSiteAddress) {
        this.supplierSiteAddress = supplierSiteAddress;
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
