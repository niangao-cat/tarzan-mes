package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeQuantityAnalyzeDocVO
 *
 * @author: chaonan.hu@hand-china.com 2021-01-18 11:39:12
 **/
@Data
public class HmeQuantityAnalyzeDocVO implements Serializable {
    private static final long serialVersionUID = 1748198600984496359L;

    @ApiModelProperty("头Id")
    private String qaDocId;

    @ApiModelProperty("工单")
    private String workOrderNum;

    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("物料批Id")
    private String materialLotId;

    @ApiModelProperty("物料批编码")
    private String materialLotCode;

    @ApiModelProperty("数量")
    private BigDecimal quantity;
}
