package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeChipLabCodeInputVO2
 *
 * @author: chaonan.hu@hand-china.com 2021-11-01 11:27:14
 **/
@Data
public class HmeChipLabCodeInputVO2 implements Serializable {
    private static final long serialVersionUID = -4595229434870813261L;

    @ApiModelProperty(value = "物料批装载表ID")
    private String materialLotLoadId;

    @ApiModelProperty(value = "loadSequence")
    private String loadSequence;

    @ApiModelProperty(value = "行")
    private Long loadRow;

    @ApiModelProperty(value = "列")
    private Long loadColumn;

    @ApiModelProperty(value = "实际芯片数")
    private Long cosNum;

    @ApiModelProperty("芯片实验代码")
    private String chipLabCode;

    @ApiModelProperty("芯片实验备注")
    private String chipLabRemark;

    @ApiModelProperty("热沉条码，后端自用")
    private String attribute4;
}
