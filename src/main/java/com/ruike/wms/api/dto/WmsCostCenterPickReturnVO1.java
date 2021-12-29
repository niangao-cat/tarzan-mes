package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 货位扫描DTO
 * @author wsg guijie.wu@hand-china.com 2020/7/24 17:19
 * @return 
 */
@Getter
@Setter
@ToString
public class WmsCostCenterPickReturnVO1 implements Serializable {
    private static final long serialVersionUID = 2681125667663630873L;
    @ApiModelProperty(value = "货位code")
    private String locatorCode;
    @ApiModelProperty(value = "已扫描实物条码List")
    private List<WmsMaterialReturnScanDTO2> barCodeList;
    @ApiModelProperty(value = "单据行list")
    private List<WmsMaterialReturnScanLineDTO> docLineList;
}