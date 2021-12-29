package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @description COS挑选查询接口返回列表1 - 芯片信息列表
 * @author 田欣
 * @email xin.t@raycuslaser.com
 * @date 2021/9/24
 * @time 18:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosSelectShowDTO implements Serializable {

    private static final long serialVersionUID = 1785138707364138478L;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "物料")
    private String materialCode;

    @ApiModelProperty(value = "旧位置")
    private String oldLoad;

    @ApiModelProperty(value = "位置")
    private String load;

    @ApiModelProperty(value = "行")
    private String loadRow;

    @ApiModelProperty(value = "列")
    private String loadColumn;

    @ApiModelProperty(value = "排序")
    private String cosPos;

    @ApiModelProperty(value = "热沉")
    private String hotSink;

    @ApiModelProperty(value = "总数")
    private String totalNum;
}
