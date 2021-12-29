package tarzan.inventory.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 库存保留日记账
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@ApiModel("库存保留日记账")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_inv_onhand_hold_journal")
@CustomPrimary
public class MtInvOnhandHoldJournal extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ONHAND_HOLD_JOURNAL_ID = "onhandHoldJournalId";
    public static final String FIELD_ONHAND_HOLD_ID = "onhandHoldId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOT_CODE = "lotCode";
    public static final String FIELD_HOLD_QUANTITY = "holdQuantity";
    public static final String FIELD_HOLD_TYPE = "holdType";
    public static final String FIELD_ORDER_TYPE = "orderType";
    public static final String FIELD_ORDER_ID = "orderId";
    public static final String FIELD_CHANGE_QUANTITY = "changeQuantity";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_EVENT_TIME = "eventTime";
    public static final String FIELD_EVENT_BY = "eventBy";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 9041793661916409977L;

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
    @ApiModelProperty("主键ID")
    @Id
    @Where
    private String onhandHoldJournalId;
    @ApiModelProperty(value = "库存保留主键ID", required = true)
    @NotBlank
    @Where
    private String onhandHoldId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "库位ID", required = true)
    @NotBlank
    @Where
    private String locatorId;
    @ApiModelProperty(value = "批次CODE")
    @Where
    private String lotCode;
    @ApiModelProperty(value = "保留数量", required = true)
    @NotNull
    @Where
    private Double holdQuantity;
    @ApiModelProperty(value = "预留类型（手工/指令）", required = true)
    @NotBlank
    @Where
    private String holdType;
    @ApiModelProperty(value = "指令类型")
    @Where
    private String orderType;
    @ApiModelProperty(value = "指令ID")
    @Where
    private String orderId;
    @ApiModelProperty(value = "库存变化数量", required = true)
    @NotNull
    @Where
    private Double changeQuantity;
    @ApiModelProperty(
                    value = "所有者类型\r\n包含：\r\nCUSTOMER INVENTORY:客户库存\r\nORDER INVENTORY:销售订单库存\r\nSUPPLIER INVENTORY:供应商寄售\r\nINVENTORY IN SUPPLIER:带料外协，跟踪至发给供应商的库存\r\nPROJECT INVENTORY:按项目管理的库存\r\nINVENTORY IN CUSTOMER:销售寄售，按客户消耗结算\r\n空：表示自有")
    @Where
    private String ownerType;
    @ApiModelProperty(value = "所有者ID（客户ID或供应商ID）")
    @Where
    private String ownerId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @ApiModelProperty(value = "事件时间", required = true)
    @NotNull
    @Where
    private Date eventTime;
    @ApiModelProperty(value = "创建人", required = true)
    @NotNull
    @Where
    private Long eventBy;
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
     * @return 主键ID
     */
    public String getOnhandHoldJournalId() {
        return onhandHoldJournalId;
    }

    public void setOnhandHoldJournalId(String onhandHoldJournalId) {
        this.onhandHoldJournalId = onhandHoldJournalId;
    }

    /**
     * @return 库存保留主键ID
     */
    public String getOnhandHoldId() {
        return onhandHoldId;
    }

    public void setOnhandHoldId(String onhandHoldId) {
        this.onhandHoldId = onhandHoldId;
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
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 库位ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 批次CODE
     */
    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    /**
     * @return 保留数量
     */
    public Double getHoldQuantity() {
        return holdQuantity;
    }

    public void setHoldQuantity(Double holdQuantity) {
        this.holdQuantity = holdQuantity;
    }

    /**
     * @return 预留类型（手工/指令）
     */
    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    /**
     * @return 指令类型
     */
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * @return 指令ID
     */
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return 库存变化数量
     */
    public Double getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(Double changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    /**
     * @return 所有者类型\r\n包含：\r\nCUSTOMER INVENTORY:客户库存\r\nORDER INVENTORY:销售订单库存\r\nSUPPLIER
     *         INVENTORY:供应商寄售\r\nINVENTORY IN SUPPLIER:带料外协，跟踪至发给供应商的库存\r\nPROJECT
     *         INVENTORY:按项目管理的库存\r\nINVENTORY IN CUSTOMER:销售寄售，按客户消耗结算\r\n空：表示自有
     */
    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * @return 所有者ID（客户ID或供应商ID）
     */
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
     * @return 事件时间
     */
    public Date getEventTime() {
        if (eventTime != null) {
            return (Date) eventTime.clone();
        } else {
            return null;
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }

    /**
     * @return 创建人
     */
    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
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
