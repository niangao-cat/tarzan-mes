package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/19 14:01
 */
@Data
public class HmeCosRetestVO2 implements Serializable {

    private static final long serialVersionUID = 6169197543980360602L;

    @ApiModelProperty(value = "条码id")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "waferNum")
    private String waferNum;

    @ApiModelProperty(value = "物料数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "条码批次")
    private String lot;

    @ApiModelProperty(value = "COS类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty(value = "COS类型含义")
    private String cosTypeMeaning;
}
