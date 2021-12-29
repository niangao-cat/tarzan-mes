package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionReturnDTO2
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/18 15:59
 * @Version 1.0
 **/
@Data
public class HmePreSelectionReturnDTO2 implements Serializable {

    private static final long serialVersionUID = 6249834234972290286L;
    @ApiModelProperty(value = "挑选批次")
    private String  preSelectionLot;

    @ApiModelProperty(value = "挑选数量")
    private String num;

    @ApiModelProperty(value = "挑选芯片数量")
    private Long cosNum;
}
