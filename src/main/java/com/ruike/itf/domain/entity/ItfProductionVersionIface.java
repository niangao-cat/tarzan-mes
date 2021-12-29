package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 生产版本表
 *
 * @author kejin.liu01@hand-china.com 2020-08-20 12:21:46
 */
@ApiModel("生产版本表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_production_version_iface")
@Data
public class ItfProductionVersionIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_BOM_NAME = "bomName";
    public static final String FIELD_BOM_VERSION = "bomVersion";
    public static final String FIELD_ROUTER_NAME = "routerName";
    public static final String FIELD_ROUTER_VERSION = "routerVersion";
    public static final String FIELD_LOCK_FLAG = "lockFlag";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
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
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("KEY")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "物料站点")
    private String materialCode;
    @ApiModelProperty(value = "工厂")
    private String siteCode;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "生产版本短文本")
    private String description;
    @ApiModelProperty(value = "Bom编号")
    private String bomName;
    @ApiModelProperty(value = "bom版本")
    private String bomVersion;
    @ApiModelProperty(value = "工艺路线号")
    private String routerName;
    @ApiModelProperty(value = "工艺路线版本")
    private String routerVersion;
    @ApiModelProperty(value = "锁定标识")
    private String lockFlag;
    @ApiModelProperty(value = "有效期开始日期")
    private Date dateFrom;
    @ApiModelProperty(value = "有效期结束日期")
    private Date dateTo;
    @ApiModelProperty(value = "批次ID")
    private Long batchId;
    @ApiModelProperty(value = "批次时间")
    private String batchDate;
    @ApiModelProperty(value = "错误信息")
    private String message;
    @ApiModelProperty(value = "CID")
    @NotNull
    private Long cid;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
