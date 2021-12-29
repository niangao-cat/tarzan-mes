package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName HmeArea
 * @Description 区域信息
 * @Author lkj
 * @Date 2021/2/25
 */
@Data
@ApiModel("区域信息")
public class HmeAreaInfo {

    @ApiModelProperty("区域Id")
    private String areaId;

    @ApiModelProperty("区域编码")
    private String areaCode;

    @ApiModelProperty("区域名称")
    private String areaName;

}
