package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author yifan.xiong@hand-china.com 2021/03/12 15:59
 */
@Data
public class WmsDistDemandOffsetVO implements Serializable {

    private static final long serialVersionUID = 6602870972491981834L;

    @ApiModelProperty(value = "配送需求ID列表")
    private List<String> distDemandIdList;
    @ApiModelProperty("配送明细ID列表")
    private List<String> demandDetailIdList;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "物料标识")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工段ID")
    private String workcellId;
    @ApiModelProperty(value = "工段编码")
    private String workcellCode;
    @ApiModelProperty(value = "产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "班次ID")
    private String calendarShiftId;
    @ApiModelProperty(value = "需求日期")
    private Date demandDate;
    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "替代标识")
    private String substituteFlag;
    @ApiModelProperty(value = "总需求数量")
    private BigDecimal requirementQty;
    @ApiModelProperty(value = "车间需求数量")
    private BigDecimal workshopDemandQty;
    @ApiModelProperty(value = "线边库存数量")
    private BigDecimal instockQty;
    @ApiModelProperty(value = "冲抵数量")
    private BigDecimal offsetQty;
    @ApiModelProperty(value = "配送单数量")
    private BigDecimal instructionQty;
    @ApiModelProperty(value = "可替代数量")
    private BigDecimal replaceQty;
    @ApiModelProperty(value = "实际替代数量,表示本物料实际给到其他物料的替代数量")
    private BigDecimal actualReplaceQty;
}
