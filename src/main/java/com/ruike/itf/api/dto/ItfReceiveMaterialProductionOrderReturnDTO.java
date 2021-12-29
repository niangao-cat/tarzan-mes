package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItfReceiveMaterialProductionOrderReturnDTO extends  ItfCommonReturnDTO{
    private List<ItfReceiveReturnDTO> detail;
}
