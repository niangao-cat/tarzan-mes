package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeWoJobSnDTO5
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/10/11 14:25
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnDTO5 implements Serializable {
    private static final long serialVersionUID = 572089273486711232L;

    @ApiModelProperty(value = "来料记录Id")
    private String operationRecordId;

    @ApiModelProperty(value = "剩余芯片数")
    private Long cosNum;

    @ApiModelProperty(value = "BAR条数")
    private Long barNum;

    @ApiModelProperty(value = "Wafer")
    private String wafer;

}
