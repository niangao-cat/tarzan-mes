package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 供应商数据接口表
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 18:49:46
 */
@ApiModel("供应商数据接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_supplier_iface")
@Data
public class ItfSupplierIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_NAME = "supplierName";
    public static final String FIELD_SUPPLIER_NAME_ALT = "supplierNameAlt";
    public static final String FIELD_SUPPLIER_DATE_FROM = "supplierDateFrom";
    public static final String FIELD_SUPPLIER_DATE_TO = "supplierDateTo";
    public static final String FIELD_SUPPLIER_TYPE = "supplierType";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
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
    public static final String FIELD_BATCH_Date = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String ifaceId;
   @ApiModelProperty(value = "供应商ID")
    private String supplierId;
   @ApiModelProperty(value = "供应商代码")
    private String supplierCode;
   @ApiModelProperty(value = "供应商名称")    
    private String supplierName;
   @ApiModelProperty(value = "供应商简称")    
    private String supplierNameAlt;
   @ApiModelProperty(value = "生效日期从")    
    private Date supplierDateFrom;
   @ApiModelProperty(value = "失效日期至")    
    private Date supplierDateTo;
   @ApiModelProperty(value = "供应商类型")    
    private String supplierType;
    @ApiModelProperty(value = "供应商地点ID")
    private String supplierSiteId;
   @ApiModelProperty(value = "供应商地点编号")    
    private String supplierSiteCode;
   @ApiModelProperty(value = "供应商详细地址")    
    private String supplierSiteAddress;
   @ApiModelProperty(value = "国家")    
    private String country;
   @ApiModelProperty(value = "省份")    
    private String province;
   @ApiModelProperty(value = "城市")    
    private String city;
   @ApiModelProperty(value = "联系电话")    
    private String contactPhoneNumber;
   @ApiModelProperty(value = "联系人")    
    private String contactPerson;
   @ApiModelProperty(value = "地点生效日期")    
    private Date siteDateFrom;
   @ApiModelProperty(value = "地点失效日期")    
    private Date siteDateTo;
   @ApiModelProperty(value = "ERP创建日期")    
    private Date erpCreationDate;
   @ApiModelProperty(value = "ERP创建人")    
    private Long erpCreatedBy;
   @ApiModelProperty(value = "ERP最后更新人")    
    private Long erpLastUpdatedBy;
   @ApiModelProperty(value = "ERP最后更新日期")    
    private Date erpLastUpdateDate;
   @ApiModelProperty(value = "数据批次ID")    
    private Double batchId;
    @ApiModelProperty(value = "数据批次日期")
    private String batchDate;
   @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P")    
    private String status;
   @ApiModelProperty(value = "数据处理消息返回")    
    private String message;
   @ApiModelProperty(value = "CID")    
    private Long cid;
   @ApiModelProperty(value = "")    
    private Long objectVersionNumber;
   @ApiModelProperty(value = "")    
    private Long createdBy;
   @ApiModelProperty(value = "")    
    private Date creationDate;
   @ApiModelProperty(value = "")    
    private Long lastUpdatedBy;
   @ApiModelProperty(value = "")    
    private Date lastUpdateDate;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

}
