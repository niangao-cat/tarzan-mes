package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 仓库同步接口
 *
 * @author kejin.liu01@hand-china.com
 */
@Data
public class ItfModLocatorIfaceSyncDTO {
    @ApiModelProperty(value = "仓库编码")
    private String LGORT;
    @ApiModelProperty(value = "仓库描述")
    private String LGOBE;
    @ApiModelProperty(value = "工厂编码")
    private String WERKS;
    @ApiModelProperty(value = "是否成功")
    private String ZFLAG;
    @ApiModelProperty(value = "信息")
    private String ZMESSAGE;
}
