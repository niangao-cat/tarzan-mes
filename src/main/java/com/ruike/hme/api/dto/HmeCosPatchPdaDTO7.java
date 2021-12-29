package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeCosPatchPdaDTO7
 *
 * @author: chaonan.hu@hand-china.com 2020/9/2 14:34:46
 **/
@Data
public class HmeCosPatchPdaDTO7 implements Serializable {
    private static final long serialVersionUID = -1116463285403943950L;

    @ApiModelProperty(value = "默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "设备编码", required = true)
    private String equipmentCode;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "投料数量")
    private BigDecimal assembleQty;
}
