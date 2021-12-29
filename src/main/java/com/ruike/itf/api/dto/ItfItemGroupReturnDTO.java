package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfItemGroupReturnDTO extends ItfCommonReturnDTO {

    @ApiModelProperty(value = "物料组编码")
    private String itemGroupCode;
    @ApiModelProperty(value = "物料组描述")
    private String itemGroupDescription;

}
