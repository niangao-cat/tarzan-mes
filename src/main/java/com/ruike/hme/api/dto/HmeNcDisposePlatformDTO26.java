package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-08-15 16:43:24
 **/
@Data
public class HmeNcDisposePlatformDTO26 implements Serializable {
    private static final long serialVersionUID = 7858024161351396009L;

    @ApiModelProperty(value = "备注")
    private String comments;
}
