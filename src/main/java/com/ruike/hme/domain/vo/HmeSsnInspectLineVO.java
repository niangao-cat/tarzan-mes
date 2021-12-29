package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description
 *
 * @author li.zhang 2021/02/01 14:00
 */
@Data
public class HmeSsnInspectLineVO implements Serializable {

    private static final long serialVersionUID = 7675869704521195430L;

    @ApiModelProperty("行表ID")
    private String ssnInspectLineId;

    @ApiModelProperty("序号")
    private String sequence;

    @ApiModelProperty("检验项ID")
    private String tagId;

    @ApiModelProperty("检验项编码")
    private String tagCode;

    @ApiModelProperty("检验项描述")
    private String tagDescription;

    @ApiModelProperty("最小值")
    private String minimumValue;

    @ApiModelProperty("最大值")
    private String maximalValue;

    @ApiModelProperty("允差")
    private BigDecimal allowDiffer;

    @ApiModelProperty("检验允差")
    private BigDecimal checkAllowDiffer;

    @ApiModelProperty("是否影响耦合")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "coupleFlagMeaning")
    private String coupleFlag;

    @ApiModelProperty(value = "是否影响耦合含义")
    private String coupleFlagMeaning;

    @ApiModelProperty("是否标准件检验判定项")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "judgeFlagMeaning")
    private String judgeFlag;

    @ApiModelProperty(value = "是否标准件判定项含义")
    private String judgeFlagMeaning;

    @ApiModelProperty("是否单路影响耦合")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "cosCoupleFlagMeaning")
    private String cosCoupleFlag;

    @ApiModelProperty(value = "是否单路影响耦合含义")
    private String cosCoupleFlagMeaning;

    @ApiModelProperty("COS位置")
    private String cosPos;

    @ApiModelProperty("变更人")
    private String lastUpdateByName;

    @ApiModelProperty("变更时间")
    private Date lastUpdateDate;
}
