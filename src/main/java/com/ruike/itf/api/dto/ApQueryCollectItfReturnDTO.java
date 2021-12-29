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
public class ApQueryCollectItfReturnDTO implements Serializable {
    private static final long serialVersionUID = 3972513014851467783L;
    @ApiModelProperty(value = "sn编码")
    private String materialLotCode;

    @ApiModelProperty(value = "电流")
    private String current;

    @ApiModelProperty(value = "时长")
    private String duration;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialDescription;

    @ApiModelProperty(value = "处理消息")
    private String processMessage;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;

}
