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
 * 客户数据全量接口表
 *
 * @author yapeng.yao@hand-china.com 2020-08-21 11:19:59
 */
@ApiModel("客户数据全量接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_customer_iface")
@Data
public class ItfCustomerIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_CODE = "customerCode";
    public static final String FIELD_CUSTOMER_NAME = "customerName";
    public static final String FIELD_CUSTOMER_NAME_ALT = "customerNameAlt";
    public static final String FIELD_CUSTOMER_SITE_CODE = "customerSiteCode";
    public static final String FIELD_CUSTOMER_TYPE = "customerType";
    public static final String FIELD_CUSTOMER_DATE_FROM = "customerDateFrom";
    public static final String FIELD_CUSTOMER_DATE_TO = "customerDateTo";
    public static final String FIELD_CUSTOMER_SITE_NUBMER = "customerSiteNumber";
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
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
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


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty("客户Id")
    private Double customerId;
    @ApiModelProperty(value = "客户编号，Oracle将ACCOUNT_NUMBER写入")
    private String customerCode;
    @ApiModelProperty(value = "客户名称，Oracle将PARTY_NAME写入")
    private String customerName;
    @ApiModelProperty(value = "客户简称")
    private String customerNameAlt;
    @ApiModelProperty(value = "客户地点编码")
    private String customerSiteCode;
    @ApiModelProperty(value = "客户类型")
    private String customerType;
    @ApiModelProperty(value = "客户生效日期")
    private Date customerDateFrom;
    @ApiModelProperty(value = "客户失效日期")
    private Date customerDateTo;
    @ApiModelProperty(value = "客户地点编号：oracle将PARTY_SITE_NUMBER写入该字段")
    private String customerSiteNumber;
    @ApiModelProperty(value = "地点用途。Oracle存在bill_to 和ship_to两种")
    private String siteUseType;
    @ApiModelProperty(value = "客户地址：Oracle将HZ_location表的location写入该字段")
    private String address;
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
    @ApiModelProperty(value = "预留字段")
    private String attribute1;
    @ApiModelProperty(value = "预留字段")
    private String attribute2;
    @ApiModelProperty(value = "预留字段")
    private String attribute3;
    @ApiModelProperty(value = "预留字段")
    private String attribute4;
    @ApiModelProperty(value = "预留字段")
    private String attribute5;
    @ApiModelProperty(value = "预留字段")
    private String attribute6;
    @ApiModelProperty(value = "预留字段")
    private String attribute7;
    @ApiModelProperty(value = "预留字段")
    private String attribute8;
    @ApiModelProperty(value = "预留字段")
    private String attribute9;
    @ApiModelProperty(value = "预留字段")
    private String attribute10;
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