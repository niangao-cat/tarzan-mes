package tarzan.instruction.domain.entity;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.common.query.Where;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 指令单据头表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@ApiModel("指令单据头表")

@ModifyAudit

@Table(name = "mt_instruction_doc")
@CustomPrimary
public class MtInstructionDoc extends AuditDomain implements Serializable {

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
    private static final long serialVersionUID = -2990406647383118613L;

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
    private String instructionDocId;
    @ApiModelProperty(value = "单据编号", required = true)
    @NotBlank
    @Where
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型（业务类型，由项目定义）", required = true)
    @NotBlank
    @Where
    @LovValue(value = "WX.WMS.SO_DOC_TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;
    @ApiModelProperty(value = "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")
    @Where
    @LovValue(value = "WX.WMS.SO_DELIVERY_STATUS", meaningField = "instructionDocStatusMeaning")
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
    @ApiModelProperty(value = "标记")
    private String mark;
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
    @ApiModelProperty(value = "最新一次新增历史表的主键")
    @Where
    private String latestHisId;
    @Cid
    @Where
    private Long cid;

    @ApiModelProperty(value = "接口状态，成功或者失败")
    private String interfaceFlag;

    @ApiModelProperty(value = "接口信息，成功或者失败")
    private String interfaceMsg;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    @Transient
    @ApiModelProperty(value = "instructionDocType含义")
    private String instructionDocTypeMeaning;
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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
     * @return 最新一次新增历史表的主键
     */
    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getInterfaceFlag() {
        return interfaceFlag;
    }

    public void setInterfaceFlag(String interfaceFlag) {
        this.interfaceFlag = interfaceFlag;
    }

    public String getInterfaceMsg() {
        return interfaceMsg;
    }

    public void setInterfaceMsg(String interfaceMsg) {
        this.interfaceMsg = interfaceMsg;
    }

    public String getInstructionDocTypeMeaning() {
        return instructionDocTypeMeaning;
    }

    public void setInstructionDocTypeMeaning(String instructionDocTypeMeaning) {
        this.instructionDocTypeMeaning = instructionDocTypeMeaning;
    }
}
