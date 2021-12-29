package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * WmsCostCenterPickReturnVO3
 *
 * @author: chaonan.hu@hand-china.com 2020/9/22 10:22
 **/
@Data
public class WmsCostCenterPickReturnVO3 implements Serializable {
    private static final long serialVersionUID = 8411876544297103368L;

    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "工厂")
    private String siteName;

    @ApiModelProperty(value = "成本中心编码")
    private String costcenterCode;

    @ApiModelProperty(value = "成本中心描述")
    private String costcenterDesc;

    @ApiModelProperty(value = "成本中心")
    private String costcenter;

    @ApiModelProperty(value = "页数")
    private String page;

    @ApiModelProperty(value = "结算类型")
    private String settleAccounts;

    @ApiModelProperty(value = "结算类型含义")
    private String settleAccountsMeaning;

    @ApiModelProperty(value = "内部订单编号")
    private String internalOrder;
}
