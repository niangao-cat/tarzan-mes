package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * description
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/09 14:37
 */
@Data
public class HmeEquipmentHisVO implements Serializable {

    private static final long serialVersionUID = -7099979769724373466L;

    @ApiModelProperty(value = "设备ID",required = true)
    private String equipmentId;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
