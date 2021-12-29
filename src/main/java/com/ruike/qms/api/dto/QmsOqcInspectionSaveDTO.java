package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * @Classname QmsOqcInspectionSaveDTO
 * @Description OQC检验条码保存头参数
 * @Date 2020/8/28 17:37
 * @Author yuchao.wang
 */
@Data
public class QmsOqcInspectionSaveDTO implements Serializable {
    private static final long serialVersionUID = -9112563763878222667L;

    @ApiModelProperty("检验单头表主键")
    private String oqcHeaderId;

    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "检验单行信息")
    List<QmsOqcInspectionSaveLineDTO> lineList;

    @ApiModelProperty(value = "建单日期")
    private Date createdDate;
}