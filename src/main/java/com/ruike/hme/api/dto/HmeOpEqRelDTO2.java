package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmeOpEqRelDTO2
 * @description: 设备类别LOV返回DTO
 * @author: chaonan.hu@hand-china.com 2020-06-22 14:24:35
 **/
@Data
public class HmeOpEqRelDTO2 implements Serializable {
    @ApiModelProperty(value = "设备类编码")
    private String equipmentCategory;

    @ApiModelProperty(value = "设备类描述")
    private String equipmentCategoryDesc;

}
