package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfWorkOrderReturnDTO extends ItfCommonReturnDTO {

    @ApiModelProperty(value = "工厂CODE")
    private String plantCode;
    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;

}
