package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName WmsPoDeliveryConfirmDTO
 * @Deacription TODO
 * @Author ywz
 * @Date 2020/4/14 11:17
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsPoDeliveryConfirmDTO implements Serializable {

    private static final long serialVersionUID = -1937983087910544903L;

    @ApiModelProperty(value = "送货单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "接收批次号")
    private String number;

//    @ApiModelProperty(value ="siteId")
//    private String siteId;
}
