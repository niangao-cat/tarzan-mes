package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @description COS接口2传入参数
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/1/19
 * @time 11:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosTwoShowDTO implements Serializable {

    private static final long serialVersionUID = -1365569264856190369L;

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
