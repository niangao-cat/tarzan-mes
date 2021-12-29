package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * COS检验工作台-自动查询数据 输入参数
 */
@Data
public class HmeCosInspectPlatformQueryInfoRequestVO implements Serializable {

    private static final long serialVersionUID = -9127577309580936050L;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("物料批条码")
    private String materialLotCode;

    @ApiModelProperty(value = "班组id")
    private String wkcShiftId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工单编码")
    private String workOrderNum;

    @ApiModelProperty("Wafer")
    private String wafer;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("设备ID")
    private String equipmentId;

    @ApiModelProperty("开始时间")
    private String beginDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("来料信息记录ID")
    private String operationRecordId;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;
}
