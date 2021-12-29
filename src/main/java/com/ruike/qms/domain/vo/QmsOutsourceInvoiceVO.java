package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/23 18:20
 */
@Data
public class QmsOutsourceInvoiceVO implements Serializable {

    private static final long serialVersionUID = 1755393079321047364L;

    @ApiModelProperty(value = "采购订单头id")
    private String instructionDocId;
}
