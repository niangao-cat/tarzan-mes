package com.ruike.hme.domain.entity;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 工单创建
 *
 * @author kejin.liu01@hand-china.com 2020-12-09 10:21:47
 */
@ApiModel("工单创建")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_repair_work_order_create")
@Data
public class HmeRepairWorkOrderCreate extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORK_ORDER_CREATE_ID = "workOrderCreateId";
    public static final String FIELD_MATERIAL_LOT_CODE = "materialLotCode";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_PRIMARY_UOM_CODE = "primaryUomCode";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_LOCATOR_CODE = "locatorCode";
    public static final String FIELD_PLAN_START_TIME = "planStartTime";
    public static final String FIELD_PLAN_END_TIME = "planEndTime";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_PARAMETER = "parameter";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String workOrderCreateId;
    @ApiModelProperty(value = "条码号", required = true)
    @NotBlank
    private String materialLotCode;
    @ApiModelProperty(value = "物料编码", required = true)
    @NotBlank
    private String materialCode;
    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    private Double qty;
    @ApiModelProperty(value = "单位编码", required = true)
    @NotBlank
    private String primaryUomCode;
    @ApiModelProperty(value = "工厂编码", required = true)
    @NotBlank
    private String siteCode;
    @ApiModelProperty(value = "仓库编码", required = true)
    @NotBlank
    private String locatorCode;
    @ApiModelProperty(value = "计划时间从", required = true)
    @NotNull
    private Date planStartTime;
    @ApiModelProperty(value = "计划时间至", required = true)
    @NotNull
    private Date planEndTime;
    @ApiModelProperty(value = "生产版本", required = true)
    @NotBlank
    private String productionVersion;
    @ApiModelProperty(value = "SAP返回生产订单号")
    private String workOrderNum;
    @ApiModelProperty(value = "SAP返回状态，S成功、E错误、W警告、I信息")
    private String status;
    @ApiModelProperty(value = "SAP返回参数")
    private String parameter;
    @ApiModelProperty(value = "SAP返回文本")
    private String message;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long cid;

    @Transient
    private String startTime;

    @Transient
    private String endTime;

    @Transient
    @ApiModelProperty(value = "生产管理员")
    private String productionAdministrator;


}
