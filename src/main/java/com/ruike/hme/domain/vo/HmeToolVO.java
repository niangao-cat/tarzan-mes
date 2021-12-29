package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeToolVo
 *
 * @author li.zhang 2021/01/07 10:36
 */
@Data
@ExcelSheet(title = "工具维护")
public class HmeToolVO implements Serializable {
    private static final long serialVersionUID = -1583081895690871529L;

    @ApiModelProperty(value = "工装ID")
    private String toolId;
    @ApiModelProperty(value = "部门ID")
    private String areaId;
    @ApiModelProperty(value = "部门名称")
    @ExcelColumn(zh = "部门名称")
    private String areaName1;
    @ApiModelProperty(value = "车间编码")
    private String areaCode;
    @ApiModelProperty(value = "车间名称")
    @ExcelColumn(zh = "车间名称")
    private String areaName2;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "工位名称")
    private String workcellName;
    @ApiModelProperty(value = "工具名称")
    @ExcelColumn(zh = "工具名称")
    private String toolName;
    @ApiModelProperty(value = "品牌")
    @ExcelColumn(zh = "品牌")
    private String brandName;
    @ApiModelProperty(value = "规格型号")
    @ExcelColumn(zh = "规格型号")
    private String specification;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位")
    private String uomName;
    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "频率")
    @ExcelColumn(zh = "使用频率")
    private String rate;
    @ApiModelProperty(value = "有效标识")
    @ExcelColumn(zh = "启用状态")
    private String enableFlag;
    @ApiModelProperty(value = "应用类型")
    @LovValue(lovCode = "HME.APPLY_TYPE", meaningField = "applyTypeMeaning")
    private String applyType;
    @ApiModelProperty(value = "应用类型含义")
    @ExcelColumn(zh = "应用类型")
    private String applyTypeMeaning;
}
