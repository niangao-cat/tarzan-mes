package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author tong.li
 * @Date 2020/6/10 19:32
 * @Version 1.0
 */
@Data
public class WmsPoDeliveryRelDTO implements Serializable {
    private static final long serialVersionUID = -3265124730359704406L;

    @ApiModelProperty(value = "送货单号")
    private String docId;
    @ApiModelProperty(value = "当天日期从")
    private Date nowDateFrom;
    @ApiModelProperty(value = "当天日期至")
    private Date nowDateTo;
}
