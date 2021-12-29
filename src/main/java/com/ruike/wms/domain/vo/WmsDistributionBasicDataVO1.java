package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author yifan.xiong@hand-china.com 2021/02/24 14:20
 */
@Data
public class WmsDistributionBasicDataVO1 implements Serializable {

    private static final long serialVersionUID = -4857709108421097484L;
    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "产线Id列表", required = true)
    private String prodLineIds;

    @ApiModelProperty(value = "产线Id")
    private String prodLineId;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "工段Id")
    private String workcellId;

    @ApiModelProperty(value = "工段编码")
    private String workcellCode;

    @ApiModelProperty(value = "工段名称")
    private String workcellName;

}
