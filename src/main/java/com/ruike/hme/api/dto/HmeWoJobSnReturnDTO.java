package com.ruike.hme.api.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeWoJobSnReturnDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/12 15:49
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnReturnDTO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty(value = "来料记录Id")
    @ExcelIgnore
    private String operationRecordId;
    @ApiModelProperty(value = "工单ID")
    @ExcelIgnore
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    @ExcelProperty(value = "工单号", index = 0)
    @ColumnWidth(16)
    private String workOrderNum;
    @ApiModelProperty(value = "物料编码")
    @ExcelProperty(value = "COS编码", index = 1)
    @ColumnWidth(16)
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    @ExcelProperty(value = "COS描述", index = 2)
    @ColumnWidth(20)
    private String materialName;
    @ApiModelProperty(value = "容器类型")
    @ExcelIgnore
    private String containerTypeCode;
    @ApiModelProperty(value = "COS类型")
    @ExcelProperty(value = "COS类型", index = 5)
    @ColumnWidth(13)
    private String cosType;
    @ApiModelProperty(value = "平均波长 Avg λ（nm）")
    @ExcelProperty(value = "平均波长Avg λ(nm)", index = 9)
    @ColumnWidth(20)
    private String averageWavelength;
    @ApiModelProperty(value = "TYPE")
    @ExcelIgnore
    private String type;
    @ApiModelProperty(value = "LOTNO")
    @ExcelProperty(value = "LOTNO", index = 10)
    @ColumnWidth(13)
    private String lotNo;
    @ApiModelProperty(value = "Wafer")
    @ExcelProperty(value = "Wafer号", index = 6)
    @ColumnWidth(13)
    private String wafer;
    @ApiModelProperty(value = "SAP批次")
    @ExcelProperty(value = "SAP批次", index = 8)
    @ColumnWidth(13)
    private String sapLot;
    @ApiModelProperty(value = "作业批次")
    @ExcelProperty(value = "录入批次", index = 12)
    @ColumnWidth(16)
    private String jobBatch;
    @ApiModelProperty(value = "BAR条数")
    @ExcelProperty(value = "BAR条数", index = 11)
    @ColumnWidth(16)
    private String barNum;
    @ApiModelProperty(value = "芯片数")
    @ExcelProperty(value = "工单芯片数", index = 3)
    @ColumnWidth(13)
    private String cosNum;
    @ApiModelProperty(value = "备注")
    @ExcelIgnore
    private String remark;
    @ApiModelProperty(value = "列")
    @ExcelIgnore
    private String locationColumn;
    @ApiModelProperty(value = "行")
    @ExcelIgnore
    private String locationRow;
    @ApiModelProperty(value = "每个位置芯片数")
    @ExcelIgnore
    private String capacity;
    @ApiModelProperty(value = "剩余芯片数量")
    @ExcelProperty(value = "剩余芯片数", index = 4)
    @ColumnWidth(13)
    private String surplusCosNum;
    @ApiModelProperty(value = "创建人")
    @ExcelIgnore
    private String createdBy;
    @ApiModelProperty(value = "创建姓名")
    @ExcelProperty(value = "创建人", index = 13)
    @ColumnWidth(13)
    private String realName;
    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间", index = 14)
    @ColumnWidth(24)
    private String creationDate;
    @ApiModelProperty(value = "芯片总数")
    @ExcelProperty(value = "芯片总数", index = 7)
    @ColumnWidth(16)
    private BigDecimal totalCosNum;
}
