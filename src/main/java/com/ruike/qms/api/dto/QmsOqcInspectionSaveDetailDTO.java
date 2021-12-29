package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname QmsOqcInspectionSaveDetailDTO
 * @Description OQC检验条码保存明细参数
 * @Date 2020/8/29 17:10
 * @Author yuchao.wang
 */
@Data
public class QmsOqcInspectionSaveDetailDTO implements Serializable {
    private static final long serialVersionUID = -3287740186649100540L;

    @ApiModelProperty(value = "序号")
    private Long number;

    @ApiModelProperty(value = "结果值")
    private String result;

    @ApiModelProperty(value = "备注")
    private String remark;
}