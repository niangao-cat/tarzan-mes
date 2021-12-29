package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeMonthlyPlanVO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 12:01
 * @Version 1.0
 **/
@Data
public class HmeMonthlyPlanVO implements Serializable {
    private static final long serialVersionUID = -471758174506242572L;

    @ApiModelProperty("产线编码")
    private String prodLineCode;

    @ApiModelProperty("产线描述")
    private String prodLineName;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("计划数量")
    private BigDecimal planQty;

    @ApiModelProperty("完工数量")
    private BigDecimal qty;

    @ApiModelProperty("入库数量")
    private BigDecimal actualQty;

    @ApiModelProperty("达成率")
    private String planReachRate;

    @ApiModelProperty("制造部编码")
    private String areaCode;

    @ApiModelProperty("制造部")
    private String areaName;

    @ApiModelProperty("制造部ID")
    private String areaId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("产线ID")
    private String prodLineId;
}
