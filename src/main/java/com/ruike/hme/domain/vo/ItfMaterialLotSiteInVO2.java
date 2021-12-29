package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ItfMaterialLotSiteInVO2
 *
 * @author: chaonan.hu chaonan.hu@hand-china.com 2021/10/11 17:40:37
 **/
@Data
public class ItfMaterialLotSiteInVO2 implements Serializable {
    private static final long serialVersionUID = 3194323386653316291L;

    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;
}
