package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeWoJobSnDTO7
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/10/26 10:06
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnDTO7 implements Serializable {
    private static final long serialVersionUID = 8237676753096456510L;

    @ApiModelProperty(value = "物料批")
    private String materialLotId;

    @ApiModelProperty(value = "物料批")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private Double transferQuantity;

    @ApiModelProperty(value = "数量")
    private Double targetBarNum;

    @ApiModelProperty(value = "请求事件")
    private String requestId;

    @ApiModelProperty(value = "父请求事件")
    private String parentEventId;

}
