package com.ruike.reports.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 设备报表 ，对应数据库查询字段
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/17 11:16
 */
@Data
public class HmeEquipmentWorkingVO3 implements Serializable {

    private static final long serialVersionUID = 9079725708992049700L;

    @ApiModelProperty("工位")
    private String workcellId;

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("资产编号")
    private String assetEncoding;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("序列号")
    private String equipmentBodyNum;

    @ApiModelProperty("使用部门")
    private String department;

    @ApiModelProperty("车间位置")
    private String areaLocation;

    @ApiModelProperty("使用人")
    private String user;

    @ApiModelProperty("进站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteInDate;

    @ApiModelProperty("出站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteOutDate;

    @ApiModelProperty("日期")
    private String dateString;

    @ApiModelProperty("工位")
    private List<String> workcellIdList;
}
