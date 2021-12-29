package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeSnReplaceDTO
 *
 * @author: chaonan.hu@hand-china.com 2020-07-13 11:28:07
 **/
@Data
public class HmeSnReplaceDTO implements Serializable {
    private static final long serialVersionUID = -6767292403348181394L;

    @ApiModelProperty(value = "原SN编码", required = true)
    private String oldMaterialLotCode;

    @ApiModelProperty(value = "老SN编码", required = true)
    private String newMaterialLotCode;
}
