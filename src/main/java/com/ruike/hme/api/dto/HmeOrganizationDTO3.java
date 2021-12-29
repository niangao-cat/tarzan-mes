package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeOrganizationDTO3
 * 产线LOV查询条件DTO
 * @author: chaonan.hu@hand-china.com 2021/4/14 10:24:34
 **/
@Data
public class HmeOrganizationDTO3 implements Serializable {
    private static final long serialVersionUID = -7418399413169407632L;

    @ApiModelProperty(value = "部门ID集合")
    private String departmentId;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线描述")
    private String prodLineName;

    @ApiModelProperty(value = "车间ID集合")
    private String workshopId;

    @ApiModelProperty(value = "站点")
    private String siteId;
}
