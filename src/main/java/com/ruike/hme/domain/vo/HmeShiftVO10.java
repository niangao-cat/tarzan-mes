package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeShiftVO10
 *
 * @author chaonan.hu@hand-china.com 2020/08/03 10:55:27
 */
@Data
public class HmeShiftVO10 implements Serializable {
    private static final long serialVersionUID = -7052946295009863952L;

    @ApiModelProperty(value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(value = "异常Id")
    private String exceptionId;
}
