package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标准工序接口表
 *
 * @author kejin.liu01@hand-china.com 2020-08-17 21:50:28
 */
@ApiModel("标准工序接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_standard_operation_iface")
@Data
@CustomPrimary
public class ItfStandardOperationIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_OPERATION_CODE = "operationCode";
    public static final String FIELD_OPERATION_DESCRIPTION = "operationDescription";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
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


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "工厂CODE")
    private String plantCode;
    @ApiModelProperty(value = "工序CODE")
    private String operationCode;
    @ApiModelProperty(value = "工序说明")
    private String operationDescription;
    @ApiModelProperty(value = "ERP创建日期", required = true)
    @NotNull
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人", required = true)
    @NotNull
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人", required = true)
    @NotNull
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期", required = true)
    @NotNull
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    private String message;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    private Long cid;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long objectVersionNumber;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long createdBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date creationDate;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date lastUpdateDate;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
