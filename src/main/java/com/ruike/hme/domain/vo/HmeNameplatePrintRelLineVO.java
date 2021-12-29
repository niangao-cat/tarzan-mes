package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 铭牌打印配置属性维护显示字段
 *
 * @author wengang.qiang@hand-china.com 2021/10/12 13:48
 */
@Data
public class HmeNameplatePrintRelLineVO implements Serializable {

    private static final long serialVersionUID = 2725677115112869874L;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "行表主键ID")
    private String nameplateLineId;

    @ApiModelProperty(value = "头表主键id")
    private String nameplateHeaderId;
}
