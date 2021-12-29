package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeProductionQueryVO
 *
 * @Author: bao.xu@hand-china.com 2020/7/13 10:55
 */
import java.io.Serializable;

@Data
public class HmeProductionQueryVO implements Serializable {

    private static final long serialVersionUID = -8951528082475554838L;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "站点")
    private String siteId;
    @ApiModelProperty(value = "产线")
    private String prodLineId;
    @ApiModelProperty(value = "产品类型")
    private String productType;
    @ApiModelProperty(value = "产品分类")
    private String productClassification;
    @ApiModelProperty(value = "产品型号")
    private String productModel;
    @ApiModelProperty(value = "产品编码")
    private String productCode;
    @ApiModelProperty(value = "工序集")
    private String procedures;

}
