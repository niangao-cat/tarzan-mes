package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName WmsSoTransferReturnDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 11:11
 * @Version 1.0
 **/
@Data
public class WmsSoTransferReturnDTO implements Serializable {

    private static final long serialVersionUID = 2099065420422848647L;
    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批")
    private String materialLotCode;

    @ApiModelProperty(value = "物料")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "WMS.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态")
    private String statusMeaning;

    @ApiModelProperty(value = "数量")
    private String qty;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "工厂")
    private String siteName;

    @ApiModelProperty(value = "仓库")
    private String warehouseCode;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;


}
