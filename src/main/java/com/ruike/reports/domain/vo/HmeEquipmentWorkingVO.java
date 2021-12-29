package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 设备运行情况明细
 *
 * @author li.zhang 2021/01/14 11:12
 */
@Data
@ExcelSheet(title = "设备运行情况报表")
public class HmeEquipmentWorkingVO implements Serializable {

    private static final long serialVersionUID = -5080738346442063923L;

    @ApiModelProperty("设备主键标识")
    private String equipmentId;

    @ApiModelProperty("资产名称")
    @ExcelColumn(title = "资产名称")
    private String assetName;

    @ApiModelProperty("资产编号")
    @ExcelColumn(title = "资产编号")
    private String assetEncoding;

    @ApiModelProperty("型号")
    @ExcelColumn(title = "型号")
    private String model;

    @ApiModelProperty("序列号")
    @ExcelColumn(title = "序列号")
    private String equipmentBodyNum;

    @ApiModelProperty("使用部门")
    @ExcelColumn(title = "使用部门")
    private String department;

    @ApiModelProperty("车间位置")
    @ExcelColumn(title = "车间位置")
    private String areaLocation;

    @ApiModelProperty("使用人")
    @ExcelColumn(title = "使用人")
    private String user;

    @ApiModelProperty("hmeEquipmentWorkingVO2List")
    @ExcelColumn(title = "hmeEquipmentWorkingVO2List")
    private List<HmeEquipmentWorkingVO2> hmeEquipmentWorkingVO2List;

}
