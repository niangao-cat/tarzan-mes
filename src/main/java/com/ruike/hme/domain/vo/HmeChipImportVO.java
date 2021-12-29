package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeChipImportVO
 *
 * @author: chaonan.hu@hand-china.com
 **/
@Data
public class HmeChipImportVO implements Serializable {
    private static final long serialVersionUID = 8288519461180530979L;

    @ApiModelProperty("是否打印")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "printFlagMeaning")
    private String printFlag;

    @ApiModelProperty("是否打印含义")
    private String printFlagMeaning;

    @ApiModelProperty("目标条码")
    private String targetBarcode;

    @ApiModelProperty("来料条码")
    private String sourceBarcode;

    @ApiModelProperty("工单")
    private String workNum;

    @ApiModelProperty("cos类型")
    private String cosType;

    @ApiModelProperty("工位")
    private String workcell;

    @ApiModelProperty("导入批次")
    private String importLot;

    @ApiModelProperty("盒号")
    private String foxNum;

    @ApiModelProperty("WAFER")
    private String wafer;

    @ApiModelProperty("容器类型")
    private String containerType;

    @ApiModelProperty("LOTNO")
    private String lotno;

    @ApiModelProperty("Avg(nm)")
    private String avgWavelenght;

    @ApiModelProperty("TYPE")
    private String type;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("目标条码Id")
    private String targetBarcodeId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("生产时间")
    private String productDate;

    @ApiModelProperty("数量")
    private BigDecimal primaryUomQty;
}
