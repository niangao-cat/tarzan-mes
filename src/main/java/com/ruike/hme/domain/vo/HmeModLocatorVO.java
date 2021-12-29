package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeModLocatorVO implements Serializable {
    private static final long serialVersionUID = -3022572409689739959L;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("库位类型")
    private List<String> locatorTypeList;
    @ApiModelProperty("工位库位类型")
    private List<String> workcellLocatorTypeList;
    @ApiModelProperty("产线库位类型")
    private List<String> prodLineLocatorTypeList;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
    @ApiModelProperty(value = "库位名称")
    private String locatorName;
}
