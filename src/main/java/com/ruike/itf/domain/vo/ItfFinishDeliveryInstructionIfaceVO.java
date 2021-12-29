package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/16 15:50
 */
@Data
public class ItfFinishDeliveryInstructionIfaceVO implements Serializable {

    private static final long serialVersionUID = 24693700495500083L;

    @ApiModelProperty("出库任务号")
    private String taskNum;
    @ApiModelProperty("报错信息 包含WCS报错信息，成功则返回S")
    private String message;
    @ApiModelProperty("状态 成功-turn 失败-false")
    private Boolean success;
}
