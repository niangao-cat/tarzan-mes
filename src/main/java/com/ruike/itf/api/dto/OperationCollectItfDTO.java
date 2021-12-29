package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OperationCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/17 19:36
 * @Version 1.0
 **/
@Data
public class OperationCollectItfDTO implements Serializable {

    private static final long serialVersionUID = 4379841695254634639L;

    @ApiModelProperty(value = "SN")
    private String sn;

    @ApiModelProperty(value = "物料")
    private String materialCode;

    @ApiModelProperty(value = "编码值")
    private String tagCode;

    @ApiModelProperty(value = "设备资产编码")
    private String cosPosition;

    @ApiModelProperty(value = "位置")
    private String current;

    @ApiModelProperty(value = "测试模式")
    private String type;

    @ApiModelProperty(value = "阈值电流")
    private String tagType;

    @ApiModelProperty(value = "阈值电压")
    private String operationCode;

    @ApiModelProperty(value = "电流")
    private String result;

    @ApiModelProperty(value = "标志")
    private String  reworkFlag;

    @ApiModelProperty(value = "时长")
    private String  duration;

    @ApiModelProperty(value = "处理消息")
    private String processMessage;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;

    @ApiModelProperty(value = "处理人")
    private String realName;

    @ApiModelProperty(value = "处理人")
    private Long createdBy;

    @ApiModelProperty(value = "处理账号")
    private String loginName;

}
