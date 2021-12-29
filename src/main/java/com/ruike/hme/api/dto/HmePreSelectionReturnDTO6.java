package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionReturnDTO6
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/11/2 16:06
 * @Version 1.0
 **/
@Data
public class HmePreSelectionReturnDTO6 implements Serializable {
    private static final long serialVersionUID = 4971462089701785922L;

    @ApiModelProperty(value = "挑选批次Id")
    private String preSelectionId;

    @ApiModelProperty(value = "挑选批次")
    private String preSelectionLot;

    @ApiModelProperty(value = "产品类型")
    @LovValue(value = "HME_PRODUCT_TYPE", meaningField = "productTypeMeaning")
    private String productType;

    @ApiModelProperty(value = "产品类型")
    private String productTypeMeaning;

    @ApiModelProperty(value = "物料")
    private String materialCode ;

    @ApiModelProperty(value = "挑选规则")
    private String cosRuleCode;

    @ApiModelProperty(value = "套数")
    private String setsNum;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "HME.SELECT_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态")
    private String statusMeaning;
}
