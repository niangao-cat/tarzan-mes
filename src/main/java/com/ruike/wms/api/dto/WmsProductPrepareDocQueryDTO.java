package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 成品备料单据 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/12 16:45
 */
@Data
public class WmsProductPrepareDocQueryDTO {
    @ApiModelProperty("出货单号，允许模糊查询")
    private String instructionDocNum;
    @ApiModelProperty("出货单ID")
    private String instructionDocId;
    @ApiModelProperty("工厂ID")
    private String siteId;
    @ApiModelProperty("客户Id")
    private String customerId;
    @ApiModelProperty(value = "预计送达时间")
    private Date expectedArrivalTime;
}
