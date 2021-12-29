package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WmsMaterialReturnConfirmDTO
 * @Deacription 成本中心退料确定传入
 * @Author ywz
 * @Date 2020/4/21 9:38
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsMaterialReturnConfirmDTO implements Serializable {
    private static final long serialVersionUID = 2529899005124172933L;
    @ApiModelProperty(value = "单据Id")
    private String instructionDocId;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "成本中心/内部订单")
    private String settleAccounts;

    @ApiModelProperty(value = "成本中心编码")
    private String costCenterCode;

    @ApiModelProperty(value = "物料批")
    private List<WmsMaterialReturnConfirmDTO2> materialLotList;

    private List<WmsMaterialReturnScanLineDTO> lineList;

}
