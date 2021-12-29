package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 货位站点信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/25 15:11
 */
@Data
public class WmsLocatorSiteVO {
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "站点ID")
    private String siteId;

}
