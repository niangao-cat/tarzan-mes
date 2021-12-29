package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WmsSoTransferDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 10:29
 * @Version 1.0
 **/
@Data
public class WmsSoTransferDTO implements Serializable {
    private static final long serialVersionUID = 1622507589652486619L;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "工厂")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private List<String> siteIds;

    @ApiModelProperty(value = "仓库")
    private String warehouseId;

    @ApiModelProperty(value = "货位")
    private String locatorId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "批次")
    private String lot;


}