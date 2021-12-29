package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备台账导入VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 17:13
 */
@Data
public class HmeEquipmentImportVO {


    @ApiModelProperty("设备类型")
    private String 	equipmentType;

    @ApiModelProperty(value = "资产编码")
    @NotBlank(message = "资产编码不能为空")
    private String 	assetEncoding;

    @ApiModelProperty(value = "SAP流水号")
    private String sapNum;

    @ApiModelProperty(value = "资产名称")
    private String assetName;

    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;

    @ApiModelProperty(value = "设备状态")
    private String equipmentStatus;

    @ApiModelProperty(value = "配置")
    private String 	equipmentConfig;

    @ApiModelProperty(value = "机身序列号")
    private String equipmentBodyNum;

    @ApiModelProperty(value = "资产类别")
    private String assetClass;

    @ApiModelProperty(value = "入账日期")
    private Date postingDate;

    @ApiModelProperty(value = "销售商")
    private String 	supplier;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "数量")
    private Long quantity;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种ID")
    private String 	currency;

    @ApiModelProperty(value = "保管部门ID")
    private String 	businessId;

    @ApiModelProperty(value = "使用人")
    private String user;

    @ApiModelProperty(value = "保管人")
    private String 	preserver;

    @ApiModelProperty(value = "存放地点")
    private String location;

    @ApiModelProperty(value = "使用频次")
    private String 	frequency;

    @ApiModelProperty(value = "是否计量")
    private String 	measureFlag;

    @ApiModelProperty(value = "合同编号")
    private String contractNum;

    @ApiModelProperty(value = "募投")
    private String recruitement;

    @ApiModelProperty(value = "募投编号")
    private String 	recruitementNum;

    @ApiModelProperty(value = "OA验收单号")
    private String 	oaCheckNum;

    @ApiModelProperty(value = "备注")
    private String 	remark;

    @ApiModelProperty(value = "质保期")
    private Date warrantyDate;

    @ApiModelProperty(value = "处置单号")
    private String 	dealNum;

    @ApiModelProperty(value = "处置原因")
    private String dealReason;

    @ApiModelProperty(value = "归属权" )
    private String belongTo;

    @ApiModelProperty(value = "设备描述")
    private String descriptions;

    @ApiModelProperty(value = "应用类型")
    private String 	applyType;

    @ApiModelProperty(value = "管理模式")
    private String attribute1;

    @ApiModelProperty(value = "台账类别")
    private String attribute2;

}
