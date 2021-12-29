package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/15 10:59
 */
@Data
public class HmeSelfMadeRepairVO2 implements Serializable {

    private static final long serialVersionUID = 3591971726990011117L;

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;
}
