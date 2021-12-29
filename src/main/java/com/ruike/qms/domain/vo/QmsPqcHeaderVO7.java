package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderVO7
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 11:47:49
 **/
@Data
public class QmsPqcHeaderVO7 implements Serializable {
    private static final long serialVersionUID = 4775146778388478392L;

    @ApiModelProperty(value = "序号")
    private String number;

    @ApiModelProperty(value = "明细ID")
    private String pqcDetailsId;

    @ApiModelProperty(value = "结果值")
    private String result;

    @ApiModelProperty(value = "备注")
    private String remark;
}
