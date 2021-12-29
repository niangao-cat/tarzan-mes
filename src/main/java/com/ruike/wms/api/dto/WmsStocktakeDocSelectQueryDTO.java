package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 库存盘点单选择查询条件
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 16:20
 */
@Data
public class WmsStocktakeDocSelectQueryDTO {
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("用户Id")
    private Long userId;
    @ApiModelProperty("盘点单号")
    private String stocktakeNum;
    @ApiModelProperty("工厂")
    private String siteId;
    @ApiModelProperty("仓库")
    private String warehouseCode;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("创建时间")
    private Date creationDate;
    @ApiModelProperty("创建时间从")
    private Date startDate;
    @ApiModelProperty("创建时间至")
    private Date endDate;
}
