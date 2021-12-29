package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SapMaterialVoucherDTO {

    @ApiModelProperty(value = "工厂ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "工厂编码", required = true)
    private String siteCode;
    @ApiModelProperty(value = "开始时间", required = true)
    private String startDate;
    @ApiModelProperty(value = "结束时间", required = true)
    private String endDate;
    @ApiModelProperty(value = "移动类型")
    private String moveType;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "仓库编码")
    private String locatorCode;
}
