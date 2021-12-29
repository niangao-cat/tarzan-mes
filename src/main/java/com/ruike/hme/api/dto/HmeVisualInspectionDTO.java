package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeVisualInspectionDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/01/20 15:25:34
 **/
@Data
public class HmeVisualInspectionDTO implements Serializable {
    private static final long serialVersionUID = -4311491149315110341L;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工艺Id", required = true)
    private String operationId;
}
