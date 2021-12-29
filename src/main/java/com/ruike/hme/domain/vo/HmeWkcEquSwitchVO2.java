package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWkcEquSwitchVO2
 * @author: chaonan.hu@hand-china.com 2020-06-23 10:57:24
 **/
@Data
public class HmeWkcEquSwitchVO2 implements Serializable {
    private static final long serialVersionUID = 3248666438442598221L;

    private HmeWkcEquSwitchVO3 hmeWkcEquSwitchVO3;

    private List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS;

    @ApiModelProperty(value = "异常设备编码-强校验")
    private String errorEquipmentCodes;

    @ApiModelProperty(value = "异常设备编码-非强校验")
    private String exceptionEquipmentCodes;
}
