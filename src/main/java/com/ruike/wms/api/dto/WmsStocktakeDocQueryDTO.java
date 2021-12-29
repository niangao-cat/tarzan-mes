package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 盘点单据查询条件
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 20:56
 */
@Data
@ApiModel("盘点单据查询条件")
public class WmsStocktakeDocQueryDTO {
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("用户Id")
    private Long userId;
    @ApiModelProperty("盘点单据Id")
    private String stocktakeId;
    @ApiModelProperty("盘点单号")
    private String stocktakeNum;
    @ApiModelProperty("盘点状态")
    private String stocktakeStatus;
    @ApiModelProperty("明盘")
    private String openFlag;
    @ApiModelProperty("工厂")
    private String siteId;
    @ApiModelProperty("仓库")
    private String warehouseId;
    /**
     * 限制货位范围中的ID 存在即可
     */
    @ApiModelProperty("货位")
    private String locatorId;
    /**
     * 限制物料范围中的物料编码和描述 存在即可
     */
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("创建人ID")
    private Long createUserId;
    @ApiModelProperty("创建时间从")
    private Date creationDateFrom;
    @ApiModelProperty("创建时间至")
    private Date creationDateTo;
}
