package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工装唯一性检验
 *
 * @author li.zhang 2021/01/11 17:13
 */
@Data
public class HmeToolCheckVO implements Serializable {

    private static final long serialVersionUID = -1701812184866044099L;

    @ApiModelProperty(value = "部门ID")
    private String areaId;
    @ApiModelProperty(value = "车间编码")
    private String workShopId;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工具名称")
    private String toolName;
    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "规格型号")
    private String specification;
}
