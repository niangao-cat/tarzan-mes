package com.ruike.wms.domain.entity;

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
 * 指令单据头表
 *
 * @author taowen.wang@hand-china.com 2021-07-07 17:18:05
 */
@ApiModel("指令单据头表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_instruction_doc")
@Data
public class WmsInstructionDocs extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_DOC_ID = "instructionDocId";
    public static final String FIELD_INSTRUCTION_DOC_NUM = "instructionDocNum";
    public static final String FIELD_INSTRUCTION_DOC_TYPE = "instructionDocType";
    public static final String FIELD_INSTRUCTION_DOC_STATUS = "instructionDocStatus";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_SITE_ID = "customerSiteId";
    public static final String FIELD_SOURCE_ORDER_TYPE = "sourceOrderType";
    public static final String FIELD_SOURCE_ORDER_ID = "sourceOrderId";
    public static final String FIELD_DEMAND_TIME = "demandTime";
    public static final String FIELD_EXPECTED_ARRIVAL_TIME = "expectedArrivalTime";
    public static final String FIELD_COST_CENTER_ID = "costCenterId";
    public static final String FIELD_PERSON_ID = "personId";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_REASON = "reason";
    public static final String FIELD_LATEST_HIS_ID = "latestHisId";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_MARK = "mark";
    public static final String FIELD_INTERFACE_FLAG = "interfaceFlag";
    public static final String FIELD_INTERFACE_MSG = "interfaceMsg";

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @GeneratedValue
    private String instructionDocId;
    @ApiModelProperty(value = "单据编号", required = true)
    @NotBlank
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型（业务类型，由项目定义）", required = true)
    @NotBlank
    private String instructionDocType;
    @ApiModelProperty(value = "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")
    private String instructionDocStatus;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商地点ID")
    private String supplierSiteId;
    @ApiModelProperty(value = "客户ID")
    private String customerId;
    @ApiModelProperty(value = "客户地点ID")
    private String customerSiteId;
    @ApiModelProperty(value = "来源ERP订单类型（PO，SO）")
    private String sourceOrderType;
    @ApiModelProperty(value = "订单ID（采购订单/销售订单")
    private String sourceOrderId;
    @ApiModelProperty(value = "需求时间")
    private Date demandTime;
    @ApiModelProperty(value = "预计送达时间")
    private Date expectedArrivalTime;
    @ApiModelProperty(value = "成本中心或账户别名")
    private String costCenterId;
    @ApiModelProperty(value = "申请人/领料人")
    private Long personId;
    @ApiModelProperty(value = "实际业务需要的单据编号")
    private String identification;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "原因")
    private String reason;
    @ApiModelProperty(value = "最新一次新增历史表的主键")
    private String latestHisId;
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
    @ApiModelProperty(value = "标记")
    private String mark;
    @ApiModelProperty(value = "接口状态，成功或者失败")
    private String interfaceFlag;
    @ApiModelProperty(value = "接口信息，成功或者失败")
    private String interfaceMsg;

//
// 非数据库字段
// ------------------------------------------------------------------------------

//
// getter/setter
// ------------------------------------------------------------------------------

}
