package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeNcDisposePlatformVO5
 *
 * @author: chaonan.hu@hand-china.com 2020-11-23 14:39:12
 **/
@Data
public class HmeNcDisposePlatformVO5 implements Serializable {
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

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "投入数量")
    private BigDecimal releaseQty;

    @ApiModelProperty(value = "申请数量")
    private BigDecimal applyQty;

    @ApiModelProperty(value = "不退料标识")
    private String noReturnMaterialFlag;

    @ApiModelProperty(value = "jobId,材料清单查询暂存，用于不良提交")
    private BigDecimal jobId;
}
