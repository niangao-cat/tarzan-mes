package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmePreSelectionReturnDTO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/19 19:38
 * @Version 1.0
 **/
@Data
public class HmePreSelectionReturnDTO3 implements Serializable {

    private static final long serialVersionUID = -2623940358873592L;
    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;
    @ApiModelProperty(value = "物料批Code")
    private String materialLotCode;
    @ApiModelProperty(value = "物料批Code")
    private String locatorCode;
    @ApiModelProperty(value = "数量")
    private String num;
    @ApiModelProperty(value = "原盒位置")
    private String oldLoad;
    @ApiModelProperty(value = "新盒位置")
    private String newLoad;
    @ApiModelProperty(value = "热沉")
    private String hotSinkCode;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "状态")
    @LovValue(value = "HME.SELECT_STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "状态")
    private String statusMeaning;
    @ApiModelProperty(value = "器件编号")
    private String deviceNumber;
    @ApiModelProperty(value = "路数")
    private String ways;
    @ApiModelProperty(value = "目标盒子")
    private String targetMaterialLotCode;
    @ApiModelProperty(value = "明细行Id")
    private String selectionDetailsId;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "性能动态列")
    private List<HmePreSelectionReturnDTO9> functionList;
}
