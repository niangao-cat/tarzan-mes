package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * WmsLibraryAgeReportDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/11/18 14:58:23
 **/
@Data
public class WmsLibraryAgeReportDTO implements Serializable {
    private static final long serialVersionUID = -7607981789719069539L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "仓库Id")
    private String parentLocatorId;

    @ApiModelProperty(value = "货位Id")
    private String locatorId;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "超期日期从")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beyondDateFrom;

    @ApiModelProperty(value = "超期日期至")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beyondDateTo;

    @ApiModelProperty(value = "超期日期至")
    private Date finalBeyondDateTo;

    @ApiModelProperty(value = "库龄从")
    private BigDecimal libraryAgeFrom;

    @ApiModelProperty(value = "库龄至")
    private BigDecimal libraryAgeTo;
}
