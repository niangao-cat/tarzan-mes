package com.ruike.hme.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * description:E-SOP查询参数
 *
 * @author penglin.sui@hand-china.com 2021-01-19 14:04
 */
@Data
public class HmeOperationInsHeadDTO3 implements Serializable {
    private static final long serialVersionUID = -4857904024692125942L;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("物料类别")
    private String materialGroup;
    @ApiModelProperty("工艺编码")
    private String operationName;
    @ApiModelProperty("进站条码的物料")
    private String snMaterialId;
}
