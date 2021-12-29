package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeEoTestDataRecordReturnDTO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/17 10:54
 * @Version 1.0
 **/
@Data
public class HmeEoJobDataRecordReturnDTO3 implements Serializable {
    private static final long serialVersionUID = 4089128424838462437L;

    @ApiModelProperty(value = "物料")
    private String snMaterialId;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "条码")
    private String materialLotId;

    @ApiModelProperty(value = "质检员")
    private Long createdBy;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "最大标准值")
    private String maximalValue;

    @ApiModelProperty(value = "最小标准值")
    private String minimumValue;

    @ApiModelProperty(value = "结果")
    private String result;

}
