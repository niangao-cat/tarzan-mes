package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/08/09 11:26
 */
@Data
public class ItfLightTaskIfaceVO implements Serializable {

    private static final long serialVersionUID = 4115993841766091428L;

    @ApiModelProperty("任务号")
    private String taskNum;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("状态")
    private String status;
}
