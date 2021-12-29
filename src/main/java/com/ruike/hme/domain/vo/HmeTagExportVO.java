package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/31 10:29
 */
@Data
@ExcelSheet(title = "数据项")
public class HmeTagExportVO implements Serializable {

    private static final long serialVersionUID = -7320201769329770148L;

    @ApiModelProperty(value = "数据项主键")
    private String tagId;

    @ApiModelProperty(value = "数据项编码")
    @ExcelColumn(title = "数据项编码", order = 0)
    private String tagCode;

    @ApiModelProperty(value = "数据项描述")
    @ExcelColumn(title = "数据项描述", order = 1)
    private String tagDescription;

    @ApiModelProperty(value = "数据收集方式")
    @LovValue(lovCode = "HME.TAG_COLLECTION_METHOD", meaningField = "collectionMethodMeaning")
    private String collectionMethod;

    @ApiModelProperty(value = "数据收集方式含义")
    @ExcelColumn(title = "数据收集方式", order = 2)
    private String collectionMethodMeaning;

    @ApiModelProperty(value = "数据类型")
    @LovValue(lovCode = "HME.TAG_VALUE_TYPE", meaningField = "valueTypeMeaning")
    private String valueType;

    @ApiModelProperty(value = "数据类型含义")
    @ExcelColumn(title = "数据类型", order = 3)
    private String valueTypeMeaning;

    @ApiModelProperty(value = "是否启用")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "是否启用含义")
    @ExcelColumn(title = "是否启用", order = 4)
    private String enableFlagMeaning;

    @ApiModelProperty(value = "最小值")
    @ExcelColumn(title = "最小值", order = 5)
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "最大值")
    @ExcelColumn(title = "最大值", order = 6)
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "最大值")
    private String unit;

    @ApiModelProperty(value = "计量单位")
    @ExcelColumn(title = "计量单位", order = 7)
    private String unitCode;

    @ApiModelProperty(value = "符合值")
    @ExcelColumn(title = "符合值", order = 8)
    private String trueValue;

    @ApiModelProperty(value = "不符合值")
    @ExcelColumn(title = "不符合值", order = 9)
    private String falseValue;

    @ApiModelProperty(value = "必需的数据条数")
    @ExcelColumn(title = "必需的数据条数", order = 10)
    private Long mandatoryNum;

    @ApiModelProperty(value = "可选的数据条数")
    @ExcelColumn(title = "可选的数据条数", order = 11)
    private Long optionalNum;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(title = "备注", order = 12)
    private String remark;

    @ApiModelProperty(value = "过程数据标识")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "processFlagMeaning")
    private String processFlag;

    @ApiModelProperty(value = "过程数据标识")
    @ExcelColumn(title = "过程数据标识", order = 13)
    private String processFlagMeaning;

    @ApiModelProperty(value = "扩展字段名")
    @ExcelColumn(title = "扩展字段名", order = 14)
    private String attrName;

    @ApiModelProperty(value = "扩展字段值")
    @ExcelColumn(title = "扩展字段值", order = 15)
    private String attrValue;
}
