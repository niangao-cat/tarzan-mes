package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/26 14:32
 */
@Data
public class HmeSelectionDetailsQueryVO implements Serializable {

    private static final long serialVersionUID = -9071696972697776116L;

    @ApiModelProperty(value = "器件序列号-EO标识")
    private String loadSequence;

    @ApiModelProperty(value = "虚拟器件号")
    private String virtualNum;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "器件序列号列表")
    private List<String> loadSequenceList;

    @ApiModelProperty(value = "装配开始时间")
    private String assemblyStartTime;

    @ApiModelProperty(value = "装配结束时间")
    private String assemblyEndTime;
}
