package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeToolDTO
 * 获取工装基础数据查询条件
 * @author li.zhang 2021/01/07 10:50
 */
@Data
public class HmeToolDTO implements Serializable {
    private static final long serialVersionUID = 431068228359931198L;

    @ApiModelProperty(value = "部门ID")
    private String areaId;
    @ApiModelProperty(value = "车间编码")
    private String areaCode;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工具名称")
    private String toolName;
    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "规格型号")
    private String specification;
}
