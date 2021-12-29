package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品追溯
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
@Data
public class HmeEoTraceBackQueryDTO2 implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "序号")
    private long lineNum;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "SN/LOT Num")
    private String materialLotCode;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "数量")
    private BigDecimal releaseQty;

    @ApiModelProperty(value = "工序流转顺序")
    @ExcelColumn(zh = "工序流转顺序", order = 5)
    private Long headerLineNum;
    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工艺步骤", order = 6)
    private String parentWorkcellName;
    @ApiModelProperty(value = "作业平台类型含义")
    @ExcelColumn(zh = "作业平台类型", order = 7)
    private String jobTypeMeaning;
    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "工位", order = 8)
    private String workcellName;
    @ApiModelProperty(value = "加工开始时间")
    @ExcelColumn(zh = "加工开始时间", order = 8, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteInDate;
    @ApiModelProperty(value = "加工结束时间")
    @ExcelColumn(zh = "加工结束时间", order = 9, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteOutDate;
    @ApiModelProperty(value = "加工时长")
    @ExcelColumn(zh = "加工时长(分)", order = 10)
    private BigDecimal processTime;
    @ApiModelProperty(value = "进站人")
    @ExcelColumn(zh = "加工人", order = 11)
    private String createUserName;
    @ApiModelProperty(value = "不良信息标识")
    @ExcelColumn(zh = "不良", order = 12)
    private String ncInfoFlagMeaning;
    @ExcelColumn(zh = "是否返修", order = 13)
    private String isRework;
    @ApiModelProperty(value = "是否异常出站")
    @ExcelColumn(zh = "是否异常出站", order = 14)
    private String exceptionFlagMeaning;
}
