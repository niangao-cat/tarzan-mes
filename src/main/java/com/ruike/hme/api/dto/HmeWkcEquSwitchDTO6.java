package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * tarzan-mes->HmeWkcEquSwitchDTO6
 * @author: chaonan.hu@hand-china.com 2020-06-28 17:05:09
 **/
@Data
public class HmeWkcEquSwitchDTO6 implements Serializable {
    private static final long serialVersionUID = 2352173626076261427L;

    @ApiModelProperty(value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(value = "设备状态")
    private String equipmentStatus;
}
