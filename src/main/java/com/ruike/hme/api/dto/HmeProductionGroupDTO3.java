package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeProductionGroupDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021/06/04 11:08:12
 **/
@Data
public class HmeProductionGroupDTO3 implements Serializable {
    private static final long serialVersionUID = -3798216278460795141L;

    @ApiModelProperty(value = "头ID", required = true)
    private String productionGroupId;

    @ApiModelProperty(value = "主键")
    private String productionGroupLineId;

    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;

    @ApiModelProperty(value = "生产版本")
    private String productionVersion;

    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;
}
