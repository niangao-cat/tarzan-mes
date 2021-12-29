package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/9 16:36
 */
@Data
public class WmsPurchaseReturnDetailsVO implements Serializable {

    private static final long serialVersionUID = 8063506064431430945L;

    @ApiModelProperty(value = "指令实际id")
    private String actualId;

    @ApiModelProperty(value = "条码Id")
    private String materialLotId;

    @ApiModelProperty(value = "操作人")
    private String lastUpdatedBy;

    @ApiModelProperty(value = "操作人名称")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "操作时间")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "条码执行数量")
    private String actualQty;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;
}
