package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeNcDisposePlatformVO3
 *
 * @author: chaonan.hu@hand-china.com 2020-09-27 17:12:35
 **/
@Data
public class HmeNcDisposePlatformVO3 implements Serializable {
    private static final long serialVersionUID = -4199965397903985202L;

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

    @ApiModelProperty(value = "报废数量")
    private BigDecimal scrapQty;

    @ApiModelProperty(value = "待审核数量")
    private BigDecimal waitAuditQty;

    @ApiModelProperty(value = "申请数量")
    private BigDecimal applyQty;

    @ApiModelProperty(value = "颜色")
    private String color;


    @ApiModelProperty(value = "数量,仅用于后台逻辑")
    private BigDecimal qty;

    @ApiModelProperty(value = "不良状态,仅用于后台逻辑")
    private String ncStatus;
}
