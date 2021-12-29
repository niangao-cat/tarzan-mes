package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 配送需求数量
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 11:40
 */
@Data
public class WmsDistributionDemandQtyVO {
    @ApiModelProperty("配送需求ID")
    private String distDemandId;
    @ApiModelProperty("班次ID")
    private String calendarShiftId;
    @ApiModelProperty("班次日期")
    private Date shiftDate;
    @ApiModelProperty("班次编码")
    private String shiftCode;
    @ApiModelProperty("次序")
    private Long sequence;
    @ApiModelProperty("可编辑")
    private Integer editableFlag;
    @ApiModelProperty("需求数量")
    private BigDecimal requestQty;
    @ApiModelProperty("已配送数量")
    private BigDecimal deliveredQty;
    @ApiModelProperty("库存数量")
    private BigDecimal distributionQty;
    @ApiModelProperty("剩余配送数量")
    private BigDecimal remainQty;
}
