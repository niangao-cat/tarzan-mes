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
 * 工序
 *
 * @author kejin.liu@hand-china.com 2020-08-17 10:15:37
 */
@ApiModel("工序")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_operation_iface")
@Data
@CustomPrimary
public class ItfOperationIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_OPERATION_NAME = "operationName";
    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_CURRENT_FLAG = "currentFlag";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_OPERATION_STATUS = "operationStatus";
    public static final String FIELD_OPERATION_TYPE = "operationType";
    public static final String FIELD_SPECIAL_ROUTER_ID = "specialRouterId";
    public static final String FIELD_TOOL_TYPE = "toolType";
    public static final String FIELD_TOOL_ID = "toolId";
    public static final String FIELD_WORKCELL_TYPE = "workcellType";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_DEFAULT_NC_ID = "defaultNcId";
    public static final String FIELD_STANDARD_MAX_LOOP = "standardMaxLoop";
    public static final String FIELD_STANDARD_SPECIAL_INTRODUCTION = "standardSpecialIntroduction";
    public static final String FIELD_STANDARD_REQD_TIME_IN_PROCESS = "standardReqdTimeInProcess";
    public static final String FIELD_COMPLETE_INCONFORMITY_FLAG = "completeInconformityFlag";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
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
    private String operationId;
    @ApiModelProperty(value = "站点标识", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "工艺名称", required = true)
    @NotBlank
    private String operationName;
    @ApiModelProperty(value = "工艺版本", required = true)
    @NotBlank
    private String revision;
    @ApiModelProperty(value = "Y/N，是否当前版本")
    private String currentFlag;
    @ApiModelProperty(value = "有效时间至", required = true)
    @NotNull
    private Date dateFrom;
    @ApiModelProperty(value = "有效时间从")
    private Date dateTo;
    @ApiModelProperty(value = "备注，有条件的话做成多行文本")
    private String description;
    @ApiModelProperty(value = "工艺状态（STATUS_GROUP:OPERATION_STATUS）：1.【NEW】“新建”2.【CAN_RELEASE】“可下达”3.【FREEZE】“冻结”4.【ABANDON】“废弃”5.【HOLD】“保留”6.【NCLIMIT】“保留未关闭NC”", required = true)
    @NotBlank
    private String operationStatus;
    @ApiModelProperty(value = "工艺类型（TYPE_GROUP:OPERATION_TYPE）：1.【NORMAL】“正常”.【SPECIAL】“特殊”3.【TEST】“测试”：此工艺用于测试。", required = true)
    @NotBlank
    private String operationType;
    @ApiModelProperty(value = "特殊工艺路线ID")
    private String specialRouterId;
    @ApiModelProperty(value = "工具类型")
    private String toolType;
    @ApiModelProperty(value = "工具")
    private String toolId;
    @ApiModelProperty(value = "工作单元类型")
    private String workcellType;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;
    @ApiModelProperty(value = "此操作的缺省不合格代码")
    private String defaultNcId;
    @ApiModelProperty(value = "路线上工艺 最大循环次数 的多语言")
    private Long standardMaxLoop;
    @ApiModelProperty(value = "路线步骤的特殊指令REQUIRED_TIME_IN_PROCESS")
    private String standardSpecialIntroduction;
    @ApiModelProperty(value = "占用时间或有效工作时间（按分钟计）")
    private BigDecimal standardReqdTimeInProcess;
    @ApiModelProperty(value = "完工不一致标识")
    private String completeInconformityFlag;
    @ApiModelProperty(value = "批次ID")
    private Long batchId;
    @ApiModelProperty(value = "批次时间")
    private String batchDate;
    @ApiModelProperty(value = "", required = true)
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

    //
    // getter/setter
    // ------------------------------------------------------------------------------



}
