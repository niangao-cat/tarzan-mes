package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName WmsMaterialReturnScanDTO2
 * @Deacription 实物条码返回值
 * @Author ywz
 * @Date 2020/4/20 14:58
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsMaterialReturnScanDTO2 implements Serializable {

    private static final long serialVersionUID = -1398387200533753373L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "版本")
    private String materialVersion;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "执行数量")
    private BigDecimal executeQty;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;

    @ApiModelProperty(value = "货位")
    private String toLocatorCode;

    @ApiModelProperty(value = "lot")
    private String lot;

    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning", defaultMeaning = "无")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "亮灯关灯接口返回标识")
    private String status;
    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "亮灯状态")
    private String taskStatus;
    @ApiModelProperty(value = "行Id")
    private String instructionId;
}
