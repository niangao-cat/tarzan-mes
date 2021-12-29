package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @Classname HmeInStorageMaterialVO
* @Description 半成品/成品入库扫描 物料信息VO
* @Date  2020/6/2 18:53
* @Created by Deng xu
*/
@Data
public class HmeInStorageMaterialVO implements Serializable {


    private static final long serialVersionUID = 902847104381800971L;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("物料批编码")
    private String materialLotCode;

    @ApiModelProperty("库位ID")
    private String locatorId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty("主单位ID")
    private String primaryUomId;

    @ApiModelProperty("物料批状态")
    private String qualityStatus;

    @ApiModelProperty("物料批状态描述")
    private String qualityStatusDes;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("站点")
    private String siteId;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("物料id+物料版本")
    private String materialVerStr;

    @ApiModelProperty("容器id")
    private String containerId;

    @ApiModelProperty("指令头id")
    private String sourceDocId;

    @ApiModelProperty("指令行id")
    private String sourceDocLineId;

    @ApiModelProperty("父层货位")
    private String parentLocatorId;

    @ApiModelProperty("仓库ID")
    private String warehouseId;

}
