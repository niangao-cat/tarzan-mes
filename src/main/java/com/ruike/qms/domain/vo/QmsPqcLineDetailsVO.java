package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcLineDetailsVO
 * @description: 巡检单查询行明细返回
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/06 13:51
 */
@Data
public class QmsPqcLineDetailsVO implements Serializable {

    private static final long serialVersionUID = -6430425448593547236L;

    @ApiModelProperty(value = "序号")
    private Long number;

    @ApiModelProperty(value = "结果")
    private String result;

    @ApiModelProperty(value = "备注")
    private String remark;
}
