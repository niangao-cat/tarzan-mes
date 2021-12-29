package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeProductionGroupDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/05/27 14:37:36
 **/
@Data
public class HmeProductionGroupDTO2 implements Serializable {
    private static final long serialVersionUID = 7882277632508013877L;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "产品组编码")
    private String productionGroupCode;

    @ApiModelProperty(value = "产品组名称")
    private String description;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
}
