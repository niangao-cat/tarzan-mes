package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SnQueryCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:34
 * @Version 1.0
 **/
@Data
public class CosQueryCollectItfReturnDTO1 implements Serializable {
    private static final long serialVersionUID = 7835507123375422234L;
    @ApiModelProperty(value = "芯片位置")
    private String cosPos;

    @ApiModelProperty(value = "热沉")
    private String hotSink;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "是否测试偏振度")
    private String polarization;

    @ApiModelProperty(value = "是否测试发散角")
    private String volatilization;

    @ApiModelProperty(value = "实验代码")
    private String labCode;
}
