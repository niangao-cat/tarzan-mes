package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * WmsLibraryAgeReportDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020/11/18 18:56:45
 **/
@Data
public class WmsLibraryAgeReportDTO2 implements Serializable {
    private static final long serialVersionUID = 6789216621480513868L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "库龄区间集合", required = true)
    private List<String> libraryAgeList;
}
