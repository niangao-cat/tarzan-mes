package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeWoJobSnDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/12 13:54
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnDTO implements Serializable {

    private static final long serialVersionUID = 1760184181393319488L;
    @ApiModelProperty(value = "产线Id")
    private String prodLineId;

    @ApiModelProperty("工艺id")
    private String operationId;

    @ApiModelProperty("工位id")
    private String workcellId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("WAFER")
    private String wafer;

    @ApiModelProperty("来源条码号")
    private String sourceMaterialLotCode;

    @ApiModelProperty("来料开始时间")
    private String creationDateFrom;

    @ApiModelProperty("来料结束时间")
    private String creationDateTo;
}
