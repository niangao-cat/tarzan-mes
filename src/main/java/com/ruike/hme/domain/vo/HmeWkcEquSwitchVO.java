package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeWkcEquSwitchVO
 * @author: chaonan.hu@hand-china.com 2020-06-23 09:56:13
 **/
@Data
public class HmeWkcEquSwitchVO implements Serializable {
    private static final long serialVersionUID = 6964539051865763942L;

    @ApiModelProperty(value = "设备类编码")
    @LovValue(lovCode = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryDesc")
    private String equipmentCategory;

    @ApiModelProperty(value = "设备类描述")
    private String equipmentCategoryDesc;

    @ApiModelProperty(value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;

    @ApiModelProperty(value = "设备描述")
    private String descriptions;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "是否强校验")
    private String attribute1;
}
