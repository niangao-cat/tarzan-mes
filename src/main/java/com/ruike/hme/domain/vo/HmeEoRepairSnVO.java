package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEoRepairSnVO
 *
 * @author: chaonan.hu@hand-china.com 2021/05/24 14:21:12
 **/
@Data
public class HmeEoRepairSnVO implements Serializable {
    private static final long serialVersionUID = 5848997322895482644L;

    @ApiModelProperty("eoId")
    private String eoId;

    @ApiModelProperty("返修SN")
    private String repairSn;
}
