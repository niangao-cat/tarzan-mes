package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/12 19:51
 */
@Data
public class ItfSoDeliveryChanOrPostVO implements Serializable {

    private static final long serialVersionUID = 901597559431028411L;

    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料类型")
    private String itemType;
}
