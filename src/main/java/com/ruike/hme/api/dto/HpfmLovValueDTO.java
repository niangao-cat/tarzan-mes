package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel("值集信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HpfmLovValueDTO {
    @ApiModelProperty(value = "值头")
    private String value;
    @ApiModelProperty(value = "值")
    private String meaning;
    @ApiModelProperty(value = "排序")
    private String orderSeq;
}
