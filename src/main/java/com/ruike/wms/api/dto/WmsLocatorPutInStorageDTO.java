package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * WmsLocatorPutInStorageDTO
 *
 * @author liyuan.lv@hand-china.com 2020/04/06 18:09
 */
@ApiModel("货位信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsLocatorPutInStorageDTO {
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位Code")
    private String locatorCode;
    @ApiModelProperty("货位名称")
    private String locatorName;
    @ApiModelProperty("工厂id")
    private String siteId;
    @ApiModelProperty("工厂ode")
    private String siteCode;
    @ApiModelProperty("工厂")
    private String siteName;
}
