package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/8 23:04
 */
@Data
@ExcelSheet(zh = "标准件检验标准")
public class HmeSsnInspectExportVO implements Serializable {

    private static final long serialVersionUID = -2096651094747027898L;

    @ApiModelProperty(value = "标准件编码")
    @ExcelColumn(zh = "标准件编码", order = 0)
    private String standardSnCode;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码", order = 1)
    private String materialCode;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "芯片类型")
    @ExcelColumn(zh = "芯片类型", order = 2)
    private String cosType;

    @ApiModelProperty(value = "工作方式")
    @LovValue(lovCode = "HME.SSN_WORK_WAY", meaningField = "workWayMeaning")
    private String workWay;

    @ApiModelProperty(value = "工作方式含义")
    @ExcelColumn(zh = "工作方式", order = 3)
    private String workWayMeaning;

    @ApiModelProperty(value = "工位编码")
    @ExcelColumn(zh = "工位编码", order = 4)
    private String workcellCode;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性含义")
    @ExcelColumn(zh = "有效性", order = 5)
    private String enableFlagMeaning;

    @ApiModelProperty(value = "序号")
    @ExcelColumn(zh = "序号", order = 6)
    private Long sequence;

    @ApiModelProperty(value = "检验项目编码")
    @ExcelColumn(zh = "检验项目编码", order = 7)
    private String tagCode;

    @ApiModelProperty(value = "检验项目ID")
    private String tagId;

    @ApiModelProperty(value = "最小值")
    @ExcelColumn(zh = "最小值", order = 8)
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "最大值")
    @ExcelColumn(zh = "最大值", order = 9)
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "是否影响耦合")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "coupleFlagMeaning")
    private String coupleFlag;

    @ApiModelProperty(value = "是否影响耦合含义")
    @ExcelColumn(zh = "是否影响耦合", order = 10)
    private String coupleFlagMeaning;

    @ApiModelProperty(value = "是否单路影响耦合")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "cosCoupleFlagMeaning")
    private String cosCoupleFlag;

    @ApiModelProperty(value = "是否单路影响耦合含义")
    @ExcelColumn(zh = "是否单路影响耦合", order = 11)
    private String cosCoupleFlagMeaning;

    @ApiModelProperty(value = "COS位置")
    @ExcelColumn(zh = "COS位置", order = 12)
    private String cosPos;

    @ApiModelProperty(value = "耦合允差")
    @ExcelColumn(zh = "耦合允差", order = 13)
    private BigDecimal allowDiffer;

    @ApiModelProperty(value = "校验差值")
    @ExcelColumn(zh = "校验差值", order = 14)
    private BigDecimal checkAllowDiffer;

    @ApiModelProperty(value = "是否标准件判定项")
    @LovValue(lovCode = "Z_MTLOT_ENABLE_FLAG", meaningField = "judgeFlagMeaning")
    private String judgeFlag;

    @ApiModelProperty(value = "是否标准件判定项含义")
    @ExcelColumn(zh = "是否标准件判定项", order = 15)
    private String judgeFlagMeaning;

    @ApiModelProperty(value = "耦合项目组编码")
    @ExcelColumn(zh = "耦合项目组编码", order = 16)
    private String tagGroupCode;

    @ApiModelProperty(value = "耦合项目组ID")
    private String tagGroupId;


}
