package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 16:58
 */
@Data
public class WmsReceiptDetailVO implements Serializable {

    private static final long serialVersionUID = 4317715817441484179L;

    @ApiModelProperty(value = "行号")
    private String lineNum;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "条码状态")
    @LovValue(lovCode = "WMS.MTLOT.STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;

    @ApiModelProperty(value = "条码状态含义")
    private String materialLotStatusMeaning;

    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位")
    private String uom;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "生产订单号")
    private String woNum;

    @ApiModelProperty(value = "仓库")
    private String warehouseCode;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "入库时间")
    private Date receiptDate;

    @ApiModelProperty(value = "执行人")
    private String executedBy;

    @ApiModelProperty(value = "执行人名称")
    private String executedByName;

    @ApiModelProperty(value = "标识 0-条码占用 1-执行实绩")
    private String flag;

    @ApiModelProperty(value = "行id")
    private String instructionId;
}
