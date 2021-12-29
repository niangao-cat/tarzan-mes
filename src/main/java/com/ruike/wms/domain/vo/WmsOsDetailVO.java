package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 外协发货查询明细的VO
 * @author: han.zhang
 * @create: 2020/06/24 11:50
 */
@Getter
@Setter
@ToString
public class WmsOsDetailVO implements Serializable {
    private static final long serialVersionUID = 4972536430422354071L;

    @ApiModelProperty(value = "指令实绩id")
    private String actualId;

    @ApiModelProperty(value = "指令实绩明细id")
    private String actualDetailId;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "指令实绩数量")
    private String actualQty;

    @ApiModelProperty(value = "单位id")
    private String uomId;

    @ApiModelProperty(value = "物料编码")
    private String uomCode;

    @ApiModelProperty(value = "物料描述")
    private String uomName;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS",meaningField ="qualityStatusMeaning" )
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "物料批条码")
    private String materialLotCode;

    @ApiModelProperty(value = "条码数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "条码状态")
    @LovValue(value = "WMS.MTLOT.STATUS",meaningField ="materialLotStatusMeaning" )
    private String materialLotStatus;

    @ApiModelProperty(value = "条码状态含义")
    private String materialLotStatusMeaning;
}