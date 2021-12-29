package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmePreSelectionDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/18 15:59
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO2 implements Serializable {

    private static final long serialVersionUID = -3740316489665600145L;

    @ApiModelProperty(value = "库位")
    private String  locatorCode;
    @ApiModelProperty(value = "套数")
    private String  setsNum;
    @ApiModelProperty(value = "挑选规则")
    private String  cosRuleId;
    @ApiModelProperty(value = "是否绑定")
    private String  isBind;
    @ApiModelProperty(value = "工单id")
    private String workOrderId;
    @ApiModelProperty(value = "产线")
    private String prodLineId;
    @ApiModelProperty(value = "芯片数")
    private String cosNum;
    @ApiModelProperty(value = "工厂")
    private String siteId;
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

    @ApiModelProperty(value = "容器code")
    private String  containerCode;



}
