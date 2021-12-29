package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestBody;

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
public class WmsCostCenterPickReturnVO2 implements Serializable {
    private static final long serialVersionUID = 2681125667663630873L;
    @ApiModelProperty(value = "实物条码")
    private String barCode;
    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "货位条码")
    private String locatorCode;
    @ApiModelProperty(value = "已扫描实物条码List")
    private List<WmsMaterialReturnScanDTO2> barCodeList;
    @ApiModelProperty(value = "单据行list")
    private List<WmsMaterialReturnScanLineDTO> docLineList;
}