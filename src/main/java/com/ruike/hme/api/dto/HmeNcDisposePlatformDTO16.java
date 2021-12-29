package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-02 10:32:26
 **/
@Data
public class HmeNcDisposePlatformDTO16 extends HmeNcDisposePlatformDTO15 implements Serializable {
    private static final long serialVersionUID = 4136520243007828861L;

    @ApiModelProperty(value = "物料Id")
    private String materialId;
}
