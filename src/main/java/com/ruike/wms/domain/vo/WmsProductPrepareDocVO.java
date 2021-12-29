package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.Date;

/**
 * <p>
 * 成品备料单据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/12 16:41
 */
@Data
public class WmsProductPrepareDocVO {

    @ApiModelProperty("单据ID")
    private String instructionDocId;

    @ApiModelProperty("出货单号")
    private String instructionDocNum;

    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "WX.WMS.SO_DELIVERY_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态含义")
    private String instructionDocStatusMeaning;

    @ApiModelProperty("工厂ID")
    private String siteId;

    @ApiModelProperty("工厂名称")
    private String siteName;

    @ApiModelProperty("客户Id")
    private String customerId;

    @ApiModelProperty("客户编码")
    private String customerCode;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty(value = "预计送达时间")
    private Date expectedArrivalTime;

    @ApiModelProperty("客户地址")
    private String address;

    @ApiModelProperty("备注")
    private String remark;
}
