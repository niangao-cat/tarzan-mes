package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 生产数据采集请求VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/16 20:28
 */
@Data
public class HmeDataCollectLineVO2 implements Serializable {

    private static final long serialVersionUID = 314161428276732051L;

    @ApiModelProperty(value = "行Id")
    private String collectLineId;

    @ApiModelProperty(value = "位置")
    private String referencePoint;

    @ApiModelProperty(value = "数据项")
    private String tagDescription;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "下限")
    private String minimumValue;

    @ApiModelProperty(value = "标准值")
    private String  standard;

    @ApiModelProperty(value = "上限")
    private String maximalValue;

    @ApiModelProperty(value = "结果")
    private String result;

    @ApiModelProperty(value = "物料编码")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private String primaryUomQty;

}
