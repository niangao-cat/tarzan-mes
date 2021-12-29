package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/09/30 17:14
 */
@Data
public class WmsMaterialTurnoverRateVO2 implements Serializable {

    @ApiModelProperty("租户Id")
    private String tenantId;
    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("批次")
    private String lotCode;
    @ApiModelProperty("ownerId")
    private String ownerId;
    @ApiModelProperty("ownerType")
    private String ownerType;
}
