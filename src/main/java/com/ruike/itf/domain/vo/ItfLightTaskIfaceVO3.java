package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/08/09 15:36
 */
@Data
public class ItfLightTaskIfaceVO3 implements Serializable {

    private static final long serialVersionUID = 8956636829311260548L;

    @ApiModelProperty("msgCode")
    private String msgCode;

    @ApiModelProperty("message")
    private String message;

    @ApiModelProperty("taskNum")
    private String taskNum;
}
