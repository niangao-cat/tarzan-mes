package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionDTO5
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/10/15 21:21
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO5 implements Serializable {
    private static final long serialVersionUID = -40746041500851979L;

    @ApiModelProperty(value = "电流")
    private String  current;

    @ApiModelProperty(value = "功率")
    private Double  power;

    @ApiModelProperty(value = "功率类型")
    private String  powerSinglePoint;

}
