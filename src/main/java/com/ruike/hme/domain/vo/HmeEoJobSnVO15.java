package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnVO15 implements Serializable {
    private static final long serialVersionUID = 118536179934640250L;
    @ApiModelProperty("eoId")
    private String eoId;
    @ApiModelProperty("eo数量")
    private BigDecimal eoQty;
    @ApiModelProperty("上一步骤ID")
    private String previousStepId;
}
