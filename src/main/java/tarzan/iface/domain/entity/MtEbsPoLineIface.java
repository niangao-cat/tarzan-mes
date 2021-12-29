package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
 * EBS采购订单行接口表
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
@ApiModel("EBS采购订单行接口表")
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_ebs_po_line_iface")
@CustomPrimary
public class MtEbsPoLineIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_PO_HEADER_ID = "poHeaderId";
    public static final String FIELD_LINE_NUM = "lineNum";
    public static final String FIELD_ITEM_ID = "itemId";
    public static final String FIELD_UOM = "uom";
    public static final String FIELD_UNIT_PRICE = "unitPrice";
    public static final String FIELD_LINE_DESCRIPTION = "lineDescription";
    public static final String FIELD_EXPIRATION_DATE = "expirationDate";
    public static final String FIELD_LINE_TPYE = "lineTpye";
    public static final String FIELD_QUANTITY_ORDERED = "quantityOrdered";
    public static final String FIELD_QUANTITY_RECEIVED = "quantityReceived";
    public static final String FIELD_QUANTITY_ACCEPTED = "quantityAccepted";
    public static final String FIELD_QUANTITY_DELIVERED = "quantityDelivered";
    public static final String FIELD_QUANTITY_CANCELLED = "quantityCancelled";
    public static final String FIELD_LINE_CANCEL_FLAG = "lineCancelFlag";
    public static final String FIELD_LINE_CLOSED_CODE = "lineClosedCode";
    public static final String FIELD_LOCATION_CANCEL_FLAG = "locationCancelFlag";
    public static final String FIELD_LOCATION_CLOSED_CODE = "locationClosedCode";
    public static final String FIELD_NEED_BY_DATE = "needByDate";
    public static final String FIELD_CONSIGNED_FLAG = "consignedFlag";
    public static final String FIELD_SHIPMENT_TYPE = "shipmentType";
    public static final String FIELD_RECEIVING_ROUTING_ID = "receivingRoutingId";
    public static final String FIELD_WIP_ENTITY_ID = "wipEntityId";
    public static final String FIELD_PO_LINE_ID = "poLineId";
    public static final String FIELD_PO_LINE_NUM = "poLineNum";
    public static final String FIELD_PO_LOCATION_ID = "poLocationId";
    public static final String FIELD_PO_SHIPMENT_NUM = "poShipmentNum";
    public static final String FIELD_PO_DISTRIBUTION_ID = "poDistributionId";
    public static final String FIELD_PO_DISTRIBUTION_NUM = "poDistributionNum";
    public static final String FIELD_PO_RELEASE_ID = "poReleaseId";
    public static final String FIELD_LINE_ATTRIBUTE1 = "lineAttribute1";
    public static final String FIELD_LINE_ATTRIBUTE2 = "lineAttribute2";
    public static final String FIELD_LINE_ATTRIBUTE3 = "lineAttribute3";
    public static final String FIELD_LINE_ATTRIBUTE4 = "lineAttribute4";
    public static final String FIELD_LINE_ATTRIBUTE5 = "lineAttribute5";
    public static final String FIELD_LINE_ATTRIBUTE6 = "lineAttribute6";
    public static final String FIELD_LINE_ATTRIBUTE7 = "lineAttribute7";
    public static final String FIELD_LINE_ATTRIBUTE8 = "lineAttribute8";
    public static final String FIELD_LINE_ATTRIBUTE9 = "lineAttribute9";
    public static final String FIELD_LINE_ATTRIBUTE10 = "lineAttribute10";
    public static final String FIELD_LINE_ATTRIBUTE11 = "lineAttribute11";
    public static final String FIELD_LINE_ATTRIBUTE12 = "lineAttribute12";
    public static final String FIELD_LINE_ATTRIBUTE13 = "lineAttribute13";
    public static final String FIELD_LINE_ATTRIBUTE14 = "lineAttribute14";
    public static final String FIELD_LINE_ATTRIBUTE15 = "lineAttribute15";
    public static final String FIELD_LOCATION_ATTRIBUTE1 = "locationAttribute1";
    public static final String FIELD_LOCATION_ATTRIBUTE2 = "locationAttribute2";
    public static final String FIELD_LOCATION_ATTRIBUTE3 = "locationAttribute3";
    public static final String FIELD_LOCATION_ATTRIBUTE4 = "locationAttribute4";
    public static final String FIELD_LOCATION_ATTRIBUTE5 = "locationAttribute5";
    public static final String FIELD_LOCATION_ATTRIBUTE6 = "locationAttribute6";
    public static final String FIELD_LOCATION_ATTRIBUTE7 = "locationAttribute7";
    public static final String FIELD_LOCATION_ATTRIBUTE8 = "locationAttribute8";
    public static final String FIELD_LOCATION_ATTRIBUTE9 = "locationAttribute9";
    public static final String FIELD_LOCATION_ATTRIBUTE10 = "locationAttribute10";
    public static final String FIELD_LOCATION_ATTRIBUTE11 = "locationAttribute11";
    public static final String FIELD_LOCATION_ATTRIBUTE12 = "locationAttribute12";
    public static final String FIELD_LOCATION_ATTRIBUTE13 = "locationAttribute13";
    public static final String FIELD_LOCATION_ATTRIBUTE14 = "locationAttribute14";
    public static final String FIELD_LOCATION_ATTRIBUTE15 = "locationAttribute15";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE1 = "distributionAttribute1";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE2 = "distributionAttribute2";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE3 = "distributionAttribute3";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE4 = "distributionAttribute4";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE5 = "distributionAttribute5";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE6 = "distributionAttribute6";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE7 = "distributionAttribute7";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE8 = "distributionAttribute8";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE9 = "distributionAttribute9";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE10 = "distributionAttribute10";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE11 = "distributionAttribute11";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE12 = "distributionAttribute12";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE13 = "distributionAttribute13";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE14 = "distributionAttribute14";
    public static final String FIELD_DISTRIBUTION_ATTRIBUTE15 = "distributionAttribute15";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -3384202812261568608L;

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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "采购所属组织（将ORG_ID转换为代码）", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "采购订单头ID", required = true)
    @NotBlank
    @Where
    private String poHeaderId;
    @ApiModelProperty(value = "物料编码ID（将采购订单行物料ID写入）", required = true)
    @NotBlank
    @Where
    private String itemId;
    @ApiModelProperty(value = "采购物料单位（将采购订单行物料单位写入）", required = true)
    @NotBlank
    @Where
    private String uom;
    @ApiModelProperty(value = "单价（将采购订单行物料单价写入）")
    @Where
    private Double unitPrice;
    @ApiModelProperty(value = "采购订单行说明（将采购订单行物料单价写入）")
    @Where
    private String lineDescription;
    @ApiModelProperty(value = "超期日（一揽子协议的分段日期价格）")
    @Where
    private Date expirationDate;
    @ApiModelProperty(value = "行类型")
    @Where
    private String lineTpye;
    @ApiModelProperty(value = "订单行数量(oracel将分配行的订单数量写入。如果是一揽子协议该字段为空）")
    @Where
    private Double quantityOrdered;
    @ApiModelProperty(value = "接收数量（oracel将发运行的接收数量写入）")
    @Where
    private Double quantityReceived;
    @ApiModelProperty(value = "接受数量（oracel将发运行的接受数量写入）")
    @Where
    private Double quantityAccepted;
    @ApiModelProperty(value = "入库数量（oracel将发运行的交货数量写入）")
    @Where
    private Double quantityDelivered;
    @ApiModelProperty(value = "取消数量（oracel将分配行的交货数量写入）")
    @Where
    private Double quantityCancelled;
    @ApiModelProperty(value = "行取消标识")
    @Where
    private String lineCancelFlag;
    @ApiModelProperty(value = "行关闭代码(CLOSED,OPEN,FINALLY CLOSED)")
    @Where
    private String lineClosedCode;
    @ApiModelProperty(value = "发运行取消标识")
    @Where
    private String locationCancelFlag;
    @ApiModelProperty(value = "发运行关闭代码(CLOSED, OPEN, FINALLY CLOSED,  CLOSED FOR INVOICE  , CLOSED FOR RECEIVING)")
    @Where
    private String locationClosedCode;
    @ApiModelProperty(value = "需要日期")
    @Where
    private Date needByDate;
    @ApiModelProperty(value = "寄售标识")
    @Where
    private String consignedFlag;
    @ApiModelProperty(value = "接收方式（标准接收，检验接受，直接接收）")
    @Where
    private String receivingRoutingId;
    @ApiModelProperty(value = "外协工单ID（oracle将分配行工单ID写入）")
    @Where
    private String wipEntityId;
    @ApiModelProperty(value = "采购订单行ID")
    @Where
    private String poLineId;
    @ApiModelProperty(value = "采购订单行号(ERP)")
    @Where
    private String poLineNum;
    @ApiModelProperty(value = "采购订单发运行ID")
    @Where
    private String poLocationId;
    @ApiModelProperty(value = "采购订单发运行号")
    @Where
    private String poShipmentNum;
    @ApiModelProperty(value = "采购订单分配行ID")
    @Where
    private String poDistributionId;
    @ApiModelProperty(value = "采购订单分配行号")
    @Where
    private String poDistributionNum;
    @ApiModelProperty(value = "一揽子发放ID")
    @Where
    private String poReleaseId;
    @ApiModelProperty(value = "采购订单行扩展字段1")
    @Where
    private String lineAttribute1;
    @ApiModelProperty(value = "采购订单行扩展字段2")
    @Where
    private String lineAttribute2;
    @ApiModelProperty(value = "采购订单行扩展字段3")
    @Where
    private String lineAttribute3;
    @ApiModelProperty(value = "采购订单行扩展字段4")
    @Where
    private String lineAttribute4;
    @ApiModelProperty(value = "采购订单行扩展字段5")
    @Where
    private String lineAttribute5;
    @ApiModelProperty(value = "采购订单行扩展字段6")
    @Where
    private String lineAttribute6;
    @ApiModelProperty(value = "采购订单行扩展字段7")
    @Where
    private String lineAttribute7;
    @ApiModelProperty(value = "采购订单行扩展字段8")
    @Where
    private String lineAttribute8;
    @ApiModelProperty(value = "采购订单行扩展字段9")
    @Where
    private String lineAttribute9;
    @ApiModelProperty(value = "采购订单行扩展字段10")
    @Where
    private String lineAttribute10;
    @ApiModelProperty(value = "采购订单行扩展字段11")
    @Where
    private String lineAttribute11;
    @ApiModelProperty(value = "采购订单行扩展字段12")
    @Where
    private String lineAttribute12;
    @ApiModelProperty(value = "采购订单行扩展字段13")
    @Where
    private String lineAttribute13;
    @ApiModelProperty(value = "采购订单行扩展字段14")
    @Where
    private String lineAttribute14;
    @ApiModelProperty(value = "采购订单行扩展字段15")
    @Where
    private String lineAttribute15;
    @ApiModelProperty(value = "采购订单发运行扩展字段1")
    @Where
    private String locationAttribute1;
    @ApiModelProperty(value = "采购订单发运行扩展字段2")
    @Where
    private String locationAttribute2;
    @ApiModelProperty(value = "采购订单发运行扩展字段3")
    @Where
    private String locationAttribute3;
    @ApiModelProperty(value = "采购订单发运行扩展字段4")
    @Where
    private String locationAttribute4;
    @ApiModelProperty(value = "采购订单发运行扩展字段5")
    @Where
    private String locationAttribute5;
    @ApiModelProperty(value = "采购订单发运行扩展字段6")
    @Where
    private String locationAttribute6;
    @ApiModelProperty(value = "采购订单发运行扩展字段7")
    @Where
    private String locationAttribute7;
    @ApiModelProperty(value = "采购订单发运行扩展字段8")
    @Where
    private String locationAttribute8;
    @ApiModelProperty(value = "采购订单发运行扩展字段9")
    @Where
    private String locationAttribute9;
    @ApiModelProperty(value = "采购订单发运行扩展字段10")
    @Where
    private String locationAttribute10;
    @ApiModelProperty(value = "采购订单发运行扩展字段11")
    @Where
    private String locationAttribute11;
    @ApiModelProperty(value = "采购订单发运行扩展字段12")
    @Where
    private String locationAttribute12;
    @ApiModelProperty(value = "采购订单发运行扩展字段13")
    @Where
    private String locationAttribute13;
    @ApiModelProperty(value = "采购订单发运行扩展字段14")
    @Where
    private String locationAttribute14;
    @ApiModelProperty(value = "采购订单发运行扩展字段15")
    @Where
    private String locationAttribute15;
    @ApiModelProperty(value = "采购订单分配行扩展字段1")
    @Where
    private String distributionAttribute1;
    @ApiModelProperty(value = "采购订单分配行扩展字段2")
    @Where
    private String distributionAttribute2;
    @ApiModelProperty(value = "采购订单分配行扩展字段3")
    @Where
    private String distributionAttribute3;
    @ApiModelProperty(value = "采购订单分配行扩展字段4")
    @Where
    private String distributionAttribute4;
    @ApiModelProperty(value = "采购订单分配行扩展字段5")
    @Where
    private String distributionAttribute5;
    @ApiModelProperty(value = "采购订单分配行扩展字段6")
    @Where
    private String distributionAttribute6;
    @ApiModelProperty(value = "采购订单分配行扩展字段7")
    @Where
    private String distributionAttribute7;
    @ApiModelProperty(value = "采购订单分配行扩展字段8")
    @Where
    private String distributionAttribute8;
    @ApiModelProperty(value = "采购订单分配行扩展字段9")
    @Where
    private String distributionAttribute9;
    @ApiModelProperty(value = "采购订单分配行扩展字段10")
    @Where
    private String distributionAttribute10;
    @ApiModelProperty(value = "采购订单分配行扩展字段11")
    @Where
    private String distributionAttribute11;
    @ApiModelProperty(value = "采购订单分配行扩展字段12")
    @Where
    private String distributionAttribute12;
    @ApiModelProperty(value = "采购订单分配行扩展字段13")
    @Where
    private String distributionAttribute13;
    @ApiModelProperty(value = "采购订单分配行扩展字段14")
    @Where
    private String distributionAttribute14;
    @ApiModelProperty(value = "采购订单分配行扩展字段15")
    @Where
    private String distributionAttribute15;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
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
     * @return 主键
     */
    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    /**
     * @return 采购所属组织（将ORG_ID转换为代码）
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return 采购订单头ID
     */
    public String getPoHeaderId() {
        return poHeaderId;
    }

    public void setPoHeaderId(String poHeaderId) {
        this.poHeaderId = poHeaderId;
    }

    /**
     * @return 物料编码ID（将采购订单行物料ID写入）
     */
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return 采购物料单位（将采购订单行物料单位写入）
     */
    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    /**
     * @return 单价（将采购订单行物料单价写入）
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return 采购订单行说明（将采购订单行物料单价写入）
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
        if (expirationDate == null) {
            return null;
        } else {
            return (Date) expirationDate.clone();
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
     * @return 行类型
     */
    public String getLineTpye() {
        return lineTpye;
    }

    public void setLineTpye(String lineTpye) {
        this.lineTpye = lineTpye;
    }

    /**
     * @return 订单行数量(oracel将分配行的订单数量写入。如果是一揽子协议该字段为空）
     */
    public Double getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Double quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    /**
     * @return 接收数量（oracel将发运行的接收数量写入）
     */
    public Double getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(Double quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    /**
     * @return 接受数量（oracel将发运行的接受数量写入）
     */
    public Double getQuantityAccepted() {
        return quantityAccepted;
    }

    public void setQuantityAccepted(Double quantityAccepted) {
        this.quantityAccepted = quantityAccepted;
    }

    /**
     * @return 入库数量（oracel将发运行的交货数量写入）
     */
    public Double getQuantityDelivered() {
        return quantityDelivered;
    }

    public void setQuantityDelivered(Double quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    /**
     * @return 取消数量（oracel将分配行的交货数量写入）
     */
    public Double getQuantityCancelled() {
        return quantityCancelled;
    }

    public void setQuantityCancelled(Double quantityCancelled) {
        this.quantityCancelled = quantityCancelled;
    }

    /**
     * @return 行取消标识
     */
    public String getLineCancelFlag() {
        return lineCancelFlag;
    }

    public void setLineCancelFlag(String lineCancelFlag) {
        this.lineCancelFlag = lineCancelFlag;
    }

    /**
     * @return 行关闭代码(CLOSED,OPEN,FINALLY CLOSED)
     */
    public String getLineClosedCode() {
        return lineClosedCode;
    }

    public void setLineClosedCode(String lineClosedCode) {
        this.lineClosedCode = lineClosedCode;
    }

    /**
     * @return 发运行取消标识
     */
    public String getLocationCancelFlag() {
        return locationCancelFlag;
    }

    public void setLocationCancelFlag(String locationCancelFlag) {
        this.locationCancelFlag = locationCancelFlag;
    }

    /**
     * @return 发运行关闭代码(CLOSED, OPEN, FINALLY CLOSED, CLOSED FOR INVOICE , CLOSED FOR RECEIVING)
     */
    public String getLocationClosedCode() {
        return locationClosedCode;
    }

    public void setLocationClosedCode(String locationClosedCode) {
        this.locationClosedCode = locationClosedCode;
    }

    /**
     * @return 需要日期
     */
    public Date getNeedByDate() {
        if (needByDate == null) {
            return null;
        } else {
            return (Date) needByDate.clone();
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
     * @return 接收方式（标准接收，检验接受，直接接收）
     */
    public String getReceivingRoutingId() {
        return receivingRoutingId;
    }

    public void setReceivingRoutingId(String receivingRoutingId) {
        this.receivingRoutingId = receivingRoutingId;
    }

    /**
     * @return 外协工单ID（oracle将分配行工单ID写入）
     */
    public String getWipEntityId() {
        return wipEntityId;
    }

    public void setWipEntityId(String wipEntityId) {
        this.wipEntityId = wipEntityId;
    }

    /**
     * @return 采购订单行ID
     */
    public String getPoLineId() {
        return poLineId;
    }

    public void setPoLineId(String poLineId) {
        this.poLineId = poLineId;
    }

    /**
     * @return 采购订单行号(ERP)
     */
    public String getPoLineNum() {
        return poLineNum;
    }

    public void setPoLineNum(String poLineNum) {
        this.poLineNum = poLineNum;
    }

    /**
     * @return 采购订单发运行ID
     */
    public String getPoLocationId() {
        return poLocationId;
    }

    public void setPoLocationId(String poLocationId) {
        this.poLocationId = poLocationId;
    }

    /**
     * @return 采购订单发运行号
     */
    public String getPoShipmentNum() {
        return poShipmentNum;
    }

    public void setPoShipmentNum(String poShipmentNum) {
        this.poShipmentNum = poShipmentNum;
    }

    /**
     * @return 采购订单分配行ID
     */
    public String getPoDistributionId() {
        return poDistributionId;
    }

    public void setPoDistributionId(String poDistributionId) {
        this.poDistributionId = poDistributionId;
    }

    /**
     * @return 采购订单分配行号
     */
    public String getPoDistributionNum() {
        return poDistributionNum;
    }

    public void setPoDistributionNum(String poDistributionNum) {
        this.poDistributionNum = poDistributionNum;
    }

    /**
     * @return 一揽子发放ID
     */
    public String getPoReleaseId() {
        return poReleaseId;
    }

    public void setPoReleaseId(String poReleaseId) {
        this.poReleaseId = poReleaseId;
    }

    /**
     * @return 采购订单行扩展字段1
     */
    public String getLineAttribute1() {
        return lineAttribute1;
    }

    public void setLineAttribute1(String lineAttribute1) {
        this.lineAttribute1 = lineAttribute1;
    }

    /**
     * @return 采购订单行扩展字段2
     */
    public String getLineAttribute2() {
        return lineAttribute2;
    }

    public void setLineAttribute2(String lineAttribute2) {
        this.lineAttribute2 = lineAttribute2;
    }

    /**
     * @return 采购订单行扩展字段3
     */
    public String getLineAttribute3() {
        return lineAttribute3;
    }

    public void setLineAttribute3(String lineAttribute3) {
        this.lineAttribute3 = lineAttribute3;
    }

    /**
     * @return 采购订单行扩展字段4
     */
    public String getLineAttribute4() {
        return lineAttribute4;
    }

    public void setLineAttribute4(String lineAttribute4) {
        this.lineAttribute4 = lineAttribute4;
    }

    /**
     * @return 采购订单行扩展字段5
     */
    public String getLineAttribute5() {
        return lineAttribute5;
    }

    public void setLineAttribute5(String lineAttribute5) {
        this.lineAttribute5 = lineAttribute5;
    }

    /**
     * @return 采购订单行扩展字段6
     */
    public String getLineAttribute6() {
        return lineAttribute6;
    }

    public void setLineAttribute6(String lineAttribute6) {
        this.lineAttribute6 = lineAttribute6;
    }

    /**
     * @return 采购订单行扩展字段7
     */
    public String getLineAttribute7() {
        return lineAttribute7;
    }

    public void setLineAttribute7(String lineAttribute7) {
        this.lineAttribute7 = lineAttribute7;
    }

    /**
     * @return 采购订单行扩展字段8
     */
    public String getLineAttribute8() {
        return lineAttribute8;
    }

    public void setLineAttribute8(String lineAttribute8) {
        this.lineAttribute8 = lineAttribute8;
    }

    /**
     * @return 采购订单行扩展字段9
     */
    public String getLineAttribute9() {
        return lineAttribute9;
    }

    public void setLineAttribute9(String lineAttribute9) {
        this.lineAttribute9 = lineAttribute9;
    }

    /**
     * @return 采购订单行扩展字段10
     */
    public String getLineAttribute10() {
        return lineAttribute10;
    }

    public void setLineAttribute10(String lineAttribute10) {
        this.lineAttribute10 = lineAttribute10;
    }

    /**
     * @return 采购订单行扩展字段11
     */
    public String getLineAttribute11() {
        return lineAttribute11;
    }

    public void setLineAttribute11(String lineAttribute11) {
        this.lineAttribute11 = lineAttribute11;
    }

    /**
     * @return 采购订单行扩展字段12
     */
    public String getLineAttribute12() {
        return lineAttribute12;
    }

    public void setLineAttribute12(String lineAttribute12) {
        this.lineAttribute12 = lineAttribute12;
    }

    /**
     * @return 采购订单行扩展字段13
     */
    public String getLineAttribute13() {
        return lineAttribute13;
    }

    public void setLineAttribute13(String lineAttribute13) {
        this.lineAttribute13 = lineAttribute13;
    }

    /**
     * @return 采购订单行扩展字段14
     */
    public String getLineAttribute14() {
        return lineAttribute14;
    }

    public void setLineAttribute14(String lineAttribute14) {
        this.lineAttribute14 = lineAttribute14;
    }

    /**
     * @return 采购订单行扩展字段15
     */
    public String getLineAttribute15() {
        return lineAttribute15;
    }

    public void setLineAttribute15(String lineAttribute15) {
        this.lineAttribute15 = lineAttribute15;
    }

    /**
     * @return 采购订单发运行扩展字段1
     */
    public String getLocationAttribute1() {
        return locationAttribute1;
    }

    public void setLocationAttribute1(String locationAttribute1) {
        this.locationAttribute1 = locationAttribute1;
    }

    /**
     * @return 采购订单发运行扩展字段2
     */
    public String getLocationAttribute2() {
        return locationAttribute2;
    }

    public void setLocationAttribute2(String locationAttribute2) {
        this.locationAttribute2 = locationAttribute2;
    }

    /**
     * @return 采购订单发运行扩展字段3
     */
    public String getLocationAttribute3() {
        return locationAttribute3;
    }

    public void setLocationAttribute3(String locationAttribute3) {
        this.locationAttribute3 = locationAttribute3;
    }

    /**
     * @return 采购订单发运行扩展字段4
     */
    public String getLocationAttribute4() {
        return locationAttribute4;
    }

    public void setLocationAttribute4(String locationAttribute4) {
        this.locationAttribute4 = locationAttribute4;
    }

    /**
     * @return 采购订单发运行扩展字段5
     */
    public String getLocationAttribute5() {
        return locationAttribute5;
    }

    public void setLocationAttribute5(String locationAttribute5) {
        this.locationAttribute5 = locationAttribute5;
    }

    /**
     * @return 采购订单发运行扩展字段6
     */
    public String getLocationAttribute6() {
        return locationAttribute6;
    }

    public void setLocationAttribute6(String locationAttribute6) {
        this.locationAttribute6 = locationAttribute6;
    }

    /**
     * @return 采购订单发运行扩展字段7
     */
    public String getLocationAttribute7() {
        return locationAttribute7;
    }

    public void setLocationAttribute7(String locationAttribute7) {
        this.locationAttribute7 = locationAttribute7;
    }

    /**
     * @return 采购订单发运行扩展字段8
     */
    public String getLocationAttribute8() {
        return locationAttribute8;
    }

    public void setLocationAttribute8(String locationAttribute8) {
        this.locationAttribute8 = locationAttribute8;
    }

    /**
     * @return 采购订单发运行扩展字段9
     */
    public String getLocationAttribute9() {
        return locationAttribute9;
    }

    public void setLocationAttribute9(String locationAttribute9) {
        this.locationAttribute9 = locationAttribute9;
    }

    /**
     * @return 采购订单发运行扩展字段10
     */
    public String getLocationAttribute10() {
        return locationAttribute10;
    }

    public void setLocationAttribute10(String locationAttribute10) {
        this.locationAttribute10 = locationAttribute10;
    }

    /**
     * @return 采购订单发运行扩展字段11
     */
    public String getLocationAttribute11() {
        return locationAttribute11;
    }

    public void setLocationAttribute11(String locationAttribute11) {
        this.locationAttribute11 = locationAttribute11;
    }

    /**
     * @return 采购订单发运行扩展字段12
     */
    public String getLocationAttribute12() {
        return locationAttribute12;
    }

    public void setLocationAttribute12(String locationAttribute12) {
        this.locationAttribute12 = locationAttribute12;
    }

    /**
     * @return 采购订单发运行扩展字段13
     */
    public String getLocationAttribute13() {
        return locationAttribute13;
    }

    public void setLocationAttribute13(String locationAttribute13) {
        this.locationAttribute13 = locationAttribute13;
    }

    /**
     * @return 采购订单发运行扩展字段14
     */
    public String getLocationAttribute14() {
        return locationAttribute14;
    }

    public void setLocationAttribute14(String locationAttribute14) {
        this.locationAttribute14 = locationAttribute14;
    }

    /**
     * @return 采购订单发运行扩展字段15
     */
    public String getLocationAttribute15() {
        return locationAttribute15;
    }

    public void setLocationAttribute15(String locationAttribute15) {
        this.locationAttribute15 = locationAttribute15;
    }

    /**
     * @return 采购订单分配行扩展字段1
     */
    public String getDistributionAttribute1() {
        return distributionAttribute1;
    }

    public void setDistributionAttribute1(String distributionAttribute1) {
        this.distributionAttribute1 = distributionAttribute1;
    }

    /**
     * @return 采购订单分配行扩展字段2
     */
    public String getDistributionAttribute2() {
        return distributionAttribute2;
    }

    public void setDistributionAttribute2(String distributionAttribute2) {
        this.distributionAttribute2 = distributionAttribute2;
    }

    /**
     * @return 采购订单分配行扩展字段3
     */
    public String getDistributionAttribute3() {
        return distributionAttribute3;
    }

    public void setDistributionAttribute3(String distributionAttribute3) {
        this.distributionAttribute3 = distributionAttribute3;
    }

    /**
     * @return 采购订单分配行扩展字段4
     */
    public String getDistributionAttribute4() {
        return distributionAttribute4;
    }

    public void setDistributionAttribute4(String distributionAttribute4) {
        this.distributionAttribute4 = distributionAttribute4;
    }

    /**
     * @return 采购订单分配行扩展字段5
     */
    public String getDistributionAttribute5() {
        return distributionAttribute5;
    }

    public void setDistributionAttribute5(String distributionAttribute5) {
        this.distributionAttribute5 = distributionAttribute5;
    }

    /**
     * @return 采购订单分配行扩展字段6
     */
    public String getDistributionAttribute6() {
        return distributionAttribute6;
    }

    public void setDistributionAttribute6(String distributionAttribute6) {
        this.distributionAttribute6 = distributionAttribute6;
    }

    /**
     * @return 采购订单分配行扩展字段7
     */
    public String getDistributionAttribute7() {
        return distributionAttribute7;
    }

    public void setDistributionAttribute7(String distributionAttribute7) {
        this.distributionAttribute7 = distributionAttribute7;
    }

    /**
     * @return 采购订单分配行扩展字段8
     */
    public String getDistributionAttribute8() {
        return distributionAttribute8;
    }

    public void setDistributionAttribute8(String distributionAttribute8) {
        this.distributionAttribute8 = distributionAttribute8;
    }

    /**
     * @return 采购订单分配行扩展字段9
     */
    public String getDistributionAttribute9() {
        return distributionAttribute9;
    }

    public void setDistributionAttribute9(String distributionAttribute9) {
        this.distributionAttribute9 = distributionAttribute9;
    }

    /**
     * @return 采购订单分配行扩展字段10
     */
    public String getDistributionAttribute10() {
        return distributionAttribute10;
    }

    public void setDistributionAttribute10(String distributionAttribute10) {
        this.distributionAttribute10 = distributionAttribute10;
    }

    /**
     * @return 采购订单分配行扩展字段11
     */
    public String getDistributionAttribute11() {
        return distributionAttribute11;
    }

    public void setDistributionAttribute11(String distributionAttribute11) {
        this.distributionAttribute11 = distributionAttribute11;
    }

    /**
     * @return 采购订单分配行扩展字段12
     */
    public String getDistributionAttribute12() {
        return distributionAttribute12;
    }

    public void setDistributionAttribute12(String distributionAttribute12) {
        this.distributionAttribute12 = distributionAttribute12;
    }

    /**
     * @return 采购订单分配行扩展字段13
     */
    public String getDistributionAttribute13() {
        return distributionAttribute13;
    }

    public void setDistributionAttribute13(String distributionAttribute13) {
        this.distributionAttribute13 = distributionAttribute13;
    }

    /**
     * @return 采购订单分配行扩展字段14
     */
    public String getDistributionAttribute14() {
        return distributionAttribute14;
    }

    public void setDistributionAttribute14(String distributionAttribute14) {
        this.distributionAttribute14 = distributionAttribute14;
    }

    /**
     * @return 采购订单分配行扩展字段15
     */
    public String getDistributionAttribute15() {
        return distributionAttribute15;
    }

    public void setDistributionAttribute15(String distributionAttribute15) {
        this.distributionAttribute15 = distributionAttribute15;
    }

    /**
     * @return 数据批次ID
     */
    public Double getBatchId() {
        return batchId;
    }

    public void setBatchId(Double batchId) {
        this.batchId = batchId;
    }

    /**
     * @return 数据处理状态，初始为N，失败为E，成功S，处理中P
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 数据处理消息返回
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtEbsPoLineIface that = (MtEbsPoLineIface) o;
        return Objects.equals(getTenantId(), that.getTenantId()) && Objects.equals(getIfaceId(), that.getIfaceId())
                        && Objects.equals(getPlantCode(), that.getPlantCode())
                        && Objects.equals(getPoHeaderId(), that.getPoHeaderId())
                        && Objects.equals(getItemId(), that.getItemId()) && Objects.equals(getUom(), that.getUom())
                        && Objects.equals(getUnitPrice(), that.getUnitPrice())
                        && Objects.equals(getLineDescription(), that.getLineDescription())
                        && Objects.equals(getExpirationDate(), that.getExpirationDate())
                        && Objects.equals(getLineTpye(), that.getLineTpye())
                        && Objects.equals(getQuantityOrdered(), that.getQuantityOrdered())
                        && Objects.equals(getQuantityReceived(), that.getQuantityReceived())
                        && Objects.equals(getQuantityAccepted(), that.getQuantityAccepted())
                        && Objects.equals(getQuantityDelivered(), that.getQuantityDelivered())
                        && Objects.equals(getQuantityCancelled(), that.getQuantityCancelled())
                        && Objects.equals(getLineCancelFlag(), that.getLineCancelFlag())
                        && Objects.equals(getLineClosedCode(), that.getLineClosedCode())
                        && Objects.equals(getLocationCancelFlag(), that.getLocationCancelFlag())
                        && Objects.equals(getLocationClosedCode(), that.getLocationClosedCode())
                        && Objects.equals(getNeedByDate(), that.getNeedByDate())
                        && Objects.equals(getConsignedFlag(), that.getConsignedFlag())
                        && Objects.equals(getReceivingRoutingId(), that.getReceivingRoutingId())
                        && Objects.equals(getWipEntityId(), that.getWipEntityId())
                        && Objects.equals(getPoLineId(), that.getPoLineId())
                        && Objects.equals(getPoLineNum(), that.getPoLineNum())
                        && Objects.equals(getPoLocationId(), that.getPoLocationId())
                        && Objects.equals(getPoShipmentNum(), that.getPoShipmentNum())
                        && Objects.equals(getPoDistributionId(), that.getPoDistributionId())
                        && Objects.equals(getPoDistributionNum(), that.getPoDistributionNum())
                        && Objects.equals(getPoReleaseId(), that.getPoReleaseId())
                        && Objects.equals(getLineAttribute1(), that.getLineAttribute1())
                        && Objects.equals(getLineAttribute2(), that.getLineAttribute2())
                        && Objects.equals(getLineAttribute3(), that.getLineAttribute3())
                        && Objects.equals(getLineAttribute4(), that.getLineAttribute4())
                        && Objects.equals(getLineAttribute5(), that.getLineAttribute5())
                        && Objects.equals(getLineAttribute6(), that.getLineAttribute6())
                        && Objects.equals(getLineAttribute7(), that.getLineAttribute7())
                        && Objects.equals(getLineAttribute8(), that.getLineAttribute8())
                        && Objects.equals(getLineAttribute9(), that.getLineAttribute9())
                        && Objects.equals(getLineAttribute10(), that.getLineAttribute10())
                        && Objects.equals(getLineAttribute11(), that.getLineAttribute11())
                        && Objects.equals(getLineAttribute12(), that.getLineAttribute12())
                        && Objects.equals(getLineAttribute13(), that.getLineAttribute13())
                        && Objects.equals(getLineAttribute14(), that.getLineAttribute14())
                        && Objects.equals(getLineAttribute15(), that.getLineAttribute15())
                        && Objects.equals(getLocationAttribute1(), that.getLocationAttribute1())
                        && Objects.equals(getLocationAttribute2(), that.getLocationAttribute2())
                        && Objects.equals(getLocationAttribute3(), that.getLocationAttribute3())
                        && Objects.equals(getLocationAttribute4(), that.getLocationAttribute4())
                        && Objects.equals(getLocationAttribute5(), that.getLocationAttribute5())
                        && Objects.equals(getLocationAttribute6(), that.getLocationAttribute6())
                        && Objects.equals(getLocationAttribute7(), that.getLocationAttribute7())
                        && Objects.equals(getLocationAttribute8(), that.getLocationAttribute8())
                        && Objects.equals(getLocationAttribute9(), that.getLocationAttribute9())
                        && Objects.equals(getLocationAttribute10(), that.getLocationAttribute10())
                        && Objects.equals(getLocationAttribute11(), that.getLocationAttribute11())
                        && Objects.equals(getLocationAttribute12(), that.getLocationAttribute12())
                        && Objects.equals(getLocationAttribute13(), that.getLocationAttribute13())
                        && Objects.equals(getLocationAttribute14(), that.getLocationAttribute14())
                        && Objects.equals(getLocationAttribute15(), that.getLocationAttribute15())
                        && Objects.equals(getDistributionAttribute1(), that.getDistributionAttribute1())
                        && Objects.equals(getDistributionAttribute2(), that.getDistributionAttribute2())
                        && Objects.equals(getDistributionAttribute3(), that.getDistributionAttribute3())
                        && Objects.equals(getDistributionAttribute4(), that.getDistributionAttribute4())
                        && Objects.equals(getDistributionAttribute5(), that.getDistributionAttribute5())
                        && Objects.equals(getDistributionAttribute6(), that.getDistributionAttribute6())
                        && Objects.equals(getDistributionAttribute7(), that.getDistributionAttribute7())
                        && Objects.equals(getDistributionAttribute8(), that.getDistributionAttribute8())
                        && Objects.equals(getDistributionAttribute9(), that.getDistributionAttribute9())
                        && Objects.equals(getDistributionAttribute10(), that.getDistributionAttribute10())
                        && Objects.equals(getDistributionAttribute11(), that.getDistributionAttribute11())
                        && Objects.equals(getDistributionAttribute12(), that.getDistributionAttribute12())
                        && Objects.equals(getDistributionAttribute13(), that.getDistributionAttribute13())
                        && Objects.equals(getDistributionAttribute14(), that.getDistributionAttribute14())
                        && Objects.equals(getDistributionAttribute15(), that.getDistributionAttribute15())
                        && Objects.equals(getBatchId(), that.getBatchId())
                        && Objects.equals(getStatus(), that.getStatus())
                        && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getCid(), that.getCid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantId(), getIfaceId(), getPlantCode(), getPoHeaderId(), getItemId(), getUom(),
                        getUnitPrice(), getLineDescription(), getExpirationDate(), getLineTpye(), getQuantityOrdered(),
                        getQuantityReceived(), getQuantityAccepted(), getQuantityDelivered(), getQuantityCancelled(),
                        getLineCancelFlag(), getLineClosedCode(), getLocationCancelFlag(), getLocationClosedCode(),
                        getNeedByDate(), getConsignedFlag(), getReceivingRoutingId(), getWipEntityId(), getPoLineId(),
                        getPoLineNum(), getPoLocationId(), getPoShipmentNum(), getPoDistributionId(),
                        getPoDistributionNum(), getPoReleaseId(), getLineAttribute1(), getLineAttribute2(),
                        getLineAttribute3(), getLineAttribute4(), getLineAttribute5(), getLineAttribute6(),
                        getLineAttribute7(), getLineAttribute8(), getLineAttribute9(), getLineAttribute10(),
                        getLineAttribute11(), getLineAttribute12(), getLineAttribute13(), getLineAttribute14(),
                        getLineAttribute15(), getLocationAttribute1(), getLocationAttribute2(), getLocationAttribute3(),
                        getLocationAttribute4(), getLocationAttribute5(), getLocationAttribute6(),
                        getLocationAttribute7(), getLocationAttribute8(), getLocationAttribute9(),
                        getLocationAttribute10(), getLocationAttribute11(), getLocationAttribute12(),
                        getLocationAttribute13(), getLocationAttribute14(), getLocationAttribute15(),
                        getDistributionAttribute1(), getDistributionAttribute2(), getDistributionAttribute3(),
                        getDistributionAttribute4(), getDistributionAttribute5(), getDistributionAttribute6(),
                        getDistributionAttribute7(), getDistributionAttribute8(), getDistributionAttribute9(),
                        getDistributionAttribute10(), getDistributionAttribute11(), getDistributionAttribute12(),
                        getDistributionAttribute13(), getDistributionAttribute14(), getDistributionAttribute15(),
                        getBatchId(), getStatus(), getMessage(), getCid());
    }
}
