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
 * 工作中心接口记录表
 *
 * @author kejin.liu01@hand-china.com 2020-08-27 16:17:14
 */
@ApiModel("工作中心接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_work_center_iface")
@Data
public class ItfWorkCenterIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_WORK_CENTER = "workCenter";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_END_DATE = "endDate";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_BATCH_ID = "batchId";
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
    private Long ifaceId;
    @ApiModelProperty(value = "工厂ID")
    private String siteCode;
    @ApiModelProperty(value = "工作中心")
    private String workCenter;
    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    @ApiModelProperty(value = "结束时间")
    private Date endDate;
    @ApiModelProperty(value = "数据处理消息返回")
    private String message;
    @ApiModelProperty(value = "批次ID")
    private Long batchId;
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


}
