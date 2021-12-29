package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmePumpPreSelectionVO11
 *
 * @author: chaonan.hu@hand-china.com 2021/09/02 15:31:21
 **/
@Data
public class HmePumpPreSelectionVO11 implements Serializable {
    private static final long serialVersionUID = -1954024320253479115L;

    @ApiModelProperty(value = "筛选池中的条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "数据项ID")
    private String tagId;

    @ApiModelProperty(value = "结果")
    private BigDecimal result;

}
