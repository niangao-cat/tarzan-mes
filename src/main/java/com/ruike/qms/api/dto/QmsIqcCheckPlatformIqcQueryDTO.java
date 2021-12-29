package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author tong.li IQC检验平台界面  查询条件
 * @Date 2020/5/12 14:40
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcQueryDTO implements Serializable {
    private static final long serialVersionUID = 1020492688329705354L;

    @ApiModelProperty(value = "检验单号")
    private String iqcNumber;

    @ApiModelProperty(value = "抽样条码")
    private String sampleCode;

    private String instructionId;
}
