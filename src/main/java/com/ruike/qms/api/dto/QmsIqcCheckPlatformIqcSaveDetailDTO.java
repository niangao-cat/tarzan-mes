package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description  IQC界面 保存明细数据
 * @Author tong.li
 * @Date 2020/5/13 9:28
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcSaveDetailDTO implements Serializable {
    private static final long serialVersionUID = 6604280824723181883L;

    @ApiModelProperty(value = "质检单明细  主键")
    private String  iqcDetailsId;

    @ApiModelProperty(value = "明细 序号")
    private Long number;

    @ApiModelProperty(value = "明细 结果值")
    private String result;

    @ApiModelProperty(value = "明细 备注")
    private String remark;
}

