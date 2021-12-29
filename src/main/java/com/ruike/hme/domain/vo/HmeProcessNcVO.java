package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * tarzan-mes->HmeProcessNcVO
 *
 * @author: chaonan.hu@hand-china.com 2021/06/07 10:59:12
 **/
@Data
@ExcelSheet(zh = "工序不良判定标准")
public class HmeProcessNcVO implements Serializable {
    private static final long serialVersionUID = 8541332103476622176L;

    @ApiModelProperty("头表ID")
    private String headerId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料",order = 0)
    private String materialCode;

    @ApiModelProperty("产品代码")
    @ExcelColumn(zh = "产品代码",order = 1)
    private String productCode;

    @ApiModelProperty("COS型号")
    @ExcelColumn(zh = "COS型号",order = 2)
    private String cosModel;

    @ApiModelProperty("芯片组合")
    @ExcelColumn(zh = "芯片组合",order = 3)
    private String chipCombination;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("工艺编码")
    @ExcelColumn(zh = "工艺编码",order = 4)
    private String operationName;

    @ApiModelProperty("工序ID")
    private String workcellId;

    @ApiModelProperty("工序编码")
    @ExcelColumn(zh = "工序编码",order = 5)
    private String workcellCode;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态含义")
    @ExcelColumn(zh = "状态",order = 6)
    private String statusMeaning;

    @ApiModelProperty("行表ID")
    private String lineId;

    @ApiModelProperty("数据项ID")
    private String tagId;

    @ApiModelProperty("数据项编码")
    @ExcelColumn(zh = "数据项编码",order = 7)
    private String tagCode;

    @ApiModelProperty("数据组ID")
    private String tagGroupId;

    @ApiModelProperty("数据组编码")
    @ExcelColumn(zh = "数据组编码",order = 8)
    private String tagGroupCode;

    @ApiModelProperty("优先级")
    @ExcelColumn(zh = "优先级",order = 9)
    private String priority;

    @ApiModelProperty("标准编码")
    @ExcelColumn(zh = "数据项标准编码",order = 10)
    private String standardCode;

    @ApiModelProperty("明细表ID")
    private String detailId;

    @ApiModelProperty("最小值")
    @ExcelColumn(zh = "最小值",order = 11)
    private String minValue;

    @ApiModelProperty("最大值")
    @ExcelColumn(zh = "最大值",order = 12)
    private String maxValue;

    @ApiModelProperty("不良代码ID")
    private String ncCodeId;

    @ApiModelProperty("不良代码编码")
    @ExcelColumn(zh = "不良代码",order = 13)
    private String ncCode;

    @ApiModelProperty("标准编码")
    @ExcelColumn(zh = "明细标准编码",order = 14)
    private String detailStandardCode;
}
