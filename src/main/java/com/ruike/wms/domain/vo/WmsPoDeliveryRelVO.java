package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/09/03 22:17
 */
@Data
public class WmsPoDeliveryRelVO implements Serializable {

    private static final long serialVersionUID = 4269717788480866069L;

    @ApiModelProperty(value = "送货单行Id")
    private String deliveryDocLineId;

    @ApiModelProperty(value = "行id对应的数量")
    private int count;
}
