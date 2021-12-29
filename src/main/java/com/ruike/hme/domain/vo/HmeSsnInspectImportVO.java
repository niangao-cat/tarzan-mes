package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/8 11:25
 */
@Data
public class HmeSsnInspectImportVO implements Serializable {

    private static final long serialVersionUID = -2692457324099233650L;

    @ApiModelProperty(value = "标准件编码")
    private String standardSnCode;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "工作方式")
    private String workWay;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "序号")
    private Long sequence;

    @ApiModelProperty(value = "检验项目编码")
    private String tagCode;

    @ApiModelProperty(value = "检验项目ID")
    private String tagId;

    @ApiModelProperty(value = "最小值")
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "最大值")
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "是否影响耦合")
    private String coupleFlag;

    @ApiModelProperty(value = "是否单路影响耦合")
    private String cosCoupleFlag;

    @ApiModelProperty(value = "COS位置")
    private String cosPos;

    @ApiModelProperty(value = "耦合允差")
    private BigDecimal allowDiffer;

    @ApiModelProperty(value = "校验差值")
    private BigDecimal checkAllowDiffer;

    @ApiModelProperty(value = "是否标准件判定项")
    private String judgeFlag;

    @ApiModelProperty(value = "耦合项目组编码")
    private String tagGroupCode;

    @ApiModelProperty(value = "耦合项目组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "导入方式")
    private String importType;
}
