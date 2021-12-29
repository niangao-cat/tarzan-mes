package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 17:19
 */
@Data
public class WmsCostCtrMaterialDTO7 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据编码")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "结算类型")
    private String settleAccounts;
    @ApiModelProperty(value = "成本中心编码")
    private String costCenterCode;

    private List<WmsCostCtrMaterialDTO2> docLineList;
    @ApiModelProperty(value = "已扫描的实物条码")
    private List<String> barCodes;
}
