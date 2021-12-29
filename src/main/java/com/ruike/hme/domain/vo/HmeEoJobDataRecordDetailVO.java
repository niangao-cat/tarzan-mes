package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 应用检机报表明细
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/8 11:35
 */
@Data
public class HmeEoJobDataRecordDetailVO {
    @ApiModelProperty(value = "SN")
    private String materialLotCode;
    @ApiModelProperty(value = "检验组")
    private String tagGroupDescription;
    @ApiModelProperty(value = "检验项")
    private String tagDescription;
    @ApiModelProperty(value = "下限值")
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "上限值")
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "采集结果")
    private String result;
    @ApiModelProperty(value = "检验员")
    private String inspectorName;
    @ApiModelProperty(value = "检验时间")
    private String inspectionDate;
}
