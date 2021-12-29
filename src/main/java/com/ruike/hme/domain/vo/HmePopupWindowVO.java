package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * 弹窗返回数据
 *
 * @author wengang.qiang@hand-china.com 2021/09/09 21:07
 */
@Data
@ExcelSheet(zh = "拦截单数据导出")
public class HmePopupWindowVO implements Serializable {

    private static final long serialVersionUID = -4036110237469005610L;

    @ApiModelProperty(value = "拦截单号")
    @ExcelColumn(zh = "拦截单号", order = 1)
    private String interceptNum;
    @ApiModelProperty(value = "拦截对象")
    @ExcelColumn(zh = "拦截对象", order = 3)
    private String interceptObject;
    @ApiModelProperty(value = "sn号")
    @ExcelColumn(zh = "SN号", order = 4)
    private String snNumber;
    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME.INTERCEPT_STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "information表状态")
    private String statusInformation;
    @ApiModelProperty(value = "对象表状态")
    private String statusObject;
    @ApiModelProperty(value = "当前工位")
    @ExcelColumn(zh = "当前工位", order = 6)
    private String workcellName;
    @ApiModelProperty(value = "当前工序")
    @ExcelColumn(zh = "当前工序", order = 7)
    private String workcellCode;
    @ApiModelProperty(value = "工序描述")
    @ExcelColumn(zh = "工序描述", order = 8)
    private String workcellNameDescription;
    @ApiModelProperty(value = "拦截维度")
    @LovValue(lovCode = "HME.INTERCEPT_DIMENSION", meaningField = "dimensionMeaning")
    private String dimension;
    @ApiModelProperty(value = "条码id")
    private String materialLotId;
    @ApiModelProperty(value = "进站时间")
    private Date siteInDate;
    private String workcellId;
    @ApiModelProperty(value = "状态描述")
    @ExcelColumn(zh = "状态", order = 5)
    private String statusMeaning;
    @ExcelColumn(zh = "拦截维度", order = 2)
    private String dimensionMeaning;
}
