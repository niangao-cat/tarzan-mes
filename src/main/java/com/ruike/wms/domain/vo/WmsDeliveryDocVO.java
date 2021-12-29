package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * WmsDeliveryDocVO
 *
 * @author: chaonan.hu@hand-china.com 2020/09/10 13:56:23
 **/
@Data
public class WmsDeliveryDocVO implements Serializable {
    private static final long serialVersionUID = -5597980900609675901L;

    @ApiModelProperty(value = "明细Id")
    private String actualDetailId;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "条码数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "接收时间")
    private Date creationDate;

    @ApiModelProperty(value = "接收人")
    private String createdBy;

    @ApiModelProperty(value = "接收人姓名")
    private String createdByName;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "WMS.MTLOT.STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;
}
