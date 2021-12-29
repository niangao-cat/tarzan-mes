package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/22 13:41
 */
@Data
public class HmeCosResVO implements Serializable {

    private static final long serialVersionUID = 744529640226140881L;


    @ApiModelProperty(value = "工作单元id")
    private String workOrderId;
    @ApiModelProperty(value = "物料id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "芯片数")
    private BigDecimal qty;
    @ApiModelProperty(value = "工作id")
    private String eoJobSnId;
    @ApiModelProperty(value = "在制记录表id")
    private String woJobSnId;
    @ApiModelProperty(value = "条码")
    private String materialLotCode;
    @ApiModelProperty(value = "条码id")
    private String materialLotId;
}
