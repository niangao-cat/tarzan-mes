package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description IQC界面 点击保存时入参
 * @Author tong.li
 * @Date 2020/5/12 20:29
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcSaveDTO implements Serializable {
    private static final long serialVersionUID = -7067361449241798703L;


    @ApiModelProperty(value = "质检单头  主键")
    private String  iqcHeaderId;

    @ApiModelProperty(value = "质检单头  检验结果")
    private String  inspectionResult;

    @ApiModelProperty(value = "质检单头  合格项数")
    private Long okQty;

    @ApiModelProperty(value = "质检单头  不良项数")
    private Long ngQty;

    @ApiModelProperty(value = "质检单头  备注")
    private String remark;


    @ApiModelProperty(value = "质检单头  检验开始时间(提交时传入)")
    private Date inspectionStartDate;

    @ApiModelProperty(value = "来源指令ID")
    private String instructionId;


    @ApiModelProperty(value = "质检单行")
    private List<QmsIqcCheckPlatformIqcSaveLineDTO> lineList;

}
