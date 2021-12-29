package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description IQC界面明细信息
 * @Author tong.li
 * @Date 2020/5/19 14:54
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcReturnDetailDTO implements Serializable {
    private static final long serialVersionUID = -3071556359074148564L;

    @ApiModelProperty(value = "头ID")
    private String iqcHeaderId;
    @ApiModelProperty(value = "行ID")
    private String iqcLineId;
    @ApiModelProperty(value = "明细ID")
    private String iqcDetailsId;
    @ApiModelProperty(value = "序号")
    private String number;
    @ApiModelProperty(value = "结果值")
    private String result;
    @ApiModelProperty(value = "备注")
    private String remark;
}
