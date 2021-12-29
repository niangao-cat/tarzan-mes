package com.ruike.hme.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnDTO2 implements Serializable {
    private static final long serialVersionUID = -1562452987103773002L;
    @ApiModelProperty("工位ID")
    private String workCellId;
    @ApiModelProperty("SN")
    private String snNum;
    @ApiModelProperty("物料类型")
    private String materialType;
}
