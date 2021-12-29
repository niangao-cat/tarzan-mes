package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * COS芯片检验入参
 */
@Data
public class HmeCosInspectPlatformCosInspectRequestVO implements Serializable {
    private static final long serialVersionUID = -2603440996566337503L;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("物料批条码")
    private String materialLotCode;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("班组ID")
    private String wkcShiftId;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("工单工艺工位在制记录id")
    private String cosRecord;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("行序号")
    private String loadSequence;
}
