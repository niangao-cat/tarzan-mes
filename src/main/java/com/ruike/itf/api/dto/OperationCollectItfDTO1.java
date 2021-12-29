package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OperationCollectItfDTO1
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/17 22:03
 * @Version 1.0
 **/
@Data
public class OperationCollectItfDTO1 implements Serializable {
    private static final long serialVersionUID = 2769294264013192284L;

    @ApiModelProperty(value = "eoId")
    private String eoId;

    @ApiModelProperty(value = "工序Id")
    private String operationId;

    @ApiModelProperty(value = "reworkFlag")
    private String reworkFlag;

    @ApiModelProperty(value = "reworkFlag")
    private String  tagCode;
}
