package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ApQueryCollectItfReturnDTO1
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/4 14:32
 * @Version 1.0
 **/
@Data
public class ApQueryCollectItfReturnDTO1 implements Serializable {
    private static final long serialVersionUID = 1954036294050912878L;
    @ApiModelProperty(value = "sn编码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

}
