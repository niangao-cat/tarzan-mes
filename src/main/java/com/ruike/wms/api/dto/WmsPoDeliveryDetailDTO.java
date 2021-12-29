package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName WmsPoDeliveryDetailDTO
 * @Deacription TODO
 * @Author ywz
 * @Date 2020/4/14 14:30
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsPoDeliveryDetailDTO implements Serializable {
    private static final long serialVersionUID = 8897684072993154257L;
    @ApiModelProperty(value = "送货单行id")
    private String instructionId;

    @ApiModelProperty(value = "扫描条码")
    private String materialLotCode;

}
