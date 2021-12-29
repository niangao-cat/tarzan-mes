package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruike.hme.domain.entity.HmeCosFunction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * COS筛选滞留表 返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/02/24 15:19
 */
@Data
@ExcelSheet(zh = "COS筛选滞留表数据")
public class HmeCosSelectionRetentionVO implements Serializable {

    private static final long serialVersionUID = -7132259403711534867L;

    @ApiModelProperty(value = "仓库")
    @ExcelColumn(zh = "仓库",order = 1)
    private String parentLocatorCode;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位",order = 2)
    private String locatorCode;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号",order = 3)
    private String workOrderNum;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 4)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 5)
    private String materialName;

    @ApiModelProperty(value = "COS类型")
    @ExcelColumn(zh = "COS类型",order = 6)
    private String cosType;

    @ApiModelProperty(value = "条码")
    @ExcelColumn(zh = "条码",order = 7)
    private String materialLotCode;

    @ApiModelProperty(value = "位置行")
    private Long loadRow;

    @ApiModelProperty(value = "位置列")
    private Long loadColumn;

    @ApiModelProperty(value = "位置")
    @ExcelColumn(zh = "位置",order = 8)
    private String position;

    @ApiModelProperty(value = "行序号")
    private String loadSequence;

    @ApiModelProperty(value = "筛选状态")
    @LovValue(value = "HME.SELECT_STATUS", meaningField = "selectionStatusMeaning")
    private String selectionStatus;

    @ApiModelProperty(value = "筛选状态含义")
    @ExcelColumn(zh = "筛选状态",order = 9)
    private String selectionStatusMeaning;

    @ApiModelProperty(value = "热沉编码")
    @ExcelColumn(zh = "热沉编码",order = 10)
    private String hotSinkCode;

    @ApiModelProperty(value = "挑选来源条码")
    @ExcelColumn(zh = "挑选来源条码",order = 11)
    private String selectedMaterialLotCode;

    @ApiModelProperty(value = "旧盒子位置")
    private String oldLoad;

    @ApiModelProperty(value = "挑选来源位置")
    @ExcelColumn(zh = "挑选来源位置",order = 12)
    private String selectedPosition;

    @ApiModelProperty(value = "虚拟号")
    @ExcelColumn(zh = "虚拟号",order = 13)
    private String virtualNum;

    @ApiModelProperty(value = "器件序列号")
    @ExcelColumn(zh = "器件序列号",order = 14)
    private String identification;

    @ApiModelProperty(value = "器件物料")
    @ExcelColumn(zh = "器件物料",order = 15)
    private String deviceMaterialCode;

    @ApiModelProperty(value = "路数")
    @ExcelColumn(zh = "路数",order = 16)
    private String attribute2;

    @ApiModelProperty(value = "WAFER")
    @ExcelColumn(zh = "WAFER",order = 17)
    private String wafer;

    @ApiModelProperty(value = "热沉条码")
    @ExcelColumn(zh = "热沉条码",order = 18)
    private String hotSinkMaterialLotCode;

    @ApiModelProperty(value = "热沉供应商批次号")
    @ExcelColumn(zh = "热沉供应商批次号",order = 19)
    private String attribute6;

    @ApiModelProperty(value = "热沉焊料金锡比")
    @ExcelColumn(zh = "热沉焊料金锡比",order = 20)
    private String attribute13;

    @ApiModelProperty(value = "操作人")
    @ExcelColumn(zh = "操作人",order = 21)
    private String realName;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "操作时间",order = 22)
    private Date lastUpdateDate;

    @ApiModelProperty(value = "5A波长")
    @ExcelColumn(zh = "5A波长",order = 23)
    private BigDecimal a04;

    @ApiModelProperty(value = "平均波长")
    @ExcelColumn(zh = "平均波长",order = 24)
    private BigDecimal avg;

    @ApiModelProperty(value = "功率等级")
    @ExcelColumn(zh = "功率等级",order = 25)
    private String a01;

    @ApiModelProperty(value = "波长等级")
    @ExcelColumn(zh = "波长等级",order = 26)
    private String a03;

    @ApiModelProperty(value = "A功率")
    @ExcelColumn(zh = "A功率",order = 27)
    private List<HmeCosFunction> listA02;

    @ApiModelProperty(value = "A电压")
    @ExcelColumn(zh = "A电压",order = 28)
    private List<HmeCosFunction> listA06;

    @ApiModelProperty(value = "A波长")
    @ExcelColumn(zh = "A波长",order = 29)
    private List<HmeCosFunction> listA04;

    @ApiModelProperty(value = "A偏振度")
    @ExcelColumn(zh = "A偏振度",order = 30)
    private List<HmeCosFunction> listA15;
}
