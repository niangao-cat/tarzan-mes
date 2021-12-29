package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 *
 * @author li.zhang 2021/01/07 13:14
 */
@Data
public class HmeToolHisVO implements Serializable {
    private static final long serialVersionUID = 8298289197538484974L;

    @ApiModelProperty(value = "工装历史Id")
    private String toolHisId;
    @ApiModelProperty(value = "工具名称")
    private String toolName;
    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "规格型号")
    private String specification;
    @ApiModelProperty(value = "单位")
    private String uomName;
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "频率")
    private String rate;
    @ApiModelProperty(value = "有效标识")
    private String enableFlag;
    @ApiModelProperty(value = "更新人")
    private String name;
    @ApiModelProperty(value = "更新时间")
    private String lastUpdateDate;
}
