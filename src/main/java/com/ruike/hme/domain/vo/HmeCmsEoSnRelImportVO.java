package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxia.huang@raycus-china.com 2021/9/28 14:05
 */
@Data
public class HmeCmsEoSnRelImportVO {

    @ApiModelProperty(value = "设备")
    private String equipmentNum;

    @ApiModelProperty(value = "盖板SN")
    private String snNum;

    @ApiModelProperty(value = "光纤EOId")
    private String eoId;

    @ApiModelProperty(value = "光纤EO")
    private String identification;


}
