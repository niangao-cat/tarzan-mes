package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WmsDistributionRevokeReturnDTO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/8 21:05
 * @Version 1.0
 **/
@Data
public class WmsDistributionRevokeReturnDTO3 implements Serializable {
    private static final long serialVersionUID = 5229477612361180485L;

    @ApiModelProperty("产线Id")
    private String prodLineId;

    @ApiModelProperty("产线编码")
    private String prodLineCode;

    @ApiModelProperty("产线")
    private String prodLineName;

    @ApiModelProperty("工段Id")
    private String workcellId;

    @ApiModelProperty("工段编码")
    private String workcellCode;

    @ApiModelProperty("工段")
    private String workcellName;
}
