package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * cms数据接口接收返回DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/4 19:59
 */
@Data
public class CmsCollectItfDTO implements Serializable {

    private static final long serialVersionUID = 3006120320940253924L;

    @ApiModelProperty(value = "设备编码")
    private String equipmentNum;
    @ApiModelProperty(value = "光纤EO")
    private String identification;
    @ApiModelProperty(value = "盖板SN")
    private String snNum;
}
