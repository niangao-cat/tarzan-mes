package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SnQueryCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:34
 * @Version 1.0
 **/
@Data
public class SnQueryCollectItfReturnDTO1 implements Serializable {
    @ApiModelProperty(value = "设备资产编码")
    private String tagId;

    @ApiModelProperty(value = "最小值")
    private String minimumValue;

    @ApiModelProperty(value = "最大值")
    private String maximalValue;

    @ApiModelProperty(value = "标准值")
    private String standardValue;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;

    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;

    @ApiModelProperty(value = "数据项类型编码")
    private String attrValue;

    @ApiModelProperty(value = "COS位置")
    private String cosPos;

    @ApiModelProperty(value = "电流")
    private String current;

    @ApiModelProperty(value = "时长")
    private String duration;


}
