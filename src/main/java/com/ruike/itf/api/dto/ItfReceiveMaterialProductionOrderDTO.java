package com.ruike.itf.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ItfReceiveMaterialProductionOrderDTO implements Serializable {
    private static final long serialVersionUID = -6025801022333173485L;
    @ApiModelProperty(value = "单据编号")
    @JsonProperty(value = "ZDONU")
    private String zdonu;

    @ApiModelProperty(value = "工厂")
    @JsonProperty(value = "WERKS")
    private String werks;

/*    @ApiModelProperty(value = "头状态")
    @JsonProperty(value = "INSTRUCTION_DOC_STATUS")
    private String instructionDocStatus;*/

    @ApiModelProperty(value = "单据类型")
    @JsonProperty(value = "ZDOTY")
    private String zdoty;

    @ApiModelProperty(value = "申请人/领料人")
    @JsonProperty(value = "ZCEAT")
    private String zceat;

    @ApiModelProperty(value = "备注")
    @JsonProperty(value = "ZBUMEN")
    private String zbumen;

    @ApiModelProperty(value = "生产订单号")
    @JsonProperty(value = "AUFNR")
    private String aufnr;

/*    @ApiModelProperty(value = "来源系统")
    @JsonProperty(value = "ZDONU-ZPSELP")
    private String zdonu_zpselp;*/

/*    @ApiModelProperty(value = "指令编号")
    @JsonProperty(value = "ZDONU-ZPSELP")
    private String zdonu_zpselp;*/

    @ApiModelProperty(value = "行号")
    @JsonProperty(value = "ZPSELP")
    private String zpselp;

    @ApiModelProperty(value = "物料")
    @JsonProperty(value = "MATNR")
    private String matnr;

    @ApiModelProperty(value = "需求数量")
    @JsonProperty(value = "MENGE")
    private String menge;

    @ApiModelProperty(value = "单位")
    @JsonProperty(value = "MEINS")
    private String meins;

/*
    @ApiModelProperty(value = "指令类型")
    @JsonProperty(value = "INSTRUCTION_TYPE")
    private String instructionType;
*/

/*    @ApiModelProperty(value = "来源工厂")
    @JsonProperty(value = "WERKS")
    private String werks;

    @ApiModelProperty(value = "目标工厂")
    @JsonProperty(value = "WERKS")
    private String werks;*/

    @ApiModelProperty(value = "行状态")
    @JsonProperty(value = "ZCOND")
    private String zcond;

    @ApiModelProperty(value = "来源仓库")
    @JsonProperty(value = "LGORT")
    private String lgort;

    @ApiModelProperty(value = "目标仓库")
    @JsonProperty(value = "UMLGO")
    private String umlgo;

/*
    @ApiModelProperty(value = "超发设置")
    @JsonProperty(value = "EXCESS_SETTING")
    private String excessSetting;
*/

    @ApiModelProperty(value = "行备注")
    @JsonProperty(value = "ZTEXT")
    private String ztext;

    @ApiModelProperty(value = "销售订单")
    @JsonProperty(value = "KDAUF")
    private String kdauf;

    @ApiModelProperty(value = "销售订单行号")
    @JsonProperty(value = "KDPOS")
    private String kdpos;

    @ApiModelProperty(value = "预留项目号")
    @JsonProperty(value = "RSNUM")
    private String rsnum;

    @ApiModelProperty(value = "预留项目行号")
    @JsonProperty(value = "RSPOS")
    private String rspos;

    @ApiModelProperty(value = "物料版本")
    @JsonProperty(value = "POTX1")
    private String potx1;

    @ApiModelProperty(value = "特殊库存，销售订单库存")
    @JsonProperty(value = "SOBKZ")
    private String sobkz;

}
