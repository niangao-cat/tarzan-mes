package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 数据采集项job明细
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 13:58
 */
@Data
public class HmeProcessJobDetailVO {
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "组件物料编码")
    private String materialCode;
    @ApiModelProperty(value = "组件物料条码")
    private String materialLotCode;
    @ApiModelProperty(value = "组件物料批次")
    private String lot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "投料数量")
    private BigDecimal releaseQty;
}
