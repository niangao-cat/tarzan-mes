package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 * 新增更新工装数据
 * @author li.zhang 2021/01/08 9:33
 */
@Data
public class HmeToolInsertDto implements Serializable {

    private static final long serialVersionUID = -881040300844314828L;

    @ApiModelProperty(value = "工装ID")
    private String toolId;
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
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "使用频率")
    private String rate;
    @ApiModelProperty(value = "启用状态")
    private String enableFlag;
}
