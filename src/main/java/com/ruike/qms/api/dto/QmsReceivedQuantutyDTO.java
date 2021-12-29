package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 收货数量DTO
 * @author: han.zhang
 * @create: 2020/04/30 11:53
 */
@Getter
@Setter
@ToString
public class QmsReceivedQuantutyDTO implements Serializable {

    private static final long serialVersionUID = -6749735779432957253L;

    @ApiModelProperty(value = "实际接收时间")
    private String actualReceiveDate;

    @ApiModelProperty(value = "实际接收数量")
    private Double actualReceiveQty;
}