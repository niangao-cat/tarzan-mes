package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import tarzan.order.domain.entity.MtEo;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnVO14 implements Serializable {
    private static final long serialVersionUID = 7137205944934710541L;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("EO列表")
    private List<HmeEoJobSnVO15> hmeEoJobSnVO15List;
}

