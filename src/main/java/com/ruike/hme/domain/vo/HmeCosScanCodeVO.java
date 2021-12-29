package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/22 11:03
 */
@Data
public class HmeCosScanCodeVO implements Serializable {

    private static final long serialVersionUID = -5801425106017265958L;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;
    @ApiModelProperty(value = "工位id")
    private String workcellId;
    @ApiModelProperty(value = "工艺id")
    private String operationId;
    @ApiModelProperty(value = "工单")
    private String workOrderId;
    @ApiModelProperty(value = "班组id")
    private String shiftId;
}
