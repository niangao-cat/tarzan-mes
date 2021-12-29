package com.ruike.wms.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 12:29
 */
@Data
public class WmsInvOnhandQtyRecordDTO4 implements Serializable {


    private static final long serialVersionUID = -1670841852959102505L;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "日期从")
    private String dateFrom;
    @ApiModelProperty(value = "日期至")
    private String dateTo;
}
