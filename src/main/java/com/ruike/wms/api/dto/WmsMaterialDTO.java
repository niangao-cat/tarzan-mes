package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生产领退料单查询:物料编码
 *
 * @author taowen.wang@hand-china.com
 * @version 1.0
 * @date 2021/7/8 15:13
 */
@Data
public class WmsMaterialDTO {
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
}
