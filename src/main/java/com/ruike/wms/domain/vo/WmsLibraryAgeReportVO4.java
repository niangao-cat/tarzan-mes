package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * WmsLibraryAgeReportVO4
 *
 * @author: chaonan.hu@hand-china.com 2020/11/19 09:14:45
 **/
@Data
@ExcelSheet(zh = "库龄")
public class WmsLibraryAgeReportVO4 implements Serializable {
    private static final long serialVersionUID = -1081465281147237742L;

    @ApiModelProperty(value = "实物条码Id")
    private String materialLotId;

    @ApiModelProperty(value = "实物条码")
    @ExcelColumn(zh = "实物条码",order = 1)
    private String materialLotCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 2)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 4)
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本",order = 3)
    private String materialVersion;

    @ApiModelProperty(value = "批次")
    @ExcelColumn(zh = "批次",order = 5)
    private String lot;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量",order = 6)
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "单位ID")
    private String primaryUomId;

    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位",order = 7)
    private String uomCode;

    @ApiModelProperty(value = "生产日期")
    @ExcelColumn(zh = "生产日期",order = 8)
    private String productDate;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "创建时间")
    @ExcelColumn(zh = "创建时间",order = 9)
    private String creationDateStr;

    @ApiModelProperty(value = "入库时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inLocatorTime;

    @ApiModelProperty(value = "入库时间")
    @ExcelColumn(zh = "入库时间",order = 10)
    private String inLocatorTimeStr;

    @ApiModelProperty(value = "接收时间")
    @ExcelColumn(zh = "接收时间",order = 11)
    private String receiptDate;

    @ApiModelProperty(value = "保质期")
    private BigDecimal shelfLife;

    @ApiModelProperty(value = "条码状态")
    @LovValue(value = "Z.MTLOT.STATUS.G", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "条码状态含义")
    @ExcelColumn(zh = "条码状态",order = 15)
    private String statusMeaning;

    @ApiModelProperty(value = "质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    @ExcelColumn(zh = "质量状态",order = 16)
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位",order = 18)
    private String locatorCode;

    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;

    @ApiModelProperty(value = "仓库")
    @ExcelColumn(zh = "仓库",order = 17)
    private String parentLocatorCode;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器条码")
    @ExcelColumn(zh = "容器条码",order = 19)
    private String containerCode;

    @ApiModelProperty(value = "库龄")
    @ExcelColumn(zh = "库龄",order = 12)
    private BigDecimal libraryAge;

    @ApiModelProperty(value = "超期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beyondDate;

    @ApiModelProperty(value = "超期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelColumn(zh = "超期日期",order = 13)
    private String beyondDateStr;

    @ApiModelProperty(value = "超期天数")
    @ExcelColumn(zh = "超期天数",order = 14)
    private BigDecimal beyondDay;
}
