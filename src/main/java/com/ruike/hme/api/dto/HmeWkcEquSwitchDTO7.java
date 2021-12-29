package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWkcEquSwitchDTO7
 * @author: chaonan.hu@hand-china.com 2020-06-28 17:05:09
 **/
@Data
public class HmeWkcEquSwitchDTO7 extends HmeWkcEquSwitchDTO2 implements Serializable {
    private static final long serialVersionUID = -7160173383801345123L;

    @ApiModelProperty(value = "是否绑定成功")
    private Boolean success;

    @ApiModelProperty(value = "之前绑定工位名称")
    private String oldWorkcellName;
}
