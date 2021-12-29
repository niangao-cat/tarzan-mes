package com.ruike.wms.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.mybatis.common.query.Where;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 送货单行与采购订单行关系表
 *
 * @author han.zhang03@hand-china.com 2020-03-27 18:46:38
 */
@ApiModel("送货单行与采购订单行关系表")
@ModifyAudit
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_po_delivery_rel")
@CustomPrimary
@VersionAudit
public class WmsPoDeliveryRel extends AuditDomain {
	public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PO_DELIVERY_REL_ID = "poDeliveryRelId";
    public static final String FIELD_DELIVERY_DOC_ID = "deliveryDocId";
    public static final String FIELD_DELIVERY_DOC_LINE_ID = "deliveryDocLineId";
    public static final String FIELD_PO_ID = "poId";
    public static final String FIELD_PO_LINE_ID = "poLineId";
    public static final String FIELD_BOM_TYPE = "bomType";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_RECEIVED_QTY = "receivedQty";
    public static final String FIELD_PO_STOCK_IN_QTY = "poStockInQty";
    public static final String FIELD_SRM_LINE_NUM = "srmLineNum";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

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
    private String poDeliveryRelId;
    @ApiModelProperty(value = "送货单号（id）",required = true)
    @NotBlank
    private String deliveryDocId;
    @ApiModelProperty(value = "送货单行号(id)",required = true)
    @NotBlank
    private String deliveryDocLineId;
    @ApiModelProperty(value = "采购订单号(id)",required = true)
    @NotBlank
    private String poId;
    @ApiModelProperty(value = "物料清单描述")
    private String poLineId;
    @ApiModelProperty(value = "采购订单行号(id)", required = true)
    @NotBlank
    private String bomType;
    @ApiModelProperty(value = "送货单行上对应采购订单行数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "采购接收数量")
    private BigDecimal receivedQty;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    @Cid
    private Long cid;

	@ApiModelProperty(value = "入库数量")
	private BigDecimal poStockInQty;

    @ApiModelProperty(value = "SRM送货单行号")
    private String srmLineNum;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
