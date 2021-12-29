package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/31 10:35
 */
@Data
@ExcelSheet(title = "数据收集组&数据项关系")
public class HmeTagGroupExportVO implements Serializable {

    private static final long serialVersionUID = -61534986771479238L;

    @ApiModelProperty(value = "数据收集组主键")
    private String tagGroupId;

    @ApiModelProperty(value = "数据收集组编码")
    @ExcelColumn(title = "数据收集组编码", order = 0)
    private String tagGroupCode;

    @ApiModelProperty(value = "序号")
    @ExcelColumn(title = "序号", order = 1)
    private Long serialNumber;

    @ApiModelProperty(value = "数据项id")
    private String tagId;

    @ApiModelProperty(value = "数据项编码")
    @ExcelColumn(title = "数据项编码", order = 2)
    private String tagCode;

    @ApiModelProperty(value = "数据收集方式")
    @LovValue(lovCode = "HME.TAG_COLLECTION_METHOD", meaningField = "collectionMethodMeaning")
    private String collectionMethod;

    @ApiModelProperty(value = "数据收集方式含义")
    @ExcelColumn(title = "数据收集方式", order = 3)
    private String collectionMethodMeaning;

    @ApiModelProperty(value = "允许缺失值")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "valueAllowMissingMeaning")
    private String valueAllowMissing;

    @ApiModelProperty(value = "允许缺失值含义")
    @ExcelColumn(title = "允许缺失值", order = 4)
    private String valueAllowMissingMeaning;

    @ApiModelProperty(value = "符合值")
    @ExcelColumn(title = "符合值", order = 5)
    private String trueValue;

    @ApiModelProperty(value = "不符合值")
    @ExcelColumn(title = "不符合值", order = 6)
    private String falseValue;

    @ApiModelProperty(value = "最小值")
    @ExcelColumn(title = "最小值", order = 7)
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "最大值")
    @ExcelColumn(title = "最大值", order = 8)
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "最大值")
    private String unit;

    @ApiModelProperty(value = "计量单位")
    @ExcelColumn(title = "计量单位", order = 9)
    private String unitCode;

    @ApiModelProperty(value = "必需的数据条数")
    @ExcelColumn(title = "必需的数据条数", order = 10)
    private Long mandatoryNum;

    @ApiModelProperty(value = "可选的数据条数")
    @ExcelColumn(title = "可选的数据条数", order = 11)
    private Long optionalNum;
}
