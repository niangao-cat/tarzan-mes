package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmePumpPreSelectionVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/08/31 09:31:21
 **/
@Data
public class HmePumpPreSelectionVO2 implements Serializable {
    private static final long serialVersionUID = 9067486469614138936L;

    @ApiModelProperty(value = "预挑选头主键")
    private String pumpPreSelectionId;

    @ApiModelProperty(value = "筛选批次")
    private String selectionLot;

    @ApiModelProperty(value = "组合物料ID")
    private String combMaterialId;

    @ApiModelProperty(value = "组合物料编码")
    private String combMaterialCode;

    @ApiModelProperty(value = "组合物料描述")
    private String combMaterialName;

    @ApiModelProperty(value = "bomId")
    private String bomId;

    @ApiModelProperty(value = "BOM版本号")
    private String revision;

    @ApiModelProperty(value = "套数")
    private Long setsNum;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME.PUMP_SELECT_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;
}
