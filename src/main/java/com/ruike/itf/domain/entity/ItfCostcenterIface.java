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
 * 成本中心数据接口记录表
 *
 * @author kejin.liu01@hand-china.com 2020-08-24 09:19:52
 */
@ApiModel("成本中心数据接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_costcenter_iface")
@Data
public class ItfCostcenterIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_COSTCENTER_CODE = "costcenterCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE_FROM_TO = "dateFromTo";
    public static final String FIELD_DATE_END_TO = "dateEndTo";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

    public static final String FIELD_COSTCENTER_TYPE = "costcenterType";
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
    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "Oracle转换库存组织代码写入, SAP将公司代码转换成工厂代码写入")
    private String plantCode;
    @ApiModelProperty(value = "SAP：成本中心、Oracle账户别名")
    private String costcenterCode;
    @ApiModelProperty(value = "SAP：成本中心描述、Oracle：账户别名描述")
    private String description;
    @ApiModelProperty(value = "地点生效日期")
    private Date dateFromTo;
    @ApiModelProperty(value = "地点失效日期")
    private Date dateEndTo;
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
    @ApiModelProperty(value = "数据批次ID")
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

    @ApiModelProperty(value = "成本中心类型")
    private String costcenterType;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute1;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute2;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute3;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute4;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute5;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute6;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute7;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute8;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute9;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute10;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute11;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute12;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute13;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute14;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
