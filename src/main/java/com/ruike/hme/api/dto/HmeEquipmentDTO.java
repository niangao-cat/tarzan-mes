package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 设备信息
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Data
public class HmeEquipmentDTO implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "设备ID")
    private String equipmentId;

}
