package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionDTO6
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/10/30 11:40
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO6 implements Serializable {

    private static final long serialVersionUID = 2030095998081246424L;

    @ApiModelProperty(value = "明细行Id")
    private String  selectionDetailsId;

    @ApiModelProperty(value = "新盒号")
    private String  newMaterialLotCode;

    @ApiModelProperty(value = "旧盒号")
    private String  oldMaterialLotId;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "路数")
    private String ways;

    @ApiModelProperty(value = "芯片号")
    private String  loadSequence;

}
