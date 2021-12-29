package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/14 15:42
 */
@Data
@ExcelSheet(zh = "库存日记账")
public class WmsInvJournalExportVO {

    @ApiModelProperty("日记账ID")
    private String journalId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 1)
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 2)
    private String materialDesc;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty(value = "库位编码")
    @ExcelColumn(zh = "库位编码",order = 8)
    private String locatorCode;
    @ApiModelProperty(value = "库位描述")
    @ExcelColumn(zh = "库位描述",order = 9)
    private String locatorDesc;
    @ApiModelProperty(value = "库存现有量值")
    @ExcelColumn(zh = "库存变化后数量",order = 5)
    private Double onhandQuantity;
    @ApiModelProperty(value = "批次CODE")
    @ExcelColumn(zh = "批次号",order = 10)
    private String lotCode;
    @ApiModelProperty(value = "库存变化数量")
    @ExcelColumn(zh = "库存变化数量",order = 4)
    private Double changeQuantity;
    @ApiModelProperty(value = "所有者类型")
    private String ownerType;
    @ApiModelProperty(value = "所有者ID（客户ID或供应商ID）")
    private String ownerId;
    @ApiModelProperty(value = "所有者编码")
    @ExcelColumn(zh = "所有者编码",order = 12)
    private String ownerCode;
    @ApiModelProperty(value = "所有者类型描述")
    @ExcelColumn(zh = "所有者类型",order = 11)
    private String ownerTypeDesc;
    @ApiModelProperty(value = "所有者描述")
    @ExcelColumn(zh = "所有者描述",order = 13)
    private String ownerDesc;
    @ApiModelProperty(value = "事件ID")
    @ExcelColumn(zh = "事件主键",order = 16)
    private String eventId;
    @ApiModelProperty(value = "事件类型")
    @ExcelColumn(zh = "事件类型描述",order = 14)
    private String eventType;
    @ApiModelProperty(value = "事件类型描述")
    @ExcelColumn(zh = "事件类型描述",order = 15)
    private String eventTypeDesc;
    @ApiModelProperty(value = "事件时间")
    private Date eventTime;
    @ApiModelProperty(value = "事件时间字符串")
    @ExcelColumn(zh = "库存变化时间",order = 3)
    private String eventTimeStr;
    @ApiModelProperty(value = "创建人")
    private Long eventBy;
    @ApiModelProperty(value = "创建人名称")
    @ExcelColumn(zh = "操作人",order = 17)
    private String eventByUserName;
    @ApiModelProperty(value = "仓库编码")
    @ExcelColumn(zh = "仓库编码",order = 6)
    private String warehouseCode;
    @ApiModelProperty(value = "仓库描述")
    @ExcelColumn(zh = "仓库描述",order = 7)
    private String warehouseDesc;
    @ApiModelProperty(value = "仓库id")
    private String warehouseId;
}
