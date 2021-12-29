package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/11/11 16:14
 */
@Data
public class HmePreSelectionMaterialVO implements Serializable {

    private static final long serialVersionUID = -6610287830494680933L;

    @ApiModelProperty("条码")
    private String materialLotId;
    @ApiModelProperty("物料")
    private String materialId;

}
