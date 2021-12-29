package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/11 15:36
 */
@Data
public class QmsIqcGradeVO implements Serializable {

    private static final long serialVersionUID = -1159556794109719239L;

    @ApiModelProperty(value = "指令行id")
    private String instructionId;

    @ApiModelProperty(value = "等级")
    private String grade;
}
