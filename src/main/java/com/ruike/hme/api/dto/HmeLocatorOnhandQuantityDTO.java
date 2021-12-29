
package com.ruike.hme.api.dto;

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
public class HmeLocatorOnhandQuantityDTO implements Serializable {
    private static final long serialVersionUID = -7409857419199785090L;
    @ApiModelProperty("库位类型")
    private List<String> locatorTypeList;
    @ApiModelProperty("工段库位类型")
    private List<String> workcellLocatorTypeList;
    @ApiModelProperty("产线库位类型")
    private List<String> prodLineLocatorTypeList;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("库位编码")
    private String locatorCode;
    @ApiModelProperty("批次")
    private String lotCode;
}
