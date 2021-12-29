package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/17 14:55
 */
@Data
public class HmeCosFreezeTransferVO implements Serializable {

    private static final long serialVersionUID = 1768534300193225894L;

    @ApiModelProperty(value = "扫描条码")
    private String materialLotCode;

    @ApiModelProperty(value = "来源条码")
    private String transferMaterialLotCode;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "班组ID")
    private String wkcShiftId;

    @ApiModelProperty(value = "容器类型")
    private String transContainerType;

    @ApiModelProperty(value = "COS类型")
    private String transCosType;
}
