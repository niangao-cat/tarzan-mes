package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
* @Classname WmsStockTransferVO
* @Description 库存调拨-通过行ID查询条码信息VO
* @Date  2020/6/8 13:48
* @Created by Deng xu
*/
@Data
public class WmsStockTransferVO implements Serializable {

    private static final long serialVersionUID = -4324527450405850696L;

    @ApiModelProperty(value = "调拨单据行ID")
    private String instructionId;

    @ApiModelProperty(value = "行号")
    private Long lineNum;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "WMS.MTLOT.STATUS", meaningField = "lotStatusMeaning")
    private String lotStatus;

    @ApiModelProperty(value = "状态描述")
    private String lotStatusMeaning;

    @ApiModelProperty(value = "数量")
    private String actualQty;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;

    @ApiModelProperty(value = "仓库")
    private String parentLocatorCode;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性描述")
    private String enableFlagMeaning;

}
