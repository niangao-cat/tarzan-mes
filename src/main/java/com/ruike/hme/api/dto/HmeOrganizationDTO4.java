package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeOrganizationDTO4
 * 工位LOV查询条件DTO
 * @author: chaonan.hu@hand-china.com 2021/4/15 09:14:41
 **/
@Data
public class HmeOrganizationDTO4 implements Serializable {
    private static final long serialVersionUID = -383793560090799020L;

    @ApiModelProperty(value = "部门ID集合")
    private String departmentId;

    @ApiModelProperty(value = "产线ID集合")
    private String prodLineId;

    @ApiModelProperty(value = "工段ID集合")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工序ID集合")
    private String processId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位描述")
    private String workcellName;
}
