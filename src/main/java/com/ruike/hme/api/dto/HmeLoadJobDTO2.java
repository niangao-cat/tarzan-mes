package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * HmeLoadJobDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/2/3 16:04:45
 **/
@Data
public class HmeLoadJobDTO2 implements Serializable {
    private static final long serialVersionUID = -8622917846973967950L;

    @ApiModelProperty(value = "作业类型")
    private String loadJobType;

    @ApiModelProperty(value = "物料编码")
    private List<String> materialCodeList;

    @ApiModelProperty(value = "条码")
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "热沉编码")
    private List<String> hotSinkCodeList;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "工单")
    private List<String> workNumList;

    @ApiModelProperty(value = "wafer")
    private List<String> waferList;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "投料物料条码")
    private String bomMaterialLotCode;

    @ApiModelProperty(value = "创建人")
    private String id;

    @ApiModelProperty(value = "创建时间从")
    private String creationDateFrom;

    @ApiModelProperty(value = "创建时间至")
    private String creationDateTo;

}
