package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 指令单据带拓展属性
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 19:58
 */
@Data
public class WmsInstructionDocAttrVO {

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    private String instructionDocId;
    @ApiModelProperty(value = "单据编号", required = true)
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型（业务类型，由项目定义）", required = true)
    private String instructionDocType;
    @ApiModelProperty(value = "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")
    private String instructionDocStatus;
    @ApiModelProperty(value = "站点ID", required = true)
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
    @ApiModelProperty(value = "标记")
    private String mark;
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
    @ApiModelProperty(value = "需要签收标志")
    private String signFlag;
    @ApiModelProperty(value = "产线")
    private String prodLineCode;
    @ApiModelProperty(value = "工段ID")
    private String workcellId;
    @ApiModelProperty(value = "工段")
    private String workcellCode;
    @ApiModelProperty(value = "目标货位ID")
    private String toLocatorId;
    @ApiModelProperty(value = "目标货位编码")
    private String toLocatorCode;
    @ApiModelProperty(value = "补料单ID")
    private String replenishmentDocId;
}
