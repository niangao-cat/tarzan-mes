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
public class WmsCostCtrMaterialDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据编码")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    @LovValue(lovCode = "WMS.COST_CENTER_DOCUMENT.TYPE",meaningField="typeMeaning",defaultMeaning = "无")
    private String instructionDocType;
    @ApiModelProperty(value = "单据类型说明")
    private String typeMeaning;
    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.COST_CENTER_DOCUMENT.STATUS",meaningField="statusMeaning",defaultMeaning = "无")
    private String instructionDocStatus;
    @ApiModelProperty(value = "单据状态说明")
    private String statusMeaning;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String siteCode;
    @ApiModelProperty(value = "工厂名称")
    private String siteName;
    @ApiModelProperty(value = "类型")
    private String orderType;
    @ApiModelProperty(value = "成本中心/内部订单编码")
    private String costCenterCode;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "结算类型")
    private String settleAccounts;
    @ApiModelProperty(value = "打印标识")
    private String printFlag;
    @ApiModelProperty(value = "内部订单ID")
    private String internalOrderId;

    private List<WmsCostCtrMaterialDTO2> docLineList;
}
