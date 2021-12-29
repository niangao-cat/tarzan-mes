package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeTagDaqAttrDTO
 *
 * @author chaonan.hu@hand-china.com 2020/07/21 10:39:13
 */
@Data
public class HmeTagDaqAttrDTO implements Serializable {
    private static final long serialVersionUID = -5509734296768103054L;

    @ApiModelProperty(value = "设备类别",required = true)
    private String equipmentCategory;

    @ApiModelProperty(value = "数据采集编码")
    private String value;

    @ApiModelProperty(value = "数据采集描述")
    private String meaning;
}
