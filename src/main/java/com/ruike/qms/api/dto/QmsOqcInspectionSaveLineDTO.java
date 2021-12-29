package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * @Classname QmsOqcInspectionSaveLineDTO
 * @Description OQC检验条码保存行参数
 * @Date 2020/8/29 16:45
 * @Author yuchao.wang
 */
@Data
public class QmsOqcInspectionSaveLineDTO implements Serializable {
    private static final long serialVersionUID = -1542051465267059425L;

    @ApiModelProperty(value = "检验单行主键ID")
    private String oqcLineId;

    @ApiModelProperty(value = "结论")
    private String inspectionResult;

    @ApiModelProperty(value = "附件ID")
    private String attachmentUuid;

    @ApiModelProperty(value = "明细数据")
    private List<QmsOqcInspectionSaveDetailDTO> detailList;
}