package tarzan.instruction.domain.entity;

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
 * 指令单据头历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@ApiModel("指令单据头历史表")

@ModifyAudit

@Table(name = "mt_instruction_doc_his")
@CustomPrimary
public class MtInstructionDocHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_DOC_HIS_ID = "instructionDocHisId";
    public static final String FIELD_EVENT_ID = "eventId";
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
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 7274269628830280708L;

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
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @Where
    private String instructionDocHisId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "主键ID，表示唯一一条记录", required = true)
    @NotBlank
    @Where
    private String instructionDocId;
    @ApiModelProperty(value = "单据编号", required = true)
    @NotBlank
    @Where
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型（业务类型，由项目定义）", required = true)
    @NotBlank
    @Where
    private String instructionDocType;
    @ApiModelProperty(value = "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")
    @Where
    private String instructionDocStatus;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "供应商ID")
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商地点ID")
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "客户ID")
    @Where
    private String customerId;
    @ApiModelProperty(value = "客户地点ID")
    @Where
    private String customerSiteId;
    @ApiModelProperty(value = "来源ERP订单类型（PO，SO）")
    @Where
    private String sourceOrderType;
    @ApiModelProperty(value = "订单ID（采购订单/销售订单")
    @Where
    private String sourceOrderId;
    @ApiModelProperty(value = "需求时间")
    @Where
    private Date demandTime;
    @ApiModelProperty(value = "预计送达时间")
    @Where
    private Date expectedArrivalTime;
    @ApiModelProperty(value = "成本中心或账户别名")
    @Where
    private String costCenterId;
    @ApiModelProperty(value = "申请人/领料人")
    @Where
    private Long personId;
    @ApiModelProperty(value = "实际业务需要的单据编号")
    @Where
    private String identification;
    @ApiModelProperty(value = "备注")
    @Where
    private String remark;
    @ApiModelProperty(value = "原因")
    @Where
    private String reason;
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
     * @return 主键ID，表示唯一一条记录
     */
    public String getInstructionDocHisId() {
        return instructionDocHisId;
    }

    public void setInstructionDocHisId(String instructionDocHisId) {
        this.instructionDocHisId = instructionDocHisId;
    }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return 主键ID，表示唯一一条记录
     */
    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    /**
     * @return 单据编号
     */
    public String getInstructionDocNum() {
        return instructionDocNum;
    }

    public void setInstructionDocNum(String instructionDocNum) {
        this.instructionDocNum = instructionDocNum;
    }

    /**
     * @return 单据类型（业务类型，由项目定义）
     */
    public String getInstructionDocType() {
        return instructionDocType;
    }

    public void setInstructionDocType(String instructionDocType) {
        this.instructionDocType = instructionDocType;
    }

    /**
     * @return 状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）
     */
    public String getInstructionDocStatus() {
        return instructionDocStatus;
    }

    public void setInstructionDocStatus(String instructionDocStatus) {
        this.instructionDocStatus = instructionDocStatus;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 供应商ID
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 供应商地点ID
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    /**
     * @return 客户ID
     */
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 客户地点ID
     */
    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    /**
     * @return 来源ERP订单类型（PO，SO）
     */
    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    /**
     * @return 订单ID（采购订单/销售订单
     */
    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    /**
     * @return 需求时间
     */
    public Date getDemandTime() {
        if (demandTime != null) {
            return (Date) demandTime.clone();
        } else {
            return null;
        }
    }

    public void setDemandTime(Date demandTime) {
        if (demandTime == null) {
            this.demandTime = null;
        } else {
            this.demandTime = (Date) demandTime.clone();
        }
    }

    public Date getExpectedArrivalTime() {
        if (expectedArrivalTime != null) {
            return (Date) expectedArrivalTime.clone();
        } else {
            return null;
        }
    }

    public void setExpectedArrivalTime(Date expectedArrivalTime) {
        if (expectedArrivalTime == null) {
            this.expectedArrivalTime = null;
        } else {
            this.expectedArrivalTime = (Date) expectedArrivalTime.clone();
        }
    }

    /**
     * @return 成本中心或账户别名
     */
    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return 申请人/领料人
     */
    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * @return 实际业务需要的单据编号
     */
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 原因
     */
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
