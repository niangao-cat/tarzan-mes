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
public class WmsCostCtrMaterialDTO4 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "实物条码")
    private String barCode;

    @ApiModelProperty(value = "已扫描条码")
    private List<WmsCostCtrMaterialDTO3> barCodeList;

    private List<WmsCostCtrMaterialDTO2> docLineList;
}
