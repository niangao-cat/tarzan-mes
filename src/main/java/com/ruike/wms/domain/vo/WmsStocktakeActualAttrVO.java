package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.infra.annotation.ExtendAttrName;
import lombok.Data;

import java.util.Date;

/**
 * 盘点实绩拓展属性
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 11:23
 */
@Data
@ApiModel("盘点实绩拓展属性")
public class WmsStocktakeActualAttrVO {
    @ApiModelProperty("盘点实绩ID")
    private String stocktakeActualId;
    @ApiModelProperty("初盘人")
    @ExtendAttrName("FIRSTCOUNT_BY")
    private Long firstcountBy;
    @ApiModelProperty("初盘时间")
    @ExtendAttrName("FIRSTCOUNT_DATE")
    private Date firstcountDate;
    @ApiModelProperty("复盘人")
    @ExtendAttrName("RECOUNT_BY")
    private Long recountBy;
    @ApiModelProperty("复盘时间")
    @ExtendAttrName("RECOUNT_DATE")
    private Date recountDate;
    @ApiModelProperty("调整人")
    @ExtendAttrName("ADJUST_BY")
    private Long adjustBy;
    @ApiModelProperty("调整时间")
    @ExtendAttrName("ADJUST_DATE")
    private Date adjustDate;
}
