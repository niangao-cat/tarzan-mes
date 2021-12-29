package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/25 10:22
 */
@Data
public class HmeCosRetestImportVO3 implements Serializable {

    private static final long serialVersionUID = -1398464188481772455L;

    @ApiModelProperty(value = "导入主键")
    private String retestImportDataId;
    @ApiModelProperty(value = "工单号")
    private String workNum;
    @ApiModelProperty(value = "目标条码")
    private String targetBarcode;
    @ApiModelProperty(value = "目标条码ID")
    private String targetBarcodeId;
    @ApiModelProperty(value = "是否打印")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "printFlagMeaning")
    private String printFlag;
    @ApiModelProperty(value = "是否打印含义")
    private String printFlagMeaning;
    @ApiModelProperty(value = "导入类型")
    @LovValue(lovCode = "HME_IMPORT_TYPE", meaningField = "importTypeMeaning")
    private String importType;
    @ApiModelProperty(value = "导入类型含义")
    private String importTypeMeaning;
    @ApiModelProperty(value = "来料条码")
    private String sourceBarcode;
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
    @ApiModelProperty(value = "打印时间")
    private Date creationDate;
    @ApiModelProperty(value = "COS类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;
    @ApiModelProperty(value = "COS类型含义")
    private String cosTypeMeaning;
    @ApiModelProperty(value = "导入批次")
    private String importLot;
    @ApiModelProperty(value = "盒号")
    private String foxNum;
    @ApiModelProperty(value = "工位")
    private String workcell;
    @ApiModelProperty(value = "操作者")
    private String operator;
    @ApiModelProperty(value = "操作者")
    private String operatorName;
    @ApiModelProperty(value = "导入人")
    private String createBy;
    @ApiModelProperty(value = "导入人名称")
    private String createByName;
}
