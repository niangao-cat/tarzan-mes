package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeOrganizationDTO
 * 工序LOV查询条件DTO
 * @author: chaonan.hu@hand-china.com 2021/4/12 18:09:11
 **/
@Data
public class HmeOrganizationDTO2 implements Serializable {
    private static final long serialVersionUID = -7511164923781212085L;

    @ApiModelProperty(value = "部门ID集合")
    private String departmentId;

    @ApiModelProperty(value = "产线ID集合")
    private String prodLineId;

    @ApiModelProperty(value = "工段ID集合")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "工序描述")
    private String workcellName;
}
