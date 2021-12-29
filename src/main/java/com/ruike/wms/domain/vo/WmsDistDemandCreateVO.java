package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 配送单创建
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/1 15:10
 */
@Data
public class WmsDistDemandCreateVO {
    @ApiModelProperty("配送明细ID")
    private String demandDetailId;
    @ApiModelProperty(value = "配送需求ID")
    private String distDemandId;
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
    @ApiModelProperty(value = "货位类型")
    private String locatorType;
    @ApiModelProperty(value = "班次ID")
    private String calendarShiftId;
    @ApiModelProperty(value = "需求日期")
    private Date demandDate;
    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal requirementQty;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "替代标识")
    private String substituteFlag;

    public WmsDistributionDocCreateSumVO summaryCreate() {
        WmsDistributionDocCreateSumVO vo = new WmsDistributionDocCreateSumVO();
        vo.setCalendarShiftId(this.calendarShiftId);
        vo.setMaterialId(this.materialId);
        vo.setMaterialVersion(this.materialVersion);
        vo.setShiftCode(this.shiftCode);
        vo.setShiftDate(this.shiftDate);
        vo.setWorkcellId(this.workcellId);
        vo.setLocatorId(this.locatorId);
        vo.setUomId(this.uomId);
        vo.setDemandDate(this.demandDate);
        vo.setSoNum(this.soNum);
        vo.setSoLineNum(this.soLineNum);
        return vo;
    }
}
