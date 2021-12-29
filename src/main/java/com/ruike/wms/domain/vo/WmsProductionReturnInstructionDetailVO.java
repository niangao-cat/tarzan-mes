package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/13 16:51
 */
@Data
public class WmsProductionReturnInstructionDetailVO implements Serializable {

    private static final long serialVersionUID = -2937693201482826518L;

    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("数量")
    private Double quality;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("货位Id")
    private String toLocatorId;
    @ApiModelProperty("货位编码")
    private String toLocatorCode;
    @ApiModelProperty("质量状态")
    private String qualityStatus;
    @ApiModelProperty("质量状态意义")
    private String qualityStatusMeaning;
    @ApiModelProperty("有效性")
    private String enableFlag;
    @ApiModelProperty("有效性意义")
    private String enableFlagMeaning;
    @ApiModelProperty("当前容器Id")
    private String currentContainerId;
    @ApiModelProperty("当前容器编码")
    private String currentContainerCode;
    @ApiModelProperty("能否选中删除")
    private String selected;

}
