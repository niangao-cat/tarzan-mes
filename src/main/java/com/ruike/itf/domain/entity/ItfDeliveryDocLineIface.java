package com.ruike.itf.domain.entity;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 送货单接口行表
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@ApiModel("送货单接口行表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_delivery_doc_line_iface")
@Data
public class ItfDeliveryDocLineIface extends AuditDomain {

    public static final String FIELD_INTERFACE_LINE_ID = "interfaceLineId";
    public static final String FIELD_INTERFACE_HEADER_ID = "interfaceHeaderId";
    public static final String FIELD_INSTRUCTION_LINE_NUM = "instructionLineNum";
    public static final String FIELD_INSTRUCTION_LINE_NEW_NUM = "instructionLineNewNum";
    public static final String FIELD_INSTRUCTION_STATUS = "instructionStatus";
    public static final String FIELD_TO_LOCATOR_CODE = "toLocatorCode";
    public static final String FIELD_TO_LOCATOR_ID = "toLocatorId";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_SO_LINE_NUM = "soLineNum";
    public static final String FIELD_SO_NUM = "soNum";
    public static final String FIELD_UAI_FLAG = "uaiFlag";
    public static final String FIELD_EXCHANGE_QTY = "exchangeQty";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_PO_NUM = "poNum";
    public static final String FIELD_PO_ID = "poId";
    public static final String FIELD_PO_LINE_NUM = "poLineNum";
    public static final String FIELD_PO_LINE_ID = "poLineId";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口行表ID，主键")
    @Id
    @GeneratedValue
    private String interfaceLineId;
    @ApiModelProperty(value = "接口头表ID", required = true)
    @NotBlank
    private String interfaceHeaderId;
    @ApiModelProperty(value = "指令行号")
    private String instructionLineNum;
    @Transient
    @ApiModelProperty(hidden = true)
    private String instructionLineNewNum;
    @ApiModelProperty(value = "指令状态（NEW，RELEASED，CANCEL，COMPLETED，COMPLETE_CANCEL）")
    private String instructionStatus;
    @ApiModelProperty(value = "目标库位编码")
    private String toLocatorCode;
    @ApiModelProperty(value = "目标库位id")
    private String toLocatorId;
    @ApiModelProperty(value = "指令数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "特采标识")
    private String uaiFlag;
    @ApiModelProperty(value = "料废调换数量")
    private String exchangeQty;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料id")
    private String materialId;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "采购订单号")
    private String poNum;
    @ApiModelProperty(value = "采购订单ID")
    private String poId;
    @ApiModelProperty(value = "采购订单行号")
    private String poLineNum;
    @ApiModelProperty(value = "采购订单行ID")
    private String poLineId;
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "")
    private String attributeCategory;
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

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

}
