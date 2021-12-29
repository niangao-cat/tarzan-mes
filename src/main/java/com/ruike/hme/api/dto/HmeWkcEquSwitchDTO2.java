package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWkcEquSwitchDTO2
 * @author: chaonan.hu@hand-china.com 2020-06-23 11:10:28
 **/
@Data
public class HmeWkcEquSwitchDTO2 implements Serializable {
    private static final long serialVersionUID = 4200630232083349219L;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("扫描的设备编码")
    private String scanAssetEncoding;

    @ApiModelProperty("设备类编码")
    private String equipmentCategory;

    @ApiModelProperty("设备类描述")
    private String equipmentCategoryDesc;
}
