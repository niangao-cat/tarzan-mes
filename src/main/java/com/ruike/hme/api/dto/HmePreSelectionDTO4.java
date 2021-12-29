package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmePreSelectionDTO4
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/26 10:29
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO4 implements Serializable {
    private static final long serialVersionUID = 4197990283811734795L;
    @ApiModelProperty(value = "盒子Id")
    private String  materialLotLoadId;
    @ApiModelProperty(value = "盒子号")
    private String  materialLotCode;
    @ApiModelProperty(value = "盒子号Id")
    private String  materialLotId;
    @ApiModelProperty(value = "芯片料号")
    private String  materialId;
    @ApiModelProperty(value = "盒内位置")
    private String loadSequence;
    @ApiModelProperty(value = "盒内行")
    private String  loadRow;
    @ApiModelProperty(value = "盒内列")
    private String  loadColumn;
    @ApiModelProperty(value = "芯片数")
    private String  cosNum;
    @ApiModelProperty(value = "电流")
    private String  current;
    @ApiModelProperty(value = "芯片类型")
    private String  cosType;
    @ApiModelProperty(value = "热沉类型")
    private String  hotSinkCode;
    @ApiModelProperty(value = "功率类型")
    private String  a01;
    @ApiModelProperty(value = "功率")
    private Double  a02;
    @ApiModelProperty(value = "波长")
    private Double  a04;

    @ApiModelProperty(value = "功率差值")
    private BigDecimal powerDif;
}
