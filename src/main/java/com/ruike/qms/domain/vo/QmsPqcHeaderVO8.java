package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * QmsPqcHeaderVO8
 *
 * @author: chaonan.hu@hand-china.com 2020/8/18 14:35:19
 **/
@Data
public class QmsPqcHeaderVO8 implements Serializable {
    private static final long serialVersionUID = 8014260422654820936L;

    @ApiModelProperty(value = "序号")
    private BigDecimal number;

    @ApiModelProperty(value = "明细ID")
    private String pqcDetailsId;

    @ApiModelProperty(value = "结果")
    private String result;

    @ApiModelProperty(value = "备注")
    private String remark;
}
