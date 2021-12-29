package com.ruike.itf.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 成品sn接口传参
 *
 * @author li.zhang 2021/07/01 11:39
 */
@Data
public class ItfSapSnIfaceDto {

    @ApiModelProperty(value = "SITE_CODE")
    @JsonProperty("B_WERK")
    private String siteCode;
    @ApiModelProperty(value = "SN")
    @JsonProperty("SERNR")
    private String sn;
    @ApiModelProperty(value = "MATERIAL_CODE")
    @JsonProperty("MATNR")
    private String materialCode;
    @ApiModelProperty(value = "UOM_CODE")
    @JsonProperty("MEINS")
    private String uomCode;
    @ApiModelProperty(value = "INVENTORY_TYPE")
    @JsonProperty("LBBSA")
    private String inventoryType;
    @ApiModelProperty(value = "WAREHOUSE_CODE")
    @JsonProperty("B_LAGER")
    private String warehouseCode;
    @ApiModelProperty(value = "SO_NUM")
    @JsonProperty("KDAUF")
    private String soNum;
    @ApiModelProperty(value = "SO_LINE_NUM")
    @JsonProperty("KDPOS")
    private String soLineNum;

}
