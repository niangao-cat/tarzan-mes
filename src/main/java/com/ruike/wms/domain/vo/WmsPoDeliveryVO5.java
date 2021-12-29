package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * WmsPoDeliveryVO5
 *
 * @author: chaonan.hu@hand-china.com
 **/
@Data
public class WmsPoDeliveryVO5 implements Serializable {
    private static final long serialVersionUID = -6152593230183706920L;

    @ApiModelProperty(value = "是否通过校验标识")
    private boolean flag;
}
