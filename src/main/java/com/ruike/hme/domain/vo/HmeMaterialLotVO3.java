package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.math.*;
import java.util.*;

/**
 * @Classname HmeMaterialLotVO3
 * @Description 物料批信息视图
 * @Date 2020/8/25 9:19
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotVO3 extends MtMaterialLot {

    private static final long serialVersionUID = -8664737042384486780L;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;

    @ApiModelProperty(value = "指定工艺路线返修标识")
    private String designedReworkFlag;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名")
    private String materialName;

    @ApiModelProperty(value = "soNum")
    private String soNum;

    @ApiModelProperty(value = "soLineNum")
    private String soLineNum;

    @ApiModelProperty(value = "产品版本")
    private String productionVersion;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料单位ID")
    private String materialPrimaryUomId;

    @ApiModelProperty(value = "物料单位编码")
    private String materialPrimaryUomCode;

    @ApiModelProperty(value = "条码创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "条码剩余可投数量")
    private BigDecimal remainPrimaryUomQty;

    @ApiModelProperty(value = "条码是否已经投完")
    private boolean releasedAll;

    @ApiModelProperty(value = "条码在制标识")
    private String afFlag;

    @ApiModelProperty(value = "完工生产版本")
    private String completeProductionVersion;

    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
}