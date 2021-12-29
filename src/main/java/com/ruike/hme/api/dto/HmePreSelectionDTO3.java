package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionDTO3
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/18 20:41
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO3 implements Serializable {
    private static final long serialVersionUID = 1393724357955594606L;

    @ApiModelProperty(value = "明细行Id")
    private String  selectionDetailsId;

    @ApiModelProperty(value = "芯片号")
    private String  loadSequence;

    @ApiModelProperty(value = "旧盒号")
    private String  oldMaterialLotId;

    @ApiModelProperty(value = "旧盒位置")
    private String  oldLoad;

    @ApiModelProperty(value = "新盒号")
    private String  newMaterialLotCode;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

}
