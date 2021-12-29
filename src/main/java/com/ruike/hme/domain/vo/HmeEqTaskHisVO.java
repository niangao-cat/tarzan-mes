package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/4 22:42
 */
@Data
public class HmeEqTaskHisVO implements Serializable {

    private static final long serialVersionUID = 3264175762414759307L;

    @ApiModelProperty(value = "任务单号")
    private String docNum;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;

    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;

    @ApiModelProperty(value = "数据类型")
    @LovValue(lovCode = "HME.TAG_VALUE_TYPE", meaningField = "valueTypeMeaning")
    private String valueType;

    @ApiModelProperty(value = "数据类型含义")
    private String valueTypeMeaning;

    @ApiModelProperty(value = "最小值")
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "最大值")
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    private String uomName;

    @ApiModelProperty(value = "精度")
    private String accuracy;

    @ApiModelProperty(value = "历史表ID")
    private String taskDocHisId;

    @ApiModelProperty(value = "检验值")
    private String checkValue;

    @ApiModelProperty(value = "结果")
    private String result;

    @ApiModelProperty(value = "检验人ID")
    private String checkBy;

    @ApiModelProperty(value = "检验人")
    private String checkByName;

    @ApiModelProperty(value = "检验日期")
    private Date checkDate;

    @ApiModelProperty(value = "点检工位ID")
    private String wkcId;

    @ApiModelProperty(value = "变更时间")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "变更人ID")
    private String lastUpdateBy;

    @ApiModelProperty(value = "变更人")
    private String lastUpdateByName;

    @ApiModelProperty(value = "事件ID")
    private String eventId;

    @ApiModelProperty(value = "点检工位")
    private String workcellCode;

    @ApiModelProperty(value = "点检工位描述")
    private String workcellName;

    @ApiModelProperty(value = "标准值")
    private String standard;
}
