package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.Date;

/**
 * 库存盘点单选择查询条件
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 16:24
 */
@Data
public class WmsStocktakeDocSelectVO {
    @ApiModelProperty("盘点单号")
    private String stocktakeNum;
    @ApiModelProperty("盘点单号")
    private String stocktakeId;
    @ApiModelProperty("明盘")
    private String openFlag;
    @ApiModelProperty("盘点状态")
    @LovValue(lovCode = "WMS.STOCKTAKE_STATUS", meaningField = "stocktakeStatusMeaning")
    private String stocktakeStatus;
    @ApiModelProperty("盘点状态含义")
    private String stocktakeStatusMeaning;
    @ApiModelProperty("工厂ID")
    private String siteId;
    @ApiModelProperty("工厂")
    private String siteCode;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库")
    private String warehouseCode;
    @ApiModelProperty("创建时间")
    private Date creationDate;
    @ApiModelProperty("备注")
    private String remark;
}
