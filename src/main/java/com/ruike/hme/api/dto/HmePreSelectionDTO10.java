package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmePreSelectionDTO10
 *
 * @author: chaonan.hu@hand-china.com 2021/9/22 10:36
 **/
@Data
public class HmePreSelectionDTO10 implements Serializable {
    private static final long serialVersionUID = 2251871546070599429L;

    @ApiModelProperty(value = "COS测试良率监控头表主键")
    private String cosMonitorHeaderId;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;
}
