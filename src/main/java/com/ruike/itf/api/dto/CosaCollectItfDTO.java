package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;

/**
 * @ClassName CosaCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/1/21 15:10
 * @Version 1.0
 **/
@Data
public class CosaCollectItfDTO implements Serializable {
    private static final long serialVersionUID = -5457851724785319210L;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "来源盒子号")
    private String sourceMaterialLotCode;
    @ApiModelProperty(value = "来源位置")
    private String sourceLoad;
    @ApiModelProperty(value = "目标盒子号")
    private String targetMaterialLotCode;
    @ApiModelProperty(value = "目标位置")
    private String targetLoad;
    @ApiModelProperty(value = "目标COS位置")
    private String targetCosPos;
    @ApiModelProperty(value = "")
    private String cosaAttribute1;
    @ApiModelProperty(value = "")
    private String cosaAttribute2;
    @ApiModelProperty(value = "")
    private String cosaAttribute3;
    @ApiModelProperty(value = "")
    private String cosaAttribute4;
    @ApiModelProperty(value = "")
    private String cosaAttribute5;
    @ApiModelProperty(value = "")
    private String cosaAttribute6;
    @ApiModelProperty(value = "")
    private String cosaAttribute7;
    @ApiModelProperty(value = "")
    private String cosaAttribute8;
    @ApiModelProperty(value = "")
    private String cosaAttribute9;
    @ApiModelProperty(value = "")
    private String cosaAttribute10;
    @ApiModelProperty(value = "")
    private String cosaAttribute11;
    @ApiModelProperty(value = "")
    private String cosaAttribute12;
    @ApiModelProperty(value = "")
    private String cosaAttribute13;
    @ApiModelProperty(value = "")
    private String cosaAttribute14;
    @ApiModelProperty(value = "")
    private String cosaAttribute15;
}
