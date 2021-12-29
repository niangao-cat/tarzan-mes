package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * @Classname HmeMaterialLotVO4
 * @Description 物料批信息视图
 * @Date 2020/8/25 9:19
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotVO4 extends MtMaterialLot {

    private static final long serialVersionUID = -237976938733738029L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名")
    private String materialName;

    @ApiModelProperty(value = "soNum")
    private String soNum;

    @ApiModelProperty(value = "soLineNum")
    private String soLineNum;

    @ApiModelProperty(value = "物料单位ID")
    private String materialPrimaryUomId;

    @ApiModelProperty(value = "物料单位编码")
    private String materialPrimaryUomCode;

    @ApiModelProperty(value = "货位编码")
    private String locatorCode;

    @ApiModelProperty(value = "库位ID")
    private String warehouseId;

    @ApiModelProperty(value = "库位编码")
    private String warehouseCode;
}