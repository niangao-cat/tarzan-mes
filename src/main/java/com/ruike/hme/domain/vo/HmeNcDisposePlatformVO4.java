package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeNcDisposePlatformVO4
 *
 * @author: chaonan.hu@hand-china.com 2020-11-23 14:36:23
 **/
@Data
public class HmeNcDisposePlatformVO4 implements Serializable {
    private static final long serialVersionUID = -5661130007229359318L;

    @ApiModelProperty(value = "组件料号ID")
    private String materialId;

    @ApiModelProperty(value = "组件料号编码")
    private String materialCode;

    @ApiModelProperty(value = "组件料号名称")
    private String materialName;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "条码数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "投入数量")
    private BigDecimal releaseQty;

    @ApiModelProperty(value = "报废数量")
    private BigDecimal scrapQty;

    @ApiModelProperty(value = "待审核数量")
    private BigDecimal waitAuditQty;

    @ApiModelProperty(value = "申请数量")
    private BigDecimal applyQty;

    @ApiModelProperty(value = "颜色")
    private String color;

    private List<HmeNcDisposePlatformVO5> detailList;

    @ApiModelProperty(value = "不退料标识")
    private String noReturnMaterialFlag;

    @ApiModelProperty(value = "报废装载信息位置")
    private List<String> materialLotLoadIdList;

    @ApiModelProperty(value = "装配标识 1-在装配清单内 0-不在")
    private Integer assemblyFlag;

    @ApiModelProperty(value = "提示信息")
    private String warnMessage;
}
