package tarzan.order.domain.entity;

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
 * 生产指令历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@ApiModel("生产指令历史")

@ModifyAudit

@Table(name = "mt_work_order_his")
@CustomPrimary
public class MtWorkOrderHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORK_ORDER_HIS_ID = "workOrderHisId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_WORK_ORDER_TYPE = "workOrderType";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_PRODUCTION_LINE_ID = "productionLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_MAKE_ORDER_ID = "makeOrderId";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_LAST_WO_STATUS = "lastWoStatus";
    public static final String FIELD_PLAN_START_TIME = "planStartTime";
    public static final String FIELD_PLAN_END_TIME = "planEndTime";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_VALIDATE_FLAG = "validateFlag";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_OPPORTUNITY_ID = "opportunityId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_COMPLETE_CONTROL_TYPE = "completeControlType";
    public static final String FIELD_COMPLETE_CONTROL_QTY = "completeControlQty";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_TRX_QTY = "trxQty";
    public static final String FIELD_SOURCE_IDENTIFICATION_ID = "sourceIdentificationId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -7579501446116498415L;

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
    @ApiModelProperty("主键ID，标识唯一一条数据")
    @Id
    @Where
    private String workOrderHisId;
    @ApiModelProperty(value = "生产指令ID", required = true)
    @NotBlank
    @Where
    private String workOrderId;
    @ApiModelProperty(value = "生产指令编码", required = true)
    @NotBlank
    @Where
    private String workOrderNum;
    @ApiModelProperty(value = "生产指令类型", required = true)
    @NotBlank
    @Where
    private String workOrderType;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "生产线ID", required = true)
    @NotBlank
    @Where
    private String productionLineId;
    @ApiModelProperty(value = "工作单元ID")
    @Where
    private String workcellId;
    @ApiModelProperty(value = "制造订单ID")
    @Where
    private String makeOrderId;
    @ApiModelProperty(value = "生产版本")
    @Where
    private String productionVersion;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "单位", required = true)
    @NotBlank
    @Where
    private String uomId;
    @ApiModelProperty(value = "优先级")
    @Where
    private Long priority;
    @ApiModelProperty(value = "生产指令状态", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "前次指令状态")
    @Where
    private String lastWoStatus;
    @ApiModelProperty(value = "计划开始时间", required = true)
    @NotNull
    @Where
    private Date planStartTime;
    @ApiModelProperty(value = "计划结束时间", required = true)
    @NotNull
    @Where
    private Date planEndTime;
    @ApiModelProperty(value = "默认完工库位ID，表示唯一货位")
    @Where
    private String locatorId;
    @ApiModelProperty(value = "装配清单ID")
    @Where
    private String bomId;
    @ApiModelProperty(value = "工艺路线ID")
    @Where
    private String routerId;
    @ApiModelProperty(value = "WO验证通过标记（Y/N）", required = true)
    @NotBlank
    @Where
    private String validateFlag;
    @ApiModelProperty(value = "备注")
    @Where
    private String remark;
    @ApiModelProperty(value = "机会订单ID")
    @Where
    private String opportunityId;
    @ApiModelProperty(value = "生产指令对应客户")
    @Where
    private String customerId;
    @ApiModelProperty(value = "完工限制类型")
    @Where
    private String completeControlType;
    @ApiModelProperty(value = "完工限制值")
    @Where
    private Double completeControlQty;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "影响数量")
    @Where
    private Double trxQty;
    @ApiModelProperty(value = "外部来源标识Id")
    @Where
    private Double sourceIdentificationId;
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
     * @return 主键ID，标识唯一一条数据
     */
    public String getWorkOrderHisId() {
        return workOrderHisId;
    }

    public void setWorkOrderHisId(String workOrderHisId) {
        this.workOrderHisId = workOrderHisId;
    }

    /**
     * @return 生产指令ID
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return 生产指令编码
     */
    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    /**
     * @return 生产指令类型
     */
    public String getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(String workOrderType) {
        this.workOrderType = workOrderType;
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
     * @return 生产线ID
     */
    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    /**
     * @return 工作单元ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 制造订单ID
     */
    public String getMakeOrderId() {
        return makeOrderId;
    }

    public void setMakeOrderId(String makeOrderId) {
        this.makeOrderId = makeOrderId;
    }

    /**
     * @return 生产版本
     */
    public String getProductionVersion() {
        return productionVersion;
    }

    public void setProductionVersion(String productionVersion) {
        this.productionVersion = productionVersion;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 数量
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return 单位
     */
    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    /**
     * @return 优先级
     */
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    /**
     * @return 生产指令状态
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 前次指令状态
     */
    public String getLastWoStatus() {
        return lastWoStatus;
    }

    public void setLastWoStatus(String lastWoStatus) {
        this.lastWoStatus = lastWoStatus;
    }

    public Date getPlanStartTime() {
        if (planStartTime != null) {
            return (Date) planStartTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime != null) {
            return (Date) planEndTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
    }

    /**
     * @return 默认完工库位ID，表示唯一货位
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 装配清单ID
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    /**
     * @return 工艺路线ID
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return WO验证通过标记（Y/N）
     */
    public String getValidateFlag() {
        return validateFlag;
    }

    public void setValidateFlag(String validateFlag) {
        this.validateFlag = validateFlag;
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
     * @return 机会订单ID
     */
    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    /**
     * @return 生产指令对应客户
     */
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 完工限制类型
     */
    public String getCompleteControlType() {
        return completeControlType;
    }

    public void setCompleteControlType(String completeControlType) {
        this.completeControlType = completeControlType;
    }

    /**
     * @return 完工限制值
     */
    public Double getCompleteControlQty() {
        return completeControlQty;
    }

    public void setCompleteControlQty(Double completeControlQty) {
        this.completeControlQty = completeControlQty;
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
     * @return 影响数量
     */
    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }

    /**
     * @return 外部来源标识Id
     */
    public Double getSourceIdentificationId() {
        return sourceIdentificationId;
    }

    public void setSourceIdentificationId(Double sourceIdentificationId) {
        this.sourceIdentificationId = sourceIdentificationId;
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
