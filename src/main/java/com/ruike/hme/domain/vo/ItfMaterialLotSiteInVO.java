package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ItfMaterialLotSiteInVO
 *
 * @author: chaonan.hu chaonan.hu@hand-china.com 2021/10/11 17:40:37
 **/
@Data
public class ItfMaterialLotSiteInVO implements Serializable {
    private static final long serialVersionUID = -740585949413442526L;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private String snQty;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "wafer号")
    private String waferNum;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "当前工序")
    private String currentStepName;

    @ApiModelProperty(value = "当前工序描述")
    private String currentStepDescription;

    @ApiModelProperty(value = "加工次数")
    private String eoStepNum;

    @ApiModelProperty(value = "工单完工数量")
    private String woQuantityOut;

    @ApiModelProperty(value = "工单数量")
    private String woQuantity;

    @ApiModelProperty(value = "SAP料号")
    private String snMaterialCode;

    @ApiModelProperty(value = "物料描述")
    private String snMaterialName;

    @ApiModelProperty(value = "生产版本")
    private String productionVersion;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "实验代码备注")
    private String labCodeRemark;

    @ApiModelProperty(value = "处理消息")
    private String processMessage;

    @ApiModelProperty(value = "处理状态(E/S:错误/成功)")
    private String processStatus;
}
