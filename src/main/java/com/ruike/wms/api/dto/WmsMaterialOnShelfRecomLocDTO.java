package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * WmsMaterialOnShelfDTO
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 14:57
 */

@Data
public class WmsMaterialOnShelfRecomLocDTO implements Serializable {

    private static final long serialVersionUID = 7398014942584934750L;

    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("单据行目标仓库id")
    private String warehouseId;
}
