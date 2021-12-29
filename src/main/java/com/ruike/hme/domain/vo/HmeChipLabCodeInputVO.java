package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeChipLabCodeInputVO
 *
 * @author: chaonan.hu@hand-china.com 2021-11-01 11:17:34
 **/
@Data
public class HmeChipLabCodeInputVO implements Serializable {
    private static final long serialVersionUID = -624626926990303337L;

    @ApiModelProperty("盒子ID")
    private String materialLotId;

    @ApiModelProperty("盒子号")
    private String materialLotCode;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("芯片类型")
    private String cosType;

    @ApiModelProperty("Wafer")
    private String wafer;

    @ApiModelProperty("盒内总数")
    private BigDecimal primaryUomQty;

    @ApiModelProperty("盒子实验代码")
    private String barcodeLabCode;

    @ApiModelProperty("盒子实验备注")
    private String barcodeLabRemark;

    @ApiModelProperty("芯片容量")
    private String chipNum;

    @ApiModelProperty("行数")
    private String locationRow;

    @ApiModelProperty("列数")
    private String locationColumn;

    @ApiModelProperty("装载信息")
    private List<HmeChipLabCodeInputVO2> materialLotLoadList;
}
