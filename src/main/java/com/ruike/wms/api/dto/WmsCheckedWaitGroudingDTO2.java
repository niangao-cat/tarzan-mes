package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/4/26 14:43
 * @Version 1.0
 */
@Data
public class WmsCheckedWaitGroudingDTO2 implements Serializable {
    private static final long serialVersionUID = -4389935572615538605L;

    @ApiModelProperty(value = "物料上架量")
    private BigDecimal materialStoragedNum;
    @ApiModelProperty(value = "每天的开始时间")
    private Date dailyTimeFrom;
    @ApiModelProperty(value = "每天的结束时间")
    private Date dailyTimeTo;
    @ApiModelProperty(value = "每月总上架量")
    private BigDecimal sumStoragedMonth;
    @ApiModelProperty(value = "每月平均上架时长")
    private BigDecimal avgTimeStoraged;
    @ApiModelProperty(value = "每月的开始时间")
    private Date monthTimeFrom;
    @ApiModelProperty(value = "每月的结束时间")
    private Date monthTimeTo;
}
