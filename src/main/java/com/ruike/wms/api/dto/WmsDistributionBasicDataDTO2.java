package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * WmsDistributionBasicDataDTO
 * @author: chaonan.hu chaonan.hu@hand-china.com 2020/7/23 09:42:35
 **/
@Data
public class WmsDistributionBasicDataDTO2 implements Serializable {
    private static final long serialVersionUID = 1553968385008056886L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "物料组ID")
    private String materialGroupId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "策略类型")
    private String distributionType;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
}
