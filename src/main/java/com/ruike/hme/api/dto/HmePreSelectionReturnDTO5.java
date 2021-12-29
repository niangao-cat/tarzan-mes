package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionReturnDTO5
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/10/16 17:02
 * @Version 1.0
 **/
@Data
public class HmePreSelectionReturnDTO5 implements Serializable {
    private static final long serialVersionUID = -8248489093290828355L;

    @ApiModelProperty(value = "盒子Id")
    private String materialLotId;

    @ApiModelProperty(value = "盒子编码")
    private String materialLotCode;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "数量")
    private String primaryUomQty ;

    @ApiModelProperty(value = "库位")
    private String locatorCode;

    @ApiModelProperty(value = "料号")
    private String materialCode;

    @ApiModelProperty(value = "剩余芯片数")
    private Long cosNum ;

    @ApiModelProperty(value = "未被筛选的数量")
    private Long notPreparaNum;

    @ApiModelProperty(value = "WAFER")
    private String wafer;
}
