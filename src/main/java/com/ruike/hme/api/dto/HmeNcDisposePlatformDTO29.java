package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-09-27 17:07:13
 **/
@Data
public class HmeNcDisposePlatformDTO29 implements Serializable {
    private static final long serialVersionUID = 5625438189932778630L;

    @ApiModelProperty(value = "扫描SNId")
    private String materialLotId;

    @ApiModelProperty(value = "工序")
    private String processId;
}
