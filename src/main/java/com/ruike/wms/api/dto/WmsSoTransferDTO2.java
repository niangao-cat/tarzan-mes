package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WmsSoTransferDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 20:52
 * @Version 1.0
 **/
@Data
public class WmsSoTransferDTO2 implements Serializable {
    private static final long serialVersionUID = 6352626602635978428L;
    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "目标销售订单号")
    private String transferSoNum;

    @ApiModelProperty(value = "目标销售订单行号")
    private String transferSoLineNum;
}
