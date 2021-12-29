package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/01/21 15:56
 */
@Data
public class HmeProcessNcDetailVO implements Serializable {

    private static final long serialVersionUID = -3515135280743364515L;

    @ApiModelProperty("明细表ID")
    private String detailId;

    @ApiModelProperty("头表ID")
    private String headerId;

    @ApiModelProperty("行表ID")
    private String lineId;

    @ApiModelProperty("最大值")
    private String maxValue;

    @ApiModelProperty("最小值")
    private String minValue;

    @ApiModelProperty("不良代码ID")
    private String ncCodeId;

    @ApiModelProperty("不良代码编码")
    private String ncCode;

    @ApiModelProperty("标准编码")
    private String standardCode;
}
