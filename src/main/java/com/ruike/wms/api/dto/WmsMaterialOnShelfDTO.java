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
public class WmsMaterialOnShelfDTO implements Serializable {

    private static final long serialVersionUID = 7398014942584934750L;

    @ApiModelProperty("条码")
    private String barCode;
    @ApiModelProperty("容器Id")
    private String containerId;
    @ApiModelProperty("条码类型")
    private String codeType;
    @ApiModelProperty("货位id")
    private String locatorId;
    @ApiModelProperty("货位code")
    private String locatorCode;
    @ApiModelProperty("货位名称")
    private String locatorName;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("单据头")
    private WmsMaterialOnShelfDocDTO orderDto;
    @ApiModelProperty("条码关系")
    private List<WmsMaterialOnShelfBarCodeDTO> barCodeDtoList;
}
