package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 设备台帐接口返回
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/8 16:09
 */
@Data
public class ItfEquipmentReturnVO {
    @ApiModelProperty("接口表ID")
    private String interfaceId;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "处理时间")
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;
}
