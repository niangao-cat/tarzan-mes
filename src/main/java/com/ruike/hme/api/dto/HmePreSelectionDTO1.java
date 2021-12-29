package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmePreSelectionDTO1
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/11/30 13:47
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO1 implements Serializable {
    private static final long serialVersionUID = -1922410546446959891L;
    @ApiModelProperty(value = "挑选批次")
    private String  selectLot;
    @ApiModelProperty(value = "套数")
    private String  setsNum;
    @ApiModelProperty(value = "挑选规则")
    private String  cosRuleId;
    @ApiModelProperty(value = "是否绑定工单")
    private String  isBind;
    @ApiModelProperty(value = "工单id")
    private String workOrderId;
    @ApiModelProperty(value = "工厂")
    private String siteId;
    @ApiModelProperty(value = "芯片数")
    private String cosNum;
    @ApiModelProperty(value = "sap料号")
    private String materialId;
    @ApiModelProperty(value = "产品类型")
    private String productType;
    @ApiModelProperty(value = "工位")
    private String  workcellId;

    @ApiModelProperty(value = "盒子数")
    private String  materialLotNum;
    @ApiModelProperty(value = "盒子列表")
    private List<String> materialLotIdList;
}
