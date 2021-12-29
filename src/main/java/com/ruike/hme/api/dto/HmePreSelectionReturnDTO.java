package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionReturnDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/18 15:03
 * @Version 1.0
 **/
@Data
public class HmePreSelectionReturnDTO implements Serializable {
    private static final long serialVersionUID = -1865724128661775618L;

    @ApiModelProperty(value = "sap料号Id")
    private String  materialId;

    @ApiModelProperty(value = "工单Id")
    private String  workOrderId;

    @ApiModelProperty(value = "工单")
    private String  workOrderNum;

    @ApiModelProperty(value = "sap料号")
    private String materialCode;

    @ApiModelProperty(value = "产品类型")
    private String productType;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "工单需求数量")
    private String qty;

    @ApiModelProperty(value = "工单备注")
    private String remark;

    @ApiModelProperty(value = "计划交付时间")
    private String planEndTime;
}
