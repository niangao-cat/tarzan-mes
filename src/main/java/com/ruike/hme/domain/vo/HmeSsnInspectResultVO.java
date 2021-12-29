package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/2/4 16:25
 */
@Data
public class HmeSsnInspectResultVO implements Serializable {

    private static final long serialVersionUID = 8905075678833253215L;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "标准件编码")
    private String standardSnCode;

    @ApiModelProperty(value = "物料id")
    private List<String> materialIdList;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "工作方式")
    private String workWay;

    @ApiModelProperty(value = "班组id")
    private String wkcShiftId;
}
