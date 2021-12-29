package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 装配清单
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 20:24
 */
@Data
public class HmeWoInputRecordDTO4 implements Serializable {

    private static final long serialVersionUID = -8279056391848534107L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "工单BOM ID")
    private String bomId;
    @ApiModelProperty(value = "组件ID")
    private String bomComponentId;
    @ApiModelProperty(value = "预留项目号")
    private String relatedProjectNum;
    @ApiModelProperty(value = "工步ID")
    private String routerStepId;
    @ApiModelProperty(value = "步骤识别码")
    private String stepName;
    @ApiModelProperty(value = "工步")
    private String stepDesc;
    @ApiModelProperty(value = "行号")
    private String lineNumber;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "单位用量")
    private BigDecimal unitDosage;
    @ApiModelProperty(value = "工单数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal needQty;
    @ApiModelProperty(value = "已装配数量")
    private BigDecimal assembleQty;
    @ApiModelProperty(value = "单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "反冲标识")
    private String recoilFlag;
    @ApiModelProperty(value = "特殊库存标识")
    private String specialInvFlag;
    @ApiModelProperty(value = "上级虚拟件")
    private String parentVirtualPart;
    @ApiModelProperty(value = "虚拟件组件标识")
    private String virtualPartFlag;
    @ApiModelProperty(value = "净需求数量")
    private String demandQty;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "计划标识")
    private String planFlag;
    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "主料仓库ID")
    private String mainWarehouseId;
    @ApiModelProperty(value = "主料货位ID")
    private String mainLocatorId;
    @ApiModelProperty(value = "主料净需求数量")
    private BigDecimal mainDemandQty;
    @ApiModelProperty(value = "所有料已装配数量")
    private BigDecimal sumAssembleQty;
    @ApiModelProperty(value = "请求事件id")
    private String eventRequestId;
    @ApiModelProperty(value = "事件id")
    private String eventId;
    @ApiModelProperty(value = "事务类型")
    private String transactionTypeCode;
    @ApiModelProperty(value = "移动类型")
    private String moveType;
    @ApiModelProperty(value = "移动原因")
    private String moveReason;
    @ApiModelProperty(value = "总需求数量")
    private String totalDemandQty;
}
