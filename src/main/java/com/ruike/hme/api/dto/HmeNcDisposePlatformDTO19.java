package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 20:32:56
 **/
@Data
public class HmeNcDisposePlatformDTO19 implements Serializable {
    private static final long serialVersionUID = -5627550566497103751L;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料条码Id")
    private String materialLotId;
}
