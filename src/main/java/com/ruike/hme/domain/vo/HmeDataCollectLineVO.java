package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 生产数据采集请求VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/16 20:18
 */
@Data
public class HmeDataCollectLineVO implements Serializable {

    private static final long serialVersionUID = -7107019482503978656L;

    @ApiModelProperty(value = "头表Id")
    private String collectHeaderId;

    @ApiModelProperty(value = "工位Id")
    private String workcellId;

    @ApiModelProperty(value = "序列号")
    private String dataRecordCode;

    @ApiModelProperty(value = "工艺Id")
    private String operationId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "特殊备注")
    private String remark;

}
