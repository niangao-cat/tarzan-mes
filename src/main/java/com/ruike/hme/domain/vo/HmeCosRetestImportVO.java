package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * COS复测导入
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/21 11:04
 */
@Data
public class HmeCosRetestImportVO implements Serializable {

    private static final long serialVersionUID = 1482514684431714583L;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "导入类型")
    private String importType;

//    @ApiModelProperty(value = "目标条码")
//    private String targetBarcode;

    @ApiModelProperty(value = "来料条码")
    private String sourceBarcode;

    @ApiModelProperty(value = "盒号")
    private String materialLotCode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "热沉号")
    private String hotSinkCode;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "容器类型")
    private String containerTypeCode;

    @ApiModelProperty(value = "LOTNO")
    private String lotNo;

    @ApiModelProperty(value = "Avg(nm)")
    private BigDecimal averageWavelength;

    @ApiModelProperty(value = "TYPE")
    private String type;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "录入批次")
    private String jobBatch;

    @ApiModelProperty(value = "位置")
    private String position;

    @ApiModelProperty(value = "位置-行")
    private Long loadRow;

    @ApiModelProperty(value = "位置-列")
    private Long loadColumn;

//    @ApiModelProperty(value = "COS数量")
//    private Long cosNum;

    @ApiModelProperty(value = "电流")
    private BigDecimal current;

    @ApiModelProperty(value = "功率等级")
    private String powerLevel;

    @ApiModelProperty(value = "功率")
    private BigDecimal power;

    @ApiModelProperty(value = "波长等级")
    private String wavelengthLevel;

    @ApiModelProperty(value = "波长")
    private BigDecimal wavelength;

    @ApiModelProperty(value = "波长")
    private BigDecimal wavelengthDiffer;

    @ApiModelProperty(value = "电压")
    private BigDecimal voltage;

    @ApiModelProperty(value = "光谱宽度(单点)")
    private BigDecimal spectralWidth;

    @ApiModelProperty(value = "设备")
    private String equipment;

    @ApiModelProperty(value = "测试模式")
    private String cosModel;

    @ApiModelProperty(value = "阈值电流")
    private BigDecimal threscholdCurrent;

    @ApiModelProperty(value = "阈值电压")
    private BigDecimal threscholdVoltage;

    @ApiModelProperty(value = "SE")
    private String se;

//    @ApiModelProperty(value = "线宽")
//    private BigDecimal lineWidth;

    @ApiModelProperty(value = "光电能转换效率")
    private BigDecimal cosIpce;

    @ApiModelProperty(value = "偏振度")
    private BigDecimal polarization;

    @ApiModelProperty(value = "X半宽高")
    private BigDecimal fwhmX;

    @ApiModelProperty(value = "Y半宽高")
    private BigDecimal fwhmY;

    @ApiModelProperty(value = "X86能量宽")
    private BigDecimal cos86x;

    @ApiModelProperty(value = "Y86能量宽")
    private BigDecimal cos86y;

    @ApiModelProperty(value = "X95能量宽")
    private BigDecimal cos95x;

    @ApiModelProperty(value = "Y95能量宽")
    private BigDecimal cos95y;

    @ApiModelProperty(value = "透镜功率")
    private BigDecimal lensPower;

    @ApiModelProperty(value = "PBS功率")
    private BigDecimal cosPbsPower;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "操作者")
    private String operator;

    @ApiModelProperty(value = "测试备注")
    private String testRemark;

    @ApiModelProperty(value = "工位")
    private String workcellCode;

//    @ApiModelProperty(value = "金线供应商批次")
//    private String goldSupplierLot;

    @ApiModelProperty(value = "热沉供应商批次")
    private String hotSinkSupplierLot;

    @ApiModelProperty(value = "热沉类型")
    private String hotSinkType;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "性能信息")
    private List<HmeCosRetestImportVO> propertyList;

    @ApiModelProperty("测试时间")
    private String testDate;

    @ApiModelProperty("电压等级")
    private String voltageLevel;
}
