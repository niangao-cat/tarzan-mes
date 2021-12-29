package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmePumpPreSelectionDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021/08/31 10:05:14
 **/
@Data
public class HmePumpPreSelectionDTO3 implements Serializable {
    private static final long serialVersionUID = 5955198015651034476L;

    @ApiModelProperty(value = "挑选批次")
    private String pumpPreSelectionId;

    @ApiModelProperty(value = "原容器")
    private String oldContainerCode;

    @ApiModelProperty(value = "目标容器")
    private String newContainerCode;

    @ApiModelProperty(value = "泵浦源条码")
    private String pumpMaterialLotCode;

    @ApiModelProperty(value = "筛选时间从")
    private Date creationDateFrom;

    @ApiModelProperty(value = "筛选时间至")
    private Date creationDateTo;


    @ApiModelProperty(value = "原容器ID,后端自用")
    private String oldContainerId;

    @ApiModelProperty(value = "目标容器ID,后端自用")
    private String newContainerId;
}
