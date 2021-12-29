package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/10 14:12
 */
@Data
public class WmsPurchaseReturnCodeVO extends MtMaterialLot implements Serializable {

    private static final long serialVersionUID = 7797285827252317529L;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "MT.MTLOT.STATUS", meaningField = "codeStatusMeaning")
    private String codeStatus;

    @ApiModelProperty(value = "状态含义")
    private String codeStatusMeaning;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "在制品")
    private String mfFlag;

    @ApiModelProperty(value = "仓库")
    private String parentLocatorId;

    @ApiModelProperty(value = "容器id")
    private String containerId;

}
