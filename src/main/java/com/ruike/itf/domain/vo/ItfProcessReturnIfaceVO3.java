package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/1 19:38
 */
@Data
public class ItfProcessReturnIfaceVO3 implements Serializable {

    private static final long serialVersionUID = -5894732112025427861L;

    @ApiModelProperty("条码")
    private String materialLotId;
    @ApiModelProperty("eo状态")
    private String eoStatus;
    @ApiModelProperty("工单类型")
    private String workOrderType;
}
