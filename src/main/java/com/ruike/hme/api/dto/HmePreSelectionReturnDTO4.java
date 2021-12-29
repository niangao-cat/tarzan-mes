package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionReturnDTO4
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/20 13:55
 * @Version 1.0
 **/
@Data
public class HmePreSelectionReturnDTO4 implements Serializable {
    private static final long serialVersionUID = 8184828296903726114L;
    @ApiModelProperty(value = "明细Id")
    private String selectionDetailsId;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "旧盒子")
    private String materialLotCode;
    @ApiModelProperty(value = "旧盒子")
    private String oldMaterialLotId;
    @ApiModelProperty(value = "旧盒位置")
    private String oldLoad;
    @ApiModelProperty(value = "旧盒位置-显示值")
    private String displayOldLoad;
    @ApiModelProperty(value = "芯片料号")
    private String materialCode;
    @ApiModelProperty(value = "新盒子")
    private String materialLotCode1;
    @ApiModelProperty(value = "芯片类型")
    private String cosType;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "新盒位置")
    private String newLoad;
    @ApiModelProperty(value = "新盒位置-显示值")
    private String displayNewLoad;
    @ApiModelProperty(value = "规则1")
    private String rule1;
    @ApiModelProperty(value = "规则2")
    private String rule2;
    @ApiModelProperty(value = "规则3")
    private String rule3;
    @ApiModelProperty(value = "是否装入")
    private String isTrue;
    @ApiModelProperty(value = "挑选批次")
    private String preSelectionLot;

}
