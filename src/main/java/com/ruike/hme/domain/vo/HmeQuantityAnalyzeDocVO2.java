package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeQuantityAnalyzeDocVO2
 *
 * @author: chaonan.hu@hand-china.com 2021-01-19 11:39:12
 **/
@Data
public class HmeQuantityAnalyzeDocVO2 implements Serializable {
    private static final long serialVersionUID = -6099785714546827193L;

    @ApiModelProperty("行Id")
    private String qaLineId;

    @ApiModelProperty("检验项目")
    private String tagCode;

    @ApiModelProperty("检验项描述")
    private String tagDescription;

    @ApiModelProperty("检验结果")
    private String result;
}
