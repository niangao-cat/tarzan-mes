package com.ruike.itf.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ItfSendOutReturnNoticeDTO implements Serializable {
    @ApiModelProperty(value = "单据编号")
    @JsonProperty(value = "VBELN")
    private String vbeln;

/*    @ApiModelProperty(value = "单据类型")
    @JsonProperty(value = "INSTRUCTION_DOC_TYPE")
    private String instructionDocType;*/

    @ApiModelProperty(value = "SAP单据类型")
    @JsonProperty(value = "LFART")
    private String lfart;

    @ApiModelProperty(value = "单据类型描述")
    @JsonProperty(value = "VTEXT")
    private String vtext;

/*    @ApiModelProperty(value = "单据状态")
    @JsonProperty(value = "INSTRUCTION_DOC_STATUS")
    private String instructionDocStatus;*/

    @ApiModelProperty(value = "工厂编码")
    @JsonProperty(value = "WERKS")
    private String werks;

    @ApiModelProperty(value = "客户编码")
    @JsonProperty(value = "KUNNR")
    private String kunnr;

    @ApiModelProperty(value = "实际发货日期")
    @JsonProperty(value = "WADAT")
    private String wadat;

    @ApiModelProperty(value = "销售组织编码")
    @JsonProperty(value = "VKORG")
    private String vkorg;

/*    @ApiModelProperty(value = "来源系统")
    @JsonProperty(value = "SOURCE_SYSTEM")
    private String sourceSystem;*/

/*    @ApiModelProperty(value = "指令编码")
    @JsonProperty(value = "VBELN-POSNR")
    private String vbeln_posnr;*/

/*    @ApiModelProperty(value = "指令类型")
    @JsonProperty(value = "INSTRUCTION_TYPE")
    private String instructionType;*/

    @ApiModelProperty(value = "指令状态")
    @JsonProperty(value = "KOSTA")
    private String kosta;

    @ApiModelProperty(value = "物料编码")
    @JsonProperty(value = "MATNR")
    private String matnr;

    @ApiModelProperty(value = "物料批管理标识")
    @JsonProperty(value = "XCHPF")
    private String xchpf;

    @ApiModelProperty(value = "物料版本")
    @JsonProperty(value = "AESKD")
    private String aeskd;

    @ApiModelProperty(value = "需求数量")
    @JsonProperty(value = "LGMNG")
    private String lgmng;

    @ApiModelProperty(value = "单位编码")
    @JsonProperty(value = "MEINS")
    private String meins;

/*    @ApiModelProperty(value = "发货工厂")
    @JsonProperty(value = "WERKS")
    private String werks;*/

    @ApiModelProperty(value = "发货仓库")
    @JsonProperty(value = "LGORT")
    private String lgort;

/*    @ApiModelProperty(value = "收货工厂")
    @JsonProperty(value = "TO_SITE_CODE")
    private String toSiteCode;*/

/*    @ApiModelProperty(value = "收货仓库")
    @JsonProperty(value = "TO_LOCATOR_CODE")
    private String toLocatorCode;*/

    @ApiModelProperty(value = "行备注")
    @JsonProperty(value = "ZTEXT")
    private String ztext;

    @ApiModelProperty(value = "销售订单备注")
    @JsonProperty(value = "ZTEXT1")
    private String ztext1;

    @ApiModelProperty(value = "销售订单号")
    @JsonProperty(value = "KDAUF")
    private String kdauf;

    @ApiModelProperty(value = "销售订单行号")
    @JsonProperty(value = "KDPOS")
    private String kdpos;

    @ApiModelProperty(value = "行号")
    @JsonProperty(value = "POSNR")
    private String  posnr;

    @ApiModelProperty(value = "特殊库存标识")
    @JsonProperty(value = "SOBKZ")
    private String sobkz;

    @ApiModelProperty(value = "SAP行类型")
    @JsonProperty(value = "PSTYV")
    private String pstyv;

    @ApiModelProperty(value = "SAP行类型描述")
    @JsonProperty(value = "VTEXT1")
    private String vtext1;

    @ApiModelProperty(value = "客户物料")
    @JsonProperty(value = "KDMAT")
    private String kdmat;
}
