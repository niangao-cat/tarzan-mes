package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/2/4 16:33
 */
@Data
public class HmeSsnInspectResultVO2 implements Serializable {

    private static final long serialVersionUID = 4891219966849879343L;

    @ApiModelProperty(value = "序号")
    private String sequence;
    @ApiModelProperty(value = "最小值")
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "最大值")
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "检验项id")
    private String tagId;
    @ApiModelProperty(value = "检验项编码")
    private String tagCode;
    @ApiModelProperty(value = "检验项描述")
    private String tagDescription;
    @ApiModelProperty(value = "结果")
    private String inspectResult;
    @ApiModelProperty(value = "数据采集ID")
    private String jobRecordId;
    @ApiModelProperty(value = "影响判定标识")
    private String judgeFlag;
    @ApiModelProperty(value = "影响耦合标识")
    private String coupleFlag;
    @ApiModelProperty(value = "是否单路影响耦合")
    private String cosCoupleFlag;
    @ApiModelProperty(value = "COS位置")
    private String cosPos;
    @ApiModelProperty(value = "标准件检验标准行ID")
    private String ssnInspectLineId;
    @ApiModelProperty(value = "耦合允差")
    private BigDecimal allowDiffer;
    @ApiModelProperty(value = "检验允差")
    private BigDecimal checkAllowDiffer;
    @ApiModelProperty(value = "数据采集ID集合")
    private List<String> jobRecordIdList;
}
