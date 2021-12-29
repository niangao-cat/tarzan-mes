package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/26 10:10
 */
@Data
public class HmeCosScrapBackVO3 implements Serializable {

    private static final long serialVersionUID = -3574534747364277597L;

    @ApiModelProperty(value = "装入条码")
    private String loadBarcode;

    @ApiModelProperty(value = "报废撤回数据")
    private List<HmeCosScrapBackVO2> reloadScrapList;

}
