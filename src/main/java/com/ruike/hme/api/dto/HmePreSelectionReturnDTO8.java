package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName HmePreSelectionReturnDTO8
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/15 14:48
 * @Version 1.0
 **/
@Data
@ExcelSheet(zh = "挑选信息导出")
public class HmePreSelectionReturnDTO8 implements Serializable {

    private static final long serialVersionUID = -1954755185522284112L;

    @ApiModelProperty(value = "器件序列号")
    @ExcelColumn(zh = "器件序列号",order = 1)
    private String newMaterialLotCode;

    @ApiModelProperty(value = "虚拟器件号")
    @ExcelColumn(zh = "虚拟器件号",order = 2)
    private String virtualNum;

    @ApiModelProperty(value = "来源条码号")
    @ExcelColumn(zh = "来源条码号",order = 3)
    private String oldMaterialLotCode;

    @ApiModelProperty(value = "位置编码")
    private String oldLoad;

    @ApiModelProperty(value = "位置编码")
    @ExcelColumn(zh = "位置编码",order = 4)
    private String load;

    @ApiModelProperty(value = "热沉")
    @ExcelColumn(zh = "热沉",order = 5)
    private String hotSinkCode;

    @ApiModelProperty(value = "芯片序列")
    @ExcelColumn(zh = "芯片序列",order = 6)
    private String ways;

    @ApiModelProperty(value = "5A波长/nm")
    @ExcelColumn(zh = "5A波长",order = 7)
    private BigDecimal a04;

    @ApiModelProperty(value = "15A功率")
    @ExcelColumn(zh = "15A功率",order = 8)
    private BigDecimal a02;

    @ApiModelProperty(value = "5A波长平均值")
    @ExcelColumn(zh = "波长平均值",order =9)
    private BigDecimal avga04;

    @ApiModelProperty(value = "15A功率和")
    @ExcelColumn(zh = "功率和",order = 10)
    private BigDecimal suna02;

    @ApiModelProperty(value = "电压/V")
    @ExcelColumn(zh = "电压",order = 11)
    private BigDecimal a06;

    @ApiModelProperty(value = "条数")
    @JsonIgnore
    private BigDecimal divisorNum;

    @ApiModelProperty(value = "芯片物料")
    @ExcelColumn(zh = "芯片物料",order = 12)
    private String cosMaterialCode;

    @ApiModelProperty(value = "wafer")
    @ExcelColumn(zh = "wafer",order = 13)
    private String wafer;

    @ApiModelProperty(value = "操作者")
    private Long createdBy;

    @ApiModelProperty(value = "操作者")
    @ExcelColumn(zh = "操作者",order = 14)
    private String userName;

    @ApiModelProperty(value = "操作时间")
    @ExcelColumn(zh = "操作时间",order = 15)
    private String creationDate;

    @ApiModelProperty(value = "不良类型")
    @ExcelColumn(zh = "不良类型",order = 16)
    private String ncType;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注",order = 17)
    private String remark;

    @ApiModelProperty(value = "工序状态")
    @ExcelColumn(zh = "工序状态",order = 18)
    private String status;

    @ApiModelProperty(value = "工位机台号")
    @ExcelColumn(zh = "工位机台号",order = 19)
    private String equipment;

    @ApiModelProperty(value = "产品编码")
    @ExcelColumn(zh = "产品编码",order = 20)
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    @ExcelColumn(zh = "产品描述",order = 21)
    private String materialName;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号", order = 22)
    private String workOrderNum;

    @ApiModelProperty(value = "产品总数")
    @ExcelColumn(zh = "产品总数", order = 23)
    private String qty;

    @ApiModelProperty(value = "cosType")
    @ExcelColumn(zh = "芯片类型", order = 24)
    private String cosType;

    @ApiModelProperty(value = "装配时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date assemblyTime;

    @ApiModelProperty(value = "装配时间字符串")
    @ExcelColumn(zh = "装配时间", order = 25)
    private String assemblyTimeStr;

    @ApiModelProperty(value = "测试ID")
    private String cosFunctionId;
}
