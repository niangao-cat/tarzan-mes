package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName WmsMaterialReturnDetailDTO2
 * @Deacription 明细查询返回
 * @Author ywz
 * @Date 2020/4/23 9:39
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsMaterialReturnDetailDTO2 implements Serializable {
    private static final long serialVersionUID = 6073092828471315463L;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning", defaultMeaning = "无")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料单位编码")
    private String primaryUomCode;

    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "批")
    private String lot;

    @ApiModelProperty(value = "货位id")
    private String locatorId;

    @ApiModelProperty(value = "是否本次扫描")
    private String scanFlag;
}
