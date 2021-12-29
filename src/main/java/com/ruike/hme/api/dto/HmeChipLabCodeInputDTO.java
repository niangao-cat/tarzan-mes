package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeChipLabCodeInputDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/011/01 14:54:12
 **/
@Data
public class HmeChipLabCodeInputDTO implements Serializable {

    @ApiModelProperty("芯片实验代码")
    private String chipLabCode;

    @ApiModelProperty("芯片实验备注")
    private String chipLabRemark;

    @ApiModelProperty("勾选的芯片数据")
    private List<String> materialLotLoadIdList;
}
