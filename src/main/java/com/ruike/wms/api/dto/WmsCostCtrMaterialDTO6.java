package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 17:19
 */
@Data
public class WmsCostCtrMaterialDTO6 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "容器Id")
    private String containerId;
    @ApiModelProperty(value = "条码类型")
    private String codeType;

    private List<String> materialLotIds;
}
