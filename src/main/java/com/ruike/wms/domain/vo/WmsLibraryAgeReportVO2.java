package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * WmsLibraryAgeReportVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/11/18 19:08:34
 **/
@Data
public class WmsLibraryAgeReportVO2 implements Serializable {
    private static final long serialVersionUID = 2432731269502652008L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;

    @ApiModelProperty(value = "仓库")
    private String parentLocatorCode;

    @ApiModelProperty(value = "单位ID")
    private String primaryUomId;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "库龄区间下的数量集合")
    private List<WmsLibraryAgeReportVO3> libraryAgeList;

}
