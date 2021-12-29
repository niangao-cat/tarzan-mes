package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName WmsDistributionRevokeReturnDTO5
 * @Description 明细
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/9 17:54
 * @Version 1.0
 **/
@Data
public class WmsDistributionRevokeReturnDTO5 implements Serializable {
    private static final long serialVersionUID = 6959220879409599848L;

    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("物料批")
    private String materialLotCode;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("数量")
    private Double primaryUomQty;

    @ApiModelProperty("单位")
    private String uomCode;

    @ApiModelProperty("批次")
    private String lot;

    @ApiModelProperty("状态")
    @LovValue(value = "WMS.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty("状态")
    private String statusMeaning;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("容器Id")
    private String containerId;

    @ApiModelProperty("容器Code")
    private String containerCode;

}
