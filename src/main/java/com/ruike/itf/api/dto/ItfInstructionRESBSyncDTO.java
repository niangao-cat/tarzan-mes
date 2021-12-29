package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 采购订单明细DTO
 *
 * @author kejin.liu01@hand-china.com 2020/8/12 9:18
 */
@Data
public class ItfInstructionRESBSyncDTO{
    @ApiModelProperty(value = "")
    private String BDART;

    @ApiModelProperty(value = "")
    private String POTX1;

    @ApiModelProperty(value = "")
    private String POTX2;

    @ApiModelProperty(value = "")
    private String BWART;

    @ApiModelProperty(value = "工厂")
    private String WERKS;

    @ApiModelProperty(value = "指令数量")
    private BigDecimal BDMNG;

    @ApiModelProperty(value = "")
    private String RSART;

    @ApiModelProperty(value = "")
    private String SPLKZ;

    @ApiModelProperty(value = "")
    private String ENMNG;

    @ApiModelProperty(value = "")
    private String SGTXT;

    @ApiModelProperty(value = "")
    private String KDAUF;

    @ApiModelProperty(value = "")
    private String ABLAD;

    @ApiModelProperty(value = "")
    private String SORTF;

    @ApiModelProperty(value = "物料")
    private String MATNR;

    @ApiModelProperty(value = "")
    private String WEMPF;

    @ApiModelProperty(value = "")
    private String EBELN;

    @ApiModelProperty(value = "")
    private String XFEHL;

    @ApiModelProperty(value = "")
    private String SOBKZ;

    @ApiModelProperty(value = "")
    private String LGORT;

    @ApiModelProperty(value = "")
    private String EBELP;

    @ApiModelProperty(value = "单位")
    private String MEINS;

    @ApiModelProperty(value = "")
    private String KZEAR;

    @ApiModelProperty(value = "预留相关的编号")
    private String RSNUM;

    @ApiModelProperty(value = "")
    private String POSNR;

    @ApiModelProperty(value = "")
    private String RSSTA;

    @ApiModelProperty(value = "预留相关的项目编号")
    private String RSPOS;

    @ApiModelProperty(value = "")
    private String DUMPS;

    @ApiModelProperty(value = "")
    private String XLOEK;

    @ApiModelProperty(value = "")
    private String KDPOS;

    @ApiModelProperty(value = "")
    private String POSTP;

    @ApiModelProperty(value = "")
    private String BDTER;

    @ApiModelProperty(value = "")
    private String XWAOK;

    @ApiModelProperty(value = "")
    private String PO_NUMBER;

    @ApiModelProperty(value = "")
    private String PO_ITEM;

}

