package tarzan.iface.domain.entity;

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
 * 采购订单计划发运行
 *
 * @author yiyang.xie@hand-china.com 2019-10-08 19:52:47
 */
@ApiModel("采购订单计划发运行")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_po_line")
@CustomPrimary
public class MtPoLine extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PO_LINE_ID = "poLineId";
    public static final String FIELD_PO_HEADER_ID = "poHeaderId";
    public static final String FIELD_LINE_NUM = "lineNum";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_UNIT_PRICE = "unitPrice";
    public static final String FIELD_LINE_TYPE = "lineType";
    public static final String FIELD_LINE_DESCRIPTION = "lineDescription";
    public static final String FIELD_EXPIRATION_DATE = "expirationDate";
    public static final String FIELD_QUANTITY_ORDERED = "quantityOrdered";
    public static final String FIELD_QUANTITY_RECEIVED = "quantityReceived";
    public static final String FIELD_QUANTITY_ACCEPTED = "quantityAccepted";
    public static final String FIELD_QUANTITY_DELIVERED = "quantityDelivered";
    public static final String FIELD_QUANTITY_CANCELLED = "quantityCancelled";
    public static final String FIELD_CANCEL_FLAG = "cancelFlag";
    public static final String FIELD_CLOSED_FLAG = "closedFlag";
    public static final String FIELD_NEED_BY_DATE = "needByDate";
    public static final String FIELD_CONSIGNED_FLAG = "consignedFlag";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_ERP_RECEIVING_ROUTING_ID = "erpReceivingRoutingId";
    public static final String FIELD_ERP_PO_HEADER_ID = "erpPoHeaderId";
    public static final String FIELD_ERP_PO_NUM = "erpPoNum";
    public static final String FIELD_ERP_PO_LINE_ID = "erpPoLineId";
    public static final String FIELD_ERP_PO_LINE_NUM = "erpPoLineNum";
    public static final String FIELD_ERP_PO_LOCATION_ID = "erpPoLocationId";
    public static final String FIELD_ERP_PO_SHIPMENT_NUM = "erpPoShipmentNum";
    public static final String FIELD_ERP_PO_DISTRIBUTION_ID = "erpPoDistributionId";
    public static final String FIELD_ERP_PO_DISTRIBUTION_NUM = "erpPoDistributionNum";
    public static final String FIELD_ERP_PO_RELEASE_ID = "erpPoReleaseId";
    public static final String FIELD_ERP_PO_RELEASE_NUM = "erpPoReleaseNum";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -6267097402670726226L;

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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String poLineId;
    @ApiModelProperty(value = "PO头ID", required = true)
    @NotBlank
    @Where
    private String poHeaderId;
    @ApiModelProperty(value = "采购订单行号", required = true)
    @NotBlank
    @Where
    private String lineNum;
    @ApiModelProperty(value = "制造站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料编码ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "单位ID", required = true)
    @NotBlank
    @Where
    private String uomId;
    @ApiModelProperty(value = "单价")
    @Where
    private Double unitPrice;
    @ApiModelProperty(value = "行类型")
    @Where
    private String lineType;
    @ApiModelProperty(value = "行说明")
    @Where
    private String lineDescription;
    @ApiModelProperty(value = "超期日（一揽子协议的分段日期价格）")
    @Where
    private Date expirationDate;
    @ApiModelProperty(value = "订单行数量", required = true)
    @NotNull
    @Where
    private Double quantityOrdered;
    @ApiModelProperty(value = "接收数量")
    @Where
    private Double quantityReceived;
    @ApiModelProperty(value = "检验合格数量")
    @Where
    private Double quantityAccepted;
    @ApiModelProperty(value = "入库数量")
    @Where
    private Double quantityDelivered;
    @ApiModelProperty(value = "取消数量")
    @Where
    private Double quantityCancelled;
    @ApiModelProperty(value = "取消标识")
    @Where
    private String cancelFlag;
    @ApiModelProperty(value = "关闭标识")
    @Where
    private String closedFlag;
    @ApiModelProperty(value = "需要日期", required = true)
    @NotNull
    @Where
    private Date needByDate;
    @ApiModelProperty(value = "寄售标识")
    @Where
    private String consignedFlag;
    @ApiModelProperty(value = "TARZAN 工单ID")
    @Where
    private String workOrderId;
    @ApiModelProperty(value = "ERP接收方式（标准接收，检验接受，直接接收）")
    @Where
    private String erpReceivingRoutingId;
    @ApiModelProperty(value = "ERP订单头ID")
    @Where
    private String erpPoHeaderId;
    @ApiModelProperty(value = "ERP订单行号")
    @Where
    private String erpPoNum;
    @ApiModelProperty(value = "ERP订单行ID")
    @Where
    private String erpPoLineId;
    @ApiModelProperty(value = "ERP订单行号")
    @Where
    private String erpPoLineNum;
    @ApiModelProperty(value = "ERP发运行ID")
    @Where
    private String erpPoLocationId;
    @ApiModelProperty(value = "ERP发运行号")
    @Where
    private String erpPoShipmentNum;
    @ApiModelProperty(value = "ERP分配行ID")
    @Where
    private String erpPoDistributionId;
    @ApiModelProperty(value = "ERP分配行号")
    @Where
    private String erpPoDistributionNum;
    @ApiModelProperty(value = "ERP一揽子发放ID")
    @Where
    private String erpPoReleaseId;
    @ApiModelProperty(value = "ERP一揽子发放行号")
    @Where
    private String erpPoReleaseNum;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getPoLineId() {
        return poLineId;
    }

    public void setPoLineId(String poLineId) {
        this.poLineId = poLineId;
    }

    /**
     * @return PO头ID
     */
    public String getPoHeaderId() {
        return poHeaderId;
    }

    public void setPoHeaderId(String poHeaderId) {
        this.poHeaderId = poHeaderId;
    }

    /**
     * @return 采购订单行号
     */
    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    /**
     * @return 制造站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 物料编码ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 单位ID
     */
    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    /**
     * @return 单价
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return 行类型
     */
    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    /**
     * @return 行说明
     */
    public String getLineDescription() {
        return lineDescription;
    }

    public void setLineDescription(String lineDescription) {
        this.lineDescription = lineDescription;
    }

    /**
     * @return 超期日（一揽子协议的分段日期价格）
     */
    public Date getExpirationDate() {
        if (expirationDate != null) {
            return (Date) expirationDate.clone();
        } else {
            return null;
        }
    }

    public void setExpirationDate(Date expirationDate) {
        if (expirationDate == null) {
            this.expirationDate = null;
        } else {
            this.expirationDate = (Date) expirationDate.clone();
        }
    }

    /**
     * @return 订单行数量
     */
    public Double getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Double quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    /**
     * @return 接收数量
     */
    public Double getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(Double quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    /**
     * @return 检验合格数量
     */
    public Double getQuantityAccepted() {
        return quantityAccepted;
    }

    public void setQuantityAccepted(Double quantityAccepted) {
        this.quantityAccepted = quantityAccepted;
    }

    /**
     * @return 入库数量
     */
    public Double getQuantityDelivered() {
        return quantityDelivered;
    }

    public void setQuantityDelivered(Double quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    /**
     * @return 取消数量
     */
    public Double getQuantityCancelled() {
        return quantityCancelled;
    }

    public void setQuantityCancelled(Double quantityCancelled) {
        this.quantityCancelled = quantityCancelled;
    }

    /**
     * @return 取消标识
     */
    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    /**
     * @return 关闭标识
     */
    public String getClosedFlag() {
        return closedFlag;
    }

    public void setClosedFlag(String closedFlag) {
        this.closedFlag = closedFlag;
    }

    /**
     * @return 需要日期
     */
    public Date getNeedByDate() {
        if (needByDate != null) {
            return (Date) needByDate.clone();
        } else {
            return null;
        }
    }

    public void setNeedByDate(Date needByDate) {
        if (needByDate == null) {
            this.needByDate = null;
        } else {
            this.needByDate = (Date) needByDate.clone();
        }
    }

    /**
     * @return 寄售标识
     */
    public String getConsignedFlag() {
        return consignedFlag;
    }

    public void setConsignedFlag(String consignedFlag) {
        this.consignedFlag = consignedFlag;
    }

    /**
     * @return TARZAN 工单ID
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return ERP接收方式（标准接收，检验接受，直接接收）
     */
    public String getErpReceivingRoutingId() {
        return erpReceivingRoutingId;
    }

    public void setErpReceivingRoutingId(String erpReceivingRoutingId) {
        this.erpReceivingRoutingId = erpReceivingRoutingId;
    }

    /**
     * @return ERP订单头ID
     */
    public String getErpPoHeaderId() {
        return erpPoHeaderId;
    }

    public void setErpPoHeaderId(String erpPoHeaderId) {
        this.erpPoHeaderId = erpPoHeaderId;
    }

    /**
     * @return ERP订单行号
     */
    public String getErpPoNum() {
        return erpPoNum;
    }

    public void setErpPoNum(String erpPoNum) {
        this.erpPoNum = erpPoNum;
    }

    /**
     * @return ERP订单行ID
     */
    public String getErpPoLineId() {
        return erpPoLineId;
    }

    public void setErpPoLineId(String erpPoLineId) {
        this.erpPoLineId = erpPoLineId;
    }

    /**
     * @return ERP订单行号
     */
    public String getErpPoLineNum() {
        return erpPoLineNum;
    }

    public void setErpPoLineNum(String erpPoLineNum) {
        this.erpPoLineNum = erpPoLineNum;
    }

    /**
     * @return ERP发运行ID
     */
    public String getErpPoLocationId() {
        return erpPoLocationId;
    }

    public void setErpPoLocationId(String erpPoLocationId) {
        this.erpPoLocationId = erpPoLocationId;
    }

    /**
     * @return ERP发运行号
     */
    public String getErpPoShipmentNum() {
        return erpPoShipmentNum;
    }

    public void setErpPoShipmentNum(String erpPoShipmentNum) {
        this.erpPoShipmentNum = erpPoShipmentNum;
    }

    /**
     * @return ERP分配行ID
     */
    public String getErpPoDistributionId() {
        return erpPoDistributionId;
    }

    public void setErpPoDistributionId(String erpPoDistributionId) {
        this.erpPoDistributionId = erpPoDistributionId;
    }

    /**
     * @return ERP分配行号
     */
    public String getErpPoDistributionNum() {
        return erpPoDistributionNum;
    }

    public void setErpPoDistributionNum(String erpPoDistributionNum) {
        this.erpPoDistributionNum = erpPoDistributionNum;
    }

    /**
     * @return ERP一揽子发放ID
     */
    public String getErpPoReleaseId() {
        return erpPoReleaseId;
    }

    public void setErpPoReleaseId(String erpPoReleaseId) {
        this.erpPoReleaseId = erpPoReleaseId;
    }

    public String getErpPoReleaseNum() {
        return erpPoReleaseNum;
    }

    public void setErpPoReleaseNum(String erpPoReleaseNum) {
        this.erpPoReleaseNum = erpPoReleaseNum;
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
